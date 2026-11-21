# 비즈니스 규칙 다층 검증 매트릭스 (TRD 3.8, 3.11) · 4지 선다

[이 모듈을 브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&module=37) · [전체 문제집](../../workbook-questions.md)

각 문제에서 1~4번 중 하나를 고르고 정답·해설을 확인합니다.

<a id="w027"></a>
### W027. totalAmount와 lineAmount 합계의 불변식을 지키는 방법은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=27)

1. 브라우저가 보낸 totalAmount를 검증 없이 저장한다
2. 모든 변경 경로가 동일한 서버 재계산 규칙을 사용하게 한다
3. 헤더 금액을 라인과 무관한 난수로 정한다
4. DB 조회 때마다 작성자를 바꾼다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 모든 변경 경로가 동일한 서버 재계산 규칙을 사용하게 한다

도메인 또는 Service의 배치는 설계 선택이지만 금액 계산은 서버에서 일관되어야 합니다.

</details>


<a id="w040"></a>
### W040. 품목코드 중복을 안전하게 차단하는 조합은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=40)

1. 화면의 경고 문구만 표시
2. Service 사전 검사와 DB unique 제약
3. Repository exists 검사만으로 동시성까지 보장
4. 등록 이후 이름만 비교

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · Service 사전 검사와 DB unique 제약

사전 검사는 친절한 오류를, unique 제약은 경쟁 요청의 최종 방어를 제공합니다.

</details>


<a id="w048"></a>
### W048. 발주 거래처 유형 검증은 어디에서 수행해야 하는가?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=48)

1. 화면 필터만으로 충분하다
2. Service에서 SUPPLIER 또는 BOTH인지 검사한다
3. 브라우저 북마크에서 검사한다
4. DB 포트 번호에서 검사한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · Service에서 SUPPLIER 또는 BOTH인지 검사한다

화면 조건은 우회 가능하므로 업무 계층에서 거래처 유형을 확인합니다.

</details>


<a id="w049"></a>
### W049. 동시 발주번호 채번의 안전한 전략은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=49)

1. count + 1만 읽으면 항상 유일하다
2. 사용자 이름만 발주번호로 쓴다
3. 같은 번호를 허용하고 나중에 주문을 삭제한다
4. DB unique와 충돌 재시도 등 동시성 대책을 결합한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · DB unique와 충돌 재시도 등 동시성 대책을 결합한다

참조 구현은 최대 번호와 unique 충돌의 독립 트랜잭션 재시도를 사용합니다.

</details>


<a id="w051"></a>
### W051. REQUESTED가 아닌 발주의 승인·반려를 막는 설계는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=51)

1. 도메인 전이 메서드에 상태 가드를 둔다
2. 화면 버튼만 숨긴다
3. 승인 후 작성자 이메일을 바꾼다
4. DTO 필드 이름을 바꾸면 충분하다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 도메인 전이 메서드에 상태 가드를 둔다

Service 인가와 별도로 도메인 메서드가 상태 불변식을 보호합니다.

</details>


<a id="w052"></a>
### W052. 반려 사유가 공백이면 어떻게 처리하는가?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=52)

1. 자동으로 승인한다
2. RECEIVED로 바꾼다
3. 검증 오류로 거절하고 상태를 변경하지 않는다
4. 공백을 그대로 저장해 반려한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 검증 오류로 거절하고 상태를 변경하지 않는다

필수 사유는 DTO 및 도메인·Service 검증으로 보장합니다.

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


<a id="w056"></a>
### W056. 같은 발주를 두 관리자가 동시에 승인하면 필요한 대응은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=56)

1. 둘 다 승인 이력을 덮어써도 무방하다
2. 버전 충돌을 감지해 중복 상태 변경을 차단한다
3. 인터넷 속도가 빠르면 충돌이 없다
4. 각 요청이 다른 DB를 초기화한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 버전 충돌을 감지해 중복 상태 변경을 차단한다

PurchaseOrder의 Version 낙관적 락이 경쟁 상태 변경을 감지합니다.

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


<a id="w062"></a>
### W062. 신규 수주의 거래처 유형 검증은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=62)

1. SUPPLIER 전용만 허용한다
2. Service에서 CUSTOMER 또는 BOTH를 허용한다
3. 브라우저 필터만 검사한다
4. 거래처 유형을 무시한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · Service에서 CUSTOMER 또는 BOTH를 허용한다

수주는 판매 흐름이므로 고객 역할이 있는 거래처를 요구합니다.

</details>


<a id="w063"></a>
### W063. 신규 주문 라인에 단종 품목이 오면?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=63)

