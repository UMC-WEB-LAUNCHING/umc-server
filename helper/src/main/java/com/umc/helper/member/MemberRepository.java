package com.umc.helper.member;

import com.umc.helper.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {


    Optional<Member> findByEmail(String email);

    Optional<Member> findById(Long memberId);

    @Override
    void delete(Member entity);
}
