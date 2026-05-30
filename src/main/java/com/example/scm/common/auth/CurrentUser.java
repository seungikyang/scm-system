package com.example.scm.common.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 세션에 저장된 {@link LoginUser}를 컨트롤러 파라미터로 주입한다.
 * 미로그인 시 null 이 주입된다(가드는 LoginInterceptor 담당).
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUser {
}
