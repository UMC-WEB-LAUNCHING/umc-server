package com.umc.helper.exception;

import com.umc.helper.config.BaseResponseStatus;

public class BusinessException extends RuntimeException{

    private BaseResponseStatus errorCode;

    public BusinessException(String message, BaseResponseStatus errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(BaseResponseStatus errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }


    public BaseResponseStatus getErrorCode() {
        return errorCode;
    }
}
