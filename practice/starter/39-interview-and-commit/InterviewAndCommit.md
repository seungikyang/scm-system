# 면접 카드 + 커밋 + PR 템플릿 (TRD 3.18 ~ 3.20) · 4지 선다

[이 모듈을 브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&module=39) · [전체 문제집](../../workbook-questions.md)

각 문제에서 1~4번 중 하나를 고르고 정답·해설을 확인합니다.

<a id="w014"></a>
### W014. PartnerType과 canSupply·canBuy 헬퍼의 목적은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=14)

1. DB 연결 비밀번호를 생성한다
2. 공급·판매 가능 여부를 도메인 규칙으로 모은다
3. INACTIVE 거래처도 항상 주문을 허용한다
4. 고객사와 공급사의 의미를 제거한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 공급·판매 가능 여부를 도메인 규칙으로 모은다

SUPPLIER·CUSTOMER·BOTH의 업무 의미를 헬퍼에 모으면 조건 중복을 줄일 수 있습니다.

</details>


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


<a id="w169"></a>
### W169. 버그 수정·리팩터링·문서·테스트의 커밋 타입 조합은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=169)

1. feat·feat·feat·feat
2. style·docs·fix·chore
3. fix·refactor·docs·test
4. chore·style·test·refactor

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · fix·refactor·docs·test

feat는 기능, fix는 버그, refactor는 구조, docs는 문서, test는 테스트 변경을 표현합니다.

</details>


<a id="w170"></a>
### W170. 커밋 본문에서 선택 이유를 설명하는 목적은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=170)

1. diff만으로 보이지 않는 동기와 판단을 전달하기 위해서다
2. 비밀키를 공유하기 위해서다
3. 모든 변경 파일 내용을 다시 복사하기 위해서다
4. 테스트 실패를 숨기기 위해서다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · diff만으로 보이지 않는 동기와 판단을 전달하기 위해서다

변경 내용은 diff로 확인하고 본문은 이유와 필요한 맥락을 전달합니다.

</details>


<a id="w171"></a>
### W171. PR 설명의 기본 네 가지 내용은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=171)

1. 비밀번호·salt·token·cookie
2. 색상·글꼴·이미지만
3. 파일명·파일명·파일명·파일명
4. 무엇·왜·검증·영향 범위

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 무엇·왜·검증·영향 범위

변경 결과와 이유·테스트·마이그레이션 등 영향 정보를 검토자가 이해할 수 있게 적습니다.

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


<a id="w233"></a>
### W233. 발주 헤더와 라인을 같은 aggregate로 관리하는 이유는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=233)

1. 라인·총금액·상태 불변식을 주문 단위로 일관되게 관리하기 위해서다
2. 모든 거래처를 같은 엔티티로 합치기 위해서다
3. 모든 라인을 독립 트랜잭션으로 저장하기 위해서다
4. 권한 검사를 생략하기 위해서다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 라인·총금액·상태 불변식을 주문 단위로 일관되게 관리하기 위해서다

주문 전체가 일관된 구성·금액·생명주기 경계를 갖게 합니다.

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


<a id="w240"></a>
### W240. 이미 승인된 발주 재승인을 차단한 커밋의 적절한 타입은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=240)

1. fix
2. style
3. docs
4. chore

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · fix

오류 동작을 수정했으므로 fix 타입이 자연스럽습니다.

</details>


<a id="w241"></a>
### W241. 동작을 유지하며 상태 전이 코드를 도메인 메서드로 옮긴 타입은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=241)

1. feat
2. refactor
3. docs
4. style

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · refactor

기능·결과를 유지하는 구조 개선은 refactor입니다.

</details>


<a id="w242"></a>
### W242. README 실행 방법을 보완한 커밋의 타입은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=242)

1. docs
2. fix
3. test
4. feat

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · docs

문서 변경을 나타내는 docs 타입을 사용합니다.

</details>


<a id="w243"></a>
### W243. 발주 승인 권한 실패 테스트를 추가한 커밋의 타입은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=243)

1. style
2. feat
3. test
4. docs

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · test

동작 검증용 테스트 변경은 test입니다.

</details>


<a id="w244"></a>
### W244. 구체적인 버그 수정 커밋 메시지로 적절한 것은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=244)

1. update
2. wip
3. fix: 아무거나 수정
4. fix: REQUESTED가 아닌 발주의 승인 요청 차단

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · fix: REQUESTED가 아닌 발주의 승인 요청 차단

변경 결과·조건·목적을 이해할 수 있는 구체적인 제목이 검토와 추적을 돕습니다.

</details>


<a id="w266"></a>
### W266. 자기소개 문단에 넣을 적절한 기술 설명은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=266)

1. 겪지 않은 장애 수치를 임의로 넣는다
2. 실제 구현한 주문·권한·재고 규칙과 검증 근거를 자신의 역할에 맞게 요약한다
3. 모든 팀원의 성과를 본인 성과로 말한다
4. 기술명만 나열하고 동작은 설명하지 않는다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 실제 구현한 주문·권한·재고 규칙과 검증 근거를 자신의 역할에 맞게 요약한다

