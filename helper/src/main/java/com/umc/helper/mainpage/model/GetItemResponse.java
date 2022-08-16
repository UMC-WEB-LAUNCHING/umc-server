package com.umc.helper.mainpage.model;

import com.umc.helper.file.model.GetFilesResponse;
import com.umc.helper.image.model.GetImagesResponse;
import com.umc.helper.link.model.GetLinksResponse;
import com.umc.helper.memo.model.GetMemosResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetItemResponse implements Comparable<GetItemResponse>{
    private LocalDateTime lastModifiedDate;
    private String category;
    private GetFilesResponse file;
    private GetLinksResponse link;
    private GetMemosResponse memo;
    private GetImagesResponse image;

    @Override
    public int compareTo(GetItemResponse getItemResponse){
        System.out.println("getItemResponse: "+getItemResponse.getLastModifiedDate());

        if(getItemResponse.getLastModifiedDate().compareTo(this.getLastModifiedDate())==1){
            return 1;
        }
        else if(getItemResponse.getLastModifiedDate().compareTo(this.getLastModifiedDate())==-1){
            return -1;
        }
        return 0;
    }
}
