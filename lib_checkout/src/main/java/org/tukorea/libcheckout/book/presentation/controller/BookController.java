package org.tukorea.libcheckout.book.presentation.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.tukorea.libcheckout.book.business.service.BookService;
import org.tukorea.libcheckout.book.model.BookStatus;
import org.tukorea.libcheckout.book.presentation.dto.BookCreateRequest;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("books", bookService.findAllBooks());
        return "book/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        populateForm(model);
        if (!model.containsAttribute("bookCreateRequest")) {
            model.addAttribute("bookCreateRequest", new BookCreateRequest());
        }
        return "book/form";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("bookCreateRequest") BookCreateRequest request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        populateForm(model);
        if (bindingResult.hasErrors()) {
            return "book/form";
        }

        try {
            bookService.createBook(request.toRegistration());
        } catch (IllegalArgumentException exception) {
            model.addAttribute("errorMessage", exception.getMessage());
            return "book/form";
        }

        redirectAttributes.addFlashAttribute("message", "도서를 등록했습니다.");
        return "redirect:/books";
    }

    private void populateForm(Model model) {
        model.addAttribute("bookStatuses", BookStatus.values());
    }
}
