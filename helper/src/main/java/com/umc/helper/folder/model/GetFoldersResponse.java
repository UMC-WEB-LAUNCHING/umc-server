package com.umc.helper.folder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetFoldersResponse implements Comparable<GetFoldersResponse>{
    private Long folderId;
    private String folderName;
    private Long creatorId;
    private LocalDateTime lastModifiedDate;
    public GetFoldersResponse(Folder folder){
        this.folderId=folder.getId();
        this.folderName=folder.getFolderName();
        this.creatorId=folder.getCreatorId();
        this.lastModifiedDate=folder.getLastModifiedDate();
    }

    @Override
    public int compareTo(GetFoldersResponse getFoldersResponse){
        if(getFoldersResponse.getLastModifiedDate().compareTo(this.getLastModifiedDate())==1){
            return 1;
        }
        else if(getFoldersResponse.getLastModifiedDate().compareTo(this.getLastModifiedDate())==-1){
            return -1;
        }
        return 0;
    }
}
