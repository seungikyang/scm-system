# 기능 구현 TODO/빈칸 워크북

이 문서는 `scm_system_PRD_TRD.md` 의 모든 기능 요구사항을 구현 단위로 다시 쪼개는 학습용 워크북입니다.

기존 `practice/problems.md` 와 `practice/starter/` 가 코드 조각 중심이라면, 이 문서는 **FR ID → 화면/API → Controller → Service → Repository → Entity/DTO → 테스트** 흐름을 직접 채워보게 합니다.

## 사용 규칙

1. 각 기능의 `개념 빈칸`을 먼저 채운다.
2. 이어서 `구현 TODO`의 계층별 빈칸을 채운다.
3. 마지막으로 `테스트 TODO`를 Given/When/Then 으로 한 줄씩 적는다.
4. 막히면 PRD/TRD 절 번호와 `practice/starter` 파일을 다시 본다.

## 공통 구현 지도

아래 빈칸을 먼저 채우면 모든 기능의 구현 방향이 흔들리지 않습니다.

| 계층 | 책임 | 빈칸 |
|---|---|---|
| Controller | HTTP 요청/응답, 인증 사용자 주입, DTO 검증 | Controller 는 Repository 를 직접 호출하지 않고 ____ 를 호출한다. |
| Service | 비즈니스 규칙, 트랜잭션, 권한/소유자 검증 | 상태 전이는 주로 ____ 계층 또는 도메인 메서드에서 검증한다. |
| Repository | DB 조회/저장, 조건 검색, 페이징 | 복잡한 조건은 메서드 이름이 길어지면 ____ 로 옮긴다. |
| Entity | 식별자, 연관관계, 상태값, 도메인 메서드 | 상태값은 String 대신 ____ 으로 관리한다. |
| DTO | 요청/응답 계약, Validation | Entity 를 그대로 응답하지 않고 ____ DTO 로 변환한다. |
| Exception | 공통 에러 응답 | 예외 응답은 status, code, message, ____ 를 포함한다. |
| Test | 성공/실패/권한/상태 전이 검증 | 승인/반려 기능은 ____ 상태가 아닌 경우를 반드시 테스트한다. |

## 전체 기능 추적표

