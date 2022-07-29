package com.umc.helper.link.model;

import com.umc.helper.folder.Folder;
import com.umc.helper.member.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetLinksResponse {

    private Long linkId;
    private String url;
    private String name;
    private String member_name; //uploader
    private String folder_name;
//    private LocalDateTime uploadDate;
//    private LocalDateTime lastModifiedDate;

    public GetLinksResponse(Link link){
        linkId=link.getId();
        url=link.getUrl();
        name=link.getName();
        member_name=link.getMember().getUsername();
        folder_name=link.getFolder().getFolderName();
    }


}
