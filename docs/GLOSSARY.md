# 용어 사전 (Glossary)

> [처음 시작하기](./GETTING_STARTED.md) · [문서 지도](./INDEX.md) · [워크북](../practice/README.md)

워크북과 문서를 읽다가 낯선 용어를 만났을 때 찾는 치트시트입니다. 교과서 정의가 아니라 **이 저장소 구현 기준**으로 설명하고, 실제 파일을 옆에 적어 두었습니다.

여러 기술이 **왜 필요한지, 어떻게 함께 동작하는지**는 [초보자를 위한 기술 안내](./TECHNOLOGY_GUIDE.md)에서 요청 흐름과 실제 코드로 읽을 수 있습니다.

---

## 1. SCM 도메인 용어

| 용어 | 뜻 (이 저장소 기준) | 실제 코드 |
|---|---|---|
| 발주 (Purchase Order) | "이 물건을 이만큼 사고 싶다"는 구매 요청서. 헤더(총 정보) + 라인(품목별 줄)로 구성 | `domain/PurchaseOrder.java`, `PurchaseOrderLine.java` |
| 라인 (Line) | 발주서 안의 품목별 줄 한 건. 품목 × 수량 × 단가 | `PurchaseOrderLine.java` |
| 공급사 / 고객사 / BOTH | 거래처 유형. 공급사는 팔아주는 곳, 고객사는 사가는 곳, BOTH는 둘 다 | `enums/PartnerType.java` |
| 거래처 상태 | ACTIVE(거래 가능) / INACTIVE(비활성). 삭제하지 않고 상태로 막는다 | `Partner.java`의 `isActive()` |
| 단종 (DISCONTINUED) | 더 이상 발주할 수 없는 품목 상태. 데이터는 남긴다(과거 발주 이력 보존) | `Item.java`의 `discontinue()` |
| 현재고 (Stock) | 창고에 지금 있는 수량. 품목당 1행으로 관리 | `domain/Stock.java` |
| 입고 (Receive) | 승인된 발주의 실물이 도착해 검수·적재되는 것. 재고가 증가한다 | `PurchaseOrderService.receive()` |
| 안전재고 (Safety Stock) | 품목별 최소 보유 기준 수량. 현재고가 이하면 "부족"으로 경고 | `Item.safetyStock` |
| 부족수량 | `안전재고 − 현재고`(0 미만은 0). 얼마를 더 사야 하는지 | `dto/stock/StockListView.java` |
| 결재 요청 (Submit) | 작성자가 DRAFT 발주를 승인자에게 올리는 행위. REQUESTED로 전이 | `PurchaseOrder.submit()` |
| 승인 / 반려 (Approve / Reject) | ADMIN/MANAGER가 요청된 발주를 허가하거나 사유와 함께 거절 | `STATE_MACHINE.md` T3/T4 |
| 리드타임 | 발주 후 물건이 도착하기까지 걸리는 시간. 이 저장소에서는 납기일(dueDate)로 표현 | `PurchaseOrder.dueDate` |
| 마스터 데이터 | 반복 참조되는 기준 정보(거래처·카테고리·품목). CRUD로 관리 | `controller/web`의 Partner/Category/Item |

## 2. 웹 애플리케이션 계층 용어

