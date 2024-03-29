package com.umc.helper.auth.oauth;

import com.umc.helper.auth.TokenResponse;
import com.umc.helper.config.BaseResponse;
import com.umc.helper.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
@Slf4j
public class OauthController {
    private final OauthService oauthService;
    private final MemberService memberService;
    /**
     * 사용자로부터 SNS 로그인 요청을 Social Login Type 을 받아 처리
     * @param socialLoginType (GOOGLE, FACEBOOK, NAVER, KAKAO)
     */
    @GetMapping(value = "/{socialLoginType}")
    public void socialLoginType(
            @PathVariable(name = "socialLoginType") SocialLoginType socialLoginType) {
        log.info(">> 사용자로부터 SNS 로그인 요청을 받음 :: {} Social Login", socialLoginType);
        oauthService.request(socialLoginType);
        //oauthService.requestProfile(socialLoginType);

    }

//    /**
//     * Social Login API Server 요청에 의한 callback 을 처리
//     * @param socialLoginType (GOOGLE, FACEBOOK, NAVER, KAKAO)
//     * @param code API Server 로부터 넘어노는 code
//     * @return SNS Login 요청 결과로 받은 Json 형태의 String 문자열 (access_token, refresh_token 등)
//     */
//    @GetMapping(value = "/{socialLoginType}/callback")
//    public ResponseEntity<String> callback(
//            @PathVariable(name = "socialLoginType") SocialLoginType socialLoginType,
//            @RequestParam(name = "code") String code) {
//        log.info(">> 소셜 로그인 API 서버로부터 받은 code :: {}", code);
//        return oauthService.requestAccessToken(socialLoginType, code);
//    }
    /**
     * Social Login API Server 요청에 의한 callback 을 처리
     * @param socialLoginType (GOOGLE, FACEBOOK, NAVER, KAKAO)
     * @param code API Server 로부터 넘어노는 code
     * @return SNS Login 요청 결과로 받은 Json 형태의 String 문자열 (access_token, refresh_token 등)
     */
    @GetMapping(value = "/{socialLoginType}/callback")
    public BaseResponse<TokenResponse> callback(
            @PathVariable(name = "socialLoginType") SocialLoginType socialLoginType,
            @RequestParam(name = "code") String code) {
        TokenResponse tokenRes=memberService.oauthLogin(code);
        return new BaseResponse<>(tokenRes);
    }
}
