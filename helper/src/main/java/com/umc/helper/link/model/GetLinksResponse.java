package com.umc.helper.link.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class GetLinksResponse {

    private Long linkId;
    private String url;
    private String name;
    private String member_name; //uploader
    private String folder_name;

    private Long bookmarkId;
    private LocalDateTime uploadDate;
    private LocalDateTime lastModifiedDate;

    public GetLinksResponse(Link link){
        linkId=link.getId();
        url=link.getUrl();
        name=link.getName();
        member_name=link.getMember().getUsername();
        folder_name=link.getFolder().getFolderName();
    }

    public GetLinksResponse(Long linkId,String url, String name, String member_name, String folderName, Long bookmarkId,LocalDateTime uploadDate,LocalDateTime lastModifiedDate){
        this.linkId=linkId;
        this.url=url;
        this.name=name;
        this.member_name=member_name;
        this.folder_name=folderName;
        this.bookmarkId=bookmarkId;
        this.uploadDate=uploadDate;
        this.lastModifiedDate=lastModifiedDate;
    }

}
