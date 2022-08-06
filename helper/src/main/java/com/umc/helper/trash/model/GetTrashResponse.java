package com.umc.helper.trash.model;

import com.umc.helper.file.model.File;
import com.umc.helper.file.model.GetFilesResponse;
import com.umc.helper.image.model.Image;
import com.umc.helper.link.model.Link;
import com.umc.helper.memo.model.Memo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class GetTrashResponse implements Comparable<GetTrashResponse>{

    private String category;
    private Long memoId;
    private Long fileId;
    private Long imageId;
    private Long linkId;
    private Long folderId;
    private String memberName;
    private LocalDateTime modifiedStatusDate;

    public GetTrashResponse(String category, Long memoId,Long fileId, Long imageId, Long linkId,String memberName){
        this.category=category;
        this.memoId=memoId;
        this.fileId=fileId;
        this.imageId=imageId;
        this.linkId=linkId;
        this.memberName=memberName;
    }

    public GetTrashResponse(Memo memo){
        this.category="memo";
        this.memoId=memo.getId();
        this.memberName=memo.getMember().getUsername();
        this.modifiedStatusDate=memo.getStatusModifiedDate();
    }
    public GetTrashResponse(File file){
        this.category="file";
        this.fileId=file.getId();
        this.memberName=file.getMember().getUsername();
        this.modifiedStatusDate=file.getStatusModifiedDate();

    }
    public GetTrashResponse(Image image){
        this.category="image";
        this.imageId=image.getId();
        this.memberName=image.getMember().getUsername();
        this.modifiedStatusDate=image.getStatusModifiedDate();

    }
    public GetTrashResponse(Link link){
        this.category="link";
        this.linkId=link.getId();
        this.memberName=link.getMember().getUsername();
        this.modifiedStatusDate=link.getStatusModifiedDate();
    }

    public int compareTo(GetTrashResponse getTrashResponse){
        if(getTrashResponse.getModifiedStatusDate().compareTo(this.getModifiedStatusDate())==1){
            return 1;
        }
        else if(getTrashResponse.getModifiedStatusDate().compareTo(this.getModifiedStatusDate())==-1){
            return -1;
        }
        return 0;
    }
}
