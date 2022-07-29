package com.umc.helper.team;

import com.umc.helper.member.Member;
import lombok.AccessLevel;
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

    //==연관관계 편의 메서드==//
    public void setMember(Member member){
        this.member=member;
        member.getTeams().add(this);
    }

    public void setTeam(Team team){
        this.team=team;
        team.getMembers().add(this);
    }


}
