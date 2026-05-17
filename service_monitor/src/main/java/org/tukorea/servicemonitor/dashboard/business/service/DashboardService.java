package org.tukorea.servicemonitor.dashboard.business.service;

import org.springframework.stereotype.Service;
import org.tukorea.servicemonitor.dashboard.model.DashboardView;
import org.tukorea.servicemonitor.dashboard.model.TargetDashboardDetail;
import org.tukorea.servicemonitor.dockercontrol.business.service.DockerControlService;
import org.tukorea.servicemonitor.global.config.MonitoringTargetsProperties;
import org.tukorea.servicemonitor.health.business.service.HealthCollectionService;
import org.tukorea.servicemonitor.health.model.HealthEventSummary;
import org.tukorea.servicemonitor.health.model.HealthSnapshot;
import org.tukorea.servicemonitor.health.model.ProbeStatus;
import org.tukorea.servicemonitor.health.model.ServiceTarget;
import org.tukorea.servicemonitor.incidents.business.service.IncidentQueryService;
import org.tukorea.servicemonitor.incidents.model.IncidentSummary;
import org.tukorea.servicemonitor.metrics.business.service.ReliabilityMetricsService;
import org.tukorea.servicemonitor.metrics.model.TargetReliabilityMetrics;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final HealthCollectionService healthCollectionService;
    private final IncidentQueryService incidentQueryService;
    private final ReliabilityMetricsService reliabilityMetricsService;
    private final MonitoringTargetsProperties monitoringTargetsProperties;
    private final DockerControlService dockerControlService;

    public DashboardService(
            HealthCollectionService healthCollectionService,
            IncidentQueryService incidentQueryService,
            ReliabilityMetricsService reliabilityMetricsService,
            MonitoringTargetsProperties monitoringTargetsProperties,
            DockerControlService dockerControlService
    ) {
        this.healthCollectionService = healthCollectionService;
        this.incidentQueryService = incidentQueryService;
        this.reliabilityMetricsService = reliabilityMetricsService;
        this.monitoringTargetsProperties = monitoringTargetsProperties;
        this.dockerControlService = dockerControlService;
    }

    public DashboardView loadDashboard() {
        List<HealthSnapshot> currentStatuses = healthCollectionService.collectAndStoreSnapshot();
        List<IncidentSummary> activeIncidents = incidentQueryService.findActiveIncidents();
        List<IncidentSummary> recentIncidents = incidentQueryService.findRecentIncidents();
        List<TargetReliabilityMetrics> reliabilityMetrics = reliabilityMetricsService.loadMetrics();
        List<HealthEventSummary> recentEvents = healthCollectionService.findRecentEvents();
        Map<ServiceTarget, HealthSnapshot> snapshotsByTarget = currentStatuses.stream()
                .collect(Collectors.toMap(HealthSnapshot::target, Function.identity()));
        Map<ServiceTarget, TargetReliabilityMetrics> metricsByTarget = reliabilityMetrics.stream()
                .collect(Collectors.toMap(TargetReliabilityMetrics::target, Function.identity()));
        long upServiceCount = currentStatuses.stream()
                .filter(snapshot -> snapshot.status() == ProbeStatus.UP)
                .count();
        long downServiceCount = currentStatuses.size() - upServiceCount;
        double averageAvailability = reliabilityMetrics.stream()
                .mapToDouble(TargetReliabilityMetrics::availabilityRate)
                .average()
                .orElse(0.0);
        TargetDashboardDetail libraryServiceDetail = buildTargetDetail(
                ServiceTarget.LIBRARY_SERVICE,
                monitoringTargetsProperties.getLibraryServiceHealthUrl(),
                snapshotsByTarget,
                metricsByTarget
        );
        TargetDashboardDetail postgresDetail = buildTargetDetail(
                ServiceTarget.POSTGRES,
                monitoringTargetsProperties.getPostgresJdbcUrl(),
                snapshotsByTarget,
                metricsByTarget
        );

        return new DashboardView(
                currentStatuses,
                activeIncidents,
                recentIncidents,
                reliabilityMetrics,
                recentEvents,
                upServiceCount,
                downServiceCount,
                averageAvailability,
                libraryServiceDetail,
                postgresDetail,
                dockerControlService.isDockerControlEnabled(),
                dockerControlService.isFailureInjectionEnabled(),
                dockerControlService.socketPath(),
                dockerControlService.allowedTargets(),
                dockerControlService.allowedCommands(),
                dockerControlService.failureInjectionCommands()
        );
    }

    private TargetDashboardDetail buildTargetDetail(
            ServiceTarget target,
            String probeTarget,
            Map<ServiceTarget, HealthSnapshot> snapshotsByTarget,
            Map<ServiceTarget, TargetReliabilityMetrics> metricsByTarget
    ) {
        return new TargetDashboardDetail(
                target,
                probeTarget,
                snapshotsByTarget.getOrDefault(target, new HealthSnapshot(target, ProbeStatus.DOWN, "No snapshot collected", LocalDateTime.now())),
                healthCollectionService.findRecentEvents(target),
                incidentQueryService.findRecentIncidents(target),
                metricsByTarget.getOrDefault(target, new TargetReliabilityMetrics(target, 0.0, 0.0, 0.0, 0.0, 0L, 0L)),
                dockerControlService.loadContainerDetails(target)
        );
    }
}
