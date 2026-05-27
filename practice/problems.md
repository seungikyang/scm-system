# 문제 (Problems)

각 문제는 PRD/TRD 문서의 어떤 절을 구현하는지 함께 적었습니다. 빈칸을 채우면서 PRD/TRD 의 해당 절을 다시 읽으세요.

---

## 0. 빌드와 애플리케이션 설정 (TRD 3.1, 3.2)

파일:

- `starter/00-build-config/build.gradle.fragment`
- `starter/00-build-config/application.yml.fragment`

- Spring Web, Spring Data JPA, Validation, Thymeleaf, H2 의존성의 역할을 구분한다.
- Java 17 / Spring Boot 3.x 조합과 `javax` → `jakarta` 변경을 인지한다.
- `ddl-auto` 옵션의 의미(create/create-drop/update/validate/none)를 구분한다.
- 로컬 H2 콘솔 활성화 설정을 채운다.

## 1. User 엔티티 (TRD 3.3.1, 3.6.1)

파일: `starter/01-user-entity/User.entity.java`

- `@Entity`, `@Table(name="users")`, `@Id`, `@GeneratedValue` 의 역할을 채운다.
- `email` 은 unique, `password` 는 평문 저장 금지(=해시 후 저장)임을 이해한다.
- `UserRole` enum 을 `@Enumerated(EnumType.STRING)` 으로 저장하는 이유를 답한다.
- `createdAt`, `updatedAt` 자동 세팅(`@CreatedDate`, `@LastModifiedDate` 또는 `@PrePersist`).

## 2. Category 엔티티 / Partner 엔티티 (TRD 3.3.2 ~ 3.3.3, 3.5.2 ~ 3.5.3, 3.6.2)

파일:

- `starter/02-category-entity/Category.entity.java`
- `starter/02-category-entity/Partner.entity.java`

### 2-A. Category

- 카테고리명(`name`)을 unique 로 잡는 이유를 답한다.
- Category 에서 Item 방향 연관관계를 매핑할지(`@OneToMany(mappedBy=...)`) 또는 단방향으로 둘지 트레이드오프를 적어본다.
- DB 컬럼 길이(VARCHAR(100), VARCHAR(255)) 와 도메인 의미를 매칭한다.

### 2-B. Partner

- 사업자번호(`businessNumber`)를 unique 로 잡는 이유를 답한다.
- 사업자번호의 공백/하이픈 정규화를 어디(DTO / Service / Entity)에서 할지 트레이드오프를 적어본다.
- `PartnerType` (SUPPLIER / CUSTOMER / BOTH) enum 을 작성하고, `canSupply()`, `canBuy()` 같은 도메인 헬퍼를 둔 이유를 답한다.
- `PartnerStatus` (ACTIVE / INACTIVE) enum 을 작성한다.
- INACTIVE 거래처로 작성된 과거 발주/수주 이력을 어떻게 보존할지 정책을 적는다.

## 3. Item 엔티티 (TRD 3.3.4, 3.5.4, 3.6.3)

파일: `starter/03-item-entity/Item.entity.java`

- `Category` 와 N:1 관계를 매핑한다.
- `fetch = LAZY` 가 기본이 되어야 하는 이유를 답한다.
- `ItemStatus` (ACTIVE / DISCONTINUED) enum 을 작성한다.
- 단종 처리 시 데이터를 DELETE 하지 않고 `DISCONTINUED` 로 두는 이유를 답한다.
- `unitPrice` 를 `BigDecimal` 로 두는 이유 (double 의 부동소수점 문제).

## 4. PurchaseOrder 엔티티와 라인 (TRD 3.3.5, 3.3.6, 3.6.4)

파일: `starter/04-purchase-order-entity/PurchaseOrder.entity.java`

