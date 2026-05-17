package org.tukorea.servicemonitor.health.dataaccess.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tukorea.servicemonitor.health.dataaccess.entity.HealthEventEntity;
import org.tukorea.servicemonitor.health.model.ServiceTarget;

import java.util.List;
import java.util.Optional;

public interface HealthEventRepository extends JpaRepository<HealthEventEntity, Long> {

    Optional<HealthEventEntity> findTopByTargetOrderByCheckedAtDesc(ServiceTarget target);

    List<HealthEventEntity> findByTargetOrderByCheckedAtAsc(ServiceTarget target);

    List<HealthEventEntity> findTop6ByTargetOrderByCheckedAtDesc(ServiceTarget target);

    List<HealthEventEntity> findTop12ByOrderByCheckedAtDesc();
}
