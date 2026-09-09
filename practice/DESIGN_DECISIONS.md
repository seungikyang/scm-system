# 참조 구현 설계 기준

> [HTML 학습 목차](../index.html) · [워크북 안내](./README.md) · [루프 엔지니어링](../docs/LOOP_ENGINEERING.md) · [문서 지도](../docs/INDEX.md)

워크북에는 JPA 연관관계 중심 모델과 ID 참조 중심 모델처럼 비교 학습을 위한 대안이 포함되어 있습니다. 아래 내용은 현재 `src/`와 자동 테스트가 보장하는 기준입니다.

## 1. 학습 범위

- 핵심 구현: 사용자, 인증, 거래처, 카테고리, 품목, 발주, 입고와 안전재고 조회
- 확장 설계: 공지와 수주. starter와 문제는 있지만 현재 제품 코드의 정답 구현은 없습니다.
- 핵심 모듈에서는 실제 코드와 테스트를 행동의 기준으로 삼습니다.
- 확장 모듈에서는 요구사항의 빈틈을 먼저 결정하고 실패 테스트로 정책을 고정합니다.

## 2. 발주 상태와 권한

- 작성: `DRAFT`
- 요청: `DRAFT → REQUESTED`, 작성자 본인
- 승인: `REQUESTED → APPROVED`, `ADMIN` 또는 `MANAGER`
- 반려: `REQUESTED → REJECTED`, `ADMIN` 또는 `MANAGER`, 반려 사유 필수
- 입고: `APPROVED → RECEIVED`, `ADMIN` 또는 `MANAGER`
- 취소: `{DRAFT, REQUESTED, APPROVED} → CANCELED`, 작성자 본인
- 상세 조회: 작성자 본인 또는 `ADMIN`/`MANAGER`

입고에서는 상태 변경과 모든 발주 라인의 재고 증가가 같은 트랜잭션으로 처리됩니다. 품목 행을 ID 순서로 비관적 잠금해 최초 재고 생성 경합을 줄이고, 수량 오버플로는 도메인에서 거부합니다. 총금액과 라인 금액은 클라이언트 값을 신뢰하지 않고 서버에서 계산합니다.

재고 목록은 `Stock`만 조회하지 않고 `Item LEFT JOIN Stock`을 사용합니다. 재고 행은 첫 입고 때 생성되므로 Stock 기준 조회는 입고 전 품목을 누락하기 때문입니다. 운영 품목은 현재고 0으로도 노출하고 `현재고 <= 안전재고`를 부족 조건으로 계산합니다.

## 3. 연관관계 모델링 비교

starter의 일부 엔티티는 `Item → Category`, `PurchaseOrder → Partner`, `PurchaseOrderLine → Item`을 `@ManyToOne`으로 매핑합니다. 이는 LAZY 로딩, cascade와 객체 그래프를 연습하기 위한 모델입니다.

현재 참조 구현은 다른 aggregate의 식별자만 저장합니다.

- `Item.categoryId`
- `PurchaseOrder.partnerId`, `writerId`, `approverId`
- `PurchaseOrderLine.itemId`
- `Notice.writerId`를 구현한다면 같은 원칙을 검토

반면 발주 헤더와 라인은 한 aggregate이므로 `PurchaseOrder.lines` 관계와 cascade를 사용합니다. ID 참조는 aggregate 경계를 분명히 하고 불필요한 객체 그래프 로딩을 줄이며, 표시용 이름은 Service에서 조회해 DTO에 채웁니다. 어느 방식이 무조건 정답은 아니지만 한 코드 경계 안에서 선택을 일관되게 유지해야 합니다.

## 4. 인증과 Spring Security의 역할

현재 구현은 하이브리드 구성입니다.

- `LoginInterceptor`: 세션의 로그인 여부 확인
- `CurrentUserArgumentResolver`: 세션 사용자를 Controller 인자로 변환
- Service의 `Authz`: 역할과 소유권 인가
- Spring Security filter chain: 웹 폼 CSRF와 기본 보안 헤더

Spring Security가 인증과 인가까지 모두 처리하는 구성도 가능하지만, 현재 프로젝트에서는 form login이나 `SecurityContext` 인증을 사용하지 않습니다. 두 체계를 역할 구분 없이 중복 적용하면 응답 코드, 로그인 경로, CSRF 처리와 권한 규칙이 충돌할 수 있습니다. 하이브리드 자체가 오류인 것은 아니며 각 계층의 책임과 테스트가 명확해야 합니다.

Thymeleaf에서 `th:action`을 사용하는 POST 폼은 Spring Security와 통합될 때 CSRF hidden input이 자동 삽입됩니다. 일반 HTML action, JavaScript 요청 또는 별도 클라이언트에서는 토큰을 직접 전달해야 합니다. 현재 JSON `/api/**`는 기존 API 호환을 위해 CSRF 예외이며, 이를 외부 서비스로 노출할 때는 토큰 기반 인증과 명시적 CORS 정책을 다시 설계해야 합니다.

## 5. 개발 환경 안전 기본값

- H2 console은 `H2_CONSOLE_ENABLED=false`가 기본이며, 활성화해도 로그인 후 `ADMIN`만 접근합니다.
- 테스트용 초기 데이터는 환경 변수로 제어하며 운영에서는 비활성화합니다.
- `ddl-auto: create`는 H2 학습용 설정입니다. MySQL은 Flyway 버전 SQL만 스키마를 변경하고 Hibernate는 `validate`를 사용합니다.
- CI는 기본 H2 테스트와 실제 MySQL 8 마이그레이션/JPA 검증을 분리해 실행합니다.
- SQL과 파라미터 로그는 민감 정보 및 로그량 때문에 기본적으로 최소화합니다.

## 6. 문서 충돌 처리

핵심 트랙에서 PRD/TRD, starter, 답안, 현재 코드가 다르면 다음 순서로 판단합니다.

1. 실행되는 테스트가 보장하는 행동
2. 이 문서와 상태 머신/API 명세
3. 현재 제품 코드
4. 원 PRD/TRD의 초안

차이를 발견하면 한쪽을 조용히 베끼지 말고 선택지, 트레이드오프, 최종 결정을 학습 기록에 남깁니다. 이 과정이 요구사항 분석 훈련입니다.
