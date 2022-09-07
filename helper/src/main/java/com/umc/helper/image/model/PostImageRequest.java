package com.umc.helper.image.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostImageRequest {
    private MultipartFile multipartFile;
    private Long folderId;
    private Long memberId;
}
