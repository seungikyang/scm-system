# 트러블슈팅 & 설계 노트 (TROUBLESHOOTING)

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

---

## 1. 빌드 환경

### 문제
- 이 저장소는 **JDK 8만 설치된 환경에서 생성**되었습니다. Spring Boot 3.2.5는 JDK 17 이상이 필요하므로 로컬에서 `gradlew build`/`test`를 실행할 수 없었습니다.
- `gradle-wrapper.jar` 바이너리가 포함되지 않았습니다(`gradle/wrapper/`에 `.properties`와 `README.txt`만 존재).

### 해결 / 대응
- 코드는 Java 17 사양으로 작성하고, 임포트/타입/시그니처를 수기 검증했습니다(QA 단계에서 컴파일 블로커 0건 판정).
- wrapper jar는 아래 절차로 생성합니다.

```bash
gradle wrapper --gradle-version 8.5     # 로컬에 Gradle 8.5 필요
# 또는 IntelliJ/Eclipse에서 Gradle 프로젝트로 import (자동 생성)
```

### 권장
- **JDK 17 환경에서 `./gradlew compileJava` / `./gradlew test`를 실행**해 실제 컴파일·테스트를 확인하세요. 작성된 테스트는 `PurchaseOrderServiceTest`, `CategoryServiceTest`, `OrderNumberGeneratorTest` 3종(Mockito 단위 테스트, DB 불필요)입니다.

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
- 발주번호 `PO-YYYYMMDD-####`는 일자별 4자리 시퀀스입니다(`OrderNumberGenerator`가 `countByOrderNumberStartingWith + 1`로 산출). 동일 일자에 동시 작성이 일어나면 같은 번호가 생성될 수 있습니다.

### 해결
- `order_number`에 UNIQUE 제약(`uk_po_order_number`)을 두어 최종 방어선으로 삼았습니다.
- `saveWithOrderNumber`는 `saveAndFlush`로 UNIQUE 충돌을 즉시 감지하고, `DataIntegrityViolationException`이 나면 **재채번 재시도**(최대 5회)합니다. 초과 시 `INTERNAL_ERROR`.

### 알려진 한계 (주의)
- 같은 `@Transactional` 안에서 flush가 실패하면 영속성 컨텍스트가 오염될 수 있어, 실제 고동시성 충돌에서는 재시도가 완전히 보장되지 않을 수 있습니다(드문 케이스). 순차 작성은 정상 처리되며, UNIQUE 제약이 중복 저장 자체는 확실히 차단합니다.
- **고동시성 환경에서는 별도 시퀀스 테이블 또는 네임드 락 도입을 권장**합니다(1차 범위에서는 UNIQUE + 재시도로 수용).

### 배운 점
- "DB 제약 + 애플리케이션 재시도"는 단순하지만, 트랜잭션 경계 안에서의 flush 실패 처리에는 한계가 있다. 채번은 동시성 요구 수준에 따라 전략을 다르게 가져가야 한다.

---

## 5. 입고-재고 트랜잭션 원자성

### 문제
- 입고 시 발주 상태를 `RECEIVED`로 바꾸는 것과 라인별 재고를 증가시키는 것이 따로 처리되면, 한쪽만 반영되어 재고가 어긋날 수 있습니다.

### 해결
- `receive()`를 단일 `@Transactional`로 처리합니다: 상태 전이 + 모든 라인의 `Stock.increase`가 같은 트랜잭션입니다. 한 라인이라도 실패하면 상태 변경을 포함해 전체 롤백됩니다.
- `Stock`은 `itemId`로 조회해 없으면 생성(`new Stock(itemId, 0)`)하고 증가합니다. `item_id` UNIQUE 제약이 동시 최초 생성의 방어선입니다.
- 이중 입고는 `requireStatus(APPROVED)`로 차단됩니다(`RECEIVED`에서 재진입 불가).

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

## 관련 문서

- [`../README.md`](../README.md) — 실행 방법, 시드 계정, 제약
- [`API_SPEC.md`](API_SPEC.md) — REST API 명세, 에러 코드
- [`ERD.md`](ERD.md) — 엔티티/테이블 구조
- [`STATE_MACHINE.md`](STATE_MACHINE.md) — 발주 상태 전이
