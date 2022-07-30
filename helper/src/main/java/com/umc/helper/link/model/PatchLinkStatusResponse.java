package com.umc.helper.link.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatchLinkStatusResponse {
    private Long linkId;
    private Boolean status;

    public PatchLinkStatusResponse(Link link){
        linkId=link.getId();
        status=link.getStatus();
    }
}
