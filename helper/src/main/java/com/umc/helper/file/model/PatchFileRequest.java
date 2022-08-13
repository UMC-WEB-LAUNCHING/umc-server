package com.umc.helper.file.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatchFileRequest {
    @Valid
    private String name;
    @Valid
    private Long memberId;
}
