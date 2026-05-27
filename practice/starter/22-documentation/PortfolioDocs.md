# 포트폴리오 문서화 워크북

TRD 3.15 ~ 3.17 에서 정의한 문서 묶음을 직접 채워봅니다. 이 워크북을 다 채우면 GitHub 저장소에 그대로 옮겨 README/ERD/API/트러블슈팅 문서 초안이 됩니다.

---

## 1. README.md 8 섹션 채우기

빈칸을 채워 보세요.

```md
# Supply Chain Management System

Java Spring Boot 기반 공급망 관리 시스템.
거래처 / 품목 / 카테고리 / 발주 / 수주 / 공지 기능을 구현한 SI 취업용 포트폴리오 프로젝트입니다.

## 1. 프로젝트 소개
- 한 줄 소개:
- 만든 기간 (예: 2026-02 ~ 2026-04):
- 1인 / 팀 여부:

## 2. 개발 목적
- ____
- ____
- ____

## 3. 사용 기술
| 구분 | 기술 |
|---|---|
| Language | Java ____ |
| Framework | Spring Boot ____ |
| DB | ____ / ____ |
| ORM | ____ |
| View | Thymeleaf |
| Build | ____ |
| Test | JUnit 5, ____ |
| Version Control | Git / GitHub |

## 4. 주요 기능
- 로그인 / 권한 구분 (USER / ADMIN / MANAGER)
- 거래처 관리 (CRUD, 검색, 페이징, 사업자번호 unique)
- 카테고리 / 품목 관리 (CRUD, 검색, 페이징, 단종 처리)
- 발주(구매) 작성/요청/승인/반려/입고 (DRAFT → REQUESTED → APPROVED → RECEIVED)
- 수주(판매) 작성/확정/출고/완료/취소 (DRAFT → CONFIRMED → SHIPPED → COMPLETED)
- 공지사항 관리

## 5. 시스템 구조
Browser → Controller → Service → Repository → Database

## 6. ERD
docs/ERD.md 참고.

## 7. API 명세
docs/API_SPEC.md 참고.

## 8. 실행 방법
- ./gradlew bootRun
- 기본 포트: ____
- H2 콘솔: http://localhost:____/h2-console
```

---

## 2. ERD 표기 선택

| 도구 | 장점 | 단점 |
|---|---|---|
| 텍스트 트리 (현재 TRD 3.4 방식) | 빠르게 갱신, diff 가능 | 시각적 구조 파악이 어려움 |
| dbdiagram.io | 무료, DSL 로 작성, 이미지 export | 별도 사이트 |
| draw.io | 자유도 높음 | ____ |
| ERD Plus / Lucidchart | 협업에 강함 | 유료 |

선택 기준:

- [ ] 처음에는 **____** 로 빠르게 시작
- [ ] 면접 직전 자료는 **____** 로 깔끔하게 다시 그림

### 텍스트 트리 직접 채우기

```text
User      1 : N  PurchaseOrder(____)        ← 작성자
User      1 : N  PurchaseOrder(____)        ← 승인자
User      1 : N  SalesOrder(____)           ← 작성자
User      1 : N  SalesOrder(____)           ← 처리자(매니저)
User      1 : N  Notice
Category  1 : N  Item
Partner   1 : N  PurchaseOrder              ← 공급사
Partner   1 : N  SalesOrder                 ← 고객사
PurchaseOrder 1 : N PurchaseOrderLine
SalesOrder    1 : N SalesOrderLine
Item      1 : N  PurchaseOrderLine
Item      1 : N  SalesOrderLine
```

---

## 3. 트러블슈팅 4단계 템플릿

TRD 3.16 에서 정의한 형식대로 직접 한 건을 채워 보세요.

### A. 발주 승인 상태 중복 처리

```md
### 발주 승인 상태 중복 처리 문제

#### 문제
이미 승인된 발주를 다시 ____ 또는 ____ 할 수 있는 버그가 발생했다.

#### 원인
승인/반려 처리 전에 현재 ____ 가 REQUESTED 인지 검증하지 않았다.

#### 해결
- Service 가 아닌 ____ 의 메서드(`approve`, `reject`)에서 REQUESTED 검증을 강제했다.
- 새 통합 테스트로 "이미 APPROVED 인 발주를 다시 승인 시도 → INVALID_STATUS" 시나리오를 추가했다.

#### 배운 점
승인/반려 같은 상태 머신은 _______(엔티티 / 서비스) 안에서 상태 전이를 캡슐화하면,
호출부에서 검증을 잊는 실수를 막을 수 있다.
```

### B. 발주 헤더-라인 부분 저장

