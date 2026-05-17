package org.tukorea.servicemonitor.health.dataaccess.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.tukorea.servicemonitor.health.model.ProbeStatus;
import org.tukorea.servicemonitor.health.model.ServiceTarget;

import java.time.LocalDateTime;

@Entity
@Table(name = "service_health_events")
public class HealthEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 64)
    private ServiceTarget target;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private ProbeStatus status;

    @Column(nullable = false, length = 500)
    private String detail;

    @Column(name = "checked_at", nullable = false)
    private LocalDateTime checkedAt;

    protected HealthEventEntity() {
    }

    public HealthEventEntity(ServiceTarget target, ProbeStatus status, String detail, LocalDateTime checkedAt) {
        this.target = target;
        this.status = status;
        this.detail = detail;
        this.checkedAt = checkedAt;
    }

    public Long getId() {
        return id;
    }

    public ServiceTarget getTarget() {
        return target;
    }

    public ProbeStatus getStatus() {
        return status;
    }

    public String getDetail() {
        return detail;
    }

    public LocalDateTime getCheckedAt() {
        return checkedAt;
    }
}
