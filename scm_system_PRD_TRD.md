# 공급망 관리 시스템(SCM) PRD / TRD

> 문서 목적: SI 취업용 포트폴리오 프로젝트인 **공급망 관리 시스템(Supply Chain Management System)** 의 제품 요구사항(PRD)과 기술 요구사항(TRD)을 정리한다.
> 프로젝트 유형: Java Spring Boot 기반 공급망/유통/구매-판매 시스템
> 주요 기능: 거래처 관리, 품목 관리, 카테고리 관리, 발주(구매) 신청/승인, 수주(판매) 작성/확정, 공지사항, 관리자 기능
> 작성 기준: 신입/주니어 SI 개발자 포트폴리오 제출용

---

# 1. 문서 개요

## 1.1 문서명

공급망 관리 시스템 PRD / TRD

## 1.2 프로젝트명

Supply Chain Management System

## 1.3 저장소명 예시

```text
scm-system
supply-chain-system
purchase-sales-system
```

## 1.4 문서 작성 목적

이 문서는 공급망 관리 시스템을 개발하기 전에 필요한 기능 요구사항과 기술 설계 내용을 정리하기 위한 문서다.

포트폴리오 관점에서는 아래 역량을 보여주는 것을 목표로 한다.

- 유통/구매/판매 도메인 요구사항 분석 능력
- 마스터 데이터(거래처/품목)와 트랜잭션 데이터(발주/수주)의 관계 설계 능력
- Spring Boot 기반 CRUD + 헤더-라인 도메인 구현 능력
- 발주 승인/수주 확정 같은 다단계 상태 전이 프로세스 구현 능력
- Controller, Service, Repository 계층 분리 능력
- 일반 사용자/관리자/매니저 권한 구분 능력
- API 문서화 및 GitHub README 정리 능력

---

# 2. PRD: Product Requirements Document

---

## 2.1 프로젝트 배경

제조/유통 기업에서는 거래처(공급사/고객사) 정보, 품목 정보, 발주(구매) 요청, 수주(판매) 요청, 공지사항 등을 관리해야 한다.

실제 SI 업무에서는 이런 형태의 SCM, ERP, 유통관리 시스템, 구매-판매 관리 시스템을 자주 개발하거나 유지보수한다.

이 프로젝트는 신입 개발자가 공급망 업무 흐름을 이해하고, 실무형 포트폴리오를 만들기 위해 기획한 SCM 시스템이다.

---

## 2.2 프로젝트 목표

### 2.2.1 기능적 목표

- 거래처(공급사/고객사) 정보를 등록, 조회, 수정, 삭제할 수 있다.
- 품목을 등록하고 카테고리에 연결할 수 있다.
- 사용자가 발주서(구매)를 작성하고 결재 요청할 수 있다.
- 관리자는 발주서를 승인 또는 반려할 수 있다.
- 사용자가 수주서(판매)를 작성하고 확정할 수 있다.
- 매니저는 수주서를 처리하고 출고 상태로 변경할 수 있다.
- 공지사항을 등록하고 조회할 수 있다.
- 사용자 권한에 따라 접근 가능한 기능을 구분한다.

### 2.2.2 포트폴리오 목표

- 단순 게시판보다 실무에 가까운 공급망 업무 흐름을 구현한다.
- 거래처, 품목, 카테고리, 발주, 수주의 관계를 DB로 설계한다.
- 헤더-라인 구조(발주서/수주서 + 라인), 다단계 상태 전이, 검색, 페이징, 예외처리, 트랜잭션을 보여준다.
- README, ERD, API 명세, 트러블슈팅 문서를 포함한다.

---

## 2.3 사용자 유형

| 사용자 | 설명 | 주요 기능 |
|---|---|---|
| 일반 사용자 | 회사 내부에서 발주/수주를 작성하는 사용자 | 발주 신청, 수주 작성, 공지 조회, 품목 조회 |
| 관리자 | 마스터 데이터 및 발주 승인을 담당하는 사용자 | 거래처/품목/카테고리 관리, 발주 승인, 공지 등록 |
| 매니저 | 수주 확정/출고를 담당하는 사용자 | 수주 확정, 수주 처리, 발주 승인 |
| 시스템 관리자 | 전체 시스템 관리 담당자 | 사용자 권한 관리, 전체 데이터 조회 |

---

## 2.4 핵심 업무 흐름

### 2.4.1 거래처 관리 흐름

```text
관리자 로그인
→ 거래처 등록 (공급사 / 고객사 / 양쪽)
→ 거래처 목록 조회
→ 거래처 정보 수정
→ 비활성 처리
```

### 2.4.2 품목 관리 흐름

```text
관리자 로그인
→ 카테고리 등록
→ 품목 등록 (카테고리 배정, 단가/단위/안전재고)
→ 품목 목록 조회
→ 품목 정보 수정
→ 단종 처리
```

### 2.4.3 발주(구매) 흐름

```text
사용자 발주서 작성 (공급사 + 품목 + 수량)
→ 결재 요청 (DRAFT → REQUESTED)
→ 관리자 검토
→ 승인 또는 반려 (REQUESTED → APPROVED / REJECTED)
→ 입고 처리 (APPROVED → RECEIVED)
```

### 2.4.4 수주(판매) 흐름

```text
사용자 수주서 작성 (고객사 + 품목 + 수량)
→ 수주 확정 요청 (DRAFT → CONFIRMED)
→ 매니저 처리
→ 출고 (CONFIRMED → SHIPPED)
→ 완료 (SHIPPED → COMPLETED)
```

### 2.4.5 공지사항 흐름

```text
관리자 공지사항 작성
→ 사용자 공지사항 목록 조회
→ 공지사항 상세 조회
```

---

## 2.5 기능 요구사항

### 2.5.1 회원/로그인 기능

| ID | 기능 | 설명 | 우선순위 |
|---|---|---|---|
| FR-USER-001 | 로그인 | 이메일과 비밀번호로 로그인한다 | 필수 |
| FR-USER-002 | 로그아웃 | 로그인 세션을 종료한다 | 필수 |
| FR-USER-003 | 내 정보 조회 | 로그인한 사용자의 정보를 조회한다 | 필수 |
| FR-USER-004 | 비밀번호 변경 | 사용자가 본인 비밀번호를 변경한다 | 선택 |
| FR-USER-005 | 권한 구분 | USER, ADMIN, MANAGER 권한을 구분한다 | 필수 |

### 2.5.2 거래처 관리 기능

| ID | 기능 | 설명 | 우선순위 |
|---|---|---|---|
| FR-PARTNER-001 | 거래처 등록 | 관리자가 거래처를 등록한다 | 필수 |
| FR-PARTNER-002 | 거래처 목록 조회 | 거래처 목록을 조회한다 | 필수 |
| FR-PARTNER-003 | 거래처 상세 조회 | 특정 거래처의 상세 정보를 조회한다 | 필수 |
| FR-PARTNER-004 | 거래처 정보 수정 | 거래처 이름, 담당자, 유형 등을 수정한다 | 필수 |
| FR-PARTNER-005 | 거래처 삭제/비활성화 | 거래처를 삭제하거나 비활성 처리한다 | 선택 |
| FR-PARTNER-006 | 거래처 검색 | 이름, 사업자번호, 유형 기준으로 검색한다 | 필수 |
| FR-PARTNER-007 | 페이징 | 거래처 목록을 페이지 단위로 조회한다 | 필수 |

### 2.5.3 카테고리 관리 기능

| ID | 기능 | 설명 | 우선순위 |
|---|---|---|---|
| FR-CAT-001 | 카테고리 등록 | 관리자가 카테고리를 등록한다 | 필수 |
| FR-CAT-002 | 카테고리 목록 조회 | 전체 카테고리 목록을 조회한다 | 필수 |
| FR-CAT-003 | 카테고리 상세 조회 | 카테고리 정보와 소속 품목을 조회한다 | 필수 |
| FR-CAT-004 | 카테고리 정보 수정 | 카테고리명, 설명 등을 수정한다 | 필수 |
| FR-CAT-005 | 카테고리 삭제 | 사용하지 않는 카테고리를 삭제한다 | 선택 |

