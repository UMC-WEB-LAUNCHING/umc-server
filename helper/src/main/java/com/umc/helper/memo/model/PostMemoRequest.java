package com.umc.helper.memo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostMemoRequest {
    private String name;
    private String content;
    private Long folderId;
    private Long memberId; //uploader
}
