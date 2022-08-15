package com.umc.helper.folder;

import com.umc.helper.bookmark.BookmarkRepository;
import com.umc.helper.bookmark.model.Bookmark;
import com.umc.helper.bookmark.model.PostBookmarkResponse;
import com.umc.helper.folder.exception.FolderNameDuplicateException;
import com.umc.helper.folder.model.*;
import com.umc.helper.member.model.Member;
import com.umc.helper.member.MemberRepository;
import com.umc.helper.team.TeamRepository;
import com.umc.helper.team.model.Team;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FolderService {

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final FolderRepository folderRepository;

    private final BookmarkRepository bookmarkRepository;
    private Logger logger= LoggerFactory.getLogger(FolderService.class);

    /**
     *  폴더 생성
     */
    @Transactional
    public PostFolderResponse createFolder(PostFolderRequest postFolderReq){

        PostFolderResponse postFolderRes=new PostFolderResponse();
        String folder_case=postFolderReq.getFolder_case();

        Folder folder=new Folder();

        if(folder_case.equals("team")){
            // 팀 내 동일 폴더 이름 확인
            if(folderRepository.findDuplicateTeamFolderName(postFolderReq.getId(), postFolderReq.getFolder_name())==1){
                throw new FolderNameDuplicateException();
            }
            folder.setTeam(teamRepository.findById(postFolderReq.getId()));
            postFolderRes.setFolder_case("team");
        }
        else if(folder_case.equals("member")){
            // 개인 동일 폴더 이름 확인
            if(folderRepository.findDuplicateMemberFolderName(postFolderReq.getId(), postFolderReq.getFolder_name())==1){
                throw new FolderNameDuplicateException();
            }
            folder.setMember(memberRepository.findById(postFolderReq.getId()).get());
            postFolderRes.setFolder_case("member");
        }

        folder.setCreatedDate(LocalDateTime.now());
        folder.setFolderName(postFolderReq.getFolder_name());
        folder.setCreatorId(postFolderReq.getCreatorId());
        folder.setStatus(Boolean.TRUE);
        folder.setLastModifiedDate(folder.getCreatedDate());
        folderRepository.save(folder);

        postFolderRes.setFolderId(folderRepository.findById(folder.getId()).getId());
        postFolderRes.setFolder_name(folderRepository.findById(folder.getId()).getFolderName());

        return postFolderRes;
    }

    /**
     *  폴더 조회
     */
    @Transactional
    public List<GetFoldersResponse> retrieveFolder(String folder_case,Long id){
        List<Folder> folders=new ArrayList<>();
        if(folder_case.equals("team")){
            Team team=teamRepository.findById(id);
            if(team!=null){
                folders=folderRepository.findAllByTeamId(team.getTeamIdx());
            }
        }
        else if(folder_case.equals("member")){
            Member member=memberRepository.findById(id).get();
            logger.info("member->{}",member.getId());
            if(member!=null){
                folders=folderRepository.findAllByMemberId(member.getId());
            }
        }

        List<GetFoldersResponse> result=folders.stream()
                .map(f->new GetFoldersResponse(f))
                .collect(Collectors.toList());

        return result;
    }

    /**
     *  폴더 상태 변경 - 쓰레기통으로,,,
     */
    @Transactional
    public PatchFolderResponse modifyFolderStatus(Long folder_id,Long creator_id){
        Folder folder=folderRepository.findById(folder_id);

        // 폴더 생성한 사람과 폴더를 휴지통으로 버리고 싶은 사람이 같아야만 휴지통에 삭제 가능
        if(folder.getCreatorId()==creator_id){
            folder.setStatus(Boolean.FALSE);
            folder.setStatusModifiedDate(LocalDateTime.now());
        }

        return new PatchFolderResponse(folder);
    }

    /**
     *  폴더 북마크 등록
     */
    @Transactional
    public PostBookmarkResponse addBookmark(Long folderId,Long memberId){
        Member member=memberRepository.findById(memberId).get();
        Folder folder=folderRepository.findById(folderId);

        Bookmark bookmark=new Bookmark();
        bookmark.setFolder(folder);
        bookmark.setMember(member);
        bookmark.setCategory("folder");
        bookmark.setAddedDate(LocalDateTime.now());
        bookmarkRepository.save(bookmark);

        return new PostBookmarkResponse("folder",folderId,bookmarkRepository.findById(bookmark.getId()).getId());
    }
}