### 2.5.4 품목 관리 기능

| ID | 기능 | 설명 | 우선순위 |
|---|---|---|---|
| FR-ITEM-001 | 품목 등록 | 관리자가 품목을 등록한다 | 필수 |
| FR-ITEM-002 | 품목 목록 조회 | 품목 목록을 조회한다 | 필수 |
| FR-ITEM-003 | 품목 상세 조회 | 특정 품목의 상세 정보를 조회한다 | 필수 |
| FR-ITEM-004 | 품목 정보 수정 | 품목명, 카테고리, 단가 등을 수정한다 | 필수 |
| FR-ITEM-005 | 품목 단종 처리 | 품목을 DISCONTINUED 상태로 변경한다 | 선택 |
| FR-ITEM-006 | 품목 검색 | 품목명, 품목코드, 카테고리 기준으로 검색한다 | 필수 |
| FR-ITEM-007 | 페이징 | 품목 목록을 페이지 단위로 조회한다 | 필수 |

### 2.5.5 발주(구매) 기능

| ID | 기능 | 설명 | 우선순위 |
|---|---|---|---|
| FR-PO-001 | 발주서 작성 | 사용자가 발주서를 작성한다 (DRAFT) | 필수 |
| FR-PO-002 | 발주 요청 | 작성한 발주서를 결재 요청한다 (DRAFT → REQUESTED) | 필수 |
| FR-PO-003 | 내 발주서 목록 | 본인이 작성한 발주서 목록을 조회한다 | 필수 |
| FR-PO-004 | 발주서 상세 조회 | 발주서 헤더와 라인 정보를 함께 조회한다 | 필수 |
| FR-PO-005 | 발주서 취소 | DRAFT/REQUESTED 상태의 발주서를 취소한다 | 선택 |
| FR-PO-006 | 발주 승인 | 관리자가 발주를 승인한다 (REQUESTED → APPROVED) | 필수 |
| FR-PO-007 | 발주 반려 | 관리자가 발주를 반려한다 (REQUESTED → REJECTED) | 필수 |
| FR-PO-008 | 입고 처리 | 승인된 발주서를 입고 완료 처리한다 (APPROVED → RECEIVED) | 필수 |
| FR-PO-009 | 관리자 발주 목록 | 관리자가 전체 발주서를 상태/거래처 조건으로 조회한다 | 필수 |

### 2.5.6 공지사항 기능

| ID | 기능 | 설명 | 우선순위 |
|---|---|---|---|
| FR-NOTICE-001 | 공지 등록 | 관리자가 공지사항을 등록한다 | 필수 |
| FR-NOTICE-002 | 공지 목록 조회 | 사용자가 공지사항 목록을 조회한다 | 필수 |
| FR-NOTICE-003 | 공지 상세 조회 | 공지사항 상세 내용을 조회한다 | 필수 |
| FR-NOTICE-004 | 공지 수정 | 관리자가 공지 내용을 수정한다 | 필수 |
| FR-NOTICE-005 | 공지 삭제 | 관리자가 공지를 삭제한다 | 선택 |
| FR-NOTICE-006 | 중요 공지 표시 | 중요 공지를 상단에 표시한다 | 선택 |

### 2.5.7 수주(판매) 기능

| ID | 기능 | 설명 | 우선순위 |
|---|---|---|---|
| FR-SO-001 | 수주서 작성 | 사용자가 수주서를 작성한다 (DRAFT) | 필수 |
| FR-SO-002 | 수주 확정 | 작성한 수주서를 확정한다 (DRAFT → CONFIRMED) | 필수 |
| FR-SO-003 | 내 수주서 목록 | 본인이 작성한 수주서 목록을 조회한다 | 필수 |
| FR-SO-004 | 처리 대기 수주서 목록 | 매니저가 처리해야 할 수주서 목록을 조회한다 | 필수 |
| FR-SO-005 | 수주서 상세 조회 | 수주서 헤더와 라인 정보를 함께 조회한다 | 필수 |
| FR-SO-006 | 수주 출고 | 매니저가 수주서를 출고 처리한다 (CONFIRMED → SHIPPED) | 필수 |
| FR-SO-007 | 수주 완료 | 매니저가 수주서를 완료 처리한다 (SHIPPED → COMPLETED) | 필수 |
| FR-SO-008 | 수주 취소 | 매니저가 수주서를 취소한다 (→ CANCELED) | 필수 |
| FR-SO-009 | 수주 상태 조회 | DRAFT, CONFIRMED, SHIPPED, COMPLETED, CANCELED 상태를 조회한다 | 필수 |

---

## 2.6 비기능 요구사항

| 구분 | 요구사항 |
|---|---|
| 성능 | 목록 조회는 페이징을 적용한다 |
| 보안 | 비밀번호는 평문 저장하지 않는다 |
| 권한 | 일반 사용자, 관리자, 매니저 접근 권한을 구분한다 |
| 유지보수 | Controller, Service, Repository 계층을 분리한다 |
| 검증 | 필수 입력값과 잘못된 요청값을 검증한다 |
| 예외처리 | 공통 예외 응답 형식을 사용한다 |
| 데이터 정합성 | 발주 헤더-라인 동시 저장, 상태 변경 시 트랜잭션을 적용한다 |
| 동시성 | 같은 발주를 두 관리자가 동시에 승인하지 못하도록 막는다 |
| 문서화 | README, ERD, API 명세를 작성한다 |

---

## 2.7 화면 요구사항

### 2.7.1 공통 화면

| 화면 | 설명 |
|---|---|
| 로그인 화면 | 이메일, 비밀번호 입력 |
| 메인 대시보드 | 거래처 수, 발주 대기 수, 수주 대기 수, 공지사항 요약 |
| 내 정보 화면 | 로그인 사용자 정보 확인 |

### 2.7.2 거래처 관리 화면

| 화면 | 설명 |
|---|---|
| 거래처 목록 화면 | 거래처 목록, 검색, 페이징 |
| 거래처 등록 화면 | 신규 거래처 등록 |
| 거래처 상세 화면 | 거래처 상세 정보 확인 |
| 거래처 수정 화면 | 거래처 정보 수정 |

### 2.7.3 카테고리 관리 화면

| 화면 | 설명 |
|---|---|
| 카테고리 목록 화면 | 전체 카테고리 목록 조회 |
| 카테고리 등록 화면 | 신규 카테고리 등록 |
| 카테고리 상세 화면 | 카테고리 정보와 소속 품목 조회 |

### 2.7.4 품목 관리 화면

| 화면 | 설명 |
|---|---|
| 품목 목록 화면 | 품목 목록, 검색, 페이징 |
| 품목 등록 화면 | 신규 품목 등록 |
| 품목 상세 화면 | 품목 상세 정보 확인 |
| 품목 수정 화면 | 품목 정보 수정 |

### 2.7.5 발주 관리 화면

| 화면 | 설명 |
|---|---|
| 발주서 작성 화면 | 공급사 선택, 품목 라인 추가, 수량/단가 입력 |
| 내 발주서 목록 화면 | 본인 발주 신청 내역 |
| 발주서 상세 화면 | 발주 헤더 + 라인 + 상태 이력 |
| 발주 승인 관리 화면 | 관리자가 발주 승인/반려 처리 |

### 2.7.6 공지사항 화면

| 화면 | 설명 |
|---|---|
| 공지 목록 화면 | 공지사항 목록 조회 |
| 공지 상세 화면 | 공지사항 상세 조회 |
| 공지 등록 화면 | 관리자가 공지 등록 |

### 2.7.7 수주 관리 화면

| 화면 | 설명 |
|---|---|
| 수주서 작성 화면 | 고객사 선택, 품목 라인 추가 |
| 내 수주서 목록 화면 | 본인이 작성한 수주서 목록 |
| 처리 대기 수주서 화면 | 매니저가 처리해야 할 수주서 |
| 수주 처리 화면 | 매니저가 출고/완료/취소 처리 |

