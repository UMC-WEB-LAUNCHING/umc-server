package com.umc.helper.trash.exception;

import com.umc.helper.config.BaseResponseStatus;
import com.umc.helper.exception.BusinessException;

public class RestoreInvalidUser extends BusinessException {
    public RestoreInvalidUser(){super(BaseResponseStatus.RESTORE_INVALID_USER);}

}
