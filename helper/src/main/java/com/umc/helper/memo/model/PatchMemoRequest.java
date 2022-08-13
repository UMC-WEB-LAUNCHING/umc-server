package com.umc.helper.memo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatchMemoRequest {
    @Valid
    private String name; // 바꿀 링크 제목
    @Valid
    private String content; // 바꿀 메모 내용
    @Valid
    private Long memberId; // 변경하고자 하는 사람 id
}
