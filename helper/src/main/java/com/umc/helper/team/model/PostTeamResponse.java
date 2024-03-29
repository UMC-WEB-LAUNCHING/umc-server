package com.umc.helper.team.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PostTeamResponse {
    private Long creatorId;
    private String teamName;
    private List<String> members;
}
