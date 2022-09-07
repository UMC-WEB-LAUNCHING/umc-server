package com.umc.helper.memo;

import com.umc.helper.bookmark.model.Bookmark;
import com.umc.helper.bookmark.BookmarkRepository;
import com.umc.helper.bookmark.model.PostBookmarkResponse;
import com.umc.helper.file.exception.FileNameDuplicateException;
import com.umc.helper.folder.FolderRepository;
import com.umc.helper.folder.exception.FolderNotFoundException;
import com.umc.helper.folder.model.Folder;
import com.umc.helper.member.exception.MemberNotFoundException;
import com.umc.helper.member.model.Member;
import com.umc.helper.member.MemberRepository;
import com.umc.helper.memo.exception.MemoNameDuplicateException;
import com.umc.helper.memo.exception.MemoSizeException;
import com.umc.helper.memo.model.*;
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
public class MemoService {

    private final MemoRepository memoRepository;
    private final FolderRepository folderRepository;
    private final MemberRepository memberRepository;
    private final TeamMemberRepository teamMemberRepository;

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
        Folder folder=folderRepository.findById(folderId);
        // 폴더 존재 확인
        if(folder==null){
            throw new FolderNotFoundException();
        }

        List<GetMemosResponse> result= memoRepository.findAllInfoByFolderId(folderId);

        return result;
    }

    /**
     *  메모 업로드
     */
    @Transactional
    public PostMemoResponse uploadMemo(PostMemoRequest postMemoReq){

        if(postMemoReq.getContent().length()>500) throw new MemoSizeException(); // 메모 내용 길이 확인

        Folder folder=folderRepository.findById(postMemoReq.getFolderId());
        // 폴더 존재 확인
        if(folder==null){
            throw new FolderNotFoundException();
        }

        // 업로더 존재 확인
        Optional<Member> member=memberRepository.findById(postMemoReq.getMemberId());
        if(member.isEmpty()) {
            throw new MemberNotFoundException();
        }

        // 폴더 내 동일 메모 이름 확인
//        if(memoRepository.findDuplicateMemoName(postMemoReq.getFolderId(),postMemoReq.getName())==1){
//            throw new MemoNameDuplicateException();
//        }

        // 폴더 내 업로드 권한 확인 TODO: 코드 리팩토링 필요
        if(folder.getMember()!=null) folder.invalidUploader(member.get().getId()); // 개인 폴더
        else if(folder.getMember()==null && folder.getTeam()!=null){ // 팀 폴더
            TeamMember teamMember=teamMemberRepository.findTeamMemberByMemberTeamId(folder.getTeam().getTeamIdx(), member.get().getId());
            teamMember.notExistTeamMember();
        }

        Memo memo=new Memo();
        memo.setName(postMemoReq.getName());
        memo.setContent(postMemoReq.getContent());
        memo.setFolder(folder);
        memo.setMember(member.get());
        memo.setStatus(Boolean.TRUE);
        memo.setUploadDate(LocalDateTime.now());
        memo.setLastModifiedDate(memo.getUploadDate());
        folder.setLastModifiedDate(memo.getUploadDate());
        memoRepository.save(memo);

        return new PostMemoResponse(memoRepository.findById(memo.getId()).getId());
    }

    /**
     *  메모 변경 - 제목
     */
    @Transactional
    public PatchMemoResponse modifyMemo(Long memoId, PatchMemoRequest patchMemoRequest){

        Memo memo=memoRepository.findById(memoId);
        Folder folder=folderRepository.findById(memo.getFolder().getId());

        // 메모 올린 사람과 메모 수정하고자 하는 사람이 같아야만 수정
        if(memo.getMember().getId().compareTo(patchMemoRequest.getMemberId())==0){
            memo.setName(patchMemoRequest.getName());
            memo.setContent(patchMemoRequest.getContent());
            memo.setLastModifiedDate(LocalDateTime.now());
            folder.setLastModifiedDate(memo.getLastModifiedDate());
        }


        return new PatchMemoResponse(memo);
    }

    /**
     *  메모 상태 변경
     */
    @Transactional
    public PatchMemoStatusResponse modifyMemoStatus(Long memoId, Long memberId){

        Memo memo=memoRepository.findById(memoId);
        memo.notExistMemo();

        Folder folder=folderRepository.findById(memo.getFolder().getId());

        // 메모 올린 사람과 메모 수정하고자 하는 사람이 같아야만 쓰레기통에 삭제 가능
        if(memo.getMember().getId().compareTo(memberId)==0) {
            memo.setStatus(Boolean.FALSE);
            memo.setStatusModifiedDate(LocalDateTime.now());
            folder.setLastModifiedDate(memo.getStatusModifiedDate());
        }

        return new PatchMemoStatusResponse(memo);
    }

    /**
     *  메모 북마크 등록
     */
    @Transactional
    public PostBookmarkResponse addBookmark(Long folderId,Long memoId, Long memberId){

        // 업로더 존재 확인
        Optional<Member> member=memberRepository.findById(memberId);
        if(member.isEmpty()) {
            throw new MemberNotFoundException();
        }

        Memo memo=memoRepository.findById(memoId);
        memo.notExistMemo();

        Folder folder=folderRepository.findById(folderId);
        // 폴더 존재 확인
        if(folder==null){
            throw new FolderNotFoundException();
        }


        // 파일 북마크 등록 권한 확인 TODO: 코드 리팩토링 필요
        if(folder.getMember()!=null) folder.invalidUploader(member.get().getId()); // 개인 폴더 내 파일
        else if(folder.getMember()==null && folder.getTeam()!=null){ // 팀 폴더 내 파일
            TeamMember teamMember=teamMemberRepository.findTeamMemberByMemberTeamId(folder.getTeam().getTeamIdx(), member.get().getId());
            teamMember.notExistTeamMember();
        }

        Bookmark bookmark=new Bookmark();
        bookmark.setMemo(memo);
        bookmark.setMember(member.get());
        bookmark.setCategory("memo");
        bookmark.setAddedDate(LocalDateTime.now());

        bookmarkRepository.save(bookmark);

        return new PostBookmarkResponse("memo",memoId,bookmarkRepository.findById(bookmark.getId()).getId());
    }
}
