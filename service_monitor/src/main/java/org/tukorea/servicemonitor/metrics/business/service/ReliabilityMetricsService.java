package org.tukorea.servicemonitor.metrics.business.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tukorea.servicemonitor.health.dataaccess.entity.HealthEventEntity;
import org.tukorea.servicemonitor.health.dataaccess.repository.HealthEventRepository;
import org.tukorea.servicemonitor.health.model.ProbeStatus;
import org.tukorea.servicemonitor.health.model.ServiceTarget;
import org.tukorea.servicemonitor.incidents.dataaccess.entity.IncidentEntity;
import org.tukorea.servicemonitor.incidents.dataaccess.repository.IncidentRepository;
import org.tukorea.servicemonitor.metrics.model.TargetReliabilityMetrics;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class ReliabilityMetricsService {

    private final HealthEventRepository healthEventRepository;
    private final IncidentRepository incidentRepository;

    public ReliabilityMetricsService(
            HealthEventRepository healthEventRepository,
            IncidentRepository incidentRepository
    ) {
        this.healthEventRepository = healthEventRepository;
        this.incidentRepository = incidentRepository;
    }

    @Transactional(readOnly = true)
    public List<TargetReliabilityMetrics> loadMetrics() {
        return Arrays.stream(ServiceTarget.values())
                .map(this::calculateMetrics)
                .toList();
    }

    private TargetReliabilityMetrics calculateMetrics(ServiceTarget target) {
        LocalDateTime referenceTime = LocalDateTime.now();
        List<HealthEventEntity> events = healthEventRepository.findByTargetOrderByCheckedAtAsc(target);
        List<IncidentEntity> incidents = incidentRepository.findByTargetOrderByStartedAtAsc(target);

        Duration uptimeTotal = Duration.ZERO;
        int uptimeSamples = 0;
        LocalDateTime uptimeStartedAt = null;

        for (HealthEventEntity event : events) {
            if (event.getStatus() == ProbeStatus.UP) {
                if (uptimeStartedAt == null) {
                    uptimeStartedAt = event.getCheckedAt();
                }
                continue;
            }

            if (uptimeStartedAt != null) {
                uptimeTotal = uptimeTotal.plus(Duration.between(uptimeStartedAt, event.getCheckedAt()));
                uptimeSamples += 1;
                uptimeStartedAt = null;
            }
        }

        if (uptimeStartedAt != null) {
            uptimeTotal = uptimeTotal.plus(Duration.between(uptimeStartedAt, referenceTime));
            uptimeSamples += 1;
        }

        Duration downtimeTotal = Duration.ZERO;
        int downtimeSamples = 0;
        long activeIncidentCount = 0L;

        for (IncidentEntity incident : incidents) {
            LocalDateTime endedAt = incident.getResolvedAt() == null ? referenceTime : incident.getResolvedAt();
            downtimeTotal = downtimeTotal.plus(Duration.between(incident.getStartedAt(), endedAt));
            downtimeSamples += 1;
            if (incident.getResolvedAt() == null) {
                activeIncidentCount += 1;
            }
        }

        double mttfMinutes = averageMinutes(uptimeTotal, uptimeSamples);
        double mttrMinutes = averageMinutes(downtimeTotal, downtimeSamples);
        double mtbfMinutes = mttfMinutes + mttrMinutes;

        long observedMillis = uptimeTotal.toMillis() + downtimeTotal.toMillis();
        double availabilityRate = observedMillis == 0L
                ? 0.0
                : uptimeTotal.toMillis() / (double) observedMillis;

        return new TargetReliabilityMetrics(
                target,
                mtbfMinutes,
                mttrMinutes,
                mttfMinutes,
                availabilityRate,
                incidents.size(),
                activeIncidentCount
        );
    }

    private double averageMinutes(Duration total, int sampleCount) {
        if (sampleCount == 0) {
            return 0.0;
        }
        return total.toSeconds() / 60.0 / sampleCount;
    }
}
