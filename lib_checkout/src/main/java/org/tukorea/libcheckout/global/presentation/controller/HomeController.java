package org.tukorea.libcheckout.global.presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.tukorea.libcheckout.global.business.service.HomeDashboardService;

@Controller
public class HomeController {

    private final HomeDashboardService homeDashboardService;

    public HomeController(HomeDashboardService homeDashboardService) {
        this.homeDashboardService = homeDashboardService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("summary", homeDashboardService.loadSummary());
        return "home/index";
    }
}
