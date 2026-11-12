// 통합 테스트 흐름 (TRD 3.12) · 4지 선다
// 브라우저에서 풀기: ../../sw-quiz/index.html?bank=workbook&module=21
// 각 문제에서 1~4번 중 하나를 고릅니다. 정답·해설 보기: practice/answers.md

// 선택형 [W087] 발주 통합 테스트의 정상 흐름은?
//    1. 로그인 → 작성 → 입고 → 요청
//    2. 비로그인 → 완료 → 생성
//    3. 승인 → 반려 → 재승인 → 삭제
//    4. 로그인 → 작성 → 요청 → 승인 → 입고
//    정답·해설: practice/workbook-questions.md#w087
//    정답: 4번 · 이전 응답의 ID와 각 역할의 세션·CSRF 조건을 이어 사용합니다.

// 선택형 [W088] 다음 요청에 필요한 생성 ID는 어떻게 얻는가?
//    1. 응답 JSON을 파싱해 ID 필드를 추출한다
//    2. 현재 시각을 무조건 ID로 사용한다
//    3. DB 전체 데이터를 삭제해 ID를 고정한다
//    4. 비밀번호 해시를 ID로 쓴다
//    정답·해설: practice/workbook-questions.md#w088
//    정답: 1번 · 실제 응답 계약을 따라 ID를 추출해야 테스트가 생성 데이터와 연결됩니다.

// 선택형 [W089] USER가 관리자 발주 API를 호출하는 테스트의 기대는?
//    1. 모든 요청에 200을 기대한다
//    2. 관리자 세션으로 바꾸고 USER 테스트라고 부른다
//    3. 권한 실패 시 발주 상태가 변경되어야 한다
//    4. 인증·CSRF 조건을 갖춘 요청에서도 403을 확인한다
//    정답·해설: practice/workbook-questions.md#w089
//    정답: 4번 · 권한 실패와 CSRF 실패를 구분해 인가 경계를 검증합니다.

// 선택형 [W232] H2와 실제 MySQL의 차이를 검증하는 방법은?
//    1. H2 테스트만으로 모든 MySQL 동작을 확정한다
//    2. MySQL 버전 문자열만 문서에 쓴다
//    3. 실제 MySQL에서 mysqlSchemaTest 등 프로필별 검증을 수행한다
//    4. 테스트 없이 ddl-auto=update로 바꾼다
//    정답·해설: practice/workbook-questions.md#w232
//    정답: 3번 · Flyway 적용과 JPA validate를 실제 MySQL에서 별도로 확인합니다.

// 선택형 [W283] FR-USER-001 로그인: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? Given 등록된 사용자, When 틀린 비밀번호로 로그인, Then HTTP ____ 와 공통 에러 응답을 받는다.
//    1. 200
//    2. 401
//    3. 403
//    4. 400
//    정답·해설: practice/workbook-questions.md#w283
//    정답: 4번 · 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

// 선택형 [W284] FR-USER-002 로그아웃: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? Given 로그인된 세션, When 로그아웃, Then 이후 보호 API 호출은 HTTP ____ 이다.
//    1. 200
//    2. 401
//    3. 400
//    4. 403
//    정답·해설: practice/workbook-questions.md#w284
//    정답: 2번 · 보호 API에 인증 세션이 없으면 AUTHENTICATION_REQUIRED로 401을 응답합니다.

// 선택형 [W285] FR-USER-003 내 정보 조회: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? Given 로그인하지 않은 사용자, When 내 정보 조회, Then HTTP ____.
//    1. 200
//    2. 400
//    3. 403
//    4. 401
//    정답·해설: practice/workbook-questions.md#w285
//    정답: 4번 · 보호 API에 인증 세션이 없으면 AUTHENTICATION_REQUIRED로 401을 응답합니다.

// 선택형 [W286] FR-USER-004 비밀번호 변경: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 현재 비밀번호가 틀리면 HTTP ____.
//    1. 200
//    2. 400
//    3. 401
//    4. 403
//    정답·해설: practice/workbook-questions.md#w286
//    정답: 2번 · 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

// 선택형 [W287] FR-USER-005 권한 구분: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? USER 가 품목 등록 API 를 호출하면 HTTP ____.
//    1. 403
//    2. 200
//    3. 400
//    4. 401
//    정답·해설: practice/workbook-questions.md#w287
//    정답: 1번 · 로그인된 사용자에게 해당 역할·소유권이 없으면 ACCESS_DENIED로 403을 응답합니다.

// 선택형 [W288] 유효한 CONFIRMED 수주·충분한 재고 등 다른 조건이 모두 충족될 때 MANAGER의 출고 API 성공 응답은?
//    1. 400
//    2. 401
//    3. 200
//    4. 403
//    정답·해설: practice/workbook-questions.md#w288
//    정답: 3번 · 유효한 업무 조건을 갖춘 현재 출고 API는 200 OK를 반환합니다.

