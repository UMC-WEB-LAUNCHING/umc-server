package com.umc.helper.bookmark.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Data
public class GetBookmarksResponse {

    private Long bookmarkId;
    private String category;
    private Long memoId;
    private Long fileId;
    private Long imageId;
    private Long linkId;

    private Long folderId;
    private Long memberId; // 북마크한 사람 id
    private LocalDateTime addedDate;

    public GetBookmarksResponse(Bookmark bookmark){
        bookmarkId=bookmark.getId();
        category= bookmark.getCategory();
        if(bookmark.getFile()!=null) fileId=bookmark.getFile().getId();
        else fileId= null;
        if(bookmark.getImage()!=null) imageId=bookmark.getImage().getId();
        else imageId= null;
        if(bookmark.getLink()!=null) linkId=bookmark.getLink().getId();
        else linkId= null;
        if(bookmark.getMemo()!=null) memoId=bookmark.getMemo().getId();
        else memoId= null;
        if(bookmark.getFolder()!=null) folderId=bookmark.getFolder().getId();
        else folderId= null;
        memberId=bookmark.getMember().getId();
        addedDate=bookmark.getAddedDate();
    }
}
