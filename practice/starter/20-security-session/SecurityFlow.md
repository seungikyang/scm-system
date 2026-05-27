# 보안 흐름 워크북

TRD 3.11 에서 정의한 1차/2차/3차 보안 진화를 직접 채워봅니다. 아래 빈칸과 질문에 한 줄씩 답을 적어 두면, 면접에서 "이 시스템의 보안은 어떻게 진화시킬 수 있나요?"에 자연스럽게 답할 수 있습니다.

---

## 1차: 세션 기반 로그인

### 흐름

```text
1. 클라이언트가 POST /api/auth/login 으로 email + password 전송
2. AuthService 가 ____Repository.findByEmail(email) 로 사용자 조회
3. passwordEncoder.____(rawPassword, user.getPassword()) 로 비밀번호 검증
4. 성공 시 httpRequest.____()  // Session Fixation 공격 방지를 위해 세션 ID 재발급
5. HttpSession 에 사용자 ID 와 Role 저장
   session.setAttribute("USER_ID", user.getId());
   session.setAttribute("USER_ROLE", user.getRole());
6. 이후 요청마다 ____Interceptor 가 세션을 확인하여 currentUser 를 구성
```

### TODO

- [ ] `JSESSIONID` 쿠키의 `HttpOnly`, `Secure`, `SameSite` 옵션을 설정한 이유를 한 줄씩 적어 보세요.
- [ ] 비밀번호를 평문으로 저장하면 PRD 의 어떤 비기능 요구사항을 위반하나요?
- [ ] 로그인 실패 시 `email` 이 존재하지 않는지, 비밀번호가 틀린지 메시지에서 구분하면 안 되는 이유는?
- [ ] 세션 고정(Session Fixation) 공격 시나리오를 한 문장으로 설명해 보세요.

### 학습 질문

- Q. 세션 기반 인증의 가장 큰 단점은? (스케일 아웃 관점)
- Q. 같은 사용자가 두 브라우저에서 로그인하면 세션은 몇 개가 만들어지나요?
- Q. 세션을 Redis 로 옮기면 어떤 장점과 비용이 생기는가?

---

## 2차: Spring Security + BCrypt + Role 기반 접근 제어

### 핵심 구성요소

| 요소 | 역할 |
|---|---|
| `SecurityFilterChain` | 필터 체인 정의 (formLogin, csrf, authorizeHttpRequests …) |
| `UserDetailsService` | DB 의 사용자 정보를 Spring Security 가 이해할 수 있는 형태로 변환 |
| `____PasswordEncoder` | 단방향 해시 + salt 자동 처리 |
| `@PreAuthorize` / `hasRole(...)` | 메서드/엔드포인트 단위 권한 검사 |
| `SessionManagement` | 세션 고정 공격 방지, 동시 세션 제한 |

### 빈칸 채우기

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.____())  // REST API + JWT 단계에서는 disable. 세션+폼 로그인 단계에서는 활성.
        .sessionManagement(s -> s.sessionFixation().____())  // 로그인 시 세션 ID 재발급
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/**", "/h2-console/**").____()
            .requestMatchers("/api/admin/**").hasRole("____")
            .requestMatchers(
                "/api/sales-orders/pending",
                "/api/sales-orders/*/ship",
                "/api/sales-orders/*/complete"
            ).hasAnyRole("MANAGER", "____")
            .anyRequest().____()
        )
        .formLogin(form -> form.loginProcessingUrl("/api/auth/login").permitAll())
        .logout(logout -> logout.logoutUrl("/api/auth/logout"));
    return http.build();
}

