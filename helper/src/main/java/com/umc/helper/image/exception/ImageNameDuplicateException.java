package com.umc.helper.image.exception;

import com.umc.helper.config.BaseResponseStatus;
import com.umc.helper.exception.BusinessException;

public class ImageNameDuplicateException extends BusinessException {
    public ImageNameDuplicateException(){super(BaseResponseStatus.EXIST_IMAGE_NAME);}

}