아래는 공부할 파일 순서가 아니라, 실행 중인 요청이 통과하는 **런타임 순서**입니다.
처음 코드를 읽는 순서는 [REFERENCE_MAP](../practice/REFERENCE_MAP.md#처음-읽는-참조-구현-순서)을
따릅니다.

```
브라우저 → 필터 → MVC 인터셉터(로그인 확인) → 컨트롤러 → 서비스(비즈니스 규칙) → 리포지토리·ORM → DB
```

엔티티는 별도 처리 단계가 아니라 Service와 Repository가 다루는, DB에 매핑되는 Java 객체입니다.

| 용어 | 뜻 (이 저장소 기준) | 실제 코드 |
|---|---|---|
| 컨트롤러 (Controller) | HTTP 요청을 받아 서비스를 호출하고 화면/JSON을 돌려주는 입구. 비즈니스 규칙을 넣지 않는다 | `controller/web`(화면), `controller/api`(JSON) |
| 서비스 (Service) | 비즈니스 규칙과 트랜잭션 경계. "언제 거절하고, 무엇을 함께 저장할지"를 결정 | `service/PurchaseOrderService.java` |
| 리포지토리 (Repository) | DB 조회/저장을 대신 해 주는 인터페이스. SQL을 직접 쓰지 않아도 메서드 이름으로 쿼리가 생긴다 | `repository/*.java` |
| 엔티티 (Entity) | DB 테이블과 1:1로 연결되는 Java 클래스. `@Entity`가 붙는다 | `domain/User.java` 등 |
| DTO | 계층 사이를 오가는 데이터 전달 객체. 엔티티를 그대로 노출하지 않으려는 장치 | `dto/` 패키지 전체 |
| 뷰 / 템플릿 | 서버가 채워서 돌려주는 HTML. Thymeleaf 문법(`th:*`)으로 데이터를 꽂는다 | `resources/templates/` |
| 인터셉터 (Interceptor) | 컨트롤러 도달 전에 요청을 가로채는 필터링 장치. 로그인 여부 확인에 사용 | `common/auth/LoginInterceptor.java` |

## 3. Spring / JPA 기술 용어

| 용어 | 초보자용 한 줄 정리 | 이 저장소 예시 |
|---|---|---|
| 의존성 주입 (DI) | 필요한 객체를 외부에서 공급받는 것. 이 코드에서는 Lombok이 생성자를 만들고 Spring이 그 생성자에 빈을 넣는다 | 모든 Service/Controller |
| 빈 (Bean) | Spring이 대신 만들어 관리하는 객체. `@Service`, `@Component`를 붙이면 된다 | `service/OrderNumberGenerator.java` |
| 트랜잭션 | "전부 성공하거나 전부 취소되는" 작업 묶음. Spring은 기본적으로 밖으로 전달된 RuntimeException·Error에 롤백하고, checked exception은 별도 설정이 필요하다 | 입고 처리(상태 변경+재고 증가가 한 묶음) |
| 영속성 컨텍스트 | JPA가 엔티티를 추적하는 1차 캐시. 같은 트랜잭션 안에서 같은 ID 조회는 같은 객체가 돌아온다 | OSIV off 환경이라 Service 안에서만 유효 |
| 지연 로딩 (LAZY) | 연관 객체를 실제로 쓸 때까지 조회를 미루는 것. 영속성 컨텍스트가 닫힌 뒤 아직 읽지 않은 연관을 조회하려 하면 `LazyInitializationException`이 날 수 있다 | OSIV off라 Service에서 DTO로 변환해 해결 |
| Cascade | 부모 저장/삭제 시 자식도 함께 처리하는 전파 설정. 발주 헤더 저장 시 라인이 함께 저장된다 | `PurchaseOrder.lines`의 `CascadeType.ALL` |
| 낙관적 락 (`@Version`) | 동시 수정 충돌을 버전 번호로 감지. 충돌 시 `OptimisticLockingFailureException` 발생 | `PurchaseOrder.version`, `Stock.version` |
| 비관적 락 (`PESSIMISTIC_WRITE`) | 다른 트랜잭션이 건드리지 못하게 행을 잠그는 방식 | `ItemRepository.findAllByIdForUpdate()` |
| 세션 (Session) | 로그인 상태를 서버 메모리에 저장해 두는 인증 방식. 이 저장소의 인증 기반 | `common/auth/SessionConst.java` |
| CSRF | 로그인된 브라우저를 이용한 위조 요청 공격. 폼에는 토큰이 자동 삽입된다 | `config/SecurityConfig.java` |
| BCrypt | 비밀번호 저장용 해시. 평문 저장 금지, 매번 다른 해시가 나온다 | `service/AuthService.java` |
| Bean Validation | `@NotNull`, `@Positive` 같은 어노테이션으로 입력값을 자동 검증 | `dto/purchaseorder/PurchaseOrderCreateRequest.java` |
| Flyway | DB 스키마 변경을 버전 있는 SQL 스크립트로 관리하는 도구 | `resources/db/migration/mysql/` |
| H2 | 개발·학습용 가벼운 DB. in-memory 모드는 재시작 시 초기화된다 | `application.yml` 기본 설정 |
| OSIV | 웹 요청 처리 동안 영속성 컨텍스트를 열어 두는 기능. 물리적 DB 연결을 계속 점유한다는 뜻과는 다르다. 이 저장소는 **끄고** Service에서 DTO를 완성한다 | `application.yml`의 `open-in-view: false` |
| Pageable / Page | 페이지 번호·크기를 담아 조회하고 결과를 페이지 단위로 받는 Spring Data 기능 | 목록 API·화면 전체 |
| Specification | 조건을 조립해 동적 검색 쿼리를 만드는 JPA 도구 | `repository/spec/*.java` |

## 4. 워크북 진행 용어

| 용어 | 뜻 |
|---|---|
| starter | 빈칸(`____`/`TODO`)이 뚫린 원본 문제 조각. 직접 수정하지 않는다 |
| workspace | `practiceInit`으로 만드는 개인 풀이 공간. Git 추적 제외 |
| 참조 구현 | `src/` 아래의 완성 코드. 풀이 후 비교하는 기준 |
| 실패 사례 | 잘못된 입력/권한/상태에서 규칙이 거절하는 경우. 학습 완료 기준의 "실패 3건" |
| 확장 트랙 | 아직 제품 코드가 없는 공지·수주 기능을 새로 설계하는 과제 (05~06, 12~14, 26~27, 30번 모듈) |

---

용어 설명이 더 필요하면 [TROUBLESHOOTING.md](./TROUBLESHOOTING.md)(깊은 이유)와 [DESIGN_DECISIONS.md](../practice/DESIGN_DECISIONS.md)(설계 선택)를 참고하세요.
