package com.umc.helper.team.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetTeamsResponseList {
    private Long memberId;
    private List<GetTeamsResponse> teamInfos;
}
