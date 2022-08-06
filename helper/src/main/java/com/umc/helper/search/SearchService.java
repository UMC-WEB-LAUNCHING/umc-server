package com.umc.helper.search;

import com.umc.helper.file.FileRepository;
import com.umc.helper.file.model.File;
import com.umc.helper.file.model.GetFilesResponse;
import com.umc.helper.folder.FolderRepository;
import com.umc.helper.folder.model.Folder;
import com.umc.helper.image.ImageRepository;
import com.umc.helper.image.model.GetImagesResponse;
import com.umc.helper.image.model.Image;
import com.umc.helper.link.LinkRepository;
import com.umc.helper.link.model.GetLinksResponse;
import com.umc.helper.link.model.Link;
import com.umc.helper.memo.MemoRepository;
import com.umc.helper.memo.model.GetMemosResponse;
import com.umc.helper.memo.model.Memo;
import com.umc.helper.search.model.GetSearchedResponse;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final MemoRepository memoRepository;
    private final LinkRepository linkRepository;
    private final FileRepository fileRepository;
    private final ImageRepository imageRepository;
    private final TeamService teamService;
    private final FolderRepository folderRepository;
    Logger logger= LoggerFactory.getLogger(SearchService.class);

    public List<GetSearchedResponse> getSearchItems(String word,Long memberId){
        List<TeamInfo> teamList=teamService.retrieveTeams(memberId).getTeamInfoList(); // 팀 목록
        List<Long> allFoldersId=new ArrayList<>();
        for(TeamInfo team:teamList){
            List<Folder> folderList=folderRepository.findAllByTeamId(team.getTeamId());
            for(Folder folder:folderList){
                allFoldersId.add(folder.getId());
            }
        }
        List<Folder> myFolders=folderRepository.findAllByMemberId(memberId);
        for(Folder folder:myFolders){
            allFoldersId.add(folder.getId());
        }


        List<GetSearchedResponse> result=new ArrayList<>();

        List<File> getFiles=fileRepository.findByWord(word,allFoldersId);
        List<GetFilesResponse> getFilesRes=getFiles.stream()
                .map(f->new GetFilesResponse(f))
                .collect(Collectors.toList());
        for(GetFilesResponse file:getFilesRes){
            result.add(new GetSearchedResponse("file",file,file.getLastModifiedDate()));
        }

        List<Memo> getMemos=memoRepository.findByWord(word);
        List<GetMemosResponse> getMemosRes=getMemos.stream()
                .map(m->new GetMemosResponse(m))
                .collect(Collectors.toList());
        for(GetMemosResponse memo:getMemosRes){
            result.add(new GetSearchedResponse("memo",memo,memo.getLastModifiedDate()));
        }

        List<Link> getLinks=linkRepository.findByWord(word);
        List<GetLinksResponse> getLinksRes=getLinks.stream()
                .map(l->new GetLinksResponse(l))
                .collect(Collectors.toList());
        for(GetLinksResponse link:getLinksRes){
            result.add(new GetSearchedResponse("link",link,link.getLastModifiedDate()));
        }

        List<Image> getImages=imageRepository.findByWord(word);
        List<GetImagesResponse> getImagesRes=getImages.stream()
                .map(i->new GetImagesResponse(i))
                .collect(Collectors.toList());
        for(GetImagesResponse image:getImagesRes){
            result.add(new GetSearchedResponse("image",image,image.getLastModifiedDate()));
        }

        Collections.sort(result);

        return result;
    }
}
