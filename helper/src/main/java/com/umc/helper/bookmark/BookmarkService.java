package com.umc.helper.bookmark;

import com.umc.helper.bookmark.model.*;
import com.umc.helper.file.FileRepository;
import com.umc.helper.file.model.File;
import com.umc.helper.file.model.GetFilesResponse;
import com.umc.helper.folder.FolderRepository;
import com.umc.helper.image.ImageRepository;
import com.umc.helper.image.model.Image;
import com.umc.helper.link.LinkRepository;
import com.umc.helper.link.model.Link;
import com.umc.helper.mainpage.model.GetItemResponse;
import com.umc.helper.mainpage.model.GetMainPageResponse;
import com.umc.helper.member.MemberRepository;
import com.umc.helper.member.exception.MemberNotFoundException;
import com.umc.helper.member.model.Member;
import com.umc.helper.memo.MemoRepository;
import com.umc.helper.memo.model.Memo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;

    private final FileRepository fileRepository;
    private final ImageRepository imageRepository;
    private final LinkRepository linkRepository;
    private final MemoRepository memoRepository;
    private final FolderRepository folderRepository;
    /**
     *  북마크 삭제
     */
    @Transactional
    public String deleteBookmark(Long bookmarkId){
        Bookmark bookmark=bookmarkRepository.findById(bookmarkId);
        bookmark.notExistBookmark();

        bookmarkRepository.remove(bookmark);

        return "북마크 삭제 성공";
    }

    /**
     * 북마크 조회
     */
    @Transactional
    public List<GetBookmarksResponse> retrieveBookmarks(Long memberId){
        // 업로더 존재 확인
        Optional<Member> member=memberRepository.findById(memberId);
        if(member.isEmpty()) {
            throw new MemberNotFoundException();
        }

        List<Bookmark> bookmarks=bookmarkRepository.findAllByMemberId(memberId);
        List<GetBookmarksResponse> bookmarkResult=new ArrayList<>();
        List<File> files=new ArrayList<>();
        List<Image> images=new ArrayList<>();
        List<Memo> memos=new ArrayList<>();
        List<Link> links=new ArrayList<>();

        for(Bookmark bm:bookmarks){
            if(bm.getCategory().equals("file")){
                File f=fileRepository.findById(bm.getFile().getId());
                GetFileResponse gf=new GetFileResponse(f.getId(),f.getFilePath(),f.getOriginalFileName(),f.getFolder().getId(),f.getUploadDate(),f.getVolume(),f.getMember().getUsername());
                bookmarkResult.add(new GetBookmarksResponse(bm.getId(),"file",gf,null,null,gf.getUploadDate()));
            }
//            else if(bm.getCategory().equals("folder")){
//
//                bookmarkResult.add(new GetBookmarksResponse(bm.getId(),"folder",null,null,null,null,bm.getFolder(),bm.getAddedDate()));
//
//            }
            else if(bm.getCategory().equals("memo")){
               Memo m=memoRepository.findById(bm.getMemo().getId());
                GetMemoResponse gm=new GetMemoResponse(m.getId(),m.getContent(),m.getName(),m.getFolder().getId(),m.getUploadDate(),m.getMember().getUsername());
                bookmarkResult.add(new GetBookmarksResponse(bm.getId(),"memo",null,null,gm,gm.getUploadDate()));

            }
            else if(bm.getCategory().equals("link")){
                Link l=linkRepository.findById(bm.getLink().getId());
                GetLinkResponse gl=new GetLinkResponse(l.getId(),l.getUrl(),l.getName(),l.getFolder().getId(),l.getUploadDate(),l.getMember().getUsername());
                bookmarkResult.add(new GetBookmarksResponse(bm.getId(),"link",null,gl,null,gl.getUploadDate()));
            }
        }

        Collections.sort(bookmarkResult);

        return bookmarkResult;
    }

//    @Transactional
//    public GetMainPageResponse retrieveBookmarks(Long memberId){
//        // 업로더 존재 확인
//        Optional<Member> member=memberRepository.findById(memberId);
//        if(member.isEmpty()) {
//            throw new MemberNotFoundException();
//        }
//
//        List<Bookmark> bookmarks=bookmarkRepository.findAllByMemberId(memberId);
//        List<GetBookmarksResponse> result=bookmarks.stream()
//                .map(bm->new GetBookmarksResponse(bm))
//                .collect(toList());
//        Collections.sort(result);
//
//        return result;
//    }
}
