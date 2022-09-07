package com.umc.helper.search.model;

import com.umc.helper.file.model.GetFilesResponse;
import com.umc.helper.image.model.GetImagesResponse;
import com.umc.helper.link.model.GetLinksResponse;
import com.umc.helper.memo.model.GetMemosResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class GetSearchedResponse implements Comparable<GetSearchedResponse>{

    private String itemCategory;
    private GetFilesResponse getFilesResponse;
    private GetLinksResponse getLinksResponse;
    private GetMemosResponse getMemosResponse;
    private GetImagesResponse getImagesResponse;
    private LocalDateTime lastModifiedDate;

    //private String uploader_profile_image;
    public GetSearchedResponse(String itemCategory,GetFilesResponse getFilesResponse,LocalDateTime lastModifiedDate){
        this.itemCategory=itemCategory;
        this.getFilesResponse=getFilesResponse;
        this.getLinksResponse=null;
        this.getImagesResponse=null;
        this.getMemosResponse=null;
        this.lastModifiedDate=lastModifiedDate;
    }
    public GetSearchedResponse(String itemCategory,GetLinksResponse getLinksResponse,LocalDateTime lastModifiedDate){
        this.itemCategory=itemCategory;
        this.getFilesResponse=null;
        this.getLinksResponse=getLinksResponse;
        this.getImagesResponse=null;
        this.getMemosResponse=null;
        this.lastModifiedDate=lastModifiedDate;

    }

    public GetSearchedResponse(String itemCategory,GetMemosResponse getMemosResponse,LocalDateTime lastModifiedDate){
        this.itemCategory=itemCategory;
        this.getFilesResponse=null;
        this.getLinksResponse=null;
        this.getImagesResponse=null;
        this.getMemosResponse=getMemosResponse;
        this.lastModifiedDate=lastModifiedDate;

    }

    public GetSearchedResponse(String itemCategory,GetImagesResponse getImagesResponse,LocalDateTime lastModifiedDate){
        this.itemCategory=itemCategory;
        this.getFilesResponse=null;
        this.getLinksResponse=null;
        this.getImagesResponse=getImagesResponse;
        this.getMemosResponse=null;
        this.lastModifiedDate=lastModifiedDate;

    }

    @Override
    public int compareTo(GetSearchedResponse getSearchedResponse){
        System.out.println("getSearchedResponse: "+getSearchedResponse.getLastModifiedDate());
        if(getSearchedResponse.getLastModifiedDate().compareTo(this.getLastModifiedDate())==1){
            return 1;
        }
        else if(getSearchedResponse.getLastModifiedDate().compareTo(this.getLastModifiedDate())==-1){
            return -1;
        }
        return 0;
    }
}
