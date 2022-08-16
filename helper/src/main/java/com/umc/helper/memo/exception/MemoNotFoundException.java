package com.umc.helper.memo.exception;

import com.umc.helper.config.BaseResponseStatus;
import com.umc.helper.exception.BusinessException;

public class MemoNotFoundException extends BusinessException {
    public MemoNotFoundException(){super(BaseResponseStatus.MEMO_EMPTY);}

}
