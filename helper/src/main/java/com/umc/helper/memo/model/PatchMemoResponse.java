package com.umc.helper.memo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatchMemoResponse {

    private Long memoId;
    private String content;
    private String name;
    private String member_name;
    private String folder_name;

    public PatchMemoResponse(Memo memo){
        memoId=memo.getId();
        content=memo.getContent();
        name=memo.getName();
        member_name=memo.getMember().getUsername();
        folder_name=memo.getFolder().getFolderName();
    }
}
