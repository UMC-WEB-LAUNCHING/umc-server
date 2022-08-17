package com.umc.helper.mainpage;

import com.amazonaws.services.clouddirectory.model.GetTypedLinkFacetInformationRequest;
import com.umc.helper.file.FileRepository;
import com.umc.helper.file.FileService;
import com.umc.helper.file.model.GetFilesResponse;
import com.umc.helper.folder.FolderRepository;
import com.umc.helper.folder.FolderService;
import com.umc.helper.folder.model.Folder;
import com.umc.helper.folder.model.GetFoldersResponse;
import com.umc.helper.image.ImageRepository;
import com.umc.helper.image.ImageService;
import com.umc.helper.image.model.GetImagesResponse;
import com.umc.helper.link.LinkRepository;
import com.umc.helper.link.LinkService;
import com.umc.helper.link.model.GetLinksResponse;
import com.umc.helper.mainpage.model.GetItemResponse;
import com.umc.helper.mainpage.model.GetMainPageResponse;
import com.umc.helper.member.model.Member;
import com.umc.helper.member.MemberRepository;
import com.umc.helper.memo.MemoRepository;
import com.umc.helper.memo.MemoService;
import com.umc.helper.memo.model.GetMemosResponse;
import com.umc.helper.team.TeamMemberRepository;
import com.umc.helper.team.TeamRepository;
import com.umc.helper.team.TeamService;
import com.umc.helper.team.model.GetTeamsResponse;
import com.umc.helper.team.model.TeamInfo;
import com.umc.helper.team.model.TeamMember;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MainPageService {

    private final TeamService teamService;
    private final MemberRepository memberRepository;
    private final FolderService folderService;
    private final FileService fileService;
    private final LinkService linkService;
    private final ImageService imageService;
    private final MemoService memoService;
    private final FolderRepository folderRepository;
    private final FileRepository fileRepository;
    private final ImageRepository imageRepository;
    private final LinkRepository linkRepository;
    private final MemoRepository memoRepository;
    private final TeamMemberRepository teamMemberRepository;

    Logger logger= LoggerFactory.getLogger(MainPageService.class);

//    public GetMainPageResponse getMainPage(Long memberId){
//        GetTeamsResponse mainPageTeams=teamService.retrieveTeams(memberId); // 팀 목록
//        Member member=memberRepository.findById(memberId).get();
//
//        List<GetFoldersResponse> mainPageMyFolders=folderService.retrieveFolder("member",memberId); // 내 폴더
//        logger.info("mainPageMyFolders size: {}",mainPageMyFolders.size());
//
//        // 팀 폴더
//        List<GetFoldersResponse> mainPageTeamFolders=new ArrayList<>(); // 팀 폴더들
//        List<TeamInfo> teamInfoList=mainPageTeams.getTeamInfoList();
//        for(TeamInfo teamInfo:teamInfoList){
//            List<GetFoldersResponse> teamFolders=folderService.retrieveFolder("team", teamInfo.getTeamId());
//            for(GetFoldersResponse teamFolder:teamFolders){
//                mainPageTeamFolders.add(teamFolder);
//            }
//        }
//        logger.info("mainPageTeamFolders size: {}",mainPageTeamFolders.size());
//
//        List<GetFoldersResponse> allFolders=new ArrayList<>(); // 전체 폴더
//
//        List<GetFilesResponse> allFiles=new ArrayList<>();  // 모든 파일 리스트
//
//        List<GetFilesResponse> teamFiles=new ArrayList<>(); // 팀 파일 리스트 - 모든 팀의 파일들 전달
//        List<GetImagesResponse> teamImages=new ArrayList<>();
//        List<GetMemosResponse>teamMemos=new ArrayList<>();
//        List<GetLinksResponse> teamLinks=new ArrayList<>();
//
//        List<GetItemResponse> items=new ArrayList<>();
//        for(GetFoldersResponse mainPageTeamFolder:mainPageTeamFolders){
//            allFolders.add(mainPageTeamFolder);
//            logger.info("teamFolder id:{}",mainPageTeamFolder.getFolderId());
//            // 팀 폴더 내 파일 조회
//            teamFiles= fileService.retrieveFiles(mainPageTeamFolder.getFolderId());
//            for(GetFilesResponse teamFile:teamFiles){
//                logger.info("teamFile lastModifiedDate");
//                items.add(new GetItemResponse(teamFile.getLastModifiedDate(),"file",teamFile,null,null,null));
//                //allFiles.add(teamFile);
//            }
//            logger.info(">teamFiles size: {}",teamFiles.size());
//            // 팀 폴더 내 이미지 조회
//            teamImages=imageService.retrieveImages(mainPageTeamFolder.getFolderId());
//            for(GetImagesResponse teamImage:teamImages){
//                items.add(new GetItemResponse(teamImage.getLastModifiedDate(),"image",null,null,null,teamImage));
//                //allFiles.add(teamFile);
//            }
//            // 팀 폴더 내 메모 조회
//            teamMemos=memoService.retrieveMemos(mainPageTeamFolder.getFolderId());
//            for(GetMemosResponse teamMemo:teamMemos){
//                items.add(new GetItemResponse(teamMemo.getLastModifiedDate(),"memo",null,null,teamMemo,null));
//                //allFiles.add(teamFile);
//            }
//            // 팀 폴더 내 링크 조회
//            teamLinks=linkService.retrieveLinks(mainPageTeamFolder.getFolderId());
//            for(GetLinksResponse teamLink:teamLinks){
//                items.add(new GetItemResponse(teamLink.getLastModifiedDate(),"link",null,teamLink,null,null));
//                //allFiles.add(teamFile);
//            }
//        }
//        logger.info("teamFiles size: {}",teamFiles.size());
//
//        List<GetFilesResponse> myFiles=new ArrayList<>();
//        List<GetImagesResponse> myImages=new ArrayList<>();
//        List<GetMemosResponse> myMemos=new ArrayList<>();
//        List<GetLinksResponse> myLinks=new ArrayList<>();
//        for(GetFoldersResponse mainPageMyFolder:mainPageMyFolders){
//            allFolders.add(mainPageMyFolder);
//
//            logger.info("myFolder id:{}",mainPageMyFolder.getFolderId());
//            // 개인 폴더 내 파일 조회
//            myFiles=fileService.retrieveFiles(mainPageMyFolder.getFolderId());
//            for(GetFilesResponse myFile:myFiles){
//                items.add(new GetItemResponse(myFile.getLastModifiedDate(),"file",myFile,null,null,null));
//                //allFiles.add(myFile);
//            }
//            // 개인 폴더 내 이미지 조회
//            myImages=imageService.retrieveImages(mainPageMyFolder.getFolderId());
//            for(GetImagesResponse myImage:myImages){
//                items.add(new GetItemResponse(myImage.getLastModifiedDate(),"image",null,null,null,myImage));
//                //allFiles.add(teamFile);
//            }
//            // 개인 폴더 내 메모 조회
//            myMemos=memoService.retrieveMemos(mainPageMyFolder.getFolderId());
//            for(GetMemosResponse myMemo:myMemos){
//                items.add(new GetItemResponse(myMemo.getLastModifiedDate(),"memo",null,null,myMemo,null));
//                //allFiles.add(teamFile);
//            }
//            // 개인 폴더 내 링크 조회
//            myLinks=linkService.retrieveLinks(mainPageMyFolder.getFolderId());
//            for(GetLinksResponse myLink:myLinks){
//                items.add(new GetItemResponse(myLink.getLastModifiedDate(),"link",null,myLink,null,null));
//                //allFiles.add(teamFile);
//            }
//            //logger.info(">myFiles size: {}",myFiles.size());
//
//        }
//        logger.info("myFiles size: {}",myFiles.size());
//
//
//        //logger.info("allFiles size:{}",allFiles.size());
//        logger.info("allFolders size:{}",allFolders.size());
//
//
//        Collections.sort(mainPageMyFolders);
//        Collections.sort(mainPageTeamFolders);
//        //Collections.sort(allFiles);
//        Collections.sort(items);
//        Collections.sort(allFolders);
//
//        GetMainPageResponse getMainPageResponse=new GetMainPageResponse(mainPageTeams,mainPageMyFolders,mainPageTeamFolders,allFolders,items,memberId,member.getUsername());
//
//        return getMainPageResponse;
//
//    }

    //TODO: 성능 개선
    public GetMainPageResponse getMainPage(Long memberId){

        // 팀 목록
        GetTeamsResponse mainPageTeams=teamService.retrieveTeams(memberId); // 팀 목록
        List<TeamMember> teamMembers=teamMemberRepository.findTeamMemberByMemberId(memberId);
        Member member=memberRepository.findById(memberId).get();

        // 내 폴더
        List<Folder> myFolders=folderRepository.findAllByMemberId(member.getId()); // my folders
        List<Long> myFolderIds=myFolders.stream() // 내 폴더 ids
                .map(myFolder->myFolder.getId())
                .collect(Collectors.toList());
        List<GetFoldersResponse> mainPageMyFolders=myFolders.stream()
                .map(mf->new GetFoldersResponse(mf))
                .collect(Collectors.toList()); // 내 폴더들

        // 팀 폴더
        List<Long> teamIds=teamMembers.stream() // 팀 ids
                .map(teamMember-> teamMember.getTeam().getTeamIdx())
                .collect(Collectors.toList());
        List<Folder> teamFolders= folderRepository.findAllByTeamIds(teamIds); // 팀 folders
        List<GetFoldersResponse> mainPageTeamFolders=teamFolders.stream()
                .map(tf->new GetFoldersResponse(tf))
                .collect(Collectors.toList()); // 팀 폴더들
        List<Long> teamFolderIds=teamFolders.stream() // 팀 폴더 ids
                        .map(teamFolder->teamFolder.getId())
                                .collect(Collectors.toList());

        // 전체 폴더
        List<GetFoldersResponse> allFolders=new ArrayList<>();
        allFolders.addAll(mainPageTeamFolders);
        allFolders.addAll(mainPageMyFolders);

        List<GetFilesResponse> allFiles=new ArrayList<>();  // 모든 파일 리스트
        List<GetItemResponse> items=new ArrayList<>();

        List<GetFilesResponse> teamFiles=fileRepository.findAllInfoByFolderIds(teamFolderIds);
        for(GetFilesResponse teamFile:teamFiles){
            logger.info("teamFile lastModifiedDate");
            items.add(new GetItemResponse(teamFile.getLastModifiedDate(),"file",teamFile,null,null,null));
        }
        List<GetImagesResponse> teamImages=imageRepository.findAllInfoByFolderIds(teamFolderIds);
        for(GetImagesResponse teamImage:teamImages){
            items.add(new GetItemResponse(teamImage.getLastModifiedDate(),"image",null,null,null,teamImage));
        }
        List<GetMemosResponse>teamMemos=memoRepository.findAllInfoByFolderIds(teamFolderIds);
        for(GetMemosResponse teamMemo:teamMemos){
            items.add(new GetItemResponse(teamMemo.getLastModifiedDate(),"memo",null,null,teamMemo,null));
        }
        List<GetLinksResponse> teamLinks=linkRepository.findAllInfoByFolderIds(teamFolderIds);
        for(GetLinksResponse teamLink:teamLinks){
            items.add(new GetItemResponse(teamLink.getLastModifiedDate(),"link",null,teamLink,null,null));
        }

        List<GetFilesResponse> myFiles=fileRepository.findAllInfoByFolderIds(myFolderIds);
        for(GetFilesResponse myFile:myFiles){
            items.add(new GetItemResponse(myFile.getLastModifiedDate(),"file",myFile,null,null,null));
        }
        List<GetImagesResponse> myImages=imageRepository.findAllInfoByFolderIds(myFolderIds);
        for(GetImagesResponse myImage:myImages){
            items.add(new GetItemResponse(myImage.getLastModifiedDate(),"image",null,null,null,myImage));
        }
        List<GetMemosResponse> myMemos=memoRepository.findAllInfoByFolderIds(myFolderIds);
        for(GetMemosResponse myMemo:myMemos){
            items.add(new GetItemResponse(myMemo.getLastModifiedDate(),"memo",null,null,myMemo,null));
        }
        List<GetLinksResponse> myLinks=linkRepository.findAllInfoByFolderIds(myFolderIds);
        for(GetLinksResponse myLink:myLinks){
            items.add(new GetItemResponse(myLink.getLastModifiedDate(),"link",null,myLink,null,null));
        }

        logger.info("teamFiles size: {}",teamFiles.size());
        logger.info("myFiles size: {}",myFiles.size());
        logger.info("allFolders size:{}",allFolders.size());
        logger.info("mainPageTeamFolders size: {}",mainPageTeamFolders.size());
        logger.info("mainPageMyFolders size: {}",mainPageMyFolders.size());


        Collections.sort(mainPageMyFolders);
        Collections.sort(mainPageTeamFolders);
        Collections.sort(items);
        Collections.sort(allFolders);

        //GetMainPageResponse getMainPageResponse=new GetMainPageResponse(mainPageTeams,mainPageMyFolders,mainPageTeamFolders,allFolders,items,memberId,member.getUsername());
        GetMainPageResponse getMainPageResponse=new GetMainPageResponse(items,memberId,member.getUsername());
        return getMainPageResponse;

    }
}
