package org.tukorea.servicemonitor.dockercontrol.business.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.tukorea.servicemonitor.dockercontrol.dataaccess.adapter.DockerEngineAdapter;
import org.tukorea.servicemonitor.dockercontrol.model.DockerContainerDetails;
import org.tukorea.servicemonitor.dockercontrol.model.DockerControlCommand;
import org.tukorea.servicemonitor.dockercontrol.model.DockerControlResult;
import org.tukorea.servicemonitor.global.config.DockerControlProperties;
import org.tukorea.servicemonitor.global.config.FailureInjectionProperties;
import org.tukorea.servicemonitor.health.model.ServiceTarget;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class DockerControlService {

    private static final Logger log = LoggerFactory.getLogger(DockerControlService.class);
    private static final List<DockerControlCommand> FAILURE_INJECTION_COMMANDS = List.of(
            DockerControlCommand.PAUSE,
            DockerControlCommand.UNPAUSE,
            DockerControlCommand.START,
            DockerControlCommand.STOP,
            DockerControlCommand.RESTART
    );

    private final DockerControlProperties dockerControlProperties;
    private final FailureInjectionProperties failureInjectionProperties;
    private final DockerEngineAdapter dockerEngineAdapter;

    public DockerControlService(
            DockerControlProperties dockerControlProperties,
            FailureInjectionProperties failureInjectionProperties,
            DockerEngineAdapter dockerEngineAdapter
    ) {
        this.dockerControlProperties = dockerControlProperties;
        this.failureInjectionProperties = failureInjectionProperties;
        this.dockerEngineAdapter = dockerEngineAdapter;
    }

    public DockerContainerDetails loadContainerDetails(ServiceTarget target) {
        if (!dockerControlProperties.isEnabled()) {
            return DockerContainerDetails.unavailable(target, "Docker control is disabled for this runtime profile.");
        }
        if (!isTargetAllowed(target)) {
            return DockerContainerDetails.unavailable(target, "Target is not included in the Docker control allowlist.");
        }

        try {
            return dockerEngineAdapter.loadContainerDetails(target);
        } catch (Exception exception) {
            return DockerContainerDetails.unavailable(target, abbreviate(exception.getMessage()));
        }
    }

    public DockerControlResult executeFailureInjection(ServiceTarget target, DockerControlCommand command, String reason) {
        LocalDateTime executedAt = LocalDateTime.now();

        try {
            validateFailureInjectionEnabled(target, command);
            dockerEngineAdapter.executeFailureInjection(target, command);
            DockerContainerDetails containerDetails = loadContainerDetails(target);
            String message = buildSuccessMessage(target, command, containerDetails, reason);
            log.info("Failure injection success target={} command={} reason={} message={}", target, command, sanitizeReason(reason), message);
            return new DockerControlResult(target, command, true, message, executedAt);
        } catch (Exception exception) {
            String message = abbreviate(exception.getMessage());
            log.warn("Failure injection failed target={} command={} reason={} message={}", target, command, sanitizeReason(reason), message);
            return new DockerControlResult(target, command, false, message, executedAt);
        }
    }

    public boolean isDockerControlEnabled() {
        return dockerControlProperties.isEnabled();
    }

    public boolean isFailureInjectionEnabled() {
        return failureInjectionProperties.isEnabled();
    }

    public String socketPath() {
        return dockerControlProperties.getSocketPath();
    }

    public List<String> allowedTargets() {
        return List.copyOf(dockerControlProperties.getAllowedTargets());
    }

    public List<String> allowedCommands() {
        return dockerControlProperties.getAllowedCommands().stream()
                .map(command -> command.toLowerCase(Locale.ROOT))
                .toList();
    }

    public List<String> failureInjectionCommands() {
        return FAILURE_INJECTION_COMMANDS.stream()
                .filter(this::isCommandAllowed)
                .map(DockerControlCommand::commandText)
                .toList();
    }

    private void validateFailureInjectionEnabled(ServiceTarget target, DockerControlCommand command) {
        if (!dockerControlProperties.isEnabled()) {
            throw new IllegalStateException("Docker control is disabled in this profile.");
        }
        if (!failureInjectionProperties.isEnabled()) {
            throw new IllegalStateException("Failure injection is disabled in this profile.");
        }
        if (!isTargetAllowed(target)) {
            throw new IllegalStateException("Target " + target.getComposeServiceName() + " is not allowlisted.");
        }
        if (!isCommandAllowed(command)) {
            throw new IllegalStateException("Command " + command.commandText() + " is not allowlisted.");
        }
        if (!Set.copyOf(FAILURE_INJECTION_COMMANDS).contains(command)) {
            throw new IllegalStateException("Command " + command.commandText() + " is not a failure injection action.");
        }
    }

    private boolean isTargetAllowed(ServiceTarget target) {
        return dockerControlProperties.getAllowedTargets().stream()
                .anyMatch(allowedTarget -> allowedTarget.equalsIgnoreCase(target.getComposeServiceName()));
    }

    private boolean isCommandAllowed(DockerControlCommand command) {
        return dockerControlProperties.getAllowedCommands().stream()
                .anyMatch(allowedCommand -> allowedCommand.equalsIgnoreCase(command.commandText()));
    }

    private String buildSuccessMessage(
            ServiceTarget target,
            DockerControlCommand command,
            DockerContainerDetails containerDetails,
            String reason
    ) {
        String baseMessage = command.commandText() + " completed for " + target.getComposeServiceName()
                + " (state=" + containerDetails.state() + ", health=" + containerDetails.healthStatus() + ")";
        if (reason == null || reason.isBlank()) {
            return baseMessage;
        }
        return baseMessage + " | reason=" + reason.trim();
    }

    private String sanitizeReason(String reason) {
        return reason == null || reason.isBlank() ? "-" : abbreviate(reason);
    }

    private String abbreviate(String detail) {
        if (detail == null || detail.isBlank()) {
            return "No detail";
        }
        return detail.length() <= 220 ? detail : detail.substring(0, 217) + "...";
    }
}