- 발주일/납기일을 `LocalDate` 로 저장하는 이유.
- `status` 의 초기값을 `DRAFT` 으로 두는 정적 팩토리 메서드를 작성한다.
- 승인 시 `approverId`, 반려 시 `rejectReason` 이 들어가는 자리를 채운다.
- 작성자(`writerId`)와 승인자(`approverId`)가 서로 다를 수 있도록 유지한다.
- `PurchaseOrderLine` 을 `@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)` 로 둘 때의 장단점.
- 헤더 `totalAmount` 와 라인 `lineAmount` 의 합을 일치시키는 위치 (도메인 메서드 vs Service).

## 5. Notice 엔티티 (TRD 3.3.7, 3.5.7)

파일: `starter/05-notice-entity/Notice.entity.java`

- 본문(`content`)을 `@Lob` 또는 `columnDefinition = "TEXT"` 로 처리하는 이유.
- `important` 가 true 인 공지를 상단에 노출하려면 어떤 정렬을 써야 하는가?
- `viewCount` 를 동시 증가시킬 때 발생할 수 있는 동시성 문제를 한 줄로 설명한다.

## 6. SalesOrder 엔티티와 라인 (TRD 3.3.8, 3.6.5)

파일: `starter/06-sales-order-entity/SalesOrder.entity.java`

- `SalesOrderStatus` enum 의 상태 전이 그래프를 그려본다. (`DRAFT → CONFIRMED → SHIPPED → COMPLETED`, 또는 `→ CANCELED`)
- 작성자(`writerId`)와 처리자(`managerId`)가 같을 수 있는지 정한다.
- `shippedAt`, `completedAt` 컬럼이 nullable 인 이유를 답한다.
- 수주 라인(`SalesOrderLine`)을 헤더 라이프사이클에 연결한다.

## 7. Repository 메서드 쿼리 (TRD 3.7.4, 3.8.2)

파일: `starter/07-repository/ItemRepository.java`

- `existsByItemCode`, `findByItemCode` 같은 메서드 이름 쿼리의 규칙.
- 검색을 위한 `findByNameContainingOrItemCodeContaining` 시그니처 채우기.
- `Pageable` 을 사용하는 페이징 메서드 시그니처 채우기.
- 어떤 카테고리에 속한 품목만 보고 싶을 때 `findByCategoryId` 같은 쿼리를 추가한다.

## 8. 품목 등록 Service (TRD 3.8.2)

파일: `starter/08-item-register/ItemService.register.java`

- `@Transactional` 의 위치와 의미.
- 품목코드 중복 검사를 어디서 하는지.
- 카테고리 존재 여부 확인의 책임 위치.
- 트랜잭션 도중 예외가 발생하면 어떤 일이 일어나는지.

## 9. 품목 검색 + 페이징 (TRD 3.5.4, 2.5.4)

파일: `starter/09-item-search/ItemService.search.java`

- `Pageable` 을 어떻게 받고 어떻게 넘기는지.
- `Page<Item>` 를 `Page<ItemResponse>` 로 변환하는 방법.
- 정렬 기준을 클라이언트에서 받을지, 서버에서 고정할지 결정한다.

## 10. 발주서 작성 Service (TRD 3.8.3, 3.10.1)

파일: `starter/10-purchase-order-create/PurchaseOrderService.create.java`

- 헤더 + 라인을 같은 트랜잭션으로 묶는 이유.
- 라인 검증 순서: 품목 존재 → 단종 여부 → 수량 > 0.
- 거래처 유형이 SUPPLIER/BOTH 인지 확인하는 위치.
- 발주번호(orderNumber) 채번 전략 선택.
- 총금액 계산 책임을 도메인에 둘지 Service에 둘지.

## 11. 발주 승인/반려 Service (TRD 3.8.3, 3.10.2)

파일: `starter/11-purchase-order-approval/PurchaseOrderService.approval.java`

- `REQUESTED` 상태가 아닌 발주에 대해 승인/반려를 막는 검증 위치.
- 반려 시 `rejectReason` 이 비어 있으면 어떻게 처리할지.
- 승인 시 `approverId`, `approvedAt` 을 채우는 시점.
- 동시에 두 관리자가 같은 발주를 승인하면 어떻게 되는지 생각해본다.

