package com.umc.helper.auth.oauth;

import com.umc.helper.member.model.Member;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
// access token으로 구글 사용자 정보를 얻는데, 그 사용자 정보에 대한 클래스
public class GoogleUser {

    public String id;
    public String email;
    public Boolean verifiedEmail;
    public String name;
    public String givenName;
    public String familyName;
    public String picture;
    public String locale;
    public String password;

    public Member toUserSignUp() {
        return new Member(id,email, name,"google", LocalDateTime.now());
    }
}
