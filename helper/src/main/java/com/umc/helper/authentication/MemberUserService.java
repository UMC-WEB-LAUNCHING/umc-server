//package com.umc.helper.authentication;
//
//import com.umc.helper.member.model.Member;
//import com.umc.helper.member.MemberRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class MemberUserService implements UserDetailsService {
//    private final MemberRepository memberRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Member principal = memberRepository.findByUsername(username)
//                .orElseThrow(()->{
//                    log.info("없는 이메일로 로그인 시도");
//                    return new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다 :" + username);
//                });
//
//        principal.setLastLoginDatetime(LocalDateTime.now());
//        memberRepository.save(principal);
//        return new MemberUser(principal);
//    }
//}
