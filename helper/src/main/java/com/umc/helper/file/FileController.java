package com.umc.helper.file;

import com.umc.helper.bookmark.model.PostBookmarkResponse;
import com.umc.helper.config.BaseResponse;
import com.umc.helper.file.model.*;
import com.umc.helper.image.model.PatchImageRequest;
import com.umc.helper.image.model.PatchImageResponse;
import com.umc.helper.link.model.PatchLinkStatusResponse;
import com.umc.helper.link.model.PostLinkRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FileController {

    private Logger logger= LoggerFactory.getLogger(FileController.class);

    private final FileService fileService;

    /**
     * retrieve files
     * @param folderId
     * @return getFilesRes
     */
    @GetMapping("folder/{folderId}/files")
    public BaseResponse<List<GetFilesResponse>> getFiles(@PathVariable("folderId") Long folderId){

        List<GetFilesResponse> getFilesRes=fileService.retrieveFiles(folderId);

        return new BaseResponse<>(getFilesRes);

    }

    /**
     * upload file
     * @param multipartFile
     * @param folderId
     * @param memberId
     * @return postFileRes
     */
    @PostMapping("folder/file") //BaseResponse<PostFileResponse>
    public BaseResponse<PostFileResponse> uploadFile(@RequestParam("multipartFile") MultipartFile multipartFile, @RequestParam("folderId") Long folderId, @RequestParam("memberId") Long memberId){
        PostFileRequest postFileReq=new PostFileRequest(multipartFile,folderId,memberId);
        PostFileResponse postFileRes=fileService.uploadFile(postFileReq);

        return new BaseResponse<>(postFileRes);
    }

    /**
     * modify file status TRUE to FALSE
     * @param fileId
     * @param memberId
     * @return modifiedFileStatus
     */
    @PatchMapping("folder/file/trash/{fileId}/{memberId}") // TODO: url 수정 필요
    public BaseResponse<PatchFileStatusResponse> modifyLinkStatus(@PathVariable("fileId") Long fileId, @PathVariable("memberId") Long memberId){

        PatchFileStatusResponse modifiedFileStatus=fileService.modifyFileStatus(fileId,memberId);

        return new BaseResponse<>(modifiedFileStatus);
    }

    /**
     * modify file name
     * @param fileId
     * @param patchFileReq
     * @return
     */
    @PatchMapping("folder/file/{fileId}")
    public BaseResponse<PatchFileResponse> modifyFile(@PathVariable("fileId") Long fileId, @RequestBody PatchFileRequest patchFileReq){
        PatchFileResponse modifiedFile=fileService.modifyFile(fileId,patchFileReq);

        return new BaseResponse<>(modifiedFile);
    }
    /**
     * 파일 북마크 등록
     * register file in bookmark
     * @param fileId
     * @param memberId
     * @return addedBookmark
     */
    @PostMapping("folder/file/bookmark/{fileId}/{memberId}")
    public BaseResponse<PostBookmarkResponse> addBookmark(@PathVariable("fileId") Long fileId, @PathVariable("memberId") Long memberId){

        PostBookmarkResponse addedBookmark=fileService.addBookmark(fileId,memberId);

        return new BaseResponse<>(addedBookmark);
    }
}