## 12. 공지사항 Service (TRD 3.8.4)

파일: `starter/12-notice-service/NoticeService.java`

- 권한 검증을 어디서 할지 (`USER` 는 등록 불가).
- `important=true` 공지를 항상 상단에 두는 정렬.
- 조회수 증가는 별도 메서드로 분리할지, 상세 조회와 함께 둘지.

## 13. 수주서 작성 Service (TRD 3.8.5)

파일: `starter/13-sales-order-create/SalesOrderService.create.java`

- 작성과 확정을 같은 API 로 둘지, 분리(`POST` / `PATCH /confirm`)할지의 트레이드오프.
- `DRAFT → CONFIRMED` 으로 상태 변경 시 검증할 항목.
- 거래처 유형이 CUSTOMER/BOTH 인지 검증하는 위치.
- 단종 품목 라인을 받았을 때의 동작.

## 14. 수주 출고/완료/취소 Service (TRD 3.8.5, 3.10.4)

파일: `starter/14-sales-order-decision/SalesOrderService.decision.java`

- 상태 전이 표 (`CONFIRMED → SHIPPED`, `SHIPPED → COMPLETED`, `→ CANCELED`) 를 검증한다.
- 매니저 권한이 본인이 맞는지 확인한다.
- 출고 시 `shippedAt`, 완료 시 `completedAt` 을 세팅한다.
- 취소 시 사유 필수.

## 15. ErrorCode 와 응답 모델 (TRD 3.9)

파일: `starter/15-error-model/ErrorCodeAndResponse.java`

- HTTP status 와 비즈니스 에러 코드의 책임 분리.
- `ErrorResponse` 가 가져야 할 필드(status, code, message, timestamp).
- 도메인별 에러 코드(`ITEM_NOT_FOUND`, `INVALID_DATE_RANGE`, `PARTNER_TYPE_MISMATCH` 등)를 enum 으로 모은다.

## 16. GlobalExceptionHandler (TRD 3.9)

파일: `starter/16-global-handler/GlobalExceptionHandler.java`

- `@RestControllerAdvice` 의 역할.
- `BusinessException`, `MethodArgumentNotValidException`, `Exception` 핸들러를 각각 만든다.
- Validation 실패 시 필드명 + 메시지 목록을 어떻게 모을지.

## 17. DTO 검증 (TRD 3.7, 비기능 검증)

파일: `starter/17-dto-validation/ItemCreateRequest.java`

- `@NotBlank`, `@Positive`, `@Size`, `@Pattern` 의 차이.
- `@Valid` 가 어떤 계층에서 동작하는지.
- 응답 DTO 와 요청 DTO 를 분리하는 이유.

## 18. ItemController (TRD 3.7.4)

파일: `starter/18-controller-item/ItemController.java`

- 경로 설계(`/api/items`)와 HTTP 메서드 매칭.
- `@PathVariable`, `@RequestBody`, `@RequestParam` 의 차이.
- 관리자만 접근하도록 권한 검증을 어디서 하는지.

## 19. PurchaseOrderController (TRD 3.7.5)

파일: `starter/19-controller-purchase/PurchaseOrderController.java`

- `/api/purchase-orders` (USER) 와 `/api/admin/purchase-orders` (ADMIN) 경로 분리.
- `PATCH /api/admin/purchase-orders/{poId}/approve` 같은 동사형 경로의 장단점.
- 로그인 사용자의 ID 를 Controller 에서 어떻게 꺼낼지(`HttpSession`, `Principal`, `@AuthenticationPrincipal`).

## 20. 보안 흐름 (TRD 3.11)

파일: `starter/20-security-session/SecurityFlow.md`

