package com.umc.helper.team.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MemberInfo {
    @JsonProperty
    private Long memberId;
    @JsonProperty

    private String memberName;

    public MemberInfo(Long memberId,String memberName){
        this.memberId=memberId;
        this.memberName=memberName;
    }
}
