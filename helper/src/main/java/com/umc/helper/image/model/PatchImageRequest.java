package com.umc.helper.image.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatchImageRequest {
    @Valid
    private String name;
    @Valid
    private Long memberId;
}
