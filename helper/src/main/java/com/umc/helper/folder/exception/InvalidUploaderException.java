package com.umc.helper.folder.exception;

import com.umc.helper.config.BaseResponseStatus;
import com.umc.helper.exception.BusinessException;

public class InvalidUploaderException extends BusinessException {
    public InvalidUploaderException(){super(BaseResponseStatus.INVALID_UPLOADER);}

}
