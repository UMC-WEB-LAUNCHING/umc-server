package com.umc.helper.file.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetFilesResponse {
    private Long fileId;
    private String filePath;
    private String originalFileName;
    private String member_name; // uploader
    private String folder_name;

    public GetFilesResponse(File file){
        fileId=file.getId();
        filePath=file.getFilePath();
        originalFileName=file.getOriginalFileName();
        member_name=file.getMember().getUsername();
        folder_name=file.getFolder().getFolderName();
    }
}
