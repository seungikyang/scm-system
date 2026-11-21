// 품목 검색 + 페이징 (TRD 3.5.4, 2.5.4) · 4지 선다
// 브라우저에서 풀기: ../../sw-quiz/index.html?bank=workbook&module=09
// 각 문제에서 1~4번 중 하나를 고릅니다. 정답·해설 보기: practice/answers.md

// 선택형 [W043] Pageable의 계층 전달 흐름은?
//    1. Controller → Service → Repository
//    2. Entity → CSS → Session
//    3. DB → README → 브라우저 쿠키
//    4. Repository → 비밀번호 해시
//    정답·해설: practice/workbook-questions.md#w043
//    정답: 1번 · 페이지·크기·정렬 조건을 필요한 검증을 거쳐 조회 계층으로 전달합니다.

// 선택형 [W044] Page<Item>을 응답 DTO 페이지로 바꾸는 방법은?
//    1. page.toString()만 반환
//    2. page.map(ItemResponse::from)
//    3. 전체 페이지를 첫 품목 하나로 바꿈
//    4. 페이지마다 데이터베이스를 초기화
//    정답·해설: practice/workbook-questions.md#w044
//    정답: 2번 · map은 content를 변환하면서 페이지 메타 정보를 유지합니다.

// 선택형 [W045] 클라이언트 정렬을 허용할 때 필요한 정책은?
//    1. 허용 필드와 기본 정렬을 명시하고 검증한다
//    2. 모든 입력을 SQL 문자열에 그대로 붙인다
//    3. 사용자가 임의의 DB 컬럼을 삭제하게 한다
//    4. 정렬을 허용하면 페이징을 제거한다
//    정답·해설: practice/workbook-questions.md#w045
//    정답: 1번 · 서버 고정과 제한적 사용자 선택 중 요구에 맞게 정하되 안정적인 기준을 둡니다.

// 선택형 [W245] 품목 정렬을 id·itemCode·name·createdAt으로 제한하는 과제에서 허용할 필드는?
//    1. password
//    2. salt
//    3. createdAt
//    4. sessionId
//    정답·해설: practice/workbook-questions.md#w245
//    정답: 3번 · 이 문제는 명시한 정렬 허용 정책을 전제로 합니다. 현재 Controller 기본 정렬은 createdAt 내림차순이며 허용 목록 검증은 별도 구현 과제입니다.

// 선택형 [W273] Specification 검색 조건과 페이징을 함께 전달하는 호출은?
//    1. findAll(passwordEncoder, session)
//    2. deleteAll(pageable)
//    3. findById(cssColor)
//    4. findAll(specification, pageable)
//    정답·해설: practice/workbook-questions.md#w273
//    정답: 4번 · JpaSpecificationExecutor의 조건과 Pageable을 함께 사용해 필터된 페이지를 조회합니다.

// 선택형 [W277] SUPPLIER만 조회하는 거래처 유형 필터에서 CUSTOMER 전용 거래처는?
//    1. 반드시 포함된다
//    2. 자동으로 SUPPLIER가 된다
//    3. 삭제된다
//    4. 제외된다
//    정답·해설: practice/workbook-questions.md#w277
//    정답: 4번 · 조건에 맞는 거래처만 조회하며 조회가 데이터를 변경하지 않습니다.

// 선택형 [W278] ADMIN이 현재 거래처 삭제 API로 비활성화를 성공하면 응답과 상태는?
//    1. 204 / INACTIVE
//    2. 200 / ACTIVE
//    3. 403 / INACTIVE
//    4. 201 / DRAFT
//    정답·해설: practice/workbook-questions.md#w278
//    정답: 1번 · PartnerApiController는 noContent, Service는 비활성화 상태 변경을 수행합니다.

// 선택형 [W279] 정렬 허용 목록을 구현하는 과제에서 금지된 sort 입력의 올바른 처리는?
//    1. SQL에 그대로 연결한다
//    2. 서버 관리자 권한을 부여한다
//    3. 입력 오류로 거절한다
//    4. 모든 데이터를 삭제한다
//    정답·해설: practice/workbook-questions.md#w279
//    정답: 3번 · 이 문제는 허용 목록 검증을 구현하는 설계 과제를 전제로 하며 현재 자동 바인딩 자체가 이를 보장하지는 않습니다.

// 선택형 [W280] 다른 기본·최대 크기 정책이 개입하지 않을 때 size=2 페이지의 content 최대 개수는?
//    1. 1
//    2. 2
//    3. 20
//    4. 전체 행 수
//    정답·해설: practice/workbook-questions.md#w280
//    정답: 2번 · 마지막 페이지는 2보다 작을 수 있지만 요청한 페이지 크기를 넘지 않습니다.

// 선택형 [W281] 품목 수정이 정상 커밋된 뒤 상세 재조회에서 기대할 결과는?
//    1. 항상 이전 값만 보인다
//    2. 수정한 품목이 자동 삭제된다
//    3. 변경된 값이 보인다
//    4. 모든 품목이 같은 값이 된다
//    정답·해설: practice/workbook-questions.md#w281
//    정답: 3번 · 트랜잭션 커밋 뒤 재조회로 저장 결과를 확인합니다.

// 선택형 [W282] A 사용자의 내 발주 목록에 B가 작성한 발주는?
//    1. 포함되지 않는다
//    2. 모두 포함된다
//    3. 자동으로 A의 발주가 된다
//    4. 비밀번호로 정렬된다
//    정답·해설: practice/workbook-questions.md#w282
//    정답: 1번 · 현재 인증 사용자 ID를 작성자 조건으로 사용해야 합니다.

// 선택형 [W328] Transactional(readOnly=true)에 대한 올바른 설명은?
//    1. DB 쓰기를 모든 환경에서 무조건 금지한다
//    2. 인증 없는 조회를 항상 허용한다
//    3. 읽기 의도를 표현하고 일부 최적화를 돕지만 쓰기 차단 보안 경계는 아니다
//    4. 조회 결과가 자동으로 불변 객체가 된다
//    정답·해설: practice/workbook-questions.md#w328
//    정답: 3번 · 읽기 힌트·최적화 효과는 구현에 의존합니다. 수정 메서드는 쓰기 트랜잭션을 사용합니다.

// 선택형 [W329] Page.map과 직접 PageImpl을 만드는 방식의 차이는?
//    1. PageImpl은 JPA 엔티티를 무조건 삭제한다
//    2. map은 기존 메타 정보를 보존하고 직접 구성은 총 개수·Pageable을 정확히 전달해야 한다
//    3. map은 첫 원소만 남긴다
//    4. PageImpl은 HTTP 세션을 요구한다
//    정답·해설: practice/workbook-questions.md#w329
//    정답: 2번 · 수동 구성에서 전체 개수와 페이징 기준을 빠뜨리면 응답 메타 정보가 어긋날 수 있습니다.
