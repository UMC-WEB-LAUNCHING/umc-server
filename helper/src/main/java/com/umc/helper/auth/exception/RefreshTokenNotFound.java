package com.umc.helper.auth.exception;

import com.umc.helper.config.BaseResponseStatus;
import com.umc.helper.exception.BusinessException;

public class RefreshTokenNotFound extends BusinessException {
    public RefreshTokenNotFound(){super(BaseResponseStatus.INVALID_JWT);}

}
