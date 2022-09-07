package com.umc.helper.folder;

import com.umc.helper.bookmark.model.PostBookmarkResponse;
import com.umc.helper.config.BaseResponse;
import com.umc.helper.folder.model.GetFoldersResponse;
import com.umc.helper.folder.model.PatchFolderResponse;
import com.umc.helper.folder.model.PostFolderRequest;
import com.umc.helper.folder.model.PostFolderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;

    /**
     * create folder
     * @param postFolderReq
     * @return postFolderRes
     */
    @PostMapping("folder")
    public BaseResponse<PostFolderResponse> createFolder(@RequestBody @Valid PostFolderRequest postFolderReq){
        PostFolderResponse postFolderRes=folderService.createFolder(postFolderReq);

        return new BaseResponse<>(postFolderRes);
    }

    /**
     * retrieve folders
     * @param folder_case
     * @param id
     * @return getFoldersRes
     */
    @GetMapping("folder/{folder_case}/{id}") // folder_case: team, member / id: team_id or member_id
    public BaseResponse<List<GetFoldersResponse>> getFolders(@PathVariable("folder_case") @Valid String folder_case, @PathVariable("id") @Valid Long id){
        List<GetFoldersResponse> getFoldersRes=folderService.retrieveFolder(folder_case,id);

        return new BaseResponse<>(getFoldersRes);
    }

    /**
     * modify folder status TRUE to FALSE
     * @param folder_id
     * @param creator_id
     * @return patchFolderRes
     */
    @PatchMapping("folder/{folder_id}/{creator_id}")
    public BaseResponse<PatchFolderResponse> modifyFolderStatus(@PathVariable("folder_id") @Valid Long folder_id,@PathVariable("creator_id") @Valid Long creator_id){
        PatchFolderResponse patchFolderRes=folderService.modifyFolderStatus(folder_id,creator_id);

        return new BaseResponse<>(patchFolderRes);
    }

    /**
     * 폴더 북마크 등록
     * @param folderId
     * @param memberId
     * @return addedBookmark
     */
    @PostMapping("folder/bookmark/{folderId}/{memberId}")
    public BaseResponse<PostBookmarkResponse> addBookmark(@PathVariable("folderId") @Valid Long folderId, @PathVariable("memberId") @Valid Long memberId){

        PostBookmarkResponse addedBookmark=folderService.addBookmark(folderId,memberId);

        return new BaseResponse<>(addedBookmark);
    }
}
