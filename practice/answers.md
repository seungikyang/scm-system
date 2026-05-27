# 정답 방향 (Answers)

빈칸의 키워드 자체보다 **왜 그렇게 채워야 하는가**를 설명합니다. PRD/TRD 의 해당 절을 다시 펴 보며 한 줄로 자기 답을 만들어 보세요.

---

## 0. 빌드와 설정

- `spring-boot-starter-web` 은 Tomcat + Spring MVC + Jackson 을 묶어 REST API 와 Thymeleaf 렌더링을 가능하게 한다.
- `spring-boot-starter-data-jpa` 는 EntityManager 와 Hibernate, Repository 인터페이스 자동 구현을 제공한다.
- `spring-boot-starter-validation` 은 `@Valid` 와 Hibernate Validator(jakarta.validation) 를 활성화한다.
- `h2` 는 인메모리/파일 DB 로, 로컬과 테스트에서 별도 설치 없이 빠르게 띄울 수 있다.
- `spring-boot-starter-thymeleaf` 는 서버 렌더링용. 화면 요구사항(2.7) 을 위해 사용한다.
- `ddl-auto: create` 는 시작할 때마다 스키마를 새로 만들기 때문에 로컬 학습용에 가깝다. 운영은 `validate` 또는 `none` 이 안전하다.
- Spring Boot 3.x 부터 패키지가 `javax.*` → `jakarta.*` 로 바뀌었다. 검증 어노테이션도 `jakarta.validation.constraints` 를 쓴다.

## 1. User

- `users` 가 SQL 예약어 충돌을 피하는 가장 흔한 테이블명이다.
- 비밀번호는 `BCryptPasswordEncoder.encode(...)` 로 해시한 결과만 저장한다. 평문 저장은 PRD 비기능 요구사항(2.6 보안) 위반.
- `@Enumerated(EnumType.STRING)` 을 쓰면 DB 에 `USER`, `ADMIN`, `MANAGER` 가 그대로 들어가서 enum 순서가 바뀌어도 데이터가 깨지지 않는다.
- `createdAt`, `updatedAt` 은 `@CreatedDate`, `@LastModifiedDate` + `@EnableJpaAuditing` 으로 자동화하거나, `@PrePersist`/`@PreUpdate` 로 수동 세팅한다.

## 2. Category / Partner

### 2-A. Category

- 카테고리명이 중복되면 같은 이름의 두 카테고리가 생겨 사용자가 혼란스럽다. `@Column(unique = true)` 또는 `@UniqueConstraint` 로 막는다.
- 양방향 매핑은 편하지만 직렬화 무한 루프, 페치 비용 증가, 양쪽 동기화 부담이 생긴다. 학습용으로는 **Item → Category 단방향**으로 두고, 카테고리별 품목 목록은 Repository 쿼리로 조회하는 편이 단순하다.

### 2-B. Partner

- 사업자번호는 법인 식별자로 같은 번호가 두 거래처에 있으면 정산/세무 정합성이 깨진다. DB `unique` + Service `existsBy...` 다층 방어.
- 사업자번호 정규화는 Entity 의 정적 팩토리에 두면 "어떤 경로로 들어와도 같은 형태로 저장" 이 보장된다. Service 에서도 한 번 더 막으면 안전망 추가.
- `PartnerType` 을 enum (SUPPLIER/CUSTOMER/BOTH) 으로 둔 이유: 한 거래처가 두 역할을 동시에 갖는 경우(BOTH) 가 흔하다. boolean 두 컬럼보다 enum 한 컬럼이 도메인 의미가 분명하다.
- `canSupply()`, `canBuy()` 같은 도메인 헬퍼는 Service 의 `if-else` 를 한 곳에 모은다. 발주는 `canSupply()` 일 때만, 수주는 `canBuy()` 일 때만 허용.
- INACTIVE 거래처의 과거 발주/수주 이력은 보존해야 한다. → hard delete 대신 status 만 변경하는 soft delete 가 안전.

## 3. Item

