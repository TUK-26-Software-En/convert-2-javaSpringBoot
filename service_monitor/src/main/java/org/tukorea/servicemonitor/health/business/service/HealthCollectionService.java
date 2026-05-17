package org.tukorea.servicemonitor.health.business.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.tukorea.servicemonitor.global.config.MonitoringTargetsProperties;
import org.tukorea.servicemonitor.health.dataaccess.entity.HealthEventEntity;
import org.tukorea.servicemonitor.health.dataaccess.repository.HealthEventRepository;
import org.tukorea.servicemonitor.health.model.HealthEventSummary;
import org.tukorea.servicemonitor.health.model.HealthSnapshot;
import org.tukorea.servicemonitor.health.model.ProbeStatus;
import org.tukorea.servicemonitor.health.model.ServiceTarget;
import org.tukorea.servicemonitor.incidents.dataaccess.entity.IncidentEntity;
import org.tukorea.servicemonitor.incidents.dataaccess.repository.IncidentRepository;
import org.tukorea.servicemonitor.incidents.model.IncidentStatus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class HealthCollectionService {

    private final RestClient monitorRestClient;
    private final MonitoringTargetsProperties monitoringTargetsProperties;
    private final HealthEventRepository healthEventRepository;
    private final IncidentRepository incidentRepository;

    public HealthCollectionService(
            RestClient monitorRestClient,
            MonitoringTargetsProperties monitoringTargetsProperties,
            HealthEventRepository healthEventRepository,
            IncidentRepository incidentRepository
    ) {
        this.monitorRestClient = monitorRestClient;
        this.monitoringTargetsProperties = monitoringTargetsProperties;
        this.healthEventRepository = healthEventRepository;
        this.incidentRepository = incidentRepository;
    }

    @Transactional
    public synchronized List<HealthSnapshot> collectAndStoreSnapshot() {
        List<HealthSnapshot> snapshots = List.of(probeLibraryService(), probePostgres());
        snapshots.forEach(this::persistTransitionIfChanged);
        return snapshots;
    }

    @Transactional(readOnly = true)
    public List<HealthEventSummary> findRecentEvents() {
        return healthEventRepository.findTop12ByOrderByCheckedAtDesc()
                .stream()
                .map(entity -> new HealthEventSummary(
                        entity.getTarget(),
                        entity.getStatus(),
                        entity.getDetail(),
                        entity.getCheckedAt()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<HealthEventSummary> findRecentEvents(ServiceTarget target) {
        return healthEventRepository.findTop6ByTargetOrderByCheckedAtDesc(target)
                .stream()
                .map(entity -> new HealthEventSummary(
                        entity.getTarget(),
                        entity.getStatus(),
                        entity.getDetail(),
                        entity.getCheckedAt()
                ))
                .toList();
    }

    private HealthSnapshot probeLibraryService() {
        LocalDateTime checkedAt = LocalDateTime.now();
        try {
            Map<String, Object> payload = monitorRestClient.get()
                    .uri(monitoringTargetsProperties.getLibraryServiceHealthUrl())
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

            String statusText = payload == null ? "UNKNOWN" : String.valueOf(payload.get("status"));
            ProbeStatus probeStatus = "UP".equalsIgnoreCase(statusText) ? ProbeStatus.UP : ProbeStatus.DOWN;
            String detail = probeStatus == ProbeStatus.UP
                    ? "Actuator health returned UP"
                    : "Actuator health returned status=" + statusText;
            return new HealthSnapshot(ServiceTarget.LIBRARY_SERVICE, probeStatus, detail, checkedAt);
        } catch (Exception exception) {
            return new HealthSnapshot(
                    ServiceTarget.LIBRARY_SERVICE,
                    ProbeStatus.DOWN,
                    abbreviateDetail(exception.getClass().getSimpleName() + ": " + exception.getMessage()),
                    checkedAt
            );
        }
    }

    private HealthSnapshot probePostgres() {
        LocalDateTime checkedAt = LocalDateTime.now();
        try (
                Connection connection = DriverManager.getConnection(
                        monitoringTargetsProperties.getPostgresJdbcUrl(),
                        monitoringTargetsProperties.getPostgresUsername(),
                        monitoringTargetsProperties.getPostgresPassword()
                );
                PreparedStatement statement = connection.prepareStatement("select 1");
                ResultSet resultSet = statement.executeQuery()
        ) {
            if (resultSet.next()) {
                return new HealthSnapshot(
                        ServiceTarget.POSTGRES,
                        ProbeStatus.UP,
                        "JDBC probe succeeded",
                        checkedAt
                );
            }

            return new HealthSnapshot(
                    ServiceTarget.POSTGRES,
                    ProbeStatus.DOWN,
                    "JDBC probe returned no rows",
                    checkedAt
            );
        } catch (Exception exception) {
            return new HealthSnapshot(
                    ServiceTarget.POSTGRES,
                    ProbeStatus.DOWN,
                    abbreviateDetail(exception.getClass().getSimpleName() + ": " + exception.getMessage()),
                    checkedAt
            );
        }
    }

    private void persistTransitionIfChanged(HealthSnapshot snapshot) {
        ProbeStatus previousStatus = healthEventRepository.findTopByTargetOrderByCheckedAtDesc(snapshot.target())
                .map(HealthEventEntity::getStatus)
                .orElse(null);

        if (previousStatus == snapshot.status()) {
            return;
        }

        healthEventRepository.save(new HealthEventEntity(
                snapshot.target(),
                snapshot.status(),
                snapshot.detail(),
                snapshot.checkedAt()
        ));

        if (snapshot.status() == ProbeStatus.DOWN) {
            incidentRepository.findTopByTargetAndStatusOrderByStartedAtDesc(snapshot.target(), IncidentStatus.ACTIVE)
                    .orElseGet(() -> incidentRepository.save(IncidentEntity.open(
                            snapshot.target(),
                            snapshot.checkedAt(),
                            snapshot.detail()
                    )));
            return;
        }

        if (previousStatus == ProbeStatus.DOWN) {
            incidentRepository.findTopByTargetAndStatusOrderByStartedAtDesc(snapshot.target(), IncidentStatus.ACTIVE)
                    .ifPresent(incident -> incident.resolve(snapshot.checkedAt(), snapshot.detail()));
        }
    }

    private String abbreviateDetail(String detail) {
        if (detail == null || detail.isBlank()) {
            return "No detail";
        }
        if (detail.length() <= 220) {
            return detail;
        }
        return detail.substring(0, 217) + "...";
    }
}
