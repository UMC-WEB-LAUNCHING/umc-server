package com.umc.helper.member;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.umc.helper.auth.Token;
import com.umc.helper.auth.TokenRepository;
import com.umc.helper.auth.TokenResponse;
import com.umc.helper.auth.JwtTokenProvider;
import com.umc.helper.auth.exception.RefreshTokenNotFound;
import com.umc.helper.auth.oauth.GoogleOauth;
import com.umc.helper.auth.oauth.GoogleUser;
import com.umc.helper.auth.oauth.OAuthToken;
import com.umc.helper.file.FileRepository;
import com.umc.helper.folder.FolderRepository;
import com.umc.helper.link.LinkRepository;
import com.umc.helper.member.exception.EmailDuplicateException;
import com.umc.helper.member.exception.MemberEmailException;
import com.umc.helper.member.exception.MemberPasswordException;
import com.umc.helper.member.model.*;
import com.umc.helper.memo.MemoRepository;
import com.umc.helper.team.TeamMemberRepository;
import com.umc.helper.team.TeamRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;
    private final GoogleOauth googleOauthService;

    private final AmazonS3Client amazonS3Client;

    private final LinkRepository linkRepository;
    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;
    private final MemoRepository memoRepository;
    private final TeamMemberRepository teamMemberRepository;

    private final TeamRepository teamRepository;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    Logger logger= LoggerFactory.getLogger(MemberService.class);


    // 회원 가입
    @Transactional
    public PostMemberResponse createMember(PostMemberRequest postMemberReq){

        // 이메일 입력 여부 확인
        postMemberReq.isPresentEmail();
        // 이름 입력 여부 확인
        postMemberReq.isPresentName();
        // 비밀번호 입력 여부 확인
        postMemberReq.isPresentPassword();

        // 이메일 중복 확인
        if(memberRepository.findByEmail(postMemberReq.getEmail()).isPresent()){
            throw new EmailDuplicateException();
        }

        postMemberReq.setPassword(passwordEncoder.encode(postMemberReq.getPassword()));
        Member member=new Member();
        member.setPassword(postMemberReq.getPassword());
        member.setEmail(postMemberReq.getEmail());
        member.setUsername(postMemberReq.getName());
        member.setRegisterDateTime(LocalDateTime.now());
        memberRepository.save(member);

//        String accessToken=jwtTokenProvider.createAccessToken(member.getEmail());
//        String refreshToken=jwtTokenProvider.createRefreshToken(member.getEmail());

        String accessToken=null;
        String refreshToken=null;

        Token token=new Token();
        token.setMember(member);
        token.setRefreshToken(refreshToken);

        tokenRepository.save(token);

        Member m=memberRepository.findByEmail(postMemberReq.getEmail()).get();
        PostMemberResponse postMemberRes=new PostMemberResponse(m.getEmail(),accessToken,refreshToken);

        return postMemberRes;
    }

    // 로그인 - jwt 적용 X
   @Transactional
    public TokenResponse signIn(PostLoginRequest postLoginReq){
        Optional<Member> member=memberRepository.findByEmail(postLoginReq.getEmail());

        // 이메일 확인 - 존재하지 않는 이메일
        if(member.isEmpty()){
            throw new MemberEmailException();
        }
        // 비밀번호 확인 - 비밀번호 불일치
        if (!passwordEncoder.matches(postLoginReq.getPassword(), member.get().getPassword())) {
            throw new MemberPasswordException();
        }

        member.get().setLastLoginDatetime(LocalDateTime.now());
        return TokenResponse.builder()
                .ACCESS_TOKEN(null)
                .REFRESH_TOKEN(null)
                .memberId(member.get().getId())
                .memberName(member.get().getUsername())
                .profile(member.get().getProfileImage())
                .build();

    }

    // 로그인 - jwt 적용
