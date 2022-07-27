package com.umc.helper.member;

import com.umc.helper.authentication.MemberUser;
import com.umc.helper.authentication.SignUpForm;
import com.umc.helper.authentication.SignUpFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.time.LocalDateTime;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member createNewUser(String name, String email){
        Member member = Member.builder()
                .username(name)
                .email("hojune0904@gmail.com")
                .password(passwordEncoder.encode("Password!"))
                .registerDateTime(LocalDateTime.now())
                .build();
        Member newMember = memberRepository.save(member);
        
        return newMember;
    }

    public Member processNewMember(SignUpForm signupForm){

        Member member = Member.builder()
                .username(signupForm.getUsername())
                .email(signupForm.getEmail())
                .password(passwordEncoder.encode(signupForm.getPassword()))
                .registerDateTime(LocalDateTime.now())
                .build();

        Member newMember = memberRepository.save(member);

        return newMember;
    }

    @InitBinder("signUpForm")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(new SignUpFormValidator(memberRepository));
    }

    public void login(Member member){

        MemberUser memberUser = new MemberUser(member);

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                        memberUser,
                        memberUser.getMember().getPassword(),
                        memberUser.getAuthorities()
                );

        SecurityContextHolder.getContext().setAuthentication(token);

    }

    public Member getMemberByUserName(String username) {
        Optional<Member> member = memberRepository.findByUsername(username);
        if(member.isEmpty()){
            return null;
        }
        return member.get();
    }


}
