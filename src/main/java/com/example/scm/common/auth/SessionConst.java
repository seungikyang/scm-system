package com.example.scm.common.auth;

/**
 * 세션에 로그인 사용자를 저장할 때 사용하는 key 상수.
 * "LOGIN_USER" 문자열을 여러 파일에 흩어 두면 오타 하나로 버그가 나므로,
 * 반드시 이 상수 하나로만 접근한다. (저장: AuthService / 조회: CurrentUserArgumentResolver)
 * 학습 모듈 34에서는 LoginUser 다음에 이 상수를 읽는다.
 */
public final class SessionConst {

    public static final String LOGIN_USER = "LOGIN_USER";

    private SessionConst() {
    }
}
