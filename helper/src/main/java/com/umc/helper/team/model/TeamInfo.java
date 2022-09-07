package com.umc.helper.team.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamInfo {

    @JsonProperty
    private Long teamId;
    @JsonProperty
    private String teamName;
    @JsonProperty
    private List<MemberInfo> memberInfoList;

}

