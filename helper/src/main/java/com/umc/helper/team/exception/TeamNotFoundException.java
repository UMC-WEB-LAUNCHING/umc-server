package com.umc.helper.team.exception;

import com.umc.helper.config.BaseResponseStatus;
import com.umc.helper.exception.BusinessException;

public class TeamNotFoundException extends BusinessException {
    public TeamNotFoundException(){super(BaseResponseStatus.TEAM_EMPTY);}

}
