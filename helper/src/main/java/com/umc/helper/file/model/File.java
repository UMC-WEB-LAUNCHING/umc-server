package com.umc.helper.file.model;

import com.umc.helper.file.exception.FileNotFoundException;
import com.umc.helper.folder.model.Folder;
import com.umc.helper.member.model.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    private String filePath; // storeFileUrl: 저장된 파일의 URL

    private Long volume;

    private Boolean status; // false - 쓰레기통에 들어감
    private LocalDateTime statusModifiedDate;
    private LocalDateTime uploadDate;
    private LocalDateTime lastModifiedDate;

    @ManyToOne
    @JoinColumn(name="folder_id")
    private Folder folder;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member; // uploader

    //==연관관계 편의 메서드==//
    public void setFolder(Folder folder){
        this.folder=folder;
        folder.getFiles().add(this);
    }

    public void setMember(Member member){
        this.member=member;
    }

    //==조회 로직==//
    public void notExistFile(){
        if(this==null) {
            throw new FileNotFoundException();
        }
    }


}