- Item 은 마스터 데이터이므로 Soft Delete 가 안전하다. 단종(`DISCONTINUED`) 상태로 두면 과거 발주/수주 라인이 깨지지 않는다.
- `fetch = LAZY` 가 기본인 이유: 품목 목록을 조회할 때마다 매번 카테고리까지 join 으로 끌어오면 N+1 또는 불필요한 부하가 생긴다.
- `BigDecimal` 을 쓰는 이유: 단가/금액 계산에서 `double` 의 부동소수점 오차(0.1 + 0.2 ≠ 0.3)가 회계 데이터에서 치명적이다.

## 4. PurchaseOrder

- 발주일/납기일은 시간 정보가 필요 없으므로 `LocalDate` 가 자연스럽다.
- 정적 팩토리(`PurchaseOrder.create(...)`)에서 `status = PurchaseOrderStatus.DRAFT` 으로 시작하도록 강제하면 호출부에서 상태 초기화를 잊을 일이 없다.
- `rejectReason` 은 `APPROVED` 일 때 null, `REJECTED` 일 때 필수다. → Service 검증에서 강제한다.
- `@OneToMany(mappedBy = "purchaseOrder", cascade = ALL, orphanRemoval = true)` 로 두면 헤더 저장만으로 라인까지 함께 저장되고 라인 삭제 동기화도 쉽다. 단, JPA cascade 는 컬렉션 mutation 시 의도치 않은 삭제를 일으킬 수 있으므로 도메인 메서드(`addLine`, `removeLine`)로 통제하는 것이 좋다.
- 총금액(`totalAmount`)은 라인 변경 시점에 갱신되어야 한다. 도메인 메서드 `recalculateTotal()` 을 두고 라인 변경 후 호출하면 일관성이 유지된다.

## 5. Notice

- 공지 본문은 길어질 수 있으니 `VARCHAR` 가 아니라 `TEXT`/`@Lob` 으로 둔다. `columnDefinition = "TEXT"` 가 가장 명시적이다.
- 중요 공지를 위로 올리려면 `ORDER BY important DESC, createdAt DESC` 같은 복합 정렬을 쓴다.
- `viewCount` 를 단순히 `+1` 하면 동시에 두 요청이 같은 값을 읽고 같은 값을 쓰는 경합이 생긴다. JPQL `UPDATE notice SET view_count = view_count + 1 WHERE id = :id` 같은 원자적 증가 쿼리를 쓰는 편이 안전하다.

## 6. SalesOrder

- 상태 전이 그래프:
  - `DRAFT` → `CONFIRMED` (수주 확정)
  - `CONFIRMED` → `SHIPPED` (출고)
  - `SHIPPED` → `COMPLETED` (완료)
  - `DRAFT/CONFIRMED/SHIPPED` → `CANCELED` (취소)
  - 그 외 전이는 모두 차단.
- 작성자와 처리자가 같을 수 있는지는 도메인 정책. 일반적으로 작성자(USER)와 처리자(MANAGER)는 다른 역할이지만, MANAGER 가 직접 작성+처리하는 경우도 허용 가능.
- `shippedAt`, `completedAt` 은 해당 상태 진입 시점에만 채워지므로 nullable 이다.

## 7. Repository

- 메서드 이름 쿼리는 Spring Data JPA 가 메서드 이름의 키워드(`existsBy`, `findBy`, `Containing`, `OrderBy` 등)를 보고 JPQL 을 자동 생성한다.
- 검색 쿼리는 `findByNameContainingOrItemCodeContainingOrCategory_NameContaining(...)` 처럼 길어질 수 있다. 조건이 동적이면 Querydsl/JPA Criteria 로 옮긴다.
- 페이징은 `Page<Item> findAll(Pageable pageable)` 처럼 시그니처만 맞추면 정렬과 페이지 메타 정보를 자동으로 채워 준다.
- `findByCategoryId(Long categoryId, Pageable pageable)` 처럼 FK 컬럼명 그대로 쓰면 된다.

## 8. 품목 등록

- `@Transactional` 은 Service 메서드에 단다. Controller 에 달면 메서드 진입 시점에 트랜잭션이 열려서 비즈니스 검증 전에 자원을 잡는다.
- 품목코드 중복 검사는 Repository 호출이 가장 가볍지만, 동시 등록 race 가 있으면 DB unique 제약이 최종 방어선이다.
- 카테고리 존재 확인은 Service 에서 `categoryRepository.findById(...).orElseThrow(...)` 로 한다.
- 트랜잭션 안에서 `RuntimeException` 이 던져지면 기본 rollback. checked exception 은 기본적으로 rollback 되지 않으므로 `rollbackFor = Exception.class` 가 필요한 경우가 있다.

## 9. 검색 + 페이징

- Controller 에서 `Pageable pageable` 을 그대로 받으면 `?page=0&size=20&sort=name,asc` 가 자동 매핑된다.
- `Page<Item> page = repo.findByXxx(...)` → `page.map(ItemResponse::from)` 으로 변환하면 페이지 메타 정보가 유지된다.
- 정렬을 클라이언트에 전부 맡기면 인덱스 없는 컬럼으로 정렬당할 수 있다. 허용 컬럼 목록을 두는 편이 안전하다.

## 10. 발주서 작성

- 헤더 + 라인은 반드시 같은 트랜잭션. 라인이 일부만 저장되면 총금액과 라인 합이 어긋난다.
- 라인 검증 순서: 품목 존재 → 단종 여부(`ITEM_DISCONTINUED`) → 수량 > 0 → 단가 ≥ 0.
- 거래처 유형이 `SUPPLIER` 또는 `BOTH` 인지 확인해야 한다. `CUSTOMER` 전용 거래처로 발주하면 `PARTNER_TYPE_MISMATCH`.
- 발주번호는 일자 + 시퀀스(예: `PO-20260526-0001`) 패턴. 동시성은 시퀀스 테이블, DB 시퀀스 또는 unique 충돌 시 재시도로 해결.
- 총금액 계산은 도메인 메서드(`recalculateTotal`)에 두면 라인 변경 시마다 일관되게 갱신된다.

## 11. 발주 승인/반려

- 상태 검증은 Service 진입 직후. `if (po.getStatus() != REQUESTED) throw INVALID_STATUS`.
- 반려 사유 blank 는 DTO `@NotBlank` 로 1차, Service `if (reason.isBlank())` 로 2차 검증.
- 승인 시 `approverId`, `approvedAt` 을 도메인 메서드 `approve(approverId)` 안에서 함께 채운다.
- 동시 승인 방지: 낙관적 락(`@Version`) 으로 두 요청 중 하나에 `OptimisticLockingFailureException` 을 발생시키거나, 비관적 락(`SELECT ... FOR UPDATE`) 으로 직렬화.

## 12. 공지

- ADMIN 만 등록/수정/삭제 가능. 1차는 Service 에서 role 검사, 2차는 Spring Security `@PreAuthorize`.
- 중요 공지 정렬은 `Sort.by(DESC, "important").and(Sort.by(DESC, "createdAt"))` 로 복합 정렬.
- 조회수 증가는 GET 안에서 처리하면 봇/미리보기로 부풀려질 수 있다. 별도 `PATCH /api/notices/{id}/view` 또는 원자 UPDATE 쿼리가 안전.

## 13. 수주서 작성

- 작성과 확정을 분리하면 임시저장(DRAFT) 기능이 자연스러워진다. POST 는 DRAFT 생성, PATCH /confirm 은 상태 전이.
- DRAFT → CONFIRMED 검증: 라인이 1개 이상인지, 라인 품목이 모두 ACTIVE 인지, 거래처가 `CUSTOMER`/`BOTH` 인지.
- 단종 품목이 라인에 포함되면 `ITEM_DISCONTINUED`. DRAFT 상태에서는 허용하되, CONFIRMED 시점에 막을 수도 있다(도메인 정책).

## 14. 수주 출고/완료/취소

- 상태 전이 표:
  - `CONFIRMED` → `SHIPPED` (`ship()`, `shippedAt = now()`)
  - `SHIPPED` → `COMPLETED` (`complete()`, `completedAt = now()`)
  - `DRAFT/CONFIRMED/SHIPPED` → `CANCELED` (`cancel(reason)`, `cancelReason` 필수)
- 매니저 권한 확인: `currentUser.role == MANAGER || ADMIN`.
- COMPLETED 또는 CANCELED 는 종료 상태로, 더 이상의 전이를 허용하지 않는다.

## 15. ErrorCode