1. 신규 라인에 무조건 허용한다
2. 품목을 자동 삭제한다
3. ITEM_DISCONTINUED로 거절한다
4. 거래처를 관리자 계정으로 변경한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · ITEM_DISCONTINUED로 거절한다

단종은 신규 사용 제한이며 과거 라인은 보존합니다.

</details>


<a id="w064"></a>
### W064. 수주 출고·완료 상태 전이의 올바른 규칙은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=64)

1. DRAFT에서 바로 완료한다
2. COMPLETED에서 다시 출고한다
3. CANCELED에서 재고를 차감한다
4. CONFIRMED에서 출고하고 SHIPPED에서 완료한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · CONFIRMED에서 출고하고 SHIPPED에서 완료한다

종료 상태 추가 전이는 차단하고 취소 가능 상태를 따로 검증합니다.

</details>


<a id="w074"></a>
### W074. NotBlank·Positive·Size·Pattern의 역할 조합은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=74)

1. 공백 금지·양수·길이/개수·정규식 검증
2. DB 생성·로그인·정렬·출고 처리
3. 모두 같은 문자열 길이 검사
4. 모두 권한 검사

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 공백 금지·양수·길이/개수·정규식 검증

각 어노테이션은 적용 타입과 검사하는 입력 조건이 다릅니다.

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


<a id="w099"></a>
### W099. 사업자번호 exists 검사와 unique 제약을 함께 두는 이유는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=99)

1. 사전 안내와 동시 등록의 최종 무결성을 각각 보장한다
2. exists 검사로 모든 경쟁 상태가 제거된다
3. unique 제약으로 화면 입력 형식까지 검증한다
4. 중복 번호를 일부러 두 번 저장한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 사전 안내와 동시 등록의 최종 무결성을 각각 보장한다

사전 조회 이후 다른 트랜잭션이 저장할 수 있으므로 DB 제약이 필요합니다.

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


<a id="w124"></a>
### W124. 본인 발주 목록의 소유권 기준은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=124)

1. 요청 쿼리 writerId를 인증 없이 신뢰한다
2. 클라이언트 이름만 비교한다
3. 현재 인증 사용자의 ID로 작성자 조건을 적용한다
4. 모든 사용자에게 전체 목록을 반환한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 현재 인증 사용자의 ID로 작성자 조건을 적용한다

URL 값 조작에 의한 IDOR를 서버 인증 정보 기반 조회로 방지합니다.

</details>


<a id="w161"></a>
### W161. DTO·Entity·Service·DB 검증 책임의 조합은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=161)

1. 화면 글꼴·CSS·이미지·북마크
2. 모두 HTTP 쿠키 생성
3. 모두 같은 역할 문자열 비교만
4. 입력 형식·불변식·업무 흐름·최종 무결성

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 입력 형식·불변식·업무 흐름·최종 무결성

검증은 서로 다른 진입 경로·업무 경계·경쟁 상태를 방어합니다.

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


<a id="w163"></a>
### W163. exists 검사 이후 같은 값이 다른 요청에서 저장되면?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=163)

1. DB unique 제약이 최종 중복 저장을 차단한다
2. exists 결과가 모든 트랜잭션에 영구 고정된다
3. 화면 색이 바뀌면 중복이 제거된다
4. 뒤 요청은 무조건 다른 값을 자동 생성한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · DB unique 제약이 최종 중복 저장을 차단한다

사전 조회와 저장 사이의 경쟁 상태는 DB 제약으로 다룹니다.

</details>


<a id="w164"></a>
### W164. 도메인 IllegalStateException을 API ErrorCode로 바꾸는 위치는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=164)

1. Entity가 HttpServletResponse를 직접 조작한다
2. CSS에서 HTTP status를 바꾼다
3. Service 등 명시한 경계에서 일관된 업무 예외로 변환한다
4. 브라우저가 DB 예외를 그대로 출력한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · Service 등 명시한 경계에서 일관된 업무 예외로 변환한다

현재 Service는 도메인 상태 실패를 INVALID_STATUS 등 안정적인 계약으로 변환합니다.

</details>


<a id="w165"></a>
### W165. 승인 중복·부분 저장·민감정보·LAZY·유형 오류·채번 경쟁을 다룰 때의 기준은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=165)

1. 모든 문제를 화면 새로 고침으로만 해결한다
2. 각 실패 경계를 재현하고 상태·트랜잭션·DTO·조회·검증·동시성 대책을 검증한다
3. 예외를 전부 무시한다
4. 비밀번호를 응답에 추가해 원인을 찾는다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 각 실패 경계를 재현하고 상태·트랜잭션·DTO·조회·검증·동시성 대책을 검증한다

