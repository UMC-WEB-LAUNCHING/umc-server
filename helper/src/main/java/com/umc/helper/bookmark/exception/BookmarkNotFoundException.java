package com.umc.helper.bookmark.exception;

import com.umc.helper.config.BaseResponseStatus;
import com.umc.helper.exception.BusinessException;

public class BookmarkNotFoundException extends BusinessException {
    public BookmarkNotFoundException(){super(BaseResponseStatus.BOOKMARK_EMPTY);}

}
