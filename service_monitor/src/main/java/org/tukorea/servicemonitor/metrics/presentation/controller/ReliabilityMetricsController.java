package org.tukorea.servicemonitor.metrics.presentation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tukorea.servicemonitor.metrics.business.service.ReliabilityMetricsService;
import org.tukorea.servicemonitor.metrics.model.TargetReliabilityMetrics;

import java.util.List;

@RestController
@RequestMapping("/api/metrics")
public class ReliabilityMetricsController {

    private final ReliabilityMetricsService reliabilityMetricsService;

    public ReliabilityMetricsController(ReliabilityMetricsService reliabilityMetricsService) {
        this.reliabilityMetricsService = reliabilityMetricsService;
    }

    @GetMapping("/reliability")
    public List<TargetReliabilityMetrics> reliability() {
        return reliabilityMetricsService.loadMetrics();
    }
}
