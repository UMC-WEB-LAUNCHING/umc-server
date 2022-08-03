package com.umc.helper.trash.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeleteAllResponse {
    private int memoDeletedCount;
    private int linkDeletedCount;
    private int fileDeletedCount;
    private int imageDeletedCount;
    private int folderDeletedCount;

}
