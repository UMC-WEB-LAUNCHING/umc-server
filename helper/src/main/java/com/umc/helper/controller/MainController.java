package com.umc.helper.controller;


import com.umc.helper.authentication.SignUpForm;
import com.umc.helper.authentication.SignUpFormValidator;
import com.umc.helper.member.Member;
import com.umc.helper.member.MemberRepository;
import com.umc.helper.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MainController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String signupForm(Model model) {
        model.addAttribute("signUpForm", new SignUpForm());
        return "member/signUpForm";
    }

    @InitBinder("signUpForm")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(new SignUpFormValidator(memberRepository));
    }

    @PostMapping("/signup")
    public String signUpSubmit(@Valid SignUpForm signUpForm, Errors errors) {
        // 유효성 검사 시작. - initBinder() 가 실행됨.
        if (errors.hasErrors()) {
            log.error("errors : {}", errors.getAllErrors());
            return "member/signUpForm";
        }
        log.info("올바른 회원 정보.");

        Member member = memberService.processNewMember(signUpForm);
        memberService.login(member);

        return "redirect:/";
    }

}
