package org.tukorea.libcheckout.member.model;

public record MemberRegistration(
        String name,
        String email,
        String phoneNumber,
        MemberStatus status
) {
}
