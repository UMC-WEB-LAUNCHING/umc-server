package com.umc.helper.file.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class GetFilesResponse {
    private Long fileId;
    private String filePath;
    private String originalFileName;
    private String member_name; // uploader
    private String folder_name;

    private Long bookmarkId;

    private LocalDateTime uploadDate;
    //private LocalDateTime lastModifiedDate;


    public GetFilesResponse(File file){
        fileId=file.getId();
        filePath=file.getFilePath();
        originalFileName=file.getOriginalFileName();
        member_name=file.getMember().getUsername();
        folder_name=file.getFolder().getFolderName();
    }

    public GetFilesResponse(Long fileId,String filePath, String originalFileName, String member_name, String folderName, Long bookmarkId,LocalDateTime uploadDate){
        this.fileId=fileId;
        this.filePath=filePath;
        this.originalFileName=originalFileName;
        this.member_name=member_name;
        this.folder_name=folderName;
        this.bookmarkId=bookmarkId;
        this.uploadDate=uploadDate;
        //this.lastModifiedDate=lastModifiedDate;
    }
}
