# 문서화 (TRD 3.15 ~ 3.17) · 4지 선다

[이 모듈을 브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&module=22) · [전체 문제집](../../workbook-questions.md)

각 문제에서 1~4번 중 하나를 고르고 정답·해설을 확인합니다.

<a id="w031"></a>
### W031. 수주의 정상 처리 상태 흐름은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=31)

1. DRAFT → CONFIRMED → SHIPPED → COMPLETED
2. DRAFT → RECEIVED → REQUESTED → COMPLETED
3. SHIPPED → DRAFT → APPROVED
4. COMPLETED → CONFIRMED → DRAFT

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · DRAFT → CONFIRMED → SHIPPED → COMPLETED

취소 가능 상태는 별도로 관리하며 COMPLETED·CANCELED는 종료 상태입니다.

</details>


<a id="w046"></a>
### W046. 발주 헤더와 라인을 같은 트랜잭션으로 저장하는 이유는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=46)

1. 헤더 저장 후 실패한 라인은 영구 누락시킨다
2. 조회 속도가 항상 정확히 두 배가 된다
3. 트랜잭션이 외래키를 없애 준다
4. 라인 실패 시 헤더까지 롤백해 부분 주문을 방지한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 라인 실패 시 헤더까지 롤백해 부분 주문을 방지한다

주문 구성 요소는 함께 성공하거나 함께 실패해야 합니다.

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


<a id="w080"></a>
### W080. 일반 발주 API와 관리자 API 경로를 나누어도 필요한 것은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=80)

1. 경로 이름만 있으면 누구나 접근해도 된다
2. 관리자 경로에서 실제 역할 인가를 수행한다
3. 관리자 API는 세션을 제거한다
4. 모든 API에 같은 작성자 ID를 저장한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 관리자 경로에서 실제 역할 인가를 수행한다

경로 분리는 계약을 명확히 하지만 실행 권한은 서버 검사로 보장합니다.

</details>


<a id="w090"></a>
### W090. README에 포함할 내용의 올바른 묶음은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=90)

1. 비밀번호 해시와 운영 비밀키만 담는다
2. 기능 이름 한 단어만 담는다
3. 목적·기술·기능·구조·DB·API·실행 등 독자의 시작 정보를 담는다
4. 실행 명령을 모두 제거한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 목적·기술·기능·구조·DB·API·실행 등 독자의 시작 정보를 담는다

개요와 실제로 재현 가능한 실행 절차·문서 연결이 필요합니다.

</details>


<a id="w091"></a>
### W091. ERD 표기 도구를 선택하는 기준은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=91)

1. 갱신·diff·공유·시각 전달 요구를 함께 고려한다
2. 가장 유료인 도구를 무조건 고른다
3. 표기 도구가 DB 무결성을 자동 보장한다
4. 도구 이름만 쓰고 관계를 생략한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 갱신·diff·공유·시각 전달 요구를 함께 고려한다

텍스트와 시각 도구에는 유지 비용과 표현력의 차이가 있습니다.

</details>


<a id="w092"></a>
### W092. 트러블슈팅 기록의 기본 순서는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=92)

1. 해결 → 비밀번호 → CSS → 주문번호
2. 문제 → 원인 → 해결 → 배운 점
3. 기술 이름만 나열
4. 실패 기록을 모두 삭제

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 문제 → 원인 → 해결 → 배운 점

재현 조건·원인·해결 및 검증을 남겨 같은 문제를 다시 분석할 수 있게 합니다.

</details>


<a id="w093"></a>
### W093. Markdown과 자동 생성 API 문서의 올바른 비교는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=93)

1. 자동 문서는 모든 업무 정책을 완벽히 작성한다
2. 수동 문서는 설명·diff에 유리하고 자동 문서도 의미·정책 검토가 필요하다
3. Markdown이면 코드와 절대 어긋나지 않는다
4. 둘 다 인증 조건을 적으면 안 된다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 수동 문서는 설명·diff에 유리하고 자동 문서도 의미·정책 검토가 필요하다

어떤 방식이든 실제 입력·응답·권한·오류 계약과 동기화해야 합니다.

</details>


<a id="w167"></a>
### W167. 본인이 겪은 장애를 포트폴리오에 쓰는 올바른 방식은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=167)

1. 겪지 않은 장애를 해결했다고 꾸민다
2. 다른 사람 수치를 자기 결과로 복사한다
3. 실제 재현·관찰·선택·검증 근거로 작성한다
4. 원인과 해결을 비밀번호 값으로 대신한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 실제 재현·관찰·선택·검증 근거로 작성한다

개인 기록의 내용은 사람마다 다르지만 증거에 근거한 작성 기준은 같습니다.

</details>


<a id="w168"></a>
### W168. 짧은 면접 기술 답변의 좋은 구성은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=168)

1. 문제·선택 이유·대안·코드나 테스트 근거를 연결한다
2. 라이브러리 이름만 외운다
3. 실패 조건을 모두 숨긴다
4. 실제 구현과 다른 정책을 확정 사실로 말한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 문제·선택 이유·대안·코드나 테스트 근거를 연결한다

기술 선택을 구체적인 업무 상황과 검증에 연결합니다.

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


<a id="w181"></a>
### W181. 발주 결재 요청의 정상 전이는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=181)

1. DRAFT → RECEIVED
2. DRAFT → REQUESTED
3. APPROVED → DRAFT
4. RECEIVED → REQUESTED

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · DRAFT → REQUESTED

작성자가 유효한 DRAFT 발주를 요청하며 이미 요청된 상태를 재요청하면 상태 오류가 됩니다.

