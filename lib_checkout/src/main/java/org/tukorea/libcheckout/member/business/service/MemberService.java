package org.tukorea.libcheckout.member.business.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tukorea.libcheckout.loan.dataaccess.entity.LoanEntity;
import org.tukorea.libcheckout.loan.dataaccess.repository.LoanRepository;
import org.tukorea.libcheckout.loan.model.LoanStatus;
import org.tukorea.libcheckout.member.dataaccess.entity.MemberEntity;
import org.tukorea.libcheckout.member.dataaccess.repository.MemberRepository;
import org.tukorea.libcheckout.member.model.MemberDashboardView;
import org.tukorea.libcheckout.member.model.MemberRegistration;
import org.tukorea.libcheckout.member.model.MemberStatus;
import org.tukorea.libcheckout.member.model.MemberSummary;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final LoanRepository loanRepository;

    public MemberService(MemberRepository memberRepository, LoanRepository loanRepository) {
        this.memberRepository = memberRepository;
        this.loanRepository = loanRepository;
    }

    @Transactional(readOnly = true)
    public List<MemberSummary> findAllMembers() {
        return memberRepository.findAllByOrderByIdDesc()
                .stream()
                .map(this::toSummary)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MemberSummary> findActiveMembers() {
        return memberRepository.findByStatusOrderByNameAsc(MemberStatus.ACTIVE)
                .stream()
                .map(this::toSummary)
                .toList();
    }

    @Transactional(readOnly = true)
    public MemberDashboardView loadDashboard() {
        List<MemberEntity> members = memberRepository.findAllByOrderByIdDesc();
        List<LoanEntity> loans = loanRepository.findAllWithBookAndMemberOrderByLoanedAtDesc();
        Map<Long, MemberDashboardAccumulator> accumulators = new LinkedHashMap<>();
        LocalDate today = LocalDate.now();

        for (MemberEntity member : members) {
            accumulators.put(member.getId(), new MemberDashboardAccumulator(member));
        }

        for (LoanEntity loan : loans) {
            Long memberId = loan.getMember().getId();
            accumulators.computeIfAbsent(memberId, ignored -> new MemberDashboardAccumulator(loan.getMember()))
                    .recordLoan(loan, today);
        }

        List<MemberDashboardView.MemberDashboardItem> memberItems = accumulators.values().stream()
                .map(MemberDashboardAccumulator::toView)
                .sorted(Comparator
                        .comparing((MemberDashboardView.MemberDashboardItem item) -> item.status() == MemberStatus.ACTIVE ? 0 : 1)
                        .thenComparing(Comparator.comparingLong(MemberDashboardView.MemberDashboardItem::overdueLoans).reversed())
                        .thenComparing(Comparator.comparingLong(MemberDashboardView.MemberDashboardItem::activeLoans).reversed())
                        .thenComparing(Comparator.comparingLong(MemberDashboardView.MemberDashboardItem::totalLoans).reversed())
                        .thenComparing(MemberDashboardView.MemberDashboardItem::name)
                )
                .toList();

        long activeMemberCount = members.stream()
                .filter(member -> member.getStatus() == MemberStatus.ACTIVE)
                .count();
        long inactiveMemberCount = members.size() - activeMemberCount;
        long borrowingMemberCount = memberItems.stream()
                .filter(item -> item.activeLoans() > 0)
                .count();
        long overdueMemberCount = memberItems.stream()
                .filter(item -> item.overdueLoans() > 0)
                .count();

        return new MemberDashboardView(
                members.size(),
                activeMemberCount,
                inactiveMemberCount,
                borrowingMemberCount,
                overdueMemberCount,
                memberItems
        );
    }

    @Transactional
    public void registerMember(MemberRegistration registration) {
        if (memberRepository.existsByEmail(registration.email())) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }

        LocalDateTime now = LocalDateTime.now();
        MemberEntity member = new MemberEntity(
                registration.name(),
                registration.email(),
                registration.phoneNumber(),
                registration.status(),
                now,
                now
        );
        memberRepository.save(member);
    }

    private MemberSummary toSummary(MemberEntity entity) {
        return new MemberSummary(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPhoneNumber(),
                entity.getStatus()
        );
    }

    private static final class MemberDashboardAccumulator {

        private final Long id;
        private final String name;
        private final String email;
        private final String phoneNumber;
        private final MemberStatus status;
        private long totalLoans;
        private long activeLoans;
        private long returnedLoans;
        private long overdueLoans;
        private String latestBookTitle = "-";
        private LocalDateTime latestLoanedAt;
        private LocalDate nearestDueDate;

        private MemberDashboardAccumulator(MemberEntity member) {
            this.id = member.getId();
            this.name = member.getName();
            this.email = member.getEmail();
            this.phoneNumber = member.getPhoneNumber();
            this.status = member.getStatus();
        }

        private void recordLoan(LoanEntity loan, LocalDate today) {
            totalLoans += 1;

            if (latestLoanedAt == null || loan.getLoanedAt().isAfter(latestLoanedAt)) {
                latestLoanedAt = loan.getLoanedAt();
                latestBookTitle = loan.getBook().getTitle();
            }

            if (loan.getStatus() == LoanStatus.ACTIVE) {
                activeLoans += 1;
                if (nearestDueDate == null || loan.getDueDate().isBefore(nearestDueDate)) {
                    nearestDueDate = loan.getDueDate();
                }
                if (loan.getDueDate().isBefore(today)) {
                    overdueLoans += 1;
                }
                return;
            }

            if (loan.getStatus() == LoanStatus.RETURNED) {
                returnedLoans += 1;
            }
        }

        private MemberDashboardView.MemberDashboardItem toView() {
            return new MemberDashboardView.MemberDashboardItem(
                    id,
                    name,
                    email,
                    phoneNumber,
                    status,
                    totalLoans,
                    activeLoans,
                    returnedLoans,
                    overdueLoans,
                    latestBookTitle,
                    latestLoanedAt,
                    nearestDueDate
            );
        }
    }
}
