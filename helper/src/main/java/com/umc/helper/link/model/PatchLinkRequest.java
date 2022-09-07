package com.umc.helper.link.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

@Data
@NoArgsConstructor @AllArgsConstructor
public class PatchLinkRequest {
    private String name; // 바꿀 링크 제목
    private Long memberId; // 변경하고자 하는 사람 id
}
