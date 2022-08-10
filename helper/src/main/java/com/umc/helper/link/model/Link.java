package com.umc.helper.link.model;

import com.umc.helper.folder.model.Folder;
import com.umc.helper.member.model.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name="links")
public class Link {

    @Id @GeneratedValue
    @Column(name="link_id")
    private Long id;

    private LocalDateTime uploadDate;

    private LocalDateTime lastModifiedDate;

    private String name;

    private String url;

    private Boolean status; // false - 쓰레기통에 들어감
    private LocalDateTime statusModifiedDate;

    @ManyToOne
    @JoinColumn(name="folder_id")
    private Folder folder;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member; // uploader

    //==연관관계 편의 메서드==//
    public void setFolder(Folder folder){
        this.folder=folder;
        folder.getLinks().add(this);
    }

    public void setMember(Member member){
        this.member=member;
    }

}
