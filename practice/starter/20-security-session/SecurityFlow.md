# 보안 흐름 (TRD 3.11) · 4지 선다

[이 모듈을 브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&module=20) · [전체 문제집](../../workbook-questions.md)

각 문제에서 1~4번 중 하나를 고르고 정답·해설을 확인합니다.

<a id="w006"></a>
### W006. 로그인 계정 저장 정책으로 올바른 것은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=6)

1. 이메일은 매번 중복 등록하고 비밀번호는 평문 저장한다
2. 이메일은 중복을 막고 비밀번호는 BCrypt 해시로 저장한다
3. 비밀번호 해시를 로그인 응답에 공개한다
4. 이메일 unique가 비밀번호를 암호화한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 이메일은 중복을 막고 비밀번호는 BCrypt 해시로 저장한다

식별자 중복 방지와 비밀번호 보호는 서로 다른 보안·무결성 책임입니다.

</details>


<a id="w054"></a>
### W054. 발주 승인·반려·입고가 허용되는 역할은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=54)

1. ADMIN 또는 MANAGER
2. USER만
3. 모든 비로그인 사용자
4. 거래처 유형이 CUSTOMER인 사용자만

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · ADMIN 또는 MANAGER

현재 참조 구현은 ADMIN과 MANAGER를 허용하며 USER는 거절합니다.

</details>


<a id="w057"></a>
### W057. 공지 등록·수정·삭제의 권한을 보장하는 곳은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=57)

1. 화면 버튼 표시 여부만 확인한다
2. Service에서 ADMIN 여부를 검증한다
3. 공지 제목에 ADMIN이 있으면 허용한다
4. 조회수에 비례해 권한을 부여한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · Service에서 ADMIN 여부를 검증한다

여러 호출 경로에서 동일한 권한 규칙을 지켜야 합니다.

</details>


<a id="w079"></a>
### W079. 관리자 전용 품목 변경을 보호하는 방법은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=79)

1. URL에 admin을 쓰면 자동으로 보호된다
2. HTML 버튼을 숨기기만 한다
3. 인증된 사용자의 ADMIN 역할을 서버 가드에서 검사한다
4. 품목 코드에 ADMIN을 붙인다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 인증된 사용자의 ADMIN 역할을 서버 가드에서 검사한다

경로 이름과 UI만으로는 권한을 보장할 수 없습니다.

</details>


<a id="w083"></a>
### W083. 세션 로그인 흐름의 올바른 순서는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=83)

1. 세션에 평문 비밀번호 저장·검증 생략
2. 계정 조회·비밀번호 검증·세션 ID 변경·로그인 정보 저장
3. 로그아웃 후 승인자 ID를 로그인 ID로 사용
4. 브라우저가 역할을 선택하면 서버가 그대로 승인

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 계정 조회·비밀번호 검증·세션 ID 변경·로그인 정보 저장

서버가 인증한 정보만 세션에 보관하고 세션 고정 공격을 방지합니다.

</details>


<a id="w084"></a>
### W084. 현재 보안 책임 분담은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=84)

1. Security가 모든 세션 사용자 인가를 자동으로 맡는다
2. Thymeleaf만 모든 인가를 수행한다
3. Repository가 세션 쿠키를 발급한다
4. Interceptor·Service는 인증/인가, Security는 CSRF·헤더를 맡는다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · Interceptor·Service는 인증/인가, Security는 CSRF·헤더를 맡는다

현재 참조 구현과 Spring Security가 전체 인증을 맡는 대안 설계를 구분합니다.

</details>


<a id="w085"></a>
### W085. 인증·인가를 Spring Security로 옮길 때 필요한 정리는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=85)

1. 기존 검사와 새 검사를 목적 없이 모두 추가한다
2. 기존 세션 인터셉터와 사용자 해석의 중복 책임을 재설계한다
3. Service의 업무 소유권 검사도 전부 삭제한다
4. 모든 API를 permitAll로 바꾼다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 기존 세션 인터셉터와 사용자 해석의 중복 책임을 재설계한다

새 인증 원천과 기존 계약을 맞추면서 필요한 Service 업무 인가는 유지합니다.

