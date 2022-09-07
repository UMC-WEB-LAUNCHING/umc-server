package com.umc.helper.team.exception;

import com.umc.helper.config.BaseResponseStatus;
import com.umc.helper.exception.BusinessException;

public class TeamMemberNotFoundException extends BusinessException {
    public TeamMemberNotFoundException(){super(BaseResponseStatus.TEAM_MEMBER_EMPTY);}

}
