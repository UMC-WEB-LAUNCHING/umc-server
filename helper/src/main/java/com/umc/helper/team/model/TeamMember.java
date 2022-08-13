package com.umc.helper.team.model;

import com.umc.helper.member.model.Member;
import com.umc.helper.team.exception.TeamMemberNotFoundException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name="team_member")
public class TeamMember {

    @Id @GeneratedValue
    @Column(name="team_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="team_id")
    private Team team;

    private Boolean status;

    //==연관관계 편의 메서드==//
    public void setMember(Member member){
        this.member=member;
        member.getTeams().add(this);
    }

    public void setTeam(Team team){
        this.team=team;
        team.getMembers().add(this);
    }

    //==조회 로직==//
    public void notExistTeamMember(){
        if(this==null){
            throw new TeamMemberNotFoundException();
        }
    }

}
