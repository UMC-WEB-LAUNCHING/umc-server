package com.umc.helper.alarm.model;

import com.umc.helper.folder.model.Folder;
import com.umc.helper.member.model.Member;
import com.umc.helper.team.model.Team;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    private LocalDateTime uploadDate;
    private LocalDateTime lastModifiedDate;

    private Boolean status; // 알림 확인 여부
}
