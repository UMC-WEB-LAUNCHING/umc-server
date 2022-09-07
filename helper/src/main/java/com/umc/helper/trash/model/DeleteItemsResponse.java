package com.umc.helper.trash.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeleteItemsResponse {
    private String category;
    private Long id;
    private Long memberId;
}
