package com.umc.helper.file;

import com.umc.helper.config.BaseResponse;
import com.umc.helper.file.model.PostFileRequest;
import com.umc.helper.file.model.PostFileResponse;
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

@RestController
@RequiredArgsConstructor
public class FileController {

    private Logger logger= LoggerFactory.getLogger(FileController.class);

    private final FileService fileService;

    // file 조회
//    @GetMapping("folder/{folderId}/files")
//    public BaseResponse<List<GetFilesResponse>> getFiles(@PathVariable("folderId") Long folderId){
//
//
//    }
   // file upload
    @PostMapping("folder/file") //BaseResponse<PostFileResponse>
    public BaseResponse<PostFileResponse> uploadFile(@RequestParam("multipartFile") MultipartFile multipartFile, @RequestParam("folderId") Long folderId, @RequestParam("memberId") Long memberId){
        PostFileRequest postFileReq=new PostFileRequest(multipartFile,folderId,memberId);
        PostFileResponse postFileRes=fileService.uploadFile(postFileReq);

        return new BaseResponse<>(postFileRes);


    }
    // file 수정

    // file 삭제
}
