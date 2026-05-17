package org.tukorea.servicemonitor.health.presentation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tukorea.servicemonitor.health.business.service.HealthCollectionService;
import org.tukorea.servicemonitor.health.model.HealthSnapshot;

import java.util.List;

@RestController
@RequestMapping("/api/health")
public class HealthSummaryController {

    private final HealthCollectionService healthCollectionService;

    public HealthSummaryController(HealthCollectionService healthCollectionService) {
        this.healthCollectionService = healthCollectionService;
    }

    @GetMapping("/summary")
    public List<HealthSnapshot> summary() {
        return healthCollectionService.collectAndStoreSnapshot();
    }
}
