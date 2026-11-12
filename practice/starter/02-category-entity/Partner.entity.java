// Category 엔티티 / Partner 엔티티 (TRD 3.3.2 ~ 3.3.3, 3.5.2 ~ 3.5.3, 3.6.2) · 4지 선다
// 브라우저에서 풀기: ../../sw-quiz/index.html?bank=workbook&module=02
// 각 문제에서 1~4번 중 하나를 고릅니다. 정답·해설 보기: practice/answers.md

// 선택형 [W007] UserRole을 EnumType.STRING으로 저장하는 이유는?
//    1. enum 선언 순서가 바뀌어도 저장된 이름의 의미를 유지하기 위해서다
//    2. 권한 검사가 자동으로 생기기 때문이다
//    3. DB 용량이 항상 ORDINAL보다 작기 때문이다
//    4. 알 수 없는 enum 값도 모두 허용하기 때문이다
//    정답·해설: practice/workbook-questions.md#w007
//    정답: 1번 · STRING은 이름을 저장합니다. enum 이름 변경에는 별도 마이그레이션이 필요합니다.

// 선택형 [W009] 카테고리명에 unique 제약을 두는 목적은?
//    1. 모든 카테고리가 같은 이름을 갖게 하기 위해서다
//    2. 품목 조회를 무조건 한 쿼리로 줄이기 위해서다
//    3. 동일한 이름의 카테고리 중복을 차단하기 위해서다
//    4. 관리자 권한을 부여하기 위해서다
//    정답·해설: practice/workbook-questions.md#w009
//    정답: 3번 · 사용자에게 식별되는 이름의 중복은 Service 검사와 DB 제약으로 막습니다.

// 선택형 [W010] Category→Item 컬렉션을 추가할 때의 올바른 판단은?
//    1. 탐색 편의와 양방향 동기화 비용을 함께 고려한다
//    2. 양방향 관계면 모든 N+1이 자동 사라진다
//    3. 단방향 관계는 JPA에서 금지되어 있다
//    4. mappedBy가 있는 쪽이 항상 외래키를 갱신한다
//    정답·해설: practice/workbook-questions.md#w010
//    정답: 1번 · 양방향 매핑은 탐색을 돕지만 관계 양쪽을 일관되게 관리해야 합니다.

// 선택형 [W011] DB 문자열 컬럼 길이와 입력 검증의 관계는?
//    1. 모든 컬럼을 VARCHAR(1)로 통일한다
//    2. 컬럼 길이가 있으면 NotBlank는 필요 없다
//    3. DB 길이는 비밀번호 해시 알고리즘을 결정한다
//    4. 도메인 최대 길이에 맞춰 DB 컬럼과 DTO 제약을 정한다
//    정답·해설: practice/workbook-questions.md#w011
//    정답: 4번 · DB 저장 한도와 사용자 입력 계약을 함께 맞춰 예측 가능한 검증을 제공합니다.

// 선택형 [W012] 사업자번호 unique 제약의 목적은?
//    1. 서로 다른 사업체의 번호를 같게 만든다
//    2. 같은 사업체가 중복 거래처로 등록되는 것을 막는다
//    3. 공급사만 자동으로 고객사로 바꾼다
//    4. 사업자번호를 세션 ID로 사용한다
//    정답·해설: practice/workbook-questions.md#w012
//    정답: 2번 · 업무 식별자에 대한 최종 중복 방어선입니다.

// 선택형 [W013] 사업자번호 정규화를 설계할 때 올바른 기준은?
//    1. 화면에서만 하이픈을 제거하고 서버는 원문을 비교한다
//    2. 조회마다 서로 다른 규칙으로 번호를 바꾼다
//    3. 정규화하면 DB unique가 불필요해진다
//    4. 중복 비교와 저장 전에 동일한 정규화 규칙을 적용한다
//    정답·해설: practice/workbook-questions.md#w013
//    정답: 4번 · 어느 계층에 두더라도 모든 입력 경로의 비교·저장 표현이 같아야 합니다.

