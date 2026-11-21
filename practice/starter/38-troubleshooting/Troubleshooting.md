# 트러블슈팅 워크북 (TRD 3.16) · 4지 선다

[이 모듈을 브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&module=38) · [전체 문제집](../../workbook-questions.md)

각 문제에서 1~4번 중 하나를 고르고 정답·해설을 확인합니다.

<a id="w039"></a>
### W039. 여러 저장 작업의 트랜잭션 경계는 어디에 두는가?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=39)

1. CSS 선택자에 둔다
2. HTML 버튼 이름에 둔다
3. 업무 작업을 묶는 Service의 프록시 호출 메서드에 둔다
4. 각 SQL 로그 줄에 둔다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 업무 작업을 묶는 Service의 프록시 호출 메서드에 둔다

Service 경계에서 여러 Repository 작업이 함께 성공·실패하도록 묶습니다.

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


<a id="w120"></a>
### W120. OSIV=false에서 LAZY 관계를 DTO로 변환할 위치는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=120)

1. 트랜잭션 안에서 필요한 관계를 로드해 Service에서 변환한다
2. 브라우저가 JPA 프록시를 조회하게 한다
3. 트랜잭션이 끝난 뒤 모든 관계를 처음 읽는다
4. JSON 응답에 EntityManager를 넣는다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 트랜잭션 안에서 필요한 관계를 로드해 Service에서 변환한다

영속성 컨텍스트가 종료된 뒤의 지연 로딩을 피하도록 조회와 변환을 설계합니다.

</details>


<a id="w137"></a>
### W137. ItemResponse.from(item) 정적 팩토리의 역할은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=137)

1. 엔티티에서 공개할 응답 필드를 일관되게 변환한다
2. 새 엔티티를 항상 DB에 저장한다
3. 사용자 비밀번호를 복호화한다
4. 모든 페이지를 삭제한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 엔티티에서 공개할 응답 필드를 일관되게 변환한다

DTO 생성·매핑 책임을 한곳에 모으는 패턴입니다.

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


<a id="w166"></a>
### W166. 트러블슈팅 문서를 작성하는 올바른 순서는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=166)

1. 문제 원인 없이 기술명만 나열한다
2. 실패한 모든 증거를 삭제한다
3. 해결하지 않은 문제를 성공했다고 적는다
4. 재현 가능한 문제·원인·해결·배운 점과 검증을 남긴다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 재현 가능한 문제·원인·해결·배운 점과 검증을 남긴다

독자가 같은 조건에서 확인할 수 있는 근거를 함께 기록합니다.

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


<a id="w261"></a>
### W261. 오류를 계층별로 추적하는 올바른 방법은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=261)

1. 모든 오류를 DB 비밀번호 탓으로 결론 낸다
2. 로그 없이 재설치만 반복한다
3. 예외 응답을 모두 성공으로 바꾼다
4. 요청 매핑·인가·입력·업무·DB·DTO 단계의 실제 실패 지점을 확인한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 요청 매핑·인가·입력·업무·DB·DTO 단계의 실제 실패 지점을 확인한다

마지막으로 성공한 단계와 실패 증거를 연결하면 원인 범위를 줄일 수 있습니다.

</details>


<a id="w262"></a>
### W262. Flyway 검증 실패와 Hibernate validate 실패의 차이는?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=262)

1. 전자는 마이그레이션 이력, 후자는 엔티티와 실제 스키마 일치를 확인한다
2. 둘 다 비밀번호를 해시한다
3. 둘 다 CSRF 토큰 만료를 뜻한다
4. 두 실패는 모두 로그아웃하면 해결된다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 전자는 마이그레이션 이력, 후자는 엔티티와 실제 스키마 일치를 확인한다

checksum·version 문제와 매핑·컬럼·타입 불일치를 서로 다른 검증 경계에서 진단합니다.

</details>


<a id="w263"></a>
### W263. Connection refused와 DB 인증 실패를 구분하는 기준은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=263)

1. 항상 JPA enum 선언 순서를 바꾼다
2. 브라우저 북마크를 삭제한다
3. 리스닝·주소·포트 연결과 사용자·비밀번호·권한 증거를 각각 확인한다
4. Flyway SQL 전체를 무조건 삭제한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 리스닝·주소·포트 연결과 사용자·비밀번호·권한 증거를 각각 확인한다

네트워크 접속 실패와 서버 접속 후 인증 거절은 원인 범위가 다릅니다.

</details>


<a id="w264"></a>
### W264. 영속 데이터가 사라진 듯 보일 때 먼저 확인할 설정은?

[브라우저에서 풀기](../../sw-quiz/index.html?bank=workbook&question=264)

1. HTML의 글꼴만 확인한다
2. 활성 프로필과 실제 JDBC URL이 임시 H2인지 확인한다
3. 문제집 정답 위치만 확인한다
4. README 파일 확장자만 확인한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 활성 프로필과 실제 JDBC URL이 임시 H2인지 확인한다

기본 in-memory H2는 재시작 시 데이터가 초기화됩니다. 영속 MySQL 연결과 구분해야 합니다.

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
