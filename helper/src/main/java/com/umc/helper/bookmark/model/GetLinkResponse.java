package com.umc.helper.bookmark.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class GetLinkResponse {
    private Long linkId;
    //private Long uploaderId;
    private String url;
    private String name;
    private Long folderId;
    private LocalDateTime uploadDate;
}