---

## 2.8 MVP 범위

### 2.8.1 1차 MVP 필수 기능

```text
1. 로그인
2. 거래처 등록/조회/수정
3. 카테고리 등록/조회
4. 품목 등록/조회/수정
5. 발주서 작성
6. 발주 요청/승인/반려
7. 공지사항 CRUD
8. 일반/관리자/매니저 권한 구분
```

### 2.8.2 2차 확장 기능

```text
1. 수주서 작성/확정/출고
2. 입고 처리 후 재고 반영
3. 검색 조건 고도화 (카테고리 + 거래처 복합 조건)
4. 첨부파일 (계약서/거래명세표)
5. 댓글/요청사항 메모
6. 발주/수주 통계 대시보드
7. Docker 배포
8. Spring Security / JWT 적용
```

---

# 3. TRD: Technical Requirements Document

---

## 3.1 기술 스택

### 3.1.1 기본 추천 스택

| 구분 | 기술 |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot |
| View | Thymeleaf |
| Database | H2, MySQL |
| ORM | Spring Data JPA |
| Build | Gradle |
| Test | JUnit 5 |
| Version Control | Git / GitHub |
| API Test | Postman, curl |
| Deploy | Render, Railway, AWS, Docker 중 선택 |

### 3.1.2 SI 친화 스택

| 구분 | 기술 |
|---|---|
| Language | Java |
| Framework | Spring Boot, Spring MVC |
| View | JSP 또는 Thymeleaf |
| Database | Oracle 또는 MySQL |
| Persistence | MyBatis 또는 JPA |
| Build | Maven 또는 Gradle |
| Server | Tomcat |
| OS | Linux |

포트폴리오 초보자에게는 **Spring Boot + Thymeleaf + JPA + H2/MySQL** 조합을 추천한다.

---

## 3.2 시스템 아키텍처

### 3.2.1 계층 구조

```text
Browser
  ↓
Controller
  ↓
Service
  ↓
Repository
  ↓
Database
```

### 3.2.2 패키지 구조

```text
scm-system
└── src
    └── main
        ├── java
        │   └── com.example.scm
        │       ├── controller
        │       ├── service
        │       ├── repository
        │       ├── domain
        │       ├── dto
        │       ├── config
        │       └── exception
        └── resources
            ├── templates
            ├── static
            └── application.yml
```

---

## 3.3 주요 도메인 설계

### 3.3.1 User

시스템 로그인 계정 정보를 관리한다.

| 필드 | 타입 | 설명 |
|---|---|---|
| id | Long | 사용자 PK |
| email | String | 로그인 이메일 |
| password | String | 비밀번호 |
| name | String | 사용자 이름 |
| role | UserRole | USER, ADMIN, MANAGER |
| createdAt | LocalDateTime | 생성일 |
| updatedAt | LocalDateTime | 수정일 |

### 3.3.2 Partner (거래처)

공급사/고객사 정보를 관리한다.

| 필드 | 타입 | 설명 |
|---|---|---|
| id | Long | 거래처 PK |
| name | String | 거래처명 |
| businessNumber | String | 사업자번호 (unique) |
| partnerType | PartnerType | SUPPLIER, CUSTOMER, BOTH |
| contactName | String | 담당자명 |
| phone | String | 연락처 |
| email | String | 이메일 |
| address | String | 주소 |
| status | PartnerStatus | ACTIVE, INACTIVE |
| createdAt | LocalDateTime | 생성일 |
| updatedAt | LocalDateTime | 수정일 |

### 3.3.3 Category (품목 카테고리)

품목 카테고리를 관리한다.

| 필드 | 타입 | 설명 |
|---|---|---|
| id | Long | 카테고리 PK |
| name | String | 카테고리명 (unique) |
| description | String | 카테고리 설명 |
| createdAt | LocalDateTime | 생성일 |
| updatedAt | LocalDateTime | 수정일 |

### 3.3.4 Item (품목)

판매/구매하는 품목 마스터 데이터를 관리한다.

| 필드 | 타입 | 설명 |
|---|---|---|
| id | Long | 품목 PK |
| itemCode | String | 품목코드 (unique) |
| name | String | 품목명 |
| categoryId | Long | 카테고리 FK |
| unit | String | 단위 (EA, BOX, KG 등) |
| unitPrice | BigDecimal | 표준 단가 |
| safetyStock | Integer | 안전재고량 |
| status | ItemStatus | ACTIVE, DISCONTINUED |
| createdAt | LocalDateTime | 생성일 |
| updatedAt | LocalDateTime | 수정일 |

### 3.3.5 PurchaseOrder (발주서)

발주(구매) 헤더 정보를 관리한다.

| 필드 | 타입 | 설명 |
|---|---|---|
| id | Long | 발주서 PK |
| orderNumber | String | 발주번호 (unique, 예: PO-20260526-0001) |
| partnerId | Long | 공급사 FK |
| writerId | Long | 작성자 FK (User) |
| approverId | Long | 승인자 FK (User) |
| orderDate | LocalDate | 발주일 |
| dueDate | LocalDate | 납기일 |
| totalAmount | BigDecimal | 총금액 (라인 합계) |
| status | PurchaseOrderStatus | DRAFT, REQUESTED, APPROVED, REJECTED, RECEIVED, CANCELED |
| rejectReason | String | 반려 사유 |
| createdAt | LocalDateTime | 작성일 |
| updatedAt | LocalDateTime | 수정일 |
| approvedAt | LocalDateTime | 승인일 |
| receivedAt | LocalDateTime | 입고일 |

### 3.3.6 PurchaseOrderLine (발주서 라인)

발주서에 포함된 품목 라인을 관리한다.

| 필드 | 타입 | 설명 |
|---|---|---|
| id | Long | 라인 PK |
| purchaseOrderId | Long | 발주서 FK |
| itemId | Long | 품목 FK |
| quantity | Integer | 수량 |
| unitPrice | BigDecimal | 라인 단가 |
| lineAmount | BigDecimal | 라인 금액 (quantity × unitPrice) |

### 3.3.7 Notice (공지사항)

공지사항 정보를 관리한다.

| 필드 | 타입 | 설명 |
|---|---|---|
| id | Long | 공지사항 PK |
| title | String | 제목 |
| content | Text | 내용 |
| writerId | Long | 작성자 FK (User) |
| important | Boolean | 중요 공지 여부 |
| viewCount | Long | 조회수 |
| createdAt | LocalDateTime | 작성일 |
| updatedAt | LocalDateTime | 수정일 |

### 3.3.8 SalesOrder (수주서)

수주(판매) 헤더 정보를 관리한다.

| 필드 | 타입 | 설명 |
|---|---|---|
| id | Long | 수주서 PK |
| orderNumber | String | 수주번호 (unique, 예: SO-20260526-0001) |
| partnerId | Long | 고객사 FK |
| writerId | Long | 작성자 FK (User) |
| managerId | Long | 처리자 FK (User, MANAGER) |
| orderDate | LocalDate | 수주일 |
| shipDate | LocalDate | 출고 예정일 |
| totalAmount | BigDecimal | 총금액 |
| status | SalesOrderStatus | DRAFT, CONFIRMED, SHIPPED, COMPLETED, CANCELED |
| cancelReason | String | 취소 사유 |
| createdAt | LocalDateTime | 작성일 |
| confirmedAt | LocalDateTime | 확정일 |
| shippedAt | LocalDateTime | 출고일 |
| completedAt | LocalDateTime | 완료일 |

### 3.3.9 SalesOrderLine (수주서 라인)

수주서에 포함된 품목 라인을 관리한다.

| 필드 | 타입 | 설명 |
|---|---|---|
| id | Long | 라인 PK |
| salesOrderId | Long | 수주서 FK |
| itemId | Long | 품목 FK |
| quantity | Integer | 수량 |
| unitPrice | BigDecimal | 라인 단가 |
| lineAmount | BigDecimal | 라인 금액 |

