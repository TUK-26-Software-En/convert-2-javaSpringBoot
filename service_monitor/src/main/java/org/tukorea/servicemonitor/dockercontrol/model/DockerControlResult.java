package org.tukorea.servicemonitor.dockercontrol.model;

import org.tukorea.servicemonitor.health.model.ServiceTarget;

import java.time.LocalDateTime;

public record DockerControlResult(
        ServiceTarget target,
        DockerControlCommand command,
        boolean success,
        String message,
        LocalDateTime executedAt
) {
}
