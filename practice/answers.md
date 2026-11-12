# 객관식 정답·해설

[먼저 문제 풀기](sw-quiz/index.html?bank=workbook) · [전체 문항](workbook-questions.md)

브라우저·Markdown 문제집과 같은 TSV에서 생성한 정답표입니다. 보기 순서와 정답 번호가 동일합니다.

## 0. 빌드와 애플리케이션 설정 (TRD 3.1, 3.2)

- [W001](workbook-questions.md#w001) · **1번**: HTTP 처리·영속성·입력 검증·서버 HTML·DB
  - 해설: 각 의존성은 요청 처리부터 저장과 화면 생성까지 서로 다른 책임을 담당합니다.
- [W002](workbook-questions.md#w002) · **2번**: Java 17 대상 컴파일과 jakarta 패키지를 사용한다
  - 해설: 정식 기준은 build.gradle이며 Servlet·Validation·Persistence에는 jakarta 이름 공간을 사용합니다.
- [W003](workbook-questions.md#w003) · **4번**: 스키마와 엔티티 매핑의 일치 여부를 검사한다
  - 해설: 이 프로젝트의 MySQL 스키마 변경은 Flyway가 맡고 Hibernate는 validate로 확인합니다.
- [W004](workbook-questions.md#w004) · **3번**: H2_CONSOLE_ENABLED=true로 실행한다
  - 해설: application.yml은 콘솔을 기본 비활성화하고 환경 변수로 활성화합니다.
- [W323](workbook-questions.md#w323) · **4번**: create는 새 스키마 생성, create-drop은 종료 시 삭제, validate는 매핑 검사, none은 자동 DDL 없음
  - 해설: update는 매핑에 맞춘 스키마 변경을 시도합니다. 현재 운영형 MySQL은 Flyway와 validate를 사용합니다.
- [W324](workbook-questions.md#w324) · **1번**: MySQL Connector/J와 Flyway MySQL 지원
  - 해설: 프로젝트는 MySQL JDBC 드라이버와 Flyway의 MySQL 모듈을 runtimeOnly로 선언합니다.
- [W325](workbook-questions.md#w325) · **2번**: 루트 빌드·Wrapper 설정과 문서의 버전 불일치를 줄이기 위해서다
  - 해설: 코드의 정식 설정을 기준으로 문서를 동기화하고 변경된 조합을 검증합니다.
- [W326](workbook-questions.md#w326) · **4번**: tools.jackson.databind와 spring-boot-starter-webmvc-test
  - 해설: 정식 소스 import와 build.gradle에서 실제 패키지·starter를 확인합니다.
- [W360](workbook-questions.md#w360) · **1번**: DB 조회·변경 기능이 데이터 노출·변조 경로가 될 수 있기 때문이다
  - 해설: 기본 비활성화와 활성화 시 서버 접근 통제를 함께 적용합니다.
- [W364](workbook-questions.md#w364) · **3번**: hibernate.format_sql: true
  - 해설: format_sql은 SQL 표시 형식을 조절합니다. 출력 여부나 DB 스키마 변경 설정과는 구분합니다.
- [W365](workbook-questions.md#w365) · **3번**: true
  - 해설: show-sql=true는 SQL 콘솔 출력을 켭니다. 로깅 프레임워크의 org.hibernate.SQL 설정과 구분하고 운영 로그 정책에 맞춰 사용합니다.
- [W366](workbook-questions.md#w366) · **1번**: TRACE
  - 해설: JDBC 파라미터 바인딩 상세 로그는 TRACE 수준으로 확인합니다. 실제 개인정보·비밀번호 등이 로그에 노출되지 않도록 학습 환경에서 필요한 범위만 사용합니다. 참조: https://docs.hibernate.org/orm/7.0/logging/logging.html
- [W367](workbook-questions.md#w367) · **4번**: spring-boot-starter-webmvc
  - 해설: 루트 build.gradle은 Spring MVC 요청 처리에 spring-boot-starter-webmvc를 사용합니다.
- [W368](workbook-questions.md#w368) · **2번**: spring-boot-starter-data-jpa
  - 해설: spring-boot-starter-data-jpa는 영속성 구현과 Spring Data JPA 사용을 위한 의존성입니다.
- [W369](workbook-questions.md#w369) · **3번**: spring-boot-starter-validation
  - 해설: 입력 DTO 제약과 검증 구현을 사용하도록 spring-boot-starter-validation을 선언합니다. 실제 요청 검증을 적용할 지점도 지정해야 합니다.
- [W370](workbook-questions.md#w370) · **4번**: spring-boot-starter-thymeleaf
  - 해설: Thymeleaf starter는 서버에서 모델 값으로 HTML 템플릿을 렌더링하는 구성을 제공합니다.
- [W371](workbook-questions.md#w371) · **1번**: spring-boot-starter-security
  - 해설: Security starter로 필터 기반 보안을 구성합니다. 현재 프로젝트의 로그인·업무 인가는 세션 인터셉터와 서비스 가드도 사용합니다.
- [W372](workbook-questions.md#w372) · **2번**: com.h2database:h2
  - 해설: 기본 인메모리 학습 DB의 JDBC 드라이버는 com.h2database:h2입니다. Boot 4의 H2 콘솔 자동 설정 모듈은 별도 의존성입니다.

## 1. User 엔티티 (TRD 3.3.1, 3.6.1)

- [W005](workbook-questions.md#w005) · **3번**: Entity는 영속 객체, Table은 테이블, Id는 키, GeneratedValue는 키 생성 전략이다
  - 해설: 매핑 어노테이션은 객체와 테이블·기본키의 관계를 정의합니다.
- [W006](workbook-questions.md#w006) · **2번**: 이메일은 중복을 막고 비밀번호는 BCrypt 해시로 저장한다
  - 해설: 식별자 중복 방지와 비밀번호 보호는 서로 다른 보안·무결성 책임입니다.
- [W007](workbook-questions.md#w007) · **1번**: enum 선언 순서가 바뀌어도 저장된 이름의 의미를 유지하기 위해서다
  - 해설: STRING은 이름을 저장합니다. enum 이름 변경에는 별도 마이그레이션이 필요합니다.
- [W008](workbook-questions.md#w008) · **4번**: 감사 어노테이션·리스너와 JPA Auditing 설정을 연결한다
  - 해설: 참조 구현은 BaseTimeEntity와 JpaAuditingConfig로 생성·수정 시각을 관리합니다.
- [W234](workbook-questions.md#w234) · **2번**: 로그인·권한 계정과 공급·고객 거래처의 책임·생명주기가 다르기 때문이다
  - 해설: 거래처 마스터는 로그인 계정과 다른 업무 개념입니다.
- [W253](workbook-questions.md#w253) · **1번**: UserRole enum
  - 해설: 사용자 역할과 거래처 유형·품목 상태·주문 상태를 별도 enum으로 구분합니다.
- [W373](workbook-questions.md#w373) · **2번**: @Entity
  - 해설: @Entity는 JPA 관리 대상 타입을 선언합니다. 서비스·컨트롤러·설정 클래스 표시는 목적이 다릅니다.

## 2. Category 엔티티 / Partner 엔티티 (TRD 3.3.2 ~ 3.3.3, 3.5.2 ~ 3.5.3, 3.6.2)

- [W009](workbook-questions.md#w009) · **3번**: 동일한 이름의 카테고리 중복을 차단하기 위해서다
  - 해설: 사용자에게 식별되는 이름의 중복은 Service 검사와 DB 제약으로 막습니다.
- [W010](workbook-questions.md#w010) · **1번**: 탐색 편의와 양방향 동기화 비용을 함께 고려한다
  - 해설: 양방향 매핑은 탐색을 돕지만 관계 양쪽을 일관되게 관리해야 합니다.
- [W011](workbook-questions.md#w011) · **4번**: 도메인 최대 길이에 맞춰 DB 컬럼과 DTO 제약을 정한다
  - 해설: DB 저장 한도와 사용자 입력 계약을 함께 맞춰 예측 가능한 검증을 제공합니다.
- [W012](workbook-questions.md#w012) · **2번**: 같은 사업체가 중복 거래처로 등록되는 것을 막는다
  - 해설: 업무 식별자에 대한 최종 중복 방어선입니다.
- [W013](workbook-questions.md#w013) · **4번**: 중복 비교와 저장 전에 동일한 정규화 규칙을 적용한다
  - 해설: 어느 계층에 두더라도 모든 입력 경로의 비교·저장 표현이 같아야 합니다.
- [W014](workbook-questions.md#w014) · **2번**: 공급·판매 가능 여부를 도메인 규칙으로 모은다
  - 해설: SUPPLIER·CUSTOMER·BOTH의 업무 의미를 헬퍼에 모으면 조건 중복을 줄일 수 있습니다.
- [W015](workbook-questions.md#w015) · **1번**: ACTIVE / INACTIVE
  - 해설: 거래처 활성 여부는 주문 상태나 사용자 역할과 다른 enum입니다.
- [W016](workbook-questions.md#w016) · **3번**: 과거 이력은 보존하고 신규 주문 사용을 차단한다
  - 해설: 상태 변경으로 신규 사용을 제한하면서 거래 이력을 유지합니다.
- [W361](workbook-questions.md#w361) · **2번**: enum은 허용 조합을 명시하고 플래그는 조합 유효성 정책을 별도로 정해야 한다
  - 해설: 현재 업무에서 허용하는 역할 조합과 확장 요구에 맞춰 표현 방식을 선택합니다.
- [W374](workbook-questions.md#w374) · **4번**: 필수 입력 검증으로 거부한다
  - 해설: PartnerCreateRequest는 partnerType에 @NotNull을 선언합니다. starter의 기본값 선택 예시는 대안 연습이므로 현재 API 정책과 혼동하지 않습니다.
- [W375](workbook-questions.md#w375) · **1번**: ACTIVE
  - 해설: Partner 생성자는 상태를 지정하지 않았을 때 PartnerStatus.ACTIVE를 사용합니다.
- [W376](workbook-questions.md#w376) · **3번**: INACTIVE
  - 해설: 해당 starter의 IllegalStateException 가드는 이미 INACTIVE인지 검사하는 연습입니다. 현재 실행되는 Partner.deactivate는 INACTIVE 재대입을 허용하므로 전제를 구분합니다.

## 3. Item 엔티티 (TRD 3.3.4, 3.5.4, 3.6.3)

- [W017](workbook-questions.md#w017) · **3번**: Item에서 ManyToOne과 category 외래키를 사용한다
  - 해설: 여러 품목이 같은 카테고리를 참조하는 N:1 관계입니다.
- [W018](workbook-questions.md#w018) · **1번**: 필요한 조회에서 관계 로딩 범위를 선택하기 위해서다
  - 해설: LAZY는 불필요한 로딩을 줄이지만 DTO 변환 시 영속성 컨텍스트와 조회 계획이 필요합니다.
- [W019](workbook-questions.md#w019) · **4번**: ACTIVE / DISCONTINUED
  - 해설: 품목은 운영 상태와 단종 상태를 별도로 표현합니다.
- [W020](workbook-questions.md#w020) · **2번**: 과거 주문 라인의 참조와 거래 이력을 보존하기 위해서다
  - 해설: 단종은 신규 사용 제한이며 과거 데이터를 파괴하는 삭제와 다릅니다.
- [W021](workbook-questions.md#w021) · **2번**: 십진 금액의 정밀도와 반올림 정책을 명시하기 위해서다
  - 해설: BigDecimal을 적절한 생성·스케일·반올림 정책과 함께 사용합니다.
- [W179](workbook-questions.md#w179) · **4번**: Category
  - 해설: Item은 하나의 카테고리를 참조하며 저장·변경 때 존재 여부를 검증합니다.
- [W377](workbook-questions.md#w377) · **4번**: ACTIVE
  - 해설: Item 생성 및 등록은 신규 품목을 ACTIVE로 시작합니다. 주문 상태 enum과 품목 상태 enum을 구분합니다.
- [W378](workbook-questions.md#w378) · **1번**: DISCONTINUED
  - 해설: 해당 starter의 재단종 거부 전제에서는 DISCONTINUED를 비교합니다. 현재 실행되는 Item.discontinue는 상태 재대입을 허용하므로 참조 구현과 대안 가드를 구분합니다.

## 4. PurchaseOrder 엔티티와 라인 (TRD 3.3.5, 3.3.6, 3.6.4)

- [W022](workbook-questions.md#w022) · **4번**: 시각·시간대 없이 업무 날짜만 표현하기 위해서다
  - 해설: 날짜만 필요한 업무 필드와 승인·입고의 시각 필드를 구분합니다.
- [W023](workbook-questions.md#w023) · **1번**: DRAFT
  - 해설: 작성과 요청·승인·입고를 구분하기 위해 초깃값을 DRAFT로 설정합니다.
- [W024](workbook-questions.md#w024) · **3번**: 승인 전이에서 승인자를, 반려 전이에서 사유를 기록한다
  - 해설: 상태와 감사 정보는 해당 전이가 성공할 때 함께 기록해야 합니다.
- [W025](workbook-questions.md#w025) · **1번**: 작성과 승인 행위자의 책임·이력을 구분하기 위해서다
  - 해설: 행위자 필드는 역할이 다르며 ID 분리 자체가 인가를 대신하지는 않습니다.
- [W026](workbook-questions.md#w026) · **3번**: 라인 생명주기를 헤더에 묶고 컬렉션 제거 시 삭제 영향을 고려한다
  - 해설: cascade·고아 삭제와 트랜잭션 원자성은 별개의 기능입니다.
- [W027](workbook-questions.md#w027) · **2번**: 모든 변경 경로가 동일한 서버 재계산 규칙을 사용하게 한다
  - 해설: 도메인 또는 Service의 배치는 설계 선택이지만 금액 계산은 서버에서 일관되어야 합니다.
- [W181](workbook-questions.md#w181) · **2번**: DRAFT → REQUESTED
  - 해설: 작성자가 유효한 DRAFT 발주를 요청하며 이미 요청된 상태를 재요청하면 상태 오류가 됩니다.
- [W185](workbook-questions.md#w185) · **4번**: 주문 공통 정보와 여러 품목·수량·단가 라인
  - 해설: 헤더는 거래처·작성일 같은 공통 정보, 라인은 개별 주문 항목을 표현합니다.
- [W231](workbook-questions.md#w231) · **4번**: 집합 간 결합·로딩 범위를 줄이고 Service에서 존재·권한을 검증한다
  - 해설: ID 참조는 선택이며 탐색·참조 무결성 확인 책임을 명시적으로 관리해야 합니다.
- [W233](workbook-questions.md#w233) · **1번**: 라인·총금액·상태 불변식을 주문 단위로 일관되게 관리하기 위해서다
  - 해설: 주문 전체가 일관된 구성·금액·생명주기 경계를 갖게 합니다.
- [W254](workbook-questions.md#w254) · **4번**: CANCELED
  - 해설: 현재 발주는 작성자의 DRAFT·REQUESTED·APPROVED 취소만 허용합니다.
- [W271](workbook-questions.md#w271) · **3번**: 허용 상태 집합을 명시하고 잘못된 상태 표현을 줄인다
  - 해설: enum 정의만으로 전이 규칙이 지켜지지는 않습니다. 도메인 메서드의 상태 가드도 필요합니다.
- [W379](workbook-questions.md#w379) · **3번**: @ManyToOne
  - 해설: 객체 연관을 사용하는 이 starter의 발주→거래처는 다대일 관계입니다. 현재 실행 엔티티는 연관 객체 대신 partnerId를 저장하는 별도 설계를 사용합니다.
- [W380](workbook-questions.md#w380) · **2번**: DRAFT·REQUESTED·APPROVED
  - 해설: PurchaseOrder.requireCancelable은 진행 중인 세 상태만 허용하며 입고 완료와 종료 상태의 취소를 막습니다.

## 5. Notice 엔티티 (TRD 3.3.7, 3.5.7)

- [W028](workbook-questions.md#w028) · **4번**: 긴 본문을 저장할 수 있는 컬럼 용량을 확보하기 위해서다
  - 해설: 저장 용량과 XSS 방어는 별도로 고려해야 합니다.
- [W029](workbook-questions.md#w029) · **2번**: important DESC, createdAt DESC
  - 해설: 참조 구현은 중요도·생성시각·ID 내림차순으로 순서를 안정화합니다.
- [W030](workbook-questions.md#w030) · **3번**: 여러 요청의 증가분이 덮어써져 유실될 수 있다
  - 해설: 원자적 UPDATE view_count = view_count + 1로 증가분 유실을 방지할 수 있습니다.
- [W228](workbook-questions.md#w228) · **1번**: Repository·관련 Service 등 필요한 협력 객체를 외부에서 받는다
  - 해설: RequiredArgsConstructor와 final 필드를 통해 필수 협력 객체를 주입합니다.
- [W381](workbook-questions.md#w381) · **4번**: 0L
  - 해설: 아직 조회되지 않은 공지는 조회수 0으로 시작합니다. 조회수의 원자적 증가는 별도 조회 처리 규칙입니다.

## 6. SalesOrder 엔티티와 라인 (TRD 3.3.8, 3.6.5)

- [W031](workbook-questions.md#w031) · **1번**: DRAFT → CONFIRMED → SHIPPED → COMPLETED
  - 해설: 취소 가능 상태는 별도로 관리하며 COMPLETED·CANCELED는 종료 상태입니다.
- [W032](workbook-questions.md#w032) · **4번**: 역할·소유권 정책을 명시하며 ID 분리만으로 동일인을 금지하지 않는다
  - 해설: 행위자 동일 여부는 명시적인 업무 정책이며 기술 매핑에서 자동으로 결정되지 않습니다.
- [W033](workbook-questions.md#w033) · **3번**: 아직 출고·완료 이벤트가 발생하지 않았기 때문이다
  - 해설: 상태 전이 시 해당 이벤트 시각을 함께 설정합니다.
- [W034](workbook-questions.md#w034) · **2번**: 헤더의 라인 관계와 cascade·삭제 정책을 명시한다
  - 해설: 라인은 주문 구성 요소이며 품목·거래처 마스터와 같은 삭제 정책을 쓰지 않습니다.
- [W180](workbook-questions.md#w180) · **2번**: DRAFT
  - 해설: 작성 후 confirm을 통해 CONFIRMED로 전이합니다.
- [W256](workbook-questions.md#w256) · **3번**: DRAFT → CONFIRMED
  - 해설: 확정과 출고·완료는 별도 명령과 검증 경계를 갖습니다.

## 7. Repository 메서드 쿼리 (TRD 3.7.4, 3.8.2)

- [W035](workbook-questions.md#w035) · **1번**: 앞은 존재 여부, 뒤는 품목 조회를 표현한다
  - 해설: Spring Data는 메서드 이름의 접두사와 속성 조건으로 쿼리를 구성합니다.
- [W036](workbook-questions.md#w036) · **4번**: 이름 또는 코드가 주어진 문자열을 포함한다
  - 해설: Or는 OR 결합이고 Containing은 부분 문자열 검색을 표현합니다.
- [W037](workbook-questions.md#w037) · **4번**: Page<Item>
  - 해설: Page는 데이터와 총 개수·페이지 수 같은 메타 정보를 제공합니다.
- [W038](workbook-questions.md#w038) · **1번**: category.id를 기준으로 조회 조건을 만든다
  - 해설: findByCategoryId 같은 속성 경로 조건 또는 Specification을 사용합니다.
- [W229](workbook-questions.md#w229) · **2번**: Spring Data가 Repository 프록시 구현을 제공한다
  - 해설: 컨테이너가 등록한 런타임 프록시가 쿼리 메서드를 처리합니다.
- [W252](workbook-questions.md#w252) · **2번**: 카테고리 조건 AND keyword 조건
  - 해설: 동일 카테고리 안에서 이름·코드 조건을 검색하려면 범위 조건과 검색 조건을 AND로 묶습니다.
- [W273](workbook-questions.md#w273) · **4번**: findAll(specification, pageable)
  - 해설: JpaSpecificationExecutor의 조건과 Pageable을 함께 사용해 필터된 페이지를 조회합니다.

## 8. 품목 등록 Service (TRD 3.8.2)

- [W039](workbook-questions.md#w039) · **3번**: 업무 작업을 묶는 Service의 프록시 호출 메서드에 둔다
  - 해설: Service 경계에서 여러 Repository 작업이 함께 성공·실패하도록 묶습니다.
- [W040](workbook-questions.md#w040) · **2번**: Service 사전 검사와 DB unique 제약
  - 해설: 사전 검사는 친절한 오류를, unique 제약은 경쟁 요청의 최종 방어를 제공합니다.
- [W041](workbook-questions.md#w041) · **3번**: Service
  - 해설: 외부 입력의 형식 검증과 실제 참조 데이터 존재 확인은 구분합니다.
- [W042](workbook-questions.md#w042) · **4번**: 해당 트랜잭션 작업이 롤백된다
  - 해설: 기본 롤백은 RuntimeException·Error이며 checked exception 정책은 별도 지정할 수 있습니다.
- [W223](workbook-questions.md#w223) · **1번**: 자동 트랜잭션 프록시를 거치지 않는다
  - 해설: 컨테이너 관리 객체의 프록시 호출과 직접 생성한 객체의 호출을 구분합니다.
- [W224](workbook-questions.md#w224) · **3번**: 프록시를 우회해 내부 메서드의 새 트랜잭션 설정이 적용되지 않을 수 있다
  - 해설: 외부에서 프록시를 통한 호출 구조와 테스트로 경계를 확인합니다.
- [W382](workbook-questions.md#w382) · **2번**: DUPLICATE_ITEM_CODE
  - 해설: 품목코드 중복은 DUPLICATE_ITEM_CODE로 처리합니다. 존재 여부 검사와 DB UNIQUE 제약을 함께 사용해 경쟁 저장도 보호합니다.

## 9. 품목 검색 + 페이징 (TRD 3.5.4, 2.5.4)

- [W043](workbook-questions.md#w043) · **1번**: Controller → Service → Repository
  - 해설: 페이지·크기·정렬 조건을 필요한 검증을 거쳐 조회 계층으로 전달합니다.
- [W044](workbook-questions.md#w044) · **2번**: page.map(ItemResponse::from)
  - 해설: map은 content를 변환하면서 페이지 메타 정보를 유지합니다.
- [W045](workbook-questions.md#w045) · **1번**: 허용 필드와 기본 정렬을 명시하고 검증한다
  - 해설: 서버 고정과 제한적 사용자 선택 중 요구에 맞게 정하되 안정적인 기준을 둡니다.
- [W245](workbook-questions.md#w245) · **3번**: createdAt
  - 해설: 이 문제는 명시한 정렬 허용 정책을 전제로 합니다. 현재 Controller 기본 정렬은 createdAt 내림차순이며 허용 목록 검증은 별도 구현 과제입니다.
- [W277](workbook-questions.md#w277) · **4번**: 제외된다
  - 해설: 조건에 맞는 거래처만 조회하며 조회가 데이터를 변경하지 않습니다.
- [W278](workbook-questions.md#w278) · **1번**: 204 / INACTIVE
  - 해설: PartnerApiController는 noContent, Service는 비활성화 상태 변경을 수행합니다.
- [W279](workbook-questions.md#w279) · **3번**: 입력 오류로 거절한다
  - 해설: 이 문제는 허용 목록 검증을 구현하는 설계 과제를 전제로 하며 현재 자동 바인딩 자체가 이를 보장하지는 않습니다.
- [W280](workbook-questions.md#w280) · **2번**: 2
  - 해설: 마지막 페이지는 2보다 작을 수 있지만 요청한 페이지 크기를 넘지 않습니다.
- [W281](workbook-questions.md#w281) · **3번**: 변경된 값이 보인다
  - 해설: 트랜잭션 커밋 뒤 재조회로 저장 결과를 확인합니다.
- [W282](workbook-questions.md#w282) · **1번**: 포함되지 않는다
  - 해설: 현재 인증 사용자 ID를 작성자 조건으로 사용해야 합니다.
- [W328](workbook-questions.md#w328) · **3번**: 읽기 의도를 표현하고 일부 최적화를 돕지만 쓰기 차단 보안 경계는 아니다
  - 해설: 읽기 힌트·최적화 효과는 구현에 의존합니다. 수정 메서드는 쓰기 트랜잭션을 사용합니다.
- [W329](workbook-questions.md#w329) · **2번**: map은 기존 메타 정보를 보존하고 직접 구성은 총 개수·Pageable을 정확히 전달해야 한다
  - 해설: 수동 구성에서 전체 개수와 페이징 기준을 빠뜨리면 응답 메타 정보가 어긋날 수 있습니다.

## 10. 발주서 작성 Service (TRD 3.8.3, 3.10.1)

- [W046](workbook-questions.md#w046) · **4번**: 라인 실패 시 헤더까지 롤백해 부분 주문을 방지한다
  - 해설: 주문 구성 요소는 함께 성공하거나 함께 실패해야 합니다.
- [W047](workbook-questions.md#w047) · **3번**: 품목 존재·활성 상태·양수 수량을 확인한다
  - 해설: 존재·품목 상태·수량은 서버에서 모두 확인해야 합니다.
- [W048](workbook-questions.md#w048) · **2번**: Service에서 SUPPLIER 또는 BOTH인지 검사한다
  - 해설: 화면 조건은 우회 가능하므로 업무 계층에서 거래처 유형을 확인합니다.
- [W049](workbook-questions.md#w049) · **4번**: DB unique와 충돌 재시도 등 동시성 대책을 결합한다
  - 해설: 참조 구현은 최대 번호와 unique 충돌의 독립 트랜잭션 재시도를 사용합니다.
- [W050](workbook-questions.md#w050) · **2번**: 서버에서 수량×단가와 라인 합계를 재계산한다
  - 해설: 도메인·Service 배치보다 입력 변조에도 동일한 계산 규칙이 적용되는 것이 핵심입니다.
- [W267](workbook-questions.md#w267) · **3번**: 라인 존재·품목·수량·단가 등 입력과 업무 규칙을 검증한다
  - 해설: 사전 검증과 주문 전체 트랜잭션으로 부분 저장과 잘못된 주문을 방지합니다.
- [W383](workbook-questions.md#w383) · **1번**: EMPTY_ORDER_LINES
  - 해설: PurchaseOrderService는 라인이 비어 있는 요청을 EMPTY_ORDER_LINES로 거부합니다. 헤더만 저장되는 부분 주문을 만들지 않습니다.

## 11. 발주 승인/반려 Service (TRD 3.8.3, 3.10.2)

- [W051](workbook-questions.md#w051) · **1번**: 도메인 전이 메서드에 상태 가드를 둔다
  - 해설: Service 인가와 별도로 도메인 메서드가 상태 불변식을 보호합니다.
- [W052](workbook-questions.md#w052) · **3번**: 검증 오류로 거절하고 상태를 변경하지 않는다
  - 해설: 필수 사유는 DTO 및 도메인·Service 검증으로 보장합니다.
- [W053](workbook-questions.md#w053) · **3번**: 유효한 승인 상태 전이가 성공할 때
  - 해설: 상태와 행위자·시각이 같은 업무 처리 안에서 일관되게 변경되어야 합니다.
- [W054](workbook-questions.md#w054) · **1번**: ADMIN 또는 MANAGER
  - 해설: 현재 참조 구현은 ADMIN과 MANAGER를 허용하며 USER는 거절합니다.
- [W055](workbook-questions.md#w055) · **4번**: 둘 중 하나가 실패하면 전체를 되돌리기 위해서다
  - 해설: 입고 이벤트와 모든 재고 반영은 원자적인 업무 작업입니다.
- [W056](workbook-questions.md#w056) · **2번**: 버전 충돌을 감지해 중복 상태 변경을 차단한다
  - 해설: PurchaseOrder의 Version 낙관적 락이 경쟁 상태 변경을 감지합니다.
- [W182](workbook-questions.md#w182) · **3번**: APPROVED → RECEIVED
  - 해설: 입고는 승인된 발주만 가능하며 재고 증가와 같은 트랜잭션에서 처리합니다.
- [W183](workbook-questions.md#w183) · **4번**: 각 라인의 입고 수량만큼 증가한다
  - 해설: StockService가 입고 수량을 반영하며 중복 입고와 경쟁 요청을 방어합니다.
- [W184](workbook-questions.md#w184) · **1번**: 낙관적 락과 비관적 락
  - 해설: 버전 충돌 감지와 DB 잠금은 서로 다른 전략이며 경쟁 수준과 업무 요구로 선택합니다.
- [W225](workbook-questions.md#w225) · **4번**: 발주 상태와 앞선 재고 변경까지 함께 롤백된다
  - 해설: 입고 상태·재고 반영을 같은 트랜잭션으로 묶어 부분 반영을 막습니다.

## 12. 공지사항 Service (TRD 3.8.4)

- [W057](workbook-questions.md#w057) · **2번**: Service에서 ADMIN 여부를 검증한다
  - 해설: 여러 호출 경로에서 동일한 권한 규칙을 지켜야 합니다.
- [W058](workbook-questions.md#w058) · **1번**: important 내림차순
  - 해설: 중요도가 같으면 생성시각과 ID 등으로 순서를 안정화합니다.
- [W059](workbook-questions.md#w059) · **4번**: 미리보기·반복 GET도 증가시킬 수 있고 원자성을 보장해야 한다
  - 해설: 현재 구현은 상세 조회에서 원자적 UPDATE를 수행합니다. 별도 API는 다른 설계 선택입니다.
- [W255](workbook-questions.md#w255) · **2번**: ADMIN 권한을 확인하고 해당 공지 행을 실제 삭제한다
  - 해설: NoticeService.delete는 권한 확인 후 delete를 호출합니다. soft delete는 대안 설계입니다.
- [W395](workbook-questions.md#w395) · **4번**: 미존재를 NOTICE_NOT_FOUND로 처리하고 존재하는 공지를 삭제한다
  - 해설: 현재 공지 삭제는 대상을 조회하고 미존재를 NOTICE_NOT_FOUND로 거부합니다. 대안 exists 검사에서도 조회와 삭제 사이의 경쟁 상황을 별도로 고려합니다.

## 13. 수주서 작성 Service (TRD 3.8.5)

- [W060](workbook-questions.md#w060) · **3번**: 임시 작성과 최종 확정의 상태·검증 시점을 구분한다
  - 해설: 현재 프로젝트는 작성과 confirm 상태 전이를 구분합니다.
- [W061](workbook-questions.md#w061) · **1번**: DRAFT 상태와 권한·소유권 등 확정 정책을 검사한다
  - 해설: 현재 구현의 정책과 상태 가드를 함께 적용해야 합니다.
- [W062](workbook-questions.md#w062) · **2번**: Service에서 CUSTOMER 또는 BOTH를 허용한다
  - 해설: 수주는 판매 흐름이므로 고객 역할이 있는 거래처를 요구합니다.
- [W063](workbook-questions.md#w063) · **3번**: ITEM_DISCONTINUED로 거절한다
  - 해설: 단종은 신규 사용 제한이며 과거 라인은 보존합니다.
- [W186](workbook-questions.md#w186) · **1번**: 수주 작성자
  - 해설: SalesOrderService.confirm은 로그인과 작성자 ID를 확인합니다. ADMIN이라는 이유만으로 타인 수주 confirm을 허용하지 않습니다.

## 14. 수주 출고/완료/취소 Service (TRD 3.8.5, 3.10.4)

- [W064](workbook-questions.md#w064) · **4번**: CONFIRMED에서 출고하고 SHIPPED에서 완료한다
  - 해설: 종료 상태 추가 전이는 차단하고 취소 가능 상태를 따로 검증합니다.
- [W065](workbook-questions.md#w065) · **2번**: 현재 인증 사용자의 역할과 해당 수주의 접근 정책을 확인한다
  - 해설: 역할 검증과 소유권·담당 정책은 서버에서 인증 사용자 기준으로 적용합니다.
- [W066](workbook-questions.md#w066) · **1번**: 각각 출고·완료 상태 전이가 성공할 때
  - 해설: 업무 이벤트 발생 시각은 그 상태 변경과 같은 작업에서 기록합니다.
- [W067](workbook-questions.md#w067) · **3번**: 공백이 아닌 사유를 요구한다
  - 해설: 사유 검증과 취소 가능한 상태 검증을 모두 수행합니다.
- [W189](workbook-questions.md#w189) · **2번**: 작성자 또는 ADMIN/MANAGER가 허용 상태에서 사유를 입력한다
  - 해설: DRAFT·CONFIRMED·SHIPPED에서만 취소하며 출고 후 취소는 재고를 복원합니다.
- [W259](workbook-questions.md#w259) · **4번**: ADMIN 또는 MANAGER
  - 해설: 역할 검증 뒤 허용 상태·재고 등 업무 조건도 검사합니다.
- [W363](workbook-questions.md#w363) · **1번**: 서버에서 출고 상태 변경과 모든 라인 재고 차감을 같은 트랜잭션으로 묶는다
  - 해설: 현재 Service는 출고 상태·품목 잠금·재고 차감을 조정합니다. 한 라인 실패 시 부분 출고가 남지 않게 합니다.

## 15. ErrorCode 와 응답 모델 (TRD 3.9)

- [W068](workbook-questions.md#w068) · **4번**: status는 HTTP 결과 분류, code는 구체적인 업무 오류를 나타낸다
  - 해설: 동일한 HTTP 상태 안에도 중복·상태 오류 등 다른 업무 코드가 있습니다.
- [W069](workbook-questions.md#w069) · **1번**: status·code·message·timestamp
  - 해설: 공통 응답은 결과·오류 식별자·메시지·발생 시각으로 구성합니다.
- [W070](workbook-questions.md#w070) · **2번**: 코드·상태·기본 메시지의 일관성을 유지한다
  - 해설: Service와 예외 처리기가 같은 오류 계약을 공유합니다.
- [W330](workbook-questions.md#w330) · **4번**: 기본 트랜잭션 롤백 대상이 되며 호출부의 checked 예외 선언을 요구하지 않는다
  - 해설: 업무 오류 계약을 예외 처리기로 전달하며 트랜잭션 기본 롤백 정책과 맞춥니다.
- [W331](workbook-questions.md#w331) · **1번**: 시간대·오프셋이 없어 실제 시점을 해석하는 기준을 정해야 한다
  - 해설: 이벤트의 절대 시점을 전달할 때 Instant·오프셋 또는 명시한 시간대 정책을 고려합니다.
- [W384](workbook-questions.md#w384) · **3번**: LocalDateTime
  - 해설: ErrorResponse는 LocalDateTime.now()로 timestamp를 생성합니다. 시간대가 필요한 외부 시스템 연동에서는 별도의 시점 표현 정책을 정합니다.

## 16. GlobalExceptionHandler (TRD 3.9)

- [W071](workbook-questions.md#w071) · **4번**: 여러 API 컨트롤러의 예외를 공통 응답으로 처리한다
  - 해설: ExceptionHandler와 함께 API 예외 처리 책임을 모읍니다.
- [W072](workbook-questions.md#w072) · **3번**: 사용자 오류와 내부 오류를 서로 맞는 계약으로 응답한다
  - 해설: 알려진 ErrorCode와 검증 메시지를 처리하고 내부 오류 정보는 노출을 제한합니다.
- [W073](workbook-questions.md#w073) · **4번**: BindingResult의 FieldError를 순회해 필드와 메시지를 추출한다
  - 해설: 검증 예외의 바인딩 결과에 필드 이름과 기본 메시지가 들어 있습니다.
- [W275](workbook-questions.md#w275) · **2번**: INVALID_INPUT
  - 해설: 현재 API 예외 처리기는 입력 검증 실패를 400과 INVALID_INPUT으로 전달합니다.
- [W332](workbook-questions.md#w332) · **3번**: 전자는 ResponseBody 의미를 포함하며 후자는 View 등 다른 응답 방식도 사용할 수 있다
  - 해설: 응답 표현과 적용 대상에 맞춰 API·Web 예외 처리기를 구분합니다.
- [W333](workbook-questions.md#w333) · **3번**: 내부 구조·SQL·경로·비밀정보가 노출될 수 있다
  - 해설: 사용자에게 안정적인 오류 계약을 응답하고 서버에서 필요한 진단 정보를 관리합니다.
- [W334](workbook-questions.md#w334) · **1번**: basePackages·annotations 등 적용 범위를 명시하고 우선순위를 검토한다
  - 해설: 예외 처리 대상·응답 형식·Advice 간 적용 순서를 일관되게 구성합니다.
- [W385](workbook-questions.md#w385) · **3번**: ResponseEntity<ErrorResponse>
  - 해설: ResponseEntity.status(...).body(...)로 상태와 본문을 지정할 수 있습니다. 도메인 예외의 HTTP 변환은 API 처리기의 책임입니다.
- [W386](workbook-questions.md#w386) · **2번**: ERROR
  - 해설: 예상 밖 예외는 log.error로 요청 경로와 예외 증거를 남깁니다. 클라이언트 응답에는 내부 상세를 노출하지 않습니다.

## 17. DTO 검증 (TRD 3.7, 비기능 검증)

- [W074](workbook-questions.md#w074) · **1번**: 공백 금지·양수·길이/개수·정규식 검증
  - 해설: 각 어노테이션은 적용 타입과 검사하는 입력 조건이 다릅니다.
- [W075](workbook-questions.md#w075) · **2번**: 컨트롤러 인자 바인딩·해석 과정에서 수행된다
  - 해설: 다른 호출 경로에는 Service 검증 등 추가 방어가 필요할 수 있습니다.
- [W076](workbook-questions.md#w076) · **3번**: 입력 허용 필드와 출력 공개 필드의 계약이 다르기 때문이다
  - 해설: 각 경계에 필요한 데이터와 검증 정책을 독립적으로 관리합니다.
- [W327](workbook-questions.md#w327) · **1번**: 해당 입력 경계의 Bean Validation이 실행되지 않아 잘못된 값이 다음 계층으로 갈 수 있다
  - 해설: Valid의 호출 지점과 별도로 Service·도메인 검증이 있는지 확인해야 합니다.
- [W335](workbook-questions.md#w335) · **4번**: null 금지·null과 빈 값 금지·문자열 공백까지 금지
  - 해설: 문자열·컬렉션 등 적용 타입과 공백 처리 조건을 구분합니다.
- [W387](workbook-questions.md#w387) · **4번**: @DecimalMin("0.00")
  - 해설: @DecimalMin의 기본 inclusive=true는 0.00도 허용합니다. 필수 입력이면 @NotNull과 함께 사용하고 자릿수도 별도로 제한합니다.

## 18. ItemController (TRD 3.7.4)

- [W077](workbook-questions.md#w077) · **4번**: 조회 GET·등록 POST·전체 수정 PUT
  - 해설: 리소스 경로와 행위에 맞는 HTTP 계약을 정합니다. 부분 수정은 PATCH를 고려합니다.
- [W078](workbook-questions.md#w078) · **1번**: 경로 값·요청 본문·쿼리/폼 파라미터
  - 해설: 서로 다른 요청 데이터 위치를 컨트롤러 파라미터에 바인딩합니다.
- [W079](workbook-questions.md#w079) · **3번**: 인증된 사용자의 ADMIN 역할을 서버 가드에서 검사한다
  - 해설: 경로 이름과 UI만으로는 권한을 보장할 수 없습니다.
- [W250](workbook-questions.md#w250) · **4번**: 조회할 품목 ID
  - 해설: 인증·역할 확인과 별도로 조회 대상 식별자를 해석합니다.
- [W336](workbook-questions.md#w336) · **2번**: 필드 누락과 명시적 null·삭제 의도를 구분하고 수정 의미를 정한다
  - 해설: 전체 교체와 부분 변경 계약을 구분하고 필요한 값의 검증을 명시합니다.
- [W337](workbook-questions.md#w337) · **2번**: PATCH 부분 변경
  - 해설: 이 문제는 선택 필드만 변경하는 정책을 전제로 하며 DTO의 nullable 여부만으로 PUT·PATCH가 결정되지는 않습니다.

## 19. PurchaseOrderController (TRD 3.7.5)

- [W080](workbook-questions.md#w080) · **2번**: 관리자 경로에서 실제 역할 인가를 수행한다
  - 해설: 경로 분리는 계약을 명확히 하지만 실행 권한은 서버 검사로 보장합니다.
- [W081](workbook-questions.md#w081) · **3번**: 업무 명령을 명확히 표현하지만 상태 전이·중복 요청 정책이 필요하다
  - 해설: 행위 중심 경로의 편의와 상태·멱등 정책을 함께 설계합니다.
- [W082](workbook-questions.md#w082) · **1번**: CurrentUser ArgumentResolver가 LoginUser를 주입한다
  - 해설: 현재 인증 구조는 세션과 LoginUser·CurrentUser이며 다른 인증 방식의 인자와 구분해야 합니다.
- [W388](workbook-questions.md#w388) · **1번**: @RequestBody
  - 해설: 반려 사유 DTO는 JSON 본문에서 읽고 @Valid 등으로 입력을 검증합니다. 발주 ID는 별도의 경로 변수입니다.

## 20. 보안 흐름 (TRD 3.11)

- [W083](workbook-questions.md#w083) · **2번**: 계정 조회·비밀번호 검증·세션 ID 변경·로그인 정보 저장
  - 해설: 서버가 인증한 정보만 세션에 보관하고 세션 고정 공격을 방지합니다.
- [W084](workbook-questions.md#w084) · **4번**: Interceptor·Service는 인증/인가, Security는 CSRF·헤더를 맡는다
  - 해설: 현재 참조 구현과 Spring Security가 전체 인증을 맡는 대안 설계를 구분합니다.
- [W085](workbook-questions.md#w085) · **2번**: 기존 세션 인터셉터와 사용자 해석의 중복 책임을 재설계한다
  - 해설: 새 인증 원천과 기존 계약을 맞추면서 필요한 Service 업무 인가는 유지합니다.
- [W086](workbook-questions.md#w086) · **3번**: 매 요청 토큰을 검증하고 폐기·권한 변경 정책을 별도로 설계한다
  - 해설: 무상태 인증이라도 키 관리·만료·재사용·폐기 문제를 해결해야 합니다.
- [W173](workbook-questions.md#w173) · **3번**: 인증된 사용자가 기능·데이터에 접근할 권한
  - 해설: 로그인 성공만으로 모든 데이터에 접근할 수 있는 것은 아닙니다.
- [W175](workbook-questions.md#w175) · **4번**: 폐기 목록·토큰 버전 등 서버 확인 정책을 추가한다
  - 해설: 클라이언트 삭제는 탈취된 복사본을 폐기하지 못합니다. 짧은 TTL은 노출 시간을 줄일 뿐 즉시 폐기는 별도 대책입니다.
- [W190](workbook-questions.md#w190) · **4번**: JS 쿠키 접근 제한·HTTPS 전송 제한·교차 사이트 전송 제어
  - 해설: 각 쿠키 속성은 다른 공격 경로를 줄입니다. HTTPS 설정과 SameSite 정책은 사용 흐름에 맞춰 정합니다.
- [W191](workbook-questions.md#w191) · **1번**: 세션 공유 저장소나 라우팅 전략과 장애·운영 비용
  - 해설: 서버별 메모리 세션은 요청 서버가 달라지면 인증 상태를 찾지 못할 수 있습니다.
- [W192](workbook-questions.md#w192) · **3번**: 보통 각각 별도 세션이 만들어진다
  - 해설: 특별한 동시 세션 제한 정책이 없다면 브라우저별 쿠키·서버 세션이 독립적입니다.
- [W193](workbook-questions.md#w193) · **3번**: 여러 서버가 세션을 공유하지만 저장소 운영·네트워크 비용이 생긴다
  - 해설: 중앙 세션 저장소는 확장을 돕지만 가용성·TTL·네트워크·장애 대응을 요구합니다.
- [W194](workbook-questions.md#w194) · **1번**: 같은 권한 문자열을 검사한다
  - 해설: 기본 ROLE_ 접두사를 전제로 합니다. 커스텀 접두사 정책이면 기준을 다시 확인해야 합니다.
- [W195](workbook-questions.md#w195) · **2번**: 브라우저가 인증 정보를 자동 첨부하는 방식과 공격 경로를 확인한다
  - 해설: 쿠키·HTTP 인증 등의 자동 전송 여부와 실제 인증 방식을 기준으로 판단합니다.
- [W196](workbook-questions.md#w196) · **4번**: EnableMethodSecurity
  - 해설: 어노테이션을 붙였다는 사실만으로 메서드 보안이 활성화되지는 않습니다.
- [W197](workbook-questions.md#w197) · **2번**: sub·exp와 필요한 역할 정보
  - 해설: payload는 인코딩된 것이며 비밀 저장소가 아닙니다. 서명·발급자·대상·만료도 검증해야 합니다.
- [W198](workbook-questions.md#w198) · **1번**: 유출 토큰이 사용될 수 있는 시간을 줄인다
  - 해설: 짧은 TTL은 노출 기간을 줄이지만 즉시 폐기와 갱신 정책은 별도로 설계해야 합니다.
- [W199](workbook-questions.md#w199) · **4번**: 장기 토큰 탈취·재사용과 안전한 회전·폐기
  - 해설: 갱신 토큰은 수명이 길 수 있어 저장 보호·회전·재사용 감지·폐기 정책이 중요합니다.
- [W200](workbook-questions.md#w200) · **3번**: 저장소 노출을 줄이고 환경별 관리·회전을 가능하게 한다
  - 해설: 환경 변수 또는 비밀 저장소를 사용하되 로그·권한·배포 노출도 관리해야 합니다.
- [W201](workbook-questions.md#w201) · **1번**: 매 요청 권한 조회나 토큰 버전·폐기 등 별도 정책이 필요하다
  - 해설: 서명된 기존 토큰은 재발급·만료 또는 서버 확인 정책 없이는 그대로 유효할 수 있습니다.
- [W202](workbook-questions.md#w202) · **3번**: 명시 헤더로 전송하고 기기 저장·유출 위험을 따로 관리한다
  - 해설: 전송 편의와 안전한 저장·수명·갱신 정책을 구분해 판단합니다.
- [W272](workbook-questions.md#w272) · **1번**: 공격자가 아는 세션 ID를 피해자가 인증 후에도 쓰게 한다
  - 해설: 로그인 성공 시 세션 ID를 변경해 인증 전 ID의 재사용 위험을 줄입니다.

## 21. 통합 테스트 흐름 (TRD 3.12)

- [W087](workbook-questions.md#w087) · **4번**: 로그인 → 작성 → 요청 → 승인 → 입고
  - 해설: 이전 응답의 ID와 각 역할의 세션·CSRF 조건을 이어 사용합니다.
- [W088](workbook-questions.md#w088) · **1번**: 응답 JSON을 파싱해 ID 필드를 추출한다
  - 해설: 실제 응답 계약을 따라 ID를 추출해야 테스트가 생성 데이터와 연결됩니다.
- [W089](workbook-questions.md#w089) · **4번**: 인증·CSRF 조건을 갖춘 요청에서도 403을 확인한다
  - 해설: 권한 실패와 CSRF 실패를 구분해 인가 경계를 검증합니다.
- [W232](workbook-questions.md#w232) · **3번**: 실제 MySQL에서 mysqlSchemaTest 등 프로필별 검증을 수행한다
  - 해설: Flyway 적용과 JPA validate를 실제 MySQL에서 별도로 확인합니다.
- [W283](workbook-questions.md#w283) · **4번**: 400
  - 해설: 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.
- [W284](workbook-questions.md#w284) · **2번**: 401
  - 해설: 보호 API에 인증 세션이 없으면 AUTHENTICATION_REQUIRED로 401을 응답합니다.
- [W285](workbook-questions.md#w285) · **4번**: 401
  - 해설: 보호 API에 인증 세션이 없으면 AUTHENTICATION_REQUIRED로 401을 응답합니다.
- [W286](workbook-questions.md#w286) · **2번**: 400
  - 해설: 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.
- [W287](workbook-questions.md#w287) · **1번**: 403
  - 해설: 로그인된 사용자에게 해당 역할·소유권이 없으면 ACCESS_DENIED로 403을 응답합니다.
- [W288](workbook-questions.md#w288) · **3번**: 200
  - 해설: 유효한 업무 조건을 갖춘 현재 출고 API는 200 OK를 반환합니다.
- [W289](workbook-questions.md#w289) · **3번**: 400
  - 해설: 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.
- [W290](workbook-questions.md#w290) · **4번**: 403
  - 해설: 로그인된 사용자에게 해당 역할·소유권이 없으면 ACCESS_DENIED로 403을 응답합니다.
- [W291](workbook-questions.md#w291) · **2번**: 404
  - 해설: 현재 ErrorCode의 대상 미존재 오류는 404입니다. 접근 권한 등 선행 조건은 충족했다고 가정합니다.
- [W292](workbook-questions.md#w292) · **1번**: 400
  - 해설: 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.
- [W293](workbook-questions.md#w293) · **1번**: 404
  - 해설: 현재 ErrorCode의 대상 미존재 오류는 404입니다. 접근 권한 등 선행 조건은 충족했다고 가정합니다.
- [W294](workbook-questions.md#w294) · **3번**: 400
  - 해설: 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.
- [W295](workbook-questions.md#w295) · **2번**: 400
  - 해설: 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.
- [W296](workbook-questions.md#w296) · **4번**: 400
  - 해설: 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.
- [W297](workbook-questions.md#w297) · **2번**: 404
  - 해설: 현재 ErrorCode의 대상 미존재 오류는 404입니다. 접근 권한 등 선행 조건은 충족했다고 가정합니다.
- [W298](workbook-questions.md#w298) · **4번**: 401
  - 해설: 보호 API에 인증 세션이 없으면 AUTHENTICATION_REQUIRED로 401을 응답합니다.
- [W299](workbook-questions.md#w299) · **1번**: 404
  - 해설: 현재 ErrorCode의 대상 미존재 오류는 404입니다. 접근 권한 등 선행 조건은 충족했다고 가정합니다.
- [W300](workbook-questions.md#w300) · **3번**: 404
  - 해설: 현재 ErrorCode의 대상 미존재 오류는 404입니다. 접근 권한 등 선행 조건은 충족했다고 가정합니다.
- [W301](workbook-questions.md#w301) · **3번**: 400
  - 해설: 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.
- [W302](workbook-questions.md#w302) · **4번**: 400
  - 해설: 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.
- [W303](workbook-questions.md#w303) · **1번**: 400
  - 해설: 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.
- [W304](workbook-questions.md#w304) · **2번**: 400
  - 해설: 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.
- [W305](workbook-questions.md#w305) · **2번**: 400
  - 해설: 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.
- [W306](workbook-questions.md#w306) · **3번**: 403
  - 해설: 로그인된 사용자에게 해당 역할·소유권이 없으면 ACCESS_DENIED로 403을 응답합니다.
- [W307](workbook-questions.md#w307) · **1번**: 400
  - 해설: 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.
- [W308](workbook-questions.md#w308) · **4번**: 403
  - 해설: 로그인된 사용자에게 해당 역할·소유권이 없으면 ACCESS_DENIED로 403을 응답합니다.
- [W309](workbook-questions.md#w309) · **4번**: 400
  - 해설: 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.
- [W310](workbook-questions.md#w310) · **3번**: 400
  - 해설: 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.
- [W311](workbook-questions.md#w311) · **2번**: 400
  - 해설: 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.
- [W312](workbook-questions.md#w312) · **1번**: 403
  - 해설: 로그인된 사용자에게 해당 역할·소유권이 없으면 ACCESS_DENIED로 403을 응답합니다.
- [W313](workbook-questions.md#w313) · **1번**: 403
  - 해설: 로그인된 사용자에게 해당 역할·소유권이 없으면 ACCESS_DENIED로 403을 응답합니다.
- [W314](workbook-questions.md#w314) · **4번**: 403
  - 해설: 로그인된 사용자에게 해당 역할·소유권이 없으면 ACCESS_DENIED로 403을 응답합니다.
- [W315](workbook-questions.md#w315) · **2번**: 400
  - 해설: 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.
- [W316](workbook-questions.md#w316) · **3번**: 400
  - 해설: 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.
- [W317](workbook-questions.md#w317) · **4번**: 400
  - 해설: 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.
- [W318](workbook-questions.md#w318) · **3번**: 403
  - 해설: 로그인된 사용자에게 해당 역할·소유권이 없으면 ACCESS_DENIED로 403을 응답합니다.
- [W319](workbook-questions.md#w319) · **1번**: 400
  - 해설: 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.
- [W320](workbook-questions.md#w320) · **2번**: 400
  - 해설: 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.
- [W321](workbook-questions.md#w321) · **3번**: 400
  - 해설: 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.
- [W322](workbook-questions.md#w322) · **2번**: 400
  - 해설: 현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.
- [W338](workbook-questions.md#w338) · **4번**: 테스트 트랜잭션은 기본 롤백되지만 다른 스레드·독립 트랜잭션은 별도 범위다
  - 해설: 테스트 격리 범위와 실제 서버·비동기·REQUIRES_NEW 트랜잭션을 구분해야 합니다.
- [W339](workbook-questions.md#w339) · **3번**: MockMvc는 MVC 테스트 환경, WebTestClient는 HTTP 클라이언트 방식이며 지원 서버·바인딩 방식이 다르다
  - 해설: 현재 프로젝트는 MockMvc 중심이며 테스트 목적과 서버 기동 방식에 맞춰 도구를 선택합니다.
- [W340](workbook-questions.md#w340) · **1번**: 경로 기반 필드 추출과 구조·타입 기반 역직렬화의 차이다
  - 해설: 간단한 필드 추출과 DTO 전체 계약 검증 중 목적에 맞는 방식을 사용합니다.
- [W389](workbook-questions.md#w389) · **3번**: 201 Created
  - 해설: 새 발주 생성은 201을 반환하고 생성 ID를 제공해 이후 조회·상태 변경 테스트가 사용할 수 있게 합니다.

## 22. 문서화 (TRD 3.15 ~ 3.17)

- [W090](workbook-questions.md#w090) · **3번**: 목적·기술·기능·구조·DB·API·실행 등 독자의 시작 정보를 담는다
  - 해설: 개요와 실제로 재현 가능한 실행 절차·문서 연결이 필요합니다.
- [W091](workbook-questions.md#w091) · **1번**: 갱신·diff·공유·시각 전달 요구를 함께 고려한다
  - 해설: 텍스트와 시각 도구에는 유지 비용과 표현력의 차이가 있습니다.
- [W092](workbook-questions.md#w092) · **2번**: 문제 → 원인 → 해결 → 배운 점
  - 해설: 재현 조건·원인·해결 및 검증을 남겨 같은 문제를 다시 분석할 수 있게 합니다.
- [W093](workbook-questions.md#w093) · **2번**: 수동 문서는 설명·diff에 유리하고 자동 문서도 의미·정책 검토가 필요하다
  - 해설: 어떤 방식이든 실제 입력·응답·권한·오류 계약과 동기화해야 합니다.
- [W236](workbook-questions.md#w236) · **3번**: ./gradlew bootRun / http://localhost:8080/login
  - 해설: 기본 서버 포트는 8080이며 문제집의 정적 서버 8000과 구분합니다.
- [W237](workbook-questions.md#w237) · **3번**: 엔티티·마이그레이션의 키·관계·nullable·제약을 대조한다
  - 해설: 업무 관계와 실제 JPA 매핑·DB 외래키는 구분해 정확히 표시합니다.
- [W238](workbook-questions.md#w238) · **4번**: 세션 쿠키·로그인·역할과 현재 CSRF 정책을 명시한다
  - 해설: 현재 참조 구현은 세션 기반이므로 인증 방식과 예외 정책을 실제 코드와 맞춥니다.
- [W239](workbook-questions.md#w239) · **2번**: 텍스트는 diff·갱신, 시각 자료는 관계 전달에 유리하지만 동기화가 필요하다
  - 해설: 도구에 대한 취향보다 변경 추적과 독자의 이해 요구를 기준으로 판단합니다.
- [W265](workbook-questions.md#w265) · **1번**: 실제 구현과 재현 가능한 기능 흐름을 보여 준다
  - 해설: 시연 자료는 현재 기능과 실행 방법을 독자가 확인하는 증거입니다.
- [W274](workbook-questions.md#w274) · **3번**: 실제 코드와 동기화한 API 명세
  - 해설: 입력·응답·권한·오류·수정 의미를 독자가 재현할 수 있게 API 계약에 적습니다.

## 23. 인증 — 로그인 / 로그아웃 / 내 정보 / 비밀번호 변경 (PRD 2.5.1, TRD 3.7.1, 3.11)

- [W094](workbook-questions.md#w094) · **4번**: passwordEncoder.matches(raw, encoded)
  - 해설: BCrypt의 salt 때문에 재인코딩 결과 문자열의 단순 비교를 사용하지 않습니다.
- [W095](workbook-questions.md#w095) · **3번**: 서버가 검증한 사용자 ID·이름·역할의 LoginUser
  - 해설: 인증된 사용자 정보만 최소한으로 보관합니다.
- [W096](workbook-questions.md#w096) · **1번**: 로그인 인증이 성공할 때
  - 해설: 인증 전 알려진 세션 ID가 인증 후에도 유지되는 것을 방지합니다.
- [W097](workbook-questions.md#w097) · **3번**: 이메일 존재 여부를 이용한 계정 열거를 줄이기 위해서다
  - 해설: 사용자에게 계정 존재 여부를 직접 구분해 알려 주지 않습니다.
- [W098](workbook-questions.md#w098) · **2번**: 현재 비밀번호 검증 후 새 비밀번호를 해시해 저장한다
  - 해설: 형식 검증과 현재 비밀번호 일치 확인·새 비밀번호 보호를 함께 수행합니다.
- [W172](workbook-questions.md#w172) · **2번**: 사용자가 주장한 신원과 인증 증거의 일치
  - 해설: 인증은 신원을 확인하고 인가는 기능·데이터 접근 권한을 판단합니다.
- [W174](workbook-questions.md#w174) · **1번**: 서버의 해당 세션 인증 상태를 폐기한다
  - 해설: 현재 서버 세션을 invalidate해 이후 보호 요청의 인증 상태를 제거합니다.
- [W176](workbook-questions.md#w176) · **2번**: 서버가 인증한 현재 사용자 ID
  - 해설: 인증 주체의 ID를 사용해 다른 사용자 정보 조회 우회를 막습니다.
- [W178](workbook-questions.md#w178) · **3번**: 탈취·공유된 로그인 세션의 피해를 줄이기 위해서다
  - 해설: 중요 작업에 추가 인증 증거를 요구하되 새 비밀번호 해시 저장도 필요합니다.
- [W260](workbook-questions.md#w260) · **1번**: LOGIN_USER 키의 LoginUser 객체
  - 해설: SessionConst.LOGIN_USER를 사용해 인증된 사용자 ID·이름·이메일·역할을 보관합니다.
- [W276](workbook-questions.md#w276) · **1번**: false
  - 해설: 기존과 다른 새 비밀번호를 저장했다는 전제입니다. 새 해시에 이전 비밀번호가 일치하면 안 됩니다.
- [W341](workbook-questions.md#w341) · **4번**: 인증 전 사용자가 자격 증명을 제출할 수 있어야 하기 때문이다
  - 해설: permitAll은 로그인 진입점 접근 허용이며 자격 증명 검증과 업무 API 인가를 생략한다는 뜻이 아닙니다.
- [W342](workbook-questions.md#w342) · **2번**: ID를 숨기는 것보다 대상별 서버 소유권·권한 검사가 핵심이다
  - 해설: 식별자의 예측 가능성과 별도로 모든 데이터 접근에서 인증 주체의 권한을 검증합니다.
- [W344](workbook-questions.md#w344) · **3번**: 204는 본문 없이, 200은 정의한 본문 계약으로 일관되게 사용한다
  - 해설: 현재 로그아웃·거래처 비활성화는 204이며 다른 계약을 택하면 클라이언트와 문서를 맞춥니다.
- [W345](workbook-questions.md#w345) · **4번**: 탈취 가능성과 요구에 맞춰 기존 인증 폐기·재인증 정책을 명시한다
  - 해설: 현재 구현에 없는 일괄 폐기를 이미 제공한다고 가정하지 않고 별도 정책과 구현을 검증합니다.
- [W346](workbook-questions.md#w346) · **1번**: 가입·저장·조회에서 같은 규칙을 사용하고 업무상의 대소문자 정책을 명시한다
  - 해설: 현재 AuthService는 입력 이메일로 조회합니다. 정규화 도입은 모든 경로와 기존 데이터를 함께 맞추는 과제입니다.
- [W347](workbook-questions.md#w347) · **2번**: 변경이 실제 새 인증 값으로 이어지게 하지만 재사용 정책만으로 탈취가 해결되지는 않는다
  - 해설: 새 비밀번호 길이·강도·유출 여부·인증 폐기 정책과 구분해 검토합니다.

## 24. 거래처 Service / 카테고리 Service — CRUD + 정책 (PRD 2.5.2, 2.5.3, TRD 3.7.2, 3.7.3)

- [W099](workbook-questions.md#w099) · **1번**: 사전 안내와 동시 등록의 최종 무결성을 각각 보장한다
  - 해설: 사전 조회 이후 다른 트랜잭션이 저장할 수 있으므로 DB 제약이 필요합니다.
- [W100](workbook-questions.md#w100) · **4번**: INACTIVE로 바꿔 이력을 보존한다
  - 해설: 참조 구현의 거래처 삭제는 비활성화 정책입니다.
- [W101](workbook-questions.md#w101) · **1번**: 과거 이력과 신규 주문 허용 정책을 명시해 일관되게 검증한다
  - 해설: 과거 참조를 보존하고 변경 뒤 신규 사용 정책을 구분해야 합니다.
- [W102](workbook-questions.md#w102) · **4번**: 조건별 Specification을 구성해 필요한 조건을 결합한다
  - 해설: Repository 검색 추상화에 선택 조건을 모아 조합 폭증을 줄입니다.
- [W103](workbook-questions.md#w103) · **3번**: Service existsByName과 DB unique
  - 해설: 사용자 오류 안내와 경쟁 요청의 무결성을 서로 보완합니다.
- [W104](workbook-questions.md#w104) · **2번**: CATEGORY_HAS_ITEMS로 거부한다
  - 해설: 현재 구현은 소속 품목이 있으면 삭제를 차단합니다.
- [W105](workbook-questions.md#w105) · **4번**: 필요한 관계를 fetch join·EntityGraph 또는 별도 일괄 조회로 가져온다
  - 해설: 조회 계획을 의도적으로 정해야 하며 컬렉션 fetch와 페이징의 제약도 고려합니다.
- [W106](workbook-questions.md#w106) · **2번**: 응답 크기·메모리·지연이 늘어 페이징을 고려해야 한다
  - 해설: 데이터 규모와 화면 요구에 맞춰 전체 조회 또는 페이지 계약을 선택합니다.
- [W246](workbook-questions.md#w246) · **1번**: 업무 식별 값의 중복을 DB에서 차단한다
  - 해설: 각 식별자의 의미는 다르지만 최종 중복 방어라는 DB 제약의 역할은 같습니다.
- [W248](workbook-questions.md#w248) · **2번**: 거래처를 조회한 Service 업무 검증
  - 해설: 화면 우회를 막기 위해 서버 업무 계층에서 거래처 상태를 검사합니다.
- [W348](workbook-questions.md#w348) · **3번**: 거절은 현재 참조 보호, 이관은 대상·일괄 변경·트랜잭션 정책이 필요하다
  - 해설: 현재는 삭제 거절이며 이관은 별도 업무 정책·구현·테스트가 필요한 대안입니다.
- [W349](workbook-questions.md#w349) · **2번**: 등록·수정·삭제 성공 후 해당 조회 캐시를 갱신하거나 제거한다
  - 해설: 트랜잭션 성공 시점과 캐시 갱신·만료·일관성 요구를 함께 설계합니다.
- [W350](workbook-questions.md#w350) · **4번**: Service가 관련 이력과 변경 정책을 검사한다
  - 해설: 이 문제는 명시한 변경 금지 정책을 전제로 하며 현재 참조 구현의 정책과 구분합니다.
- [W390](workbook-questions.md#w390) · **4번**: DUPLICATE_CATEGORY_NAME
  - 해설: 이름 중복은 DUPLICATE_CATEGORY_NAME입니다. 미존재와 참조 중인 카테고리의 삭제 금지는 서로 다른 오류입니다.
- [W391](workbook-questions.md#w391) · **2번**: 카테고리 ID로 별도 품목 페이지를 조회하고 DTO로 변환한다
  - 해설: 해당 starter는 findByCategory_Id(categoryId, pageable) 결과를 DTO로 매핑합니다. 현재 상세 API의 계약과 소속 품목을 포함하는 대안 연습을 구분합니다.

## 25. 거래처 Controller / 카테고리 Controller + DTO (TRD 3.7.2, 3.7.3)

- [W107](workbook-questions.md#w107) · **1번**: 메서드 보안 활성화가 필요하며 Service 업무 인가도 별도로 검토한다
  - 해설: 현재 구현은 Service 가드 중심이며 대안의 PreAuthorize에는 EnableMethodSecurity 등이 필요합니다.
- [W108](workbook-questions.md#w108) · **3번**: PUT / PATCH
  - 해설: PUT은 전체 표현 교체, PATCH는 부분 변경 계약에 사용합니다.
- [W109](workbook-questions.md#w109) · **4번**: 화면 요구와 조회 비용·응답 크기를 함께 고려한다
  - 해설: 통계를 같은 응답에 넣거나 별도 API로 나누는 선택을 명시합니다.
- [W110](workbook-questions.md#w110) · **3번**: List로 시작하되 규모가 커지면 Page 계약을 고려한다
  - 해설: 목록 규모와 사용자 경험에 맞춰 반환 계약을 정합니다.
- [W111](workbook-questions.md#w111) · **1번**: 응답 크기와 N+1, 필요하면 별도 품목 페이징이다
  - 해설: 응답 구조와 조회 계획은 함께 검토해야 합니다.
- [W112](workbook-questions.md#w112) · **2번**: 400 Bad Request
  - 해설: 현재 ErrorCode는 업무 삭제 금지 규칙을 400으로 응답합니다.
- [W113](workbook-questions.md#w113) · **1번**: 변경은 ADMIN, 조회는 로그인 사용자에게 허용한다
  - 해설: 조회와 변경은 서로 다른 권한 범위를 갖습니다.
- [W251](workbook-questions.md#w251) · **1번**: 조회할 거래처 ID
  - 해설: 조회 대상 ID를 받아 존재 여부와 접근 조건을 확인합니다.

## 26. 공지 Controller + DTO (TRD 3.7.6)

- [W114](workbook-questions.md#w114) · **2번**: 재요청·봇·캐시가 조회수 의미에 영향을 줄 수 있다
  - 해설: 별도 증가 API도 가능한 대안이며 어느 방식이든 집계 의미와 원자성을 정해야 합니다.
- [W115](workbook-questions.md#w115) · **4번**: 긴 본문 때문에 페이로드와 전송·렌더링 비용이 커질 수 있다
  - 해설: 목록 요약과 상세 본문 응답을 분리할 수 있습니다.
- [W116](workbook-questions.md#w116) · **3번**: 요청이 생략한 페이지 크기·정렬 기본값을 지정한다
  - 해설: 기본값 설정과 최대 크기·허용 정렬 검증은 별도로 설계합니다.

## 27. 수주 Controller + DTO — my / pending / confirm / ship / complete (TRD 3.7.7)

- [W117](workbook-questions.md#w117) · **4번**: my는 본인 목록, 처리 대기는 ADMIN/MANAGER용이다
  - 해설: 목록 대상과 실행 권한을 Service에서 함께 확인합니다.
- [W118](workbook-questions.md#w118) · **2번**: 업무 명령이 명확하지만 각 상태 전이 계약을 검사해야 한다
  - 해설: 명령형 API에는 권한·사유·상태·중복 호출 정책이 필요합니다.
- [W119](workbook-questions.md#w119) · **3번**: 두 역할 중 하나라도 있으면 허용한다
  - 해설: 일반적인 ROLE_ 접두사 규칙을 적용하며 현재 Service 가드 구조와 구분합니다.

## 28. 품목 상세 / 수정 / 단종 처리 (PRD 2.5.4)

- [W120](workbook-questions.md#w120) · **1번**: 트랜잭션 안에서 필요한 관계를 로드해 Service에서 변환한다
  - 해설: 영속성 컨텍스트가 종료된 뒤의 지연 로딩을 피하도록 조회와 변환을 설계합니다.
- [W121](workbook-questions.md#w121) · **2번**: 새 카테고리 존재를 확인하고 도메인 변경 메서드를 호출한다
  - 해설: 참조 무결성 확인과 의미 있는 변경 동작을 분리해 수행합니다.
- [W122](workbook-questions.md#w122) · **4번**: 업무 의미와 불변식 검증을 변경 지점에 모은다
  - 해설: changeCategory·discontinue 같은 메서드는 허용된 상태 변경을 표현합니다.
- [W123](workbook-questions.md#w123) · **1번**: soft delete는 상태로 제한하면서 이력을 보존하고 조회 조건 관리가 필요하다
  - 해설: 보존 정책과 데이터 정리·조회 복잡도의 차이를 고려합니다.
- [W351](workbook-questions.md#w351) · **3번**: 재활성화 허용 조건·권한·이력 의미를 명시하고 검증한다
  - 해설: 재활성화는 정책 선택입니다. 현재 단종 기능과 추가 기능 요구를 구분합니다.
- [W392](workbook-questions.md#w392) · **1번**: ITEM_DISCONTINUED
  - 해설: 단종을 오류로 다루는 가정에서는 ITEM_DISCONTINUED가 해당 의미를 나타냅니다. 현재 ItemService의 재단종 처리와 대안 연습의 전제를 구분합니다.

## 29. 발주 — 내 목록 / 상세 / 취소 + 관리자 목록 (PRD 2.5.5)

- [W124](workbook-questions.md#w124) · **3번**: 현재 인증 사용자의 ID로 작성자 조건을 적용한다
  - 해설: URL 값 조작에 의한 IDOR를 서버 인증 정보 기반 조회로 방지합니다.
- [W125](workbook-questions.md#w125) · **3번**: DRAFT·REQUESTED·APPROVED
  - 해설: 현재 PurchaseOrder는 입고 후 작성자 취소를 허용하지 않습니다.
- [W126](workbook-questions.md#w126) · **1번**: 작성자 또는 ADMIN/MANAGER
  - 해설: 역할과 작성자 소유권을 인증된 사용자 기준으로 검사합니다.
- [W127](workbook-questions.md#w127) · **4번**: 상태와 거래처 조건
  - 해설: 현재 관리자 목록은 status·partnerId 선택 조건을 사용합니다.
- [W128](workbook-questions.md#w128) · **2번**: 전달된 조건만 만들어 AND로 결합한다
  - 해설: Specification 등의 조합으로 여러 선택 조건을 일관되게 처리합니다.
- [W129](workbook-questions.md#w129) · **3번**: 날짜 범위를 검증하고 조회 조건·API·테스트를 함께 확장한다
  - 해설: 기간 검색의 경계와 누락 값 정책을 명시하고 다른 검색 조건과 조합합니다.
- [W352](workbook-questions.md#w352) · **1번**: 서버가 인증한 사용자 정보와 대상의 소유권 정보
  - 해설: 대상별 정책을 공통화하되 현재 역할과 실제 대상 관계를 서버 값으로 판단합니다.

## 30. 수주 — my / pending / detail 권한 검사 (PRD 2.5.7)

- [W130](workbook-questions.md#w130) · **2번**: ACCESS_DENIED로 거절한다
  - 해설: 현재 구현은 작성자 또는 ADMIN/MANAGER에 조회를 허용합니다. 과거 담당자 제한 과제와 구분합니다.
- [W131](workbook-questions.md#w131) · **1번**: 담당자 기준이면 위임된 ID로 조회 조건·권한 정책을 일치시켜야 한다
  - 해설: 현재 참조 구현의 역할 기반 처리 대기 목록과 담당자별 대안 정책을 구분합니다.
- [W132](workbook-questions.md#w132) · **4번**: 인증 사용자 정보와 검색·페이지 조건을 받아 관리자 권한을 확인한다
  - 해설: 검색 계약과 실행 권한을 서버에서 함께 검증합니다.
- [W187](workbook-questions.md#w187) · **2번**: 작성자 또는 ADMIN/MANAGER
  - 해설: 현재 requireAccess는 작성자와 두 관리 역할을 허용합니다. 과거 과제의 담당자 제한과 구분합니다.
- [W188](workbook-questions.md#w188) · **3번**: CONFIRMED
  - 해설: 현재 SalesOrderService.pending은 출고 전 CONFIRMED 목록을 조회합니다. 완료 대기는 별도 SHIPPED 목록입니다.
- [W257](workbook-questions.md#w257) · **3번**: 현재 사용자가 작성한 수주
  - 해설: 인증 사용자 ID의 작성자 조건을 사용해 본인 목록을 조회합니다.
- [W258](workbook-questions.md#w258) · **2번**: 조회 시점의 수주 상태
  - 해설: 상태 조건은 작성자·거래처·기간·페이지 조건과 요구에 맞게 조합합니다.
- [W353](workbook-questions.md#w353) · **1번**: ADMIN 또는 MANAGER의 역할 기반 목록에 포함될 수 있다
  - 해설: 현재 pending은 상태로 조회하고 역할을 검사합니다. 담당자별 제한 목록은 별도 대안 정책입니다.

## 31. Repository — User / Partner / Category / PurchaseOrder / Notice / SalesOrder (TRD 3.5)

- [W133](workbook-questions.md#w133) · **2번**: 연관된 partner의 businessNumber 조건이다
  - 해설: 중첩 속성 탐색 구분자를 사용해 연관관계의 속성을 조회합니다.
- [W134](workbook-questions.md#w134) · **4번**: 양 끝 날짜를 포함하는 범위 조건이다
  - 해설: 날짜 범위 조건과 from≤to 검증을 함께 적용합니다.
- [W135](workbook-questions.md#w135) · **1번**: DB에서 viewCount = viewCount + 1을 직접 실행한다
  - 해설: Modifying Query와 트랜잭션에서 DB가 증가 연산을 수행해 유실을 줄입니다.
- [W136](workbook-questions.md#w136) · **3번**: 가독성·조합 조건·조인 요구에 따라 Query나 Specification을 선택한다
  - 해설: 명시 쿼리와 동적 조건 추상화의 목적에 맞게 선택합니다.
- [W230](workbook-questions.md#w230) · **1번**: Item을 기준으로 Stock을 LEFT JOIN하고 없는 재고를 0으로 처리한다
  - 해설: 재고 행이 없는 품목을 누락하지 않도록 품목 중심 조회와 null 처리로 구성합니다.
- [W354](workbook-questions.md#w354) · **3번**: 엔티티 속성을 기준으로 파싱하며 Column 매핑이 DB 이름에 연결한다
  - 해설: 속성명을 바꾸면 메서드 경로를 함께 고쳐야 하며 DB 컬럼만 변경하는 경우 매핑을 대조합니다.
- [W355](workbook-questions.md#w355) · **2번**: partner.id 속성 경로를 조건으로 해석하며 실제 SQL은 매핑·최적화에 따라 결정된다
  - 해설: 속성 경로와 실제 SQL JOIN 유무를 단정하지 말고 매핑과 쿼리 로그를 확인합니다.

## 32. 응답 DTO 매핑 패턴 (TRD 3.16.3)

- [W137](workbook-questions.md#w137) · **1번**: 엔티티에서 공개할 응답 필드를 일관되게 변환한다
  - 해설: DTO 생성·매핑 책임을 한곳에 모으는 패턴입니다.
- [W138](workbook-questions.md#w138) · **3번**: 페이지 메타 정보와 변환된 content
  - 해설: 전체 개수와 페이지 수를 잃지 않고 Entity를 DTO로 바꿉니다.
- [W139](workbook-questions.md#w139) · **4번**: 헤더 DTO 안에 라인 DTO 목록을 둔다
  - 해설: 응답 계약에 필요한 헤더·라인 필드를 명시해 중첩 구조로 구성합니다.
- [W140](workbook-questions.md#w140) · **2번**: Entity가 HTTP 응답 DTO에 의존하지 않게 한다
  - 해설: 도메인 모델은 외부 표현 계약의 변경으로부터 독립시키는 것이 좋습니다.
- [W141](workbook-questions.md#w141) · **4번**: record는 구성 요소·접근자·값 동등성 등을 간결하게 선언한다
  - 해설: record의 참조 재대입 제한과 참조하는 객체의 변경 가능성은 별개입니다.
- [W142](workbook-questions.md#w142) · **2번**: 반복 매핑의 규모·복잡도와 디버깅·의존 비용을 비교한다
  - 해설: 단순 팩토리로 충분한지, 도구가 유지 비용을 줄이는지 판단합니다.
- [W177](workbook-questions.md#w177) · **1번**: 비밀번호 해시·내부 토큰 등 민감정보
  - 해설: 평문뿐 아니라 비밀번호 해시와 내부 인증 자료도 응답 계약에서 제외합니다.
- [W247](workbook-questions.md#w247) · **4번**: 화면이 한 상세 응답에서 필요한 구성 정보를 얻는다
  - 해설: HTTP 요청 횟수와 SQL 조회 횟수는 다릅니다. 응답 구조와 조회 계획을 따로 설계합니다.

## 33. 핵심 Bean 설정 — JPA Auditing / PasswordEncoder / WebMvcConfig (TRD 3.5, 3.11)

- [W143](workbook-questions.md#w143) · **3번**: 감사 어노테이션이 붙어도 시각 자동 기록이 동작하지 않을 수 있다
  - 해설: 감사 리스너와 EnableJpaAuditing 등 관련 설정이 연결되어야 합니다.
- [W144](workbook-questions.md#w144) · **1번**: salt와 조절 가능한 연산 비용으로 대량 추측 비용을 높인다
  - 해설: 비밀번호 해시는 빠른 일반 해시와 목적이 다르며 BCrypt는 matches로 검증합니다.
- [W145](workbook-questions.md#w145) · **3번**: addInterceptors는 요청 검사, addArgumentResolvers는 인자 해석 등록이다
  - 해설: MVC 요청 처리의 확장 지점을 설정에 연결합니다.
- [W146](workbook-questions.md#w146) · **1번**: 인증 원천·리다이렉트·예외 정책 충돌을 피하기 위해서다
  - 해설: 현재 구조는 역할을 분담하고 전체 인증 이전 때는 중복 계약을 조정해야 합니다.
- [W226](workbook-questions.md#w226) · **2번**: PasswordEncoder
  - 해설: BCryptPasswordEncoder는 PasswordEncoder 인터페이스를 구현합니다. 현재 설정의 반환 타입과 Service 주입 타입은 BCryptPasswordEncoder입니다.
- [W356](workbook-questions.md#w356) · **4번**: 알고리즘·설정과 주입할 객체를 애플리케이션이 명확히 선택하기 위해서다
  - 해설: 자동 설정으로 원하는 Encoder가 항상 생긴다고 가정하지 않고 PasswordConfig의 실제 등록을 확인합니다.
- [W357](workbook-questions.md#w357) · **4번**: CreatedBy·LastModifiedBy는 감사 사용자 공급이 필요하고 시각 감사와 별도다
  - 해설: 사용자 감사에는 AuditorAware 등의 정책이 필요하며 현재 시각 감사 구성과 구분합니다.
- [W393](workbook-questions.md#w393) · **2번**: /swagger-ui/**와 /v3/api-docs/**
  - 해설: 기본 Swagger UI와 OpenAPI 문서 경로입니다. 현재 프로젝트가 해당 라이브러리를 이미 제공한다는 뜻은 아니며 운영 공개 여부는 별도 권한 정책으로 결정합니다. 참조: https://springdoc.org/

## 34. 세션 인터셉터 + @CurrentUser ArgumentResolver (TRD 3.11.1)

- [W147](workbook-questions.md#w147) · **4번**: 세션 로그인 정보를 컨트롤러 파라미터 값으로 해석한다
  - 해설: supportsParameter와 resolveArgument로 지원 파라미터를 찾고 LoginUser를 제공합니다.
- [W148](workbook-questions.md#w148) · **2번**: Filter가 MVC에 앞서고 Interceptor는 Controller 호출 전후에 동작한다
  - 해설: Filter는 Servlet 계층, Interceptor는 MVC 핸들러 처리 계층에 있습니다.
- [W149](workbook-questions.md#w149) · **4번**: 컨트롤러마다 세션 로그인 정보를 꺼내 확인하는 코드
  - 해설: 사용자 해석을 공통화하면 컨트롤러의 반복과 일관성 문제를 줄입니다.
- [W150](workbook-questions.md#w150) · **3번**: 인증 정보 추출·토큰 검증·사용자 해석과 폐기 정책이다
  - 해설: 인증 원천은 달라져도 업무 권한과 소유권 규칙은 계속 필요합니다.
- [W227](workbook-questions.md#w227) · **3번**: 전반적 로그인 검사에서 로그인·정적 리소스 등을 제외한다
  - 해설: H2 콘솔 서블릿의 실제 보호는 SecurityConfig에서 별도 처리합니다.
- [W343](workbook-questions.md#w343) · **1번**: 전자는 지정 세션 속성 바인딩, 후자는 사용자 해석 정책을 공통 resolver로 추상화한다
  - 해설: 현재 CurrentUser는 LoginUser·미인증 처리 등 공통 정책을 컨트롤러 인자 해석에 모읍니다.
- [W358](workbook-questions.md#w358) · **3번**: MVC 예외 처리 흐름·Advice 적용 범위·해당 핸들러가 맞아야 한다
  - 해설: 컨트롤러 인자 해석 단계의 예외도 처리할 수 있으나 Filter 등 다른 실행 경계와 구분합니다.
- [W362](workbook-questions.md#w362) · **4번**: 전자는 Security 인증 principal, 후자는 현재 세션 LoginUser 해석을 사용한다
  - 해설: 인증 구조를 옮길 때 컨트롤러 인자 계약과 인증 원천을 함께 맞춰야 합니다.

## 35. Thymeleaf 화면 (PRD 2.7)

- [W151](workbook-questions.md#w151) · **2번**: templates와 static 아래에서 역할별로 분리한다
  - 해설: 서버 렌더링 템플릿과 직접 제공하는 CSS·JS 리소스의 위치를 구분합니다.
- [W152](workbook-questions.md#w152) · **1번**: HTML escape를 수행하는 th:text
  - 해설: th:utext는 신뢰·정화된 HTML을 의도적으로 표시할 때만 신중히 사용합니다.
- [W153](workbook-questions.md#w153) · **2번**: 통합된 토큰 hidden input을 사용하고 JS 요청은 토큰을 명시 전달한다
  - 해설: 서버 폼 통합과 일반 HTML·JavaScript 요청의 토큰 전달 방식을 구분합니다.
- [W154](workbook-questions.md#w154) · **1번**: th:fragment 정의와 th:replace 사용
  - 해설: 공통 레이아웃을 조각으로 정의해 여러 화면에서 재사용합니다.
- [W155](workbook-questions.md#w155) · **4번**: 추가·삭제 후 입력 name 인덱스와 서버 검증을 유지한다
  - 해설: 사용성 보조 JS와 서버 바인딩·업무 검증이 함께 맞아야 합니다.
- [W156](workbook-questions.md#w156) · **3번**: RestController는 본문 응답, Controller는 View 이름 반환 등을 사용한다
  - 해설: 웹 화면과 API는 같은 Service를 공유하며 표현 방식을 나눌 수 있습니다.
- [W203](workbook-questions.md#w203) · **2번**: 보통 403으로 거절된다
  - 해설: 보호 대상 메서드·경로와 정책을 전제로 합니다. 현재 API 예외 정책과 일반 원칙을 구분합니다.
- [W204](workbook-questions.md#w204) · **4번**: replace는 대상 요소를 교체하고 insert는 내부에 조각을 넣는다
  - 해설: 호스트 요소의 유지 여부가 달라 최종 HTML 구조에 영향을 줍니다.
- [W205](workbook-questions.md#w205) · **1번**: Spring 바인더가 각 라인의 필드를 올바르게 구성하도록 하기 위해서다
  - 해설: 중복·누락된 인덱스는 잘못된 값 매핑이나 바인딩 실패로 이어질 수 있습니다.
- [W206](workbook-questions.md#w206) · **4번**: 추가 왕복 지연과 입력 상태 보존 처리가 필요하다
  - 해설: JS 보조는 화면 내 편집을 돕지만 서버의 검증·계산 책임은 유지해야 합니다.
- [W207](workbook-questions.md#w207) · **2번**: 표현 컨트롤러를 나누고 업무 Service를 공유한다
  - 해설: 중복되는 업무 로직을 공통 Service에 모으고 각 표현 계약을 분리합니다.
- [W208](workbook-questions.md#w208) · **3번**: 서버 HTML은 초기 구성에 단순하고 복잡한 상호작용에는 클라이언트 코드가 추가된다
  - 해설: 화면 복잡도·팀 기술·렌더링 방식·운영 비용에 맞춰 선택합니다.
- [W209](workbook-questions.md#w209) · **1번**: HTML 형태를 유지해 브라우저에서 시안을 열어 볼 수 있다
  - 해설: 브라우저의 정적 미리보기와 서버의 표현식 렌더링은 다릅니다.
- [W210](workbook-questions.md#w210) · **2번**: 폼 객체·필드 이름·값·검증 오류의 바인딩
  - 해설: 객체 기반 폼 필드 구성을 돕고 서버에서 DTO 검증을 수행합니다.
- [W211](workbook-questions.md#w211) · **4번**: templates/error/404.html 같은 경로
  - 해설: 기본 오류 처리에 맞는 상태별 템플릿을 사용하거나 현재 WebExceptionHandler 계약에 맞게 구성합니다.
- [W212](workbook-questions.md#w212) · **3번**: 사용 편의를 위한 표시이며 실제 API·Service 인가가 필요하다
  - 해설: 현재 화면 조건과 서버 인가를 구분합니다. Security dialect 방식은 관련 통합과 인증 정보를 요구합니다.
- [W394](workbook-questions.md#w394) · **3번**: Model.addAttribute로 이름과 값을 등록한다
  - 해설: 컨트롤러가 Model에 이름으로 등록한 값을 템플릿에서 참조합니다. 영속성 컨텍스트나 세션 종료 기능과 구분합니다.

## 36. 패키지 구조와 계층 책임 (TRD 3.2.2)

- [W157](workbook-questions.md#w157) · **3번**: 전자는 기술 계층별, 후자는 기능별 응집을 우선한다
  - 해설: 규모와 변경 단위에 맞춰 선택하며 패키지 전략만으로 좋은 설계가 보장되지는 않습니다.
- [W158](workbook-questions.md#w158) · **4번**: Controller → Service → Repository, Service → Domain
  - 해설: 업무 도메인을 HTTP 표현과 세션으로부터 독립시키는 방향입니다.
- [W159](workbook-questions.md#w159) · **1번**: Controller·HttpSession·응답 DTO
  - 해설: 도메인 타입 사용과 외부 표현·인프라 책임의 결합을 구분합니다.
- [W160](workbook-questions.md#w160) · **2번**: Controller가 Repository를 직접 호출하며 업무 상태를 변경한다
  - 해설: HTTP 표현 책임과 업무 처리 책임을 분리합니다.
- [W213](workbook-questions.md#w213) · **4번**: 조회 전용 projection 계약을 사용하는 경우
  - 해설: 모든 DTO 의존을 기계적으로 금지하지 않고 조회 모델의 목적과 결합을 검토합니다.
- [W214](workbook-questions.md#w214) · **2번**: Entity는 데이터·setter만 갖고 업무 규칙이 Service에 집중된다
  - 해설: 업무 규칙의 표현과 변경 지점을 검토해 의미 있는 도메인 동작으로 옮길 수 있습니다.
- [W215](workbook-questions.md#w215) · **3번**: 응집된 업무·정책 단위로 책임을 분리한다
  - 해설: 도메인 서비스·정책 객체 등으로 응집도와 의존 방향을 개선하고 동작을 검증합니다.
- [W216](workbook-questions.md#w216) · **1번**: 공통 업무 책임과 호출 방향을 재설계한다
  - 해설: 순환은 책임·경계 문제의 신호일 수 있어 객체 관계와 흐름을 정리해야 합니다.
- [W217](workbook-questions.md#w217) · **2번**: controller·service·repository·domain 중심의 계층 우선
  - 해설: 기본 계층을 쉽게 찾을 수 있지만 한 기능 변경이 여러 패키지에 걸칠 수 있습니다.
- [W218](workbook-questions.md#w218) · **1번**: 변경 단위·응집도·의존 관계를 검토하고 점진적으로 이동한다
  - 해설: 패키지 구조는 도구이며 실제 책임과 검증된 동작을 유지해야 합니다.
- [W219](workbook-questions.md#w219) · **4번**: 사용 이유·변경 파급·순환과 불필요한 책임을 확인한다
  - 해설: 공통 타입의 필요성과 과도한 결합을 실제 변경·의존 방향으로 평가합니다.
- [W270](workbook-questions.md#w270) · **4번**: Service
  - 해설: Controller는 HTTP 계약을 처리하고 Service가 업무 흐름·트랜잭션·인가를 조정합니다.

## 37. 비즈니스 규칙 다층 검증 매트릭스 (TRD 3.8, 3.11)

- [W161](workbook-questions.md#w161) · **4번**: 입력 형식·불변식·업무 흐름·최종 무결성
  - 해설: 검증은 서로 다른 진입 경로·업무 경계·경쟁 상태를 방어합니다.
- [W162](workbook-questions.md#w162) · **2번**: 서로 다른 우회·호출 경로와 경쟁 요청을 방어하기 위해서다
  - 해설: 같은 정책을 일관되게 유지하면서 계층마다 다른 위험을 다룹니다.
- [W163](workbook-questions.md#w163) · **1번**: DB unique 제약이 최종 중복 저장을 차단한다
  - 해설: 사전 조회와 저장 사이의 경쟁 상태는 DB 제약으로 다룹니다.
- [W164](workbook-questions.md#w164) · **3번**: Service 등 명시한 경계에서 일관된 업무 예외로 변환한다
  - 해설: 현재 Service는 도메인 상태 실패를 INVALID_STATUS 등 안정적인 계약으로 변환합니다.
- [W220](workbook-questions.md#w220) · **3번**: DTO의 DecimalMin과 도메인·Service의 값 검증을 맞춘다
  - 해설: 외부 형식 검증과 다른 호출 경로의 불변식 보호를 함께 고려합니다.
- [W221](workbook-questions.md#w221) · **2번**: DTO의 Size(min=1)와 중첩 입력 검증
  - 해설: 입력 경계에서 검증하고 Service·도메인에서도 필요한 불변식을 보장합니다.
- [W222](workbook-questions.md#w222) · **4번**: 관련 이력을 조회하는 Service 정책에서 검증한다
  - 해설: 이 문제는 해당 금지 정책을 전제로 합니다. 이력 조회가 필요한 업무 정책을 Service에서 조정합니다.
- [W249](workbook-questions.md#w249) · **3번**: 다른 호출부가 도메인 메서드를 직접 사용하면서 검증을 빠뜨릴 수 있다
  - 해설: 중요 불변식을 도메인 동작에 모으면 다른 진입 경로에서도 같은 규칙을 보호합니다.
- [W268](workbook-questions.md#w268) · **4번**: 다른 진입 경로가 Service를 호출할 때 업무 인가를 우회할 수 있다
  - 해설: 서버 업무 경계에서 필요한 인가를 보장해야 합니다.

## 38. 트러블슈팅 워크북 (TRD 3.16)

- [W165](workbook-questions.md#w165) · **2번**: 각 실패 경계를 재현하고 상태·트랜잭션·DTO·조회·검증·동시성 대책을 검증한다
  - 해설: 실패 종류마다 다른 원인과 대책이 있으며 회귀 테스트로 근거를 남깁니다.
- [W166](workbook-questions.md#w166) · **4번**: 재현 가능한 문제·원인·해결·배운 점과 검증을 남긴다
  - 해설: 독자가 같은 조건에서 확인할 수 있는 근거를 함께 기록합니다.
- [W167](workbook-questions.md#w167) · **3번**: 실제 재현·관찰·선택·검증 근거로 작성한다
  - 해설: 개인 기록의 내용은 사람마다 다르지만 증거에 근거한 작성 기준은 같습니다.
- [W261](workbook-questions.md#w261) · **4번**: 요청 매핑·인가·입력·업무·DB·DTO 단계의 실제 실패 지점을 확인한다
  - 해설: 마지막으로 성공한 단계와 실패 증거를 연결하면 원인 범위를 줄일 수 있습니다.
- [W262](workbook-questions.md#w262) · **1번**: 전자는 마이그레이션 이력, 후자는 엔티티와 실제 스키마 일치를 확인한다
  - 해설: checksum·version 문제와 매핑·컬럼·타입 불일치를 서로 다른 검증 경계에서 진단합니다.
- [W263](workbook-questions.md#w263) · **3번**: 리스닝·주소·포트 연결과 사용자·비밀번호·권한 증거를 각각 확인한다
  - 해설: 네트워크 접속 실패와 서버 접속 후 인증 거절은 원인 범위가 다릅니다.
- [W264](workbook-questions.md#w264) · **2번**: 활성 프로필과 실제 JDBC URL이 임시 H2인지 확인한다
  - 해설: 기본 in-memory H2는 재시작 시 데이터가 초기화됩니다. 영속 MySQL 연결과 구분해야 합니다.

## 39. 면접 카드 + 커밋 + PR 템플릿 (TRD 3.18 ~ 3.20)

- [W168](workbook-questions.md#w168) · **1번**: 문제·선택 이유·대안·코드나 테스트 근거를 연결한다
  - 해설: 기술 선택을 구체적인 업무 상황과 검증에 연결합니다.
- [W169](workbook-questions.md#w169) · **3번**: fix·refactor·docs·test
  - 해설: feat는 기능, fix는 버그, refactor는 구조, docs는 문서, test는 테스트 변경을 표현합니다.
- [W170](workbook-questions.md#w170) · **1번**: diff만으로 보이지 않는 동기와 판단을 전달하기 위해서다
  - 해설: 변경 내용은 diff로 확인하고 본문은 이유와 필요한 맥락을 전달합니다.
- [W171](workbook-questions.md#w171) · **4번**: 무엇·왜·검증·영향 범위
  - 해설: 변경 결과와 이유·테스트·마이그레이션 등 영향 정보를 검토자가 이해할 수 있게 적습니다.
- [W235](workbook-questions.md#w235) · **4번**: SCM 마스터·주문 상태·권한·재고 흐름을 Java/Spring으로 구현하고 검증한다
  - 해설: 실제 기능과 코드·테스트 근거에 맞게 프로젝트 목적을 설명합니다.
- [W240](workbook-questions.md#w240) · **1번**: fix
  - 해설: 오류 동작을 수정했으므로 fix 타입이 자연스럽습니다.
- [W241](workbook-questions.md#w241) · **2번**: refactor
  - 해설: 기능·결과를 유지하는 구조 개선은 refactor입니다.
- [W242](workbook-questions.md#w242) · **1번**: docs
  - 해설: 문서 변경을 나타내는 docs 타입을 사용합니다.
- [W243](workbook-questions.md#w243) · **3번**: test
  - 해설: 동작 검증용 테스트 변경은 test입니다.
- [W244](workbook-questions.md#w244) · **4번**: fix: REQUESTED가 아닌 발주의 승인 요청 차단
  - 해설: 변경 결과·조건·목적을 이해할 수 있는 구체적인 제목이 검토와 추적을 돕습니다.
- [W266](workbook-questions.md#w266) · **2번**: 실제 구현한 주문·권한·재고 규칙과 검증 근거를 자신의 역할에 맞게 요약한다
  - 해설: 개인 표현은 달라도 사실에 맞는 역할·선택·검증을 연결하는 기준은 같습니다.
- [W269](workbook-questions.md#w269) · **2번**: 오답을 다시 풀고 해설의 규칙을 실제 코드·테스트와 대조한다
  - 해설: 객관식 채점과 코드 구현의 검증은 각각의 학습·확인 절차를 갖습니다.
- [W359](workbook-questions.md#w359) · **2번**: 재고 키·출고지 권한·예약/차감·동시성·테스트 계약을 함께 설계한다
  - 해설: 추가 요구의 데이터·권한·상태·트랜잭션 경계를 명시해 실제 구현과 검증을 연결합니다.
- [W396](workbook-questions.md#w396) · **1번**: 취업 역량이나 사용자 가치와 연결된 한 문장 목표
  - 해설: 목표는 기능 수보다 증명할 역량이나 개선할 가치와 연결되어야 합니다.
- [W397](workbook-questions.md#w397) · **1번**: 현재 코드·문서·관련 테스트와 실제 동작
  - 해설: 기준선은 변경 전 동작과 근거를 확인해 이후 검증의 출발점을 만듭니다.
- [W398](workbook-questions.md#w398) · **3번**: 입력·출력·권한·상태·실패 조건
  - 해설: 규칙표와 API·상태 계약을 통해 성공 경로와 실패 경로의 기대를 명확히 합니다.
- [W399](workbook-questions.md#w399) · **4번**: 잘못된 구현을 잡는 테스트나 재현 절차
  - 해설: 테스트나 재현 절차는 오류 조건을 실제로 드러내고 수정 효과를 확인할 근거가 됩니다.
- [W400](workbook-questions.md#w400) · **2번**: 계약을 만족하는 필요한 코드·문서 변경
  - 해설: 변경 범위를 계약에 맞춰 좁혀 결과를 검토하고 검증할 수 있게 합니다.
- [W401](workbook-questions.md#w401) · **4번**: 적절한 테스트의 실행 명령과 실제 결과
  - 해설: 실행한 검사와 결과를 기록합니다. 환경상 실행하지 못한 검증은 성공으로 기록하지 않습니다.
- [W402](workbook-questions.md#w402) · **1번**: 선택한 설계와 대안·검증 근거를 코드와 대조한다
  - 해설: 선택 이유는 실제 구현과 증거로 뒷받침합니다. 객관식 정답·해설을 실제 코드에 적용해 이해를 확인합니다.
- [W403](workbook-questions.md#w403) · **3번**: 코드와 문서·HTML 목차·변경 이력·관련 링크
  - 해설: 산출물이 같은 동작을 설명하고 파일 링크가 실제 자료를 가리키도록 정리합니다.
