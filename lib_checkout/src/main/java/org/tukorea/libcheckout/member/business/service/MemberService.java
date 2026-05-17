package org.tukorea.libcheckout.member.business.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tukorea.libcheckout.member.dataaccess.entity.MemberEntity;
import org.tukorea.libcheckout.member.dataaccess.repository.MemberRepository;
import org.tukorea.libcheckout.member.model.MemberRegistration;
import org.tukorea.libcheckout.member.model.MemberStatus;
import org.tukorea.libcheckout.member.model.MemberSummary;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
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
}
