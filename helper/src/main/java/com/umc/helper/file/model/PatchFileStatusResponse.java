package com.umc.helper.file.model;

import com.umc.helper.link.model.Link;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatchFileStatusResponse {
    private Long fileId;
    private Boolean status;

    public PatchFileStatusResponse(File file){
        fileId=file.getId();
        status=file.getStatus();
    }
}
