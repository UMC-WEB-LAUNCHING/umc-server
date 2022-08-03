package com.umc.helper.folder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetFoldersResponse {
    private Long folderId;
    private String folderName;
    private Long creatorId;

    public GetFoldersResponse(Folder folder){
        this.folderId=folder.getId();
        this.folderName=folder.getFolderName();
        this.creatorId=folder.getCreatorId();
    }

}
