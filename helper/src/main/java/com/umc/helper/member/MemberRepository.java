package com.umc.helper.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String name);

    Optional<Member> findByEmail(String email);

    Optional<Member> findById(Long memberId);
}
