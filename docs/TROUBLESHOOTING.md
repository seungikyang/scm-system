# 트러블슈팅 & 설계 노트 (TROUBLESHOOTING)

> [HTML 학습 목차](../index.html) · [문서 지도](./INDEX.md) · [루프 엔지니어링](./LOOP_ENGINEERING.md) · [실행 README](../README.md)

구현 과정에서 마주친 환경/설계 이슈와 해결 방식, PRD와 구현이 달라진 지점, QA가 남긴 MINOR 항목을 정리합니다.

---

## 목차

1. [빌드 환경](#1-빌드-환경)
2. [OSIV off와 표시값 DTO 변환](#2-osiv-off와-표시값-dto-변환)
3. [낙관적 락 → INVALID_STATUS](#3-낙관적-락--invalid_status)
4. [발주번호 채번 동시성](#4-발주번호-채번-동시성)
5. [입고-재고 트랜잭션 원자성](#5-입고-재고-트랜잭션-원자성)
6. [PRD-구현 차이표](#6-prd-구현-차이표)
7. [QA MINOR 4건과 권장 후속](#7-qa-minor-4건과-권장-후속)
8. [MySQL 스키마 드리프트 방지](#8-mysql-스키마-드리프트-방지)

---

## 1. 빌드 환경

### 문제
- 초기 저장소는 JDK 17 빌드와 Gradle Wrapper 실행이 검증되지 않은 상태였습니다.
- Spring Boot 3.2 계열의 오픈소스 지원이 종료되어 관리 의존성의 보안/버그 수정이 더 이상 제공되지 않습니다.

### 해결 / 대응
- Gradle Wrapper 8.5 전체 파일을 복구하고 Spring Boot를 Java 17 호환 유지보수 계열인 3.5.14로 갱신했습니다.
- 단위 테스트, Spring Context/MockMvc 보안 테스트, H2/JPA 발주 전체 흐름 통합 테스트를 추가하고 실제 빌드를 통과시켰습니다.

```bash
./gradlew clean build
```

### 배운 점
- 빌드 환경(JDK 버전, wrapper jar)은 코드만큼이나 인수인계에 중요하다. 제약은 숨기지 말고 README/문서에 명시한다.

---

## 2. OSIV off와 표시값 DTO 변환

### 문제
- `application.yml`에서 `spring.jpa.open-in-view: false`로 설정했습니다. OSIV가 꺼지면 영속성 컨텍스트가 트랜잭션 종료 시 닫히므로, 뷰(Thymeleaf)나 컨트롤러에서 지연 로딩 연관을 탐색하면 `LazyInitializationException`이 발생합니다.
- 발주는 거래처/품목/작성자를 **ID(Long)로만 보관**하므로, 응답에 거래처명/품목명/작성자명 같은 표시값이 엔티티에 직접 들어있지 않습니다.

### 규약 (해결)
- 모든 표시값은 **Service 트랜잭션 안에서 조회해 Response/View DTO에 채워** 내려줍니다. 뷰는 DTO 필드만 참조하고 엔티티 연관을 탐색하지 않습니다.
- 목록은 N+1을 피하기 위해 거래처/카테고리 표시값을 `findAllById`로 한 번에 조회해 `id → name` Map으로 resolve합니다(`toSummaryPage`, Item 목록의 `categoryName`).
- 상세는 `findByIdWithLines`로 라인을 fetch join해 로딩한 뒤 표시값을 채웁니다.

### 배운 점
- OSIV off는 경계(트랜잭션)를 명확히 강제한다. 표시값을 어디서 채울지(Service)를 규약으로 못박으면 경계면 버그가 줄어든다.

---

## 3. 낙관적 락 → INVALID_STATUS

### 문제
- 두 관리자가 동시에 같은 발주를 승인하면 한 건만 성공해야 합니다(중복 처리 방지).

### 해결
- `PurchaseOrder`(및 `Stock`)에 `@Version` 컬럼을 두어 낙관적 락을 적용했습니다. 동시 처리 시 두 번째 커밋에서 `OptimisticLockingFailureException`이 발생합니다.
- 이 예외를 `ApiExceptionHandler`에서 **`INVALID_STATUS`(400)** 로 변환합니다(메시지: "다른 사용자가 먼저 처리했습니다. 다시 확인해 주세요."). 409 대신 400을 쓰는 것은 backend/QA 합의 사항입니다.

```java
@ExceptionHandler(OptimisticLockingFailureException.class)
public ResponseEntity<ErrorResponse> handleOptimisticLock(OptimisticLockingFailureException e) {
    return ResponseEntity.status(ErrorCode.INVALID_STATUS.getHttpStatus())
            .body(ErrorResponse.of(ErrorCode.INVALID_STATUS, "다른 사용자가 먼저 처리했습니다. 다시 확인해 주세요."));
}
```

### 배운 점
- 동시성 충돌도 "현재 상태에서 처리 불가"의 일종으로 보면 사용자에게 일관된 메시지를 줄 수 있다. 엔티티에 `@Version`만 추가하면 핸들러가 이미 처리하므로 Service 코드가 깨끗해진다.

---

## 4. 발주번호 채번 동시성

### 문제
- 발주번호 `PO-YYYYMMDD-####`는 일자별 4자리 시퀀스입니다. 동일 일자에 동시 작성이 일어나면 두 요청이 같은 마지막 번호를 읽을 수 있습니다. 단순 `count + 1`은 중간 데이터가 삭제된 경우 이미 존재하는 번호를 반복 계산하는 문제도 있습니다.

### 해결
- `order_number`에 UNIQUE 제약(`uk_po_order_number`)을 두어 최종 방어선으로 삼았습니다.
- `OrderNumberGenerator`는 건수가 아니라 **당일 최대 발주번호의 suffix + 1**을 계산해 삭제·공백의 영향을 받지 않습니다. 9999를 넘으면 5자리 번호를 조용히 만들지 않고 명시적으로 실패합니다.
- 각 저장 시도를 `PurchaseOrderPersistenceService`의 **독립 트랜잭션(`REQUIRES_NEW`)** 으로 실행합니다. UNIQUE 충돌로 실패한 영속성 컨텍스트는 즉시 폐기하고 다음 번호로 재시도합니다(최대 5회).
- 실패한 번호가 실제로 존재하는지 확인해 **발주번호 충돌만** 재시도합니다. 금액 범위 등 다른 무결성 오류는 채번 오류로 숨기지 않습니다.
- `PurchaseOrderConcurrencyIntegrationTest`는 두 스레드가 같은 `0001`을 계산해 한쪽이 UNIQUE 충돌한 뒤 서로 다른 번호로 저장되는 흐름을 실제로 검증합니다.

### 알려진 한계 (주의)
- 재시도는 최대 5회이므로 같은 날짜에 5건을 크게 넘는 생성 요청이 정확히 동시에 몰리는 환경에서는 일부 요청이 실패할 수 있습니다.
- 높은 처리량이 필요한 운영 환경에서는 별도 시퀀스 테이블 또는 DB 네임드 락 도입을 권장합니다.

### 배운 점
- "DB 제약 + 애플리케이션 재시도"는 단순하지만, 트랜잭션 경계 안에서의 flush 실패 처리에는 한계가 있다. 채번은 동시성 요구 수준에 따라 전략을 다르게 가져가야 한다.

---

## 5. 입고-재고 트랜잭션 원자성

### 문제
- 입고 시 발주 상태를 `RECEIVED`로 바꾸는 것과 라인별 재고를 증가시키는 것이 따로 처리되면, 한쪽만 반영되어 재고가 어긋날 수 있습니다.

### 해결
- `receive()`를 단일 `@Transactional`로 처리합니다: 상태 전이 + 모든 라인의 `Stock.increase`가 같은 트랜잭션입니다. 한 라인이라도 실패하면 상태 변경을 포함해 전체 롤백됩니다.
- 관련 `Item` 행을 ID 순서로 비관적 잠금한 뒤 `Stock`을 조회/생성/증가합니다. 서로 다른 발주가 같은 품목의 최초 재고 행을 동시에 만드는 경합을 직렬화하며, `item_id` UNIQUE 제약은 최종 방어선입니다.
- `Stock.increase`는 0 이하 수량과 `Integer` overflow를 차단해 음수 재고로 뒤집히는 것을 방지합니다.
- 이중 입고는 `requireStatus(APPROVED)`로 차단됩니다(`RECEIVED`에서 재진입 불가).
- 동시성 통합 테스트는 서로 다른 두 발주가 같은 품목을 동시에 최초 입고해도 `Stock`이 한 행만 생성되고 최종 수량이 두 입고량의 합과 같은지 확인합니다.

### 배운 점
- 상태 전이와 그 부수효과(재고)는 반드시 한 트랜잭션으로 묶어야 정합성이 유지된다.

---

## 6. PRD-구현 차이표

요구사항 확정 과정(`01_analyst_requirements.md` 7.1)에서 PRD 본문과 **반대로 결정된 4건**입니다. 구현은 확정안을 따랐고, PRD 원본은 보존했습니다.

| # | PRD 본문 | 구현(확정) | 사유 |
|---|---|---|---|
| OQ-3 취소 가능 범위 | FR-PO-005 설명상 `{DRAFT, REQUESTED}`만 취소 | **`{DRAFT, REQUESTED, APPROVED}`** 취소 가능. `RECEIVED`만 불가 | 승인 후에도 입고 전이면 취소 수요가 있다는 판단. 재고 반영 전이라 롤백 부담 없음(상태머신에 T8 추가) |
| OQ-4 입고 시 재고 반영 | 2.8.2/3.10.3에서 "확장 기능"(2차) | **1차 포함** — 입고 시 라인별 재고 증가 | 입고가 재고에 반영되지 않으면 입고 기능의 실효성이 떨어짐. `Stock` 엔티티/테이블 신규 도입 |
| OQ-6 승인/반려/입고 권한 | 3.11.3 권한 정책표는 **ADMIN만** | **ADMIN + MANAGER** | PRD 2.3 사용자 유형에서 매니저 주요 기능에 "발주 승인"이 포함됨(내부 충돌). 매니저 업무 실효성을 고려해 2.3을 우선 적용 |
| OQ-1 총금액 검증 | 3.8.3 "총금액은 라인 합계와 일치"(검증) | **서버 재계산** — 클라이언트 totalAmount 미수신, `lineAmount`/`totalAmount`를 서버가 계산·저장 | 클라이언트 값을 신뢰하면 위변조/불일치 위험. Request DTO에서 `totalAmount` 제거 |

그 외 확정 사항(PRD와 동일 방향): 동시 승인은 `@Version` 낙관적 락(OQ-5), 발주 취소는 사유 없음(OQ-8), DRAFT 수정 API 미제공(OQ-9), 상태 이력 엔티티 미도입(타임스탬프로 대체, OQ-10), 반려 시 approverId 기록(OQ-11), INACTIVE 거래처 발주는 `INVALID_STATUS` 재사용(OQ-12), 라인 단가 누락 시 품목 표준단가(OQ-13), 발주번호 일자별 4자리 시퀀스(OQ-14).

> ⚠️ **PRD 원본(`scm_system_PRD_TRD.md`)은 수정하지 않았습니다.** 위 차이는 구현·문서 기준입니다.

---

## 7. QA MINOR 4건과 권장 후속

QA 보고서(`_workspace/04_qa_report.md`)에서 BLOCKER/MAJOR는 0건이며, 아래 MINOR 4건은 동작에 문제가 없는 선택적 개선 항목입니다.

| # | 항목 | 현황 | 판정 | 권장 후속 |
|---|---|---|---|---|
| D-1 | 품목 수정 폼에서 `itemCode` readonly | `ItemService.update`는 `itemCode`를 변경하지 않고(`ItemUpdateRequest`에도 없음), readonly가 "코드 불변" 정책과 정합 | **정상(비이슈)** | 없음 |
| D-2 | 발주 상세의 반려 모달이 `rejectForm` 미주입 + 빈 사유 처리 | 상세 화면은 `name="rejectReason"` 직접 바인딩 + `required`. 빈 사유는 `PurchaseOrderService.reject()`가 서버에서 `INVALID_INPUT`으로 차단(REST는 `@Valid`로도 방어) | **정상(MINOR)** | (선택) Web reject 핸들러에 빈 사유 시 flash `errorMessage`로 사용자 피드백 개선 |
| D-3 | 관리자 목록(admin-list)에 행별 액션 버튼 없음, `isAdminView`/`rejectForm` 미사용 | `PurchaseOrderSummaryResponse`에 행별 플래그가 없어, 승인/반려/입고는 **상세 화면**에서 수행하도록 설계. 주입된 `isAdminView`/`rejectForm`은 무해(미참조) | **MINOR (계약-구현 경미한 차이)** | (선택) 목록 행 인라인 액션이 필요하면 Summary DTO에 행별 가능 여부 플래그 추가 — architect 판단 |
| D-4 | 상세에서 관리자 액션(approve/reject/receive) 후 redirect 목적지 | 관리자 액션은 모두 `redirect:/admin/purchase-orders`(관리자 목록)로 이동. 작성자 submit/cancel은 상세로 복귀 → 흐름 비대칭 | **MINOR (UX)** | (선택) 관리자 액션 후 `redirect:/purchase-orders/{poId}`(원 상세)로 복귀하면 흐름 일관 |

> 즉시 수정이 필요한 결함은 없습니다. D-3은 제품 의사결정(architect), D-2/D-4는 UX 개선 차원의 선택 항목입니다.

---

## 8. MySQL 스키마 드리프트 방지

### 문제

- 영속 MySQL에서 `ddl-auto=update`를 사용하면 어떤 DDL이 실행됐는지 코드 리뷰로 추적하기 어렵고, 개발·운영 스키마가 조용히 달라질 수 있습니다.
- H2 통합 테스트만으로는 MySQL 전용 타입, 외래키, CHECK, 인덱스 계약을 충분히 검증할 수 없습니다.

### 해결

- MySQL 프로필을 `ddl-auto=validate`로 바꾸고 Flyway의 버전 SQL만 스키마를 변경하도록 했습니다.
- 기준 마이그레이션에 7개 테이블, 외래키, UNIQUE, CHECK, 조회 인덱스를 명시했습니다.
- 기본 테스트는 H2를 사용하고, 별도 `mysqlSchemaTest`는 CI의 실제 MySQL 8.0 서비스에 Flyway를 적용한 뒤 JPA 매핑을 검증합니다.
- 동일 SQL은 로컬에서 H2 MySQL 모드로도 전체 파싱·실행해 문법 오류를 빠르게 탐지합니다.
- 기존 `ddl-auto=update` 데이터베이스는 테이블은 있지만 Flyway 이력이 없으므로 자동 baseline하지 않습니다. 데모 볼륨은 초기화하고, 실제 데이터는 백업·스키마 대조·baseline 승인 절차를 별도 루프로 수행합니다.

### 배운 점

- ORM 검증과 마이그레이션은 역할이 다르다. Flyway는 변경 이력을, Hibernate `validate`는 코드와 스키마의 현재 계약을 검증한다.

---

## 관련 문서

- [`../README.md`](../README.md) — 실행 방법, 시드 계정, 제약
- [`API_SPEC.md`](API_SPEC.md) — REST API 명세, 에러 코드
- [`ERD.md`](ERD.md) — 엔티티/테이블 구조
- [`STATE_MACHINE.md`](STATE_MACHINE.md) — 발주 상태 전이
