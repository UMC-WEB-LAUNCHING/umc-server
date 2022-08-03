package com.umc.helper.trash.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatchRestoreItemResponse {
    private String category;
    private Long id;
}
