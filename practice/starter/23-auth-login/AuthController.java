// 실제 구현 위치 예: src/main/java/com/example/scm/controller/AuthController.java
// 목표: 인증 관련 API 엔드포인트를 채우세요. TRD 3.7.1 참고.

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // TODO 01: 로그인. 세션 ID 재발급을 위해 HttpServletRequest 를 주입받습니다.
    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request,
            ____ httpRequest) {
        return authService.login(request, httpRequest);
    }

    // TODO 02: 로그아웃. 응답 body 가 없으면 어떤 상태 코드가 자연스럽나요?
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        authService.logout(session);
        return ResponseEntity.____().build();
    }
}


// ===== /api/users/me 는 보통 별도 UserController 로 분리 =====
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
class UserController {

    private final AuthService authService;

    // TODO 03: 내 정보 조회. 현재 로그인한 사용자 ID 는 어떻게 꺼낼까요?
    //          (학습용 세션: @SessionAttribute("USER_ID"), 권장: @CurrentUser, Security: @AuthenticationPrincipal …)
    @GetMapping("/me")
    public MyInfoResponse me(@____ Long currentUserId) {
        return authService.me(currentUserId);
    }

    // TODO 04: 비밀번호 변경. 응답 body 가 없으면 어떤 상태 코드?
    @PatchMapping("/me/password")
    public ResponseEntity<Void> changePassword(
            @CurrentUser Long currentUserId,
            @Valid @RequestBody PasswordChangeRequest request) {
        authService.changePassword(currentUserId, request);
        return ResponseEntity.____Content().build();
    }
}


// ===== DTO =====
public record LoginRequest(
        @NotBlank @Email String email,
        @NotBlank String password
) { }

public record LoginResponse(Long userId, String email, String name, UserRole role) {
    public static LoginResponse from(User u) {
        return new LoginResponse(u.getId(), u.getEmail(), u.getName(), u.getRole());
    }
}

public record PasswordChangeRequest(
        @NotBlank String currentPassword,
        // TODO 05: 새 비밀번호 길이 제약을 채우세요.
        @NotBlank @Size(min = ____, max = 64) String newPassword
) { }

public record MyInfoResponse(
        Long userId, String email, String name, UserRole role
) {
    public static MyInfoResponse from(User user) {
        return new MyInfoResponse(
                user.getId(), user.getEmail(), user.getName(), user.getRole());
    }
}

// 학습 질문:
// Q1. /api/auth/login 은 왜 permitAll() 이어야 하는가? (Spring Security 단계 기준)
//     A:
// Q2. /api/users/me 의 비밀번호 변경을 PATCH 로 둔 이유는?
//     A:
// Q3. LoginResponse 에 사용자 ID 를 노출해도 괜찮은가? (idor 공격 관점)
//     A:
// Q4. @SessionAttribute 와 @CurrentUser 의 차이 — 어떤 경우에 어떤 방식이 적절한가?
//     A:
// Q5. 응답 status 204 No Content 와 200 OK + 메시지 중 어느 컨벤션을 택할까?
//     A:
