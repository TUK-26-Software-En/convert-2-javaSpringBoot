package org.tukorea.libcheckout.global.model;

import org.tukorea.libcheckout.book.model.BookStatus;
import org.tukorea.libcheckout.loan.model.LoanStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record HomeDashboardSummary(
        long bookCount,
        long memberCount,
        long loanCount,
        long activeLoanCount,
        long returnedLoanCount,
        long overdueLoanCount,
        List<BookInventoryItem> books,
        List<LoanOverviewItem> loans,
        List<MemberLoanStat> memberLoanStats
) {

    public record BookInventoryItem(
            Long id,
            String title,
            String author,
            String isbn,
            int totalQuantity,
            int availableQuantity,
            BookStatus status
    ) {
    }

    public record LoanOverviewItem(
            Long id,
            String bookTitle,
            String memberName,
            LoanStatus status,
            boolean overdue,
            LocalDateTime loanedAt,
            LocalDate dueDate,
            LocalDateTime returnedAt
    ) {
    }

    public record MemberLoanStat(
            Long memberId,
            String memberName,
            String email,
            long totalLoans,
            long activeLoans,
            long returnedLoans,
            long overdueLoans
    ) {
    }
}
