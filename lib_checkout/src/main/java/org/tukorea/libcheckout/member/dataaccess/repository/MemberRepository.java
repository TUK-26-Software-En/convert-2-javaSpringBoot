package org.tukorea.libcheckout.member.dataaccess.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tukorea.libcheckout.member.dataaccess.entity.MemberEntity;
import org.tukorea.libcheckout.member.model.MemberStatus;

import java.util.List;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    boolean existsByEmail(String email);

    List<MemberEntity> findAllByOrderByIdDesc();

    List<MemberEntity> findByStatusOrderByNameAsc(MemberStatus status);
}
