package com.umc.helper.member.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatchPasswordRequest {
    @Valid
    String email;
    @Valid
    String password;
}
