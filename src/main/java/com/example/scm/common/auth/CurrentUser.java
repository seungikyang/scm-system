package com.example.scm.common.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 함수의 입력칸에 현재 로그인한 사람을 넣어 달라는 표시입니다.
 * 표시 자체가 사람을 찾는 것은 아니며 CurrentUserArgumentResolver가 실제로 찾아 넣습니다.
 *
 * 세션에 저장된 {@link LoginUser}를 컨트롤러 파라미터로 주입한다.
 * 미로그인 시 null 이 주입된다(가드는 LoginInterceptor 담당).
 * 학습 모듈 34에서 애너테이션 선언을 먼저 보고 CurrentUserArgumentResolver로 이동한다.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUser {
}