// 선택형 [W289] FR-PARTNER-001 거래처 등록: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 중복 사업자번호는 HTTP ____ 와 `DUPLICATE_BUSINESS_NUMBER`.
//    1. 200
//    2. 401
//    3. 400
//    4. 403
//    정답·해설: practice/workbook-questions.md#w289
//    정답: 3번 · 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

// 선택형 [W290] FR-PARTNER-001 거래처 등록: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? USER 권한 등록 시도는 HTTP ____.
//    1. 200
//    2. 400
//    3. 401
//    4. 403
//    정답·해설: practice/workbook-questions.md#w290
//    정답: 4번 · 로그인된 사용자에게 해당 역할·소유권이 없으면 ACCESS_DENIED로 403을 응답합니다.

// 선택형 [W291] FR-PARTNER-003 거래처 상세 조회: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 없는 거래처 ID 조회 시 HTTP ____.
//    1. 200
//    2. 404
//    3. 400
//    4. 401
//    정답·해설: practice/workbook-questions.md#w291
//    정답: 2번 · 현재 ErrorCode의 대상 미존재 오류는 404입니다. 접근 권한 등 선행 조건은 충족했다고 가정합니다.

// 선택형 [W292] FR-PARTNER-004 거래처 정보 수정: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 다른 거래처와 같은 사업자번호로 변경하면 HTTP ____.
//    1. 400
//    2. 200
//    3. 401
//    4. 403
//    정답·해설: practice/workbook-questions.md#w292
//    정답: 1번 · 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

// 선택형 [W293] FR-CAT-003 카테고리 상세 조회: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 없는 카테고리 ID 는 HTTP ____.
//    1. 404
//    2. 200
//    3. 400
//    4. 401
//    정답·해설: practice/workbook-questions.md#w293
//    정답: 1번 · 현재 ErrorCode의 대상 미존재 오류는 404입니다. 접근 권한 등 선행 조건은 충족했다고 가정합니다.

// 선택형 [W294] FR-CAT-004 카테고리 정보 수정: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 다른 카테고리가 이미 쓰는 이름으로 변경하면 HTTP ____.
//    1. 200
//    2. 401
//    3. 400
//    4. 403
//    정답·해설: practice/workbook-questions.md#w294
//    정답: 3번 · 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

// 선택형 [W295] FR-CAT-005 카테고리 삭제: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 품목이 있는 카테고리 삭제 시 HTTP ____.
//    1. 200
//    2. 400
//    3. 401
//    4. 403
//    정답·해설: practice/workbook-questions.md#w295
//    정답: 2번 · 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

// 선택형 [W296] FR-ITEM-001 품목 등록: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 중복 품목코드는 HTTP ____ 와 `DUPLICATE_ITEM_CODE`.
//    1. 200
//    2. 401
//    3. 403
//    4. 400
//    정답·해설: practice/workbook-questions.md#w296
//    정답: 4번 · 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

// 선택형 [W297] FR-ITEM-001 품목 등록: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 존재하지 않는 카테고리는 HTTP ____ 와 `CATEGORY_NOT_FOUND`.
//    1. 200
//    2. 404
//    3. 400
//    4. 401
//    정답·해설: practice/workbook-questions.md#w297
//    정답: 2번 · 현재 ErrorCode의 대상 미존재 오류는 404입니다. 접근 권한 등 선행 조건은 충족했다고 가정합니다.

// 선택형 [W298] FR-ITEM-002 품목 목록 조회: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 로그인하지 않은 사용자 조회는 HTTP ____.
//    1. 200
//    2. 400
//    3. 403
//    4. 401
//    정답·해설: practice/workbook-questions.md#w298
//    정답: 4번 · 보호 API에 인증 세션이 없으면 AUTHENTICATION_REQUIRED로 401을 응답합니다.

// 선택형 [W299] FR-ITEM-003 품목 상세 조회: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 없는 품목 ID 조회 시 HTTP ____.
//    1. 404
//    2. 200
//    3. 400
//    4. 401
//    정답·해설: practice/workbook-questions.md#w299
//    정답: 1번 · 현재 ErrorCode의 대상 미존재 오류는 404입니다. 접근 권한 등 선행 조건은 충족했다고 가정합니다.

// 선택형 [W300] FR-ITEM-004 품목 정보 수정: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 존재하지 않는 카테고리로 변경하면 HTTP ____.
//    1. 200
//    2. 400
//    3. 404
//    4. 401
//    정답·해설: practice/workbook-questions.md#w300
//    정답: 3번 · 현재 ErrorCode의 대상 미존재 오류는 404입니다. 접근 권한 등 선행 조건은 충족했다고 가정합니다.

