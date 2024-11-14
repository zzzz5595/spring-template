package com.app.api.login.validator;

import com.app.domain.member.constant.MemberType;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.BusinessException;
import org.springframework.stereotype.Service;

@Service
public class OauthValidator {

    //memberType 체크
    public void validateMemberType(String memberType) {
        if(!MemberType.isMemberType(memberType)){
            throw new BusinessException(ErrorCode.INVALID_MEMBER_TYPE);
        }
    }



}