- HTTP status 는 클라이언트(브라우저/Postman/모바일)가 즉시 분기하는 거시 시그널이고, ErrorCode 는 서버가 도메인 의미를 전달하는 미시 시그널이다.
- `ErrorResponse(status, code, message, timestamp)` 가 최소 필드. validation 실패는 `fields: [{field, message}]` 같은 nested 필드를 둘 수 있다.
- 도메인별 코드: `ITEM_NOT_FOUND`, `DUPLICATE_ITEM_CODE`, `PARTNER_TYPE_MISMATCH`, `EMPTY_ORDER_LINES`, `INVALID_STATUS`, `ITEM_DISCONTINUED` 등.

## 16. GlobalExceptionHandler

- `@RestControllerAdvice` 가 있으면 모든 `@RestController` 의 예외가 한 곳으로 모인다.
- `BusinessException(ErrorCode)` → 도메인 에러, `MethodArgumentNotValidException` → DTO Validation, `Exception` → 알 수 없는 서버 오류.
- Validation 실패 시 `BindingResult.getFieldErrors()` 를 돌면서 필드명 + 메시지를 모은다.

## 17. DTO 검증

- `@NotBlank` 는 문자열 + 공백 검증, `@Positive` 는 숫자 > 0, `@Size` 는 길이, `@Pattern` 은 정규식.
- `@Valid` 는 Controller 의 `@RequestBody` 파라미터에 붙어야 동작한다. Service 안의 객체 검증은 Validator 를 수동 호출.
- 요청 DTO 는 검증 규칙, 응답 DTO 는 노출 정책이 다르므로 분리한다.

## 18. ItemController

- `/api/items` 와 HTTP 메서드 매칭: 목록 GET, 등록 POST, 상세 GET /{id}, 수정 PUT /{id}, 단종 PATCH /{id}/discontinue.
- 권한 검증은 Spring Security 의 `@PreAuthorize("hasRole('ADMIN')")` 또는 Service 에서 `requireRole(ADMIN)`. 가급적 둘 다 두는 다층 방어.

## 19. PurchaseOrderController

- USER 용 `/api/purchase-orders/*` 와 ADMIN 용 `/api/admin/purchase-orders/*` 를 분리하면 권한 정책을 경로로 표현할 수 있다.
- `PATCH /{id}/approve` 같은 동사형 경로는 RESTful 원리에 살짝 어긋나지만 상태 전이 의미가 명확해서 실무에서 자주 쓴다.
- 로그인 사용자 ID 추출: 1차는 `HttpSession.getAttribute("USER_ID")`, 개선은 `@CurrentUser Long userId`, Spring Security 는 `@AuthenticationPrincipal`.

## 20. 보안 흐름

- 1차(세션): 로그인 성공 시 `session.changeSessionId()` 로 세션 ID 재발급 → `USER_ID`, `USER_ROLE` 저장 → Interceptor 가 매 요청 검사.
- 2차(Spring Security): `SecurityFilterChain`, `BCryptPasswordEncoder`, `UserDetailsService`, `@PreAuthorize` 로 선언적 권한.
- 3차(JWT): 무상태 토큰 발급 → `Authorization: Bearer ...` → 토큰 만료 시 refresh. 로그아웃 강제는 블랙리스트/짧은 TTL.

## 21. 통합 테스트

- MockMvc 로 로그인 → 발주 작성 → submit → 관리자 승인 → receive 흐름을 한 메서드 안에 묶는다.
- 응답 JSON 에서 다음 요청용 ID 는 `JsonPath.read(json, "$.purchaseOrderId")` 로 꺼낸다.
- 권한 실패는 `USER` 세션으로 `/api/admin/...` 호출 → `status().isForbidden()` 검증.

## 22. 문서화

- README 8섹션: 소개, 기능, 기술스택, 실행, ERD, API 명세, 트러블슈팅, 배운 점.
- ERD 는 텍스트 트리(빠름) → dbdiagram.io(공유) → draw.io(상세) 순서로 진화.
- 트러블슈팅 4단계: 문제 → 원인 → 해결 → 배운 점. 같은 템플릿을 유지해야 면접 때 흐름이 끊기지 않는다.
- API 명세: 초기엔 Markdown 표, 규모가 커지면 Swagger/OpenAPI 자동화.

