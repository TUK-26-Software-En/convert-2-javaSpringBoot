package org.tukorea.libcheckout.member.presentation.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.tukorea.libcheckout.member.business.service.MemberService;
import org.tukorea.libcheckout.member.model.MemberStatus;
import org.tukorea.libcheckout.member.presentation.dto.MemberCreateRequest;

@Controller
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("members", memberService.findAllMembers());
        return "member/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        populateForm(model);
        if (!model.containsAttribute("memberCreateRequest")) {
            model.addAttribute("memberCreateRequest", new MemberCreateRequest());
        }
        return "member/form";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("memberCreateRequest") MemberCreateRequest request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        populateForm(model);
        if (bindingResult.hasErrors()) {
            return "member/form";
        }

        try {
            memberService.registerMember(request.toRegistration());
        } catch (IllegalArgumentException exception) {
            model.addAttribute("errorMessage", exception.getMessage());
            return "member/form";
        }

        redirectAttributes.addFlashAttribute("message", "회원을 등록했습니다.");
        return "redirect:/members";
    }

    private void populateForm(Model model) {
        model.addAttribute("memberStatuses", MemberStatus.values());
    }
}