| FR ID | 기능 | 구현 핵심 | 관련 starter |
|---|---|---|---|
| FR-USER-001 | 로그인 | 이메일 조회, 비밀번호 해시 비교, 세션 생성 | `23-auth-login/*`, `20-security-session` |
| FR-USER-002 | 로그아웃 | 세션 무효화 | `23-auth-login/*` |
| FR-USER-003 | 내 정보 조회 | current user 조회, 응답 DTO | `23-auth-login/*`, `34-current-user-interceptor` |
| FR-USER-004 | 비밀번호 변경 | 현재 비밀번호 검증, 새 비밀번호 해시 | `23-auth-login/*` |
| FR-USER-005 | 권한 구분 | USER/ADMIN/MANAGER, 접근 제어 | `20-security-session`, `34-current-user-interceptor` |
| FR-PARTNER-001 | 거래처 등록 | 사업자번호 unique, 유형 enum | `24-partner-service`, `25-partner-controller` |
| FR-PARTNER-002 | 거래처 목록 조회 | Page 조회, 검색 조건 | `24-partner-service`, `25-partner-controller` |
| FR-PARTNER-003 | 거래처 상세 조회 | 단건 조회 | `24-partner-service`, `25-partner-controller` |
| FR-PARTNER-004 | 거래처 정보 수정 | 도메인 메서드 | `24-partner-service` |
| FR-PARTNER-005 | 거래처 삭제/비활성화 | soft delete, INACTIVE 상태 | `24-partner-service` |
| FR-PARTNER-006 | 거래처 검색 | 이름/사업자번호/유형 조건 | `24-partner-service` |
| FR-PARTNER-007 | 페이징 | Pageable, Page.map | `32-response-dto-mapping` |
| FR-CAT-001 | 카테고리 등록 | 카테고리명 중복 검증 | `02-category-entity`, `24-partner-service` (Category), `25-partner-controller` (Category) |
| FR-CAT-002 | 카테고리 목록 조회 | 전체 또는 페이징 목록 | `24-partner-service`, `25-partner-controller`, `31-repositories-all` |
| FR-CAT-003 | 카테고리 상세 조회 | 소속 품목 포함, N+1 주의 | `24-partner-service`, `25-partner-controller`, `31-repositories-all` |
| FR-CAT-004 | 카테고리 정보 수정 | PUT/PATCH 정책, unique 유지 | `24-partner-service`, `25-partner-controller` |
| FR-CAT-005 | 카테고리 삭제 | 소속 품목 존재 시 삭제 정책 | `24-partner-service`, `25-partner-controller` |
| FR-ITEM-001 | 품목 등록 | 품목코드 unique + 카테고리 FK | `08-item-register`, `17-dto-validation` |
| FR-ITEM-002 | 품목 목록 조회 | Page 조회, DTO 변환 | `09-item-search`, `18-controller-item` |
| FR-ITEM-003 | 품목 상세 조회 | 단건 조회, LAZY 관계 DTO 변환 | `28-item-detail-update` |
| FR-ITEM-004 | 품목 정보 수정 | 카테고리 존재 확인, 도메인 메서드 | `28-item-detail-update` |
| FR-ITEM-005 | 품목 단종 처리 | soft delete, DISCONTINUED 상태 | `28-item-detail-update` |
| FR-ITEM-006 | 품목 검색 | 이름/코드/카테고리 조건 | `07-repository`, `09-item-search` |
| FR-ITEM-007 | 페이징 | Pageable, Page.map | `09-item-search`, `32-response-dto-mapping` |
| FR-PO-001 | 발주서 작성 | 헤더 + 라인 원자적 저장 | `10-purchase-order-create` |
| FR-PO-002 | 발주 요청 | DRAFT → REQUESTED | `10-purchase-order-create`, `19-controller-purchase` |
| FR-PO-003 | 내 발주서 목록 | writerId 조건 | `29-purchase-my-cancel` |
| FR-PO-004 | 발주서 상세 조회 | 작성자/ADMIN 권한 분기 | `29-purchase-my-cancel` |
| FR-PO-005 | 발주서 취소 | 본인 + DRAFT/REQUESTED 상태만 | `29-purchase-my-cancel` |
| FR-PO-006 | 발주 승인 | ADMIN, REQUESTED → APPROVED | `11-purchase-order-approval` |
| FR-PO-007 | 발주 반려 | ADMIN, 반려 사유 필수 | `11-purchase-order-approval` |
| FR-PO-008 | 입고 처리 | APPROVED → RECEIVED | `11-purchase-order-approval` |
| FR-PO-009 | 관리자 발주 목록 | 상태/거래처 조건별 전체 조회 | `19-controller-purchase`, `29-purchase-my-cancel` |
| FR-NOTICE-001 | 공지 등록 | ADMIN 만 작성 | `12-notice-service`, `26-notice-controller` |
| FR-NOTICE-002 | 공지 목록 조회 | 중요 공지 우선 정렬 | `12-notice-service`, `26-notice-controller` |
| FR-NOTICE-003 | 공지 상세 조회 | 조회수 증가 정책 | `12-notice-service`, `26-notice-controller` |
| FR-NOTICE-004 | 공지 수정 | 작성 권한, 필수값 검증 | `12-notice-service`, `26-notice-controller` |
| FR-NOTICE-005 | 공지 삭제 | ADMIN 만 삭제 | `12-notice-service`, `26-notice-controller` |
| FR-NOTICE-006 | 중요 공지 표시 | important DESC 정렬 | `05-notice-entity`, `12-notice-service` |
| FR-SO-001 | 수주서 작성 | 헤더 + 라인 작성 | `13-sales-order-create` |
| FR-SO-002 | 수주 확정 | DRAFT → CONFIRMED | `13-sales-order-create`, `27-sales-controller` |
| FR-SO-003 | 내 수주서 목록 | writerId 조건 | `30-sales-lists` |
| FR-SO-004 | 처리 대기 수주서 | 매니저 처리 대기 | `30-sales-lists` |
| FR-SO-005 | 수주서 상세 조회 | 작성자/매니저/ADMIN 조회 | `30-sales-lists` |
| FR-SO-006 | 수주 출고 | manager 검증, CONFIRMED → SHIPPED | `14-sales-order-decision` |
| FR-SO-007 | 수주 완료 | SHIPPED → COMPLETED | `14-sales-order-decision` |
| FR-SO-008 | 수주 취소 | 사유 필수 | `14-sales-order-decision` |
| FR-SO-009 | 수주 상태 조회 | DRAFT/CONFIRMED/SHIPPED/COMPLETED/CANCELED | `06-sales-order-entity`, `30-sales-lists` |

---

# 1. 회원/인증 기능

## FR-USER-001 로그인

개념 빈칸:

- 인증(Authentication)은 사용자가 ____ 인지 확인하는 과정이다.
- 인가(Authorization)는 사용자가 특정 기능을 ____ 수 있는지 판단하는 과정이다.
- 비밀번호는 평문 비교가 아니라 `passwordEncoder.____(raw, encoded)` 로 검증한다.
- 로그인 실패 메시지를 이메일/비밀번호로 나누지 않는 이유는 ____ 공격을 줄이기 위해서다.

구현 TODO:

- Request DTO: `email`, `password` 에 각각 `@____`, `@____` 를 적용한다.
- Repository: `Optional<User> findBy____(String email)` 을 만든다.
- Service:
  - 이메일을 `trim().toLowerCase()` 로 ____ 한다.
  - 사용자를 찾지 못하면 `ErrorCode.____` 또는 로그인 실패 전용 코드를 던진다.
  - 비밀번호 불일치도 같은 메시지로 응답한다.
  - 성공 시 세션에 `USER_ID`, `USER_ROLE` 을 저장한다.
  - 세션 고정 공격 방지를 위해 `request.____()` 를 호출한다.
- Controller:
  - `POST /api/auth/____`
  - 성공 응답에는 비밀번호를 절대 포함하지 않는다.

테스트 TODO:

- Given 등록된 사용자, When 올바른 비밀번호로 로그인, Then 세션에 ____ 가 저장된다.
- Given 등록된 사용자, When 틀린 비밀번호로 로그인, Then HTTP ____ 와 공통 에러 응답을 받는다.

## FR-USER-002 로그아웃

개념 빈칸:

- 세션 로그아웃은 서버가 보관하던 인증 상태를 ____ 하는 것이다.
- JWT 로그아웃은 서버가 토큰 상태를 보관하지 않으면 즉시 강제 로그아웃이 ____.

구현 TODO:

- Service: `session.____()` 로 기존 세션을 폐기한다.
- Controller: `POST /api/auth/____`
- 응답: 본문 없이 `204 No Content` 또는 메시지 포함 `200 OK` 중 팀 컨벤션을 정한다.

