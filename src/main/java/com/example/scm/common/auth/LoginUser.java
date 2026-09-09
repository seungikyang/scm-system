package com.example.scm.common.auth;

import com.example.scm.domain.enums.UserRole;

/**
 * 세션에 저장하는 변경 불가능한 로그인 사용자 DTO.
 *
 * <p>학습 모듈 34: LoginUser → SessionConst → LoginInterceptor → CurrentUser →
 * CurrentUserArgumentResolver 순서로 읽는다.</p>
 *
 * <p>Java {@code record}는 생성자와 필드 접근자({@code id()}, {@code role()} 등)를
 * 자동으로 만든다. User 엔티티 전체를 세션에 넣지 않고 화면과 권한 검사에 꼭 필요한
 * 값만 복사해, 비밀번호 노출과 JPA 엔티티의 장기 보관을 피한다.</p>
 */
public record LoginUser(Long id, String name, String email, UserRole role) {

    public boolean isAdmin() {
        return role == UserRole.ADMIN;
    }

    public boolean isManager() {
        return role == UserRole.MANAGER;
    }
}
