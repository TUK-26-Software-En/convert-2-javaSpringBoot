package org.tukorea.servicemonitor.incidents.business.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tukorea.servicemonitor.health.model.ServiceTarget;
import org.tukorea.servicemonitor.incidents.dataaccess.entity.IncidentEntity;
import org.tukorea.servicemonitor.incidents.dataaccess.repository.IncidentRepository;
import org.tukorea.servicemonitor.incidents.model.IncidentStatus;
import org.tukorea.servicemonitor.incidents.model.IncidentSummary;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class IncidentQueryService {

    private final IncidentRepository incidentRepository;

    public IncidentQueryService(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    @Transactional(readOnly = true)
    public List<IncidentSummary> findActiveIncidents() {
        return incidentRepository.findByStatusOrderByStartedAtDesc(IncidentStatus.ACTIVE)
                .stream()
                .map(this::toSummary)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<IncidentSummary> findRecentIncidents() {
        return incidentRepository.findTop10ByOrderByStartedAtDesc()
                .stream()
                .map(this::toSummary)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<IncidentSummary> findRecentIncidents(ServiceTarget target) {
        return incidentRepository.findTop5ByTargetOrderByStartedAtDesc(target)
                .stream()
                .map(this::toSummary)
                .toList();
    }

    private IncidentSummary toSummary(IncidentEntity entity) {
        LocalDateTime endTime = entity.getResolvedAt() == null ? LocalDateTime.now() : entity.getResolvedAt();
        long durationMinutes = Math.max(0L, Duration.between(entity.getStartedAt(), endTime).toMinutes());

        return new IncidentSummary(
                entity.getTarget(),
                entity.getStatus(),
                entity.getStartedAt(),
                entity.getResolvedAt(),
                durationMinutes,
                entity.getStartDetail(),
                entity.getResolutionDetail()
        );
    }
}
