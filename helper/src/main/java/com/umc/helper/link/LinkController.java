package com.umc.helper.link;

import com.umc.helper.bookmark.model.PostBookmarkResponse;
import com.umc.helper.config.BaseResponse;
import com.umc.helper.link.model.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;


@RestController
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;

    Logger log= LoggerFactory.getLogger(LinkController.class);


    /**
     * retrieve links
     * @param folderId
     * @return getLinksRes
     */
    //link/{folderId}
    @GetMapping("folder/{folderId}/links") // url이 뭔가 이상함
    public BaseResponse<List<GetLinksResponse>> getLinks(@PathVariable("folderId") Long folderId){

        List<GetLinksResponse> getLinksRes=linkService.retrieveLinks(folderId);

        return new BaseResponse<>(getLinksRes);
    }

    /**
     * upload link
     * @param postLinkReq - name, url, folder, member(uploader)
     * @return postLinkRes
     */
    @PostMapping("folder/link")
    public BaseResponse<PostLinkResponse> uploadLink(@RequestBody PostLinkRequest postLinkReq){

        PostLinkResponse postLinkRes=linkService.uploadLink(postLinkReq);

        return new BaseResponse<>(postLinkRes);
    }

    /**
     * modify link name(title)
     * @param linkId
     * @param patchLinkRequest
     * @return modifiedLink
     */
    @PatchMapping("folder/link/{linkId}")
    public BaseResponse<PatchLinkResponse> modifyLink(@PathVariable("linkId") Long linkId, @RequestBody PatchLinkRequest patchLinkRequest){

        PatchLinkResponse modifiedLink=linkService.modifyLink(linkId,patchLinkRequest);

        return new BaseResponse<>(modifiedLink);
    }

    /**
     * modify link status TRUE to FALSE
     * @param linkId
     * @param memberId
     * @return modifiedLinkStatus
     */
    @PatchMapping("folder/link/trash/{linkId}/{memberId}")
    public BaseResponse<PatchLinkStatusResponse> modifyLinkStatus(@PathVariable("linkId") Long linkId,@PathVariable("memberId") Long memberId){

        PatchLinkStatusResponse modifiedLinkStatus=linkService.modifyLinkStatus(linkId,memberId);

        return new BaseResponse<>(modifiedLinkStatus);
    }

    /**
     * 링크 북마크 등록
     * register link in bookmark
     * @param linkId
     * @param memberId
     * @return addedBookmark
     */
    @PostMapping("folder/link/bookmark/{linkId}/{memberId}")
    public BaseResponse<PostBookmarkResponse> addBookmark(@PathVariable("linkId") Long linkId, @PathVariable("memberId") Long memberId){

        PostBookmarkResponse addedBookmark=linkService.addBookmark(linkId,memberId);

        return new BaseResponse<>(addedBookmark);
    }
}
