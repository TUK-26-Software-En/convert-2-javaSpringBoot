package org.tukorea.libcheckout.loan.business.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tukorea.libcheckout.book.dataaccess.entity.BookEntity;
import org.tukorea.libcheckout.book.dataaccess.repository.BookRepository;
import org.tukorea.libcheckout.loan.dataaccess.entity.LoanEntity;
import org.tukorea.libcheckout.loan.dataaccess.repository.LoanRepository;
import org.tukorea.libcheckout.loan.model.LoanRegistration;
import org.tukorea.libcheckout.loan.model.LoanStatus;
import org.tukorea.libcheckout.loan.model.LoanSummary;
import org.tukorea.libcheckout.member.dataaccess.entity.MemberEntity;
import org.tukorea.libcheckout.member.dataaccess.repository.MemberRepository;
import org.tukorea.libcheckout.member.model.MemberStatus;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    public LoanService(
            LoanRepository loanRepository,
            BookRepository bookRepository,
            MemberRepository memberRepository
    ) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public List<LoanSummary> findAllLoans() {
        return loanRepository.findAllWithBookAndMemberOrderByLoanedAtDesc()
                .stream()
                .map(this::toSummary)
                .toList();
    }

    @Transactional
    public void createLoan(LoanRegistration registration) {
        BookEntity book = bookRepository.findById(registration.bookId())
                .orElseThrow(() -> new IllegalArgumentException("도서를 찾을 수 없습니다."));

        MemberEntity member = memberRepository.findById(registration.memberId())
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        if (member.getStatus() != MemberStatus.ACTIVE) {
            throw new IllegalStateException("비활성 회원은 대출할 수 없습니다.");
        }

        book.lendOne();

        LoanEntity loan = new LoanEntity(
                book,
                member,
                LocalDateTime.now(),
                registration.dueDate(),
                null,
                LoanStatus.ACTIVE
        );
        loanRepository.save(loan);
    }

    @Transactional
    public void returnLoan(Long loanId) {
        LoanEntity loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("대출 정보를 찾을 수 없습니다."));

        loan.returnLoan(LocalDateTime.now());
        loan.getBook().returnOne();
    }

    private LoanSummary toSummary(LoanEntity entity) {
        return new LoanSummary(
                entity.getId(),
                entity.getBook().getTitle(),
                entity.getMember().getName(),
                entity.getLoanedAt(),
                entity.getDueDate(),
                entity.getReturnedAt(),
                entity.getStatus()
        );
    }
}
