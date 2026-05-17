package org.tukorea.libcheckout.global.business.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tukorea.libcheckout.book.dataaccess.entity.BookEntity;
import org.tukorea.libcheckout.book.dataaccess.repository.BookRepository;
import org.tukorea.libcheckout.global.model.HomeDashboardSummary;
import org.tukorea.libcheckout.loan.dataaccess.entity.LoanEntity;
import org.tukorea.libcheckout.loan.dataaccess.repository.LoanRepository;
import org.tukorea.libcheckout.loan.model.LoanStatus;
import org.tukorea.libcheckout.member.dataaccess.entity.MemberEntity;
import org.tukorea.libcheckout.member.dataaccess.repository.MemberRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class HomeDashboardService {

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final LoanRepository loanRepository;

    public HomeDashboardService(
            BookRepository bookRepository,
            MemberRepository memberRepository,
            LoanRepository loanRepository
    ) {
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.loanRepository = loanRepository;
    }

    @Transactional(readOnly = true)
    public HomeDashboardSummary loadSummary() {
        List<BookEntity> books = bookRepository.findAllByOrderByIdDesc();
        List<MemberEntity> members = memberRepository.findAllByOrderByIdDesc();
        List<LoanEntity> loans = loanRepository.findAllWithBookAndMemberOrderByLoanedAtDesc();
        LocalDate today = LocalDate.now();

        List<HomeDashboardSummary.BookInventoryItem> bookItems = books.stream()
                .map(book -> new HomeDashboardSummary.BookInventoryItem(
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getIsbn(),
                        book.getTotalQuantity(),
                        book.getAvailableQuantity(),
                        book.getStatus()
                ))
                .toList();

        Map<Long, MemberLoanAccumulator> memberLoanAccumulators = new LinkedHashMap<>();
        for (MemberEntity member : members) {
            memberLoanAccumulators.put(member.getId(), new MemberLoanAccumulator(member.getId(), member.getName(), member.getEmail()));
        }

        List<HomeDashboardSummary.LoanOverviewItem> loanItems = new ArrayList<>();
        long activeLoanCount = 0L;
        long returnedLoanCount = 0L;
        long overdueLoanCount = 0L;

        for (LoanEntity loan : loans) {
            boolean overdue = loan.getStatus() == LoanStatus.ACTIVE && loan.getDueDate().isBefore(today);
            if (loan.getStatus() == LoanStatus.ACTIVE) {
                activeLoanCount += 1;
            }
            if (loan.getStatus() == LoanStatus.RETURNED) {
                returnedLoanCount += 1;
            }
            if (overdue) {
                overdueLoanCount += 1;
            }

            loanItems.add(new HomeDashboardSummary.LoanOverviewItem(
                    loan.getId(),
                    loan.getBook().getTitle(),
                    loan.getMember().getName(),
                    loan.getStatus(),
                    overdue,
                    loan.getLoanedAt(),
                    loan.getDueDate(),
                    loan.getReturnedAt()
            ));

            MemberEntity member = loan.getMember();
            memberLoanAccumulators.computeIfAbsent(
                    member.getId(),
                    ignored -> new MemberLoanAccumulator(member.getId(), member.getName(), member.getEmail())
            ).recordLoan(loan.getStatus(), overdue);
        }

        List<HomeDashboardSummary.MemberLoanStat> memberLoanStats = memberLoanAccumulators.values().stream()
                .map(MemberLoanAccumulator::toView)
                .sorted(Comparator
                        .comparingLong(HomeDashboardSummary.MemberLoanStat::activeLoans).reversed()
                        .thenComparingLong(HomeDashboardSummary.MemberLoanStat::overdueLoans).reversed()
                        .thenComparingLong(HomeDashboardSummary.MemberLoanStat::totalLoans).reversed()
                        .thenComparing(HomeDashboardSummary.MemberLoanStat::memberName)
                )
                .toList();

        return new HomeDashboardSummary(
                books.size(),
                members.size(),
                loans.size(),
                activeLoanCount,
                returnedLoanCount,
                overdueLoanCount,
                bookItems,
                loanItems,
                memberLoanStats
        );
    }

    private static final class MemberLoanAccumulator {

        private final Long memberId;
        private final String memberName;
        private final String email;
        private long totalLoans;
        private long activeLoans;
        private long returnedLoans;
        private long overdueLoans;

        private MemberLoanAccumulator(Long memberId, String memberName, String email) {
            this.memberId = memberId;
            this.memberName = memberName;
            this.email = email;
        }

        private void recordLoan(LoanStatus status, boolean overdue) {
            totalLoans += 1;
            if (status == LoanStatus.ACTIVE) {
                activeLoans += 1;
            }
            if (status == LoanStatus.RETURNED) {
                returnedLoans += 1;
            }
            if (overdue) {
                overdueLoans += 1;
            }
        }

        private HomeDashboardSummary.MemberLoanStat toView() {
            return new HomeDashboardSummary.MemberLoanStat(
                    memberId,
                    memberName,
                    email,
                    totalLoans,
                    activeLoans,
                    returnedLoans,
                    overdueLoans
            );
        }
    }
}
