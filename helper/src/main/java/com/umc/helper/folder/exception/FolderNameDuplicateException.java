package com.umc.helper.folder.exception;

import com.umc.helper.config.BaseResponseStatus;
import com.umc.helper.exception.BusinessException;

public class FolderNameDuplicateException extends BusinessException {
    public FolderNameDuplicateException(){super(BaseResponseStatus.EXIST_FOLDER_NAME);}

}
