package com.example.scm.common.auth;

import com.example.scm.common.exception.BusinessException;
import com.example.scm.common.exception.ErrorCode;
import com.example.scm.domain.enums.UserRole;

/**
 * 이 사람이 로그인했는지, 이 일을 해도 되는 역할인지 확인합니다.
 * 학교에서 학생과 선생님이 할 수 있는 일이 다르듯 USER, MANAGER, ADMIN의 권한도 다릅니다.
 *
 * 로그인 여부와 역할(role)을 검사하는 공통 인가 도구.
 *
 * <p>학습 모듈 34에서 현재 사용자 주입 흐름을 끝낸 뒤, 주입된 값이 Service에서 어떻게
 * 인가에 쓰이는지 확인한다.</p>
 *
 * <p>컨트롤러에서만 권한을 검사하면 API와 화면 중 한쪽에서 검사를 빠뜨릴 수 있다.
 * 그래서 실제 데이터가 변경되기 직전인 Service가 이 메서드를 호출한다. 로그인하지
 * 않았다면 401, 로그인했지만 역할이 맞지 않으면 403에 해당하는 예외를 던진다.</p>
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
        // 허용 목록에 내 역할이 하나라도 있으면 통과합니다. 전부 다르다면 아래에서 작업을 중단합니다.
        // 가변 인자(varargs)를 사용해 ADMIN 하나 또는 ADMIN, MANAGER 둘 다 허용할 수 있다.
        for (UserRole role : roles) {
            if (loginUser.role() == role) {
                return;
            }
        }
        throw new BusinessException(ErrorCode.ACCESS_DENIED);
    }
}
