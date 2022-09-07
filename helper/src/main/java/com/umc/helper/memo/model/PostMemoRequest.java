package com.umc.helper.memo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostMemoRequest {
    @Valid
    private String name;
    @Valid
    private String content;
    @Valid
    private Long folderId;
    @Valid
    private Long memberId; //uploader
}
