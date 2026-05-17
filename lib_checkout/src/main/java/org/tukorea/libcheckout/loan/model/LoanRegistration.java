package org.tukorea.libcheckout.loan.model;

import java.time.LocalDate;

public record LoanRegistration(
        Long bookId,
        Long memberId,
        LocalDate dueDate
) {
}
