# PRD/TRD 3.19 면접 대비 · 4지 선다

[브라우저 문제집](../practice/sw-quiz/index.html?bank=workbook) · [전체 객관식 워크북](../practice/workbook-questions.md)

서술형·개념 확인 질문을 4지 선다로 전환했습니다. 한 문제에 하나의 답을 고른 뒤 해설을 확인하세요.

<a id="w235"></a>
### W235. 이 프로젝트의 학습 목적을 가장 잘 설명한 것은?

[브라우저에서 풀기](../practice/sw-quiz/index.html?bank=workbook&question=235)

1. 이미지 편집만 제공한다
2. 문제집이 MySQL 서버를 대체한다
3. 비밀번호를 평문으로 공유하는 도구다
4. SCM 마스터·주문 상태·권한·재고 흐름을 Java/Spring으로 구현하고 검증한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · SCM 마스터·주문 상태·권한·재고 흐름을 Java/Spring으로 구현하고 검증한다

실제 기능과 코드·테스트 근거에 맞게 프로젝트 목적을 설명합니다.

</details>


<a id="w233"></a>
### W233. 발주 헤더와 라인을 같은 aggregate로 관리하는 이유는?

[브라우저에서 풀기](../practice/sw-quiz/index.html?bank=workbook&question=233)

1. 라인·총금액·상태 불변식을 주문 단위로 일관되게 관리하기 위해서다
2. 모든 거래처를 같은 엔티티로 합치기 위해서다
3. 모든 라인을 독립 트랜잭션으로 저장하기 위해서다
4. 권한 검사를 생략하기 위해서다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 라인·총금액·상태 불변식을 주문 단위로 일관되게 관리하기 위해서다

주문 전체가 일관된 구성·금액·생명주기 경계를 갖게 합니다.

</details>


<a id="w361"></a>
### W361. SUPPLIER·CUSTOMER·BOTH enum과 두 boolean 역할 플래그를 비교하면?

[브라우저에서 풀기](../practice/sw-quiz/index.html?bank=workbook&question=361)

1. 두 플래그는 어떤 조합도 자동으로 항상 유효하다
2. enum은 허용 조합을 명시하고 플래그는 조합 유효성 정책을 별도로 정해야 한다
3. enum은 DB에 저장할 수 없다
4. BOTH를 저장하면 공지 관리자 권한이 생긴다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · enum은 허용 조합을 명시하고 플래그는 조합 유효성 정책을 별도로 정해야 한다

현재 업무에서 허용하는 역할 조합과 확장 요구에 맞춰 표현 방식을 선택합니다.

</details>


<a id="w017"></a>
### W017. 여러 Item이 하나의 Category에 속하는 매핑은?

[브라우저에서 풀기](../practice/sw-quiz/index.html?bank=workbook&question=17)

1. 모든 Item에 OneToOne을 사용한다
2. Item과 Category를 같은 기본키로 강제한다
3. Item에서 ManyToOne과 category 외래키를 사용한다
4. 카테고리를 HTTP 세션으로만 저장한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · Item에서 ManyToOne과 category 외래키를 사용한다

여러 품목이 같은 카테고리를 참조하는 N:1 관계입니다.

</details>


<a id="w051"></a>
### W051. REQUESTED가 아닌 발주의 승인·반려를 막는 설계는?

[브라우저에서 풀기](../practice/sw-quiz/index.html?bank=workbook&question=51)

1. 도메인 전이 메서드에 상태 가드를 둔다
2. 화면 버튼만 숨긴다
3. 승인 후 작성자 이메일을 바꾼다
4. DTO 필드 이름을 바꾸면 충분하다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 도메인 전이 메서드에 상태 가드를 둔다

Service 인가와 별도로 도메인 메서드가 상태 불변식을 보호합니다.

</details>


<a id="w267"></a>
### W267. 신규 주문의 헤더를 저장하기 전에 해야 할 일은?

[브라우저에서 풀기](../practice/sw-quiz/index.html?bank=workbook&question=267)

1. 헤더만 먼저 독립 커밋하고 오류 라인을 버린다
2. 클라이언트 금액을 그대로 저장한다
3. 라인 존재·품목·수량·단가 등 입력과 업무 규칙을 검증한다
4. 검증은 상세 화면을 볼 때만 한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 라인 존재·품목·수량·단가 등 입력과 업무 규칙을 검증한다

