package com.umc.helper.link.model;

import lombok.*;

@RequiredArgsConstructor @AllArgsConstructor
@Data
public class PostLinkResponse {

    private Long linkId;
    private String url;
    private String name;

}
