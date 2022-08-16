package com.umc.helper.link.exception;

import com.umc.helper.config.BaseResponseStatus;
import com.umc.helper.exception.BusinessException;

public class LinkNameDuplicateException extends BusinessException {
    public LinkNameDuplicateException(){super(BaseResponseStatus.EXIST_LINK_NAME);}

}
