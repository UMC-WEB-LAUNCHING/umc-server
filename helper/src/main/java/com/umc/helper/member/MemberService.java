package com.umc.helper.member;

import com.umc.helper.auth.Token;
import com.umc.helper.auth.TokenRepository;
import com.umc.helper.auth.TokenResponse;
import com.umc.helper.auth.TokenUtils;
import com.umc.helper.auth2.JwtTokenProvider;
import com.umc.helper.member.model.*;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;
    private final TokenUtils tokenUtils;
    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    Logger logger= LoggerFactory.getLogger(MemberService.class);
    // 회원 가입
    @Transactional
    public PostMemberResponse createMember(PostMemberRequest postMemberReq){
        postMemberReq.setPassword(passwordEncoder.encode(postMemberReq.getPassword()));
        Member member=new Member();
        member.setPassword(postMemberReq.getPassword());
        member.setEmail(postMemberReq.getEmail());
        member.setUsername(postMemberReq.getName());
        member.setRegisterDateTime(LocalDateTime.now());
        memberRepository.save(member);

        String accessToken=jwtTokenProvider.createAccessToken(member.getEmail());
        String refreshToken=jwtTokenProvider.createRefreshToken(member.getEmail());

        Token token=new Token();
        token.setMember(member);
        token.setRefreshToken(refreshToken);

        tokenRepository.save(token);

        Member m=memberRepository.findByEmail(postMemberReq.getEmail()).get();
        PostMemberResponse postMemberRes=new PostMemberResponse(m.getEmail(),accessToken,refreshToken);

        return postMemberRes;
    }

//    // 로그인
//    @Transactional
//    public TokenResponse signIn(PostLoginRequest postLoginReq){
//        Member member=memberRepository.findByEmail(postLoginReq.getEmail()).get();
//        Token token=tokenRepository.findByMemberId(member.getId()).get();
//
//        String accessToken="";
//        String refreshToken=token.getRefreshToken();
//
//        if (tokenUtils.isValidRefreshToken(refreshToken)) {
//            accessToken = tokenUtils.generateJwtToken(token.getMember());
//           TokenResponse tokenResponse=new TokenResponse(accessToken,token.getRefreshToken());
//
//           return tokenResponse;
//        } else {
//            refreshToken = tokenUtils.saveRefreshToken(member);
//            token.setRefreshToken(refreshToken);
//        }
//        member.setLastLoginDatetime(LocalDateTime.now());
//        return new TokenResponse(accessToken,refreshToken);
//    }

    // 로그인
    @Transactional
    public TokenResponse signIn(PostLoginRequest postLoginReq) throws Exception{
        Member member=memberRepository.findByEmail(postLoginReq.getEmail()).get();
        Token token=tokenRepository.findByMemberId(member.getId()).get();

        if (!passwordEncoder.matches(postLoginReq.getPassword(), member.getPassword())) {
            throw new Exception("비밀번호가 일치하지 않습니다.");
        }

        String accessToken="";
        String refreshToken=token.getRefreshToken();

        if (jwtTokenProvider.isValidRefreshToken(refreshToken)) {
            accessToken = jwtTokenProvider.createAccessToken(member.getEmail()); //Access Token 새로 만들어서 줌

            return TokenResponse.builder()
                    .ACCESS_TOKEN(accessToken)
                    .REFRESH_TOKEN(refreshToken)
                    .build();
        } else {
            //둘 다 새로 발급
            accessToken = jwtTokenProvider.createAccessToken(member.getEmail());
            refreshToken = jwtTokenProvider.createRefreshToken(member.getEmail());
            token.setRefreshToken(refreshToken);   //DB Refresh 토큰 갱신
        }

        return TokenResponse.builder()
                .ACCESS_TOKEN(accessToken)
                .REFRESH_TOKEN(refreshToken)
                .build();
    }
    // refresh token으로 access token 재발급
    public TokenResponse issueAccessToken(HttpServletRequest request){
        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);
        System.out.println("accessToken = " + accessToken);
        System.out.println("refreshToken = " + refreshToken);
        //accessToken이 만료됐고 refreshToken이 맞으면 accessToken을 새로 발급(refreshToken의 내용을 통해서)
        if(!jwtTokenProvider.isValidAccessToken(accessToken)){  //클라이언트에서 토큰 재발급 api로의 요청을 확정해주면 이 조건문은 필요없다.
            System.out.println("Access 토큰 만료됨");
            if(jwtTokenProvider.isValidRefreshToken(refreshToken)){     //들어온 Refresh 토큰이 유효한지
                System.out.println("Refresh 토큰은 유효함");
                Claims claimsToken = jwtTokenProvider.getClaimsToken(refreshToken);
                String email = (String)claimsToken.get("email");
                Optional<Member> member = memberRepository.findByEmail(email);
                String tokenFromDB = tokenRepository.findByMemberId(member.get().getId()).get().getRefreshToken();
                System.out.println("tokenFromDB = " + tokenFromDB);
                if(refreshToken.equals(tokenFromDB)) {   //DB의 refresh토큰과 지금들어온 토큰이 같은지 확인
                    System.out.println("Access 토큰 재발급 완료");
                    accessToken = jwtTokenProvider.createAccessToken(email);
                }
                else{
                    //DB의 Refresh토큰과 들어온 Refresh토큰이 다르면 중간에 변조된 것임
                    System.out.println("Refresh Token Tampered");
                    //예외발생
                }
            }
            else{
                //입력으로 들어온 Refresh 토큰이 유효하지 않음
            }
        }
        return TokenResponse.builder()
                .ACCESS_TOKEN(accessToken)
                .REFRESH_TOKEN(refreshToken)
                .build();
    }
    // 소셜 로그인

    // 비밀번호 재설정 위해 이메일 찾기
    @Transactional
    public GetFindPasswordResponse findPassword(String email){
        Optional<Member> member=memberRepository.findByEmail(email);
        GetFindPasswordResponse result;
        if(member.isEmpty()){
            result=new GetFindPasswordResponse("이메일 오류"); //TODO: 예외처리로 변경 필요
        }
        else{
            result=new GetFindPasswordResponse(member.get().getEmail());
        }
        return result;

    }

    // 비밀번호 재설정
    @Transactional
    public PatchPasswordResponse resetPassword(PatchPasswordRequest patchPasswordReq){
        patchPasswordReq.setPassword(passwordEncoder.encode(patchPasswordReq.getPassword()));
        Member member=memberRepository.findByEmail(patchPasswordReq.getEmail()).get();
        member.setPassword(patchPasswordReq.getPassword());

        PatchPasswordResponse result=new PatchPasswordResponse(member.getEmail());

        return result;
    }


}