---

## 3.4 ERD 초안

```text
User 1 : N PurchaseOrder(writer)
User 1 : N PurchaseOrder(approver)
User 1 : N SalesOrder(writer)
User 1 : N SalesOrder(manager)
User 1 : N Notice
Category 1 : N Item
Partner 1 : N PurchaseOrder
Partner 1 : N SalesOrder
PurchaseOrder 1 : N PurchaseOrderLine
SalesOrder 1 : N SalesOrderLine
Item 1 : N PurchaseOrderLine
Item 1 : N SalesOrderLine
```

### 3.4.1 테이블 관계 설명

- User는 로그인 계정이다.
- Partner는 SUPPLIER(공급사), CUSTOMER(고객사), BOTH(양쪽) 유형을 가진다.
- Category는 여러 Item을 가질 수 있다.
- PurchaseOrder는 SUPPLIER 유형의 Partner를 참조한다.
- SalesOrder는 CUSTOMER 유형의 Partner를 참조한다.
- PurchaseOrder는 여러 PurchaseOrderLine을 가진다 (헤더-라인).
- SalesOrder는 여러 SalesOrderLine을 가진다.
- Notice는 User(ADMIN)가 작성한다.

---

## 3.5 DB 테이블 설계

### 3.5.1 users

| 컬럼 | 타입 | 제약조건 | 설명 |
|---|---|---|---|
| id | BIGINT | PK | 사용자 ID |
| email | VARCHAR(100) | UNIQUE, NOT NULL | 이메일 |
| password | VARCHAR(255) | NOT NULL | 비밀번호 |
| name | VARCHAR(50) | NOT NULL | 이름 |
| role | VARCHAR(20) | NOT NULL | 권한 |
| created_at | DATETIME | NOT NULL | 생성일 |
| updated_at | DATETIME | NULL | 수정일 |

### 3.5.2 partners

| 컬럼 | 타입 | 제약조건 | 설명 |
|---|---|---|---|
| id | BIGINT | PK | 거래처 ID |
| name | VARCHAR(150) | NOT NULL | 거래처명 |
| business_number | VARCHAR(20) | UNIQUE, NOT NULL | 사업자번호 |
| partner_type | VARCHAR(20) | NOT NULL | SUPPLIER/CUSTOMER/BOTH |
| contact_name | VARCHAR(50) | NULL | 담당자명 |
| phone | VARCHAR(30) | NULL | 연락처 |
| email | VARCHAR(100) | NULL | 이메일 |
| address | VARCHAR(255) | NULL | 주소 |
| status | VARCHAR(20) | NOT NULL | ACTIVE/INACTIVE |
| created_at | DATETIME | NOT NULL | 생성일 |
| updated_at | DATETIME | NULL | 수정일 |

### 3.5.3 categories

| 컬럼 | 타입 | 제약조건 | 설명 |
|---|---|---|---|
| id | BIGINT | PK | 카테고리 ID |
| name | VARCHAR(100) | UNIQUE, NOT NULL | 카테고리명 |
| description | VARCHAR(255) | NULL | 설명 |
| created_at | DATETIME | NOT NULL | 생성일 |
| updated_at | DATETIME | NULL | 수정일 |

### 3.5.4 items

| 컬럼 | 타입 | 제약조건 | 설명 |
|---|---|---|---|
| id | BIGINT | PK | 품목 ID |
| item_code | VARCHAR(50) | UNIQUE, NOT NULL | 품목코드 |
| name | VARCHAR(150) | NOT NULL | 품목명 |
| category_id | BIGINT | FK, NOT NULL | 카테고리 ID |
| unit | VARCHAR(20) | NOT NULL | 단위 |
| unit_price | DECIMAL(15,2) | NOT NULL | 단가 |
| safety_stock | INT | NOT NULL DEFAULT 0 | 안전재고 |
| status | VARCHAR(20) | NOT NULL | 상태 |
| created_at | DATETIME | NOT NULL | 생성일 |
| updated_at | DATETIME | NULL | 수정일 |

### 3.5.5 purchase_orders

| 컬럼 | 타입 | 제약조건 | 설명 |
|---|---|---|---|
| id | BIGINT | PK | 발주서 ID |
| order_number | VARCHAR(30) | UNIQUE, NOT NULL | 발주번호 |
| partner_id | BIGINT | FK, NOT NULL | 공급사 |
| writer_id | BIGINT | FK, NOT NULL | 작성자 |
| approver_id | BIGINT | FK, NULL | 승인자 |
| order_date | DATE | NOT NULL | 발주일 |
| due_date | DATE | NULL | 납기일 |
| total_amount | DECIMAL(15,2) | NOT NULL | 총금액 |
| status | VARCHAR(30) | NOT NULL | 상태 |
| reject_reason | VARCHAR(500) | NULL | 반려 사유 |
| created_at | DATETIME | NOT NULL | 작성일 |
| updated_at | DATETIME | NULL | 수정일 |
| approved_at | DATETIME | NULL | 승인일 |
| received_at | DATETIME | NULL | 입고일 |

### 3.5.6 purchase_order_lines

| 컬럼 | 타입 | 제약조건 | 설명 |
|---|---|---|---|
| id | BIGINT | PK | 라인 ID |
| purchase_order_id | BIGINT | FK, NOT NULL | 발주서 |
| item_id | BIGINT | FK, NOT NULL | 품목 |
| quantity | INT | NOT NULL | 수량 |
| unit_price | DECIMAL(15,2) | NOT NULL | 단가 |
| line_amount | DECIMAL(15,2) | NOT NULL | 라인 금액 |

### 3.5.7 notices

| 컬럼 | 타입 | 제약조건 | 설명 |
|---|---|---|---|
| id | BIGINT | PK | 공지 ID |
| title | VARCHAR(200) | NOT NULL | 제목 |
| content | TEXT | NOT NULL | 내용 |
| writer_id | BIGINT | FK, NOT NULL | 작성자 |
| important | BOOLEAN | NOT NULL | 중요 여부 |
| view_count | BIGINT | NOT NULL | 조회수 |
| created_at | DATETIME | NOT NULL | 작성일 |
| updated_at | DATETIME | NULL | 수정일 |

### 3.5.8 sales_orders

| 컬럼 | 타입 | 제약조건 | 설명 |
|---|---|---|---|
| id | BIGINT | PK | 수주서 ID |
| order_number | VARCHAR(30) | UNIQUE, NOT NULL | 수주번호 |
| partner_id | BIGINT | FK, NOT NULL | 고객사 |
| writer_id | BIGINT | FK, NOT NULL | 작성자 |
| manager_id | BIGINT | FK, NULL | 처리자 |
| order_date | DATE | NOT NULL | 수주일 |
| ship_date | DATE | NULL | 출고 예정일 |
| total_amount | DECIMAL(15,2) | NOT NULL | 총금액 |
| status | VARCHAR(30) | NOT NULL | 상태 |
| cancel_reason | VARCHAR(500) | NULL | 취소 사유 |
| created_at | DATETIME | NOT NULL | 작성일 |
| confirmed_at | DATETIME | NULL | 확정일 |
| shipped_at | DATETIME | NULL | 출고일 |
| completed_at | DATETIME | NULL | 완료일 |

### 3.5.9 sales_order_lines

| 컬럼 | 타입 | 제약조건 | 설명 |
|---|---|---|---|
| id | BIGINT | PK | 라인 ID |
| sales_order_id | BIGINT | FK, NOT NULL | 수주서 |
| item_id | BIGINT | FK, NOT NULL | 품목 |
| quantity | INT | NOT NULL | 수량 |
| unit_price | DECIMAL(15,2) | NOT NULL | 단가 |
| line_amount | DECIMAL(15,2) | NOT NULL | 라인 금액 |

---

## 3.6 Enum 설계

### 3.6.1 UserRole

