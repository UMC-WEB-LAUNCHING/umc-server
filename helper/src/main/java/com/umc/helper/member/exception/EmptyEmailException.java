package com.umc.helper.member.exception;

import com.umc.helper.config.BaseResponseStatus;
import com.umc.helper.exception.BusinessException;

public class EmptyEmailException extends BusinessException {
    public EmptyEmailException(){super(BaseResponseStatus.POST_USERS_EMPTY_EMAIL);}

}
