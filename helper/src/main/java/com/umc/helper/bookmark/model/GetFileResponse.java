package com.umc.helper.bookmark.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@NoArgsConstructor
@Data
@AllArgsConstructor
public class GetFileResponse {
    private Long fileId;
    private String filePath;
    private String originalFileName;
    private Long folderId;
    private LocalDateTime uploadDate;
    private Long volume;
}
