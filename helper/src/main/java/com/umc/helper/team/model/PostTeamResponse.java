package com.umc.helper.team.model;

import com.umc.helper.member.Member;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PostTeamResponse {
    private String teamName;
    private List<String> members;
}
