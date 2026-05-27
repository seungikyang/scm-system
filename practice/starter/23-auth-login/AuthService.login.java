// 실제 구현 위치 예: src/main/java/com/example/scm/service/AuthService.java
// 목표: 로그인 / 로그아웃 / 내 정보 조회 / 비밀번호 변경. PRD 2.5.1, TRD 3.11 참고.

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ===== 로그인 =====
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request, HttpServletRequest httpRequest) {

        String email = request.getEmail().trim().toLowerCase();

        // TODO 01: 이메일 사용자가 존재하는지 확인. 존재 여부와 비번 불일치를
        //          같은 메시지로 응답하는 이유는?
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessException(ErrorCode.____, "이메일 또는 비밀번호가 올바르지 않습니다."));

        // TODO 02: 비밀번호 검증. 평문이 아닌 해시 비교를 위한 메서드는?
        if (!passwordEncoder.____(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED, "이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        // TODO 03: 세션 고정 공격 방지를 위해 로그인 직후 세션 ID 를 재발급.
        httpRequest.____();

        // TODO 04: 세션에 사용자 ID 와 Role 을 저장.
        HttpSession session = httpRequest.getSession(true);
        session.setAttribute("USER_ID", user.____());
        session.setAttribute("USER_ROLE", user.getRole().name());

        return LoginResponse.from(user);
    }

    // ===== 로그아웃 =====
    public void logout(HttpSession session) {
        // TODO 05: 어떤 메서드로 세션을 종료해야 안전한가요?
        if (session != null) session.____();
    }

    // ===== 내 정보 조회 =====
    @Transactional(readOnly = true)
    public MyInfoResponse me(Long currentUserId) {
        if (currentUserId == null) {
            throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
        }
        User user = userRepository.findById(currentUserId)
            .orElseThrow(() -> new BusinessException(ErrorCode.____));

        return MyInfoResponse.from(user);
    }

    // ===== 비밀번호 변경 =====
    @Transactional
    public void changePassword(Long currentUserId, PasswordChangeRequest request) {

        User user = userRepository.findById(currentUserId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // TODO 06: 현재 비밀번호 확인. 본인이 맞는지 한 번 더 검증해야 합니다.
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.____, "현재 비밀번호가 일치하지 않습니다.");
        }

        // TODO 07: 새 비밀번호가 현재 비밀번호와 같으면 거부하는 정책 (선택).
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.____, "새 비밀번호가 현재와 동일합니다.");
        }

        // TODO 08: 새 비밀번호를 해시해 반영하세요.
        user.changePassword(passwordEncoder.____(request.getNewPassword()));
    }
}

// 학습 질문 (한 줄 답을 적어 보세요):
// Q1. 로그인 실패 시 "존재하지 않는 이메일" / "비밀번호 틀림" 을 구분해서 응답하면 안 되는 이유는?
//     A:
// Q2. 세션 ID 를 로그인 직후에 재발급하는 이유 (Session Fixation) 는?
//     A:
// Q3. 비밀번호 변경 후 사용자의 기존 세션/토큰을 어떻게 처리하는 것이 안전한가?
//     A:
// Q4. login() 에 @Transactional(readOnly=true) 를 단 이유와 changePassword() 에는 빼고 그냥 @Transactional 을 단 이유는?
//     A:
// Q5. 이메일을 trim + toLowerCase 로 정규화하는 시점은? (DTO / Service / Entity)
//     A:
// Q6. 새 비밀번호가 현재와 같을 때 막는 정책은 어떤 보안 위협을 줄여 주는가?
//     A:
