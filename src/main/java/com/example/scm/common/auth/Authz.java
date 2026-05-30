package com.example.scm.common.auth;

import com.example.scm.common.exception.BusinessException;
import com.example.scm.common.exception.ErrorCode;
import com.example.scm.domain.enums.UserRole;

/**
 * 역할 기반 인가 헬퍼. 불충족 시 ACCESS_DENIED(403). 미로그인 시 AUTHENTICATION_REQUIRED(401).
 */
public final class Authz {

    private Authz() {
    }

    public static void requireLogin(LoginUser loginUser) {
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
        }
    }

    public static void requireRole(LoginUser loginUser, UserRole... roles) {
        requireLogin(loginUser);
        for (UserRole role : roles) {
            if (loginUser.role() == role) {
                return;
            }
        }
        throw new BusinessException(ErrorCode.ACCESS_DENIED);
    }
}
