package com.umc.helper.folder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostFolderRequest {

    private String folder_case;
    private Long id; // teamId or memberId
    private String folder_name;
    private Long creatorId;
}
