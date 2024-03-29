package com.umc.helper.folder.model;

import com.umc.helper.file.model.File;
import com.umc.helper.folder.exception.FolderNotFoundException;
import com.umc.helper.folder.exception.InvalidUploaderException;
import com.umc.helper.image.model.Image;
import com.umc.helper.link.model.Link;
import com.umc.helper.member.model.Member;
import com.umc.helper.memo.model.Memo;
import com.umc.helper.team.model.Team;
import com.umc.helper.team.model.TeamMember;
import lombok.*;

import javax.persistence.*;
import java.io.FileNotFoundException;
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

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    private Long creatorId;
    @ManyToOne
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

    private Boolean status; // false - 쓰레기통에 들어감

    private LocalDateTime statusModifiedDate;

    //==연관관계 편의 메서드==//
    public void setMember(Member member){
        this.member=member;
        member.getFolders().add(this);
    }

    public void setTeam(Team team){
        this.team=team;
        team.getFolders().add(this);
    }

    //==조회 로직==//

    public void invalidUploader(Long memberId){
        if(this.member.getId()!=memberId){
            throw new InvalidUploaderException();
        }
    }


}
