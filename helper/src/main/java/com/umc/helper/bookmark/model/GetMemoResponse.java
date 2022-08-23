package com.umc.helper.bookmark.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetMemoResponse {
    private Long memoId;
    private String content;
    private String name;
    //private Long uploaderId;
    private Long folderId;
    private LocalDateTime uploadDate;
    private String uploader;

}
