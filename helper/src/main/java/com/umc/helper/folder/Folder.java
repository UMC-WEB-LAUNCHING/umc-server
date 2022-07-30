package com.umc.helper.folder;

import com.umc.helper.file.model.File;
import com.umc.helper.image.model.Image;
import com.umc.helper.link.model.Link;
import com.umc.helper.member.Member;
import com.umc.helper.memo.model.Memo;
import com.umc.helper.team.Team;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
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

    @OneToMany(mappedBy="folder")
    private List<File> files=new ArrayList<>();

    @OneToMany(mappedBy="folder")
    private List<Memo> memos=new ArrayList<>();

    @OneToMany(mappedBy="folder")
    private List<Image> images=new ArrayList<>();

    //==연관관계 편의 메서드==//
    public void setMember(Member member){
        this.member=member;
        member.getFolders().add(this);
    }

    public void setTeam(Team team){
        this.team=team;
        team.getFolders().add(this);
    }
}
