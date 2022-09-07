package com.umc.helper.image.exception;

import com.umc.helper.config.BaseResponseStatus;
import com.umc.helper.exception.BusinessException;

public class ImageNotFoundException extends BusinessException {
    public ImageNotFoundException(){super(BaseResponseStatus.IMAGE_EMPTY);}

}
