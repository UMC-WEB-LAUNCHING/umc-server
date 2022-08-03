package com.umc.helper.trash.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeleteItemsRequestList {
    List<DeleteItemsRequest> items;
}
