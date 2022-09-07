package com.umc.helper.bookmark.model;

import com.umc.helper.file.model.File;
import com.umc.helper.file.model.GetFilesResponse;
import com.umc.helper.folder.model.Folder;
import com.umc.helper.image.model.GetImagesResponse;
import com.umc.helper.image.model.Image;
import com.umc.helper.link.model.GetLinksResponse;
import com.umc.helper.link.model.Link;
import com.umc.helper.memo.model.GetMemosResponse;
import com.umc.helper.memo.model.Memo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Data
@AllArgsConstructor
public class GetBookmarksResponse implements Comparable<GetBookmarksResponse>{

    private Long bookmarkId;
    private String category;
    private GetFileResponse file;
    private GetLinkResponse link;
    private GetMemoResponse memo;
    //private GetImageResponse image;
    //private Folder folder;
    private LocalDateTime uploadDate;

//    public GetBookmarksResponse(Bookmark bookmark){
//        bookmarkId=bookmark.getId();
//        category= bookmark.getCategory();
//        if(bookmark.getFile()!=null) {
//            file=bookmark.getFile();
//        }
//        else fileId= null;
//        if(bookmark.getImage()!=null) image=bookmark.getImage();
//        else imageId= null;
//        if(bookmark.getLink()!=null) linkId=bookmark.getLink().getId();
//        else linkId= null;
//        if(bookmark.getMemo()!=null) memoId=bookmark.getMemo().getId();
//        else memoId= null;
//        if(bookmark.getFolder()!=null) folderId=bookmark.getFolder().getId();
//        else folderId= null;
//        memberId=bookmark.getMember().getId();
//        addedDate=bookmark.getAddedDate();
//    }

    @Override
    public int compareTo(GetBookmarksResponse getBookmarksResponse){
        if(getBookmarksResponse.getUploadDate().compareTo(this.getUploadDate())==1){
            return 1;
        }
        else if(getBookmarksResponse.getUploadDate().compareTo(this.getUploadDate())==-1){
            return -1;
        }
        return 0;
    }
}
