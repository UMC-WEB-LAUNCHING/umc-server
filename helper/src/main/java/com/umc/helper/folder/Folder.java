package com.umc.helper.folder;

import com.umc.helper.link.Link;
import com.umc.helper.member.Member;
import com.umc.helper.team.Team;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="folders")
public class Folder {

    @Id @GeneratedValue
    @Column(name="folder_id")
    private Long id;

    private String folderName;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="team_id")
    private Team team;

    @OneToMany(mappedBy="folder")
    private List<Link> links=new ArrayList<>();

//    @OneToMany(mappedBy="folder")
//    private List<File> files=new ArrayList<>();
//
//    @OneToMany(mappedBy="folder")
//    private List<Memo> memos=new ArrayList<>();


}
