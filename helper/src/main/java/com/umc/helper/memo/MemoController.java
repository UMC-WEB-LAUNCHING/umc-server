package com.umc.helper.memo;

import com.umc.helper.bookmark.BookmarkService;
import com.umc.helper.bookmark.model.PostBookmarkResponse;
import com.umc.helper.config.BaseResponse;

import com.umc.helper.memo.model.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;
    private final BookmarkService bookmarkService;

    Logger log= LoggerFactory.getLogger(MemoController.class);

    /**
     * retrieve memos
     * @param folderId
     * @return getMemosRes
     */
    //memo/{folderId}
    @GetMapping("folder/{folderId}/memos") // url이 뭔가 이상함 TODO: url 수정 필요
    public BaseResponse<List<GetMemosResponse>> getMemos(@PathVariable("folderId") Long folderId){

        List<GetMemosResponse> getMemosRes=memoService.retrieveMemos(folderId);

        return new BaseResponse<>(getMemosRes);
    }

    /**
     * upload memo
     * @param postMemoReq - name, url, folder, member(uploader)
     * @return postMemoRes
     */
    @PostMapping("folder/memo")
    public BaseResponse<PostMemoResponse> uploadMemo(@RequestBody PostMemoRequest postMemoReq){

        PostMemoResponse postMemoRes=memoService.uploadMemo(postMemoReq);

        return new BaseResponse<>(postMemoRes);
    }

    /**
     * modify memo - title / content / title and content
     * @param memoId
     * @param patchMemoRequest
     * @return modifiedMemo
     */
    @PatchMapping("folder/memo/{memoId}")
    public BaseResponse<PatchMemoResponse> modifyMemo(@PathVariable("memoId") Long memoId, @RequestBody PatchMemoRequest patchMemoRequest){

        PatchMemoResponse modifiedMemo=memoService.modifyMemo(memoId,patchMemoRequest);

        return new BaseResponse<>(modifiedMemo);
    }

    /**
     * modify memo status TRUE to FALSE
     * @param memoId
     * @param memberId
     * @return modifiedMemoStatus
     */
    @PatchMapping("folder/memo/trash/{memoId}/{memberId}") // TODO: url 수정 필요
    public BaseResponse<PatchMemoStatusResponse> modifyMemoStatus(@PathVariable("memoId") Long memoId, @PathVariable("memberId") Long memberId){

        PatchMemoStatusResponse modifiedMemoStatus=memoService.modifyMemoStatus(memoId,memberId);

        return new BaseResponse<>(modifiedMemoStatus);
    }

    /**
     * 메모 북마크 등록
     * register memo in bookmark
     * @param memoId
     * @param memberId
     * @return addedBookmark
     */
    @PostMapping("folder/memo/bookmark/{memoId}/{memberId}")
    public BaseResponse<PostBookmarkResponse> addBookmark(@PathVariable("memoId") Long memoId, @PathVariable("memberId") Long memberId){

        PostBookmarkResponse addedBookmark=memoService.addBookmark(memoId,memberId);

        return new BaseResponse<>(addedBookmark);
    }


}