실패 종류마다 다른 원인과 대책이 있으며 회귀 테스트로 근거를 남깁니다.

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


<a id="w179"></a>
### W179. 품목이 반드시 속해야 하는 마스터는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=179)

1. HttpSession
2. Notice
3. PasswordEncoder
4. Category

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · Category

Item은 하나의 카테고리를 참조하며 저장·변경 때 존재 여부를 검증합니다.

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


<a id="w184"></a>
### W184. 경쟁 상태 변경을 다루는 두 가지 DB 잠금 방식은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=184)

1. 낙관적 락과 비관적 락
2. HTML 락과 CSS 락
3. 세션 글꼴과 북마크
4. 문자열 escape와 URL encoding

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 낙관적 락과 비관적 락

버전 충돌 감지와 DB 잠금은 서로 다른 전략이며 경쟁 수준과 업무 요구로 선택합니다.

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


<a id="w215"></a>
### W215. 너무 큰 Service를 리팩터링하는 적절한 방향은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=215)

1. 모든 Service를 하나의 더 큰 클래스에 합친다
2. Controller로 업무 규칙을 모두 옮긴다
3. 응집된 업무·정책 단위로 책임을 분리한다
4. 테스트를 삭제해 파일을 줄인다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 응집된 업무·정책 단위로 책임을 분리한다

도메인 서비스·정책 객체 등으로 응집도와 의존 방향을 개선하고 동작을 검증합니다.

</details>


<a id="w220"></a>
### W220. 단가 0 이상 규칙의 다층 방어는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=220)

1. 브라우저 placeholder만 설정한다
2. 금액이 음수여도 서버는 저장한다
3. DTO의 DecimalMin과 도메인·Service의 값 검증을 맞춘다
4. 모든 금액을 비밀번호 필드로 옮긴다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · DTO의 DecimalMin과 도메인·Service의 값 검증을 맞춘다

외부 형식 검증과 다른 호출 경로의 불변식 보호를 함께 고려합니다.

</details>


<a id="w221"></a>
### W221. 주문 라인 최소 개수 규칙의 입력 1차 방어는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=221)

1. CSS margin 설정
2. DTO의 Size(min=1)와 중첩 입력 검증
3. RequestParam의 기본 포트 변경
4. 조회수 내림차순

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · DTO의 Size(min=1)와 중첩 입력 검증

입력 경계에서 검증하고 Service·도메인에서도 필요한 불변식을 보장합니다.

</details>


<a id="w222"></a>
### W222. 과거 공급 이력 때문에 BOTH→CUSTOMER를 금지하는 정책이라면 어디서 검사하는가?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=222)

1. 화면 select에서만 선택지를 숨긴다
2. 브라우저 localStorage만 확인한다
3. 엔티티가 HttpSession을 직접 읽는다
4. 관련 이력을 조회하는 Service 정책에서 검증한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 관련 이력을 조회하는 Service 정책에서 검증한다

이 문제는 해당 금지 정책을 전제로 합니다. 이력 조회가 필요한 업무 정책을 Service에서 조정합니다.

</details>


<a id="w248"></a>
### W248. INACTIVE 거래처의 신규 발주·수주 사용을 어디에서 막는가?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=248)

1. CSS에서 버튼 색상만 변경
2. 거래처를 조회한 Service 업무 검증
3. 브라우저 주소 표시줄
4. README의 제목

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 거래처를 조회한 Service 업무 검증

화면 우회를 막기 위해 서버 업무 계층에서 거래처 상태를 검사합니다.

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


<a id="w256"></a>
### W256. 수주 확정의 정상 상태 전이는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=256)

1. CONFIRMED → SHIPPED
2. SHIPPED → COMPLETED
3. DRAFT → CONFIRMED
4. COMPLETED → DRAFT

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · DRAFT → CONFIRMED

확정과 출고·완료는 별도 명령과 검증 경계를 갖습니다.

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


<a id="w269"></a>
### W269. 객관식 학습 완료를 확인하는 적절한 방법은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=269)

1. 답 번호만 외우면 모든 구현이 검증된다
2. 오답을 다시 풀고 해설의 규칙을 실제 코드·테스트와 대조한다
3. 정답 화면을 본 횟수만 센다
4. 문제 데이터를 임의로 삭제한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 오답을 다시 풀고 해설의 규칙을 실제 코드·테스트와 대조한다

객관식 채점과 코드 구현의 검증은 각각의 학습·확인 절차를 갖습니다.

</details>