// 선택형 [W301] FR-ITEM-005 품목 단종 처리: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 단종 후 신규 발주 라인에 포함하면 HTTP ____.
//    1. 200
//    2. 401
//    3. 400
//    4. 403
//    정답·해설: practice/workbook-questions.md#w301
//    정답: 3번 · 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

// 선택형 [W302] FR-PO-001 발주서 작성: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 라인이 0개면 HTTP ____.
//    1. 200
//    2. 401
//    3. 403
//    4. 400
//    정답·해설: practice/workbook-questions.md#w302
//    정답: 4번 · 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

// 선택형 [W303] FR-PO-001 발주서 작성: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 단종 품목 라인은 HTTP ____.
//    1. 400
//    2. 200
//    3. 401
//    4. 403
//    정답·해설: practice/workbook-questions.md#w303
//    정답: 1번 · 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

// 선택형 [W304] FR-PO-001 발주서 작성: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? CUSTOMER 전용 거래처는 HTTP ____.
//    1. 200
//    2. 400
//    3. 401
//    4. 403
//    정답·해설: practice/workbook-questions.md#w304
//    정답: 2번 · 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

// 선택형 [W305] FR-PO-002 발주 요청 (submit): 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 이미 REQUESTED 발주를 다시 submit 하면 HTTP ____.
//    1. 200
//    2. 400
//    3. 401
//    4. 403
//    정답·해설: practice/workbook-questions.md#w305
//    정답: 2번 · 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

// 선택형 [W306] FR-PO-004 발주서 상세 조회: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 작성자가 아닌 USER 는 HTTP ____.
//    1. 200
//    2. 400
//    3. 403
//    4. 401
//    정답·해설: practice/workbook-questions.md#w306
//    정답: 3번 · 로그인된 사용자에게 해당 역할·소유권이 없으면 ACCESS_DENIED로 403을 응답합니다.

// 선택형 [W307] FR-PO-005 발주서 취소: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? RECEIVED 발주를 본인이 취소 시도하면 HTTP ____.
//    1. 400
//    2. 200
//    3. 401
//    4. 403
//    정답·해설: practice/workbook-questions.md#w307
//    정답: 1번 · 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

// 선택형 [W308] FR-PO-005 발주서 취소: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 타인의 DRAFT 발주 취소 시도는 HTTP ____.
//    1. 200
//    2. 400
//    3. 401
//    4. 403
//    정답·해설: practice/workbook-questions.md#w308
//    정답: 4번 · 로그인된 사용자에게 해당 역할·소유권이 없으면 ACCESS_DENIED로 403을 응답합니다.

// 선택형 [W309] FR-PO-006 ~ 007 발주 승인/반려: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 이미 승인된 발주 재승인은 HTTP ____.
//    1. 200
//    2. 401
//    3. 403
//    4. 400
//    정답·해설: practice/workbook-questions.md#w309
//    정답: 4번 · 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

// 선택형 [W310] FR-PO-006 ~ 007 발주 승인/반려: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 반려 사유 blank 는 HTTP ____.
//    1. 200
//    2. 401
//    3. 400
//    4. 403
//    정답·해설: practice/workbook-questions.md#w310
//    정답: 3번 · 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

// 선택형 [W311] FR-PO-008 입고 처리: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? APPROVED 가 아닌 발주 입고 시도는 HTTP ____.
//    1. 200
//    2. 400
//    3. 401
//    4. 403
//    정답·해설: practice/workbook-questions.md#w311
//    정답: 2번 · 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

// 선택형 [W312] FR-PO-009 관리자 발주 목록: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? USER 가 `/api/admin/purchase-orders` 호출하면 HTTP ____.
//    1. 403
//    2. 200
//    3. 400
//    4. 401
//    정답·해설: practice/workbook-questions.md#w312
//    정답: 1번 · 로그인된 사용자에게 해당 역할·소유권이 없으면 ACCESS_DENIED로 403을 응답합니다.

// 선택형 [W313] FR-NOTICE-001 공지 등록: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? USER 가 공지 등록하면 HTTP ____.
//    1. 403
//    2. 200
//    3. 400
//    4. 401
//    정답·해설: practice/workbook-questions.md#w313
//    정답: 1번 · 로그인된 사용자에게 해당 역할·소유권이 없으면 ACCESS_DENIED로 403을 응답합니다.

// 선택형 [W314] FR-NOTICE-004 공지 수정: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? USER 수정 시도는 HTTP ____.
//    1. 200
//    2. 400
//    3. 401
//    4. 403
//    정답·해설: practice/workbook-questions.md#w314
//    정답: 4번 · 로그인된 사용자에게 해당 역할·소유권이 없으면 ACCESS_DENIED로 403을 응답합니다.

// 선택형 [W315] FR-NOTICE-004 공지 수정: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 제목 blank 는 HTTP ____.
//    1. 200
//    2. 400
//    3. 401
//    4. 403
//    정답·해설: practice/workbook-questions.md#w315
//    정답: 2번 · 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

