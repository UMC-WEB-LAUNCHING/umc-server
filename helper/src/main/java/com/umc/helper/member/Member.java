package com.umc.helper.member;


import com.umc.helper.folder.Folder;
import com.umc.helper.team.Team;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Member {

    @Generated
    @Id
    @Column(name="member_id")
    private Long id;

    private String email;

    private String username;

    private String password;

    private LocalDateTime registerDateTime;

    private LocalDateTime lastLoginDatetime;

    private String provider;

    @Enumerated(EnumType.STRING)
    private MemberType type;

    @OneToMany(mappedBy = "member",cascade=CascadeType.ALL)
    private List<Team> teams=new ArrayList<>();

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL)
    private List<Folder> folders=new ArrayList<>();

    //==연관관계 편의 메서드==//



}
