package com.umc.helper.member;

import com.umc.helper.config.BaseResponse;
import com.umc.helper.member.model.*;
import com.umc.helper.auth.TokenResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    Logger logger= LoggerFactory.getLogger(MemberController.class);

    /**
     * sign-up
     * @param postMemberRequest
     * @return postMemberRes
     */
    @PostMapping("member/register")
    public BaseResponse<PostMemberResponse> createMember(@RequestBody @Valid PostMemberRequest postMemberRequest){
        PostMemberResponse postMemberRes=memberService.createMember(postMemberRequest);

        return new BaseResponse<>(postMemberRes);
    }

    /**
     * sign-in
     * @param postLoginReq
     * @return tokenRes
     */
    @PostMapping("member/sign-in")
    public BaseResponse<TokenResponse> signIn(@RequestBody @Valid  PostLoginRequest postLoginReq) throws Exception {
        TokenResponse tokenRes=memberService.signIn(postLoginReq);

        return new BaseResponse<>(tokenRes);
    }
    // 비밀번호 찾기 위해 이메일 확인

    /**
     * 비밀번호 재설정을 위한 이메일 확인
     * @param email
     * @return getFindEmailRes
     */
    @GetMapping("member/findPw")
    public BaseResponse<GetFindPasswordResponse> findEmail(@RequestParam String email){
        GetFindPasswordResponse getFindEmailRes=memberService.findPassword(email);

        return new BaseResponse<>(getFindEmailRes);
    }

    /**
     * reset password
     * @param patchPasswordReq
     * @return patchPasswordRes
     */
    @PatchMapping("member/resetPw")
    public BaseResponse<PatchPasswordResponse> resetPassword(@RequestBody @Valid  PatchPasswordRequest patchPasswordReq){
        PatchPasswordResponse patchPasswordRes=memberService.resetPassword(patchPasswordReq);

        return new BaseResponse<>(patchPasswordRes);
    }

    /**
     * edit profile image
     * @param memberId
     * @param profile
     * @return patchMemberProfileRes
     */
    @PatchMapping("member/edit/profile/{memberId}")
    public BaseResponse<PatchMemberInfoResponse> editMemberProfile(@PathVariable("memberId") @Valid  Long memberId,@RequestParam @Valid  MultipartFile profile){
        PatchMemberInfoResponse patchMemberProfileRes=memberService.editProfile(memberId,profile);

        return new BaseResponse<>(patchMemberProfileRes);
    }

    /**
     * edit member name
     * @param memberId
     * @param patchMemberNameReq
     * @return patchMemberNameRes
     */
    @PatchMapping("member/edit/name/{memberId}")
    public BaseResponse<PatchMemberInfoResponse> editMemberName(@PathVariable("memberId") @Valid  Long memberId, @RequestBody @Valid  PatchMemberNameRequest patchMemberNameReq){
        PatchMemberInfoResponse patchMemberNameRes=memberService.editName(memberId, patchMemberNameReq);

        return new BaseResponse<>(patchMemberNameRes);
    }

    /**
     * edit member email
     * @param memberId
     * @param patchMemberEmailReq
     * @return patchMemberEmailRes
     */
    @PatchMapping("member/edit/email/{memberId}")
    public BaseResponse<PatchMemberInfoResponse> editMemberEmail(@PathVariable("memberId") @Valid  Long memberId, @RequestBody @Valid  PatchMemberEmailRequest patchMemberEmailReq){
        PatchMemberInfoResponse patchMemberEmailRes=memberService.editEmail(memberId,patchMemberEmailReq);

        return new BaseResponse<>(patchMemberEmailRes);
    }
    // 로그아웃
    @DeleteMapping("member/logout/{memberId}")
    public BaseResponse<Integer> logout(@PathVariable("memberId") Long memberId){
        int deleteLogoutRes=memberService.logout(memberId);

        return new BaseResponse<>(deleteLogoutRes);
    }

    // 탈퇴
    @DeleteMapping("member/withdraw/{memberId}")
    public BaseResponse<Long> withdraw(@PathVariable("memberId") Long memberId){
        Long memberIdx=memberService.withdraw(memberId);

        return new BaseResponse<>(memberIdx);
    }


}
