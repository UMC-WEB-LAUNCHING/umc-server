package com.umc.helper.trash.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeleteItemsRequest {
    private String category;
    private Long id;
    private Long memberId;
}
