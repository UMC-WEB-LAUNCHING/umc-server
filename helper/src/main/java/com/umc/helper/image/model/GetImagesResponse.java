package com.umc.helper.image.model;

import com.umc.helper.file.model.File;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@NoArgsConstructor
@Data
public class GetImagesResponse {

    private Long imageId;
    private String filePath;
    private String originalFileName;
    private String member_name; // uploader
    private String uploaderImage;
    private String folder_name;

    private Long bookmarkId;
    private Long volume;
    private LocalDateTime uploadDate;
    private LocalDateTime lastModifiedDate;

    public GetImagesResponse(Image image){
        imageId=image.getId();
        filePath=image.getFilePath();
        originalFileName=image.getOriginalFileName();
        member_name=image.getMember().getUsername();
        folder_name=image.getFolder().getFolderName();
        lastModifiedDate=image.getLastModifiedDate();
    }

    public GetImagesResponse(Long imageId,String filePath, String originalFileName, String member_name, String folderName, Long bookmarkId,Long volume,LocalDateTime uploadDate,LocalDateTime lastModifiedDate,String uploaderImage){
        this.imageId=imageId;
        this.filePath=filePath;
        this.originalFileName=originalFileName;
        this.member_name=member_name;
        this.folder_name=folderName;
        this.bookmarkId=bookmarkId;
        this.volume=volume;
        this.uploadDate=uploadDate;
        this.lastModifiedDate=lastModifiedDate;
        this.uploaderImage=uploaderImage;
    }
}
