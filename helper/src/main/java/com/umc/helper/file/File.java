package com.umc.helper.file;

import com.umc.helper.folder.Folder;
import com.umc.helper.member.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Table(name="files")
@RequiredArgsConstructor
@Setter
public class File {

    @Id
    @GeneratedValue
    @Column(name="file_id")
    private Long id;

    private String originalFileName;

    private String fileName;

    private String filePath;

    private String volume;

    @ManyToOne
    @JoinColumn(name="folder_id")
    private Folder folder;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member; // uploader

//    @Builder
//    public File(Long id, String origFilename, String filename, String filePath) {
//        this.id = id;
//        this.originalFileName = origFilename;
//        this.fileName = filename;
//        this.filePath = filePath;
//    }

}