//    @Transactional
//    public TokenResponse signIn(PostLoginRequest postLoginReq) throws Exception{
//        Member member=memberRepository.findByEmail(postLoginReq.getEmail()).get();
//        Token token=tokenRepository.findByMemberId(member.getId());
//        logger.info("signIn- refresh token: {}",token.getRefreshToken());
//
//        if (!passwordEncoder.matches(postLoginReq.getPassword(), member.getPassword())) {
//            throw new Exception("비밀번호가 일치하지 않습니다.");
//        }
//
//        String accessToken="";
//        String refreshToken=token.getRefreshToken();
//
//        if (jwtTokenProvider.isValidRefreshToken(refreshToken)) {
//            accessToken = jwtTokenProvider.createAccessToken(member.getEmail()); //Access Token 새로 만들어서 줌
//            logger.info(">MemberService: isValidRefreshToken");
//            return TokenResponse.builder()
//                    .ACCESS_TOKEN(accessToken)
//                    .REFRESH_TOKEN(refreshToken)
//                    .build();
//        } else {
//            //둘 다 새로 발급
//            logger.info(">MemberService: 둘 다 새로 발급");
//            accessToken = jwtTokenProvider.createAccessToken(member.getEmail());
//            refreshToken = jwtTokenProvider.createRefreshToken(member.getEmail());
//            token.setRefreshToken(refreshToken);   //DB Refresh 토큰 갱신
//        }
//
//        return TokenResponse.builder()
//                .ACCESS_TOKEN(accessToken)
//                .REFRESH_TOKEN(refreshToken)
//                .build();
//    }

    // refresh token으로 access token 재발급
    public TokenResponse issueAccessToken(HttpServletRequest request){
        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);
        logger.info("accessToken = {}",accessToken);
        logger.info("refreshToken = {}",refreshToken);

        //accessToken이 만료됐고 refreshToken이 맞으면 accessToken을 새로 발급(refreshToken의 내용을 통해서)
        if(!jwtTokenProvider.isValidAccessToken(accessToken)){  //클라이언트에서 토큰 재발급 api로의 요청을 확정해주면 이 조건문은 필요없다.
            logger.info("Expired Access Token");
            if(jwtTokenProvider.isValidRefreshToken(refreshToken)){     //들어온 Refresh 토큰이 유효한지
                logger.info("Valid Refresh Token");
                Claims claimsToken = jwtTokenProvider.getClaimsToken(refreshToken);
                String email = (String)claimsToken.get("email");
                Optional<Member> member = memberRepository.findByEmail(email);
                String tokenFromDB = tokenRepository.findByMemberId(member.get().getId()).getRefreshToken();
                logger.info("refresh token from DB: {}",tokenFromDB);
                if(refreshToken.equals(tokenFromDB)) {   //DB의 refresh토큰과 지금들어온 토큰이 같은지 확인
                    logger.info("reissue access token");
                    accessToken = jwtTokenProvider.createAccessToken(email);

                }
                else{
                    //DB의 Refresh토큰과 들어온 Refresh토큰이 다르면 중간에 변조된 것임
                    logger.error("Refresh Token Tampered");
                    throw new RefreshTokenNotFound();
                }
            }
            else{
                //입력으로 들어온 Refresh 토큰이 유효하지 않음
                logger.error("Invalid Refresh Token");
                throw new RefreshTokenNotFound();
            }
        }
        return TokenResponse.builder()
                .ACCESS_TOKEN(accessToken)
                .REFRESH_TOKEN(refreshToken)
                .build();
    }

    // 비밀번호 재설정 위해 이메일 찾기
    @Transactional
    public GetFindPasswordResponse findPassword(String email){
        Optional<Member> member=memberRepository.findByEmail(email);

        // 이메일 존재 확인
        if(member.isEmpty()){
            throw new MemberEmailException();
        }

        GetFindPasswordResponse result=new GetFindPasswordResponse(member.get().getEmail());

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

    // 회원 프로필 수정
    @Transactional
    public PatchMemberInfoResponse editProfile(Long memberId,MultipartFile profile){
        Member member=memberRepository.findById(memberId).get();

        // TODO: 회원 프로필 사진이 존재하면 s3에서 기존의 프로필 사진 삭제
//        if(member.getProfileImage()!=null){
//            String key="profiles/"+member.getProfileImage();
//            amazonS3Client.deleteObject(bucket,key);
//        }

        ObjectMetadata objectMetadata=new ObjectMetadata();
        objectMetadata.setContentType(profile.getContentType());
        objectMetadata.setContentLength(profile.getSize());

        String originalFileName= profile.getOriginalFilename();
        int index=originalFileName.lastIndexOf(".");
        String ext= originalFileName.substring(index+1); // 확장자

        String storeFileName= UUID.randomUUID()+"."+ext; // 저장되는 이름
        String key="profiles/"+storeFileName;

        try (InputStream inputStream = profile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, key, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String storeFileUrl = amazonS3Client.getUrl(bucket, key).toString();

        member.setProfileImage(storeFileUrl);

        return new PatchMemberInfoResponse(member.getId(),member.getUsername(),member.getEmail(),member.getProfileImage());
    }

    // 회원 사용자명 수정
    @Transactional
    public PatchMemberInfoResponse editName(Long memberId, PatchMemberNameRequest patchMemberNameReq){
        Member member=memberRepository.findById(memberId).get();

        member.setUsername(patchMemberNameReq.getName());

        return new PatchMemberInfoResponse(member.getId(),member.getUsername(),member.getEmail(),member.getProfileImage());

    }
    // 회원 이메일 수정
    @Transactional
    public PatchMemberInfoResponse editEmail(Long memberId,PatchMemberEmailRequest patchMemberEmailReq){
        Member member=memberRepository.findById(memberId).get();

        member.setEmail(patchMemberEmailReq.getEmail());

        return new PatchMemberInfoResponse(member.getId(),member.getUsername(),member.getEmail(),member.getProfileImage());

    }

    /**
     * 로그아웃
     * TODO: 수정필요
     */
    @Transactional
    public int logout(Long memberId){
        int count=tokenRepository.removeTokenByMemberId(memberId);

        return count;
    }

    /**
     * 소셜 로그인
     */
    public TokenResponse oauthLogin(String code)  {
        ResponseEntity<String> accessTokenResponse = googleOauthService.requestAccessToken(code);
        OAuthToken oAuthToken = googleOauthService.getAccessToken(accessTokenResponse);
        logger.info("Access Token: {}", oAuthToken.getAccessToken());

        ResponseEntity<String> userInfoResponse = googleOauthService.createGetRequest(oAuthToken);
        logger.info("userInfoResponse: {}",userInfoResponse);
        GoogleUser googleUser = googleOauthService.getUserInfo(userInfoResponse);
        logger.info("Google User Name: {}", googleUser.getName());
        logger.info("Google User Email: {}", googleUser.getEmail());

        String jwtAccessToken=jwtTokenProvider.createAccessToken(googleUser.getEmail());
        String jwtRefreshToken=jwtTokenProvider.createRefreshToken(googleUser.getEmail());

        if (!isJoinedUser(googleUser)) {
            signUp(googleUser, jwtRefreshToken);
        }

        Member member = memberRepository.findByEmail(googleUser.getEmail()).orElseThrow(IllegalArgumentException::new);
        member.setLastLoginDatetime(LocalDateTime.now());

        //logger.info("accessToken:{}",jwtAccessToken);
        TokenResponse tokenResponse=new TokenResponse(jwtAccessToken,jwtRefreshToken,member.getId(),member.getUsername(),member.getProfileImage());

        return tokenResponse;
    }

    private boolean isJoinedUser(GoogleUser googleUser) {
        Optional<Member> member = memberRepository.findByEmail(googleUser.getEmail());
        logger.info("Joined User: {}", member);
        return member.isPresent();
    }

    private void signUp(GoogleUser googleUser, String refreshToken) {

        Member member = googleUser.toUserSignUp();
        memberRepository.save(member);

        Token token=new Token();
        token.setMember(member);
        token.setRefreshToken(refreshToken);
        tokenRepository.save(token);


    }

    // 탈퇴 TODO: team, link, folder, file, memo, bookmark, trash, teamMember 다 삭제
    @Transactional
    public Long withdraw(Long memberId){
        Member member=memberRepository.findById(memberId).get();
        logger.info("tokenId: {}",tokenRepository.findByMemberId(member.getId()).getId());

//        linkRepository.removeByMemberId(member.getId());
//        fileRepository.removeByMemberId(member.getId());
//        memoRepository.removeByMemberId(member.getId());
//        folderRepository.removeByMemberId(member.getId());
//        teamMemberRepository.removeTeamMemberByMemberId(member.getId());
//        tokenRepository.removeTokenByMemberId(member.getId());
//        memberRepository.delete(member);

        return member.getId();
    }
}
