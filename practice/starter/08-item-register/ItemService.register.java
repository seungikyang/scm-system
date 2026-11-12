// 품목 등록 Service (TRD 3.8.2) · 4지 선다
// 브라우저에서 풀기: ../../sw-quiz/index.html?bank=workbook&module=08
// 각 문제에서 1~4번 중 하나를 고릅니다. 정답·해설 보기: practice/answers.md

// 선택형 [W039] 여러 저장 작업의 트랜잭션 경계는 어디에 두는가?
//    1. CSS 선택자에 둔다
//    2. HTML 버튼 이름에 둔다
//    3. 업무 작업을 묶는 Service의 프록시 호출 메서드에 둔다
//    4. 각 SQL 로그 줄에 둔다
//    정답·해설: practice/workbook-questions.md#w039
//    정답: 3번 · Service 경계에서 여러 Repository 작업이 함께 성공·실패하도록 묶습니다.

// 선택형 [W040] 품목코드 중복을 안전하게 차단하는 조합은?
//    1. 화면의 경고 문구만 표시
//    2. Service 사전 검사와 DB unique 제약
//    3. Repository exists 검사만으로 동시성까지 보장
//    4. 등록 이후 이름만 비교
//    정답·해설: practice/workbook-questions.md#w040
//    정답: 2번 · 사전 검사는 친절한 오류를, unique 제약은 경쟁 요청의 최종 방어를 제공합니다.

// 선택형 [W041] 품목 등록 시 카테고리 존재를 확인하는 주된 계층은?
//    1. CSS
//    2. 브라우저 제목
//    3. Service
//    4. Gradle 캐시
//    정답·해설: practice/workbook-questions.md#w041
//    정답: 3번 · 외부 입력의 형식 검증과 실제 참조 데이터 존재 확인은 구분합니다.

// 선택형 [W042] 기본 트랜잭션 정책에서 RuntimeException이 전파되면?
//    1. 이미 저장한 헤더만 항상 남는다
//    2. 모든 checked exception도 예외 없이 자동 롤백된다
//    3. 정적 HTML 파일이 삭제된다
//    4. 해당 트랜잭션 작업이 롤백된다
//    정답·해설: practice/workbook-questions.md#w042
//    정답: 4번 · 기본 롤백은 RuntimeException·Error이며 checked exception 정책은 별도 지정할 수 있습니다.

// 선택형 [W076] 요청 DTO와 응답 DTO를 분리하는 이유는?
//    1. 모든 응답에 비밀번호를 넣기 위해서다
//    2. DTO가 DB 잠금을 대체하기 때문이다
//    3. 입력 허용 필드와 출력 공개 필드의 계약이 다르기 때문이다
//    4. JPA 엔티티를 두 번 저장하기 위해서다
//    정답·해설: practice/workbook-questions.md#w076
//    정답: 3번 · 각 경계에 필요한 데이터와 검증 정책을 독립적으로 관리합니다.

// 선택형 [W163] exists 검사 이후 같은 값이 다른 요청에서 저장되면?
//    1. DB unique 제약이 최종 중복 저장을 차단한다
//    2. exists 결과가 모든 트랜잭션에 영구 고정된다
//    3. 화면 색이 바뀌면 중복이 제거된다
//    4. 뒤 요청은 무조건 다른 값을 자동 생성한다
//    정답·해설: practice/workbook-questions.md#w163
//    정답: 1번 · 사전 조회와 저장 사이의 경쟁 상태는 DB 제약으로 다룹니다.

// 선택형 [W223] Spring 프록시 방식에서 new로 직접 만든 Service의 특징은?
//    1. 자동 트랜잭션 프록시를 거치지 않는다
//    2. 자동으로 ApplicationContext에 등록된다
//    3. 모든 final 필드가 자동 주입된다
//    4. JPA가 HTTP 세션을 생성한다
//    정답·해설: practice/workbook-questions.md#w223
//    정답: 1번 · 컨테이너 관리 객체의 프록시 호출과 직접 생성한 객체의 호출을 구분합니다.

// 선택형 [W224] 기본 프록시 방식의 같은 객체 내부 Transactional 호출은?
//    1. 항상 별도 트랜잭션을 생성한다
//    2. 자동으로 다른 서버에 위임된다
//    3. 프록시를 우회해 내부 메서드의 새 트랜잭션 설정이 적용되지 않을 수 있다
//    4. 모든 checked exception을 자동 제거한다
//    정답·해설: practice/workbook-questions.md#w224
//    정답: 3번 · 외부에서 프록시를 통한 호출 구조와 테스트로 경계를 확인합니다.

// 선택형 [W327] MVC 요청 DTO에서 Valid가 빠지면 생길 수 있는 문제는?
//    1. 해당 입력 경계의 Bean Validation이 실행되지 않아 잘못된 값이 다음 계층으로 갈 수 있다
//    2. DB unique가 자동 삭제된다
//    3. 모든 입력이 무조건 404가 된다
//    4. 모든 Service 검증도 자동으로 제거된다
//    정답·해설: practice/workbook-questions.md#w327
//    정답: 1번 · Valid의 호출 지점과 별도로 Service·도메인 검증이 있는지 확인해야 합니다.

// 선택형 [W382] 이미 등록된 품목코드로 신규 품목을 저장하려 할 때 현재 업무 오류 코드는?
//    1. ITEM_NOT_FOUND
//    2. DUPLICATE_ITEM_CODE
//    3. CATEGORY_HAS_ITEMS
//    4. INSUFFICIENT_STOCK
//    정답·해설: practice/workbook-questions.md#w382
//    정답: 2번 · 품목코드 중복은 DUPLICATE_ITEM_CODE로 처리합니다. 존재 여부 검사와 DB UNIQUE 제약을 함께 사용해 경쟁 저장도 보호합니다.
