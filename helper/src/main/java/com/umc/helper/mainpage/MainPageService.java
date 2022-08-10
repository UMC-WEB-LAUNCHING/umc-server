package com.umc.helper.mainpage;

import com.umc.helper.file.FileService;
import com.umc.helper.file.model.GetFilesResponse;
import com.umc.helper.folder.FolderService;
import com.umc.helper.folder.model.GetFoldersResponse;
import com.umc.helper.mainpage.model.GetMainPageResponse;
import com.umc.helper.member.model.Member;
import com.umc.helper.member.MemberRepository;
import com.umc.helper.team.TeamService;
import com.umc.helper.team.model.GetTeamsResponse;
import com.umc.helper.team.model.TeamInfo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MainPageService {

    private final TeamService teamService;
    private final MemberRepository memberRepository;
    private final FolderService folderService;
    private final FileService fileService;
    Logger logger= LoggerFactory.getLogger(MainPageService.class);

    public GetMainPageResponse getMainPage(Long memberId){
        GetTeamsResponse mainPageTeams=teamService.retrieveTeams(memberId); // 팀 목록
        Member member=memberRepository.findById(memberId).get();

        List<GetFoldersResponse> mainPageMyFolders=folderService.retrieveFolder("member",memberId); // 내 폴더
        logger.info("mainPageMyFolders size: {}",mainPageMyFolders.size());

        // 팀 폴더
        List<GetFoldersResponse> mainPageTeamFolders=new ArrayList<>();
        List<TeamInfo> teamInfoList=mainPageTeams.getTeamInfoList();
        for(TeamInfo teamInfo:teamInfoList){
            List<GetFoldersResponse> teamFolders=folderService.retrieveFolder("team", teamInfo.getTeamId());
            for(GetFoldersResponse teamFolder:teamFolders){
                mainPageTeamFolders.add(teamFolder);
            }
        }
        logger.info("mainPageTeamFolders size: {}",mainPageTeamFolders.size());

        List<GetFoldersResponse> allFolders=new ArrayList<>(); // 전체 폴더


        List<GetFilesResponse> allFiles=new ArrayList<>(); // 모든 파일
        List<GetFilesResponse> teamFiles=new ArrayList<>();
        for(GetFoldersResponse mainPageTeamFolder:mainPageTeamFolders){
            logger.info("teamFolder id:{}",mainPageTeamFolder.getFolderId());
            teamFiles= fileService.retrieveFiles(mainPageTeamFolder.getFolderId());
            for(GetFilesResponse teamFile:teamFiles){
                allFiles.add(teamFile);
            }
            logger.info(">teamFiles size: {}",teamFiles.size());

            allFolders.add(mainPageTeamFolder);
        }
        logger.info("teamFiles size: {}",teamFiles.size());
        List<GetFilesResponse> myFiles=new ArrayList<>();
        for(GetFoldersResponse mainPageMyFolder:mainPageMyFolders){
            logger.info("myFolder id:{}",mainPageMyFolder.getFolderId());
            myFiles=fileService.retrieveFiles(mainPageMyFolder.getFolderId());
            for(GetFilesResponse myFile:myFiles){
                allFiles.add(myFile);
            }
            logger.info(">myFiles size: {}",myFiles.size());
            allFolders.add(mainPageMyFolder);
        }
        logger.info("myFiles size: {}",myFiles.size());


        logger.info("allFiles size:{}",allFiles.size());
        logger.info("allFolders size:{}",allFolders.size());

//        List<GetFilesResponse> sortedAllFiles=allFiles.stream()
//                .sorted(Comparator.comparing(GetFilesResponse::getLastModifiedDate))
//                .collect(Collectors.toList());
//
//        List<GetFoldersResponse> sortedAllFolders=allFolders.stream()
//                .sorted(Comparator.comparing(GetFoldersResponse::getLastModifiedDate))
//                .collect(Collectors.toList());
        Collections.sort(mainPageMyFolders);
        Collections.sort(mainPageTeamFolders);
        Collections.sort(allFiles);
        Collections.sort(allFolders);

        GetMainPageResponse getMainPageResponse=new GetMainPageResponse(mainPageTeams,mainPageMyFolders,mainPageTeamFolders,allFolders,allFiles,memberId,member.getUsername());

        return getMainPageResponse;
    }
}
