package com.umc.helper.alarm;

import com.umc.helper.folder.model.Folder;
import com.umc.helper.member.model.Member;
import com.umc.helper.team.model.Team;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="alarms")
@Getter @Setter
@NoArgsConstructor
public class Alarm {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name="team_id")
    private Team team;

    @OneToOne
    private Member member;

    @ManyToOne
    @JoinColumn(name="folder_id")
    private Folder folder;

    private Boolean status;
}
