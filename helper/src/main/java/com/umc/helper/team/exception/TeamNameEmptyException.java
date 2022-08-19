package com.umc.helper.team.exception;

import com.umc.helper.config.BaseResponseStatus;
import com.umc.helper.exception.BusinessException;

public class TeamNameEmptyException extends BusinessException {
    public TeamNameEmptyException(){super(BaseResponseStatus.TEAM_NAME_EMPTY);}

}
