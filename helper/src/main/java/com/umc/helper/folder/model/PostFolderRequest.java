package com.umc.helper.folder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostFolderRequest {

    @Valid
    private String folder_case;
    @Valid
    private Long id; // teamId or memberId
    @Valid
    private String folder_name;
    @Valid
    private Long creatorId;
}
