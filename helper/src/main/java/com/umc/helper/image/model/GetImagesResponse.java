package com.umc.helper.image.model;

import com.umc.helper.file.model.File;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetImagesResponse {

    private Long imageId;
    private String filePath;
    private String originalFileName;
    private String member_name; // uploader
    private String folder_name;

    public GetImagesResponse(Image image){
        imageId=image.getId();
        filePath=image.getFilePath();
        originalFileName=image.getOriginalFileName();
        member_name=image.getMember().getUsername();
        folder_name=image.getFolder().getFolderName();
    }
}
