package com.umc.helper.folder.exception;

import com.umc.helper.config.BaseResponseStatus;
import com.umc.helper.exception.BusinessException;

public class FolderNotFoundException extends BusinessException {

    public FolderNotFoundException(){super(BaseResponseStatus.FOLDER_EMPTY);}
}