```java
public enum UserRole {
    USER,
    ADMIN,
    MANAGER
}
```

### 3.6.2 PartnerType / PartnerStatus

```java
public enum PartnerType {
    SUPPLIER,
    CUSTOMER,
    BOTH
}

public enum PartnerStatus {
    ACTIVE,
    INACTIVE
}
```

### 3.6.3 ItemStatus

```java
public enum ItemStatus {
    ACTIVE,
    DISCONTINUED
}
```

### 3.6.4 PurchaseOrderStatus

```java
public enum PurchaseOrderStatus {
    DRAFT,
    REQUESTED,
    APPROVED,
    REJECTED,
    RECEIVED,
    CANCELED
}
```

### 3.6.5 SalesOrderStatus

```java
public enum SalesOrderStatus {
    DRAFT,
    CONFIRMED,
    SHIPPED,
    COMPLETED,
    CANCELED
}
```

---

## 3.7 API 설계

### 3.7.1 사용자 API

| Method | URL | 설명 | 권한 |
|---|---|---|---|
| POST | /api/auth/login | 로그인 | 전체 |
| POST | /api/auth/logout | 로그아웃 | 로그인 사용자 |
| GET | /api/users/me | 내 정보 조회 | 로그인 사용자 |
| PATCH | /api/users/me/password | 비밀번호 변경 | 로그인 사용자 |

### 3.7.2 거래처 API

| Method | URL | 설명 | 권한 |
|---|---|---|---|
| POST | /api/partners | 거래처 등록 | ADMIN |
| GET | /api/partners | 거래처 목록 조회 | 로그인 사용자 |
| GET | /api/partners/{partnerId} | 거래처 상세 조회 | 로그인 사용자 |
| PUT | /api/partners/{partnerId} | 거래처 수정 | ADMIN |
| DELETE | /api/partners/{partnerId} | 거래처 삭제/비활성 | ADMIN |

#### 거래처 등록 Request 예시

```json
{
  "name": "(주)베스트공급사",
  "businessNumber": "123-45-67890",
  "partnerType": "SUPPLIER",
  "contactName": "홍길동",
  "phone": "02-1234-5678",
  "email": "contact@best.com",
  "address": "서울시 강남구 ..."
}
```

#### 거래처 등록 Response 예시

```json
{
  "partnerId": 1,
  "name": "(주)베스트공급사",
  "businessNumber": "123-45-67890",
  "partnerType": "SUPPLIER",
  "status": "ACTIVE"
}
```

### 3.7.3 카테고리 API

| Method | URL | 설명 | 권한 |
|---|---|---|---|
| POST | /api/categories | 카테고리 등록 | ADMIN |
| GET | /api/categories | 카테고리 목록 조회 | 로그인 사용자 |
| GET | /api/categories/{categoryId} | 카테고리 상세 조회 | 로그인 사용자 |
| PUT | /api/categories/{categoryId} | 카테고리 수정 | ADMIN |
| DELETE | /api/categories/{categoryId} | 카테고리 삭제 | ADMIN |

### 3.7.4 품목 API

| Method | URL | 설명 | 권한 |
|---|---|---|---|
| POST | /api/items | 품목 등록 | ADMIN |
| GET | /api/items | 품목 목록 조회 | 로그인 사용자 |
| GET | /api/items/{itemId} | 품목 상세 조회 | 로그인 사용자 |
| PUT | /api/items/{itemId} | 품목 수정 | ADMIN |
| PATCH | /api/items/{itemId}/discontinue | 품목 단종 처리 | ADMIN |

### 3.7.5 발주 API

| Method | URL | 설명 | 권한 |
|---|---|---|---|
| POST | /api/purchase-orders | 발주서 작성 (DRAFT) | 로그인 사용자 |
| PATCH | /api/purchase-orders/{poId}/submit | 발주 요청 (DRAFT → REQUESTED) | 작성자 |
| GET | /api/purchase-orders/my | 내 발주서 목록 | 로그인 사용자 |
| GET | /api/purchase-orders/{poId} | 발주서 상세 조회 | 작성자/ADMIN |
| PATCH | /api/purchase-orders/{poId}/cancel | 발주서 취소 | 작성자 |
| GET | /api/admin/purchase-orders | 관리자 발주 목록 | ADMIN |
| PATCH | /api/admin/purchase-orders/{poId}/approve | 발주 승인 | ADMIN |
| PATCH | /api/admin/purchase-orders/{poId}/reject | 발주 반려 | ADMIN |
| PATCH | /api/admin/purchase-orders/{poId}/receive | 입고 처리 | ADMIN |

#### 발주서 작성 Request 예시

```json
{
  "partnerId": 1,
  "orderDate": "2026-06-01",
  "dueDate": "2026-06-15",
  "lines": [
    { "itemId": 10, "quantity": 100, "unitPrice": 5000 },
    { "itemId": 11, "quantity": 50,  "unitPrice": 12000 }
  ]
}
```

#### 발주 승인 Response 예시

```json
{
  "purchaseOrderId": 1,
  "orderNumber": "PO-20260526-0001",
  "status": "APPROVED",
  "message": "발주가 승인되었습니다."
}
```

### 3.7.6 공지사항 API

| Method | URL | 설명 | 권한 |
|---|---|---|---|
| POST | /api/notices | 공지 등록 | ADMIN |
| GET | /api/notices | 공지 목록 조회 | 로그인 사용자 |
| GET | /api/notices/{noticeId} | 공지 상세 조회 | 로그인 사용자 |
| PUT | /api/notices/{noticeId} | 공지 수정 | ADMIN |
| DELETE | /api/notices/{noticeId} | 공지 삭제 | ADMIN |

### 3.7.7 수주 API

| Method | URL | 설명 | 권한 |
|---|---|---|---|
| POST | /api/sales-orders | 수주서 작성 | 로그인 사용자 |
| PATCH | /api/sales-orders/{soId}/confirm | 수주 확정 | 작성자 |
| GET | /api/sales-orders/my | 내 수주서 목록 | 로그인 사용자 |
| GET | /api/sales-orders/pending | 처리 대기 수주서 | MANAGER, ADMIN |
| GET | /api/sales-orders/{soId} | 수주서 상세 조회 | 작성자/MANAGER/ADMIN |
| PATCH | /api/sales-orders/{soId}/ship | 출고 처리 | MANAGER, ADMIN |
| PATCH | /api/sales-orders/{soId}/complete | 완료 처리 | MANAGER, ADMIN |
| PATCH | /api/sales-orders/{soId}/cancel | 수주 취소 | MANAGER, ADMIN, 작성자(본인) |

---

## 3.8 비즈니스 규칙

### 3.8.1 거래처 관리 규칙

- 사업자번호는 중복될 수 없다.
- 거래처는 유형(SUPPLIER/CUSTOMER/BOTH)을 가진다.
- INACTIVE 상태 거래처로는 발주/수주를 작성할 수 없다.

### 3.8.2 품목 관리 규칙

- 품목코드는 중복될 수 없다.
- 품목은 반드시 하나의 카테고리에 속해야 한다.
- DISCONTINUED 상태 품목은 새 발주/수주 라인에 포함할 수 없다.

### 3.8.3 발주(구매) 규칙

- 발주일은 납기일보다 늦을 수 없다.
- 발주 라인은 최소 1개 이상 필요하다.
- 총금액은 라인 합계와 일치해야 한다.
- 공급사가 아닌 거래처(CUSTOMER만 가진 거래처)로는 발주할 수 없다.
- DRAFT 상태만 결재 요청 가능하다.
- REQUESTED 상태만 승인/반려 가능하다.
- APPROVED 상태만 입고 처리 가능하다.
- 반려 시 반려 사유를 입력해야 한다.
- 이미 승인된 발주는 다시 반려할 수 없다.

### 3.8.4 공지사항 규칙

- 공지 제목과 내용은 필수다.
- 공지는 관리자만 등록, 수정, 삭제할 수 있다.
- 일반 사용자는 공지 목록과 상세만 조회할 수 있다.

