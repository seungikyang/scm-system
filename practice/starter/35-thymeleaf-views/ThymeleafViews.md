# Thymeleaf 화면 (PRD 2.7) · 4지 선다

[이 모듈을 브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&module=35) · [전체 문제집](../../workbook-questions.md)

각 문제에서 1~4번 중 하나를 고르고 정답·해설을 확인합니다.

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


<a id="w126"></a>
### W126. 발주 상세 조회를 허용하는 현재 정책은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=126)

1. 작성자 또는 ADMIN/MANAGER
2. 아무 비로그인 사용자
3. 작성자가 아닌 USER만
4. 공급사 이메일을 입력한 사람 모두

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 작성자 또는 ADMIN/MANAGER

역할과 작성자 소유권을 인증된 사용자 기준으로 검사합니다.

</details>


<a id="w151"></a>
### W151. Thymeleaf 화면과 정적 리소스의 기본 위치는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=151)

1. 모든 템플릿을 gradle 캐시에 보관한다
2. templates와 static 아래에서 역할별로 분리한다
3. 엔티티 package에 CSS만 둔다
4. 모든 화면을 DB 비밀번호 컬럼에 넣는다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · templates와 static 아래에서 역할별로 분리한다

서버 렌더링 템플릿과 직접 제공하는 CSS·JS 리소스의 위치를 구분합니다.

</details>


<a id="w152"></a>
### W152. 사용자 입력을 표시할 때 안전한 기본 Thymeleaf 속성은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=152)

1. HTML escape를 수행하는 th:text
2. 항상 th:utext
3. 사용자 입력을 script 태그로 평가
4. HTML을 모두 비밀번호 필드에 표시

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · HTML escape를 수행하는 th:text

th:utext는 신뢰·정화된 HTML을 의도적으로 표시할 때만 신중히 사용합니다.

</details>


<a id="w153"></a>
### W153. Spring Security와 Thymeleaf의 th:action POST 폼에서 CSRF는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=153)

1. 모든 fetch 요청에 토큰이 자동으로 붙는다
2. 통합된 토큰 hidden input을 사용하고 JS 요청은 토큰을 명시 전달한다
3. 폼이면 CSRF 검사가 항상 비활성화된다
4. 브라우저가 토큰을 비밀번호로 해시한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 통합된 토큰 hidden input을 사용하고 JS 요청은 토큰을 명시 전달한다

서버 폼 통합과 일반 HTML·JavaScript 요청의 토큰 전달 방식을 구분합니다.

</details>


<a id="w154"></a>
### W154. Thymeleaf Fragment 재사용의 기본 조합은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=154)

1. th:fragment 정의와 th:replace 사용
2. th:password와 th:session
3. SQL unique와 CSS color
4. JPA Id와 RequestBody

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · th:fragment 정의와 th:replace 사용

공통 레이아웃을 조각으로 정의해 여러 화면에서 재사용합니다.

</details>


<a id="w155"></a>
### W155. 동적 주문 라인 입력에서 중요한 것은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=155)

1. 브라우저 총금액을 검증 없이 신뢰한다
2. 삭제된 라인의 비밀번호를 전송한다
3. 동적 줄 추가가 DB 트랜잭션을 대신한다
4. 추가·삭제 후 입력 name 인덱스와 서버 검증을 유지한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 추가·삭제 후 입력 name 인덱스와 서버 검증을 유지한다

사용성 보조 JS와 서버 바인딩·업무 검증이 함께 맞아야 합니다.

</details>


<a id="w156"></a>
### W156. RestController와 Controller의 기본 응답 차이는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=156)

1. 둘 다 항상 DB 엔티티를 저장한다
2. Controller는 JSON 응답을 절대 만들 수 없다
3. RestController는 본문 응답, Controller는 View 이름 반환 등을 사용한다
4. RestController는 Service를 호출할 수 없다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · RestController는 본문 응답, Controller는 View 이름 반환 등을 사용한다

웹 화면과 API는 같은 Service를 공유하며 표현 방식을 나눌 수 있습니다.

</details>


<a id="w177"></a>
### W177. 응답 DTO에서 제외해야 할 정보는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=177)

1. 비밀번호 해시·내부 토큰 등 민감정보
2. 공개 품목 이름
3. 공개 카테고리 이름
4. 공지 제목

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 비밀번호 해시·내부 토큰 등 민감정보

평문뿐 아니라 비밀번호 해시와 내부 인증 자료도 응답 계약에서 제외합니다.

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


<a id="w203"></a>
### W203. CSRF 보호가 활성화된 폼 변경 요청에 토큰이 없으면?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=203)

1. 항상 201로 저장된다
2. 보통 403으로 거절된다
3. 비밀번호가 자동 변경된다
4. JPA가 무조건 404를 반환한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 보통 403으로 거절된다

보호 대상 메서드·경로와 정책을 전제로 합니다. 현재 API 예외 정책과 일반 원칙을 구분합니다.

</details>


<a id="w204"></a>
### W204. th:replace와 th:insert의 차이는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=204)

1. 둘 다 SQL UPDATE를 실행한다
2. replace는 항상 escape를 제거한다
3. insert는 로그인 세션을 발급한다
4. replace는 대상 요소를 교체하고 insert는 내부에 조각을 넣는다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · replace는 대상 요소를 교체하고 insert는 내부에 조각을 넣는다

