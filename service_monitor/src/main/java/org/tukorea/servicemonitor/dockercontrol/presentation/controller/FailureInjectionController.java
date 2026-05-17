package org.tukorea.servicemonitor.dockercontrol.presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.tukorea.servicemonitor.dockercontrol.business.service.DockerControlService;
import org.tukorea.servicemonitor.dockercontrol.model.DockerControlCommand;
import org.tukorea.servicemonitor.dockercontrol.model.DockerControlResult;
import org.tukorea.servicemonitor.health.model.ServiceTarget;

@Controller
@RequestMapping("/failure-injection")
public class FailureInjectionController {

    private final DockerControlService dockerControlService;

    public FailureInjectionController(DockerControlService dockerControlService) {
        this.dockerControlService = dockerControlService;
    }

    @PostMapping
    public String execute(
            @RequestParam ServiceTarget target,
            @RequestParam DockerControlCommand command,
            @RequestParam(required = false) String reason,
            RedirectAttributes redirectAttributes
    ) {
        DockerControlResult result = dockerControlService.executeFailureInjection(target, command, reason);
        redirectAttributes.addFlashAttribute("controlResult", result);
        return "redirect:/#tab-failure-injection";
    }
}
