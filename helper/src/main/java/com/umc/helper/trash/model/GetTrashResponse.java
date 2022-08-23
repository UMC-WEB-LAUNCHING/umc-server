package com.umc.helper.trash.model;

import com.umc.helper.file.model.File;
import com.umc.helper.file.model.GetFilesResponse;
import com.umc.helper.folder.model.Folder;
import com.umc.helper.folder.model.GetFoldersResponse;
import com.umc.helper.image.model.GetImagesResponse;
import com.umc.helper.image.model.Image;
import com.umc.helper.link.model.GetLinksResponse;
import com.umc.helper.link.model.Link;
import com.umc.helper.memo.model.GetMemosResponse;
import com.umc.helper.memo.model.Memo;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class GetTrashResponse implements Comparable<GetTrashResponse>{

    private String category;
    private GetMemosResponse memo;
    private GetFilesResponse file;
    private GetLinksResponse link;
    private GetImagesResponse image;
    private GetFoldersResponse folder;
    private String memberName;
    private LocalDateTime modifiedStatusDate;

    //Logger logger= LoggerFactory.getLogger(GetTrashResponse.class);

   public GetTrashResponse(LocalDateTime modifiedStatusDate,String category, GetMemosResponse memo,GetFilesResponse file,GetLinksResponse link,GetImagesResponse image,GetFoldersResponse folder){
       this.modifiedStatusDate=modifiedStatusDate;
       this.category=category;
       this.memo=memo;
       this.file=file;
       this.link=link;
       this.image=image;
       this.folder=folder;
   }

    public int compareTo(GetTrashResponse getTrashResponse){
       //logger.info("modifiedStatusDate: {}",getTrashResponse.modifiedStatusDate);
        if(getTrashResponse.getModifiedStatusDate().compareTo(this.getModifiedStatusDate())==1){
            return 1;
        }
        else if(getTrashResponse.getModifiedStatusDate().compareTo(this.getModifiedStatusDate())==-1){
            return -1;
        }
        return 0;
    }
}