</details>


<a id="w086"></a>
### W086. 서버 세션을 사용하지 않는 JWT 인증의 특징은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=86)

1. 토큰이 있으면 서명과 만료 검사가 필요 없다
2. 로그아웃 시 모든 복제 토큰이 자동 무효화된다
3. 매 요청 토큰을 검증하고 폐기·권한 변경 정책을 별도로 설계한다
4. payload에 비밀번호를 평문으로 넣어야 한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 매 요청 토큰을 검증하고 폐기·권한 변경 정책을 별도로 설계한다

무상태 인증이라도 키 관리·만료·재사용·폐기 문제를 해결해야 합니다.

</details>


<a id="w096"></a>
### W096. 세션 고정 공격을 줄이기 위한 ID 변경 시점은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=96)

1. 로그인 인증이 성공할 때
2. 비밀번호 검증 전에 항상 관리자 권한을 부여할 때
3. 발주 입고 후에만
4. 프로그램 종료 후

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 로그인 인증이 성공할 때

인증 전 알려진 세션 ID가 인증 후에도 유지되는 것을 방지합니다.

</details>


<a id="w097"></a>
### W097. 로그인 실패 메시지를 통일하는 이유는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=97)

1. 모든 실패를 성공으로 바꾸기 위해서다
2. 비밀번호 해시를 숨기지 않기 위해서다
3. 이메일 존재 여부를 이용한 계정 열거를 줄이기 위해서다
4. 로그인 속도를 항상 두 배로 만들기 위해서다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 이메일 존재 여부를 이용한 계정 열거를 줄이기 위해서다

사용자에게 계정 존재 여부를 직접 구분해 알려 주지 않습니다.

</details>


<a id="w113"></a>
### W113. 카테고리 변경·조회 권한의 현재 정책은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=113)

1. 변경은 ADMIN, 조회는 로그인 사용자에게 허용한다
2. 변경은 USER만, 조회는 비로그인만 허용한다
3. 모든 변경을 공개한다
4. MANAGER만 모든 카테고리를 조회한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 변경은 ADMIN, 조회는 로그인 사용자에게 허용한다

조회와 변경은 서로 다른 권한 범위를 갖습니다.

</details>


<a id="w144"></a>
### W144. 비밀번호에 BCrypt를 사용하는 이유는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=144)

1. salt와 조절 가능한 연산 비용으로 대량 추측 비용을 높인다
2. 평문을 누구나 쉽게 복원할 수 있다
3. 빠른 SHA-256 한 번이면 항상 더 안전하다
4. 같은 비밀번호가 항상 같은 해시를 만들게 한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · salt와 조절 가능한 연산 비용으로 대량 추측 비용을 높인다

비밀번호 해시는 빠른 일반 해시와 목적이 다르며 BCrypt는 matches로 검증합니다.

</details>


<a id="w162"></a>
### W162. 여러 계층에 검증을 두는 이유는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=162)

1. 검증이 많으면 트랜잭션이 필요 없어진다
2. 서로 다른 우회·호출 경로와 경쟁 요청을 방어하기 위해서다
3. 중복되는 모든 코드를 무조건 유지하기 위해서다
4. 화면 검증이 DB 무결성을 대체하기 때문이다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 서로 다른 우회·호출 경로와 경쟁 요청을 방어하기 위해서다

같은 정책을 일관되게 유지하면서 계층마다 다른 위험을 다룹니다.

</details>


<a id="w172"></a>
### W172. 인증(Authentication)이 확인하는 것은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=172)

1. 업무 기능 실행 권한만
2. 사용자가 주장한 신원과 인증 증거의 일치
3. 주문의 현재 재고만
4. CSS 적용 여부

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 사용자가 주장한 신원과 인증 증거의 일치

인증은 신원을 확인하고 인가는 기능·데이터 접근 권한을 판단합니다.

</details>


<a id="w173"></a>
### W173. 인가(Authorization)가 판단하는 것은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=173)

