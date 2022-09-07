package com.umc.helper.team.model;

import com.umc.helper.file.exception.FileNotFoundException;
import com.umc.helper.folder.model.Folder;
import com.umc.helper.member.model.Member;
import com.umc.helper.team.exception.TeamNotFoundException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name="teams")
public class Team {

    @Id @GeneratedValue
    @Column(name="team_id")
    private Long teamIdx;

    @OneToMany(mappedBy = "team",cascade=CascadeType.ALL)
    private List<TeamMember> members=new ArrayList<>();

    private String name; // 팀 이름

    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "team",cascade = CascadeType.ALL)
    private List<Folder> folders=new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member creator; // 팀 생성한 사람(팀장)

    private Boolean status;

    private String profileImage;
    //==연관관계 편의 메서드==//


    //==조회 로직==//
    public void notExistTeam(){
        if(this==null) {
            throw new TeamNotFoundException();
        }
    }

}