테스트 TODO:

- Given 로그인된 세션, When 로그아웃, Then 이후 보호 API 호출은 HTTP ____ 이다.

## FR-USER-003 내 정보 조회

개념 빈칸:

- 내 정보 조회는 URL 에 userId 를 받기보다 현재 인증 사용자에서 ____ 를 꺼내는 편이 안전하다.
- 응답 DTO 에 password, internal token, salt 같은 값은 ____.

구현 TODO:

- 인증 사용자 주입:
  - 1차 세션 방식: `HttpSession.getAttribute("____")`
  - 개선 방식: `@____ Long currentUserId`
  - Spring Security 방식: `@____`
- Service: `userRepository.findById(currentUserId)` 로 조회한다.
- Response DTO: `userId`, `email`, `name`, `role` 정도만 포함한다.
- Controller: `GET /api/users/____`

테스트 TODO:

- Given 로그인하지 않은 사용자, When 내 정보 조회, Then HTTP ____.
- Given 로그인한 사용자, When 내 정보 조회, Then 응답에 ____ 필드가 없다.

## FR-USER-004 비밀번호 변경

개념 빈칸:

- 비밀번호 변경 전 현재 비밀번호를 다시 확인하는 이유는 ____ 된 세션 피해를 줄이기 위해서다.
- 새 비밀번호는 저장 전 반드시 ____ 해야 한다.

구현 TODO:

- Request DTO:
  - `currentPassword`: `@NotBlank`
  - `newPassword`: `@Size(min = ____, max = ____)`
- Service:
  - 현재 사용자 조회
  - `passwordEncoder.matches(currentPassword, user.getPassword())`
  - 새 비밀번호를 `passwordEncoder.____(...)`
  - `user.changePassword(encodedPassword)`
- Controller: `PATCH /api/users/me/password`

테스트 TODO:

- 현재 비밀번호가 틀리면 HTTP ____.
- 성공 후 기존 raw password 로는 `matches` 가 ____ 이어야 한다.

## FR-USER-005 권한 구분

개념 빈칸:

- USER, ADMIN, MANAGER 는 `UserRole` ____ 으로 정의한다.
- `@Enumerated(EnumType.____)` 를 사용하는 이유는 enum 순서 변경에 안전하기 때문이다.
- ADMIN 권한 검사는 Controller 에서 1차, ____ 에서 2차로 할 수 있다.

구현 TODO:

- `UserRole`: `USER`, `ADMIN`, `MANAGER`
- 세션 방식:
  - 로그인 시 `USER_ROLE` 저장
  - Interceptor 또는 Service 가 `role == UserRole.____` 검사
- Spring Security 방식:
  - `@PreAuthorize("hasRole('____')")`
  - `@PreAuthorize("hasAnyRole('MANAGER','____')")`

테스트 TODO:

- USER 가 품목 등록 API 를 호출하면 HTTP ____.
- MANAGER 가 출고 처리 API 를 호출하면 HTTP ____.

---

# 2. 거래처 관리 기능

## FR-PARTNER-001 거래처 등록

개념 빈칸:

- 사업자번호는 ____ 식별자이므로 DB unique 제약을 둔다.
- 거래처 유형은 ____ enum 으로 관리하면 잘못된 값을 막을 수 있다.

구현 TODO:

- Request DTO: `name`, `businessNumber`, `partnerType`, `contactName`, `phone`, `email`, `address`
- Repository:
  - `partnerRepository.existsBy____(businessNumber)`
- Service:
  - ADMIN 권한 확인
  - 사업자번호 중복 확인
  - `Partner.create(...)`
- Controller: `POST /api/____`
- Response: `partnerId`, `name`, `businessNumber`, `partnerType`, `status`

테스트 TODO:

- 중복 사업자번호는 HTTP ____ 와 `DUPLICATE_BUSINESS_NUMBER`.
- USER 권한 등록 시도는 HTTP ____.

## FR-PARTNER-002 거래처 목록 조회

개념 빈칸:

- 목록은 ____ 를 적용해 페이지 단위로 조회한다.

구현 TODO:

- Controller: `GET /api/partners?page=0&size=20&type=SUPPLIER`
- Service: 조건 분기 (`partnerType` 필터)
- Response DTO 변환

테스트 TODO:

- type=SUPPLIER 조회 시 CUSTOMER 거래처는 ____.

## FR-PARTNER-003 거래처 상세 조회

개념 빈칸:

- 상세 조회는 PathVariable 로 ____ 를 받는다.
- 거래처 상세 응답에 발주/수주 통계까지 포함할지는 ____ 와 응답 크기의 트레이드오프다.

구현 TODO:

- Repository: `findById(partnerId)`
- Service:
  - 거래처 없으면 `ErrorCode.____`
  - Service 안에서 DTO 변환
- Controller: `GET /api/partners/{____}`

테스트 TODO:

- 없는 거래처 ID 조회 시 HTTP ____.
- 응답에 사업자번호와 유형이 포함된다.

## FR-PARTNER-004 거래처 정보 수정

개념 빈칸:

- 수정 시 Service 가 setter 를 직접 호출하기보다 Entity 의 ____ 메서드를 쓰면 규칙이 모인다.
- 사업자번호 변경 요청이 들어왔다면 ____ 검증을 다시 해야 한다.

구현 TODO:

- Request DTO: `name`, `businessNumber`, `partnerType`, `contactName`, `phone`, `email`, `address`
- Service:
  - 거래처 조회
  - 사업자번호가 바뀌었다면 중복 재검사
  - `partner.update(...)`
- Controller: `PUT /api/partners/{partnerId}`

테스트 TODO:

- 다른 거래처와 같은 사업자번호로 변경하면 HTTP ____.
- 유형(SUPPLIER → BOTH) 변경 후 발주 가능한지 확인.

## FR-PARTNER-005 거래처 삭제 / 비활성화

개념 빈칸:

- 발주/수주 이력이 있는 거래처를 hard delete 하면 ____ 가 깨질 수 있다.
- 그래서 보통은 ____ 처리(soft delete)로 둔다.

구현 TODO:

- Repository: `purchaseOrderRepository.countByPartner_Id(partnerId)`, `salesOrderRepository.countByPartner_Id(partnerId)`
- Service:
  - 이력이 0 이면 delete
  - 이력이 있으면 `partner.deactivate()` 로 상태 변경
- Controller: `DELETE /api/partners/{partnerId}` 또는 `PATCH /api/partners/{partnerId}/deactivate`

테스트 TODO:

- 발주 이력이 있는 거래처 삭제 시 응답 상태는 ____, 거래처 status 는 ____ 가 된다.
- 이력이 없는 거래처는 실제 DELETE 된다.

## FR-PARTNER-006 거래처 검색

개념 빈칸:

- 검색 조건이 이름/사업자번호/유형으로 늘어나면 단순 메서드 이름 쿼리가 ____ 질 수 있다.

구현 TODO:

- Controller: `GET /api/partners?keyword=best&type=SUPPLIER`
- Repository:
  - 단순 버전: `findByNameContainingOrBusinessNumberContaining(...)`
  - 확장 버전: `@Query` 또는 Specification/Querydsl
- Service:
  - keyword blank 면 전체
  - keyword trim

테스트 TODO:

- 이름 일부로 검색하면 해당 거래처만 나온다.

---

# 3. 카테고리 / 품목 관리 기능

## FR-PARTNER-007 페이징

개념 빈칸:

- Spring Data 의 `Page` 는 content 뿐 아니라 totalElements, totalPages 같은 ____ 정보를 가진다.
- `Page.map` 을 쓰면 페이징 메타 정보가 ____ 된다.

구현 TODO:

- Controller 파라미터: `Pageable pageable`
- 기본값: `@PageableDefault(size = ____, sort = "id")`
- 정렬 허용 컬럼 정책: ____

테스트 TODO:

- `page=1, size=10` 요청 시 두 번째 페이지가 조회된다.
- 허용하지 않는 sort 컬럼은 ____.

---

## FR-CAT-001 카테고리 등록

개념 빈칸:

- 카테고리명은 사용자 식별성이 높으므로 DB 에 ____ 제약을 둔다.

구현 TODO:

- Request DTO: `name`, `description`
- Service:
  - ADMIN 권한 확인
  - `categoryRepository.existsBy____(name)`
  - `Category.create(name, description)`
- Controller: `POST /api/____`

테스트 TODO:

- 중복 카테고리명은 `DUPLICATE_CATEGORY_NAME` 으로 응답한다.

## FR-CAT-002 카테고리 목록 조회

개념 빈칸:

- 카테고리는 품목보다 수가 적어 MVP 에서는 ____ 조회로 충분하다.
- 그러나 카테고리가 100개를 넘어가면 ____ 적용을 고려한다.

구현 TODO:

- Controller: `GET /api/categories`
- Service: `categoryRepository.findAll(Sort.by("name").____())`
- Response: `categoryId`, `name`, `description`, `itemCount`(선택)

테스트 TODO:

- 로그인 사용자는 USER/ADMIN/MANAGER 모두 조회 가능하다.

## FR-CAT-003 카테고리 상세 조회

개념 빈칸:

- 카테고리 상세에 소속 품목까지 포함하면 N+1 문제를 피하기 위해 ____ 또는 EntityGraph 를 사용할 수 있다.
- 또는 소속 품목을 ____ API 로 분리해 페이징을 적용한다.

구현 TODO:

- Repository:
  - `categoryRepository.findById(categoryId)`
  - 소속 품목: `itemRepository.findByCategory_Id(categoryId, pageable)`
- Response: 카테고리 정보 + 소속 품목 요약 DTO
- Controller: `GET /api/categories/{categoryId}`

테스트 TODO:

- 없는 카테고리 ID 는 HTTP ____.
- 상세 응답에 item summary 목록이 포함된다.

## FR-CAT-004 카테고리 정보 수정

개념 빈칸:

- 전체 갱신은 ____, 부분 갱신은 ____ HTTP 메서드가 자연스럽다.
- 이름 변경 시에도 중복 검증은 ____.

구현 TODO:

- Controller: `PUT /api/categories/{categoryId}` 또는 `PATCH /api/categories/{categoryId}`
- Service:
  - ADMIN 권한 확인
  - 카테고리 조회
  - 새 이름이 기존과 다르면 중복 확인
  - `category.update(name, description)`

테스트 TODO:

- 다른 카테고리가 이미 쓰는 이름으로 변경하면 HTTP ____.

## FR-CAT-005 카테고리 삭제

개념 빈칸:

- 소속 품목이 있는 카테고리를 삭제하면 품목의 FK 가 ____ 될 수 있다.

구현 TODO:

