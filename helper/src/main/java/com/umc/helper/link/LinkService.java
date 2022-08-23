package com.umc.helper.link;

import com.umc.helper.bookmark.BookmarkRepository;
import com.umc.helper.bookmark.model.Bookmark;
import com.umc.helper.bookmark.model.PostBookmarkResponse;
import com.umc.helper.file.exception.FileNameDuplicateException;
import com.umc.helper.folder.FolderRepository;
import com.umc.helper.folder.exception.FolderNotFoundException;
import com.umc.helper.folder.model.Folder;
import com.umc.helper.link.exception.LinkNameDuplicateException;
import com.umc.helper.link.exception.LinkNotFoundException;
import com.umc.helper.link.model.*;
import com.umc.helper.member.exception.MemberNotFoundException;
import com.umc.helper.member.model.Member;
import com.umc.helper.member.MemberRepository;
import com.umc.helper.team.TeamMemberRepository;
import com.umc.helper.team.model.TeamMember;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LinkService {

    //private final LinkRepositoryJpa linkRepositoryJpa;
    private final LinkRepository linkRepository;
    private final FolderRepository folderRepository;
    private final MemberRepository memberRepository;
    private final BookmarkRepository bookmarkRepository;
    private final TeamMemberRepository teamMemberRepository;


    Logger log= LoggerFactory.getLogger(LinkService.class);

    /**
     *  해당 폴더 모든 링크 조회
     */
//    @Transactional
//    public List<GetLinksResponse> retrieveLinks(Long folderId){
//
//        List<Link> links= linkRepository.findAllByFolderId(folderId);
//        List<GetLinksResponse> result=links.stream()
//                .map(l->new GetLinksResponse(l))
//                .collect(toList());
//
//        return result;
//    }

    /**
     *  해당 폴더 모든 링크 조회 - 모든 정보(즐겨찾기 여부 등)
     */
    @Transactional
    public List<GetLinksResponse> retrieveLinks(Long folderId){
        Folder folder=folderRepository.findById(folderId);
        // 폴더 존재 확인
        if(folder==null){
            throw new FolderNotFoundException();
        }

        List<GetLinksResponse> result= linkRepository.findAllInfoByFolderId(folderId);

        return result;
    }



    /**
     *  링크 업로드
     */

    @Transactional
    public PostLinkResponse uploadLink(PostLinkRequest postLinkReq){

        Folder folder=folderRepository.findById(postLinkReq.getFolderId());
        // 폴더 존재 확인
        if(folder==null){
            throw new FolderNotFoundException();
        }

        // 업로더 존재 확인
        Optional<Member> member=memberRepository.findById(postLinkReq.getMemberId());
        if(member.isEmpty()) {
            throw new MemberNotFoundException();
        }

        // 폴더 내 동일 링크 이름 확인
//        if(linkRepository.findDuplicateLinkName(postLinkReq.getFolderId(),postLinkReq.getName())==1){
//            throw new LinkNameDuplicateException();
//        }

        // 폴더 내 업로드 권한 확인 TODO: 코드 리팩토링 필요
        if(folder.getMember()!=null) folder.invalidUploader(member.get().getId()); // 개인 폴더
        else if(folder.getMember()==null && folder.getTeam()!=null){ // 팀 폴더
            TeamMember teamMember=teamMemberRepository.findTeamMemberByMemberTeamId(folder.getTeam().getTeamIdx(), member.get().getId());
            teamMember.notExistTeamMember();
        }

        Link link=new Link();
        link.setName(postLinkReq.getName());
        link.setUrl(postLinkReq.getUrl());
        link.setFolder(folder);
        link.setMember(member.get());
        link.setStatus(Boolean.TRUE);
        link.setUploadDate(LocalDateTime.now());
        link.setLastModifiedDate(link.getUploadDate());
        folder.setLastModifiedDate(link.getLastModifiedDate());
        linkRepository.save(link);

        return new PostLinkResponse(linkRepository.findById(link.getId()).getId(), link.getUrl(), link.getName());
    }
    /**
     *  링크 변경 - 제목
     */
    @Transactional
    public PatchLinkResponse modifyLink(Long linkId, PatchLinkRequest patchLinkRequest){

        Link link=linkRepository.findById(linkId);
        link.notExistLink(); // 링크 존재 확인

        Folder folder=folderRepository.findById(link.getFolder().getId());

        // 링크 올린 사람과 링크 수정하고자 하는 사람이 같아야만 수정
        if(link.getMember().getId()==patchLinkRequest.getMemberId()){
            link.setName(patchLinkRequest.getName());
            link.setLastModifiedDate(LocalDateTime.now());
            folder.setLastModifiedDate(LocalDateTime.now());
        }


        return new PatchLinkResponse(link);
    }

    /**
     *  링크 상태 변경 - 쓰레기통으로,,,
     */
    @Transactional
    public PatchLinkStatusResponse modifyLinkStatus(Long linkId,Long memberId){

        Link link=linkRepository.findById(linkId);
        if(link==null){
            throw new LinkNotFoundException();
        }


        Folder folder=folderRepository.findById(link.getFolder().getId());

        // 링크 올린 사람과 링크 수정하고자 하는 사람이 같아야만 쓰레기통에 삭제 가능
        if(link.getMember().getId()==memberId) {
            link.setStatus(Boolean.FALSE);
            link.setStatusModifiedDate(LocalDateTime.now());
            folder.setLastModifiedDate(link.getStatusModifiedDate());

        }

        return new PatchLinkStatusResponse(link);
    }

    /**
     *  링크 북마크 등록
     */
    @Transactional
    public PostBookmarkResponse addBookmark(Long folderId, Long linkId, Long memberId){

        // 업로더 존재 확인
        Optional<Member> member=memberRepository.findById(memberId);
        if(member.isEmpty()) {
            throw new MemberNotFoundException();
        }

        // 링크 존재 확인
        Link link=linkRepository.findById(linkId);
        if(link==null){
            throw new LinkNotFoundException();
        }

        Folder folder=folderRepository.findById(folderId);
        // 폴더 존재 확인
        if(folder==null){
            throw new FolderNotFoundException();
        }

        // 링크 북마크 등록 권한 확인 TODO: 코드 리팩토링 필요
        if(folder.getMember()!=null) folder.invalidUploader(member.get().getId()); // 개인 폴더 내 파일
        else if(folder.getMember()==null && folder.getTeam()!=null){ // 팀 폴더 내 파일
            TeamMember teamMember=teamMemberRepository.findTeamMemberByMemberTeamId(folder.getTeam().getTeamIdx(), member.get().getId());
            teamMember.notExistTeamMember();
        }

        Bookmark bookmark=new Bookmark();
        bookmark.setLink(link);
        bookmark.setMember(member.get());
        bookmark.setCategory("link");
        bookmark.setAddedDate(LocalDateTime.now());

        bookmarkRepository.save(bookmark);

        return new PostBookmarkResponse("link",linkId,bookmarkRepository.findById(bookmark.getId()).getId());
    }

}
