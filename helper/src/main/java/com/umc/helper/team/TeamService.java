package com.umc.helper.team;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.umc.helper.file.FileRepository;
import com.umc.helper.folder.FolderRepository;
import com.umc.helper.folder.model.Folder;
import com.umc.helper.image.ImageRepository;
import com.umc.helper.link.LinkRepository;
import com.umc.helper.member.exception.MemberNotFoundException;
import com.umc.helper.member.model.Member;
import com.umc.helper.member.MemberRepository;
import com.umc.helper.member.model.PatchMemberInfoResponse;
import com.umc.helper.member.model.PatchMemberNameRequest;
import com.umc.helper.memo.MemoRepository;
import com.umc.helper.team.exception.InvalidDeleteTeam;
import com.umc.helper.team.exception.TeamNotFoundException;
import com.umc.helper.team.model.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamService {

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final FolderRepository folderRepository;
    private final FileRepository fileRepository;
    private final ImageRepository imageRepository;
    private final MemoRepository memoRepository;
    private final LinkRepository linkRepository;

    private Logger logger= LoggerFactory.getLogger(TeamService.class);

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * 팀 생성
     */
    @Transactional
    public PostTeamResponse createTeam(PostTeamRequestList postTeamReq){
        Team team=new Team();
        team.setName(postTeamReq.getTeamName());

        Optional<Member> creator=memberRepository.findById(postTeamReq.getCreatorId());
        if(creator.isEmpty()){
            throw new MemberNotFoundException();
        }

        List<TeamMember> members=new ArrayList<>();
        List<String> memberNames=new ArrayList<>();

        for(PostTeamRequest member: postTeamReq.getMembers()){
            if(memberRepository.findByEmail(member.getMemberEmail()).isEmpty()){
                throw new MemberNotFoundException();
            }
            TeamMember teamMember=new TeamMember();
            teamMember.setMember(memberRepository.findByEmail(member.getMemberEmail()).get());
            teamMember.setTeam(team);
            members.add(teamMember);

            memberNames.add(memberRepository.findByEmail(member.getMemberEmail()).get().getUsername());
        }
        team.setMembers(members);
        team.setCreatedDate(LocalDateTime.now());
        team.setCreator(creator.get());

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
        //TODO: 예외처리
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
        if(teamRepository.findById(postTeamInvitationReq.getTeamId())==null){
            throw new TeamNotFoundException();
        }
        Team team=teamRepository.findById(postTeamInvitationReq.getTeamId());
        List<String> invitedMembers=postTeamInvitationReq.getMemberEmail();

        if(team!=null){
            for(String invitedMember: invitedMembers){
                if(memberRepository.findByEmail(invitedMember).isEmpty()){
                    throw new MemberNotFoundException();
                }
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
    public DeleteTeamResponse deleteTeam(Long teamId,Long deletingId){

        Team team=teamRepository.findById(teamId);
        team.notExistTeam(); // 팀 존재 확인

        if(team.getCreator().getId()!=deletingId){ // 팀 생성자와 팀 삭제 시도 유저 idx 비교
            throw new InvalidDeleteTeam();
        }

        teamMemberRepository.removeTeamMemberByTeamId(teamId);
        teamRepository.removeTeamByTeamId(teamId);

        List<Folder> folders=folderRepository.findEveryByTeamId(teamId); // 팀 폴더 조회
        for(Folder folder :folders) {                    // 폴더 내 모든 데이터 삭제
            fileRepository.removeEveryByFolderId(folder.getId());
            imageRepository.removeEveryByFolderId(folder.getId());
            memoRepository.removeEveryByFolderId(folder.getId());
            linkRepository.removeEveryByFolderId(folder.getId());
        }

        folderRepository.removeFolderByTeamId(teamId); // 팀 폴더 모두 삭제


        return new DeleteTeamResponse(teamId);
    }

    /**
     *  팀에서 팀원 퇴장 TODO: 팀에서 모든 팀원 나가면 팀 제거되게
     */
    @Transactional
    public DeleteTeamMemberResponse deleteMemberFromTeam(Long teamId,Long memberId){
        Team team=teamRepository.findById(teamId);
        if(team==null){
            throw new TeamNotFoundException();
        }

        // 유저 존재 확인
        Optional<Member> member=memberRepository.findById(memberId);
        if(member.isEmpty()) {
            throw new MemberNotFoundException();
        }


        teamMemberRepository.removeTeamMemberByMemberTeamId(memberId,teamId);

        //팀에서 모든 팀원 나가면 팀 제거
        if(teamMemberRepository.findTeamMembersByTeamId(teamId).size()==0){
            teamRepository.removeTeamByTeamId(teamId);
        }

        return new DeleteTeamMemberResponse(memberId,teamId);
    }

    // 팀 프로필 사진 변경
    //TODO: 팀 생성자만 수정할 수 있게 할 것인지, 모든 팀원이 수정가능할지
    @Transactional
    public PatchTeamInfoResponse editProfile(Long teamId, MultipartFile profile){
        Team team=teamRepository.findById(teamId);

        // TODO: 팀 프로필 사진이 존재하면 s3에서 기존의 프로필 사진 삭제
//        if(team.getProfileImage()!=null){
//            String key="profiles/"+team.getProfileImage();
//            String storeFileUrl = amazonS3Client.getUrl(bucket, key).toString();
//            amazonS3Client.deleteObject(bucket,storeFileUrl);
//        }

        ObjectMetadata objectMetadata=new ObjectMetadata();
        objectMetadata.setContentType(profile.getContentType());
        objectMetadata.setContentLength(profile.getSize());

        String originalFileName= profile.getOriginalFilename();
        int index=originalFileName.lastIndexOf(".");
        String ext= originalFileName.substring(index+1); // 확장자

        String storeFileName= UUID.randomUUID()+"."+ext; // 저장되는 이름
        String key="profiles/"+storeFileName;

        try (InputStream inputStream = profile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, key, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String storeFileUrl = amazonS3Client.getUrl(bucket, key).toString();

        team.setProfileImage(storeFileUrl);

        return new PatchTeamInfoResponse(team.getTeamIdx(),team.getName(),team.getProfileImage());
    }

    //  팀 이름 수정
    //TODO: 팀 생성자만 수정할 수 있게 할 것인지, 모든 팀원이 수정가능할지
    @Transactional
    public PatchTeamInfoResponse editName(Long teamId, PatchTeamNameRequest patchTeamNameReq){
        Team team=teamRepository.findById(teamId);

        team.setName(patchTeamNameReq.getName());

        return new PatchTeamInfoResponse(team.getTeamIdx(),team.getName(),team.getProfileImage());

    }

    // 팀 정보 조회
    @Transactional
    public GetTeamInfoResponse getTeamInfo(Long teamId){
        Team team=teamRepository.findById(teamId);
        List<TeamMember> teamMembers=teamMemberRepository.findTeamMembersByTeamId(teamId);

        List<TeamMemberInfo> teamMemberInfos=new ArrayList<>();
        for(TeamMember teamMember:teamMembers){
            Member member=memberRepository.findById(teamMember.getMember().getId()).get();
            teamMemberInfos.add(new TeamMemberInfo(member.getId(),member.getUsername(),member.getEmail()));
        }

        Member creator=memberRepository.findById(team.getCreator().getId()).get();

        return new GetTeamInfoResponse(team.getProfileImage(),team.getName(),team.getTeamIdx(),creator.getId(),creator.getUsername(),teamMemberInfos);

    }
}
