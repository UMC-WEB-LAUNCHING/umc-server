package com.umc.helper.auth;

import com.umc.helper.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtTokenInterceptor2 implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    Logger logger= LoggerFactory.getLogger(JwtTokenInterceptor2.class);

    // preHandle -> 컨트롤러의 메서드에 매핑된 특정 URI가 호출됐을 때 실행되는 메서드, 컨트롤러를 접근하기 직전에 실행되는 메서드.
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        logger.info("JwtToken 호출");
        String accessToken = request.getHeader("ACCESS_TOKEN");
        logger.info("AccessToken: {}",accessToken);
        String refreshToken = request.getHeader("REFRESH_TOKEN");
        logger.info("RefreshToken: {}",refreshToken);


        if (accessToken != null && jwtTokenProvider.isValidAccessToken(accessToken)) {
            logger.info(">JwtTokenInterceptor2 - isValidAccessToken");
            return true;
        }

        // access token은 유효하지 않고, refresh token은 유효한 경우
        else if(!jwtTokenProvider.isValidAccessToken(accessToken)&&jwtTokenProvider.isValidRefreshToken(refreshToken)){
            logger.info(">JwtTokenInterceptor2 - invalid AccessToken && valid RefreshToken");
            TokenResponse tokenResponse=memberService.issueAccessToken(request);
            response.setHeader("ACCESS_TOKEN",tokenResponse.getACCESS_TOKEN());
            response.setHeader("REFRESH_TOKEN",tokenResponse.getREFRESH_TOKEN());
            response.setHeader("msg", "Reissue access token.");
            return true;
        }

        response.setStatus(401);
        response.setHeader("ACCESS_TOKEN", accessToken);
        response.setHeader("REFRESH_TOKEN", refreshToken);
        response.setHeader("msg", "Check the tokens.");

        return false;
    }
}
