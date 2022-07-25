package com.umc.helper.member;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.time.LocalDateTime;


@Entity
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class Member {

    @Generated
    @Id
    private Long id;

    private String email;

    private String username;

    private String password;

    private LocalDateTime registerDateTime;

    private LocalDateTime lastLoginDatetime;

    private String provider;

    @Enumerated(EnumType.STRING)
    private MemberType type;

}
