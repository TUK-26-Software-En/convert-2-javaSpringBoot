package org.tukorea.libcheckout.member.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record MemberDashboardView(
        long totalMemberCount,
        long activeMemberCount,
        long inactiveMemberCount,
        long borrowingMemberCount,
        long overdueMemberCount,
        List<MemberDashboardItem> members
) {

    public record MemberDashboardItem(
            Long id,
            String name,
            String email,
            String phoneNumber,
            MemberStatus status,
            long totalLoans,
            long activeLoans,
            long returnedLoans,
            long overdueLoans,
            String latestBookTitle,
            LocalDateTime latestLoanedAt,
            LocalDate nearestDueDate
    ) {
    }
}
