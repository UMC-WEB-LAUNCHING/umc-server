package com.umc.helper.link;

import com.umc.helper.config.BaseResponse;
import com.umc.helper.folder.Folder;
import com.umc.helper.folder.FolderRepository;
import com.umc.helper.link.model.*;
import com.umc.helper.member.Member;
import com.umc.helper.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;


@RestController
@RequiredArgsConstructor
public class LinkController {

    private final LinkRepositoryJpa linkRepositoryJpa;
    private final FolderRepository folderRepository;
    private final MemberRepository memberRepository;
    private final LinkService linkService;

    Logger log= LoggerFactory.getLogger(LinkController.class);


    /**
     * retrieve links
     * @param folderId
     * @return getLinksRes
     */
    //link/{folderId}
    @GetMapping("folder/{folderId}/links") // url이 뭔가 이상함 TODO: url 수정 필요 이름으로 받아서 id로 찾기
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

        ////////////////////////////////////// 되는 코드 controller에 너무 많은 로직 있는 거 같아서 service로 옮김
//        Optional<Folder> folder=folderRepository.findById(postLinkReq.getFolderId());
//        Optional<Member> member=memberRepository.findById(postLinkReq.getMemberId());
//
//        Link link=new Link();
//        link.setName(postLinkReq.getName());
//        link.setUrl(postLinkReq.getUrl());
//        link.setFolder(folder.get());
//        link.setMember(member.get());
//
//        PostLinkResponse postLinkRes=linkService.uploadLink(link);
//
//        log.info("postLinkRes - id: {}",postLinkRes.getLinkId());
        /////////////////////////////////////////////////////////////////////

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

    // 링크 쓰레기통으로,,,(링크 삭제) 여기서는 patch로 링크 엔티티 상태 수정해주고 쓰레기통에서 delete method로 삭제 진행

}
