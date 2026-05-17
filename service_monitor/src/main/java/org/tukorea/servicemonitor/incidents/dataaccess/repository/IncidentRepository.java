package org.tukorea.servicemonitor.incidents.dataaccess.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tukorea.servicemonitor.health.model.ServiceTarget;
import org.tukorea.servicemonitor.incidents.dataaccess.entity.IncidentEntity;
import org.tukorea.servicemonitor.incidents.model.IncidentStatus;

import java.util.List;
import java.util.Optional;

public interface IncidentRepository extends JpaRepository<IncidentEntity, Long> {

    Optional<IncidentEntity> findTopByTargetAndStatusOrderByStartedAtDesc(ServiceTarget target, IncidentStatus status);

    List<IncidentEntity> findTop10ByOrderByStartedAtDesc();

    List<IncidentEntity> findTop5ByTargetOrderByStartedAtDesc(ServiceTarget target);

    List<IncidentEntity> findByStatusOrderByStartedAtDesc(IncidentStatus status);

    List<IncidentEntity> findByTargetOrderByStartedAtAsc(ServiceTarget target);
}
