# 문제와 참조 구현 연결표

> [HTML 학습 목차](../index.html) · [워크북 안내](./README.md) · [루프 엔지니어링](../docs/LOOP_ENGINEERING.md) · [문서 지도](../docs/INDEX.md)

문제를 먼저 푼 뒤 아래 경로를 따라 실제 구현과 테스트를 비교합니다. 확장 모듈에는 현재 참조 코드가 없으므로 스스로 계약과 테스트를 먼저 만들어야 합니다.

| 모듈 | 확인할 참조 구현 | 확인할 증거 / 관찰점 |
|---|---|---|
| 00 | `build.gradle`, `application*.yml`, `db/migration/mysql/` | H2 학습 설정, Flyway, MySQL `validate`, 품질 게이트 |
| 01~04 | `src/main/java/com/example/scm/domain/` | enum 문자열 저장, 불변식, 상태 전이, aggregate 경계 |
| 07~09, 28, 31~32 | `repository/`, `service/ItemService.java`, 관련 DTO | 검색·페이징, ID 참조, DTO 변환 시점 |
| 10~11, 29 | `PurchaseOrderService`, `StockService`, `domain/PurchaseOrder.java` | 서버 금액 계산, 상태 전이, 동시 입고, 안전재고 조회 |
| 15~19 | `common/exception/`, `controller/api/` | 에러 응답 계약, Validation과 Service 검증 분리 |
| 20, 23, 33~35 | `common/auth/`, `config/`, 인증 Controller/Service, templates | 세션 인증, Service 인가, CSRF와 보안 헤더의 역할 분담 |
| 21 | `PurchaseOrderFlowIntegrationTest`, `PurchaseOrderConcurrencyIntegrationTest`, `StockFeatureIntegrationTest` | 정상 흐름, 두 스레드 경합, 재고 화면·API 증거 |
| 22, 36~39 | 루트 문서, 패키지 구조, 테스트 보고 | 구현 결정을 자신의 말로 설명하는지 점검 |
| 24~25 | `PartnerService`, `CategoryService`, API/Web Controller | unique 다층 방어, 삭제/비활성 정책 |
| 05~06, 12~14, 26~27, 30 | 현재 참조 구현 없음 | 확장 트랙: 계약 → 실패 테스트 → 최소 구현 순서 |

## 발주 수직 흐름 읽기 순서

1. 요청 DTO의 Validation을 읽고 형식 검증 범위를 찾습니다.
2. API Controller에서 인증 사용자가 어떻게 주입되는지 확인합니다.
3. Service에서 거래처·품목·권한·상태를 어떻게 검증하는지 찾습니다.
4. `PurchaseOrder`와 `PurchaseOrderLine`에서 상태 전이와 금액 불변식을 찾습니다.
5. Repository의 잠금 및 조회 전략을 확인합니다.
6. 테스트에서 정상 흐름, 잘못된 상태, 권한 실패, 경합 사례를 찾아 표로 만듭니다.

소스 경로는 패키지 재구성으로 달라질 수 있습니다. 파일명이 없으면 `rg --files src | rg 'PurchaseOrder|Security|Workbook'`처럼 이름으로 찾습니다.
