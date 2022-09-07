package com.umc.helper.link.exception;

import com.umc.helper.config.BaseResponseStatus;
import com.umc.helper.exception.BusinessException;

public class LinkNotFoundException extends BusinessException {
    public LinkNotFoundException(){super(BaseResponseStatus.LINK_EMPTY);}

}
