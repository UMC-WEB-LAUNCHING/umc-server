package com.umc.helper.memo.model;

import com.umc.helper.link.model.Link;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetMemosResponse {

    private Long memoId;
    private String url;
    private String name;
    private String member_name; //uploader
    private String folder_name;
//    private LocalDateTime uploadDate;
//    private LocalDateTime lastModifiedDate;

    public GetMemosResponse(Memo memo){
        memoId=memo.getId();
        url=memo.getContent();
        name=memo.getName();
        member_name=memo.getMember().getUsername();
        folder_name=memo.getFolder().getFolderName();
    }

}