1. JDK 설치 여부
2. 문자열의 UTF-8 바이트 수
3. 인증된 사용자가 기능·데이터에 접근할 권한
4. 비밀번호 자체의 평문 값

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 인증된 사용자가 기능·데이터에 접근할 권한

로그인 성공만으로 모든 데이터에 접근할 수 있는 것은 아닙니다.

</details>


<a id="w175"></a>
### W175. 무상태 JWT의 즉시 강제 로그아웃을 구현하려면?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=175)

1. 클라이언트 토큰 삭제만으로 유출 복사본까지 무효화한다
2. JWT의 exp 검사를 제거한다
3. payload의 role을 암호 없이 믿는다
4. 폐기 목록·토큰 버전 등 서버 확인 정책을 추가한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 폐기 목록·토큰 버전 등 서버 확인 정책을 추가한다

클라이언트 삭제는 탈취된 복사본을 폐기하지 못합니다. 짧은 TTL은 노출 시간을 줄일 뿐 즉시 폐기는 별도 대책입니다.

</details>


<a id="w176"></a>
### W176. 내 정보 조회에서 사용자를 정하는 안전한 기준은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=176)

1. URL에서 받은 임의의 userId만
2. 서버가 인증한 현재 사용자 ID
3. 요청 본문의 isAdmin만
4. 브라우저 북마크 이름

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 서버가 인증한 현재 사용자 ID

인증 주체의 ID를 사용해 다른 사용자 정보 조회 우회를 막습니다.

</details>


<a id="w186"></a>
### W186. 현재 수주 confirm을 실행할 수 있는 사용자 기준은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=186)

1. 수주 작성자
2. 아무 비로그인 사용자
3. 작성자가 아닌 USER
4. 요청 본문에 임의 ID를 넣은 사람

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 수주 작성자

SalesOrderService.confirm은 로그인과 작성자 ID를 확인합니다. ADMIN이라는 이유만으로 타인 수주 confirm을 허용하지 않습니다.

</details>


<a id="w189"></a>
### W189. 현재 수주 취소의 올바른 정책은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=189)

1. 비로그인 누구나 완료 수주를 취소한다
2. 작성자 또는 ADMIN/MANAGER가 허용 상태에서 사유를 입력한다
3. 사유를 넣으면 모든 종료 상태도 취소한다
4. USER가 URL ID만 바꾸면 타인 수주 취소가 가능하다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 작성자 또는 ADMIN/MANAGER가 허용 상태에서 사유를 입력한다

DRAFT·CONFIRMED·SHIPPED에서만 취소하며 출고 후 취소는 재고를 복원합니다.

</details>


<a id="w190"></a>
### W190. HttpOnly·Secure·SameSite의 역할 조합은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=190)

1. DB 암호화·페이징·정렬
2. JWT 복호화·트랜잭션·캐시 삭제
3. 로그인 우회·관리자 부여·조회수 증가
4. JS 쿠키 접근 제한·HTTPS 전송 제한·교차 사이트 전송 제어

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · JS 쿠키 접근 제한·HTTPS 전송 제한·교차 사이트 전송 제어

각 쿠키 속성은 다른 공격 경로를 줄입니다. HTTPS 설정과 SameSite 정책은 사용 흐름에 맞춰 정합니다.

</details>


<a id="w191"></a>
### W191. 세션 기반 인증을 여러 서버로 확장할 때 필요한 고려는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=191)

1. 세션 공유 저장소나 라우팅 전략과 장애·운영 비용
2. 각 서버가 세션을 따로 갖더라도 모든 요청이 항상 인증된다
3. DB unique만 추가하면 세션이 자동 공유된다
4. 사용자를 모두 로그아웃시키면 확장 문제가 사라진다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 세션 공유 저장소나 라우팅 전략과 장애·운영 비용

서버별 메모리 세션은 요청 서버가 달라지면 인증 상태를 찾지 못할 수 있습니다.

</details>


<a id="w192"></a>
### W192. 독립 쿠키 저장소를 쓰는 두 브라우저에서 같은 사용자가 로그인하면?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=192)

