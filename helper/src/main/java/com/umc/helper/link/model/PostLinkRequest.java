package com.umc.helper.link.model;

import lombok.*;

import javax.validation.Valid;

@AllArgsConstructor @NoArgsConstructor
@Data
public class PostLinkRequest {
    @Valid
    private String name;
    @Valid
    private String url;
    @Valid
    private Long folderId;
    @Valid
    private Long memberId; // uploader

}
