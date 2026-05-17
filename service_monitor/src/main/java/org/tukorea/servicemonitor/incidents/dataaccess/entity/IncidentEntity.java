package org.tukorea.servicemonitor.incidents.dataaccess.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.tukorea.servicemonitor.health.model.ServiceTarget;
import org.tukorea.servicemonitor.incidents.model.IncidentStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "incidents")
public class IncidentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 64)
    private ServiceTarget target;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private IncidentStatus status;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "start_detail", nullable = false, length = 500)
    private String startDetail;

    @Column(name = "resolution_detail", length = 500)
    private String resolutionDetail;

    protected IncidentEntity() {
    }

    public IncidentEntity(
            ServiceTarget target,
            IncidentStatus status,
            LocalDateTime startedAt,
            LocalDateTime resolvedAt,
            String startDetail,
            String resolutionDetail
    ) {
        this.target = target;
        this.status = status;
        this.startedAt = startedAt;
        this.resolvedAt = resolvedAt;
        this.startDetail = startDetail;
        this.resolutionDetail = resolutionDetail;
    }

    public static IncidentEntity open(ServiceTarget target, LocalDateTime startedAt, String startDetail) {
        return new IncidentEntity(target, IncidentStatus.ACTIVE, startedAt, null, startDetail, null);
    }

    public void resolve(LocalDateTime resolvedAt, String resolutionDetail) {
        this.status = IncidentStatus.RESOLVED;
        this.resolvedAt = resolvedAt;
        this.resolutionDetail = resolutionDetail;
    }

    public Long getId() {
        return id;
    }

    public ServiceTarget getTarget() {
        return target;
    }

    public IncidentStatus getStatus() {
        return status;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public String getStartDetail() {
        return startDetail;
    }

    public String getResolutionDetail() {
        return resolutionDetail;
    }
}
