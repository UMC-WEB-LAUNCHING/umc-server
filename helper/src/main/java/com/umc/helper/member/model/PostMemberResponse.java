package com.umc.helper.member.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostMemberResponse {

    String email;
    String accessToken;
    String refreshToken;
}