## 23. 인증

- `email.trim().toLowerCase()` 정규화 → `userRepository.findByEmail(...)` → `passwordEncoder.matches(raw, encoded)`.
- 세션 고정 공격 방지: 로그인 직후 `request.changeSessionId()` 로 세션 ID 재발급.
- "이메일/비밀번호 중 무엇이 틀렸는지" 알려주면 enumeration 공격에 노출된다. 동일 메시지로 응답.
- 비밀번호 변경: 현재 비밀번호 `matches` 확인 → 새 비밀번호 `encode` → `user.changePassword(encoded)`.

## 24. 거래처 Service / 카테고리 Service

### 24-A. PartnerService

- 사업자번호 unique 는 Service `existsByBusinessNumber` 1차 검사 + DB unique 2차 방어.
- 삭제 정책: 발주/수주 이력이 있으면 hard delete 대신 `INACTIVE` 상태로 두는 soft delete 가 안전.
- 유형(SUPPLIER → BOTH)으로 확장은 OK. CUSTOMER → SUPPLIER 같은 변경은 기존 수주 이력과 모순될 수 있어 정책으로 차단.
- 검색은 keyword + partnerType 조건 4가지 조합으로 분기. 동적 조건이 더 늘면 Querydsl/Specification.

### 24-B. CategoryService

- 카테고리명 unique 는 Service `existsByName` 1차 검사 + DB unique 2차 방어.
- 카테고리 삭제 시 소속 품목이 있으면 `CATEGORY_HAS_ITEMS` 에러로 거부. 대안: "기본 카테고리" 로 일괄 이관 (도메인 정책 선택).
- 상세 응답에 소속 품목까지 포함하면 응답 페이로드가 커지므로 별도 페이징 가능한 nested Page 로 분리.
- 카테고리는 보통 수십 ~ 수백 개라 MVP 에서는 전체 조회로 충분. 1000개 넘으면 페이징 + 캐싱 검토.
- 카테고리명을 캐싱한다면 update/delete 시점에 캐시 무효화 필요.

## 25. 거래처 Controller / 카테고리 Controller

### 25-A. PartnerController

- ADMIN 검사는 `@PreAuthorize` 1차, Service `requireRole(ADMIN)` 2차.
- 전체 갱신은 PUT (모든 필드 교체), 부분 갱신은 PATCH (변경 필드만). 학습용으론 PUT 으로 단순화 가능.
- 거래처 상세에 발주/수주 통계까지 포함하면 API 가 무거워지므로 별도 `/stats` 엔드포인트로 분리할지 결정.

### 25-B. CategoryController

- 카테고리는 보통 수십~수백 개이므로 목록 응답은 `List` 가 자연스럽다. 1000개 이상이면 `Page` 로 전환.
- 상세에 소속 품목은 별도 페이지로 `Page<ItemResponse>` 형태가 안전. 같은 응답에서 무거운 컬렉션을 통째로 내려주면 N+1 + 페이로드 부담.
- 삭제 정책: 소속 품목 0개일 때만 hard delete. 0이 아니면 `CATEGORY_HAS_ITEMS (400)` 으로 거부 (대안: 기본 카테고리로 일괄 이관).
- 등록/수정/삭제는 ADMIN, 조회는 로그인 사용자 전체. 캐싱한다면 등록/수정/삭제 시점에 캐시 무효화.

## 26. 공지 Controller

- GET 안에서 viewCount 증가는 봇/프리뷰에 노출. 별도 `PATCH /view` 또는 클라이언트 명시 호출이 안전.
- 목록 응답에 긴 `content` 가 들어가면 페이로드 비용. 목록 DTO 는 `summary` 또는 `contentPreview` 필드만 둔다.
- `@PageableDefault(size = 20, sort = "createdAt", direction = DESC)` 로 기본값.

## 27. 수주 Controller

- `/my` 는 작성자, `/pending` 은 처리자(매니저)가 본다. 같은 데이터지만 필터 조건이 다르다.
- 동사형 경로는 RESTful 원리에서 벗어나지만 상태 전이를 명확하게 표현한다. 일관성만 지키면 실무에서 충분히 받아들여진다.
- `@PreAuthorize("hasAnyRole('MANAGER','ADMIN')")` 는 두 역할 중 하나에 해당하면 허용.

