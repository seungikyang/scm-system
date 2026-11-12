// Item 엔티티 (TRD 3.3.4, 3.5.4, 3.6.3) · 4지 선다
// 브라우저에서 풀기: ../../sw-quiz/index.html?bank=workbook&module=03
// 각 문제에서 1~4번 중 하나를 고릅니다. 정답·해설 보기: practice/answers.md

// 선택형 [W017] 여러 Item이 하나의 Category에 속하는 매핑은?
//    1. 모든 Item에 OneToOne을 사용한다
//    2. Item과 Category를 같은 기본키로 강제한다
//    3. Item에서 ManyToOne과 category 외래키를 사용한다
//    4. 카테고리를 HTTP 세션으로만 저장한다
//    정답·해설: practice/workbook-questions.md#w017
//    정답: 3번 · 여러 품목이 같은 카테고리를 참조하는 N:1 관계입니다.

// 선택형 [W018] Category 연관관계를 LAZY로 두는 이유는?
//    1. 필요한 조회에서 관계 로딩 범위를 선택하기 위해서다
//    2. 모든 연관 객체를 항상 즉시 조회하기 위해서다
//    3. 트랜잭션 밖에서도 추가 조회를 항상 보장하기 위해서다
//    4. 외래키 제약을 해제하기 위해서다
//    정답·해설: practice/workbook-questions.md#w018
//    정답: 1번 · LAZY는 불필요한 로딩을 줄이지만 DTO 변환 시 영속성 컨텍스트와 조회 계획이 필요합니다.

// 선택형 [W019] ItemStatus의 올바른 값 조합은?
//    1. SUPPLIER / CUSTOMER
//    2. REQUESTED / REJECTED
//    3. USER / MANAGER
//    4. ACTIVE / DISCONTINUED
//    정답·해설: practice/workbook-questions.md#w019
//    정답: 4번 · 품목은 운영 상태와 단종 상태를 별도로 표현합니다.

// 선택형 [W020] 단종 시 삭제 대신 DISCONTINUED를 사용하는 이유는?
//    1. 신규 주문에 단종 품목을 강제로 추가하기 위해서다
//    2. 과거 주문 라인의 참조와 거래 이력을 보존하기 위해서다
//    3. 재고를 무조건 0으로 만들기 위해서다
//    4. DB 백업 없이 테이블을 초기화하기 위해서다
//    정답·해설: practice/workbook-questions.md#w020
//    정답: 2번 · 단종은 신규 사용 제한이며 과거 데이터를 파괴하는 삭제와 다릅니다.

// 선택형 [W021] 금액을 BigDecimal로 계산하는 이유는?
//    1. double이 모든 십진 금액을 정확히 표현하기 때문이다
//    2. 십진 금액의 정밀도와 반올림 정책을 명시하기 위해서다
//    3. BigDecimal은 비교와 반올림이 전혀 필요 없기 때문이다
//    4. 문자열 연결만으로 금액 합계를 계산하기 위해서다
//    정답·해설: practice/workbook-questions.md#w021
//    정답: 2번 · BigDecimal을 적절한 생성·스케일·반올림 정책과 함께 사용합니다.

// 선택형 [W040] 품목코드 중복을 안전하게 차단하는 조합은?
//    1. 화면의 경고 문구만 표시
//    2. Service 사전 검사와 DB unique 제약
//    3. Repository exists 검사만으로 동시성까지 보장
//    4. 등록 이후 이름만 비교
//    정답·해설: practice/workbook-questions.md#w040
//    정답: 2번 · 사전 검사는 친절한 오류를, unique 제약은 경쟁 요청의 최종 방어를 제공합니다.

// 선택형 [W179] 품목이 반드시 속해야 하는 마스터는?
//    1. HttpSession
//    2. Notice
//    3. PasswordEncoder
//    4. Category
//    정답·해설: practice/workbook-questions.md#w179
//    정답: 4번 · Item은 하나의 카테고리를 참조하며 저장·변경 때 존재 여부를 검증합니다.

// 선택형 [W377] 신규 품목의 현재 초기 상태는?
//    1. DISCONTINUED
//    2. REQUESTED
//    3. CANCELED
//    4. ACTIVE
//    정답·해설: practice/workbook-questions.md#w377
//    정답: 4번 · Item 생성 및 등록은 신규 품목을 ACTIVE로 시작합니다. 주문 상태 enum과 품목 상태 enum을 구분합니다.

// 선택형 [W378] 이미 단종된 품목을 예외로 거부하는 starter 가드가 비교할 상태는?
//    1. DISCONTINUED
//    2. ACTIVE
//    3. INACTIVE
//    4. APPROVED
//    정답·해설: practice/workbook-questions.md#w378
//    정답: 1번 · 해당 starter의 재단종 거부 전제에서는 DISCONTINUED를 비교합니다. 현재 실행되는 Item.discontinue는 상태 재대입을 허용하므로 참조 구현과 대안 가드를 구분합니다.
