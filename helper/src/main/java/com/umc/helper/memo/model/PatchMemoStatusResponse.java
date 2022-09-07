package com.umc.helper.memo.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatchMemoStatusResponse {
    private Long memoId;
    private Boolean status;

    public PatchMemoStatusResponse(Memo memo){
        memoId=memo.getId();
        status=memo.getStatus();
    }
}
