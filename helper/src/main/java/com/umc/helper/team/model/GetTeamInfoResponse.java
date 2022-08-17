package com.umc.helper.team.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetTeamInfoResponse {

    private String profileImage;
    private String teamName;
    private Long teamId;
    private Long creatorId;
    private String creatorName;
    private List<TeamMemberInfo> teamMembers;
}
