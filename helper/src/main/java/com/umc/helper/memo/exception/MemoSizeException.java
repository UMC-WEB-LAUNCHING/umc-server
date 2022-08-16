package com.umc.helper.memo.exception;

import com.umc.helper.config.BaseResponseStatus;
import com.umc.helper.exception.BusinessException;

public class MemoSizeException extends BusinessException {
    public MemoSizeException(){super(BaseResponseStatus.MEMO_SIZE_EXCEED);}

}
