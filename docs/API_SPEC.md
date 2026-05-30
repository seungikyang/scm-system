# API 명세서 (API_SPEC)

SCM 시스템 REST API 명세입니다. **실제 컨트롤러(`controller.api`) 구현을 기준**으로 작성했으며, 설계 계약(`_workspace/02_architect_contracts.md`)과 1:1로 대조해 일치를 확인했습니다.

- 모든 JSON 필드는 camelCase, URL은 소문자 + kebab-case입니다.
- enum은 문자열로 직렬화됩니다(예: `"SUPPLIER"`, `"DRAFT"`).
- 날짜: `LocalDate → "YYYY-MM-DD"`, 시각: `LocalDateTime → "YYYY-MM-DDTHH:mm:ss"`. 금액(`BigDecimal`)은 JSON number.

---

## 목차

1. [공통 규약](#1-공통-규약)
2. [인증 / 사용자 API](#2-인증--사용자-api)
3. [거래처 API](#3-거래처-api)
4. [카테고리 API](#4-카테고리-api)
5. [품목 API](#5-품목-api)
6. [발주 API (사용자 영역)](#6-발주-api-사용자-영역)
7. [발주 API (관리자/매니저 영역)](#7-발주-api-관리자매니저-영역)
8. [에러 코드 표](#8-에러-코드-표)

---

## 1. 공통 규약

### 1.1 인증 / 권한

- **세션 기반** 인증입니다. `POST /api/auth/login` 성공 시 세션에 로그인 사용자(`LoginUser{id, name, email, role}`)가 저장됩니다.
- 화이트리스트(`/login`, `/logout`, `/api/auth/login`, `/css/**`, `/js/**`, `/images/**`, `/favicon.ico`, `/h2-console/**`, `/error`)를 제외한 모든 요청은 세션이 필요합니다.
- 세션이 없는 `/api/**` 요청은 `LoginInterceptor`가 **401 `AUTHENTICATION_REQUIRED`** (JSON)로 응답합니다.
- 권한 등급: `USER`, `ADMIN`, `MANAGER`.

### 1.2 공통 에러 응답 형식

모든 `/api/**` 에러는 `ApiExceptionHandler`(`@RestControllerAdvice`)가 아래 형식으로 직렬화합니다.

```json
{
  "status": 400,
  "code": "INVALID_STATUS",
  "message": "현재 상태에서는 처리할 수 없습니다.",
  "timestamp": "2026-05-30T20:00:00"
}
```

| 필드 | 타입 | 설명 |
|---|---|---|
| `status` | int | HTTP 상태 코드 |
| `code` | string | 에러 코드(ErrorCode 이름) |
| `message` | string | 사람이 읽는 메시지(기본 메시지 또는 커스텀 메시지) |
| `timestamp` | string(datetime) | 응답 생성 시각(ISO-8601) |

예외 매핑 규칙(`ApiExceptionHandler`):

| 발생 예외 | 응답 |
|---|---|
| `BusinessException` | 해당 ErrorCode의 HTTP status + code/message |
| `MethodArgumentNotValidException` / `BindException` | `400 INVALID_INPUT` (필드 에러 메시지를 `필드: 메시지` 형태로 취합) |
| `OptimisticLockingFailureException` | `400 INVALID_STATUS` ("다른 사용자가 먼저 처리했습니다...") — 동시 승인 충돌 |
| 그 외 `Exception` | `500 INTERNAL_ERROR` |

### 1.3 페이지네이션

목록 API는 Spring `Pageable`을 사용하며 응답은 `PageResponse<T>`로 감쌉니다.

- 쿼리 파라미터: `page`(0-base, 기본 0), `size`(기본 **20**). 기본 정렬은 `createdAt,desc`로 고정됩니다(`@PageableDefault`).
- 응답 메타 필드: `content[]`, `page`, `size`, `totalElements`, `totalPages`, `first`, `last`.

```json
{
  "content": [ /* ... */ ],
  "page": 0,
  "size": 20,
  "totalElements": 1,
  "totalPages": 1,
  "first": true,
  "last": true
}
```

### 1.4 식별자 키 규약

응답 식별자는 엔티티 `id`가 아니라 도메인 접두 키를 사용합니다: `userId`, `partnerId`, `categoryId`, `itemId`, `purchaseOrderId`, `lineId`.

---

## 2. 인증 / 사용자 API

### 2.1 로그인 — `POST /api/auth/login`

| 항목 | 값 |
|---|---|
| 권한 | 전체(화이트리스트) |

Request (`LoginRequest`):

```json
{ "email": "admin@scm.com", "password": "password1!" }
```

| 필드 | 타입 | 규칙 |
|---|---|---|
| `email` | string | `@NotBlank`, `@Email` |
| `password` | string | `@NotBlank` |

Response `200` (`UserResponse`) + 세션 생성:

```json
{ "userId": 1, "email": "admin@scm.com", "name": "관리자", "role": "ADMIN" }
```

주요 에러: `INVALID_INPUT`(형식 오류 또는 이메일/비밀번호 불일치).

### 2.2 로그아웃 — `POST /api/auth/logout`

- 권한: 로그인. 세션 무효화. Response `204 No Content`.

### 2.3 내 정보 — `GET /api/users/me`

- 권한: 로그인. Response `200` (`UserResponse`, 2.1과 동일 shape).

### 2.4 비밀번호 변경 — `PATCH /api/users/me/password`

- 권한: 로그인.

Request (`PasswordChangeRequest`):

```json
{ "currentPassword": "password1!", "newPassword": "newPassword2@" }
```

| 필드 | 타입 | 규칙 |
|---|---|---|
| `currentPassword` | string | `@NotBlank` |
| `newPassword` | string | `@NotBlank`, 8~64자 |

Response `204 No Content`. 주요 에러: `INVALID_INPUT`(현재 비밀번호 불일치/형식).

---

## 3. 거래처 API

기준 경로 `/api/partners`. 등록/수정/삭제는 **ADMIN만** 가능합니다.

| Method | URL | 권한 | 성공 |
|---|---|---|---|
| POST | `/api/partners` | ADMIN | 201 `PartnerResponse` |
| GET | `/api/partners` | 로그인 | 200 `PageResponse<PartnerListView>` |
| GET | `/api/partners/{partnerId}` | 로그인 | 200 `PartnerDetailView` |
| PUT | `/api/partners/{partnerId}` | ADMIN | 200 `PartnerResponse` |
| DELETE | `/api/partners/{partnerId}` | ADMIN | 204 (status=INACTIVE 비활성화) |

### 3.1 거래처 등록 — `POST /api/partners`

Request (`PartnerCreateRequest`):

```json
{
  "name": "(주)베스트공급사",
  "businessNumber": "123-45-67890",
  "partnerType": "SUPPLIER",
  "contactName": "김공급",
  "phone": "02-1234-5678",
  "email": "contact@best.com",
  "address": "서울시 강남구 테헤란로 1"
}
```

| 필드 | 타입 | 규칙 |
|---|---|---|
| `name` | string | `@NotBlank`, max 150 |
| `businessNumber` | string | `@NotBlank`, max 20, **unique** |
| `partnerType` | enum | `@NotNull` — `SUPPLIER`/`CUSTOMER`/`BOTH` |
| `contactName` | string | max 50 |
| `phone` | string | max 30 |
| `email` | string | `@Email`, max 100 |
| `address` | string | max 255 |

Response `201` (`PartnerResponse`):

```json
{ "partnerId": 1, "name": "(주)베스트공급사", "businessNumber": "123-45-67890", "partnerType": "SUPPLIER", "status": "ACTIVE" }
```

주요 에러: `DUPLICATE_BUSINESS_NUMBER`(중복), `INVALID_INPUT`, `ACCESS_DENIED`(비 ADMIN).

### 3.2 거래처 목록 — `GET /api/partners`

- 쿼리(전부 선택): `name`(부분 일치), `businessNumber`, `partnerType`, `status` + `page`, `size`.
- Response `200` (`PageResponse<PartnerListView>`). 항목 필드: `partnerId, name, businessNumber, partnerType, contactName, phone, status`.

### 3.3 거래처 상세 — `GET /api/partners/{partnerId}`

Response `200` (`PartnerDetailView`):

```json
{
  "partnerId": 1, "name": "(주)베스트공급사", "businessNumber": "123-45-67890",
  "partnerType": "SUPPLIER", "contactName": "김공급", "phone": "02-1234-5678",
  "email": "contact@best.com", "address": "서울시 강남구 테헤란로 1", "status": "ACTIVE",
  "createdAt": "2026-05-30T09:00:00", "updatedAt": null
}
```

주요 에러: `PARTNER_NOT_FOUND`.

### 3.4 거래처 수정 — `PUT /api/partners/{partnerId}`

Request (`PartnerUpdateRequest`) — 필드는 등록과 동일하되 **`businessNumber` 없음(unique 키, 변경 불가)**.
Response `200` (`PartnerResponse`). 주요 에러: `PARTNER_NOT_FOUND`, `INVALID_INPUT`, `ACCESS_DENIED`.

### 3.5 거래처 비활성화 — `DELETE /api/partners/{partnerId}`

- 실제 삭제가 아니라 `status`를 `INACTIVE`로 변경합니다. Response `204`. 주요 에러: `PARTNER_NOT_FOUND`, `ACCESS_DENIED`.

---

## 4. 카테고리 API

기준 경로 `/api/categories`. 등록/수정/삭제는 **ADMIN만**.

| Method | URL | 권한 | 성공 |
|---|---|---|---|
| POST | `/api/categories` | ADMIN | 201 `CategoryView` |
| GET | `/api/categories` | 로그인 | 200 `List<CategoryView>` |
| GET | `/api/categories/{categoryId}` | 로그인 | 200 `CategoryDetailView` |
| PUT | `/api/categories/{categoryId}` | ADMIN | 200 `CategoryView` |
| DELETE | `/api/categories/{categoryId}` | ADMIN | 204 |

### 4.1 카테고리 등록 — `POST /api/categories`

Request (`CategoryCreateRequest`):

```json
{ "name": "전자부품", "description": "저항, 콘덴서, IC 등 전자부품" }
```

| 필드 | 타입 | 규칙 |
|---|---|---|
| `name` | string | `@NotBlank`, max 100, **unique** |
| `description` | string | max 255 |

Response `201` (`CategoryView`):

```json
{ "categoryId": 1, "name": "전자부품", "description": "저항, 콘덴서, IC 등 전자부품", "itemCount": 0, "createdAt": "2026-05-30T09:00:00", "updatedAt": null }
```

주요 에러: `DUPLICATE_CATEGORY_NAME`, `INVALID_INPUT`, `ACCESS_DENIED`.

### 4.2 카테고리 목록 — `GET /api/categories`

Response `200` (`List<CategoryView>`) — 페이징 없음, 이름 오름차순. `itemCount`는 소속 품목 수.

### 4.3 카테고리 상세 — `GET /api/categories/{categoryId}`

Response `200` (`CategoryDetailView`) — 소속 품목 목록 포함:

```json
{
  "categoryId": 1, "name": "전자부품", "description": "...",
  "createdAt": "2026-05-30T09:00:00", "updatedAt": null,
  "items": [
    { "itemId": 1, "itemCode": "ITM-001", "name": "볼트 M6", "categoryId": 1, "categoryName": "전자부품",
      "unit": "EA", "unitPrice": 150.00, "safetyStock": 100, "status": "ACTIVE" }
  ]
}
```

주요 에러: `CATEGORY_NOT_FOUND`.

### 4.4 카테고리 수정 — `PUT /api/categories/{categoryId}`

Request (`CategoryUpdateRequest`, 등록과 동일 필드). 이름 변경 시 중복 재검증. Response `200` (`CategoryView`).
주요 에러: `CATEGORY_NOT_FOUND`, `DUPLICATE_CATEGORY_NAME`, `ACCESS_DENIED`.

### 4.5 카테고리 삭제 — `DELETE /api/categories/{categoryId}`

- **소속 품목이 있으면 삭제 불가**. Response `204`. 주요 에러: `CATEGORY_HAS_ITEMS`, `CATEGORY_NOT_FOUND`, `ACCESS_DENIED`.

---

## 5. 품목 API

기준 경로 `/api/items`. 등록/수정/단종은 **ADMIN만**.

| Method | URL | 권한 | 성공 |
|---|---|---|---|
| POST | `/api/items` | ADMIN | 201 `ItemDetailView` |
| GET | `/api/items` | 로그인 | 200 `PageResponse<ItemListView>` |
| GET | `/api/items/{itemId}` | 로그인 | 200 `ItemDetailView` |
| PUT | `/api/items/{itemId}` | ADMIN | 200 `ItemDetailView` |
| PATCH | `/api/items/{itemId}/discontinue` | ADMIN | 200 `ItemDetailView` (status=DISCONTINUED) |

### 5.1 품목 등록 — `POST /api/items`

Request (`ItemCreateRequest`):

```json
{ "itemCode": "ITM-001", "name": "볼트 M6", "categoryId": 1, "unit": "EA", "unitPrice": 150.00, "safetyStock": 100 }
```

| 필드 | 타입 | 규칙 |
|---|---|---|
| `itemCode` | string | `@NotBlank`, max 50, **unique** |
| `name` | string | `@NotBlank`, max 150 |
| `categoryId` | number(Long) | `@NotNull` — 존재하는 카테고리 |
| `unit` | string | `@NotBlank`, max 20 |
| `unitPrice` | number | `@NotNull`, ≥ 0 |
| `safetyStock` | number(int) | `@NotNull`, ≥ 0 |

Response `201` (`ItemDetailView`):

```json
{
  "itemId": 1, "itemCode": "ITM-001", "name": "볼트 M6", "categoryId": 1, "categoryName": "전자부품",
  "unit": "EA", "unitPrice": 150.00, "safetyStock": 100, "status": "ACTIVE",
  "createdAt": "2026-05-30T09:00:00", "updatedAt": null
}
```

주요 에러: `DUPLICATE_ITEM_CODE`, `CATEGORY_NOT_FOUND`(categoryId 미존재), `INVALID_INPUT`, `ACCESS_DENIED`.

### 5.2 품목 목록 — `GET /api/items`

- 쿼리(전부 선택): `name`(부분 일치), `itemCode`, `categoryId`, `status` + `page`, `size`.
- Response `200` (`PageResponse<ItemListView>`). 항목 필드: `itemId, itemCode, name, categoryId, categoryName, unit, unitPrice, safetyStock, status`.

### 5.3 품목 상세 — `GET /api/items/{itemId}`

Response `200` (`ItemDetailView`, 5.1과 동일 shape). 주요 에러: `ITEM_NOT_FOUND`.

### 5.4 품목 수정 — `PUT /api/items/{itemId}`

Request (`ItemUpdateRequest`) — **`itemCode` 없음(unique 키, 변경 불가)**. 필드: `name, categoryId, unit, unitPrice, safetyStock`.
Response `200` (`ItemDetailView`). 주요 에러: `ITEM_NOT_FOUND`, `CATEGORY_NOT_FOUND`, `INVALID_INPUT`, `ACCESS_DENIED`.

### 5.5 품목 단종 — `PATCH /api/items/{itemId}/discontinue`

- `status`를 `DISCONTINUED`로 변경. Response `200` (`ItemDetailView`). 주요 에러: `ITEM_NOT_FOUND`, `ACCESS_DENIED`.

---

## 6. 발주 API (사용자 영역)

기준 경로 `/api/purchase-orders`.

| # | Method | URL | 권한 | 성공 |
|---|---|---|---|---|
| 6.1 | POST | `/api/purchase-orders` | 로그인 | 201 `PurchaseOrderCreateResponse` |
| 6.2 | PATCH | `/api/purchase-orders/{poId}/submit` | 작성자 본인 | 200 `PurchaseOrderStatusResponse` |
| 6.3 | GET | `/api/purchase-orders/my` | 로그인(본인) | 200 `PageResponse<PurchaseOrderSummaryResponse>` |
| 6.4 | GET | `/api/purchase-orders/{poId}` | 작성자 본인 또는 ADMIN/MANAGER | 200 `PurchaseOrderDetailResponse` |
| 6.5 | PATCH | `/api/purchase-orders/{poId}/cancel` | 작성자 본인 | 200 `PurchaseOrderStatusResponse` |

### 6.1 발주서 작성 — `POST /api/purchase-orders`

Request (`PurchaseOrderCreateRequest`) — **`totalAmount`/`lineAmount` 없음**(서버 계산):

```json
{
  "partnerId": 1,
  "orderDate": "2026-06-01",
  "dueDate": "2026-06-15",
  "lines": [
    { "itemId": 1, "quantity": 100, "unitPrice": 150.00 },
    { "itemId": 2, "quantity": 50,  "unitPrice": null }
  ]
}
```

| 필드 | 타입 | 규칙 |
|---|---|---|
| `partnerId` | number(Long) | `@NotNull` — 존재 · `SUPPLIER`/`BOTH` · `ACTIVE` |
| `orderDate` | string(date) | `@NotNull`, `dueDate`가 있으면 `orderDate ≤ dueDate` |
| `dueDate` | string(date) | nullable |
| `lines` | array | `@NotEmpty`(최소 1건) |
| `lines[].itemId` | number(Long) | `@NotNull` — 존재 · `ACTIVE`(단종 불가) |
| `lines[].quantity` | number(int) | `@NotNull`, `@Positive`(> 0) |
| `lines[].unitPrice` | number | nullable, ≥ 0. **null이면 품목 표준단가 적용** |

서버 처리:
- `lineAmount = quantity × unitPrice`, `totalAmount = Σ lineAmount`를 **서버에서 재계산**해 저장(클라이언트 값 미수신).
- 발주번호 `orderNumber`(`PO-YYYYMMDD-####`)를 채번하고 헤더+라인을 한 트랜잭션에 저장. 초기 status는 `DRAFT`.

Response `201` (`PurchaseOrderCreateResponse`):

```json
{ "purchaseOrderId": 1, "orderNumber": "PO-20260601-0001", "status": "DRAFT", "totalAmount": 22500.00 }
```

주요 에러: `EMPTY_ORDER_LINES`, `PARTNER_NOT_FOUND`, `PARTNER_TYPE_MISMATCH`(CUSTOMER 거래처), `INVALID_STATUS`(INACTIVE 거래처), `ITEM_NOT_FOUND`, `ITEM_DISCONTINUED`, `INVALID_DATE_RANGE`, `INVALID_INPUT`(수량 ≤ 0 / 단가 < 0), `INTERNAL_ERROR`(채번 재시도 초과), `AUTHENTICATION_REQUIRED`.

### 6.2 발주 결재 요청 — `PATCH /api/purchase-orders/{poId}/submit`

- 권한: 작성자 본인. 전이: `DRAFT → REQUESTED`. Request Body 없음.

Response `200` (`PurchaseOrderStatusResponse`):

```json
{ "purchaseOrderId": 1, "orderNumber": "PO-20260601-0001", "status": "REQUESTED", "message": "결재 요청되었습니다." }
```

주요 에러: `PURCHASE_ORDER_NOT_FOUND`(404), `ACCESS_DENIED`(403, 비작성자), `INVALID_STATUS`(400, DRAFT 아님), `AUTHENTICATION_REQUIRED`(401).

### 6.3 내 발주서 목록 — `GET /api/purchase-orders/my`

- 권한: 로그인(본인 `writerId`만 반환). 쿼리: `status`(선택, enum), `page`, `size`.
- Response `200` (`PageResponse<PurchaseOrderSummaryResponse>`):

```json
{
  "content": [
    { "purchaseOrderId": 1, "orderNumber": "PO-20260601-0001", "partnerId": 1, "partnerName": "(주)베스트공급사",
      "orderDate": "2026-06-01", "dueDate": "2026-06-15", "totalAmount": 22500.00, "status": "DRAFT",
      "createdAt": "2026-06-01T09:00:00" }
  ],
  "page": 0, "size": 20, "totalElements": 1, "totalPages": 1, "first": true, "last": true
}
```

주요 에러: `AUTHENTICATION_REQUIRED`.

### 6.4 발주서 상세 — `GET /api/purchase-orders/{poId}`

- 권한: **작성자 본인 또는 ADMIN/MANAGER**. 그 외 `ACCESS_DENIED`.
- Response `200` (`PurchaseOrderDetailResponse`) — 헤더 + 라인:

```json
{
  "purchaseOrderId": 1,
  "orderNumber": "PO-20260601-0001",
  "partnerId": 1, "partnerName": "(주)베스트공급사",
  "writerId": 3, "writerName": "홍길동",
  "approverId": null, "approverName": null,
  "orderDate": "2026-06-01", "dueDate": "2026-06-15",
  "totalAmount": 22500.00,
  "status": "DRAFT",
  "rejectReason": null,
  "createdAt": "2026-06-01T09:00:00", "updatedAt": null,
  "approvedAt": null, "receivedAt": null,
  "lines": [
    { "lineId": 1, "itemId": 1, "itemCode": "ITM-001", "itemName": "볼트 M6", "quantity": 100, "unitPrice": 150.00, "lineAmount": 15000.00 },
    { "lineId": 2, "itemId": 2, "itemCode": "ITM-002", "itemName": "너트 M6", "quantity": 50,  "unitPrice": 90.00,  "lineAmount": 4500.00 }
  ]
}
```

주요 에러: `PURCHASE_ORDER_NOT_FOUND`(404), `ACCESS_DENIED`(403), `AUTHENTICATION_REQUIRED`(401).

### 6.5 발주서 취소 — `PATCH /api/purchase-orders/{poId}/cancel`

- 권한: 작성자 본인. 취소 가능 상태 = **`{DRAFT, REQUESTED, APPROVED}`**. 사유 없음(Request Body 없음).
- 전이: `→ CANCELED`. `RECEIVED`(및 종료 상태)는 취소 불가.

Response `200` (`PurchaseOrderStatusResponse`):

```json
{ "purchaseOrderId": 1, "orderNumber": "PO-20260601-0001", "status": "CANCELED", "message": "발주가 취소되었습니다." }
```

주요 에러: `PURCHASE_ORDER_NOT_FOUND`(404), `ACCESS_DENIED`(403, 비작성자), `INVALID_STATUS`(400, RECEIVED/종료 상태), `AUTHENTICATION_REQUIRED`(401).

---

## 7. 발주 API (관리자/매니저 영역)

기준 경로 `/api/admin/purchase-orders`. **모든 엔드포인트 권한 = ADMIN + MANAGER**.

| # | Method | URL | 성공 |
|---|---|---|---|
| 7.1 | GET | `/api/admin/purchase-orders` | 200 `PageResponse<PurchaseOrderSummaryResponse>` |
| 7.2 | PATCH | `/api/admin/purchase-orders/{poId}/approve` | 200 `PurchaseOrderStatusResponse` |
| 7.3 | PATCH | `/api/admin/purchase-orders/{poId}/reject` | 200 `PurchaseOrderStatusResponse` |
| 7.4 | PATCH | `/api/admin/purchase-orders/{poId}/receive` | 200 `PurchaseOrderStatusResponse` |

### 7.1 관리자 발주 목록 — `GET /api/admin/purchase-orders`

- 쿼리(전부 선택): `status`(enum), `partnerId`(Long) + `page`, `size`.
- Response `200` (`PageResponse<PurchaseOrderSummaryResponse>`, 6.3과 동일 구조이나 전체 발주 대상).
- 주요 에러: `ACCESS_DENIED`(403, USER), `AUTHENTICATION_REQUIRED`(401).

### 7.2 발주 승인 — `PATCH /api/admin/purchase-orders/{poId}/approve`

- 전이: `REQUESTED → APPROVED`. 부수효과: `approverId = 현재 사용자`, `approvedAt = now`. Request Body 없음.
- 동시 승인은 `@Version` 낙관적 락으로 방지 → 충돌 시 `INVALID_STATUS`.

```json
{ "purchaseOrderId": 1, "orderNumber": "PO-20260601-0001", "status": "APPROVED", "message": "발주가 승인되었습니다." }
```

주요 에러: `PURCHASE_ORDER_NOT_FOUND`(404), `ACCESS_DENIED`(403), `INVALID_STATUS`(400, REQUESTED 아님 또는 동시 처리 충돌), `AUTHENTICATION_REQUIRED`(401).

### 7.3 발주 반려 — `PATCH /api/admin/purchase-orders/{poId}/reject`

- 전이: `REQUESTED → REJECTED`. 부수효과: `rejectReason` 저장, `approverId` 기록.

Request (`PurchaseOrderRejectRequest`):

```json
{ "rejectReason": "예산 초과로 반려합니다." }
```

| 필드 | 타입 | 규칙 |
|---|---|---|
| `rejectReason` | string | `@NotBlank`(공란 불가), max 500 |

```json
{ "purchaseOrderId": 1, "orderNumber": "PO-20260601-0001", "status": "REJECTED", "message": "발주가 반려되었습니다." }
```

주요 에러: `PURCHASE_ORDER_NOT_FOUND`(404), `ACCESS_DENIED`(403), `INVALID_INPUT`(400, 사유 공란), `INVALID_STATUS`(400, REQUESTED 아님), `AUTHENTICATION_REQUIRED`(401).

### 7.4 입고 처리 — `PATCH /api/admin/purchase-orders/{poId}/receive`

- 전이: `APPROVED → RECEIVED`. 부수효과: `receivedAt = now`, **라인별 재고(Stock) 증가**(없으면 itemId 기준 생성). Request Body 없음.
- 상태 전이 + 모든 라인 재고 증가가 **하나의 트랜잭션**으로 처리되어 원자성이 보장됩니다(일부 실패 시 전체 롤백). `RECEIVED` 상태는 재진입 불가하여 이중 증가가 방지됩니다.

```json
{ "purchaseOrderId": 1, "orderNumber": "PO-20260601-0001", "status": "RECEIVED", "message": "입고 처리되었습니다." }
```

주요 에러: `PURCHASE_ORDER_NOT_FOUND`(404), `ACCESS_DENIED`(403), `INVALID_STATUS`(400, APPROVED 아님 또는 락 충돌), `AUTHENTICATION_REQUIRED`(401).

---

## 8. 에러 코드 표

`ErrorCode` enum(`common.exception`)에 정의된 코드입니다. 메시지는 기본값이며, 일부 케이스는 Service가 커스텀 메시지로 덮어씁니다.

| 코드 | HTTP | 기본 메시지 | 주요 발생 지점 |
|---|---|---|---|
| `INVALID_INPUT` | 400 | 입력값 검증에 실패했습니다. | 필수값 누락/형식 오류, 수량 ≤ 0, 단가 < 0, 반려 사유 공란 |
| `DUPLICATE_EMAIL` | 400 | 이미 사용 중인 이메일입니다. | (사용자 등록 — 1차 미사용 화면) |
| `DUPLICATE_BUSINESS_NUMBER` | 400 | 이미 등록된 사업자번호입니다. | 거래처 등록 |
| `DUPLICATE_CATEGORY_NAME` | 400 | 이미 사용 중인 카테고리명입니다. | 카테고리 등록/수정 |
| `DUPLICATE_ITEM_CODE` | 400 | 이미 등록된 품목코드입니다. | 품목 등록 |
| `CATEGORY_HAS_ITEMS` | 400 | 소속 품목이 있는 카테고리는 삭제할 수 없습니다. | 카테고리 삭제 |
| `PARTNER_TYPE_MISMATCH` | 400 | 거래처 유형이 일치하지 않습니다. | CUSTOMER 거래처로 발주 |
| `EMPTY_ORDER_LINES` | 400 | 주문 라인이 비어 있습니다. | 발주 작성 시 라인 0건 |
| `INVALID_DATE_RANGE` | 400 | 잘못된 날짜 범위입니다. | 발주일 > 납기일 |
| `INVALID_STATUS` | 400 | 현재 상태에서는 처리할 수 없습니다. | 잘못된 상태 전이 / INACTIVE 거래처 / 낙관적 락 충돌 |
| `ITEM_DISCONTINUED` | 400 | 단종된 품목은 사용할 수 없습니다. | 단종 품목 라인 포함 |
| `AUTHENTICATION_REQUIRED` | 401 | 로그인이 필요합니다. | 미로그인 `/api/**` 요청 |
| `ACCESS_DENIED` | 403 | 접근 권한이 없습니다. | 작성자 아님 / 권한 등급 부족 |
| `USER_NOT_FOUND` | 404 | 사용자를 찾을 수 없습니다. | 사용자 조회 실패 |
| `PARTNER_NOT_FOUND` | 404 | 거래처를 찾을 수 없습니다. | partnerId 미존재 |
| `CATEGORY_NOT_FOUND` | 404 | 카테고리를 찾을 수 없습니다. | categoryId 미존재 |
| `ITEM_NOT_FOUND` | 404 | 품목을 찾을 수 없습니다. | itemId 미존재 |
| `PURCHASE_ORDER_NOT_FOUND` | 404 | 발주서를 찾을 수 없습니다. | poId 미존재 |
| `SALES_ORDER_NOT_FOUND` | 404 | 수주서를 찾을 수 없습니다. | (수주 모듈 — 미구현) |
| `NOTICE_NOT_FOUND` | 404 | 공지사항을 찾을 수 없습니다. | (공지 모듈 — 미구현) |
| `INTERNAL_ERROR` | 500 | 서버 내부 오류가 발생했습니다. | 채번 재시도 초과, 미처리 예외 |

> `SALES_ORDER_NOT_FOUND`, `NOTICE_NOT_FOUND`, `DUPLICATE_EMAIL`은 enum에 정의되어 있으나 수주/공지/사용자 등록 화면이 미구현이라 현재 사용되지 않습니다.

> 발주 상태 전이의 상세 규칙은 [`STATE_MACHINE.md`](STATE_MACHINE.md)를, 엔티티/테이블 구조는 [`ERD.md`](ERD.md)를 참고하세요.
