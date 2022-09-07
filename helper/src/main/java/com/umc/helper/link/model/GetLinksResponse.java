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
    private String uploaderProfile;
    private String folder_name;
    private Long folderId;
    private Long bookmarkId;
    private LocalDateTime uploadDate;
    private LocalDateTime lastModifiedDate;

    public GetLinksResponse(Link link){
        linkId=link.getId();
        url=link.getUrl();
        name=link.getName();
        member_name=link.getMember().getUsername();
        folder_name=link.getFolder().getFolderName();
        lastModifiedDate=link.getLastModifiedDate();
    }

    public GetLinksResponse(Long linkId,String url, String name, String member_name, String folderName,Long folderId, Long bookmarkId,LocalDateTime uploadDate,LocalDateTime lastModifiedDate,String uploaderProfile){
        this.linkId=linkId;
        this.url=url;
        this.name=name;
        this.member_name=member_name;
        this.folder_name=folderName;
        this.folderId=folderId;
        this.bookmarkId=bookmarkId;
        this.uploadDate=uploadDate;
        this.lastModifiedDate=lastModifiedDate;
        this.uploaderProfile=uploaderProfile;
    }

}