// 선택형 [W316] FR-SO-001 수주서 작성: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 라인 0개는 HTTP ____.
//    1. 200
//    2. 401
//    3. 400
//    4. 403
//    정답·해설: practice/workbook-questions.md#w316
//    정답: 3번 · 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

// 선택형 [W317] FR-SO-001 수주서 작성: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? SUPPLIER 전용 거래처는 HTTP ____.
//    1. 200
//    2. 401
//    3. 403
//    4. 400
//    정답·해설: practice/workbook-questions.md#w317
//    정답: 4번 · 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

// 선택형 [W318] 작성자가 아닌 USER가 타인의 수주 상세 API를 조회할 때 응답은?
//    1. 200
//    2. 400
//    3. 403
//    4. 401
//    정답·해설: practice/workbook-questions.md#w318
//    정답: 3번 · 로그인된 사용자에게 해당 역할·소유권이 없으면 ACCESS_DENIED로 403을 응답합니다.

// 선택형 [W319] FR-SO-006 ~ 008 수주 출고/완료/취소: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? CONFIRMED 가 아닌 상태에서 ship 호출은 HTTP ____.
//    1. 400
//    2. 200
//    3. 401
//    4. 403
//    정답·해설: practice/workbook-questions.md#w319
//    정답: 1번 · 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

// 선택형 [W320] FR-SO-006 ~ 008 수주 출고/완료/취소: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 취소 사유 blank 는 HTTP ____.
//    1. 200
//    2. 400
//    3. 401
//    4. 403
//    정답·해설: practice/workbook-questions.md#w320
//    정답: 2번 · 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

// 선택형 [W321] FR-SO-006 ~ 008 수주 출고/완료/취소: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 완료된 수주를 취소하면 HTTP ____.
//    1. 200
//    2. 401
//    3. 400
//    4. 403
//    정답·해설: practice/workbook-questions.md#w321
//    정답: 3번 · 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

// 선택형 [W322] FR-SO-009 수주 상태 조회: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 종료 상태(`COMPLETED`/`CANCELED`)에서 추가 전이를 시도하면 HTTP ____.
//    1. 200
//    2. 400
//    3. 401
//    4. 403
//    정답·해설: practice/workbook-questions.md#w322
//    정답: 2번 · 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

// 선택형 [W338] 기본 Spring 트랜잭션 테스트의 종료 동작에 대한 설명은?
//    1. 모든 서버 요청과 모든 스레드의 변경이 무조건 함께 롤백된다
//    2. 테스트의 DB 변경은 항상 영구 커밋된다
//    3. Transactional이 assertion을 대신한다
//    4. 테스트 트랜잭션은 기본 롤백되지만 다른 스레드·독립 트랜잭션은 별도 범위다
//    정답·해설: practice/workbook-questions.md#w338
//    정답: 4번 · 테스트 격리 범위와 실제 서버·비동기·REQUIRES_NEW 트랜잭션을 구분해야 합니다.

// 선택형 [W339] MockMvc와 WebTestClient를 비교한 올바른 설명은?
//    1. MockMvc는 반드시 외부 MySQL을 요구한다
//    2. WebTestClient는 JSON을 검사할 수 없다
//    3. MockMvc는 MVC 테스트 환경, WebTestClient는 HTTP 클라이언트 방식이며 지원 서버·바인딩 방식이 다르다
//    4. 둘 다 Java 소스 컴파일러다
//    정답·해설: practice/workbook-questions.md#w339
//    정답: 3번 · 현재 프로젝트는 MockMvc 중심이며 테스트 목적과 서버 기동 방식에 맞춰 도구를 선택합니다.

// 선택형 [W340] JsonPath와 ObjectMapper로 응답을 검사하는 방식의 차이는?
//    1. 경로 기반 필드 추출과 구조·타입 기반 역직렬화의 차이다
//    2. 둘 다 비밀번호를 복호화한다
//    3. JsonPath는 모든 데이터를 DB에 저장한다
//    4. ObjectMapper는 HTTP 역할을 자동 검사한다
//    정답·해설: practice/workbook-questions.md#w340
//    정답: 1번 · 간단한 필드 추출과 DTO 전체 계약 검증 중 목적에 맞는 방식을 사용합니다.

// 선택형 [W389] 현재 발주 생성 API가 정상적으로 새 주문을 만들었을 때 HTTP 상태는?
//    1. 200 OK
//    2. 204 No Content
//    3. 201 Created
//    4. 403 Forbidden
//    정답·해설: practice/workbook-questions.md#w389
//    정답: 3번 · 새 발주 생성은 201을 반환하고 생성 ID를 제공해 이후 조회·상태 변경 테스트가 사용할 수 있게 합니다.
