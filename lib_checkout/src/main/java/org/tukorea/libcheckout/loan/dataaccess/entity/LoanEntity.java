package org.tukorea.libcheckout.loan.dataaccess.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.tukorea.libcheckout.book.dataaccess.entity.BookEntity;
import org.tukorea.libcheckout.loan.model.LoanStatus;
import org.tukorea.libcheckout.member.dataaccess.entity.MemberEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "loans")
public class LoanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private BookEntity book;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @Column(name = "loaned_at", nullable = false)
    private LocalDateTime loanedAt;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "returned_at")
    private LocalDateTime returnedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private LoanStatus status;

    protected LoanEntity() {
    }

    public LoanEntity(
            BookEntity book,
            MemberEntity member,
            LocalDateTime loanedAt,
            LocalDate dueDate,
            LocalDateTime returnedAt,
            LoanStatus status
    ) {
        this.book = book;
        this.member = member;
        this.loanedAt = loanedAt;
        this.dueDate = dueDate;
        this.returnedAt = returnedAt;
        this.status = status;
    }

    public void returnLoan(LocalDateTime returnedAt) {
        if (status != LoanStatus.ACTIVE) {
            throw new IllegalStateException("활성 대출만 반납할 수 있습니다.");
        }

        this.returnedAt = returnedAt;
        this.status = LoanStatus.RETURNED;
    }

    public Long getId() {
        return id;
    }

    public BookEntity getBook() {
        return book;
    }

    public MemberEntity getMember() {
        return member;
    }

    public LocalDateTime getLoanedAt() {
        return loanedAt;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDateTime getReturnedAt() {
        return returnedAt;
    }

    public LoanStatus getStatus() {
        return status;
    }
}