- Repository: `itemRepository.countBy____(categoryId)`
- Service: count > 0 이면 `CATEGORY_HAS_ITEMS`

테스트 TODO:

- 품목이 있는 카테고리 삭제 시 HTTP ____.

## FR-ITEM-001 품목 등록

개념 빈칸:

- 품목코드는 ____ 식별자이므로 DB unique 와 Service 중복 검사를 함께 둔다.
- 품목은 반드시 하나의 ____ 에 속해야 한다.
- 단가는 `____` 타입으로 저장해 부동소수점 오차를 막는다.

구현 TODO:

- Request DTO: `itemCode`, `name`, `categoryId`, `unit`, `unitPrice`, `safetyStock`
- Repository:
  - `itemRepository.existsBy____(itemCode)`
  - `categoryRepository.findById(categoryId)`
- Service:
  - ADMIN 권한 확인
  - 품목코드 중복 확인
  - 카테고리 존재 확인
  - `Item.create(category, itemCode, name, unit, unitPrice, safetyStock)`
- Controller: `POST /api/____`
- Response: `itemId`, `itemCode`, `name`, `categoryName`, `unitPrice`, `status`

테스트 TODO:

- 중복 품목코드는 HTTP ____ 와 `DUPLICATE_ITEM_CODE`.
- 존재하지 않는 카테고리는 HTTP ____ 와 `CATEGORY_NOT_FOUND`.

## FR-ITEM-002 품목 목록 조회

개념 빈칸:

- 목록 조회는 데이터가 많을 수 있으므로 ____ 를 적용한다.
- Entity 목록을 그대로 응답하지 않고 `Page<ItemResponse>` 로 ____ 한다.

구현 TODO:

- Controller: `GET /api/items?page=0&size=20&sort=itemCode,asc`
- Service: `itemRepository.findAll(pageable)` → `page.____(ItemResponse::from)`
- Response DTO 에는 BigDecimal unitPrice 가 포함된다 (JSON 직렬화 시 문자열로 가야 안전한지 검토).

테스트 TODO:

- size=2 로 조회하면 응답 content 크기는 최대 ____ 이다.
- 로그인하지 않은 사용자 조회는 HTTP ____.

## FR-ITEM-003 품목 상세 조회

개념 빈칸:

- 상세 조회는 PathVariable 로 ____ 를 받는다.
- LAZY 로 묶인 Category 는 DTO 변환 시점에 접근되므로 트랜잭션이 살아 있어야 한다. 또는 ____ 으로 함께 로드한다.

구현 TODO:

- Repository: `findById(itemId)` 또는 상세 전용 fetch join.
- Service:
  - 품목 없으면 `ErrorCode.____`
  - Service 안에서 DTO 변환
- Controller: `GET /api/items/{____}`

테스트 TODO:

- 없는 품목 ID 조회 시 HTTP ____.
- 응답에 카테고리명과 단가가 포함된다.

## FR-ITEM-004 품목 정보 수정

개념 빈칸:

- 수정 시 Service 가 setter 를 직접 부르기보다 Entity 의 ____ 메서드를 쓴다.
- 카테고리 변경은 새 categoryId 가 실제 존재하는지 먼저 ____ 한다.

구현 TODO:

- Request DTO: `name`, `categoryId`, `unit`, `unitPrice`, `safetyStock`
- Service:
  - 품목 조회
  - categoryId 가 있으면 카테고리 조회 + `item.changeCategory(category)`
  - `item.updateProfile(...)`
- Controller: `PUT /api/items/{itemId}`

테스트 TODO:

- 존재하지 않는 카테고리로 변경하면 HTTP ____.
- 수정 후 상세 조회에서 변경값이 ____ 된다.

## FR-ITEM-005 품목 단종 처리

개념 빈칸:

- 단종은 hard delete 대신 ____ 로 처리해 과거 발주/수주 라인을 보존한다.

구현 TODO:

- Entity: `ItemStatus.DISCONTINUED`
- 도메인 메서드: `item.____()`
- Controller: `PATCH /api/items/{itemId}/discontinue`

테스트 TODO:

- 단종 후 신규 발주 라인에 포함하면 HTTP ____.

---

## FR-ITEM-006 품목 검색

개념 빈칸:

- 검색 조건이 이름/코드/카테고리로 늘어나면 단순 메서드 이름 쿼리가 ____ 질 수 있다.
- 검색 결과도 목록이므로 ____ 를 유지한다.

구현 TODO:

- Controller: `GET /api/items?keyword=usb&categoryId=3`
- Repository:
  - 단순 버전: `findByNameContainingOrItemCodeContaining(...)`
  - 확장 버전: `@Query` 또는 Specification/Querydsl
- Service:
  - keyword 가 blank 면 전체 조회
  - keyword 를 trim 한다.

테스트 TODO:

- 이름 일부로 검색하면 해당 품목만 나온다.
- 카테고리 조건과 keyword 조건을 함께 주면 두 조건을 ____ 한다.

## FR-ITEM-007 페이징

개념 빈칸:

- Spring Data 의 `Page` 는 content 뿐 아니라 totalElements, totalPages 같은 ____ 정보를 가진다.
- `Page.map` 을 쓰면 페이징 메타 정보가 ____ 된다.

구현 TODO:

- Controller 파라미터: `Pageable pageable`
- 기본값: `@PageableDefault(size = ____, sort = "id")`
- 정렬 허용 컬럼을 제한하는 정책을 적는다: ____

테스트 TODO:

- `page=1, size=10` 요청 시 두 번째 페이지가 조회된다.
- 허용하지 않는 sort 컬럼은 ____.

---

# 4. 발주(구매) 기능

## FR-PO-001 발주서 작성

개념 빈칸:

- 발주서는 ____ + ____ 의 헤더-라인 구조다.
- 새 발주의 초기 상태는 `PurchaseOrderStatus.____`.
- 헤더와 라인은 ____ 트랜잭션으로 묶어야 한다.
- 거래처 유형이 SUPPLIER 또는 ____ 인지 검증한다.

구현 TODO:

- Request DTO: `partnerId`, `orderDate`, `dueDate`, `lines: List<LineDto>`
  - LineDto: `itemId`, `quantity`, `unitPrice`
- Service:
  - 현재 사용자 조회 (writer)
  - 거래처 조회 + 유형 검증
  - 라인 검증:
    - 라인 ≥ 1 (`EMPTY_ORDER_LINES`)
    - 각 라인 품목 존재 + 상태 ACTIVE
    - 수량 > 0
  - 라인별 `lineAmount = quantity × unitPrice`
  - 헤더 `totalAmount = sum(lineAmount)`
  - 발주번호 채번
  - `PurchaseOrder.create(partner, writer, orderDate, dueDate)`
  - 라인 추가 `po.addLine(item, quantity, unitPrice)`
  - 헤더 + 라인 저장 (cascade)
- Controller: `POST /api/purchase-orders`

테스트 TODO:

- 라인이 0개면 HTTP ____.
- 단종 품목 라인은 HTTP ____.
- CUSTOMER 전용 거래처는 HTTP ____.
- 작성 성공 시 status 는 ____.

## FR-PO-002 발주 요청 (submit)

개념 빈칸:

- DRAFT 와 REQUESTED 를 분리하면 ____ 저장 기능을 자연스럽게 표현할 수 있다.
- 발주 요청은 `DRAFT → ____` 전이다.

구현 TODO:

- Service:
  - 작성자 본인 확인
  - 현재 상태가 DRAFT 인지 확인
  - 라인 비어있지 않은지 재확인
  - `po.submit()`
- Controller: `PATCH /api/purchase-orders/{poId}/submit`

테스트 TODO:

- DRAFT 발주는 submit 성공.
- 이미 REQUESTED 발주를 다시 submit 하면 HTTP ____.

## FR-PO-003 내 발주서 목록

개념 빈칸:

- IDOR 공격을 막으려면 URL 의 writerId 보다 인증 사용자 기반 ____ 조건을 쓴다.

구현 TODO:

- Repository: `findByWriterId(currentUserId, pageable)`
- Service: current user 의 발주만 조회
- Controller: `GET /api/purchase-orders/____`

테스트 TODO:

- A 사용자가 B 사용자의 발주를 목록에서 볼 수 ____.

## FR-PO-004 발주서 상세 조회

개념 빈칸:

- 상세 조회는 본인 또는 ADMIN 만 가능하도록 ____ 검증을 한다.
- 응답에 헤더와 라인을 함께 담을 때 nested DTO 구조를 쓰면 ____ 한 번에 끝낼 수 있다.

구현 TODO:

- Repository: `findById(poId)` 또는 라인까지 fetch join.
- Service:
  - 발주 없으면 `PURCHASE_ORDER_NOT_FOUND`
  - 본인 여부(`writerId == currentUserId`) 또는 ADMIN 여부 확인
- Controller: `GET /api/purchase-orders/{poId}`

테스트 TODO:

- 작성자가 아닌 USER 는 HTTP ____.
- ADMIN 은 다른 사용자의 발주도 조회 가능하다.
- 응답에 lines 배열이 헤더와 함께 포함된다.

## FR-PO-005 발주서 취소

개념 빈칸:

- 취소는 본인의 ____, ____ 상태 발주만 허용한다.
- 취소를 별도 상태로 둘 경우 enum 에 ____ 를 추가한다.

구현 TODO:

- Entity: `po.cancelByOwner(currentUserId)`
- Service:
  - 본인 여부 확인 (도메인 메서드 내부에서)
  - 상태 검증 (DRAFT/REQUESTED)
- Controller: `PATCH /api/purchase-orders/{poId}/cancel`

테스트 TODO:

- APPROVED 발주를 본인이 취소 시도하면 HTTP ____.
- 타인의 DRAFT 발주 취소 시도는 HTTP ____.

## FR-PO-006 ~ 007 발주 승인/반려

개념 빈칸:

- 승인/반려는 상태 전이이므로 같은 요청을 두 번 보내도 데이터가 이상해지지 않도록 ____ 을 검증한다.
- 동시에 두 관리자가 승인하면 ____ 락 또는 ____ 락을 고려한다.
- 반려 사유는 빈 문자열이 아니어야 하므로 `@____` 를 적용한다.

구현 TODO:

- Service:
  - ADMIN 권한 확인
  - 발주 조회
  - `po.approve(approverUserId)` 또는 `po.reject(approverUserId, reason)`
- Controller:
  - `PATCH /api/admin/purchase-orders/{poId}/approve`
  - `PATCH /api/admin/purchase-orders/{poId}/reject`

테스트 TODO:

- REQUESTED 발주 승인 성공.
- 이미 승인된 발주 재승인은 HTTP ____.
- 반려 사유 blank 는 HTTP ____.

## FR-PO-008 입고 처리

개념 빈칸:

