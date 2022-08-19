package com.umc.helper.team;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.helper.config.BaseResponse;
import com.umc.helper.team.model.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    Logger logger= LoggerFactory.getLogger(TeamController.class);
    /**
     * create team
     * @param postTeamReq
     * @return postTeamRes
     */
    @PostMapping("team")
    public BaseResponse<PostTeamResponse> createTeam(@RequestBody PostTeamRequestList postTeamReq){
        ObjectMapper mapper=new ObjectMapper();

        PostTeamResponse postTeamRes=teamService.createTeam(postTeamReq);
        logger.info("postTeamReq - creatorId: {}",postTeamReq.getCreatorId());
        logger.info("postTeamReq - teamName: {}",postTeamReq.getTeamName());
        for(PostTeamRequest member: postTeamReq.getMembers()){
            logger.info("postTeamReq - teamMember:{}",member.getMemberEmail());
        }
        return new BaseResponse<>(postTeamRes);
    }
//    @PostMapping("team")
//    public ResponseEntity<Object> createTeam(@RequestBody  @Valid PostTeamRequestList postTeamReq) throws URISyntaxException {
//        PostTeamResponse postTeamRes=teamService.createTeam(postTeamReq);
//        HttpHeaders httpHeaders=new HttpHeaders();
//        httpHeaders.add("ACCESS_TOKEN","Dfd");
//        URI redirect=new URI("localhost:8080/stomp/team");
//
//        httpHeaders.setLocation(redirect);
//        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
//    }

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

    // 팀 정보 조회
    @GetMapping("team/info/{teamId}")
    public BaseResponse<GetTeamInfoResponse> getTeamInfo(@PathVariable("teamId") Long teamId){
        GetTeamInfoResponse getTeamInfoRes=teamService.getTeamInfo(teamId);

        return new BaseResponse<>(getTeamInfoRes);
    }
}
