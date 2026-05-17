package org.tukorea.servicemonitor.dockercontrol.dataaccess.adapter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.tukorea.servicemonitor.dockercontrol.model.DockerContainerDetails;
import org.tukorea.servicemonitor.dockercontrol.model.DockerControlCommand;
import org.tukorea.servicemonitor.global.config.DockerControlProperties;
import org.tukorea.servicemonitor.health.model.ServiceTarget;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DockerEngineAdapter {

    private static final DateTimeFormatter STARTED_AT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final DockerControlProperties dockerControlProperties;
    private final ObjectMapper objectMapper;

    public DockerEngineAdapter(DockerControlProperties dockerControlProperties, ObjectMapper objectMapper) {
        this.dockerControlProperties = dockerControlProperties;
        this.objectMapper = objectMapper;
    }

    public DockerContainerDetails loadContainerDetails(ServiceTarget target) {
        ResolvedContainer container = resolveContainer(target);
        JsonNode inspect = readJsonNode("GET", "/containers/" + container.id() + "/json");
        JsonNode stats = tryReadJsonNode("GET", "/containers/" + container.id() + "/stats?stream=false");
        List<String> recentLogs = tryReadLogs(container.id(), 20);

        return new DockerContainerDetails(
                target,
                true,
                buildRuntimeMessage(stats, recentLogs),
                abbreviateContainerId(container.id()),
                trimLeadingSlash(inspect.path("Name").asText(container.name())),
                inspect.path("Config").path("Image").asText("-"),
                inspect.path("State").path("Status").asText("-"),
                resolveHealthStatus(inspect),
                formatTimestamp(inspect.path("State").path("StartedAt").asText("")),
                inspect.path("RestartCount").asLong(0L),
                formatPortBindings(inspect.path("NetworkSettings").path("Ports")),
                formatCpuUsage(stats),
                formatMemoryUsage(stats, "usage"),
                formatMemoryUsage(stats, "limit"),
                stats == null ? "-" : stats.path("networks").isMissingNode() ? "-" : formatNetworkIo(stats.path("networks")),
                stats == null ? "-" : String.valueOf(stats.path("pids_stats").path("current").asLong(0L)),
                recentLogs
        );
    }

    public void executeFailureInjection(ServiceTarget target, DockerControlCommand command) {
        ResolvedContainer container = resolveContainer(target);
        String actionPath = switch (command) {
            case PAUSE -> "/containers/" + container.id() + "/pause";
            case UNPAUSE -> "/containers/" + container.id() + "/unpause";
            case START -> "/containers/" + container.id() + "/start";
            case STOP -> "/containers/" + container.id() + "/stop";
            case RESTART -> "/containers/" + container.id() + "/restart";
            default -> throw new IllegalArgumentException("Unsupported failure injection command: " + command);
        };

        requestBytes("POST", actionPath);
    }

    private ResolvedContainer resolveContainer(ServiceTarget target) {
        JsonNode containers = readJsonNode("GET", "/containers/json?all=true");

        for (JsonNode container : containers) {
            String composeService = container.path("Labels").path("com.docker.compose.service").asText("");
            if (!target.getComposeServiceName().equalsIgnoreCase(composeService)) {
                continue;
            }

            String containerId = container.path("Id").asText();
            JsonNode names = container.path("Names");
            String containerName = names.isArray() && !names.isEmpty()
                    ? trimLeadingSlash(names.get(0).asText())
                    : target.getComposeServiceName();

            return new ResolvedContainer(containerId, containerName);
        }

        throw new IllegalStateException("Docker container not found for service " + target.getComposeServiceName());
    }

    private JsonNode readJsonNode(String method, String path) {
        try {
            return objectMapper.readTree(requestBytes(method, path));
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to parse Docker response: " + exception.getMessage(), exception);
        }
    }

    private JsonNode tryReadJsonNode(String method, String path) {
        try {
            return readJsonNode(method, path);
        } catch (Exception exception) {
            return null;
        }
    }

    private List<String> tryReadLogs(String containerId, int tailLines) {
        try {
            byte[] payload = requestBytes(
                    "GET",
                    "/containers/" + containerId + "/logs?stdout=true&stderr=true&tail=" + tailLines + "&timestamps=true"
            );
            return decodeLogs(payload);
        } catch (Exception exception) {
            return List.of("<log unavailable: " + abbreviate(exception.getMessage()) + ">"
            );
        }
    }

    private byte[] requestBytes(String method, String path) {
        List<String> command = List.of(
                "curl",
                "--silent",
                "--show-error",
                "--fail-with-body",
                "--unix-socket",
                dockerControlProperties.getSocketPath(),
                "--request",
                method,
                "http://localhost" + path
        );

        try {
            Process process = new ProcessBuilder(command)
                    .redirectErrorStream(true)
                    .start();

            if (!process.waitFor(10, TimeUnit.SECONDS)) {
                process.destroyForcibly();
                throw new IllegalStateException("Docker request timed out for path " + path);
            }

            byte[] output = process.getInputStream().readAllBytes();

            if (process.exitValue() != 0) {
                throw new IllegalStateException(extractDockerMessage(output));
            }

            return output;
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Docker request failed: " + exception.getMessage(), exception);
        } catch (IOException exception) {
            throw new IllegalStateException("Docker request failed: " + exception.getMessage(), exception);
        }
    }

    private List<String> decodeLogs(byte[] payload) {
        String textPayload = new String(payload, StandardCharsets.UTF_8);
        if (textPayload.indexOf('\u0000') < 0 && textPayload.indexOf('\u0001') < 0 && textPayload.indexOf('\u0002') < 0) {
            return splitLines(textPayload);
        }

        try {
            ByteArrayOutputStream plainLogs = new ByteArrayOutputStream();
            int index = 0;
            while (index + 8 <= payload.length) {
                int chunkLength = ByteBuffer.wrap(payload, index + 4, 4).getInt();
                index += 8;
                if (chunkLength < 0 || index + chunkLength > payload.length) {
                    return splitLines(textPayload);
                }
                plainLogs.write(payload, index, chunkLength);
                index += chunkLength;
            }
            return splitLines(plainLogs.toString(StandardCharsets.UTF_8));
        } catch (Exception exception) {
            return splitLines(textPayload);
        }
    }

    private List<String> splitLines(String value) {
        return value.lines()
                .map(String::trim)
                .filter(line -> !line.isBlank())
                .toList();
    }

    private String resolveHealthStatus(JsonNode inspect) {
        JsonNode healthNode = inspect.path("State").path("Health").path("Status");
        return healthNode.isMissingNode() || healthNode.asText().isBlank() ? "n/a" : healthNode.asText();
    }

    private String formatTimestamp(String rawTimestamp) {
        if (rawTimestamp == null || rawTimestamp.isBlank() || rawTimestamp.startsWith("0001-01-01")) {
            return "-";
        }
        try {
            return OffsetDateTime.parse(rawTimestamp).toLocalDateTime().format(STARTED_AT_FORMAT);
        } catch (Exception exception) {
            return rawTimestamp;
        }
    }

    private String formatPortBindings(JsonNode portsNode) {
        if (portsNode == null || !portsNode.isObject() || portsNode.isEmpty()) {
            return "-";
        }

        List<String> bindings = new ArrayList<>();
        Iterator<Map.Entry<String, JsonNode>> fields = portsNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            JsonNode hostBindings = entry.getValue();
            if (hostBindings == null || hostBindings.isNull() || hostBindings.isEmpty()) {
                bindings.add(entry.getKey());
                continue;
            }

            List<String> published = new ArrayList<>();
            for (JsonNode binding : hostBindings) {
                published.add(binding.path("HostIp").asText("0.0.0.0") + ":" + binding.path("HostPort").asText("-"));
            }
            bindings.add(entry.getKey() + " -> " + String.join(", ", published));
        }

        return String.join(" | ", bindings);
    }

    private String formatCpuUsage(JsonNode stats) {
        if (stats == null) {
            return "-";
        }

        JsonNode cpuStats = stats.path("cpu_stats");
        JsonNode preCpuStats = stats.path("precpu_stats");
        double cpuDelta = cpuStats.path("cpu_usage").path("total_usage").asDouble(0.0)
                - preCpuStats.path("cpu_usage").path("total_usage").asDouble(0.0);
        double systemDelta = cpuStats.path("system_cpu_usage").asDouble(0.0)
                - preCpuStats.path("system_cpu_usage").asDouble(0.0);
        int cpuCount = cpuStats.path("online_cpus").asInt(cpuStats.path("cpu_usage").path("percpu_usage").size());
        if (cpuDelta <= 0.0 || systemDelta <= 0.0 || cpuCount <= 0) {
            return "0.00%";
        }
        double usage = cpuDelta / systemDelta * cpuCount * 100.0;
        return String.format("%.2f%%", usage);
    }

    private String formatMemoryUsage(JsonNode stats, String fieldName) {
        if (stats == null) {
            return "-";
        }
        double bytes = stats.path("memory_stats").path(fieldName).asDouble(0.0);
        if (bytes <= 0.0) {
            return "0.00 MiB";
        }
        return String.format("%.2f MiB", bytes / 1024.0 / 1024.0);
    }

    private String formatNetworkIo(JsonNode networksNode) {
        if (networksNode == null || !networksNode.isObject() || networksNode.isEmpty()) {
            return "-";
        }

        long rxBytes = 0L;
        long txBytes = 0L;
        Iterator<Map.Entry<String, JsonNode>> fields = networksNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            rxBytes += entry.getValue().path("rx_bytes").asLong(0L);
            txBytes += entry.getValue().path("tx_bytes").asLong(0L);
        }

        return String.format("RX %.2f KiB / TX %.2f KiB", rxBytes / 1024.0, txBytes / 1024.0);
    }

    private String buildRuntimeMessage(JsonNode stats, List<String> recentLogs) {
        List<String> messages = new ArrayList<>();
        if (stats == null) {
            messages.add("Live stats unavailable");
        }
        if (recentLogs.isEmpty()) {
            messages.add("Recent logs unavailable");
        }
        return messages.isEmpty() ? "Runtime details loaded from Docker API" : String.join(" | ", messages);
    }

    private String extractDockerMessage(byte[] output) {
        String rawMessage = new String(output, StandardCharsets.UTF_8).trim();
        if (rawMessage.isBlank()) {
            return "Docker request failed without output";
        }

        try {
            JsonNode jsonNode = objectMapper.readTree(rawMessage);
            if (jsonNode.hasNonNull("message")) {
                return abbreviate(jsonNode.path("message").asText());
            }
        } catch (Exception ignored) {
        }

        return abbreviate(rawMessage);
    }

    private String abbreviateContainerId(String containerId) {
        return containerId.length() <= 12 ? containerId : containerId.substring(0, 12);
    }

    private String trimLeadingSlash(String containerName) {
        return containerName.startsWith("/") ? containerName.substring(1) : containerName;
    }

    private String abbreviate(String detail) {
        if (detail == null || detail.isBlank()) {
            return "No detail";
        }
        String normalized = Stream.of(detail.split("\\R"))
                .map(String::trim)
                .filter(line -> !line.isBlank())
                .collect(Collectors.joining(" | "));
        if (normalized.length() <= 220) {
            return normalized;
        }
        return normalized.substring(0, 217) + "...";
    }

    private record ResolvedContainer(String id, String name) {
    }
}