사전 검증과 주문 전체 트랜잭션으로 부분 저장과 잘못된 주문을 방지합니다.

</details>


<a id="w046"></a>
### W046. 발주 헤더와 라인을 같은 트랜잭션으로 저장하는 이유는?

[브라우저에서 풀기](../practice/sw-quiz/index.html?bank=workbook&question=46)

1. 헤더 저장 후 실패한 라인은 영구 누락시킨다
2. 조회 속도가 항상 정확히 두 배가 된다
3. 트랜잭션이 외래키를 없애 준다
4. 라인 실패 시 헤더까지 롤백해 부분 주문을 방지한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 라인 실패 시 헤더까지 롤백해 부분 주문을 방지한다

주문 구성 요소는 함께 성공하거나 함께 실패해야 합니다.

</details>


<a id="w177"></a>
### W177. 응답 DTO에서 제외해야 할 정보는?

[브라우저에서 풀기](../practice/sw-quiz/index.html?bank=workbook&question=177)

1. 비밀번호 해시·내부 토큰 등 민감정보
2. 공개 품목 이름
3. 공개 카테고리 이름
4. 공지 제목

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 비밀번호 해시·내부 토큰 등 민감정보

평문뿐 아니라 비밀번호 해시와 내부 인증 자료도 응답 계약에서 제외합니다.

</details>


<a id="w084"></a>
### W084. 현재 보안 책임 분담은?

[브라우저에서 풀기](../practice/sw-quiz/index.html?bank=workbook&question=84)

1. Security가 모든 세션 사용자 인가를 자동으로 맡는다
2. Thymeleaf만 모든 인가를 수행한다
3. Repository가 세션 쿠키를 발급한다
4. Interceptor·Service는 인증/인가, Security는 CSRF·헤더를 맡는다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · Interceptor·Service는 인증/인가, Security는 CSRF·헤더를 맡는다

현재 참조 구현과 Spring Security가 전체 인증을 맡는 대안 설계를 구분합니다.

</details>


<a id="w106"></a>
### W106. 소수 카테고리 전체 조회가 대규모에서 달라져야 하는 이유는?

[브라우저에서 풀기](../practice/sw-quiz/index.html?bank=workbook&question=106)

1. 1000개가 되면 모든 데이터가 자동 삭제된다
2. 응답 크기·메모리·지연이 늘어 페이징을 고려해야 한다
3. List 반환은 Java에서 항상 금지되어 있다
4. 전체 조회면 DB 연결이 필요 없다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 응답 크기·메모리·지연이 늘어 페이징을 고려해야 한다

데이터 규모와 화면 요구에 맞춰 전체 조회 또는 페이지 계약을 선택합니다.

</details>


<a id="w359"></a>
### W359. 출고지별 재고로 수주 기능을 확장하는 요구에서 적절한 작업은?

[브라우저에서 풀기](../practice/sw-quiz/index.html?bank=workbook&question=359)

1. 출고지 문자열만 화면에 추가하고 서버는 그대로 둔다
2. 재고 키·출고지 권한·예약/차감·동시성·테스트 계약을 함께 설계한다
3. 재고 부족 검사를 삭제한다
4. 모든 창고의 재고를 무조건 같은 값으로 만든다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 재고 키·출고지 권한·예약/차감·동시성·테스트 계약을 함께 설계한다

추가 요구의 데이터·권한·상태·트랜잭션 경계를 명시해 실제 구현과 검증을 연결합니다.

</details>


<a id="w167"></a>
### W167. 본인이 겪은 장애를 포트폴리오에 쓰는 올바른 방식은?

[브라우저에서 풀기](../practice/sw-quiz/index.html?bank=workbook&question=167)

1. 겪지 않은 장애를 해결했다고 꾸민다
2. 다른 사람 수치를 자기 결과로 복사한다
3. 실제 재현·관찰·선택·검증 근거로 작성한다
4. 원인과 해결을 비밀번호 값으로 대신한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 실제 재현·관찰·선택·검증 근거로 작성한다

개인 기록의 내용은 사람마다 다르지만 증거에 근거한 작성 기준은 같습니다.

</details>
