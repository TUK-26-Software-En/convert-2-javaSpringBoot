package org.tukorea.libcheckout.loan.presentation.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import org.tukorea.libcheckout.loan.model.LoanRegistration;

import java.time.LocalDate;

public class LoanCreateRequest {

    @NotNull
    private Long bookId;

    @NotNull
    private Long memberId;

    @NotNull
    @FutureOrPresent
    private LocalDate dueDate = LocalDate.now().plusDays(14);

    public LoanRegistration toRegistration() {
        return new LoanRegistration(bookId, memberId, dueDate);
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
