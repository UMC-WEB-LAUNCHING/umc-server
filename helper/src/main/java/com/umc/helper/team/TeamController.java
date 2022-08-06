package com.umc.helper.team;

import com.umc.helper.config.BaseResponse;
import com.umc.helper.team.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    /**
     * create team
     * @param postTeamReq
     * @return postTeamRes
     */
    @PostMapping("team")
    public BaseResponse<PostTeamResponse> createTeam(@RequestBody PostTeamRequestList postTeamReq){
        PostTeamResponse postTeamRes=teamService.createTeam(postTeamReq);

        return new BaseResponse<>(postTeamRes);
    }

    /**
     * retrieve teams
     * @param memberId
     * @return getTeamsRes
     */
   @GetMapping("team/{memberId}")
    public BaseResponse<GetTeamsResponse> getTeams(@PathVariable("memberId") Long memberId){
        GetTeamsResponse getTeamsRes=teamService.retrieveTeams(memberId);

        return new BaseResponse<>(getTeamsRes);
   }

    /**
     * invite members
     * @param postTeamInvitationReq
     * @return postTeamInvitationRes
     */
    @PostMapping("team/invite")
    public BaseResponse<PostTeamInvitationResponse> inviteTeamMembers(@RequestBody PostTeamInvitationRequest postTeamInvitationReq){
        PostTeamInvitationResponse postTeamInvitationRes=teamService.inviteTeamMembers(postTeamInvitationReq);

       return new BaseResponse<>(postTeamInvitationRes);
    }


    /**
     * delete team
     * @param teamId
     * @param memberId
     * @return deleteTeamRes
     */
    @DeleteMapping("team/delete/{teamId}/{memberId}")
    public BaseResponse<DeleteTeamResponse> deleteTeam(@PathVariable("teamId") Long teamId, @PathVariable("memberId") Long memberId){
        DeleteTeamResponse deleteTeamRes=teamService.deleteTeam(teamId,memberId);

        return new BaseResponse<>(deleteTeamRes);
    }

    /**
     * exit member from team
     * @param teamId
     * @param memberId
     * @return
     */
    @DeleteMapping("team/exit/{teamId}/{memberId}")
    public BaseResponse<DeleteTeamMemberResponse> deleteMemberFromTeam(@PathVariable("teamId") Long teamId, @PathVariable("memberId") Long memberId){
        DeleteTeamMemberResponse deleteTeamMemberRes=teamService.deleteMemberFromTeam(teamId,memberId);

        return new BaseResponse<>(deleteTeamMemberRes);
    }
}
