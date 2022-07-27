package com.umc.helper.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Table(name="user")
public class User {

    @Id
    @GeneratedValue
    @Column(name="userIdx")
    private Long userIdx;

    private String name;

//    @OneToMany(mappedBy = "uploader")
//    private List<Site> sites=new ArrayList<>();

//    @OneToMany(mappedBy = "uploader")
//    private List<File> files=new ArrayList<>();

}