1. 반드시 같은 세션 ID가 발급된다
2. 사용자가 같으면 쿠키를 주고받는다
3. 보통 각각 별도 세션이 만들어진다
4. 두 번째 브라우저는 JPA 엔티티가 된다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 보통 각각 별도 세션이 만들어진다

특별한 동시 세션 제한 정책이 없다면 브라우저별 쿠키·서버 세션이 독립적입니다.

</details>


<a id="w193"></a>
### W193. 세션을 Redis에 저장하는 장점과 비용은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=193)

1. 별도 저장소 없이 세션이 자동 공유된다
2. 모든 CSRF 검사가 불필요해진다
3. 여러 서버가 세션을 공유하지만 저장소 운영·네트워크 비용이 생긴다
4. 비밀번호 해시를 저장할 필요가 없어진다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 여러 서버가 세션을 공유하지만 저장소 운영·네트워크 비용이 생긴다

중앙 세션 저장소는 확장을 돕지만 가용성·TTL·네트워크·장애 대응을 요구합니다.

</details>


<a id="w194"></a>
### W194. 기본 역할 접두사에서 hasRole('ADMIN')과 hasAuthority('ROLE_ADMIN')은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=194)

1. 같은 권한 문자열을 검사한다
2. hasRole은 USER만 검사한다
3. hasAuthority는 로그인이 필요 없다
4. 두 표현식이 모두 비밀번호를 검사한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 같은 권한 문자열을 검사한다

기본 ROLE_ 접두사를 전제로 합니다. 커스텀 접두사 정책이면 기준을 다시 확인해야 합니다.

</details>


<a id="w195"></a>
### W195. CSRF 보호 생략 여부를 판단할 올바른 기준은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=195)

1. REST API라는 이름이면 무조건 꺼도 된다
2. 브라우저가 인증 정보를 자동 첨부하는 방식과 공격 경로를 확인한다
3. 쿠키 기반 로그인에는 CSRF 위험이 없다
4. JWT를 쿠키에 넣으면 항상 CSRF가 사라진다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 브라우저가 인증 정보를 자동 첨부하는 방식과 공격 경로를 확인한다

쿠키·HTTP 인증 등의 자동 전송 여부와 실제 인증 방식을 기준으로 판단합니다.

</details>


<a id="w196"></a>
### W196. PreAuthorize 사용을 위해 필요한 대표 설정은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=196)

1. EnableJpaAuditing만
2. EntityScan만
3. NotBlank만
4. EnableMethodSecurity

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · EnableMethodSecurity

어노테이션을 붙였다는 사실만으로 메서드 보안이 활성화되지는 않습니다.

</details>


<a id="w197"></a>
### W197. 일반적인 JWT 인증 클레임 조합은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=197)

1. 평문 password·DB root 비밀번호
2. sub·exp와 필요한 역할 정보
3. CSS 색상·글꼴·이미지 주소만
4. 모든 JPA 엔티티 전체

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · sub·exp와 필요한 역할 정보

payload는 인코딩된 것이며 비밀 저장소가 아닙니다. 서명·발급자·대상·만료도 검증해야 합니다.

</details>


<a id="w198"></a>
### W198. AccessToken의 만료 시간을 짧게 두는 주된 목적은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=198)

1. 유출 토큰이 사용될 수 있는 시간을 줄인다
2. 토큰 서명 검사를 없앤다
3. 즉시 폐기를 별도 정책 없이 보장한다
4. 모든 권한 변경을 자동 반영한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 유출 토큰이 사용될 수 있는 시간을 줄인다

짧은 TTL은 노출 기간을 줄이지만 즉시 폐기와 갱신 정책은 별도로 설계해야 합니다.

</details>


<a id="w199"></a>
### W199. RefreshToken 도입 시 추가로 다룰 위험은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=199)

1. JPA 기본키 길이만
2. Thymeleaf 글꼴만
3. SQL 정렬 오름차순만
4. 장기 토큰 탈취·재사용과 안전한 회전·폐기

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 장기 토큰 탈취·재사용과 안전한 회전·폐기

갱신 토큰은 수명이 길 수 있어 저장 보호·회전·재사용 감지·폐기 정책이 중요합니다.

</details>


