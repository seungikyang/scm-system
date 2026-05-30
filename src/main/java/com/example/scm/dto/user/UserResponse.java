package com.example.scm.dto.user;

import com.example.scm.domain.User;
import com.example.scm.domain.enums.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {

    private final Long userId;
    private final String email;
    private final String name;
    private final UserRole role;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .build();
    }
}
