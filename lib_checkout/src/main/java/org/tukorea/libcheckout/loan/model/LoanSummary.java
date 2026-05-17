package org.tukorea.libcheckout.loan.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record LoanSummary(
        Long id,
        String bookTitle,
        String memberName,
        LocalDateTime loanedAt,
        LocalDate dueDate,
        LocalDateTime returnedAt,
        LoanStatus status
) {
}