- 1차 구현: 세션 기반 로그인 흐름을 단계별로 적는다.
- 2차 구현: Spring Security + BCrypt + Role 기반 접근 제어로 진화시키는 차이.
- 3차 구현: JWT 로 무상태(stateless) 인증을 적용할 때 달라지는 점.

## 21. 통합 테스트 흐름 (TRD 3.12)

파일: `starter/21-test-flow/PurchaseFlowTest.java`

- MockMvc 로 로그인 → 발주 작성 → 발주 요청 → 관리자 승인 → 입고 흐름을 채운다.
- 응답 JSON 에서 다음 요청에 쓸 값(purchaseOrderId 등)을 어떻게 꺼낼지.
- 권한 실패 테스트(USER 가 ADMIN API 호출)를 어떻게 작성할지.

## 22. 문서화 (TRD 3.15 ~ 3.17)

파일: `starter/22-documentation/PortfolioDocs.md`

- README 에 들어가야 할 8가지 섹션을 적는다.
- ERD 문서의 표기 방식(텍스트 트리 / dbdiagram.io / draw.io) 선택 기준.
- 트러블슈팅 문서의 4단계 템플릿(문제 → 원인 → 해결 → 배운 점).
- API 명세 문서를 손으로 적을지(Markdown) Swagger/OpenAPI 로 자동화할지.

## 23. 인증 — 로그인 / 로그아웃 / 내 정보 / 비밀번호 변경 (PRD 2.5.1, TRD 3.7.1, 3.11)

파일:

- `starter/23-auth-login/AuthService.login.java`
- `starter/23-auth-login/AuthController.java`

- 이메일 정규화 후 사용자 조회, `passwordEncoder.matches()` 로 비번 검증.
- 세션 기반 인증(`HttpSession`) 에서 사용자 ID / Role 저장.
- 세션 고정 공격(Session Fixation) 방지를 위한 세션 ID 재발급 시점.
- 로그인 실패 메시지를 "이메일 vs 비밀번호" 로 구분하지 않는 이유.
- 비밀번호 변경 시 현재 비밀번호 확인 + 새 비밀번호 해시.

## 24. 거래처 Service / 카테고리 Service — CRUD + 정책 (PRD 2.5.2, 2.5.3, TRD 3.7.2, 3.7.3)

파일:

- `starter/24-partner-service/PartnerService.java`
- `starter/24-partner-service/CategoryService.java`

### 24-A. PartnerService

- 사업자번호 중복 검증 (`existsByBusinessNumber`) 과 DB unique 의 다층 방어.
- 거래처 삭제 시 발주/수주 이력이 있을 때의 정책(거부 / 비활성화 / 소프트 삭제).
- 거래처 유형(SUPPLIER/CUSTOMER/BOTH) 변경 시 기존 발주/수주의 무결성.
- 거래처 검색을 이름/사업자번호/유형 조건으로 분기.

### 24-B. CategoryService

- 카테고리명 중복 검증 (`existsByName`) 과 DB unique 의 다층 방어.
- 카테고리 삭제 시 소속 품목이 있을 때의 정책(거부 / 기본 카테고리 이동).
- 카테고리 상세에서 소속 품목까지 N+1 없이 조회하는 방법.
- 카테고리 목록을 페이징 없이 전체 조회로 둔 이유와, 카테고리가 1000개를 넘었을 때의 대안.

## 25. 거래처 Controller / 카테고리 Controller + DTO (TRD 3.7.2, 3.7.3)

파일:

- `starter/25-partner-controller/PartnerController.java`
- `starter/25-partner-controller/CategoryController.java`

### 25-A. PartnerController

- `@PreAuthorize` 와 Service 가드의 다층 권한 검사.
- 전체 갱신(PUT) vs 부분 갱신(PATCH) 선택.
- `PartnerDetailResponse` 가 발주/수주 통계까지 포함할지 결정.

### 25-B. CategoryController

