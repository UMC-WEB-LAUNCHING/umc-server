package com.umc.helper.team.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class PostTeamRequestList {
    @Valid
    private Long creatorId;
    @Valid
    private String teamName;
    @Valid
    private List<PostTeamRequest> members;
}