## 28. 품목 상세 / 수정 / 단종

- LAZY 로 묶인 카테고리는 DTO 변환 시점에 트랜잭션이 살아있어야 한다. open-in-view 가 켜져 있으면 view 렌더링 시점까지 트랜잭션이 유지된다(성능 트레이드오프).
- 카테고리 변경: 새 카테고리 존재 확인 → `item.changeCategory(category)` 도메인 메서드. setter 를 직접 호출하지 않으면 도메인 규칙이 한 곳에 모인다.
- soft delete(`DISCONTINUED`)는 과거 발주/수주 라인을 보존한다. hard delete 는 FK 제약 때문에 어렵고 이력 추적이 끊긴다.

## 29. 발주 — 내 목록 / 상세 / 취소 + 관리자 목록

- 본인 발주 조회: `findByWriterId(currentUserId, pageable)`. URL 에 writerId 를 받지 않고 세션 사용자 기반.
- 본인 취소 가능 상태: `DRAFT`, `REQUESTED` 까지. `APPROVED` 이후는 관리자 처리 영역.
- 관리자 목록은 status + partnerId 조건 분기. 4가지 조합을 if-else 또는 Specification 으로 처리.
- 기간 조건(from/to)은 `orderDate BETWEEN :from AND :to` JPQL 또는 Querydsl.

## 30. 수주 — my / pending / detail

- 상세 권한: 작성자(writerId == current) OR 매니저(managerId == current) OR ADMIN. 그 외는 `ACCESS_DENIED`.
- 매니저 위임(managerId 변경) 시 pending 목록은 새 매니저 기준으로 보이게 된다. 위임 이력은 별도 테이블로 남길 수도 있다.
- 관리자 전체 조회는 `findAll(pageable)` 또는 조건별 메서드를 분기해서 호출.

## 31. Repository 전체

- nested property: `findByPartner_BusinessNumber(...)` 는 SQL JOIN 으로 풀린다.
- 발주 기간 검색: `@Query("SELECT po FROM PurchaseOrder po WHERE po.orderDate BETWEEN :from AND :to")`.
- 조회수 원자 증가: `@Modifying @Query("UPDATE Notice n SET n.viewCount = n.viewCount + 1 WHERE n.id = :id")`.
- 메서드 이름이 4단어를 넘어가면 `@Query` 로 옮기는 편이 가독성에 좋다.

## 32. 응답 DTO 매핑

- `ItemResponse.from(item)` 정적 팩토리로 변환 책임을 DTO 에 둔다.
- 페이징은 `page.map(ItemResponse::from)` 로 메타 정보(totalElements 등) 유지.
- 헤더-라인은 `PurchaseOrderResponse(headerFields..., List<PurchaseOrderLineResponse> lines)` nested 구조.
- DTO → Entity 변환은 OK, Entity → DTO 도 OK. 그러나 Entity 가 DTO 를 알면 도메인이 표현 계층에 오염된다.
- `record` DTO 는 불변/간결, `class` + Lombok 은 유연. 응답엔 record, 요청엔 class 가 흔하다.
- MapStruct 는 필드 수가 많고 변환 규칙이 단순할 때 도입. 학습 단계에선 손으로 쓰는 편이 도움이 된다.

## 33. Bean 설정

- `@EnableJpaAuditing` 가 없으면 `@CreatedDate`, `@LastModifiedDate` 가 동작하지 않아 null 이 들어간다.
- BCrypt 의 안전성: salt 자동 생성 + work factor(라운드 수)로 GPU 무차별 대입에 강하다.
- `addInterceptors` 는 매 요청 전후 가로채기, `addArgumentResolvers` 는 컨트롤러 파라미터 해석.
- 1차(세션 Interceptor)와 2차(Security) 를 동시에 켜면 검증이 두 번 + 충돌. 한 가지로 통일.

## 34. CurrentUser / Interceptor

