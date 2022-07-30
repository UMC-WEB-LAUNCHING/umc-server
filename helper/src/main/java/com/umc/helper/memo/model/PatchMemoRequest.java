package com.umc.helper.memo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatchMemoRequest {
    private String name; // 바꿀 링크 제목
    private String content; // 바꿀 메모 내용
    private Long memberId; // 변경하고자 하는 사람 id
}
