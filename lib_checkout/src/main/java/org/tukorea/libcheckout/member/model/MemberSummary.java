package org.tukorea.libcheckout.member.model;

public record MemberSummary(
        Long id,
        String name,
        String email,
        String phoneNumber,
        MemberStatus status
) {
}
