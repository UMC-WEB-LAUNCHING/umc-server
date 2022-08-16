package com.umc.helper.auth2;

import com.umc.helper.member.model.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name="tokens")
public class Token {

    @Id @GeneratedValue
    private Long id;

    private String refreshToken;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

}
