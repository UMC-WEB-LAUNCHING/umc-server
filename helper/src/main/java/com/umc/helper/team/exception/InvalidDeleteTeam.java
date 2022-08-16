package com.umc.helper.team.exception;

import com.umc.helper.config.BaseResponseStatus;
import com.umc.helper.exception.BusinessException;

public class InvalidDeleteTeam extends BusinessException {
    public InvalidDeleteTeam(){super(BaseResponseStatus.INVALID_DELETE_TEAM);}

}
