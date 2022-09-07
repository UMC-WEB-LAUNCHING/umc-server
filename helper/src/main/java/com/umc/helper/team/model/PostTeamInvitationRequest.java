package com.umc.helper.team.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostTeamInvitationRequest {
    @Valid
    private Long teamId;
    @Valid
    private List<String> memberEmail;
}