### 3.8.5 수주(판매) 규칙

- 수주 라인은 최소 1개 이상 필요하다.
- 고객사가 아닌 거래처(SUPPLIER만 가진 거래처)로는 수주할 수 없다.
- DRAFT 상태만 확정 가능하다.
- CONFIRMED 상태만 출고 가능하다.
- SHIPPED 상태만 완료 가능하다.
- 취소는 COMPLETED 이전 상태에서만 가능하다.
- 취소 시 취소 사유를 입력해야 한다.

---

## 3.9 예외 처리 설계

### 3.9.1 공통 에러 응답 형식

```json
{
  "status": 400,
  "code": "INVALID_REQUEST",
  "message": "잘못된 요청입니다.",
  "timestamp": "2026-05-26T20:00:00"
}
```

### 3.9.2 주요 예외 목록

| 예외 코드 | HTTP Status | 설명 |
|---|---|---|
| INVALID_INPUT | 400 | 입력값 검증 실패 |
| AUTHENTICATION_REQUIRED | 401 | 로그인이 필요함 |
| USER_NOT_FOUND | 404 | 사용자를 찾을 수 없음 |
| PARTNER_NOT_FOUND | 404 | 거래처를 찾을 수 없음 |
| CATEGORY_NOT_FOUND | 404 | 카테고리를 찾을 수 없음 |
| ITEM_NOT_FOUND | 404 | 품목을 찾을 수 없음 |
| PURCHASE_ORDER_NOT_FOUND | 404 | 발주서를 찾을 수 없음 |
| SALES_ORDER_NOT_FOUND | 404 | 수주서를 찾을 수 없음 |
| NOTICE_NOT_FOUND | 404 | 공지사항을 찾을 수 없음 |
| DUPLICATE_EMAIL | 400 | 이메일 중복 |
| DUPLICATE_BUSINESS_NUMBER | 400 | 사업자번호 중복 |
| DUPLICATE_CATEGORY_NAME | 400 | 카테고리명 중복 |
| DUPLICATE_ITEM_CODE | 400 | 품목코드 중복 |
| CATEGORY_HAS_ITEMS | 400 | 소속 품목이 있는 카테고리 삭제 시도 |
| PARTNER_TYPE_MISMATCH | 400 | 거래처 유형 불일치 (예: CUSTOMER로 발주 시도) |
| EMPTY_ORDER_LINES | 400 | 발주/수주 라인이 비어 있음 |
| INVALID_DATE_RANGE | 400 | 잘못된 날짜 범위 |
| INVALID_STATUS | 400 | 처리할 수 없는 상태 |
| ITEM_DISCONTINUED | 400 | 단종된 품목 사용 |
| ACCESS_DENIED | 403 | 접근 권한 없음 |
| INTERNAL_ERROR | 500 | 서버 내부 오류 |

---

## 3.10 트랜잭션 설계

### 3.10.1 발주서 작성 (헤더 + 라인)

발주서 헤더와 라인이 함께 저장되어야 한다.

```text
PurchaseOrder 헤더 생성
→ 라인 N개 검증 (품목 존재, 단종 여부)
→ 총금액 계산
→ 헤더 + 라인 저장
→ 일부 실패 시 rollback
```

Service 메서드에 `@Transactional`을 적용한다.

### 3.10.2 발주 승인

발주 승인 상태 변경은 하나의 트랜잭션으로 처리한다.

```text
발주서 조회
→ 상태 검증 (REQUESTED인가)
→ 승인자 ID 설정
→ 상태 APPROVED 변경
→ approvedAt 기록
→ 저장
```

### 3.10.3 입고 처리

발주 입고 처리는 향후 재고 반영을 포함한다.

```text
발주서 조회
→ 상태 검증 (APPROVED인가)
→ 라인별 재고 증가 (확장 기능)
→ 상태 RECEIVED 변경
→ receivedAt 기록
```

### 3.10.4 수주 확정/출고

수주 상태 전이도 트랜잭션으로 처리한다.

```text
DRAFT → CONFIRMED: 라인 검증, 고객사 검증
CONFIRMED → SHIPPED: 출고일 기록
SHIPPED → COMPLETED: 완료일 기록
```

---

## 3.11 보안 설계

### 3.11.1 1차 구현

포트폴리오 초기 버전에서는 세션 또는 단순 로그인 방식으로 구현한다.

```text
로그인 성공
→ Session에 userId 저장
→ 요청 시 Session에서 사용자 확인
```

### 3.11.2 확장 구현

나중에 Spring Security 또는 JWT를 적용한다.

```text
Spring Security
BCryptPasswordEncoder
Role 기반 접근 제어
JWT Access Token
```

### 3.11.3 권한 정책

| 기능 | USER | ADMIN | MANAGER |
|---|---|---|---|
| 내 정보 조회 | 가능 | 가능 | 가능 |
| 거래처 관리 | 불가 | 가능 | 불가 |
| 카테고리 관리 | 불가 | 가능 | 불가 |
| 품목 관리 | 불가 | 가능 | 불가 |
| 거래처/품목 조회 | 가능 | 가능 | 가능 |
| 발주 작성 | 가능 | 가능 | 가능 |
| 발주 승인/반려 | 불가 | 가능 | 불가 |
| 입고 처리 | 불가 | 가능 | 불가 |
| 공지 조회 | 가능 | 가능 | 가능 |
| 공지 등록 | 불가 | 가능 | 불가 |
| 수주 작성 | 가능 | 가능 | 가능 |
| 수주 확정 | 가능(작성자) | 가능 | 가능 |
| 수주 출고/완료 | 불가 | 가능 | 가능 |
| 수주 취소 | 가능(작성자) | 가능 | 가능 |

---

## 3.12 테스트 계획

### 3.12.1 단위 테스트

| 테스트 대상 | 테스트 내용 |
|---|---|
| PartnerService | 거래처 등록, 사업자번호 중복 검증 |
| CategoryService | 카테고리 등록, 중복 검증 |
| ItemService | 품목 등록, 단가/카테고리 검증, 단종 처리 |
| PurchaseOrderService | 발주 작성, 라인 검증, 상태 전이 |
| SalesOrderService | 수주 작성, 확정, 출고 |
| NoticeService | 공지 등록, 수정, 삭제 |

### 3.12.2 통합 테스트

| 테스트 | 설명 |
|---|---|
| 품목 등록 API 테스트 | 품목 등록 요청부터 DB 저장까지 확인 |
| 발주 작성 API 테스트 | 헤더 + 라인 동시 저장 확인 |
| 발주 승인 API 테스트 | 승인 후 상태 변경 확인 |
| 공지사항 API 테스트 | 공지 CRUD 확인 |
| 권한 테스트 | 일반 사용자가 관리자 API 접근 시 실패 확인 |

### 3.12.3 수동 테스트 시나리오

```text
1. 관리자 계정으로 로그인한다.
2. 거래처(공급사) 1건과 (고객사) 1건을 등록한다.
3. 카테고리와 품목 2건을 등록한다.
4. 일반 사용자 계정으로 로그인한다.
5. 발주서를 작성하고 결재 요청한다.
6. 관리자 계정으로 발주를 승인한다.
7. 관리자가 입고 처리를 진행한다.
8. 일반 사용자 계정으로 수주서를 작성하고 확정한다.
9. 매니저 계정으로 출고/완료 처리를 진행한다.
10. 관리자가 공지사항을 등록한다.
11. 일반 사용자가 공지사항을 조회한다.
```

---

## 3.13 개발 단계

### 3.13.1 1단계: 기본 프로젝트 생성

```text
Spring Boot 프로젝트 생성
GitHub 저장소 연결
README 초안 작성
패키지 구조 생성
```

### 3.13.2 2단계: 기본 도메인 구현

```text
User
Partner
Category
Item
```

### 3.13.3 3단계: 마스터 데이터 CRUD

```text
거래처 등록/조회/수정/삭제
카테고리 등록/조회/수정/삭제
품목 등록/조회/수정/검색/페이징/단종처리
```

