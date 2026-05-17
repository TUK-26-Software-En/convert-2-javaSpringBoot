package org.tukorea.servicemonitor.incidents.presentation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tukorea.servicemonitor.incidents.business.service.IncidentQueryService;
import org.tukorea.servicemonitor.incidents.model.IncidentSummary;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    private final IncidentQueryService incidentQueryService;

    public IncidentController(IncidentQueryService incidentQueryService) {
        this.incidentQueryService = incidentQueryService;
    }

    @GetMapping
    public List<IncidentSummary> incidents() {
        return incidentQueryService.findRecentIncidents();
    }
}
