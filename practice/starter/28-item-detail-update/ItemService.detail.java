// 품목 상세 / 수정 / 단종 처리 (PRD 2.5.4) · 4지 선다
// 브라우저에서 풀기: ../../sw-quiz/index.html?bank=workbook&module=28
// 각 문제에서 1~4번 중 하나를 고릅니다. 정답·해설 보기: practice/answers.md

// 선택형 [W120] OSIV=false에서 LAZY 관계를 DTO로 변환할 위치는?
//    1. 트랜잭션 안에서 필요한 관계를 로드해 Service에서 변환한다
//    2. 브라우저가 JPA 프록시를 조회하게 한다
//    3. 트랜잭션이 끝난 뒤 모든 관계를 처음 읽는다
//    4. JSON 응답에 EntityManager를 넣는다
//    정답·해설: practice/workbook-questions.md#w120
//    정답: 1번 · 영속성 컨텍스트가 종료된 뒤의 지연 로딩을 피하도록 조회와 변환을 설계합니다.

// 선택형 [W121] 품목의 카테고리를 바꾸기 전에 할 일은?
//    1. 존재하지 않는 ID도 그대로 저장한다
//    2. 새 카테고리 존재를 확인하고 도메인 변경 메서드를 호출한다
//    3. 모든 과거 라인을 삭제한다
//    4. 카테고리 ID를 서버 포트로 바꾼다
//    정답·해설: practice/workbook-questions.md#w121
//    정답: 2번 · 참조 무결성 확인과 의미 있는 변경 동작을 분리해 수행합니다.

// 선택형 [W122] setter 대신 도메인 메서드를 사용하는 장점은?
//    1. 모든 SQL이 없어지고 DB가 불필요해진다
//    2. 권한 검사가 무조건 자동 생성된다
//    3. 모든 필드를 외부에 무제한 공개한다
//    4. 업무 의미와 불변식 검증을 변경 지점에 모은다
//    정답·해설: practice/workbook-questions.md#w122
//    정답: 4번 · changeCategory·discontinue 같은 메서드는 허용된 상태 변경을 표현합니다.

// 선택형 [W123] 품목 soft delete와 hard delete의 차이는?
//    1. soft delete는 상태로 제한하면서 이력을 보존하고 조회 조건 관리가 필요하다
//    2. soft delete는 DB 행을 항상 물리 삭제한다
//    3. hard delete는 모든 이력을 무조건 보존한다
//    4. 두 방법 모두 신규 주문 검증이 필요 없다
//    정답·해설: practice/workbook-questions.md#w123
//    정답: 1번 · 보존 정책과 데이터 정리·조회 복잡도의 차이를 고려합니다.

// 선택형 [W351] 단종 품목을 다시 ACTIVE로 바꾸는 기능을 추가할 때 필요한 것은?
//    1. 상태만 바꾸면 모든 업무 요구가 자동 충족된다
//    2. 모든 과거 주문을 다시 작성한다
//    3. 재활성화 허용 조건·권한·이력 의미를 명시하고 검증한다
//    4. 현재 API가 제공한다고 검증 없이 가정한다
//    정답·해설: practice/workbook-questions.md#w351
//    정답: 3번 · 재활성화는 정책 선택입니다. 현재 단종 기능과 추가 기능 요구를 구분합니다.

// 선택형 [W392] 단종 재요청을 거부하는 대안 Service에서 품목 단종 오류로 매핑할 현재 ErrorCode는?
//    1. ITEM_DISCONTINUED
//    2. DUPLICATE_ITEM_CODE
//    3. CATEGORY_HAS_ITEMS
//    4. INSUFFICIENT_STOCK
//    정답·해설: practice/workbook-questions.md#w392
//    정답: 1번 · 단종을 오류로 다루는 가정에서는 ITEM_DISCONTINUED가 해당 의미를 나타냅니다. 현재 ItemService의 재단종 처리와 대안 연습의 전제를 구분합니다.
