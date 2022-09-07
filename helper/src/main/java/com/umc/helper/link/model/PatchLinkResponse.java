package com.umc.helper.link.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatchLinkResponse {

    private Long linkId;
    private String url;
    private String name;
    private String member_name;
    private String folder_name;

    public PatchLinkResponse(Link link){
        linkId=link.getId();
        url=link.getUrl();
        name=link.getName();
        member_name=link.getMember().getUsername();
        folder_name=link.getFolder().getFolderName();
    }
}
