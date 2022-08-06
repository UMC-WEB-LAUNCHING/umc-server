package com.umc.helper.link;

import com.umc.helper.bookmark.BookmarkRepository;
import com.umc.helper.bookmark.model.Bookmark;
import com.umc.helper.bookmark.model.PostBookmarkResponse;
import com.umc.helper.folder.FolderRepository;
import com.umc.helper.folder.model.Folder;
import com.umc.helper.link.model.*;
import com.umc.helper.member.Member;
import com.umc.helper.member.MemberRepository;
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
     *  해당 폴더 모든 링크 조회
     */
    @Transactional
    public List<GetLinksResponse> retrieveLinks(Long folderId){

        List<GetLinksResponse> result= linkRepository.findAllInfoByFolderId(folderId);

        return result;
    }



    /**
     *  링크 업로드
     */

    @Transactional
    public PostLinkResponse uploadLink(PostLinkRequest postLinkReq){

        Folder folder=folderRepository.findById(postLinkReq.getFolderId());
        Optional<Member> member=memberRepository.findById(postLinkReq.getMemberId());

        Link link=new Link();
        link.setName(postLinkReq.getName());
        link.setUrl(postLinkReq.getUrl());
        link.setFolder(folder);
        link.setMember(member.get());
        link.setStatus(Boolean.TRUE);
        link.setUploadDate(LocalDateTime.now());
        folder.setLastModifiedDate(LocalDateTime.now());
        linkRepository.save(link);

        return new PostLinkResponse(linkRepository.findById(link.getId()).getId(), link.getUrl(), link.getName());
    }
    /**
     *  링크 변경 - 제목
     */
    @Transactional
    public PatchLinkResponse modifyLink(Long linkId, PatchLinkRequest patchLinkRequest){

        Link link=linkRepository.findById(linkId);
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
     *  링크 상태 변경
     */
    @Transactional
    public PatchLinkStatusResponse modifyLinkStatus(Long linkId,Long memberId){

        Link link=linkRepository.findById(linkId);
        // 링크 올린 사람과 링크 수정하고자 하는 사람이 같아야만 쓰레기통에 삭제 가능
        if(link.getMember().getId()==memberId) {
            link.setStatus(Boolean.FALSE);
            link.setStatusModifiedDate(LocalDateTime.now());

        }

        return new PatchLinkStatusResponse(link);
    }

    /**
     *  링크 북마크 등록
     */
    @Transactional
    public PostBookmarkResponse addBookmark(Long linkId, Long memberId){

        Member member=memberRepository.findById(memberId).get();
        Link link=linkRepository.findById(linkId);

        Bookmark bookmark=new Bookmark();
        bookmark.setLink(link);
        bookmark.setMember(member);
        bookmark.setCategory("link");
        bookmark.setAddedDate(LocalDateTime.now());

        bookmarkRepository.save(bookmark);

        return new PostBookmarkResponse("link",linkId,bookmarkRepository.findById(bookmark.getId()).getId());
    }

}
