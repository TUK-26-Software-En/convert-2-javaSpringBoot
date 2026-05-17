package org.tukorea.servicemonitor.metrics.model;

import org.tukorea.servicemonitor.health.model.ServiceTarget;

public record TargetReliabilityMetrics(
        ServiceTarget target,
        double mtbfMinutes,
        double mttrMinutes,
        double mttfMinutes,
        double availabilityRate,
        long incidentCount,
        long activeIncidentCount
) {
}
