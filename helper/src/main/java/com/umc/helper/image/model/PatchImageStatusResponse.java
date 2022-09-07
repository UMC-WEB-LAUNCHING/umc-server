package com.umc.helper.image.model;

import com.umc.helper.file.model.File;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatchImageStatusResponse {

    private Long imageId;
    private Boolean status;

    public PatchImageStatusResponse(Image image){
        imageId=image.getId();
        status=image.getStatus();
    }
}