호스트 요소의 유지 여부가 달라 최종 HTML 구조에 영향을 줍니다.

</details>


<a id="w205"></a>
### W205. 동적 폼 라인의 name 인덱스를 올바르게 관리해야 하는 이유는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=205)

1. Spring 바인더가 각 라인의 필드를 올바르게 구성하도록 하기 위해서다
2. 관리자 권한을 자동 생성하기 위해서다
3. BigDecimal을 String으로 강제하기 위해서다
4. DB unique를 제거하기 위해서다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · Spring 바인더가 각 라인의 필드를 올바르게 구성하도록 하기 위해서다

중복·누락된 인덱스는 잘못된 값 매핑이나 바인딩 실패로 이어질 수 있습니다.

</details>


<a id="w206"></a>
### W206. 라인 추가마다 서버 화면을 다시 요청하는 방식의 비용은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=206)

1. 브라우저 네트워크 요청이 사라진다
2. 서버 검증이 항상 불필요해진다
3. 라인 추가가 무조건 SQL 삭제가 된다
4. 추가 왕복 지연과 입력 상태 보존 처리가 필요하다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 추가 왕복 지연과 입력 상태 보존 처리가 필요하다

JS 보조는 화면 내 편집을 돕지만 서버의 검증·계산 책임은 유지해야 합니다.

</details>


<a id="w207"></a>
### W207. 같은 도메인의 화면과 API를 함께 운영할 때 좋은 구조는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=207)

1. 두 컨트롤러에 업무 규칙을 각각 복사한다
2. 표현 컨트롤러를 나누고 업무 Service를 공유한다
3. View 컨트롤러가 API 컨트롤러를 직접 HTTP 호출한다
4. 모든 엔티티에 HTML을 저장한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 표현 컨트롤러를 나누고 업무 Service를 공유한다

중복되는 업무 로직을 공통 Service에 모으고 각 표현 계약을 분리합니다.

</details>


<a id="w208"></a>
### W208. Thymeleaf와 SPA를 비교할 때 올바른 설명은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=208)

1. Thymeleaf에서는 JavaScript를 사용할 수 없다
2. SPA에서는 서버 인가가 필요 없다
3. 서버 HTML은 초기 구성에 단순하고 복잡한 상호작용에는 클라이언트 코드가 추가된다
4. 서버 HTML이면 네트워크 지연이 없다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 서버 HTML은 초기 구성에 단순하고 복잡한 상호작용에는 클라이언트 코드가 추가된다

화면 복잡도·팀 기술·렌더링 방식·운영 비용에 맞춰 선택합니다.

</details>


<a id="w209"></a>
### W209. Thymeleaf의 자연 템플릿이 의미하는 것은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=209)

1. HTML 형태를 유지해 브라우저에서 시안을 열어 볼 수 있다
2. 서버 처리 없이 모든 th 속성이 실행된다
3. JSP와 Thymeleaf는 같은 컴파일 파일이다
4. 템플릿은 반드시 DB에 저장되어야 한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · HTML 형태를 유지해 브라우저에서 시안을 열어 볼 수 있다

브라우저의 정적 미리보기와 서버의 표현식 렌더링은 다릅니다.

</details>


<a id="w210"></a>
### W210. th:object와 th:field가 폼에서 돕는 것은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=210)

1. SQL 락과 unique 생성
2. 폼 객체·필드 이름·값·검증 오류의 바인딩
3. 로그인 권한 자동 승인
4. DB 비밀번호 복호화

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 폼 객체·필드 이름·값·검증 오류의 바인딩

객체 기반 폼 필드 구성을 돕고 서버에서 DTO 검증을 수행합니다.

</details>


<a id="w211"></a>
### W211. Spring Boot의 HTTP 상태별 HTML 오류 페이지를 두는 대표 위치는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=211)

1. gradle/wrapper/password.txt
2. static/db/root.sql
3. practice/workspace에만 보관
4. templates/error/404.html 같은 경로

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · templates/error/404.html 같은 경로

기본 오류 처리에 맞는 상태별 템플릿을 사용하거나 현재 WebExceptionHandler 계약에 맞게 구성합니다.

</details>


<a id="w212"></a>
### W212. 현재 화면에서 역할별 메뉴를 보여 주는 것의 보안 의미는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=212)

1. 메뉴를 숨기면 URL 직접 호출도 차단된다
2. sec:authorize는 별도 통합 없이 항상 사용 가능하다
3. 사용 편의를 위한 표시이며 실제 API·Service 인가가 필요하다
4. CSS display:none이 DB 권한을 보장한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 사용 편의를 위한 표시이며 실제 API·Service 인가가 필요하다

현재 화면 조건과 서버 인가를 구분합니다. Security dialect 방식은 관련 통합과 인증 정보를 요구합니다.

</details>


<a id="w394"></a>
### W394. Thymeleaf 템플릿이 items와 page를 모델 변수로 읽게 하는 컨트롤러 처리는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=394)

1. HttpSession.invalidate로 세션을 지운다
2. Pageable의 크기를 0으로 만든다
3. Model.addAttribute로 이름과 값을 등록한다
4. EntityManager.clear로 모델을 생성한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · Model.addAttribute로 이름과 값을 등록한다

컨트롤러가 Model에 이름으로 등록한 값을 템플릿에서 참조합니다. 영속성 컨텍스트나 세션 종료 기능과 구분합니다.

</details>
