package com.umc.helper.team;

import com.umc.helper.folder.Folder;
import com.umc.helper.member.Member;
import lombok.AccessLevel;
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

    //==연관관계 편의 메서드==//



}
