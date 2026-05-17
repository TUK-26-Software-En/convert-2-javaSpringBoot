package org.tukorea.libcheckout.loan.presentation.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.tukorea.libcheckout.book.business.service.BookService;
import org.tukorea.libcheckout.loan.business.service.LoanService;
import org.tukorea.libcheckout.loan.presentation.dto.LoanCreateRequest;
import org.tukorea.libcheckout.member.business.service.MemberService;

@Controller
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;
    private final BookService bookService;
    private final MemberService memberService;

    public LoanController(
            LoanService loanService,
            BookService bookService,
            MemberService memberService
    ) {
        this.loanService = loanService;
        this.bookService = bookService;
        this.memberService = memberService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("loans", loanService.findAllLoans());
        return "loan/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        populateForm(model);
        if (!model.containsAttribute("loanCreateRequest")) {
            model.addAttribute("loanCreateRequest", new LoanCreateRequest());
        }
        return "loan/form";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("loanCreateRequest") LoanCreateRequest request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        populateForm(model);
        if (bindingResult.hasErrors()) {
            return "loan/form";
        }

        try {
            loanService.createLoan(request.toRegistration());
        } catch (IllegalArgumentException | IllegalStateException exception) {
            model.addAttribute("errorMessage", exception.getMessage());
            return "loan/form";
        }

        redirectAttributes.addFlashAttribute("message", "대출을 등록했습니다.");
        return "redirect:/loans";
    }

    @PostMapping("/{loanId}/return")
    public String returnLoan(@PathVariable Long loanId, RedirectAttributes redirectAttributes) {
        loanService.returnLoan(loanId);
        redirectAttributes.addFlashAttribute("message", "반납을 처리했습니다.");
        return "redirect:/loans";
    }

    private void populateForm(Model model) {
        model.addAttribute("availableBooks", bookService.findAvailableBooksForLoan());
        model.addAttribute("activeMembers", memberService.findActiveMembers());
    }
}
