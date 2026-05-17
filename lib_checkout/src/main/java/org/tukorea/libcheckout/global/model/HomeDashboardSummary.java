package org.tukorea.libcheckout.global.model;

public record HomeDashboardSummary(
        long bookCount,
        long memberCount,
        long loanCount,
        long activeLoanCount
) {
}
