package com.umc.helper.memo.model;

import com.umc.helper.link.model.Link;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@NoArgsConstructor
@Data
public class GetMemosResponse {

    private Long memoId;
    private String content;
    private String name; // 메모 제목
    private String member_name; //uploader
    private String uploaderImage;
    private String folder_name;
    private Long folderId;
    private Long bookmarkId;
    private LocalDateTime uploadDate;
    private LocalDateTime lastModifiedDate;

    public GetMemosResponse(Memo memo){
        memoId=memo.getId();
        content=memo.getContent();
        name=memo.getName();
        member_name=memo.getMember().getUsername();
        folder_name=memo.getFolder().getFolderName();
        lastModifiedDate=memo.getLastModifiedDate();
    }

    public GetMemosResponse(Long memoId,String name, String content, String member_name, String folderName, Long folderId,Long bookmarkId,LocalDateTime uploadDate,LocalDateTime lastModifiedDate,String uploaderImage){
        this.memoId=memoId;
        this.content=content;
        this.name=name;
        this.member_name=member_name;
        this.folder_name=folderName;
        this.folderId=folderId;
        this.bookmarkId=bookmarkId;
        this.uploadDate=uploadDate;
        this.lastModifiedDate=lastModifiedDate;
        this.uploaderImage=uploaderImage;
    }

}
