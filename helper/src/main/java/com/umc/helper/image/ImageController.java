package com.umc.helper.image;

import com.umc.helper.bookmark.model.PostBookmarkResponse;
import com.umc.helper.config.BaseResponse;
import com.umc.helper.file.FileController;
import com.umc.helper.file.FileService;
import com.umc.helper.file.model.GetFilesResponse;
import com.umc.helper.file.model.PatchFileStatusResponse;
import com.umc.helper.file.model.PostFileRequest;
import com.umc.helper.file.model.PostFileResponse;
import com.umc.helper.image.model.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private Logger logger= LoggerFactory.getLogger(FileController.class);

    private final ImageService imageService;

    /**
     * 폴더 내 이미지 조회
     * retrieve images
     * @param folderId
     * @return getImagesRes
     */
    @GetMapping("folder/{folderId}/images")
    public BaseResponse<List<GetImagesResponse>> getImages(@PathVariable("folderId") @Valid Long folderId){

        List<GetImagesResponse> getImagesRes=imageService.retrieveImages(folderId);

        return new BaseResponse<>(getImagesRes);

    }

    /**
     * upload image
     * @param multipartFile
     * @param folderId
     * @param memberId
     * @return postImageRes
     */
    @PostMapping("folder/image") //BaseResponse<PostFileResponse>
    public BaseResponse<PostImageResponse> uploadImage(@RequestParam("multipartFile") @Valid  MultipartFile multipartFile, @RequestParam("folderId") @Valid  Long folderId, @RequestParam("memberId") @Valid  Long memberId){
        PostImageRequest postImageReq=new PostImageRequest(multipartFile,folderId,memberId);
        PostImageResponse postImageRes=imageService.uploadImage(postImageReq);

        return new BaseResponse<>(postImageRes);
    }

    /**
     * modify image status TRUE to FALSE
     * @param imageId
     * @param memberId
     * @return modifiedImageStatus
     */
    @PatchMapping("folder/image/trash/{imageId}/{memberId}") // TODO: url 수정 필요
    public BaseResponse<PatchImageStatusResponse> modifyLinkStatus(@PathVariable("imageId") @Valid  Long imageId, @PathVariable("memberId") @Valid  Long memberId){

        PatchImageStatusResponse modifiedImageStatus=imageService.modifyImageStatus(imageId,memberId);

        return new BaseResponse<>(modifiedImageStatus);
    }

    /**
     * modify image name
     * @param imageId
     * @param patchImageReq
     * @return
     */
    @PatchMapping("folder/image/{imageId}")
    public BaseResponse<PatchImageResponse> modifyImage(@PathVariable("imageId") @Valid  Long imageId, @RequestBody @Valid PatchImageRequest patchImageReq){
        PatchImageResponse modifiedImage=imageService.modifyImage(imageId,patchImageReq);

        return new BaseResponse<>(modifiedImage);
    }
    /**
     * 이미지 북마크 등록
     * register image in bookmark
     * @param imageId
     * @param memberId
     * @return addedBookmark
     */
    @PostMapping("folder/image/bookmark/{folderId}/{imageId}/{memberId}")
    public BaseResponse<PostBookmarkResponse> addBookmark(@PathVariable("folderId") @Valid  Long folderId,@PathVariable("imageId") @Valid  Long imageId, @PathVariable("memberId") @Valid  Long memberId){

        PostBookmarkResponse addedBookmark=imageService.addBookmark(folderId,imageId,memberId);

        return new BaseResponse<>(addedBookmark);
    }

}
