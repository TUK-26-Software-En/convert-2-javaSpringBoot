package org.tukorea.servicemonitor.dashboard.model;

import org.tukorea.servicemonitor.health.model.HealthEventSummary;
import org.tukorea.servicemonitor.health.model.HealthSnapshot;
import org.tukorea.servicemonitor.incidents.model.IncidentSummary;
import org.tukorea.servicemonitor.metrics.model.TargetReliabilityMetrics;

import java.util.List;

public record DashboardView(
        List<HealthSnapshot> currentStatuses,
        List<IncidentSummary> activeIncidents,
        List<IncidentSummary> recentIncidents,
        List<TargetReliabilityMetrics> reliabilityMetrics,
        List<HealthEventSummary> recentEvents,
        long upServiceCount,
        long downServiceCount,
        double averageAvailability,
        TargetDashboardDetail libraryServiceDetail,
        TargetDashboardDetail postgresDetail,
        boolean dockerControlEnabled,
        boolean failureInjectionEnabled,
        String dockerSocketPath,
        List<String> allowedTargets,
        List<String> allowedCommands,
        List<String> failureInjectionCommands
) {
}
