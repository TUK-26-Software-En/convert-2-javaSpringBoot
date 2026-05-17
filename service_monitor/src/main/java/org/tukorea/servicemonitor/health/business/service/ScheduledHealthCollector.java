package org.tukorea.servicemonitor.health.business.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "monitoring.collection.enabled", havingValue = "true", matchIfMissing = true)
public class ScheduledHealthCollector {

    private final HealthCollectionService healthCollectionService;

    public ScheduledHealthCollector(HealthCollectionService healthCollectionService) {
        this.healthCollectionService = healthCollectionService;
    }

    @Scheduled(fixedDelayString = "${monitoring.collection.fixed-delay:30000}")
    public void collect() {
        healthCollectionService.collectAndStoreSnapshot();
    }
}
