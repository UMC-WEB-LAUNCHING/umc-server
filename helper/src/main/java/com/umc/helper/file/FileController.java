package com.umc.helper.file;

import com.umc.helper.config.BaseResponse;
import com.umc.helper.file.model.GetFilesResponse;
import com.umc.helper.file.model.PatchFileStatusResponse;
import com.umc.helper.file.model.PostFileRequest;
import com.umc.helper.file.model.PostFileResponse;
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
}
