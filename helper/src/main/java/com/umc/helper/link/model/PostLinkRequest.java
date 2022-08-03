package com.umc.helper.link.model;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Data
public class PostLinkRequest {
    private String name;
    private String url;
    private Long folderId;
    private Long memberId; // uploader

}
