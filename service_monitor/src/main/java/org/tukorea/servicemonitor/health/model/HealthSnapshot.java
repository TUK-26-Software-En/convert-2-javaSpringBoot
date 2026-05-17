package org.tukorea.servicemonitor.health.model;

import java.time.LocalDateTime;

public record HealthSnapshot(
        ServiceTarget target,
        ProbeStatus status,
        String detail,
        LocalDateTime checkedAt
) {
}
