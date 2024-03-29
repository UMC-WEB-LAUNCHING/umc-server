package com.umc.helper.image.model;

import com.umc.helper.file.exception.FileNotFoundException;
import com.umc.helper.folder.model.Folder;
import com.umc.helper.image.exception.ImageNotFoundException;
import com.umc.helper.member.model.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="images")
public class Image {

    @Id
    @GeneratedValue
    @Column(name="image_id")
    private Long id;

    private String originalFileName;

    private String fileName;

    private String filePath; // storeFileUrl: 저장된 이미지의 URL

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
        folder.getImages().add(this);
    }

    public void setMember(Member member){
        this.member=member;
    }

    //==조회 로직==//
    public void notExistImage(){
        if(this==null) {
            throw new ImageNotFoundException();
        }
    }
}