- `@CurrentUser` 어노테이션이 붙은 파라미터를 `HandlerMethodArgumentResolver` 가 가로채서 `session.getAttribute("USER_ID")` 를 주입.
- Filter → Interceptor → Controller 순서. Filter 는 Servlet 단계, Interceptor 는 Spring MVC 단계.
- ArgumentResolver 가 없으면 모든 Controller 가 매번 session 코드를 반복.
- JWT 진화 시: Interceptor 대신 Security Filter, ArgumentResolver 는 `@AuthenticationPrincipal` 사용.

## 35. Thymeleaf

- `templates/items/`, `templates/purchase-orders/` 처럼 도메인별로 디렉터리 분리.
- `th:text` 는 escape 자동(XSS 안전), `th:utext` 는 raw HTML(취약). 사용자 입력은 항상 `th:text`.
- CSRF 토큰은 폼 안에 `<input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}" />`.
- Fragment 로 헤더/푸터 재사용: `<header th:fragment="header">...</header>` → `<div th:replace="~{fragments :: header}"></div>`.
- 발주 작성 화면의 라인 동적 추가: JS 로 행 복제 또는 Thymeleaf list rendering + 클라이언트 스크립트.
- `@RestController` = JSON 응답, `@Controller` + 메서드 String 반환 = view 렌더링.

## 36. 패키지 구조

- Layer-first: `controller/`, `service/`, `repository/`, `domain/`. 초보자에게 직관적.
- Feature-first: `purchase/`, `sales/`, `item/`, `partner/` 안에 각자의 controller/service/... 둠. 규모가 커질 때 응집도가 좋다.
- 의존 방향: Controller → Service → Repository → Domain. 반대 방향 의존은 금지.
- 안티 패턴: Controller 가 Repository 직접 호출, Service 가 Entity 를 그대로 응답, Domain 이 Service 의존, DTO 가 Entity 의존 등.

## 37. 비즈니스 규칙 매트릭스

| 규칙 | D | E | S | DB |
|---|---|---|---|---|
| 품목코드 중복 금지 | - | - | exists 검사 | unique 제약 |
| 사업자번호 중복 금지 | - | - | exists 검사 | unique 제약 |
| 발주일 ≤ 납기일 | - | - | validateDateRange | - |
| 발주 라인 ≥ 1 | @Size(min=1) | - | validateLines | - |
| 거래처 유형 일치 | - | - | validatePartnerType | - |
| 상태 전이 검증 | - | 도메인 메서드 | check before transition | - |
| 단종 품목 라인 금지 | - | - | validateItemStatus | - |

다층 방어 이유: DTO 검증을 우회하는 클라이언트, Service 가 빠진 직접 Repository 호출, 동시성 race 등 각 계층마다 빠질 수 있는 공격면이 다르기 때문이다.

## 38. 트러블슈팅 (PRD 형식 5건)

- **발주 승인 중복**: 상태 검증 누락 → Service `if (status != REQUESTED) throw INVALID_STATUS`.
- **발주 라인 부분 저장**: 트랜잭션 누락 → `@Transactional` 적용 + cascade 정합성.
- **Entity 직접 반환**: 비밀번호 노출 → Response DTO 분리.
- **LazyInitializationException**: 트랜잭션 밖 LAZY 접근 → DTO 변환을 Service 트랜잭션 안에서.
- **거래처 유형 불일치**: 화면에서만 막음 → Service `validatePartnerType` 추가.
- **발주번호 동시 채번**: 시퀀스 race → DB 시퀀스/유니크 충돌 재시도.

## 39. 면접/커밋

- 면접 8질문: 프로젝트 동기 / SCM 도메인 이해 / 헤더-라인 설계 / 상태 전이 / 트랜잭션 / 권한 / DTO / 가장 어려웠던 문제.
- 커밋 타입: `feat`(기능), `fix`(버그), `refactor`(구조), `docs`(문서), `test`(테스트), `chore`(빌드/설정), `style`(코드 포맷).
- 메시지는 "왜": `feat: 발주 라인 cascade 로 헤더-라인 정합성 보장` ← "헤더와 라인을 따로 저장하면 라인이 일부만 저장되는 문제가 있어서".
- PR 4섹션: 무엇 / 왜 / 테스트 / 영향(다른 기능에 끼치는 영향, 마이그레이션 필요 여부 등).
