package com.umc.helper.auth.oauth;

import lombok.Getter;

@Getter
// 일회성 코드로 받아온 oauth token
public class OAuthToken {

    private String accessToken;
    private String expiresIn;
    private String idToken;
    private String refreshToken;
    private String scope;
    private String tokenType;
}
