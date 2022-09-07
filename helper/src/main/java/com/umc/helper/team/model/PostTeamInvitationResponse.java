package com.umc.helper.team.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostTeamInvitationResponse {
    private Long teamId;
    private String teamName;
    private List<MemberInfo> memberInfoList;
}
