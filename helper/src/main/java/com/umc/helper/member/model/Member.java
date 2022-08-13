package com.umc.helper.member.model;


import com.umc.helper.bookmark.model.Bookmark;
import com.umc.helper.folder.model.Folder;
import com.umc.helper.member.MemberType;
import com.umc.helper.member.exception.MemberNotFoundException;
import com.umc.helper.team.model.TeamMember;
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

    @GeneratedValue
    @Id
    @Column(name="member_id")
    private Long id;

    private String email;

    private String username;

    private String password;

    private LocalDateTime registerDateTime;

    private LocalDateTime lastLoginDatetime;

    private String provider;

    private String profileImage;

    @Enumerated(EnumType.STRING)
    private MemberType type;

    @OneToMany(mappedBy = "member",cascade=CascadeType.ALL)
    private List<TeamMember> teams=new ArrayList<>();

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL)
    private List<Folder> folders=new ArrayList<>();

    @OneToMany(mappedBy="member",cascade = CascadeType.ALL)
    private List<Bookmark> bookmarks =new ArrayList<>();

    //==조회 로직==//
    public void notExistMember(){
        if(this==null) {
            throw new MemberNotFoundException();
        }
    }

}
