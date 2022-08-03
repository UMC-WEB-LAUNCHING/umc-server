package com.umc.helper.folder.model;

import com.umc.helper.file.model.File;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatchFolderResponse {
    private Long folderId;
    private Boolean status;

    public PatchFolderResponse(Folder folder){
        folderId=folder.getId();
        status=folder.getStatus();
    }
}