<a id="w200"></a>
### W200. 토큰 서명 비밀키를 소스와 분리하는 이유는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=200)

1. 환경 변수면 모든 공격을 자동 방어한다
2. 키가 공개되어도 서명이 항상 안전하다
3. 저장소 노출을 줄이고 환경별 관리·회전을 가능하게 한다
4. payload를 CSS로 바꾸기 위해서다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 저장소 노출을 줄이고 환경별 관리·회전을 가능하게 한다

환경 변수 또는 비밀 저장소를 사용하되 로그·권한·배포 노출도 관리해야 합니다.

</details>


<a id="w201"></a>
### W201. JWT에 담긴 역할 변경을 즉시 반영하려면?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=201)

1. 매 요청 권한 조회나 토큰 버전·폐기 등 별도 정책이 필요하다
2. DB 역할을 바꾸면 발급된 JWT 내용도 자동 바뀐다
3. exp만 삭제하면 즉시 반영된다
4. payload의 문자열을 브라우저가 수정해 보내면 된다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 매 요청 권한 조회나 토큰 버전·폐기 등 별도 정책이 필요하다

서명된 기존 토큰은 재발급·만료 또는 서버 확인 정책 없이는 그대로 유효할 수 있습니다.

</details>


<a id="w202"></a>
### W202. 모바일 API에서 Authorization Bearer 토큰을 쓰는 특징은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=202)

1. 토큰이 있으면 HTTPS가 필요 없다
2. 모바일이면 만료 검사를 생략한다
3. 명시 헤더로 전송하고 기기 저장·유출 위험을 따로 관리한다
4. 쿠키는 모든 모바일 환경에서 기술적으로 금지된다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 명시 헤더로 전송하고 기기 저장·유출 위험을 따로 관리한다

전송 편의와 안전한 저장·수명·갱신 정책을 구분해 판단합니다.

</details>


<a id="w249"></a>
### W249. 상태 규칙을 Service에만 두면 어떤 경로에서 깨질 수 있는가?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=249)

1. DTO에 NotBlank가 있으면 절대 깨지지 않는다
2. DB가 모든 주문 상태 전이를 자동 판단한다
3. 다른 호출부가 도메인 메서드를 직접 사용하면서 검증을 빠뜨릴 수 있다
4. 모든 경로가 반드시 같은 컨트롤러를 거친다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 다른 호출부가 도메인 메서드를 직접 사용하면서 검증을 빠뜨릴 수 있다

중요 불변식을 도메인 동작에 모으면 다른 진입 경로에서도 같은 규칙을 보호합니다.

</details>


<a id="w259"></a>
### W259. 현재 수주 출고·완료 처리가 허용되는 역할은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=259)

1. USER만
2. 비로그인만
3. SUPPLIER 또는 CUSTOMER
4. ADMIN 또는 MANAGER

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · ADMIN 또는 MANAGER

역할 검증 뒤 허용 상태·재고 등 업무 조건도 검사합니다.

</details>


<a id="w268"></a>
### W268. 컨트롤러에만 역할 검사를 두고 Service를 보호하지 않으면?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=268)

1. DB unique가 모든 인가를 대신한다
2. 메뉴 표시 조건이 모든 호출을 차단한다
3. 컨트롤러 없는 호출은 Java에서 불가능하다
4. 다른 진입 경로가 Service를 호출할 때 업무 인가를 우회할 수 있다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 다른 진입 경로가 Service를 호출할 때 업무 인가를 우회할 수 있다

서버 업무 경계에서 필요한 인가를 보장해야 합니다.

</details>


<a id="w272"></a>
### W272. 세션 고정 공격의 전형적인 흐름은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=272)

1. 공격자가 아는 세션 ID를 피해자가 인증 후에도 쓰게 한다
2. DB unique로 중복 품목을 차단한다
3. 승인 시 Version이 증가한다
4. Thymeleaf가 문자열을 escape한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 공격자가 아는 세션 ID를 피해자가 인증 후에도 쓰게 한다

로그인 성공 시 세션 ID를 변경해 인증 전 ID의 재사용 위험을 줄입니다.

</details>
