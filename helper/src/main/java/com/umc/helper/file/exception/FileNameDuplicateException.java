package com.umc.helper.file.exception;

import com.umc.helper.config.BaseResponseStatus;
import com.umc.helper.exception.BusinessException;

public class FileNameDuplicateException  extends BusinessException {
    public FileNameDuplicateException(){super(BaseResponseStatus.EXIST_FILE_NAME);}

}
