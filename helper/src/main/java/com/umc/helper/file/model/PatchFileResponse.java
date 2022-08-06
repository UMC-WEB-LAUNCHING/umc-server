package com.umc.helper.file.model;

import com.umc.helper.image.model.Image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatchFileResponse {
    private Long fileId;
    private String name;
    private String filePath;
    private String member_name;
    private String folder_name;

    public PatchFileResponse(File file){
        fileId=file.getId();
        name=file.getOriginalFileName();
        filePath=file.getFilePath();
        member_name=file.getMember().getUsername();
        folder_name=file.getFolder().getFolderName();
    }
}