@Bean
public PasswordEncoder passwordEncoder() {
    return new ____PasswordEncoder();
}
```

### 학습 질문

- Q. `hasRole("ADMIN")` 과 `hasAuthority("ROLE_ADMIN")` 의 차이는?
- Q. CSRF 보호를 disable 해도 되는 조건은?
- Q. `@PreAuthorize` 가 동작하려면 어떤 어노테이션이 Config 에 필요한가?
- Q. BCrypt 가 SHA-256 보다 안전한 두 가지 이유는? (힌트: salt, work factor)

---

## 3차: JWT 무상태 인증

### 흐름

```text
1. POST /api/auth/login → AccessToken + (선택) RefreshToken 발급
2. 클라이언트가 Authorization: ____ {token} 헤더로 요청
3. JwtAuthenticationFilter 가 헤더를 파싱하여 SecurityContext 에 Authentication 주입
4. 서버는 ____ 상태이므로, 토큰만 유효하면 어떤 인스턴스든 인증을 처리 가능
```

### TODO

- [ ] JWT payload 에 어떤 클레임을 담을지 적어 보세요. (sub, role, exp 등)
- [ ] AccessToken 의 만료 시간을 짧게 두는 이유는?
- [ ] RefreshToken 을 도입하면 어떤 새로운 보안 위협이 생기나요?
- [ ] 토큰 서명에 사용하는 비밀키를 환경 변수로 분리해야 하는 이유는?
- [ ] 강제 로그아웃을 어떻게 구현할까? (블랙리스트 / 짧은 TTL / 키 회전)

### 비교 표 (직접 채워보세요)

| 항목 | 세션 | JWT |
|---|---|---|
| 상태 위치 | 서버 메모리 | ____ |
| 강제 로그아웃 | 세션 삭제로 즉시 가능 | ____ |
| 분산 환경 | 세션 클러스터링/Redis 필요 | ____ |
| 페이로드 변조 | 서버 메모리라 불가능 | 서명 검증으로 차단 |
| 모바일 친화도 | 쿠키 관리 필요 | ____ |
| 권한 변경 즉시 반영 | 즉시 | ____ |

---

## 권한 정책 매트릭스 (TRD 3.11.3 채우기)

| 기능 | USER | ADMIN | MANAGER |
|---|---|---|---|
| 내 정보 조회 | ____ | 가능 | 가능 |
| 거래처 관리 (등록/수정/삭제) | ____ | 가능 | ____ |
| 카테고리 관리 | ____ | 가능 | ____ |
| 품목 관리 | ____ | 가능 | ____ |
| 거래처 / 품목 조회 | 가능 | 가능 | 가능 |
| 발주 작성 | 가능 | 가능 | 가능 |
| 발주 승인 / 반려 | ____ | 가능 | ____ |
| 발주 입고 처리 | ____ | 가능 | ____ |
| 공지 조회 | 가능 | 가능 | 가능 |
| 공지 등록 / 수정 / 삭제 | ____ | 가능 | ____ |
| 수주 작성 | 가능 | 가능 | 가능 |
| 수주 확정 (DRAFT → CONFIRMED) | ____ (본인) | 가능 | 가능 |
| 수주 출고 / 완료 | ____ | 가능 | 가능 |
| 수주 취소 | ____ (본인) | 가능 | 가능 |

---

## 다층 방어 (Defense in Depth)

같은 권한 검증을 여러 곳에 두면 한 곳이 뚫려도 다음 단계가 막는다.

```text
[1] Spring Security FilterChain
       ↓ (URL 패턴 / Role)
[2] @PreAuthorize  메서드 진입 직전
       ↓
[3] Service 의 권한 가드 (currentUser.getRole() == ADMIN)
       ↓
[4] 도메인 메서드 가드 (cancelByOwner: writerId == currentUserId)
       ↓
[5] DB 제약 (FK / unique / not null)
```

빈칸 채우기:

- 1단계가 빠지면 어떤 공격이 가능한가? ____
- 2단계만 두고 3단계를 빼면 어떤 우회가 가능한가? ____
- 4단계의 도메인 가드는 왜 Service 가드와 별개로 필요한가? ____

---

## 셀프 체크

- [ ] 1차/2차/3차 보안 흐름을 도식 없이 말로 설명할 수 있는가?
- [ ] BCrypt 가 단순 SHA-256 보다 안전한 이유를 한 줄로 답할 수 있는가?
- [ ] 권한 검사가 다층(필터 → 어노테이션 → 서비스 가드 → 도메인 가드)으로 있어야 하는 이유를 답할 수 있는가?
- [ ] Session Fixation 공격을 어떻게 방어하는지 한 문장으로 답할 수 있는가?
- [ ] JWT 의 강제 로그아웃 한계와 대안을 답할 수 있는가?
