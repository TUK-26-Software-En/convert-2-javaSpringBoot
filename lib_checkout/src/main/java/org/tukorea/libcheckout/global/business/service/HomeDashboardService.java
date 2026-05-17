package org.tukorea.libcheckout.global.business.service;

import org.springframework.stereotype.Service;
import org.tukorea.libcheckout.book.dataaccess.repository.BookRepository;
import org.tukorea.libcheckout.global.model.HomeDashboardSummary;
import org.tukorea.libcheckout.loan.dataaccess.repository.LoanRepository;
import org.tukorea.libcheckout.loan.model.LoanStatus;
import org.tukorea.libcheckout.member.dataaccess.repository.MemberRepository;

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

    public HomeDashboardSummary loadSummary() {
        return new HomeDashboardSummary(
                bookRepository.count(),
                memberRepository.count(),
                loanRepository.count(),
                loanRepository.countByStatus(LoanStatus.ACTIVE)
        );
    }
}