</details>


<a id="w182"></a>
### W182. 발주 입고의 정상 전이는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=182)

1. DRAFT → RECEIVED
2. REQUESTED → APPROVED
3. APPROVED → RECEIVED
4. CANCELED → RECEIVED

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · APPROVED → RECEIVED

입고는 승인된 발주만 가능하며 재고 증가와 같은 트랜잭션에서 처리합니다.

</details>


<a id="w234"></a>
### W234. User와 Partner를 분리하는 이유는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=234)

1. 모든 거래처가 반드시 로그인해야 하기 때문이다
2. 로그인·권한 계정과 공급·고객 거래처의 책임·생명주기가 다르기 때문이다
3. User가 있으면 Partner는 저장할 수 없기 때문이다
4. 사용자 비밀번호를 거래처 번호로 쓰기 위해서다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 로그인·권한 계정과 공급·고객 거래처의 책임·생명주기가 다르기 때문이다

거래처 마스터는 로그인 계정과 다른 업무 개념입니다.

</details>


<a id="w235"></a>
### W235. 이 프로젝트의 학습 목적을 가장 잘 설명한 것은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=235)

1. 이미지 편집만 제공한다
2. 문제집이 MySQL 서버를 대체한다
3. 비밀번호를 평문으로 공유하는 도구다
4. SCM 마스터·주문 상태·권한·재고 흐름을 Java/Spring으로 구현하고 검증한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · SCM 마스터·주문 상태·권한·재고 흐름을 Java/Spring으로 구현하고 검증한다

실제 기능과 코드·테스트 근거에 맞게 프로젝트 목적을 설명합니다.

</details>


<a id="w236"></a>
### W236. 기본 SCM 프로그램의 로그인 주소와 실행 명령은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=236)

1. python3 questions.tsv / localhost:3306/login
2. node quiz.css / localhost:443/db
3. ./gradlew bootRun / http://localhost:8080/login
4. git status / localhost:8000/mysql

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · ./gradlew bootRun / http://localhost:8080/login

기본 서버 포트는 8080이며 문제집의 정적 서버 8000과 구분합니다.

</details>


<a id="w237"></a>
### W237. ERD를 실제 구현과 맞출 때 필요한 것은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=237)

1. 그림 색만 맞춘다
2. 모든 관계를 무조건 OneToOne으로 표시한다
3. 엔티티·마이그레이션의 키·관계·nullable·제약을 대조한다
4. 모든 SQL 파일을 삭제한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 엔티티·마이그레이션의 키·관계·nullable·제약을 대조한다

업무 관계와 실제 JPA 매핑·DB 외래키는 구분해 정확히 표시합니다.

</details>


<a id="w238"></a>
### W238. 현재 API 인증 조건을 문서에 쓸 때 올바른 방식은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=238)

1. 모든 API가 JWT Bearer라고 가정한다
2. 운영 비밀키를 문서에 복사한다
3. 관리자 API라는 URL 이름만 적는다
4. 세션 쿠키·로그인·역할과 현재 CSRF 정책을 명시한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 세션 쿠키·로그인·역할과 현재 CSRF 정책을 명시한다

현재 참조 구현은 세션 기반이므로 인증 방식과 예외 정책을 실제 코드와 맞춥니다.

</details>


<a id="w239"></a>
### W239. 텍스트 ERD와 시각 ERD를 함께 사용할 때의 올바른 설명은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=239)

1. 그림을 만들면 DB 스키마가 자동 변경된다
2. 텍스트는 diff·갱신, 시각 자료는 관계 전달에 유리하지만 동기화가 필요하다
3. 텍스트 ERD에는 기본키를 쓸 수 없다
4. 시각 ERD는 관계 검증이 불필요하다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 텍스트는 diff·갱신, 시각 자료는 관계 전달에 유리하지만 동기화가 필요하다

도구에 대한 취향보다 변경 추적과 독자의 이해 요구를 기준으로 판단합니다.

</details>


<a id="w265"></a>
### W265. README의 화면 캡처·데모 자료가 해야 할 역할은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=265)

1. 실제 구현과 재현 가능한 기능 흐름을 보여 준다
2. 구현하지 않은 기능을 완성했다고 보여 준다
3. 운영 비밀키와 비밀번호 해시를 공개한다
4. 오류 화면을 숨기기만 한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 실제 구현과 재현 가능한 기능 흐름을 보여 준다

시연 자료는 현재 기능과 실행 방법을 독자가 확인하는 증거입니다.

</details>


<a id="w267"></a>
### W267. 신규 주문의 헤더를 저장하기 전에 해야 할 일은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=267)

1. 헤더만 먼저 독립 커밋하고 오류 라인을 버린다
2. 클라이언트 금액을 그대로 저장한다
3. 라인 존재·품목·수량·단가 등 입력과 업무 규칙을 검증한다
4. 검증은 상세 화면을 볼 때만 한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 라인 존재·품목·수량·단가 등 입력과 업무 규칙을 검증한다

사전 검증과 주문 전체 트랜잭션으로 부분 저장과 잘못된 주문을 방지합니다.

</details>


<a id="w274"></a>
### W274. PUT·PATCH 입력 정책과 허용 필드를 기록할 문서는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=274)

1. 비밀번호 해시 컬럼
2. Gradle 다운로드 캐시
3. 실제 코드와 동기화한 API 명세
4. 브라우저 세션 쿠키

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 실제 코드와 동기화한 API 명세

입력·응답·권한·오류·수정 의미를 독자가 재현할 수 있게 API 계약에 적습니다.

</details>
