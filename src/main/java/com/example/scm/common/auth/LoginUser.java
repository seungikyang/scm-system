package com.example.scm.common.auth;

import com.example.scm.domain.enums.UserRole;

/**
 * 세션에 저장되는 immutable 로그인 사용자 DTO. (엔티티 직접 세션 저장 금지)
 */
public record LoginUser(Long id, String name, String email, UserRole role) {

    public boolean isAdmin() {
        return role == UserRole.ADMIN;
    }

    public boolean isManager() {
        return role == UserRole.MANAGER;
    }
}
