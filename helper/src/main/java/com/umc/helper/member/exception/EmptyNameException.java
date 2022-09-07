package com.umc.helper.member.exception;

import com.umc.helper.config.BaseResponseStatus;
import com.umc.helper.exception.BusinessException;

public class EmptyNameException extends BusinessException {
    public EmptyNameException(){super(BaseResponseStatus.POST_USERS_EMPTY_NAME);}

}
