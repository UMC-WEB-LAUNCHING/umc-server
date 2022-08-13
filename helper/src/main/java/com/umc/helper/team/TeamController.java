package com.umc.helper.team;

import com.umc.helper.config.BaseResponse;
import com.umc.helper.team.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

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
    public BaseResponse<PostTeamResponse> createTeam(@RequestBody  @Valid PostTeamRequestList postTeamReq){
        PostTeamResponse postTeamRes=teamService.createTeam(postTeamReq);

        return new BaseResponse<>(postTeamRes);
    }

    /**
     * retrieve teams
     * @param memberId
     * @return getTeamsRes
     */
   @GetMapping("team/{memberId}")
    public BaseResponse<GetTeamsResponse> getTeams(@PathVariable("memberId") @Valid  Long memberId){
        GetTeamsResponse getTeamsRes=teamService.retrieveTeams(memberId);

        return new BaseResponse<>(getTeamsRes);
   }

    /**
     * invite members
     * @param postTeamInvitationReq
     * @return postTeamInvitationRes
     */
    @PostMapping("team/invite")
    public BaseResponse<PostTeamInvitationResponse> inviteTeamMembers(@RequestBody @Valid  PostTeamInvitationRequest postTeamInvitationReq){
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
    public BaseResponse<DeleteTeamResponse> deleteTeam(@PathVariable("teamId")  @Valid Long teamId, @PathVariable("memberId") @Valid  Long memberId){
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
    public BaseResponse<DeleteTeamMemberResponse> deleteMemberFromTeam(@PathVariable("teamId") @Valid  Long teamId, @PathVariable("memberId") @Valid  Long memberId){
        DeleteTeamMemberResponse deleteTeamMemberRes=teamService.deleteMemberFromTeam(teamId,memberId);

        return new BaseResponse<>(deleteTeamMemberRes);
    }

    // 팀 프로필 사진 변경
    @PatchMapping("team/edit/profile/{teamId}")
    public BaseResponse<PatchTeamInfoResponse> editTeamProfile(@PathVariable("teamId") @Valid Long teamId, @RequestParam @Valid MultipartFile profile){
        PatchTeamInfoResponse patchTeamInfoRes=teamService.editProfile(teamId,profile);

        return new BaseResponse<>(patchTeamInfoRes);
    }

    // 팀 이름 변경
    @PatchMapping("team/edit/name/{teamId}")
    public BaseResponse<PatchTeamInfoResponse> editTeamName(@PathVariable("teamId") @Valid Long teamId,@RequestBody @Valid PatchTeamNameRequest patchTeamNameReq){
        PatchTeamInfoResponse patchTeamInfoRes=teamService.editName(teamId,patchTeamNameReq);

        return new BaseResponse<>(patchTeamInfoRes);
    }
}
