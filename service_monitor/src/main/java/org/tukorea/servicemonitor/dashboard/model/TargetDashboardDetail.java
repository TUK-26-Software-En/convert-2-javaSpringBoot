package org.tukorea.servicemonitor.dashboard.model;

import org.tukorea.servicemonitor.dockercontrol.model.DockerContainerDetails;
import org.tukorea.servicemonitor.health.model.HealthEventSummary;
import org.tukorea.servicemonitor.health.model.HealthSnapshot;
import org.tukorea.servicemonitor.health.model.ServiceTarget;
import org.tukorea.servicemonitor.incidents.model.IncidentSummary;
import org.tukorea.servicemonitor.metrics.model.TargetReliabilityMetrics;

import java.util.List;

public record TargetDashboardDetail(
        ServiceTarget target,
        String probeTarget,
        HealthSnapshot currentStatus,
        List<HealthEventSummary> recentEvents,
        List<IncidentSummary> recentIncidents,
        TargetReliabilityMetrics reliabilityMetrics,
        DockerContainerDetails containerDetails
) {
}
