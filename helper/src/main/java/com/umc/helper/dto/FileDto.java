package com.umc.helper.dto;


import com.umc.helper.domain.Files;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FileDto {

    private int id;
    private String origFilename;
    private String filename;
    private String filePath;

    public Files toEntity() {
        Files build = Files.builder()
                .id(id)
                .origFilename(origFilename)
                .filename(filename)
                .filePath(filePath)
                .build();
        return build;
    }

    @Builder
    public FileDto(int id, String origFilename, String filename, String filePath) {
        this.id = id;
        this.origFilename = origFilename;
        this.filename = filename;
        this.filePath = filePath;
    }
}