### 3.13.4 4단계: 발주(구매) 기능

```text
발주서 작성 (헤더 + 라인)
내 발주서 목록
발주서 상세
발주 요청 / 취소
관리자 발주 목록
발주 승인 / 반려
입고 처리
```

### 3.13.5 5단계: 공지사항

```text
공지 등록 / 목록 / 상세 / 수정 / 삭제
```

### 3.13.6 6단계: 수주(판매) 기능

```text
수주서 작성 (헤더 + 라인)
내 수주서 목록
처리 대기 수주서
수주 확정 / 출고 / 완료 / 취소
```

### 3.13.7 7단계: 문서화

```text
README.md
docs/PRD.md
docs/TRD.md
docs/ERD.md
docs/API_SPEC.md
docs/TROUBLESHOOTING.md
```

---

## 3.14 Codex 개발 프롬프트 예시

### 3.14.1 프로젝트 구조 생성

```text
Spring Boot 기반 SCM 시스템을 만들려고 합니다.

패키지 구조를 아래와 같이 생성해주세요.

com.example.scm
- controller
- service
- repository
- domain
- dto
- exception
- config

아직 기능 구현은 하지 말고 기본 README.md와 docs 폴더 구조도 함께 만들어주세요.
```

### 3.14.2 거래처/품목 도메인 생성

```text
SCM 시스템의 거래처와 품목 도메인을 구현해주세요.

요구사항:
- User 엔티티 (USER/ADMIN/MANAGER)
- Partner 엔티티 (SUPPLIER/CUSTOMER/BOTH, 사업자번호 unique)
- Category 엔티티 (카테고리명 unique)
- Item 엔티티 (품목코드 unique, 카테고리 FK)
- 각 enum: PartnerType, PartnerStatus, ItemStatus
- Repository 생성
- 기본 생성자, 연관관계, createdAt, updatedAt 포함

품목은 하나의 카테고리에 속하고, 카테고리는 여러 품목을 가질 수 있도록 설계해주세요.
```

### 3.14.3 발주 기능 구현

```text
발주(구매) 기능을 구현해주세요.

요구사항:
- PurchaseOrder 헤더 + PurchaseOrderLine 라인 엔티티
- PurchaseOrderStatus enum (DRAFT/REQUESTED/APPROVED/REJECTED/RECEIVED/CANCELED)
- 발주서 작성 API: POST /api/purchase-orders (헤더 + 라인 동시 저장)
- 발주 요청 API: PATCH /api/purchase-orders/{poId}/submit
- 내 발주서 목록 API: GET /api/purchase-orders/my
- 발주서 상세 API: GET /api/purchase-orders/{poId}
- 발주 취소 API: PATCH /api/purchase-orders/{poId}/cancel
- 관리자 발주 목록 API: GET /api/admin/purchase-orders
- 발주 승인 API: PATCH /api/admin/purchase-orders/{poId}/approve
- 발주 반려 API: PATCH /api/admin/purchase-orders/{poId}/reject
- 입고 처리 API: PATCH /api/admin/purchase-orders/{poId}/receive

DRAFT 상태만 결재 요청 가능, REQUESTED 상태만 승인/반려, APPROVED만 입고 처리 가능하도록 검증해주세요.
```

### 3.14.4 공지사항 기능 구현

```text
공지사항 기능을 구현해주세요.

요구사항:
- Notice 엔티티
- 공지 등록 API: POST /api/notices
- 공지 목록 API: GET /api/notices
- 공지 상세 API: GET /api/notices/{noticeId}
- 공지 수정 API: PUT /api/notices/{noticeId}
- 공지 삭제 API: DELETE /api/notices/{noticeId}

목록 조회는 페이징을 적용하고, important=true인 공지는 상단 노출이 가능하도록 정렬 기준을 고려해주세요.
```

### 3.14.5 수주 기능 구현

```text
수주(판매) 기능을 구현해주세요.

요구사항:
- SalesOrder 헤더 + SalesOrderLine 라인 엔티티
- SalesOrderStatus enum (DRAFT/CONFIRMED/SHIPPED/COMPLETED/CANCELED)
- 수주서 작성 API: POST /api/sales-orders
- 수주 확정 API: PATCH /api/sales-orders/{soId}/confirm
- 내 수주서 목록 API: GET /api/sales-orders/my
- 처리 대기 수주서 API: GET /api/sales-orders/pending
- 수주서 상세 API: GET /api/sales-orders/{soId}
- 출고 처리 API: PATCH /api/sales-orders/{soId}/ship
- 완료 처리 API: PATCH /api/sales-orders/{soId}/complete
- 취소 API: PATCH /api/sales-orders/{soId}/cancel

DRAFT 상태만 확정 가능, CONFIRMED만 출고, SHIPPED만 완료 가능하도록 구현해주세요.
취소 시 사유 필수입니다.
```

---

## 3.15 GitHub README 구성

```md
# Supply Chain Management System

Java Spring Boot 기반 공급망 관리 시스템입니다.
거래처 관리, 품목 관리, 발주(구매) 신청/승인, 수주(판매) 작성/확정, 공지사항 기능을 구현했습니다.

## 1. 프로젝트 소개

SI 업무에서 자주 등장하는 SCM/구매-판매 관리 기능을 구현한 포트폴리오 프로젝트입니다.

## 2. 개발 목적

- Spring Boot 기반 SCM 시스템 개발 학습
- 거래처/품목/카테고리/발주/수주 도메인 설계
- 헤더-라인 구조와 다단계 상태 전이 구현
- CRUD, 검색, 페이징 구현
- 관리자/일반/매니저 권한 구분
- SI 취업용 포트폴리오 완성

## 3. 사용 기술

| 구분 | 기술 |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot |
| DB | H2 / MySQL |
| ORM | Spring Data JPA |
| View | Thymeleaf |
| Build | Gradle |
| Test | JUnit 5 |

## 4. 주요 기능

- 로그인
- 거래처 관리
- 카테고리 / 품목 관리
- 발주(구매) 작성/승인/입고
- 수주(판매) 작성/확정/출고/완료
- 공지사항 관리
- 관리자 / 매니저 기능

## 5. 시스템 구조

Controller → Service → Repository → Database

## 6. ERD

User 1 : N PurchaseOrder
Partner 1 : N PurchaseOrder
Category 1 : N Item
PurchaseOrder 1 : N PurchaseOrderLine
SalesOrder 1 : N SalesOrderLine

## 7. API 명세

자세한 API 명세는 `docs/API_SPEC.md`를 참고하세요.

## 8. 실행 방법

git clone https://github.com/사용자명/scm-system.git
cd scm-system
./gradlew bootRun
```

---

## 3.16 트러블슈팅 예시

### 3.16.1 발주 승인 상태 중복 처리 문제

#### 문제

이미 승인된 발주를 다시 승인하거나 반려할 수 있는 문제가 있었다.

#### 원인

발주 승인/반려 처리 전에 현재 상태가 REQUESTED인지 검증하지 않았다.

#### 해결

Service 계층에서 상태 검증 로직을 추가했다.

```java
if (purchaseOrder.getStatus() != PurchaseOrderStatus.REQUESTED) {
    throw new BusinessException(ErrorCode.INVALID_STATUS);
}
```

#### 배운 점

승인/반려 같은 업무 프로세스에서는 현재 상태를 반드시 검증해야 한다.

### 3.16.2 발주 라인 일부만 저장되는 문제

#### 문제

발주서 작성 시 헤더는 저장되었지만 라인 저장 중 오류가 발생하면 헤더만 남는 경우가 있었다.

#### 원인

헤더와 라인 저장이 하나의 트랜잭션으로 묶여 있지 않았다.

#### 해결

발주서 생성 Service 메서드에 `@Transactional`을 적용하고 라인 검증을 먼저 끝낸 뒤 헤더+라인을 cascade 또는 명시적으로 함께 저장했다.

