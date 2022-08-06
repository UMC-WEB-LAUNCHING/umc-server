package com.umc.helper.image.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatchImageResponse {
    private Long imageId;
    private String name;
    private String filePath;
    private String member_name;
    private String folder_name;

    public PatchImageResponse(Image image){
        imageId=image.getId();
        name=image.getOriginalFileName();
        filePath=image.getFilePath();
        member_name=image.getMember().getUsername();
        folder_name=image.getFolder().getFolderName();
    }
}
