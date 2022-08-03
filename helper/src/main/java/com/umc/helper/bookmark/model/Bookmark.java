package com.umc.helper.bookmark.model;

import com.umc.helper.file.model.File;
import com.umc.helper.folder.model.Folder;
import com.umc.helper.image.model.Image;
import com.umc.helper.link.model.Link;
import com.umc.helper.member.Member;
import com.umc.helper.memo.model.Memo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="bookmarks")
public class Bookmark {

    @Id
    @GeneratedValue
    @Column(name="bookmark_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name="link_id")
    private Link link;

    @ManyToOne
    @JoinColumn(name="image_id")
    private Image image;

    @ManyToOne
    @JoinColumn(name="memo_id")
    private Memo memo;

    @ManyToOne
    @JoinColumn(name="file_id")
    private File file;

    @ManyToOne
    @JoinColumn(name="folder_id")
    private Folder folder;

    private String category; // link, memo, file, image

    private LocalDateTime addedDate;

    //==연관관계 편의 메서드==//
    public void setMember(Member member){
        this.member=member;
        member.getBookmarks().add(this);
    }



}
