package com.umc.helper.member.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatchMemberInfoResponse {
    private Long memberId;
    private String name;
    private String email;
    private String profile;
}