- 카테고리 목록은 List 와 Page 중 어떤 응답이 자연스러운가.
- 카테고리 상세에 소속 품목까지 nested 로 담을 때의 응답 크기 / N+1 고려.
- 카테고리 삭제 시 `CATEGORY_HAS_ITEMS` 응답이 어떻게 동작하는지.
- 권한: 등록/수정/삭제는 ADMIN, 조회는 로그인 사용자 전체.

## 26. 공지 Controller + DTO (TRD 3.7.6)

파일: `starter/26-notice-controller/NoticeController.java`

- 조회수 증가를 GET 안에서 처리할 때의 단점 / 대안 (별도 PATCH).
- 공지 목록 응답에 `content` 를 그대로 포함할 때의 페이로드 비용.
- `@PageableDefault` 로 페이지 크기 기본값 설정.

## 27. 수주 Controller + DTO — my / pending / confirm / ship / complete (TRD 3.7.7)

파일: `starter/27-sales-controller/SalesOrderController.java`

- `/api/sales-orders/my` (USER) 와 `/api/sales-orders/pending` (MANAGER/ADMIN) 의 권한 차이.
- 상태 전이를 동사형 경로(`/confirm`, `/ship`, `/complete`, `/cancel`)로 표현한 트레이드오프.
- `@PreAuthorize("hasAnyRole('MANAGER','ADMIN')")` 의 의미.

## 28. 품목 상세 / 수정 / 단종 처리 (PRD 2.5.4)

파일: `starter/28-item-detail-update/ItemService.detail.java`

- LAZY 관계가 응답 변환 시점에 어떻게 풀리는지(open-in-view).
- 카테고리 변경 시 카테고리 존재 확인 + 도메인 메서드(`changeCategory`) 사용.
- Service 가 setter 를 직접 호출하지 않고 도메인 메서드를 만드는 이유.
- soft delete(`DISCONTINUED`) vs hard delete 의 트레이드오프.

## 29. 발주 — 내 목록 / 상세 / 취소 + 관리자 목록 (PRD 2.5.5)

파일: `starter/29-purchase-my-cancel/PurchaseOrderService.my.java`

- 본인 발주만 조회 가능하도록 Service 에서 소유자 검증.
- 본인 DRAFT/REQUESTED 발주만 취소 가능하도록 도메인 메서드에서 상태 검증.
- 관리자 전체 목록은 FR-PO-009 기준으로 `/api/admin/purchase-orders` 에서 상태/거래처 조건을 받는다.
- 관리자 목록을 status + partnerId 조건으로 분기 조회하는 패턴.
- 기간(from/to) 조건을 추가했을 때의 확장 방법.

## 30. 수주 — my / pending / detail 권한 검사 (PRD 2.5.7)

파일: `starter/30-sales-lists/SalesOrderService.lists.java`

- 작성자도 매니저도 아니고 ADMIN 도 아닌 사람이 상세 조회하면 ACCESS_DENIED.
- 처리자(`managerId`) 위임 시 pending 쿼리가 어떻게 영향받는가.
- 관리자에게 모든 수주서를 보여줄 때 Service 시그니처 설계.

## 31. Repository — User / Partner / Category / PurchaseOrder / Notice / SalesOrder (TRD 3.5)

파일: `starter/31-repositories-all/AllRepositories.java`

- `findByPartner_BusinessNumber` 같은 nested property 메서드 쿼리.
- 발주 기간 검색 JPQL (`orderDate BETWEEN :from AND :to`).
- 조회수 원자적 증가(`@Modifying @Query`) 패턴.
- 메서드 이름이 길어졌을 때 `@Query` 로 옮기는 기준.

## 32. 응답 DTO 매핑 패턴 (TRD 3.16.3)

파일: `starter/32-response-dto-mapping/ResponseDtoMapping.java`

