package com.umc.helper.member.exception;

import com.umc.helper.config.BaseResponseStatus;
import com.umc.helper.exception.BusinessException;

public class MemberEmailException  extends BusinessException {
    public MemberEmailException(){super(BaseResponseStatus.USERS_EMPTY_USER_EMAIL);}

}
