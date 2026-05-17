package org.tukorea.servicemonitor.incidents.model;

import org.tukorea.servicemonitor.health.model.ServiceTarget;

import java.time.LocalDateTime;

public record IncidentSummary(
        ServiceTarget target,
        IncidentStatus status,
        LocalDateTime startedAt,
        LocalDateTime resolvedAt,
        long durationMinutes,
        String startDetail,
        String resolutionDetail
) {
}