// 선택형 [W014] PartnerType과 canSupply·canBuy 헬퍼의 목적은?
//    1. DB 연결 비밀번호를 생성한다
//    2. 공급·판매 가능 여부를 도메인 규칙으로 모은다
//    3. INACTIVE 거래처도 항상 주문을 허용한다
//    4. 고객사와 공급사의 의미를 제거한다
//    정답·해설: practice/workbook-questions.md#w014
//    정답: 2번 · SUPPLIER·CUSTOMER·BOTH의 업무 의미를 헬퍼에 모으면 조건 중복을 줄일 수 있습니다.

// 선택형 [W015] PartnerStatus의 올바른 값 조합은?
//    1. ACTIVE / INACTIVE
//    2. DRAFT / RECEIVED
//    3. ADMIN / USER
//    4. ASC / DESC
//    정답·해설: practice/workbook-questions.md#w015
//    정답: 1번 · 거래처 활성 여부는 주문 상태나 사용자 역할과 다른 enum입니다.

// 선택형 [W016] 거래처를 비활성화한 뒤 과거 주문은 어떻게 관리하는가?
//    1. 과거 주문과 라인을 모두 자동 삭제한다
//    2. 모든 과거 주문의 작성자를 바꾼다
//    3. 과거 이력은 보존하고 신규 주문 사용을 차단한다
//    4. 비활성화하면 외래키 검사가 사라진다
//    정답·해설: practice/workbook-questions.md#w016
//    정답: 3번 · 상태 변경으로 신규 사용을 제한하면서 거래 이력을 유지합니다.

// 선택형 [W361] SUPPLIER·CUSTOMER·BOTH enum과 두 boolean 역할 플래그를 비교하면?
//    1. 두 플래그는 어떤 조합도 자동으로 항상 유효하다
//    2. enum은 허용 조합을 명시하고 플래그는 조합 유효성 정책을 별도로 정해야 한다
//    3. enum은 DB에 저장할 수 없다
//    4. BOTH를 저장하면 공지 관리자 권한이 생긴다
//    정답·해설: practice/workbook-questions.md#w361
//    정답: 2번 · 현재 업무에서 허용하는 역할 조합과 확장 요구에 맞춰 표현 방식을 선택합니다.

// 선택형 [W374] 현재 참조 구현의 거래처 등록 API에서 partnerType이 null이면 올바른 처리는?
//    1. 항상 BOTH로 바꿔 저장한다
//    2. 사용자 역할을 거래처 유형으로 저장한다
//    3. null을 문자열 NULL로 저장한다
//    4. 필수 입력 검증으로 거부한다
//    정답·해설: practice/workbook-questions.md#w374
//    정답: 4번 · PartnerCreateRequest는 partnerType에 @NotNull을 선언합니다. starter의 기본값 선택 예시는 대안 연습이므로 현재 API 정책과 혼동하지 않습니다.

// 선택형 [W375] 별도 상태를 지정하지 않은 신규 거래처의 현재 초기 상태는?
//    1. ACTIVE
//    2. INACTIVE
//    3. DRAFT
//    4. DISCONTINUED
//    정답·해설: practice/workbook-questions.md#w375
//    정답: 1번 · Partner 생성자는 상태를 지정하지 않았을 때 PartnerStatus.ACTIVE를 사용합니다.

// 선택형 [W376] 재비활성화를 예외로 거부하는 starter 가드에서 검사할 거래처 상태는?
//    1. ACTIVE
//    2. DRAFT
//    3. INACTIVE
//    4. RECEIVED
//    정답·해설: practice/workbook-questions.md#w376
//    정답: 3번 · 해당 starter의 IllegalStateException 가드는 이미 INACTIVE인지 검사하는 연습입니다. 현재 실행되는 Partner.deactivate는 INACTIVE 재대입을 허용하므로 전제를 구분합니다.
