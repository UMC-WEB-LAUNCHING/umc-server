package com.umc.helper.bookmark.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class PostBookmarkResponse {
    private String category;
    private Long category_id;
    private Long bookmark_id;
    public PostBookmarkResponse(String category,Long category_id,Long bookmark_id){
        this.category=category;
        this.category_id=category_id;
        this.bookmark_id=bookmark_id;
    }
}