개인 표현은 달라도 사실에 맞는 역할·선택·검증을 연결하는 기준은 같습니다.

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


<a id="w359"></a>
### W359. 출고지별 재고로 수주 기능을 확장하는 요구에서 적절한 작업은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=359)

1. 출고지 문자열만 화면에 추가하고 서버는 그대로 둔다
2. 재고 키·출고지 권한·예약/차감·동시성·테스트 계약을 함께 설계한다
3. 재고 부족 검사를 삭제한다
4. 모든 창고의 재고를 무조건 같은 값으로 만든다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 재고 키·출고지 권한·예약/차감·동시성·테스트 계약을 함께 설계한다

추가 요구의 데이터·권한·상태·트랜잭션 경계를 명시해 실제 구현과 검증을 연결합니다.

</details>


<a id="w396"></a>
### W396. 학습 루프의 목표 단계에서 먼저 정할 내용은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=396)

1. 취업 역량이나 사용자 가치와 연결된 한 문장 목표
2. 검증되지 않은 구현의 성공 선언
3. 기존 데이터의 무조건 삭제
4. 변경할 파일 수를 최대로 늘리는 계획

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 취업 역량이나 사용자 가치와 연결된 한 문장 목표

목표는 기능 수보다 증명할 역량이나 개선할 가치와 연결되어야 합니다.

</details>


<a id="w397"></a>
### W397. 루프의 기준선 단계에서 확보할 증거는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=397)

1. 현재 코드·문서·관련 테스트와 실제 동작
2. 예상만으로 작성한 최종 성공 보고
3. 원인 확인 전의 임시 오류 숨김
4. 모든 참조 구현의 삭제

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 현재 코드·문서·관련 테스트와 실제 동작

기준선은 변경 전 동작과 근거를 확인해 이후 검증의 출발점을 만듭니다.

</details>


<a id="w398"></a>
### W398. 계약 단계에서 함께 정리할 요소는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=398)

1. 화면 색상만
2. 성공 메시지의 글꼴만
3. 입력·출력·권한·상태·실패 조건
4. 개발자의 개인 비밀번호

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 입력·출력·권한·상태·실패 조건

규칙표와 API·상태 계약을 통해 성공 경로와 실패 경로의 기대를 명확히 합니다.

</details>


<a id="w399"></a>
### W399. 실패 증거 단계의 목적에 맞는 산출물은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=399)

1. 실패를 지운 로그만
2. 정답 번호를 외운 횟수
3. 검증 없는 완료 표시
4. 잘못된 구현을 잡는 테스트나 재현 절차

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 잘못된 구현을 잡는 테스트나 재현 절차

테스트나 재현 절차는 오류 조건을 실제로 드러내고 수정 효과를 확인할 근거가 됩니다.

</details>


<a id="w400"></a>
### W400. 계약을 확인한 뒤 최소 변경 단계에서 선택할 작업은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=400)

1. 문제와 무관한 모든 기능의 전면 재작성
2. 계약을 만족하는 필요한 코드·문서 변경
3. 테스트와 오류 처리를 일괄 제거
4. 사용자의 개인 풀이를 무조건 덮어쓰기

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 계약을 만족하는 필요한 코드·문서 변경

변경 범위를 계약에 맞춰 좁혀 결과를 검토하고 검증할 수 있게 합니다.

</details>


<a id="w401"></a>
### W401. 검증 단계가 제공해야 하는 근거는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=401)

1. 성공이라고 적은 문장만
2. 아직 실행하지 않은 테스트 이름만
3. 라이브러리 버전 문자열만
4. 적절한 테스트의 실행 명령과 실제 결과

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 적절한 테스트의 실행 명령과 실제 결과

실행한 검사와 결과를 기록합니다. 환경상 실행하지 못한 검증은 성공으로 기록하지 않습니다.

</details>


<a id="w402"></a>
### W402. 루프의 설명 단계에서 이해를 확인하는 방식은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=402)

1. 선택한 설계와 대안·검증 근거를 코드와 대조한다
2. 기술 이름만 나열한다
3. 실패 사례를 모두 감춘다
4. 검증하지 않은 기능을 구현했다고 주장한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 선택한 설계와 대안·검증 근거를 코드와 대조한다

선택 이유는 실제 구현과 증거로 뒷받침합니다. 객관식 정답·해설을 실제 코드에 적용해 이해를 확인합니다.

</details>


<a id="w403"></a>
### W403. 루프의 정리 단계에서 함께 맞출 대상은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=403)

1. 문서만 삭제하고 코드와의 차이를 숨긴다
2. 임시 빌드 결과를 유일한 원본으로 삼는다
3. 코드와 문서·HTML 목차·변경 이력·관련 링크
4. 모든 변경을 검증 없이 게시한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 코드와 문서·HTML 목차·변경 이력·관련 링크

산출물이 같은 동작을 설명하고 파일 링크가 실제 자료를 가리키도록 정리합니다.

</details>
