package com.umc.helper.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Table(name="file")
@RequiredArgsConstructor
@Setter
public class Files {

    @Id
    @GeneratedValue
    @Column(name="fileIdx")
    private int fileIdx;


//    @ManyToOne
//    @JoinColumn(name="id")
  //  private int uploader; // 올린 사람

    //private LocalDateTime uploadDate;

    //private String category; // 과목, 카테고리

    private String originalFileName;

    private String fileName;

    private String filePath;

    @Builder
    public Files(int id, String origFilename, String filename, String filePath) {
        this.fileIdx = id;
        this.originalFileName = origFilename;
        this.fileName = filename;
        this.filePath = filePath;
    }

}
