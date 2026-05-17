package org.tukorea.libcheckout.loan.dataaccess.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.tukorea.libcheckout.loan.dataaccess.entity.LoanEntity;
import org.tukorea.libcheckout.loan.model.LoanStatus;

import java.util.List;

public interface LoanRepository extends JpaRepository<LoanEntity, Long> {

    long countByStatus(LoanStatus status);

    @Query("select loan from LoanEntity loan join fetch loan.book join fetch loan.member order by loan.loanedAt desc")
    List<LoanEntity> findAllWithBookAndMemberOrderByLoanedAtDesc();
}
