package com.umc.helper.folder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostFolderResponse {
    private String folder_case;
    private Long folderId;
    private String folder_name;

}
