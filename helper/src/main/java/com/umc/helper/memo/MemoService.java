package com.umc.helper.memo;

import com.umc.helper.bookmark.model.Bookmark;
import com.umc.helper.bookmark.BookmarkRepository;
import com.umc.helper.bookmark.model.PostBookmarkResponse;
import com.umc.helper.folder.FolderRepository;
import com.umc.helper.folder.model.Folder;
import com.umc.helper.member.Member;
import com.umc.helper.member.MemberRepository;
import com.umc.helper.memo.model.*;
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
public class MemoService {

    private final MemoRepository memoRepository;
    private final FolderRepository folderRepository;
    private final MemberRepository memberRepository;

    private final BookmarkRepository bookmarkRepository;

    Logger log= LoggerFactory.getLogger(MemoService.class);

    /**
     *  해당 폴더 모든 메모 조회
     */
//    @Transactional
//    public List<GetMemosResponse> retrieveMemos(Long folderId){
//
//        List<Memo> memos= memoRepository.findAllByFolderId(folderId);
//        List<GetMemosResponse> result=memos.stream()
//                .map(m->new GetMemosResponse(m))
//                .collect(toList());
//
//        return result;
//    }

    /**
     *  해당 폴더 모든 메모 조회 - 모든 정보(즐겨찾기 여부 등)
     */
    @Transactional
    public List<GetMemosResponse> retrieveMemos(Long folderId){

        List<GetMemosResponse> result= memoRepository.findAllInfoByFolderId(folderId);

        return result;
    }

    /**
     *  메모 업로드
     */
    @Transactional
    public PostMemoResponse uploadMemo(PostMemoRequest postMemoReq){
        Folder folder=folderRepository.findById(postMemoReq.getFolderId());
        Optional<Member> member=memberRepository.findById(postMemoReq.getMemberId());

        Memo memo=new Memo();
        memo.setName(postMemoReq.getName());
        memo.setContent(postMemoReq.getContent());
        memo.setFolder(folder);
        memo.setMember(member.get());
        memo.setStatus(Boolean.TRUE);
        memo.setUploadDate(LocalDateTime.now());
        memoRepository.save(memo);

        return new PostMemoResponse(memoRepository.findById(memo.getId()).getId());
    }

    /**
     *  메모 변경 - 제목
     */
    @Transactional
    public PatchMemoResponse modifyMemo(Long memoId, PatchMemoRequest patchMemoRequest){

        Memo memo=memoRepository.findById(memoId);

        // 메모 올린 사람과 메모 수정하고자 하는 사람이 같아야만 수정
        if(memo.getMember().getId()==patchMemoRequest.getMemberId()){
            memo.setName(patchMemoRequest.getName());
            memo.setContent(patchMemoRequest.getContent());
            memo.setLastModifiedDate(LocalDateTime.now());
        }


        return new PatchMemoResponse(memo);
    }

    /**
     *  메모 상태 변경
     */
    @Transactional
    public PatchMemoStatusResponse modifyMemoStatus(Long memoId, Long memberId){

        Memo memo=memoRepository.findById(memoId);
        // 메모 올린 사람과 메모 수정하고자 하는 사람이 같아야만 쓰레기통에 삭제 가능
        if(memo.getMember().getId()==memberId) {
            memo.setStatus(Boolean.FALSE);
            memo.setStatusModifiedDate(LocalDateTime.now());

        }

        return new PatchMemoStatusResponse(memo);
    }

    /**
     *  메모 북마크 등록
     */
    @Transactional
    public PostBookmarkResponse addBookmark(Long memoId, Long memberId){

        Member member=memberRepository.findById(memberId).get();
        Memo memo=memoRepository.findById(memoId);

        Bookmark bookmark=new Bookmark();
        bookmark.setMemo(memo);
        bookmark.setMember(member);
        bookmark.setCategory("memo");
        bookmark.setAddedDate(LocalDateTime.now());

        bookmarkRepository.save(bookmark);

        return new PostBookmarkResponse("memo",memoId,bookmarkRepository.findById(bookmark.getId()).getId());
    }
}