- 입고는 `APPROVED → ____` 전이다.
- 입고 시 향후 ____ 반영(증가)이 자연스럽다.

구현 TODO:

- Service:
  - ADMIN 권한 확인
  - 발주 조회
  - 상태 APPROVED 검증
  - `po.receive()`, `receivedAt = now()`
  - (확장) 라인별 재고 +quantity
- Controller: `PATCH /api/admin/purchase-orders/{poId}/receive`

테스트 TODO:

- APPROVED 가 아닌 발주 입고 시도는 HTTP ____.

## FR-PO-009 관리자 발주 목록

개념 빈칸:

- 관리자 발주 목록은 개인의 `/my` 와 달리 전체 발주를 ____ 조건으로 조회한다.

구현 TODO:

- Controller: `GET /api/admin/purchase-orders`
- Query param: `status`, `partnerId`, `from`, `to`
- 권한: `@PreAuthorize("hasRole('____')")`
- Service:
  - status + partnerId 조합 분기
  - 조건이 없으면 `findAll(pageable)`

테스트 TODO:

- USER 가 `/api/admin/purchase-orders` 호출하면 HTTP ____.

---

# 5. 공지사항 기능

## FR-NOTICE-001 공지 등록

개념 빈칸:

- 공지 등록은 ____ 권한만 가능하다.
- 제목과 내용은 `@____` 로 필수 검증한다.

구현 TODO:

- Request DTO: `title`, `content`, `important`
- Service:
  - ADMIN 권한 확인
  - writerId 저장
  - `viewCount = ____`
- Controller: `POST /api/notices`

테스트 TODO:

- USER 가 공지 등록하면 HTTP ____.

## FR-NOTICE-002 공지 목록 조회

개념 빈칸:

- 공지 목록은 로그인 사용자 모두가 볼 수 있지만, 등록/수정/삭제는 ____ 만 가능하다.
- 중요 공지 우선 정렬은 `important ____ , createdAt ____`.

구현 TODO:

- Repository: `findAll(pageable)` with Sort
- Service: 기본 정렬을 important desc + createdAt desc 로 구성
- Controller: `GET /api/notices`
- Response: 목록 DTO 는 긴 `content` 대신 ____ 를 둘 수 있다.

테스트 TODO:

- important=true 공지가 일반 공지보다 먼저 나온다.

## FR-NOTICE-003 공지 상세 조회

개념 빈칸:

- GET 상세 조회에서 조회수를 증가시키면 검색 봇/미리보기 요청으로 조회수가 ____ 수 있다.
- 원자적 조회수 증가는 `UPDATE notice SET view_count = view_count + ____` 형태가 안전하다.

구현 TODO:

- Service:
  - 공지 조회
  - 조회수 증가 정책 선택: 상세 조회 안에서 증가 또는 별도 `PATCH /view`
  - 상세 DTO 반환
- Repository:
  - `@Modifying @Query` 로 조회수 증가
- Controller: `GET /api/notices/{noticeId}`

테스트 TODO:

- 상세 조회 후 viewCount 가 1 증가한다.

## FR-NOTICE-004 공지 수정

개념 빈칸:

- 수정 권한은 작성자 여부보다 PRD 기준 ____ 권한이 핵심이다.

구현 TODO:

- Request DTO: `title`, `content`, `important`
- Service:
  - ADMIN 권한 확인
  - 공지 조회
  - `notice.update(...)`
- Controller: `PUT /api/notices/{noticeId}`

테스트 TODO:

- USER 수정 시도는 HTTP ____.
- 제목 blank 는 HTTP ____.

## FR-NOTICE-005 공지 삭제

개념 빈칸:

- 공지 삭제는 실제 삭제 또는 `deleted` 플래그를 두는 ____ delete 중 선택할 수 있다.

구현 TODO:

- Service:
  - ADMIN 권한 확인
  - 공지 조회
  - delete 또는 soft delete
- Controller: `DELETE /api/notices/{noticeId}`

테스트 TODO:

- 삭제 후 목록에 노출되지 않는다.

## FR-NOTICE-006 중요 공지 표시

개념 빈칸:

- `important` 는 Boolean 이지만 정렬에서는 true 를 먼저 두기 위해 ____ 정렬을 사용한다.

구현 TODO:

- Entity: `Boolean important`
- Repository/Service sort:
  - `Sort.by(Sort.Direction.____, "important").and(Sort.by(Sort.Direction.____, "createdAt"))`
- 화면: 중요 공지는 badge 또는 상단 고정 영역으로 표시할 수 있다.

테스트 TODO:

- important=false 최신글보다 important=true 이전글이 먼저 나오는지 확인한다.

---

# 6. 수주(판매) 기능

## FR-SO-001 수주서 작성

개념 빈칸:

- 수주서도 발주서와 같은 ____-____ 구조다.
- 거래처 유형이 CUSTOMER 또는 ____ 인지 검증한다.
- 새 수주의 초기 상태는 `SalesOrderStatus.____`.

구현 TODO:

- Request DTO: `partnerId`, `orderDate`, `shipDate`, `lines: List<LineDto>`
- Service:
  - 현재 사용자 조회
  - 거래처 조회 + 유형 검증
  - 라인 검증 (≥ 1, 품목 존재, 수량 > 0)
  - 총금액 계산
  - 수주번호 채번
  - `SalesOrder.create(...)`
- Controller: `POST /api/sales-orders`

테스트 TODO:

