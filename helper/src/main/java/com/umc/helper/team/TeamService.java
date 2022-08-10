package com.umc.helper.team;

import com.umc.helper.member.model.Member;
import com.umc.helper.member.MemberRepository;
import com.umc.helper.team.model.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamService {

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    private Logger logger= LoggerFactory.getLogger(TeamService.class);

    /**
     * 팀 생성
     */
    @Transactional
    public PostTeamResponse createTeam(PostTeamRequestList postTeamReq){
        Team team=new Team();
        team.setName(postTeamReq.getTeamName());

        Member creator=memberRepository.findById(postTeamReq.getCreatorId()).get();

        List<TeamMember> members=new ArrayList<>();
        List<String> memberNames=new ArrayList<>();

        for(PostTeamRequest member: postTeamReq.getMembers()){
            TeamMember teamMember=new TeamMember();
            teamMember.setMember(memberRepository.findById(member.getMemberId()).get());
            teamMember.setTeam(team);
            members.add(teamMember);

            memberNames.add(memberRepository.findById(member.getMemberId()).get().getUsername());
        }
        team.setMembers(members);
        team.setCreatedDate(LocalDateTime.now());
        team.setCreator(creator);

        teamRepository.save(team);

        PostTeamResponse createdTeam=new PostTeamResponse(teamRepository.findById(team.getTeamIdx()).getName(),memberNames);

        return createdTeam;
    }

    /**
     *  팀 조회
     */
    @Transactional
    public GetTeamsResponse retrieveTeams(Long memberId){
        //TODO: query 리팩토링 하면 코드 바꿀 수 있을듯
        String memberName=memberRepository.findById(memberId).get().getUsername();
        List<TeamMember> teams= teamMemberRepository.findTeamMemberByMemberId(memberId); // memberid:3 - team_id: 4,5,20
        List<TeamInfo> teamInfoList = new ArrayList<>();

        for(TeamMember team:teams) {
            List<MemberInfo> memberInfoList = new ArrayList<>();

            Team getTeam = teamRepository.findById(team.getTeam().getTeamIdx());// team_id: 4,5,20
            List<TeamMember> members = teamMemberRepository.findTeamMembersByTeamId(getTeam.getTeamIdx()); // team_id:4 - member_id:1,2,3 / team_id:5 - member_id:3,11 / team_id:20 - member_id:1,2,3,11

            for (TeamMember member : members) {
                Member getMember = memberRepository.findById(member.getMember().getId()).get();
                memberInfoList.add(new MemberInfo(getMember.getId(), getMember.getUsername()));
            }
            teamInfoList.add(new TeamInfo(getTeam.getTeamIdx(), getTeam.getName(), memberInfoList));
        }
        return new GetTeamsResponse(memberName,memberId,teamInfoList);
    }

    /**
     * 팀원 초대
     */
    @Transactional
    public PostTeamInvitationResponse inviteTeamMembers(PostTeamInvitationRequest postTeamInvitationReq){

        Team team=teamRepository.findById(postTeamInvitationReq.getTeamId());
        List<String> invitedMembers=postTeamInvitationReq.getMemberEmail();

        if(team!=null){
            for(String invitedMember: invitedMembers){
                Member member=memberRepository.findByEmail(invitedMember).get();
                TeamMember teamMember=new TeamMember();
                teamMember.setTeam(team);
                teamMember.setMember(member);
                teamMemberRepository.save(teamMember);
            }
        }

        List<TeamMember> members=teamMemberRepository.findTeamMembersByTeamId(team.getTeamIdx());
        List<MemberInfo> memberInfoList=new ArrayList<>();
        for(TeamMember member:members){
            Member getMember=memberRepository.findById(member.getMember().getId()).get();
            memberInfoList.add(new MemberInfo(getMember.getId(),getMember.getUsername()));
        }

        return new PostTeamInvitationResponse(team.getTeamIdx(), team.getName(),memberInfoList);
    }

    /**
     * 팀 삭제 TODO: 팀이 가지고 있던 폴더, 데이터 다 삭제
     */
    @Transactional
    public DeleteTeamResponse deleteTeam(Long teamId,Long creatorId){

//        List<TeamMember> teamMembers=teamMemberRepository.findTeamMembersByTeamId(teamId);
        teamMemberRepository.removeTeamMemberByTeamId(teamId);
//        for(TeamMember teamMember:teamMembers){
//            teamMemberRepository.removeTeamMemberByTeamId(teamMember.getTeam().getTeamIdx());
//        }

//        Team team=teamRepository.findById(teamId);
        teamRepository.removeTeamByTeamId(teamId);

        return new DeleteTeamResponse(teamId);
    }

    /**
     *  팀에서 팀원 퇴장 TODO: 팀에서 모든 팀원 나가면 팀 제거되게
     */
    @Transactional
    public DeleteTeamMemberResponse deleteMemberFromTeam(Long teamId,Long memberId){
        teamMemberRepository.removeTeamMemberByMemberTeamId(memberId,teamId);

        return new DeleteTeamMemberResponse(memberId,teamId);
    }
}
