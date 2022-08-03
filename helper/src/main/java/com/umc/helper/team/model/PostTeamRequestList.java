package com.umc.helper.team.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class PostTeamRequestList {
    private Long creatorId;
    private String teamName;
    private List<PostTeamRequest> members;
}
