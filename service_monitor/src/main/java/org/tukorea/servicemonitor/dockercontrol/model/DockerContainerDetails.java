package org.tukorea.servicemonitor.dockercontrol.model;

import org.tukorea.servicemonitor.health.model.ServiceTarget;

import java.util.List;

public record DockerContainerDetails(
        ServiceTarget target,
        boolean available,
        String message,
        String containerId,
        String containerName,
        String image,
        String state,
        String healthStatus,
        String startedAt,
        long restartCount,
        String ports,
        String cpuUsage,
        String memoryUsage,
        String memoryLimit,
        String networkIo,
        String processCount,
        List<String> recentLogs
) {

    public static DockerContainerDetails unavailable(ServiceTarget target, String message) {
        return new DockerContainerDetails(
                target,
                false,
                message,
                "-",
                "-",
                "-",
                "-",
                "-",
                "-",
                0L,
                "-",
                "-",
                "-",
                "-",
                "-",
                "-",
                List.of()
        );
    }
}
