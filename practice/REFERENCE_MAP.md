# 문제와 참조 구현 연결표

> [HTML 학습 목차](../index.html) · [워크북 안내](./README.md) · [루프 엔지니어링](../docs/LOOP_ENGINEERING.md) · [문서 지도](../docs/INDEX.md)

문제를 먼저 푼 뒤 아래 경로를 따라 실제 구현과 테스트를 비교합니다. 표의 행은
모듈 번호순이 아니라 워크북의 **권장 학습 순서**입니다. 확장 모듈에는 현재 참조 코드가
없으므로 스스로 계약과 테스트를 먼저 만들어야 합니다.

| 단계·모듈 | 확인할 참조 구현 | 확인할 증거 / 관찰점 |
|---|---|---|
| 기반 00 | `build.gradle`, `application*.yml`, `ScmApplication` | 의존성, H2 학습 설정, MySQL/Flyway 계약, 시작점 |
| 기반 01 → 04 | `domain/User`, `Category`·`Partner`, `Item`, `PurchaseOrder`·`PurchaseOrderLine` | ID와 컬럼 → 마스터 상태 → 금액 타입 → aggregate와 상태 전이 |
| 기반 07 | `ItemRepository`, 이후 `repository/spec/ItemSpecs` | 파생 쿼리와 페이징을 먼저 보고, 동적 조건은 09에서 이어서 확인 |
| 발주 08 → 09 | `ItemService`의 create → search, `ItemSpecs`, 품목 DTO | 트랜잭션·중복 검사 다음에 검색·Page 변환을 읽음 |
| 발주 10 → 11 | `PurchaseOrderService`의 작성 → 상태 전이·입고, `OrderNumberGenerator`, `Stock` | 서버 금액 계산, 헤더·라인 저장, 상태·권한, 동시 입고 |
| 발주 15 → 17 | `common/exception/`, `common/response/`, 요청 DTO | 에러 모델 → 전역 처리 → 입력 형식 검증과 업무 검증 분리 |
| 발주 18 → 19 | `ItemApiController`, 사용자/관리자 `PurchaseOrderApiController` | HTTP 계약이 앞서 만든 Service와 예외·DTO에 연결되는지 확인 |
| 발주 21 | `PurchaseOrderFlowIntegrationTest`, `PurchaseOrderConcurrencyIntegrationTest`, `StockFeatureIntegrationTest` | 정상 흐름 → 경합 → 화면·API 관찰 순으로 증명 |
| 발주 29 | `PurchaseOrderService`의 조회·취소, 관련 Web Controller와 응답 DTO | 작성자 소유권, 상세 접근, 취소 가능 상태 |
| 인증·마스터 20 → 23 | `SecurityConfig`, `AuthService`, 로그인 API/Web Controller, `UserService` | 보안 역할 분담을 이해한 뒤 로그인·세션 생성 구현을 읽음 |
| 인증·마스터 24 → 25 | `PartnerService`, `CategoryService`, 해당 API/Web Controller | unique 다층 방어, 삭제/비활성 정책, 폼 오류 흐름 |
| 인증·마스터 28 | `ItemService`의 상세·수정·단종, Item Controller | 불변 품목코드, 변경 감지, soft delete |
| 인증·마스터 31 → 32 | 전체 `repository/`, 응답 DTO와 Service 변환 메서드 | 도메인별 조회·잠금 책임을 점검한 뒤 OSIV off DTO 변환 확인 |
| 인증·마스터 33 → 35 | `config/`, `common/auth/`, `controller/web/`, `templates/` | Bean 설정 → 인터셉터/현재 사용자 주입 → Thymeleaf 화면 |
| 회고 22 → 36 → 37 → 38 → 39 | 루트 문서, 패키지 구조, 규칙표, 테스트·장애 기록 | 구현 결정을 문서와 자신의 말로 설명하는지 점검 |
| 확장 05 → 06 → 12 → 13 → 14 → 26 → 27 → 30 | 현재 참조 구현 없음 | 공지·수주를 Entity → Service → Controller → 조회 권한 순으로 설계 |

## 처음 읽는 참조 구현 순서

전체 코드를 처음부터 패키지 이름순으로 읽지 않습니다. 다음 한 줄을 첫 번째 코드 읽기
경로로 사용합니다.

```text
ScmApplication / 설정
→ User → Category·Partner → Item → PurchaseOrder·Line
→ ItemRepository → ItemService(create → search)
→ PurchaseOrderService(create → submit/approve/reject/receive)
→ ErrorCode·ExceptionHandler → 요청 DTO
→ API Controller → 통합 테스트 → 조회·취소
→ SecurityConfig → AuthService·로그인 Controller
→ 마스터 Service·Controller → 전체 Repository·응답 DTO
→ WebMvcConfig·Interceptor·ArgumentResolver → Thymeleaf
```

이것은 **학습 순서**입니다. 실제 HTTP 요청은 Filter/Interceptor → Controller → Service →
Repository → DB 순서로 실행됩니다. 두 순서를 섞지 않습니다.

## 두 번째 읽기: 발주 요청 한 건 추적하기

기반 트랙을 끝낸 뒤에는 실행 방향으로 되짚어 봅니다.

1. 요청 DTO의 Validation에서 형식 검증 범위를 찾습니다.
2. API Controller에서 URL·HTTP 상태와 현재 사용자 주입을 확인합니다.
3. Service에서 거래처·품목·권한·상태를 검증하는 순서를 찾습니다.
4. `PurchaseOrder`와 `PurchaseOrderLine`에서 상태 전이와 금액 불변식을 확인합니다.
5. Repository의 저장·잠금·조회 전략을 확인합니다.
6. 테스트에서 정상 흐름, 잘못된 상태, 권한 실패, 경합 사례를 표로 만듭니다.

소스 경로는 패키지 재구성으로 달라질 수 있습니다. 파일명이 없으면 `rg --files src | rg 'PurchaseOrder|Security|Workbook'`처럼 이름으로 찾습니다.
