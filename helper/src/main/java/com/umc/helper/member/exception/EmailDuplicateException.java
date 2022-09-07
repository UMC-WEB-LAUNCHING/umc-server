package com.umc.helper.member.exception;

import com.umc.helper.config.BaseResponseStatus;
import com.umc.helper.exception.BusinessException;

public class EmailDuplicateException extends BusinessException {
    public EmailDuplicateException(){super(BaseResponseStatus.POST_USERS_EXISTS_EMAIL);}

}
