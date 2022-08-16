package com.umc.helper.member.model;

import com.umc.helper.member.exception.EmailDuplicateException;
import com.umc.helper.member.exception.EmptyEmailException;
import com.umc.helper.member.exception.EmptyNameException;
import com.umc.helper.member.exception.EmptyPasswordException;
import lombok.Data;

import javax.validation.Valid;

@Data
public class PostMemberRequest {
    @Valid
    private String name;
    @Valid
    private String email;
    @Valid
    private String password;

    public void isPresentEmail(){
        if(this.getEmail()==null){
            throw new EmptyEmailException();
        }
    }
    public void isPresentName(){
        if(this.getName()==null){
            throw new EmptyNameException();
        }
    }

    public void isPresentPassword(){
        if(this.getPassword()==null){
            throw new EmptyPasswordException();
        }
    }

}
