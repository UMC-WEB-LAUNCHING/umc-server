package com.umc.helper.member.exception;

import com.umc.helper.config.BaseResponseStatus;
import com.umc.helper.exception.BusinessException;

public class EmptyPasswordException extends BusinessException {
    public EmptyPasswordException(){super(BaseResponseStatus.POST_USERS_EMPTY_PASSWORD);}

}