#### 배운 점

헤더-라인 구조는 트랜잭션으로 묶고, 라인 검증을 헤더 저장 전에 끝내는 편이 안전하다.

### 3.16.3 Entity 직접 반환 문제

#### 문제

Controller에서 Entity를 그대로 반환하면 비밀번호나 내부 관리 필드가 노출될 위험이 있었다.

#### 해결

응답 DTO를 별도로 만들어 필요한 필드만 반환했다. 라인 정보는 nested DTO로 감쌌다.

#### 배운 점

Entity와 DTO를 분리하면 보안과 유지보수 측면에서 유리하다.

### 3.16.4 거래처 유형 불일치 문제

#### 문제

CUSTOMER 유형 거래처로 발주를 작성하면 안 되는데, 그 검증이 없었다.

#### 원인

거래처 유형 검증을 Service에서 하지 않고 화면에서만 막았다.

#### 해결

발주 작성 Service에서 `partner.getPartnerType() == SUPPLIER || BOTH` 인지 확인했다.

#### 배운 점

화면 검증은 우회 가능하므로 Service에서 도메인 규칙을 한 번 더 막아야 한다.

### 3.16.5 발주번호 생성 동시성

#### 문제

같은 날 두 사용자가 발주서를 동시에 만들면 발주번호 시퀀스가 겹칠 수 있었다.

#### 해결

발주번호 발번 로직을 DB 시퀀스/감사용 별도 테이블/유니크 제약 + 재시도 패턴 중 하나로 정리했다.

#### 배운 점

문서 번호 채번은 동시성 이슈가 있고 도메인별 규칙이 필요하다.

---

## 3.17 개발 체크리스트

### 3.17.1 기능 체크리스트

- [ ] 로그인
- [ ] 로그아웃
- [ ] 내 정보 조회
- [ ] 비밀번호 변경
- [ ] 권한 구분
- [ ] 거래처 등록
- [ ] 거래처 목록 조회
- [ ] 거래처 상세 조회
- [ ] 거래처 수정
- [ ] 거래처 삭제/비활성화
- [ ] 거래처 검색
- [ ] 거래처 페이징
- [ ] 카테고리 등록
- [ ] 카테고리 목록 조회
- [ ] 카테고리 상세 조회
- [ ] 카테고리 수정
- [ ] 카테고리 삭제
- [ ] 품목 등록
- [ ] 품목 목록 조회
- [ ] 품목 상세 조회
- [ ] 품목 수정
- [ ] 품목 단종 처리
- [ ] 품목 검색/페이징
- [ ] 발주서 작성 (헤더 + 라인)
- [ ] 내 발주서 목록
- [ ] 발주서 상세 조회
- [ ] 발주 요청
- [ ] 발주 취소
- [ ] 관리자 발주 목록
- [ ] 발주 승인
- [ ] 발주 반려
- [ ] 입고 처리
- [ ] 공지사항 등록
- [ ] 공지사항 목록 조회
- [ ] 공지사항 상세 조회
- [ ] 공지사항 수정
- [ ] 공지사항 삭제
- [ ] 중요 공지 표시
- [ ] 수주서 작성
- [ ] 수주 확정
- [ ] 내 수주서 목록
- [ ] 처리 대기 수주서
- [ ] 수주 상세 조회
- [ ] 수주 출고/완료/취소

### 3.17.2 기술 체크리스트

- [ ] Controller, Service, Repository 분리
- [ ] Entity와 DTO 분리
- [ ] Enum 사용
- [ ] 공통 예외 처리
- [ ] Validation 적용
- [ ] 검색 기능 구현
- [ ] 페이징 구현
- [ ] @Transactional 적용
- [ ] 헤더-라인 cascade 처리
- [ ] 테스트 코드 작성
- [ ] README 작성
- [ ] ERD 작성
- [ ] API 명세 작성
- [ ] 트러블슈팅 작성
- [ ] 화면 캡처 정리

---

## 3.18 커밋 메시지 예시

```text
feat: 거래처 등록 기능 구현
feat: 품목 관리 기능 구현
feat: 발주서 작성(헤더+라인) 기능 구현
feat: 발주 승인 및 반려 기능 구현
feat: 입고 처리 기능 구현
feat: 공지사항 CRUD 기능 구현
feat: 수주 확정/출고/완료 기능 구현
fix: 발주 승인 상태 검증 오류 수정
refactor: PurchaseOrderService 비즈니스 로직 분리
docs: PRD/TRD 문서 추가
docs: API 명세서 작성
test: 발주 승인 서비스 테스트 추가
```

---

## 3.19 면접 대비 질문

```text
Q1. 이 프로젝트를 왜 만들었나요?

Q2. SCM 시스템에서 가장 중요한 도메인은 무엇이라고 생각했나요?

Q3. 거래처 유형(SUPPLIER/CUSTOMER/BOTH)을 왜 enum 하나로 묶었나요?

Q4. 품목과 카테고리의 관계는 어떻게 설계했나요?

Q5. 발주 상태값(DRAFT/REQUESTED/APPROVED/...)은 어떻게 관리했나요?

Q6. 헤더-라인 구조(발주서/수주서)에서 가장 중요한 검증은 무엇인가요?

Q7. @Transactional은 어디에 적용했고, 왜 적용했나요?

Q8. Entity를 직접 응답하지 않고 DTO를 사용한 이유는 무엇인가요?

Q9. 관리자와 일반 사용자/매니저의 권한은 어떻게 구분했나요?

Q10. 발주서 목록에서 페이징이 필요한 이유는 무엇인가요?

Q11. 수주 기능을 확장한다면 어떻게 개선하고 싶나요? (재고/출고지 등)

Q12. 이 프로젝트에서 가장 어려웠던 문제와 해결 방법은 무엇인가요?
```

---

## 3.20 포트폴리오 설명 문장 예시

```text
SCM 시스템은 유통/제조 업무에서 자주 등장하는 거래처 관리, 품목 관리, 발주(구매) 신청/승인, 수주(판매) 작성/확정, 공지사항 기능을 구현한 포트폴리오 프로젝트입니다.

거래처와 품목의 관계를 설계하고, 발주서/수주서의 다단계 상태(DRAFT → REQUESTED → APPROVED → RECEIVED 등)를 Enum으로 관리했습니다.

특히 발주서 작성 시 헤더와 라인이 함께 저장되어야 하므로 @Transactional을 적용하여 데이터 정합성을 보장했습니다.

또한 Controller, Service, Repository 계층을 분리하고 Entity와 DTO를 분리하여 유지보수성과 보안성을 고려했습니다.
```

---

# 4. 최종 정리

SCM 시스템은 SI 취업 포트폴리오로 적합한 프로젝트다.

이유는 다음과 같다.

1. 실제 SCM/ERP/유통 시스템과 유사하다.
2. CRUD, 검색, 페이징, 헤더-라인, 다단계 상태 전이를 모두 보여줄 수 있다.
3. 거래처, 품목, 카테고리, 발주, 수주 등 도메인 관계를 설계할 수 있다.
4. 관리자/일반 사용자/매니저 권한 구분을 설명할 수 있다.
5. 트랜잭션, 예외처리, DTO 분리, 도메인 규칙 같은 실무 개념을 포함할 수 있다.

최소 완성 목표는 아래와 같다.

```text
거래처 관리
카테고리 / 품목 관리
발주서 작성 (헤더+라인)
발주 승인/반려/입고
공지사항 CRUD
공통 예외 처리
README / ERD / API 명세 / 트러블슈팅
```

확장 목표는 아래와 같다.

```text
수주 작성/확정/출고/완료
재고 반영 (입고 시 + / 출고 시 -)
창고 / 재고 이동
Spring Security / JWT
첨부파일 / 댓글
통계 대시보드
Docker / AWS 배포
```

이 문서를 기준으로 구현하면 단순 예제 프로젝트가 아니라, SI 취업 면접에서 설명 가능한 SCM 포트폴리오로 발전시킬 수 있다.
