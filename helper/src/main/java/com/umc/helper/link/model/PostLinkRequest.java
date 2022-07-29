package com.umc.helper.link.model;

import com.umc.helper.folder.Folder;
import com.umc.helper.member.Member;
import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Data
public class PostLinkRequest {
    private String name;
    private String url;
    private Long folderId;
    private Long memberId; // uploader

}
