package com.umc.helper.file.exception;

import com.umc.helper.config.BaseResponseStatus;
import com.umc.helper.exception.BusinessException;

public class FileNotFoundException extends BusinessException {
    public FileNotFoundException(){super(BaseResponseStatus.FILE_EMPTY);}
}
