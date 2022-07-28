package com.umc.helper.link;

import com.umc.helper.folder.Folder;
import com.umc.helper.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="links")
public class Link {

    @Id @GeneratedValue
    @Column(name="link_id")
    private Long id;

    private LocalDateTime uploadDate;

    private LocalDateTime lastModifiedDate;

    private String name;

    private String url;

    @ManyToOne
    @JoinColumn(name="folder_id")
    private Folder folder;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member; // uploader

}
