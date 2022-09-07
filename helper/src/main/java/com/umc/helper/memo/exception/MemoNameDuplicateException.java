package com.umc.helper.memo.exception;

import com.umc.helper.config.BaseResponseStatus;
import com.umc.helper.exception.BusinessException;

public class MemoNameDuplicateException extends BusinessException {
    public MemoNameDuplicateException(){super(BaseResponseStatus.EXIST_MEMO_NAME);}

}
