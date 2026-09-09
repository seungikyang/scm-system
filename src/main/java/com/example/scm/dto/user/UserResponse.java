package com.example.scm.dto.user;

import com.example.scm.domain.User;
import com.example.scm.domain.enums.UserRole;
import lombok.Builder;
import lombok.Getter;

/**
 * 사용자 엔티티에서 외부에 공개해도 되는 값만 복사한 응답 DTO.
 * 비밀번호 필드가 아예 없으므로 실수로 API나 화면에 노출되는 것을 구조적으로 막는다.
 * 학습 모듈 32에서 가장 단순한 Entity → DTO 변환 예제로 먼저 읽는다.
 */
@Getter
@Builder
public class UserResponse {

    private final Long userId;
    private final String email;
    private final String name;
    private final UserRole role;

    /** 엔티티를 응답 DTO로 바꾸는 변환 지점을 한곳에 모은 정적 팩토리 메서드. */
    public static UserResponse from(User user) {
        return UserResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .build();
    }
}
