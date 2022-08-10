//package com.umc.helper.controller;
//
//
////import com.umc.helper.authentication.SignUpForm;
////import com.umc.helper.authentication.SignUpFormValidator;
//import com.umc.helper.member.model.Member;
//import com.umc.helper.member.MemberRepository;
////import com.umc.helper.member.MemberService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.Errors;
//import org.springframework.web.bind.WebDataBinder;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//
//@Controller
//@Slf4j
//@RequiredArgsConstructor
//public class MainController {
//
//    private final MemberRepository memberRepository;
//    private final MemberService memberService;
//
//    @GetMapping("/")
//    public String index(){
//        return "index";
//    }
//
//    @GetMapping("/member/login")
//    public String loginPage() {
//        return "member/loginForm";
//    }
//
//    @GetMapping("/member/signup")
//    public String signupForm(Model model) {
//        model.addAttribute("signUpForm", new SignUpForm());
//        return "member/signUpForm";
//    }
//
//
//    @InitBinder("signUpForm")
//    protected void initBinder(WebDataBinder binder) {
//        binder.addValidators(new SignUpFormValidator(memberRepository));
//    }
//
//    @RequestMapping(value = "/login", method = RequestMethod.GET)
//    public String goLogin() {
//        return "login";
//    }
//
//    @PostMapping("/signup")
//    public String signUpSubmit(@Valid SignUpForm signUpForm, Errors errors) {
//        // 유효성 검사 시작. - initBinder() 가 실행됨.
//        if (errors.hasErrors()) {
//            log.error("errors : {}", errors.getAllErrors());
//            return "member/signUpForm";
//        }
//        log.info("올바른 회원 정보.");
//
//        Member member = memberService.processNewMember(signUpForm);
//        memberService.login(member);
//
//        return "redirect:/";
//    }
//
//}