- 라인 0개는 HTTP ____.
- SUPPLIER 전용 거래처는 HTTP ____.

## FR-SO-002 수주 확정

개념 빈칸:

- 확정은 `DRAFT → ____` 전이다.

구현 TODO:

- Service:
  - 작성자 본인 확인
  - 상태 DRAFT 검증
  - 라인 재확인
  - `so.confirm()`
- Controller: `PATCH /api/sales-orders/{soId}/confirm`

## FR-SO-003 내 수주서 목록

개념 빈칸:

- "내 수주 목록"은 내가 ____ 한 수주서 목록이다.
- 처리자로서의 목록은 별도 `pending` API 로 분리한다.

구현 TODO:

- Repository: `findByWriterId(currentUserId, pageable)`
- Service: current user 를 writerId 로 사용
- Controller: `GET /api/sales-orders/____`

테스트 TODO:

- A 작성 수주서 목록에 B 작성 수주서는 보이지 않는다.

## FR-SO-005 수주서 상세 조회

개념 빈칸:

- 상세 조회는 작성자, 처리자(매니저) 또는 ____ 만 허용한다.
- ADMIN 전체 조회 허용은 Service 에서 role 기반으로 ____ 해야 한다.

구현 TODO:

- Service:
  - 수주서 조회
  - currentUserId 가 writerId 또는 managerId 인지 확인
  - currentRole 이 ADMIN 이면 전체 조회 허용
  - 아니면 `ErrorCode.____`
- Controller: `GET /api/sales-orders/{soId}`

테스트 TODO:

- 작성자도 처리자도 아니고 ADMIN 도 아닌 사용자 접근은 HTTP ____.
- 상세 응답에 lines 배열이 포함된다.

## FR-SO-006 ~ 008 수주 출고/완료/취소

개념 빈칸:

- 상태 전이:
  - `CONFIRMED → ____` (출고)
  - `____ → COMPLETED` (완료)
  - `DRAFT/CONFIRMED/SHIPPED → ____` (취소)
- 매니저 권한은 ____ 또는 ADMIN.
- 취소 사유는 ____.

구현 TODO:

- Service:
  - 권한 확인
  - 상태 검증
  - 도메인 메서드 호출: `so.ship()`, `so.complete()`, `so.cancel(reason)`
- Controller:
  - `PATCH /api/sales-orders/{soId}/ship`
  - `PATCH /api/sales-orders/{soId}/complete`
  - `PATCH /api/sales-orders/{soId}/cancel`

테스트 TODO:

- CONFIRMED 가 아닌 상태에서 ship 호출은 HTTP ____.
- 취소 사유 blank 는 HTTP ____.
- 완료된 수주를 취소하면 HTTP ____.

## FR-SO-009 수주 상태 조회

개념 빈칸:

- 상태값은 `DRAFT`, `CONFIRMED`, `SHIPPED`, `COMPLETED`, `CANCELED` 를 ____ 으로 관리한다.
- 목록에서 status 조건을 받으면 통계/관리 화면에서 ____ 필터가 가능하다.

구현 TODO:

- Query param: `status=CONFIRMED`
- Repository: `findByStatus(status, pageable)` 또는 관리자 조건별 메서드
- Response: 상태 enum 문자열 + 상태 변경 일시(`confirmedAt`, `shippedAt`, `completedAt`) 포함

테스트 TODO:

- status=CONFIRMED 요청 시 출고된 수주는 제외된다.
- 종료 상태(`COMPLETED`/`CANCELED`)에서 추가 전이를 시도하면 HTTP ____.

## FR-SO-004 처리 대기 수주서

개념 빈칸:

- "처리 대기"는 보통 `____` 상태의 수주서를 의미한다.

구현 TODO:

- Repository: `findByStatus(SalesOrderStatus.CONFIRMED, pageable)`
- 권한: `@PreAuthorize("hasAnyRole('MANAGER','ADMIN')")`
- Controller: `GET /api/sales-orders/pending`

---

# 7. 기능별 공통 테스트 체크리스트

각 기능을 구현할 때 아래 체크리스트를 최소 1개 이상 채우세요.

| 분류 | 질문 | 내 답 |
|---|---|---|
| 성공 | 정상 요청의 HTTP status 는? | ____ |
| 검증 | 필수값 누락 시 어떤 ErrorCode 인가? | ____ |
| 권한 | USER/ADMIN/MANAGER 중 누가 가능한가? | ____ |
| 소유자 | 본인 데이터만 봐야 하는가? | ____ |
| 상태 | 허용되는 상태 전이는? | ____ |
| 트랜잭션 | 실패 시 함께 rollback 되어야 하는 데이터는? | ____ |
| DTO | 응답에서 숨겨야 할 필드는? | ____ |
| 동시성 | 중복 승인/중복 등록 같은 race 가 있는가? | ____ |
| 도메인 규칙 | 거래처 유형/품목 상태 검증을 어디서 하는가? | ____ |
| 헤더-라인 | 라인 검증을 헤더 저장 전/후 중 어디서 하는가? | ____ |

## 마무리 자가 점검

- 이 기능은 PRD 의 어떤 FR ID 를 만족하는가? ____
- 이 기능의 핵심 비즈니스 규칙은 무엇인가? ____
- 이 기능에서 Controller 가 하면 안 되는 일은 무엇인가? ____
- 이 기능에서 Service 가 반드시 검증해야 하는 것은 무엇인가? ____
- 이 기능의 실패 케이스 테스트 2개는 무엇인가? ____