- `ItemResponse.from(item)` 같은 정적 팩토리.
- `Page.map(ItemResponse::from)` 으로 페이지 메타 정보 유지.
- 헤더-라인 DTO(`PurchaseOrderResponse` + `PurchaseOrderLineResponse`) 의 nested 구조.
- Entity ↔ DTO 의 단방향 의존(DTO → Entity 는 OK, 반대는 금지).
- record vs class DTO 의 차이.
- MapStruct / ModelMapper 를 도입할 시점.

## 33. 핵심 Bean 설정 — JPA Auditing / PasswordEncoder / WebMvcConfig (TRD 3.5, 3.11)

파일: `starter/33-config-beans/ConfigBeans.java`

- `@EnableJpaAuditing` 없으면 `createdAt` 이 null 로 들어오는 이유.
- BCrypt 가 SHA-256 보다 안전한 이유(salt + work factor).
- `WebMvcConfigurer` 의 `addInterceptors` / `addArgumentResolvers` 역할.
- 1차(세션) Config 와 2차(Security) Config 를 동시에 켜면 안 되는 이유.

## 34. 세션 인터셉터 + @CurrentUser ArgumentResolver (TRD 3.11.1)

파일: `starter/34-current-user-interceptor/CurrentUserAndInterceptor.java`

- 커스텀 `@CurrentUser` 어노테이션 + ArgumentResolver 의 동작 원리.
- Interceptor 와 Filter 의 실행 순서.
- ArgumentResolver 가 없다면 Controller 마다 반복되어야 할 코드.
- JWT 단계로 진화하면 어떤 부분이 바뀌어야 하는가.

## 35. Thymeleaf 화면 (PRD 2.7)

파일: `starter/35-thymeleaf-views/ThymeleafViews.md`

- `templates/` 디렉터리 구조와 도메인별 분리.
- `th:text` vs `th:utext` (escape) 의 안전한 사용.
- 폼 + CSRF 토큰 처리.
- Fragment 재사용 (`th:fragment`, `th:replace`).
- 발주서 작성 화면에서 라인 동적 추가 UX.
- `@RestController` 와 `@Controller` 의 차이와 동시 운영.

## 36. 패키지 구조와 계층 책임 (TRD 3.2.2)

파일: `starter/36-package-architecture/PackageArchitecture.md`

- 계층 우선(Layer-first) vs 도메인 우선(Feature-first) 패키지 전략.
- 의존 방향 그래프(Controller → Service → Repository → Domain).
- 각 계층이 의존해서는 안 되는 패키지.
- 5가지 안티 패턴 점검.

## 37. 비즈니스 규칙 다층 검증 매트릭스 (TRD 3.8, 3.11)

파일: `starter/37-business-rules/BusinessRulesChecklist.md`

- 각 규칙을 D(DTO)/E(Entity)/S(Service)/DB 중 어디에서 검증하는지 매핑.
- 같은 검증을 여러 계층에 두는 이유(다층 방어).
- DB unique 제약과 Service `exists` 검사의 관계.
- 도메인 메서드의 IllegalStateException 을 ErrorCode 로 변환하는 일관성.

## 38. 트러블슈팅 워크북 (TRD 3.16)

파일: `starter/38-troubleshooting/Troubleshooting.md`

- 발주 승인 중복 처리 / 발주 라인 부분 저장 / Entity 직접 반환 / LazyInitializationException / 거래처 유형 불일치 / 발주번호 동시 채번.
- 각 케이스를 PRD 형식(문제 → 원인 → 해결 → 배운 점) 으로 채우기.
- 본인의 트러블슈팅 1건 추가.

## 39. 면접 카드 + 커밋 + PR 템플릿 (TRD 3.18 ~ 3.20)

파일: `starter/39-interview-and-commit/InterviewAndCommit.md`

- TRD 3.19 의 12 질문 중 핵심 8개에 30초 분량 답변.
- 커밋 메시지 타입(feat/fix/refactor/docs/test/chore/style).
- "무엇" 이 아닌 "왜" 를 적는 메시지 원칙.
- PR 템플릿 4섹션(무엇/왜/테스트/영향).
