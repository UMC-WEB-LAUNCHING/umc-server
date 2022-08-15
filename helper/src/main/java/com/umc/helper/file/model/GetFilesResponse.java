package com.umc.helper.file.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class GetFilesResponse implements Comparable<GetFilesResponse>{
    private Long fileId;
    private String filePath;
    private String originalFileName;
    private String member_name; // uploader
    private String uploaderProfile; // 프로필 사진
    private String folder_name;
    private Long folderId;
    private Long bookmarkId;

    private LocalDateTime uploadDate;
    private LocalDateTime lastModifiedDate;
    private Long volume;
    public GetFilesResponse(File file){
        fileId=file.getId();
        filePath=file.getFilePath();
        originalFileName=file.getOriginalFileName();
        member_name=file.getMember().getUsername();
        folder_name=file.getFolder().getFolderName();
        lastModifiedDate=file.getLastModifiedDate();
    }

    public GetFilesResponse(Long fileId,String filePath, String originalFileName, String member_name, String folderName, Long folderId,Long bookmarkId,LocalDateTime uploadDate,LocalDateTime lastModifiedDate,Long volume,String uploaderProfile){
        this.fileId=fileId;
        this.filePath=filePath;
        this.originalFileName=originalFileName;
        this.member_name=member_name;
        this.folder_name=folderName;
        this.folderId=folderId;
        this.bookmarkId=bookmarkId;
        this.uploadDate=uploadDate;
        this.volume=volume;
        this.lastModifiedDate=lastModifiedDate;
        this.uploaderProfile=uploaderProfile;
    }

    @Override
    public int compareTo(GetFilesResponse getFilesResponse){
        System.out.println("getFilesResponse: "+getFilesResponse.getLastModifiedDate());

        if(getFilesResponse.getLastModifiedDate().compareTo(this.getLastModifiedDate())==1){
            return 1;
        }
        else if(getFilesResponse.getLastModifiedDate().compareTo(this.getLastModifiedDate())==-1){
            return -1;
        }
        return 0;
    }
}