```md
### 발주 헤더는 저장되었는데 라인이 일부만 저장되는 문제

#### 문제
발주서 작성 중 라인 저장 단계에서 예외가 발생하면 헤더만 남는 경우가 있었다.

#### 원인
Service 메서드에 ____ 가 빠져 있었고, cascade 설정도 ____ 만 두어 라인이 자동 저장되지 않았다.

#### 해결
- Service 메서드에 `@____` 적용.
- `@OneToMany(cascade = CascadeType.____, orphanRemoval = true)` 로 헤더 저장과 라인 저장을 묶음.
- 라인 검증을 헤더 save 호출 ____ 에 끝내도록 순서 정리.

#### 배운 점
헤더-라인 도메인은 ____ 한 트랜잭션에서 끝내야 하며, 검증을 헤더 저장 전에 완료해야 안전하다.
```

---

## 4. API 명세 작성 방법

### A. 손으로 적기 (Markdown)

장점:
- diff 친화적, 코드와 함께 PR 리뷰 가능.
- 신입 단계에서 어떤 필드가 어떤 의미인지 직접 생각하게 된다.

단점:
- 실제 코드와 어긋날 수 있다.

### B. 자동 생성 (springdoc-openapi)

`build.gradle` 에 의존성 추가만으로 `/swagger-ui.html` 이 뜬다.

```groovy
implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:____'
```

선택 기준:

- [ ] 학습 1~2주차: ____ 로 시작
- [ ] 학습 3~4주차: ____ 로 자동 생성하고, 손으로 적은 명세는 차이점만 유지

### 발주 API 명세 표 직접 채우기

| Method | URL | 권한 | 요청 본문 핵심 | 응답 핵심 |
|---|---|---|---|---|
| POST | /api/purchase-orders | ____ | partnerId, lines[] | purchaseOrderId, orderNumber, status |
| PATCH | /api/purchase-orders/{poId}/submit | 작성자 | (없음) | status=____ |
| GET | /api/purchase-orders/my | USER | page, size | Page<PurchaseOrderResponse> |
| GET | /api/purchase-orders/{poId} | 본인/ADMIN | (없음) | 헤더 + lines[] |
| PATCH | /api/admin/purchase-orders/{poId}/approve | ____ | (없음) | status=____ |
| PATCH | /api/admin/purchase-orders/{poId}/reject | ADMIN | reason | status=REJECTED |
| PATCH | /api/admin/purchase-orders/{poId}/receive | ADMIN | (없음) | status=____ |

---

## 5. docs 폴더 권장 구조 채우기

```text
docs/
├── ____.md             # 제품 요구사항 (현재 PRD/TRD 통합 문서를 분리해도 됨)
├── TRD.md
├── ____.md             # 데이터베이스 모델 다이어그램
├── API_SPEC.md
├── ____.md             # 트러블슈팅 모음 (TRD 3.16 의 5가지 + 본인이 추가한 항목)
└── ____.md             # 화면 캡처 / 데모 영상 링크
```

---

## 6. 면접 답변 카드 (TRD 3.19 응용)

면접관이 물어볼 12가지 질문(TRD 3.19) 중 5개를 골라, 30초 분량의 답변을 한 문단으로 적어 보세요.

| # | 질문 요지 | 30초 답변 (직접 작성) |
|---|---|---|
| 1 | 왜 이 프로젝트를 만들었나? |  |
| 2 | User 와 Partner 를 왜 분리했나? |  |
| 3 | @Transactional 을 어디에, 왜? |  |
| 4 | 헤더-라인 구조에서 가장 중요한 검증은? |  |
| 5 | Entity 를 직접 응답하지 않고 DTO 를 쓴 이유? |  |
| 6 | 가장 어려웠던 문제와 해결 방법? |  |

---

## 7. 셀프 체크리스트

- [ ] README 가 8 섹션을 모두 갖추고 있는가?
- [ ] ERD 가 TRD 3.4 의 관계(User/Partner/Category/Item/PurchaseOrder/SalesOrder/Notice)를 빠짐없이 표현하는가?
- [ ] API 명세에 인증 헤더 형식이 명시되어 있는가?
- [ ] 트러블슈팅 문서가 최소 5건 이상 있는가? (PRD 3.16 기준)
- [ ] 실행 방법(`./gradlew bootRun`)이 클론한 사람이 그대로 따라 할 수 있을 만큼 구체적인가?
- [ ] 화면 캡처 또는 데모 영상이 README 에 1개 이상 있는가?
- [ ] 발주/수주 상태 전이 다이어그램이 docs/ 또는 README 에 포함되어 있는가?
