package com.umc.helper.link;

import com.umc.helper.link.model.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LinkService {

    //private final LinkRepositoryJpa linkRepositoryJpa;
    private final LinkRepository linkRepository;
    Logger log= LoggerFactory.getLogger(LinkService.class);

    /**
     *  해당 폴더 모든 링크 조회
     */
    @Transactional
    public List<GetLinksResponse> retrieveLinks(Long folderId){

        List<Link> links= linkRepository.findAllByFolderId(folderId);
        List<GetLinksResponse> result=links.stream()
                .map(l->new GetLinksResponse(l))
                .collect(toList());

        return result;
    }

    /**
     *  링크 업로드
     */
    @Transactional
    public PostLinkResponse uploadLink(Link link){
        linkRepository.save(link);

        return new PostLinkResponse(linkRepository.findById(link.getId()).getId());
        //return new PostLinkResponse(linkRepositoryJpa.findById(link.getId()).get().getId());
    }

    /**
     *  링크 변경 - 제목
     */
    @Transactional
    public PatchLinkResponse modifyLink(Long linkId, PatchLinkRequest patchLinkRequest){

        Link link=linkRepository.findById(linkId);
        link.setName(patchLinkRequest.getName());

        return new PatchLinkResponse(link);
    }

}
