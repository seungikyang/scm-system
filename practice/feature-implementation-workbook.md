# 기능 구현 TODO/빈칸 워크북

이 문서는 `scm_system_PRD_TRD.md` 의 모든 기능 요구사항을 구현 단위로 다시 쪼개는 학습용 워크북입니다.

기존 `practice/problems.md` 와 `practice/starter/` 가 코드 조각 중심이라면, 이 문서는 **FR ID → 화면/API → Controller → Service → Repository → Entity/DTO → 테스트** 흐름을 직접 채워보게 합니다.

## 사용 규칙

1. 각 기능의 `개념 빈칸`을 먼저 채운다.
2. 이어서 `구현 TODO`의 계층별 빈칸을 채운다.
3. 마지막으로 `테스트 TODO`를 Given/When/Then 으로 한 줄씩 적는다.
4. 막히면 PRD/TRD 절 번호와 `practice/starter` 파일을 다시 본다.
5. 공통 계층·검색·페이징 계약은 한 절만 기준으로 삼고, 기능별 절에는 차이점과 증거만 적는다.

핵심 구현(사용자·마스터·품목·발주·입고 재고)은 현재 테스트와 [DESIGN_DECISIONS.md](./DESIGN_DECISIONS.md)를 기준으로 비교합니다. 공지·수주는 현재 `src/`에 없는 확장 설계 과제이므로 요구사항, 상태 표, 실패 테스트를 먼저 작성합니다.

## 공통 구현 지도

아래 빈칸을 먼저 채우면 모든 기능의 구현 방향이 흔들리지 않습니다.

| 계층 | 책임 | 경계: 넣지 말아야 할 것 | 빈칸 |
|---|---|---|---|
| Controller | HTTP 요청/응답, 인증 사용자 주입, DTO 검증 | 중복 조회, 상태 전이, 직접 SQL/Repository 호출 | Controller 는 Repository 를 직접 호출하지 않고 ____ 를 호출한다. |
| Service | 비즈니스 규칙, 트랜잭션, 권한/소유자 검증 | Servlet 응답 조작, 화면 HTML 생성 | 상태 전이는 주로 ____ 계층 또는 도메인 메서드에서 검증한다. |
| Repository | DB 조회/저장, 조건 검색, 페이징 | HTTP status와 사용자 메시지 결정 | 복잡한 조건은 메서드 이름이 길어지면 ____ 로 옮긴다. |
| Entity | 식별자, 연관관계, 상태값, 도메인 메서드 | Controller DTO 의존, 세션 접근 | 상태값은 String 대신 ____ 으로 관리한다. |
| DTO | 요청/응답 계약, Validation | 영속성 생명주기와 비즈니스 상태 변경 | Entity 를 그대로 응답하지 않고 ____ DTO 로 변환한다. |
| Exception | 공통 에러 응답 | 기능마다 제각각인 오류 형식 | 예외 응답은 status, code, message, ____ 를 포함한다. |
| Test | 성공/실패/권한/상태 전이 검증 | 구현 세부사항에만 묶인 검증 | 승인/반려 기능은 ____ 상태가 아닌 경우를 반드시 테스트한다. |

## Spring Framework 상세 이해

이 프로젝트에서 Spring을 단순히 "어노테이션을 붙이면 실행되는 도구"로 외우지 말고, **객체 생성과 연결을 담당하는 컨테이너**, **HTTP 요청을 객체 호출로 바꾸는 MVC**, **공통 기능을 앞뒤에 끼우는 프록시/AOP**, **DB 작업 경계를 관리하는 트랜잭션 추상화**로 나누어 이해합니다.

### Spring Framework와 Spring Boot의 관계

- **Spring Framework**는 IoC/DI, Spring MVC, Validation 연동, AOP, 트랜잭션 같은 핵심 프로그래밍 모델을 제공합니다.
- **Spring Boot**는 Spring Framework 위에서 자동 설정, starter 의존성, 내장 Tomcat, 외부 설정, 실행 가능한 JAR 패키징을 제공합니다.
- `@SpringBootApplication`은 구성 클래스 선언, 컴포넌트 스캔, 자동 설정 활성화를 묶은 시작점입니다. `ScmApplication`의 패키지 아래에 있는 `@Controller`, `@Service`, `@Repository`, `@Configuration` 등이 스캔 대상이 됩니다.
- starter는 기능 묶음입니다. 예를 들어 `spring-boot-starter-webmvc`는 Spring MVC와 내장 웹 서버를, `spring-boot-starter-data-jpa`는 Spring Data JPA와 Hibernate 연동을 준비합니다. starter 자체가 비즈니스 로직을 대신 작성해 주지는 않습니다.

### 현재 기준 버전과 호환성 경계

이 워크북의 기준일은 **2026-08-25**이며, 현재 기준은 **Spring Boot 4.1.1, Spring Framework 7.0.x, Gradle Wrapper 9.7.1**입니다. 버전은 설명에 흩어 쓰지 않고 루트 `build.gradle`과 `gradle/wrapper/gradle-wrapper.properties`를 최종 기준으로 삼습니다.

| 항목 | 현재 기준 | 구현에서의 의미 |
|---|---|---|
| Java 컴파일 대상 | 17 | 기존 배포 호환성을 유지하며 Boot 4 최소 요구사항을 만족 |
| macOS 운영 JVM | 21 | Java 17 대상 JAR를 LTS JVM으로 직접 실행 |
| Spring Boot | 4.1.1 | 자동 설정, 의존성 BOM, 실행 가능한 JAR, 운영 설정 제공 |
| Spring Framework | 7.0.x | Boot가 관리하는 IoC/MVC/AOP/트랜잭션 기반 |
| Gradle Wrapper | 9.7.1 | 저장소의 재현 가능한 정식 빌드 도구 |
| dependency-management plugin | 1.1.7 | Boot BOM의 관리 버전을 Gradle 의존성에 적용 |
| JSON | Jackson 3 | `ObjectMapper` 패키지가 `tools.jackson.databind`로 이동 |

Java 버전에는 세 가지 의미가 있습니다. `sourceCompatibility`는 작성 가능한 문법, `targetCompatibility`는 생성되는 bytecode 수준, 실제 `java -jar`의 JVM은 실행 환경입니다. 이 저장소는 source/target 17이므로 Java 21에서 실행할 수 있지만, 반대로 Java 21 bytecode를 Java 17 JVM에서 실행할 수는 없습니다.

### Spring Boot 4에서 반드시 이해할 변경

1. **Spring Framework 7과 Jakarta EE 11**: Servlet, Validation, Persistence API는 `jakarta.*` 이름 공간을 사용합니다. 오래된 `javax.*` 예제를 그대로 복사하면 컴파일되지 않습니다.
2. **Jackson 3 기본화**: JSON mapper의 대표 패키지가 `com.fasterxml.jackson.databind`에서 `tools.jackson.databind`로 이동했습니다. 애플리케이션 코드의 `ObjectMapper` 타입도 Boot가 자동 구성하는 Jackson 3 타입과 맞춰야 합니다.
3. **builder 기반 mapper 구성**: Jackson 2의 `new ObjectMapper().findAndRegisterModules()` 대신 Jackson 3에서는 `JsonMapper.builder().findAndAddModules().build()`처럼 구성 후 불변 mapper를 만드는 방식을 사용합니다.
4. **starter 모듈화**: 기존 `spring-boot-starter-web` 대신 `spring-boot-starter-webmvc`를 사용하고, MVC 테스트는 `spring-boot-starter-webmvc-test`를 별도로 선언합니다. `@AutoConfigureMockMvc`의 패키지도 `org.springframework.boot.webmvc.test.autoconfigure`로 이동했습니다.
5. **자동 설정 패키지 재구성**: Boot 모듈별로 자동 설정 package가 나뉘었습니다. 예를 들어 기본 보안 사용자 자동 설정 제외 대상은 `org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration`입니다.
6. **BOM 우선**: Jackson, Hibernate, Spring Data 같은 하위 라이브러리 버전을 임의로 각각 올리지 않습니다. Boot 4.1.1이 검증한 BOM 조합을 사용하고, 꼭 override해야 한다면 회귀 테스트와 이유를 문서화합니다.

업그레이드 완료 조건은 컴파일 성공만이 아닙니다. `./gradlew clean portfolioCheck`, JAR 직접 실행, `/login` HTTP 200, DB 프로필의 Flyway/JPA 검증, 로그와 종료 동작까지 확인해야 자동 설정·직렬화·Servlet·DB 드라이버의 런타임 호환성을 증명할 수 있습니다.

### IoC, DI, Bean

- **IoC(Inversion of Control)**: 애플리케이션이 필요한 객체를 직접 생성·관리하는 대신 `ApplicationContext`가 객체의 생성, 연결, 생명주기를 맡습니다.
- **Bean**: Spring 컨테이너가 관리하는 객체입니다. 컴포넌트 스캔으로 발견되거나 `@Configuration`의 `@Bean` 메서드가 반환한 객체가 Bean이 됩니다.
- **DI(Dependency Injection)**: 한 객체가 필요로 하는 협력 객체를 외부에서 넣어 주는 방식입니다. 이 프로젝트는 Lombok의 `@RequiredArgsConstructor`와 `final` 필드를 이용한 생성자 주입을 주로 사용합니다.
- 생성자 주입은 필수 의존성을 빠뜨린 객체 생성을 막고, 테스트에서 가짜 협력 객체를 전달하기 쉽고, 필드를 불변으로 유지할 수 있습니다.
- 같은 타입의 Bean이 둘 이상이면 이름만 믿지 말고 `@Qualifier` 또는 대표 Bean인 `@Primary`로 선택 기준을 명시합니다.

이 프로젝트에서 직접 찾아 적기:

| 확인 대상 | Spring이 하는 일 | 이 저장소의 예 |
|---|---|---|
| `ScmApplication` | `ApplicationContext` 시작과 자동 설정 | `src/main/java/com/example/scm/ScmApplication.java` |
| `PasswordConfig` | 외부 라이브러리 객체를 `@Bean`으로 등록 | 등록되는 타입: ____ |
| `WebMvcConfig` | MVC 확장 지점에 Interceptor와 ArgumentResolver 연결 | 보호 경로와 제외 경로: ____ |
| `ItemService` | 생성자 주입으로 Repository 등 협력 객체를 받음 | 주입되는 필드: ____ |
| `ItemRepository` | 런타임 프록시가 Repository 구현을 제공 | 직접 구현 클래스가 없어도 호출 가능한 이유: ____ |

### HTTP 요청이 DB까지 가는 흐름

```text
브라우저
  → 내장 Tomcat
  → Spring Security FilterChain
  → DispatcherServlet
  → HandlerMapping / HandlerAdapter
  → LoginInterceptor 또는 AdminOnlyInterceptor
  → CurrentUserArgumentResolver + Bean Validation
  → Controller
  → Service의 트랜잭션 프록시
  → Spring Data Repository 프록시
  → JPA(EntityManager) / Hibernate
  → DataSource / DB
  → DTO 변환 → View 또는 JSON 응답
```

1. **Filter**는 Servlet 계층에서 먼저 실행됩니다. 현재 `SecurityConfig`는 CSRF와 기본 보안 헤더를 담당하며, 세션 로그인·역할 인가 전체를 Spring Security에 맡긴 구조는 아닙니다.
2. **DispatcherServlet**은 모든 MVC 요청의 중앙 진입점입니다. `HandlerMapping`으로 Controller 메서드를 찾고 `HandlerAdapter`로 호출합니다.
3. **Interceptor**는 Controller 전후에서 세션 로그인과 관리자 접근을 확인합니다. Filter보다 Spring MVC에 가까우므로 어떤 Controller가 호출될지도 활용할 수 있습니다.
4. **ArgumentResolver**는 `@CurrentUser` 같은 사용자 정의 파라미터를 실제 값으로 바꿉니다. `@Valid`/`@Validated`는 DTO의 `jakarta.validation` 제약을 검사합니다.
5. **Controller**는 HTTP 계약에 집중합니다. 요청 파싱, 검증 결과, 상태 코드, View/응답 DTO를 다루고 비즈니스 규칙은 Service에 위임합니다.
6. **Service**는 권한·소유자·상태 전이·중복 같은 규칙과 트랜잭션 경계를 책임집니다.
7. **Repository**는 조회와 저장을 추상화합니다. Spring Data JPA가 인터페이스의 프록시 구현을 만들고 Hibernate가 Entity 상태 변경을 SQL로 변환합니다.
8. **예외 처리기**는 Controller 밖으로 나온 예외를 공통 응답 또는 오류 화면으로 바꿉니다. 이 프로젝트는 API와 Web 예외 처리기를 구분합니다.

### 프록시, AOP, `@Transactional`

- Spring은 대상 Service Bean 앞에 **프록시**를 두고 메서드 호출 전 트랜잭션을 시작하고, 정상 반환 시 commit, 실패 시 rollback한 뒤 연결을 정리합니다.
- 기본 rollback 대상은 `RuntimeException`과 `Error`입니다. checked exception까지 rollback해야 한다면 `rollbackFor` 정책을 명시하고 그 이유를 테스트로 고정합니다.
- 프록시를 거쳐야 부가기능이 적용됩니다. 같은 클래스 안에서 `this.someTransactionalMethod()`처럼 호출하는 **self-invocation**은 기본 프록시 방식에서 새 트랜잭션 설정을 적용하지 못할 수 있습니다.
- 트랜잭션 범위 안에서 조회한 Entity는 변경 감지(dirty checking) 대상입니다. setter를 무조건 늘리는 대신 의미 있는 도메인 메서드로 상태를 바꾸고, 트랜잭션 종료 시 SQL이 실행되는지 확인합니다.
- `readOnly = true`는 읽기 의도를 드러내고 일부 최적화에 도움을 주지만, DB 권한처럼 쓰기를 절대 차단하는 보안 경계로 오해하면 안 됩니다.
- LAZY 연관관계를 트랜잭션 밖의 View에서 처음 읽으면 초기화 예외나 추가 쿼리 문제가 생길 수 있습니다. 이 저장소는 `open-in-view: false`이므로 Service 안에서 필요한 값을 DTO로 변환합니다.
- 발주 입고는 상태 변경과 모든 재고 증가가 한 트랜잭션이어야 합니다. 한 라인이라도 실패하면 일부 재고만 반영되지 않도록 전체가 rollback되어야 합니다.

프록시 동작을 확인하는 질문:

- `new ItemService(...)`로 직접 만든 객체와 Spring에서 주입받은 `ItemService`의 차이는 무엇인가? ____
- `@Transactional` 메서드를 같은 객체 내부에서 직접 호출하면 어떤 문제가 생길 수 있는가? ____
- 입고 처리 중 세 번째 라인에서 예외가 나면 발주 상태와 앞선 두 라인의 재고는 어떻게 되어야 하는가? ____

### JPA와 Spring Data JPA의 역할 구분

- **JPA**는 Entity 매핑과 영속성 컨텍스트 같은 표준 API/규약이고, **Hibernate**는 이 프로젝트가 사용하는 JPA 구현체입니다.
- **Spring Data JPA**는 Repository 인터페이스, 메서드 이름 쿼리, `Pageable`, `Specification` 같은 편의 추상화를 제공합니다.
- Entity가 연관관계를 가진다는 사실만으로 API 응답 계약이 되지는 않습니다. Controller 응답은 전용 DTO로 변환하여 비밀번호 노출, 순환 참조, LAZY 로딩, API 스키마 결합을 막습니다.
- 운영형 MySQL 프로필에서 Flyway가 스키마 변경 이력을 관리하고 Hibernate의 `ddl-auto=validate`가 Entity 매핑과 실제 스키마의 일치 여부를 검사합니다.

### Spring 흐름에서 관찰할 지점

- 요청마다 URL·HTTP method·status와 Controller 매핑을 먼저 기록합니다.
- Filter → DispatcherServlet → Interceptor → Controller → Service → Repository 순서에서 마지막으로 확인된 지점을 찾으면 실패 범위를 빠르게 줄일 수 있습니다.
- 세션 ID·비밀번호 같은 비밀은 로그에 남기지 않고, 오류 증상별 점검 위치는 아래 [11단계: 오류를 계층별로 추적하기](#11단계-오류를-계층별로-추적하기)의 단일 진단표를 사용합니다.

공식 참고 자료:

- https://docs.spring.io/spring-framework/reference/core.html
- https://docs.spring.io/spring-framework/reference/core/beans/annotation-config.html
- https://docs.spring.io/spring-framework/reference/data-access/transaction/declarative.html
- https://docs.spring.io/spring-boot/system-requirements.html
- https://docs.spring.io/spring-boot/reference/web/index.html
- https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.0-Migration-Guide
- https://docs.gradle.org/9.7.1/release-notes.html

## 워크북 요구사항을 프로그램으로 만드는 기술

워크북의 빈칸을 채우는 것과 실행 가능한 프로그램을 만드는 것은 다릅니다. 프로그램 구현은 **요구사항을 관찰 가능한 계약으로 바꾸고, 계약을 지키는 최소 수직 흐름을 만든 뒤, 실패 사례로 경계를 검증하는 작업**입니다. 아래 순서를 기능마다 반복합니다.

```text
FR 읽기
→ 입력·출력·권한·상태·실패 조건 결정
→ 테스트 가능한 계약 작성
→ Entity/DTO/Repository/Service/Controller 구현
→ 단위·통합 테스트
→ 실제 실행과 로그 확인
→ 문서·설계 결정 동기화
```

### 1단계: 요구사항을 구현 카드로 바꾸기

기능 이름만 보고 바로 Controller부터 만들지 않습니다. 먼저 하나의 FR을 다음 카드로 바꿉니다.

| 항목 | 결정할 질문 | 품목 등록 예시 |
|---|---|---|
| 행위자 | 누가 실행할 수 있는가? | `ADMIN` |
| 진입점 | HTTP method와 URL은? | `POST /api/items` |
| 입력 | 필수값, 형식, 범위는? | 코드·이름·카테고리·단위·단가·안전재고 |
| 출력 | status와 응답 DTO는? | `201 Created`, `ItemDetailView` |
| 비즈니스 규칙 | 단순 형식 검사를 넘어 무엇을 확인하는가? | 품목코드 중복 금지, 카테고리 존재 |
| 상태 변화 | Entity가 어떻게 변하는가? | 새 품목은 `ACTIVE` |
| 트랜잭션 | 함께 성공·실패해야 하는 작업은? | 검증 후 품목 저장 |
| 실패 계약 | 어떤 ErrorCode/status를 반환하는가? | 권한 없음, 중복 코드, 카테고리 없음 |
| 증거 | 어떤 테스트가 완료를 증명하는가? | 성공·검증·권한·중복·참조 무결성 테스트 |

요구사항이 모호하면 코딩으로 숨기지 말고 [DESIGN_DECISIONS.md](./DESIGN_DECISIONS.md)에 선택과 이유를 기록합니다. 예를 들어 “삭제”가 실제 행 삭제인지 `INACTIVE`/`DISCONTINUED` 전환인지 먼저 정해야 Repository와 API가 흔들리지 않습니다.

구현 카드 빈칸:

- 대상 FR ID: ____
- 행위자와 권한: ____
- 정상 입력/출력: ____
- 지켜야 할 규칙 3개: ____
- 실패 사례 3개와 ErrorCode: ____
- 하나의 트랜잭션으로 묶을 범위: ____

### 2단계: 수직 슬라이스로 작게 완성하기

Entity 전체를 먼저 만들고 모든 Repository, 모든 Service를 차례로 만드는 수평식 구현은 기능이 실제로 연결되는 시점이 늦습니다. 워크북에서는 기능 하나를 아래처럼 끝까지 연결하는 **수직 슬라이스**를 우선합니다.

```text
요청 DTO → Controller → Service → Repository → Entity/DB
          ← 응답 DTO  ← 결과/예외 ←
```

권장 구현 순서:

1. 성공과 실패를 설명하는 테스트 이름을 먼저 적습니다.
2. Entity의 필드, 상태, 도메인 메서드를 만듭니다.
3. 저장·조회에 꼭 필요한 Repository 계약만 추가합니다.
4. Request/Response DTO로 HTTP 계약을 고정합니다.
5. Service에 권한, 비즈니스 규칙, 트랜잭션을 구현합니다.
6. Controller를 얇게 연결하고 공통 예외 응답을 확인합니다.
7. 통합 테스트와 실제 요청으로 전체 흐름을 검증합니다.

한 번에 여러 FR을 구현하지 않습니다. 예를 들어 `FR-ITEM-001 품목 등록`의 성공·실패 흐름을 끝낸 뒤 목록, 상세, 수정으로 이동하면 어느 변경이 어느 실패를 만들었는지 추적하기 쉽습니다.

### 3단계: 계층의 경계를 코드로 지키기

계층별 책임과 금지 항목은 문서 앞부분의 [공통 구현 지도](#공통-구현-지도)를 단일 기준으로 사용합니다. 경계는 “파일을 나누는 규칙”이 아니라 변경 이유를 분리하는 기술입니다. HTTP 응답 형식이 바뀌어도 Entity 규칙은 유지되고, DB 조회 방식이 바뀌어도 Controller 계약은 불필요하게 흔들리지 않아야 합니다.

코드 리뷰에서는 다음 세 질문으로 경계를 확인합니다.

- 이 로직은 HTTP, 비즈니스 규칙, 영속성 중 무엇 때문에 바뀌는가?
- 하위 계층이 상위 계층의 DTO·세션·status code를 알고 있지는 않은가?
- Controller 없이 Service를, 실제 DB 없이 도메인 규칙을 각각 테스트할 수 있는가?

### 4단계: 입력 검증과 비즈니스 검증을 분리하기

두 종류의 검증을 섞지 않습니다.

- **입력 검증**은 값 하나의 모양을 확인합니다. `@NotBlank`, `@Size`, `@PositiveOrZero`, `@Digits`처럼 요청을 해석하자마자 판단할 수 있으며 DTO에 둡니다.
- **비즈니스 검증**은 DB나 현재 상태가 필요합니다. 품목코드 중복, 카테고리 존재, 작성자 본인, `REQUESTED` 상태 여부처럼 다른 객체와 정책을 알아야 하므로 Service/도메인에 둡니다.
- **DB 제약**은 마지막 안전망입니다. 애플리케이션의 사전 중복 검사만으로 동시 요청을 완전히 막을 수 없으므로 unique/FK 제약을 함께 둡니다.

예를 들어 `ItemCreateRequest`는 단가가 0 이상인지 검사하지만, 품목코드가 이미 존재하는지는 알 수 없습니다. `ItemService.create()`가 `existsByItemCode`로 사용자 친화적인 오류를 만들고, `items.item_code`의 unique 제약이 경합 상황의 최종 무결성을 보장합니다.

### 5단계: 도메인 상태를 데이터가 아닌 규칙으로 다루기

Entity의 status를 아무 곳에서나 setter로 바꾸면 금지된 전이를 막기 어렵습니다. 상태 변경은 의도가 드러나는 메서드로 표현합니다.

```java
public void discontinue() {
    this.status = ItemStatus.DISCONTINUED;
}
```

현재 참조 구현은 같은 요청을 다시 받아도 최종 상태가 동일한 멱등 동작을 선택했습니다. 반복 요청을 오류로 거절하고 싶다면 `INVALID_STATUS` 검증을 추가하되 API 계약, 테스트, 설계 결정 문서를 함께 바꿉니다. 구현 전에 다음을 판단합니다.

- 동일 상태 재요청을 멱등 성공으로 볼 것인가, 잘못된 상태로 거절할 것인가?
- 규칙을 Entity에 둘 것인가 Service에 둘 것인가?
- 어떤 예외 코드가 API 사용자에게 가장 안정적인 계약인가?
- 그 선택을 어떤 테스트로 고정할 것인가?

발주처럼 상태가 많은 기능은 먼저 [DESIGN_DECISIONS.md](./DESIGN_DECISIONS.md) 또는 [STATE_MACHINE.md](../docs/STATE_MACHINE.md)의 전이 표를 보고, **허용 목록 방식**으로 구현합니다. 현재 상태별 허용 동작을 명시하면 새 상태가 추가됐을 때 의도치 않게 모든 동작이 허용되는 문제를 줄일 수 있습니다.

### 6단계: Service 메서드를 규칙의 실행 순서로 작성하기

Service 코드는 단순 Repository 호출 모음이 아니라 유스케이스의 순서를 표현해야 합니다. 변경 기능은 보통 아래 순서를 따릅니다.

```text
인증 확인
→ 역할/소유자 권한 확인
→ 입력이 가리키는 Entity 조회
→ 현재 상태와 중복 규칙 검증
→ 계산 또는 상태 변경
→ 저장
→ 결과 식별자/DTO 반환
```

품목 등록의 실제 흐름은 다음과 같습니다.

```java
@Transactional
public Long create(ItemCreateRequest request, LoginUser loginUser) {
    Authz.requireRole(loginUser, UserRole.ADMIN);
    validateItemCodeUnique(request.getItemCode());
    validateCategoryExists(request.getCategoryId());

    Item item = Item.builder()
            .itemCode(request.getItemCode())
            .name(request.getName())
            .categoryId(request.getCategoryId())
            .unit(request.getUnit())
            .unitPrice(request.getUnitPrice())
            .safetyStock(request.getSafetyStock())
            .status(ItemStatus.ACTIVE)
            .build();
    return itemRepository.save(item).getId();
}
```

이 코드에서 학습할 것은 문법보다 순서입니다. 권한 없는 사용자가 DB 중복 여부를 탐색하지 못하게 권한을 먼저 확인하고, 저장 전에 참조 카테고리를 검증하며, 성공한 변경 전체를 하나의 트랜잭션으로 묶습니다.

### 7단계: DTO로 외부 계약을 보호하기

Entity를 요청·응답에 직접 사용하지 않습니다.

- Request DTO는 클라이언트가 설정할 수 있는 필드만 엽니다. `id`, `status`, `createdAt`, 작성자처럼 서버가 정해야 하는 값은 받지 않습니다.
- Response DTO는 화면/API에 필요한 값만 노출합니다. 비밀번호, 내부 토큰, 영속성 프록시를 포함하지 않습니다.
- Entity 필드명이 바뀌어도 외부 API 계약을 유지할 수 있도록 `from(entity)` 같은 변환 지점을 둡니다.
- 금액은 `double`이 아니라 `BigDecimal`을 사용하고, 합계는 클라이언트 값을 믿지 않고 서버에서 다시 계산합니다.
- 목록 응답은 Entity `Page`를 그대로 노출하지 않고 `PageResponse<T>`처럼 프로젝트가 통제하는 형식으로 변환합니다.

### 8단계: 트랜잭션과 동시성을 별도로 설계하기

`@Transactional`은 여러 DB 작업의 원자성을 보장하지만 모든 동시성 문제를 해결하지는 않습니다.

| 문제 | 필요한 기술 | 이 프로젝트의 예 |
|---|---|---|
| 중간 실패 시 일부만 저장 | 트랜잭션 rollback | 발주 상태 변경 + 모든 재고 증가 |
| 같은 요청을 두 번 처리 | 현재 상태 재검증, 멱등성 정책 | `RECEIVED` 재진입 차단 |
| 두 요청이 같은 최초 재고 행 생성 | 잠금 + DB unique 제약 | `findAllByIdForUpdate` |
| 중복 코드 동시 등록 | 사전 검사 + DB unique 제약 | `item_code`, `business_number` |
| 서로 다른 순서로 여러 행 잠금 | 잠금 순서 통일 | 품목 ID 정렬 후 잠금 |

동시성 기능은 “한 번 실행하면 성공”만으로 완료하지 않습니다. 두 개 이상의 요청을 동시에 보내 최종 상태, 행 개수, 수량 합계가 보존되는지 테스트합니다.

### 9단계: 인증·인가·소유권을 각각 검증하기

- **인증**: 로그인한 사용자인가?
- **역할 인가**: `USER`, `ADMIN`, `MANAGER` 중 이 기능을 실행할 역할인가?
- **소유권**: 해당 역할이라도 이 데이터의 작성자 또는 담당자인가?

URL의 `userId`나 요청 본문의 역할을 신뢰하지 않습니다. 현재 사용자는 세션과 `@CurrentUser`에서 얻고, 대상 데이터의 작성자 ID와 서버에서 비교합니다. 화면에서 버튼을 숨기는 것은 사용성일 뿐 보안 경계가 아니므로 API/Service에서도 반드시 거절해야 합니다.

최소 권한 테스트:

- 인증 없음 → 보호 기능 실패
- 로그인했지만 역할 부족 → 실패
- 역할은 맞지만 다른 사용자의 데이터 → 실패
- 올바른 역할과 소유자 → 성공

### 10단계: 테스트를 구현 순서와 함께 설계하기

테스트는 마지막 확인 작업이 아니라 구현 방향을 고정하는 계약입니다.

1. **도메인 단위 테스트**: 상태 전이와 계산을 빠르게 검증합니다.
2. **Service 단위 테스트**: 권한, 호출 순서보다 결과와 예외, 저장 여부를 검증합니다.
3. **Repository 테스트**: 메서드 이름 쿼리, `Specification`, 잠금, unique 제약처럼 DB 의미가 있는 부분을 검증합니다.
4. **Controller/MockMvc 테스트**: URL, JSON, Validation, HTTP status, 세션/CSRF를 검증합니다.
5. **통합 테스트**: 실제 Spring Context와 H2/MySQL에서 트랜잭션·매핑·전체 흐름을 검증합니다.

Given/When/Then을 구체적으로 씁니다.

```text
Given ADMIN과 존재하는 카테고리, 중복되지 않은 품목코드
When POST /api/items를 호출
Then 201과 생성된 품목 DTO를 받고 DB의 status는 ACTIVE다
```

“정상 동작한다” 같은 이름은 피하고 `USER가_품목을_등록하면_FORBIDDEN`, `중복_품목코드는_DUPLICATE_ITEM_CODE`처럼 실패 원인과 기대 결과가 드러나게 작성합니다.

### 11단계: 오류를 계층별로 추적하기

기능이 실패했을 때 코드를 무작정 바꾸지 말고 관찰 지점을 좁힙니다.

| 관찰 결과 | 먼저 볼 위치 |
|---|---|
| 404 또는 잘못된 method | Controller 매핑, URL |
| 400과 필드 오류 | Request DTO Validation, JSON 이름/타입 |
| 401/403 | 세션, Interceptor, CSRF, Service 권한 |
| 예상과 다른 ErrorCode | Service 검증 순서, ExceptionHandler |
| SQL/제약 오류 | Repository 쿼리, Entity 매핑, migration |
| 응답 시 LAZY 오류 | 트랜잭션 안 DTO 변환, 조회 전략 |
| 일부 데이터만 변경 | 트랜잭션 경계, 예외 삼킴 여부 |
| 동시 실행에서만 실패 | 잠금, unique 제약, 재시도/멱등성 |

실패 로그에는 “무슨 예외인가”뿐 아니라 입력 조건, 현재 상태, 트랜잭션 범위, 최종 DB 상태를 함께 기록합니다. 비밀번호나 세션 ID 같은 비밀정보는 기록하지 않습니다.

### 12단계: 완료 정의로 끝내기

기능은 컴파일만 되거나 정상 사례 하나가 성공했다고 완료되지 않습니다.

- [ ] FR ID와 구현 파일·테스트가 연결되어 있다.
- [ ] 정상 입력과 응답 status/DTO가 계약과 일치한다.
- [ ] Validation, 권한, 소유권, 잘못된 상태 실패가 검증된다.
- [ ] DB unique/FK와 애플리케이션 규칙이 서로 보완한다.
- [ ] 트랜잭션 실패 시 일부 데이터가 남지 않는다.
- [ ] Entity를 직접 외부 응답으로 노출하지 않는다.
- [ ] 목록 조회의 페이징·정렬과 N+1 가능성을 확인했다.
- [ ] 단위 테스트와 관련 통합 테스트가 통과한다.
- [ ] 실제 실행에서 요청·응답·SQL·로그를 확인했다.
- [ ] 설계 선택이 문서와 현재 참조 구현에 반영되어 있다.
- [ ] 다른 사람이 1분 안에 “왜 이렇게 구현했는지” 설명을 따라갈 수 있다.

### 기능별 구현 증거 기록표

1단계 구현 카드는 코딩 전 계약을 정하는 양식이고, 아래 표는 구현 후 증거만 기록합니다. 같은 설계 질문을 다시 쓰지 말고 실제 파일·명령·결과를 연결합니다.

| 완료 증거 | 내 기록 |
|---|---|
| 변경한 프로그램 파일 | ____ |
| 추가한 정상/실패 테스트 | ____ |
| 실행한 검증 명령과 결과 | ____ |
| 실제 HTTP 요청·응답 | ____ |
| 확인한 SQL·트랜잭션·로그 | ____ |
| 설계 결정 문서와 선택하지 않은 대안 | ____ |
| 남은 위험과 다음 작업 | ____ |

## 전체 기능 추적표

| FR ID | 기능 | 구현 핵심 | 관련 starter |
|---|---|---|---|
| FR-USER-001 | 로그인 | 이메일 조회, 비밀번호 해시 비교, 세션 생성 | `23-auth-login/*`, `20-security-session` |
| FR-USER-002 | 로그아웃 | 세션 무효화 | `23-auth-login/*` |
| FR-USER-003 | 내 정보 조회 | current user 조회, 응답 DTO | `23-auth-login/*`, `34-current-user-interceptor` |
| FR-USER-004 | 비밀번호 변경 | 현재 비밀번호 검증, 새 비밀번호 해시 | `23-auth-login/*` |
| FR-USER-005 | 권한 구분 | USER/ADMIN/MANAGER, 접근 제어 | `20-security-session`, `34-current-user-interceptor` |
| FR-PARTNER-001 | 거래처 등록 | 사업자번호 unique, 유형 enum | `24-partner-service`, `25-partner-controller` |
| FR-PARTNER-002 | 거래처 목록 조회 | Page 조회, 검색 조건 | `24-partner-service`, `25-partner-controller` |
| FR-PARTNER-003 | 거래처 상세 조회 | 단건 조회 | `24-partner-service`, `25-partner-controller` |
| FR-PARTNER-004 | 거래처 정보 수정 | 도메인 메서드 | `24-partner-service` |
| FR-PARTNER-005 | 거래처 삭제/비활성화 | soft delete, INACTIVE 상태 | `24-partner-service` |
| FR-PARTNER-006 | 거래처 검색 | 이름/사업자번호/유형 조건 | `24-partner-service` |
| FR-PARTNER-007 | 페이징 | Pageable, Page.map | `32-response-dto-mapping` |
| FR-CAT-001 | 카테고리 등록 | 카테고리명 중복 검증 | `02-category-entity`, `24-partner-service` (Category), `25-partner-controller` (Category) |
| FR-CAT-002 | 카테고리 목록 조회 | 전체 또는 페이징 목록 | `24-partner-service`, `25-partner-controller`, `31-repositories-all` |
| FR-CAT-003 | 카테고리 상세 조회 | 소속 품목 포함, N+1 주의 | `24-partner-service`, `25-partner-controller`, `31-repositories-all` |
| FR-CAT-004 | 카테고리 정보 수정 | PUT/PATCH 정책, unique 유지 | `24-partner-service`, `25-partner-controller` |
| FR-CAT-005 | 카테고리 삭제 | 소속 품목 존재 시 삭제 정책 | `24-partner-service`, `25-partner-controller` |
| FR-ITEM-001 | 품목 등록 | 품목코드 unique + 카테고리 FK | `08-item-register`, `17-dto-validation` |
| FR-ITEM-002 | 품목 목록 조회 | Page 조회, DTO 변환 | `09-item-search`, `18-controller-item` |
| FR-ITEM-003 | 품목 상세 조회 | 단건 조회, LAZY 관계 DTO 변환 | `28-item-detail-update` |
| FR-ITEM-004 | 품목 정보 수정 | 카테고리 존재 확인, 도메인 메서드 | `28-item-detail-update` |
| FR-ITEM-005 | 품목 단종 처리 | soft delete, DISCONTINUED 상태 | `28-item-detail-update` |
| FR-ITEM-006 | 품목 검색 | 이름/코드/카테고리 조건 | `07-repository`, `09-item-search` |
| FR-ITEM-007 | 페이징 | Pageable, Page.map | `09-item-search`, `32-response-dto-mapping` |
| FR-PO-001 | 발주서 작성 | 헤더 + 라인 원자적 저장 | `10-purchase-order-create` |
| FR-PO-002 | 발주 요청 | DRAFT → REQUESTED | `10-purchase-order-create`, `19-controller-purchase` |
| FR-PO-003 | 내 발주서 목록 | writerId 조건 | `29-purchase-my-cancel` |
| FR-PO-004 | 발주서 상세 조회 | 작성자/ADMIN/MANAGER 권한 분기 | `29-purchase-my-cancel` |
| FR-PO-005 | 발주서 취소 | 본인 + DRAFT/REQUESTED/APPROVED | `29-purchase-my-cancel` |
| FR-PO-006 | 발주 승인 | ADMIN/MANAGER, REQUESTED → APPROVED | `11-purchase-order-approval` |
| FR-PO-007 | 발주 반려 | ADMIN/MANAGER, 반려 사유 필수 | `11-purchase-order-approval` |
| FR-PO-008 | 입고 처리 | ADMIN/MANAGER, 상태 변경 + 재고 증가 원자성 | `11-purchase-order-approval` |
| FR-PO-009 | 관리자 발주 목록 | 상태/거래처 조건별 전체 조회 | `19-controller-purchase`, `29-purchase-my-cancel` |
| FR-NOTICE-001 | 공지 등록 | ADMIN 만 작성 | `12-notice-service`, `26-notice-controller` |
| FR-NOTICE-002 | 공지 목록 조회 | 중요 공지 우선 정렬 | `12-notice-service`, `26-notice-controller` |
| FR-NOTICE-003 | 공지 상세 조회 | 조회수 증가 정책 | `12-notice-service`, `26-notice-controller` |
| FR-NOTICE-004 | 공지 수정 | 작성 권한, 필수값 검증 | `12-notice-service`, `26-notice-controller` |
| FR-NOTICE-005 | 공지 삭제 | ADMIN 만 삭제 | `12-notice-service`, `26-notice-controller` |
| FR-NOTICE-006 | 중요 공지 표시 | important DESC 정렬 | `05-notice-entity`, `12-notice-service` |
| FR-SO-001 | 수주서 작성 | 헤더 + 라인 작성 | `13-sales-order-create` |
| FR-SO-002 | 수주 확정 | DRAFT → CONFIRMED | `13-sales-order-create`, `27-sales-controller` |
| FR-SO-003 | 내 수주서 목록 | writerId 조건 | `30-sales-lists` |
| FR-SO-004 | 처리 대기 수주서 | 매니저 처리 대기 | `30-sales-lists` |
| FR-SO-005 | 수주서 상세 조회 | 작성자/매니저/ADMIN 조회 | `30-sales-lists` |
| FR-SO-006 | 수주 출고 | manager 검증, CONFIRMED → SHIPPED | `14-sales-order-decision` |
| FR-SO-007 | 수주 완료 | SHIPPED → COMPLETED | `14-sales-order-decision` |
| FR-SO-008 | 수주 취소 | 사유 필수 | `14-sales-order-decision` |
| FR-SO-009 | 수주 상태 조회 | DRAFT/CONFIRMED/SHIPPED/COMPLETED/CANCELED | `06-sales-order-entity`, `30-sales-lists` |

---

# 1. 회원/인증 기능

## FR-USER-001 로그인

개념 빈칸:

- 인증(Authentication)은 사용자가 ____ 인지 확인하는 과정이다.
- 인가(Authorization)는 사용자가 특정 기능을 ____ 수 있는지 판단하는 과정이다.
- 비밀번호는 평문 비교가 아니라 `passwordEncoder.____(raw, encoded)` 로 검증한다.
- 로그인 실패 메시지를 이메일/비밀번호로 나누지 않는 이유는 ____ 공격을 줄이기 위해서다.

구현 TODO:

- Request DTO: `email`, `password` 에 각각 `@____`, `@____` 를 적용한다.
- Repository: `Optional<User> findBy____(String email)` 을 만든다.
- Service:
  - 이메일을 `trim().toLowerCase()` 로 ____ 한다.
  - 사용자를 찾지 못하면 `ErrorCode.____` 또는 로그인 실패 전용 코드를 던진다.
  - 비밀번호 불일치도 같은 메시지로 응답한다.
  - 성공 시 세션에 `USER_ID`, `USER_ROLE` 을 저장한다.
  - 세션 고정 공격 방지를 위해 `request.____()` 를 호출한다.
- Controller:
  - `POST /api/auth/____`
  - 성공 응답에는 비밀번호를 절대 포함하지 않는다.

테스트 TODO:

- Given 등록된 사용자, When 올바른 비밀번호로 로그인, Then 세션에 ____ 가 저장된다.
- Given 등록된 사용자, When 틀린 비밀번호로 로그인, Then HTTP ____ 와 공통 에러 응답을 받는다.

## FR-USER-002 로그아웃

개념 빈칸:

- 세션 로그아웃은 서버가 보관하던 인증 상태를 ____ 하는 것이다.
- JWT 로그아웃은 서버가 토큰 상태를 보관하지 않으면 즉시 강제 로그아웃이 ____.

구현 TODO:

- Service: `session.____()` 로 기존 세션을 폐기한다.
- Controller: `POST /api/auth/____`
- 응답: 본문 없이 `204 No Content` 또는 메시지 포함 `200 OK` 중 팀 컨벤션을 정한다.

테스트 TODO:

- Given 로그인된 세션, When 로그아웃, Then 이후 보호 API 호출은 HTTP ____ 이다.

## FR-USER-003 내 정보 조회

개념 빈칸:

- 내 정보 조회는 URL 에 userId 를 받기보다 현재 인증 사용자에서 ____ 를 꺼내는 편이 안전하다.
- 응답 DTO 에 password, internal token, salt 같은 값은 ____.

구현 TODO:

- 인증 사용자 주입:
  - 1차 세션 방식: `HttpSession.getAttribute("____")`
  - 개선 방식: `@____ Long currentUserId`
  - Spring Security 방식: `@____`
- Service: `userRepository.findById(currentUserId)` 로 조회한다.
- Response DTO: `userId`, `email`, `name`, `role` 정도만 포함한다.
- Controller: `GET /api/users/____`

테스트 TODO:

- Given 로그인하지 않은 사용자, When 내 정보 조회, Then HTTP ____.
- Given 로그인한 사용자, When 내 정보 조회, Then 응답에 ____ 필드가 없다.

## FR-USER-004 비밀번호 변경

개념 빈칸:

- 비밀번호 변경 전 현재 비밀번호를 다시 확인하는 이유는 ____ 된 세션 피해를 줄이기 위해서다.
- 새 비밀번호는 저장 전 반드시 ____ 해야 한다.

구현 TODO:

- Request DTO:
  - `currentPassword`: `@NotBlank`
  - `newPassword`: `@Size(min = ____, max = ____)`
- Service:
  - 현재 사용자 조회
  - `passwordEncoder.matches(currentPassword, user.getPassword())`
  - 새 비밀번호를 `passwordEncoder.____(...)`
  - `user.changePassword(encodedPassword)`
- Controller: `PATCH /api/users/me/password`

테스트 TODO:

- 현재 비밀번호가 틀리면 HTTP ____.
- 성공 후 기존 raw password 로는 `matches` 가 ____ 이어야 한다.

## FR-USER-005 권한 구분

개념 빈칸:

- USER, ADMIN, MANAGER 는 `UserRole` ____ 으로 정의한다.
- `@Enumerated(EnumType.____)` 를 사용하는 이유는 enum 순서 변경에 안전하기 때문이다.
- ADMIN 권한 검사는 Controller 에서 1차, ____ 에서 2차로 할 수 있다.

구현 TODO:

- `UserRole`: `USER`, `ADMIN`, `MANAGER`
- 세션 방식:
  - 로그인 시 `USER_ROLE` 저장
  - Interceptor 또는 Service 가 `role == UserRole.____` 검사
- Spring Security 방식:
  - `@PreAuthorize("hasRole('____')")`
  - `@PreAuthorize("hasAnyRole('MANAGER','____')")`

테스트 TODO:

- USER 가 품목 등록 API 를 호출하면 HTTP ____.
- MANAGER 가 출고 처리 API 를 호출하면 HTTP ____.

---

# 2. 거래처 관리 기능

## FR-PARTNER-001 거래처 등록

개념 빈칸:

- 사업자번호는 ____ 식별자이므로 DB unique 제약을 둔다.
- 거래처 유형은 ____ enum 으로 관리하면 잘못된 값을 막을 수 있다.

구현 TODO:

- Request DTO: `name`, `businessNumber`, `partnerType`, `contactName`, `phone`, `email`, `address`
- Repository:
  - `partnerRepository.existsBy____(businessNumber)`
- Service:
  - ADMIN 권한 확인
  - 사업자번호 중복 확인
  - `Partner.create(...)`
- Controller: `POST /api/____`
- Response: `partnerId`, `name`, `businessNumber`, `partnerType`, `status`

테스트 TODO:

- 중복 사업자번호는 HTTP ____ 와 `DUPLICATE_BUSINESS_NUMBER`.
- USER 권한 등록 시도는 HTTP ____.

## FR-PARTNER-002 거래처 목록 조회

개념 빈칸:

- 목록은 ____ 를 적용해 페이지 단위로 조회한다.

구현 TODO:

- Controller: `GET /api/partners?page=0&size=20&type=SUPPLIER`
- Service: 조건 분기 (`partnerType` 필터)
- Response DTO 변환

테스트 TODO:

- type=SUPPLIER 조회 시 CUSTOMER 거래처는 ____.

## FR-PARTNER-003 거래처 상세 조회

개념 빈칸:

- 상세 조회는 PathVariable 로 ____ 를 받는다.
- 거래처 상세 응답에 발주/수주 통계까지 포함할지는 ____ 와 응답 크기의 트레이드오프다.

구현 TODO:

- Repository: `findById(partnerId)`
- Service:
  - 거래처 없으면 `ErrorCode.____`
  - Service 안에서 DTO 변환
- Controller: `GET /api/partners/{____}`

테스트 TODO:

- 없는 거래처 ID 조회 시 HTTP ____.
- 응답에 사업자번호와 유형이 포함된다.

## FR-PARTNER-004 거래처 정보 수정

개념 빈칸:

- 수정 시 Service 가 setter 를 직접 호출하기보다 Entity 의 ____ 메서드를 쓰면 규칙이 모인다.
- 사업자번호 변경 요청이 들어왔다면 ____ 검증을 다시 해야 한다.

구현 TODO:

- Request DTO: 등록 계약을 재사용하되 `PUT`이면 전체 필드, `PATCH`이면 변경 허용 필드만 받는 정책을 ____ 에 기록한다.
- Service:
  - 거래처 조회
  - 사업자번호가 바뀌었다면 중복 재검사
  - `partner.update(...)`
- Controller: `PUT /api/partners/{partnerId}`

테스트 TODO:

- 다른 거래처와 같은 사업자번호로 변경하면 HTTP ____.
- 유형(SUPPLIER → BOTH) 변경 후 발주 가능한지 확인.

## FR-PARTNER-005 거래처 삭제 / 비활성화

개념 빈칸:

- 발주/수주 이력이 있는 거래처를 hard delete 하면 ____ 가 깨질 수 있다.
- 그래서 보통은 ____ 처리(soft delete)로 둔다.

구현 TODO:

- Repository: `purchaseOrderRepository.countByPartner_Id(partnerId)`, `salesOrderRepository.countByPartner_Id(partnerId)`
- Service:
  - 이력이 0 이면 delete
  - 이력이 있으면 `partner.deactivate()` 로 상태 변경
- Controller: `DELETE /api/partners/{partnerId}` 또는 `PATCH /api/partners/{partnerId}/deactivate`

테스트 TODO:

- 발주 이력이 있는 거래처 삭제 시 응답 상태는 ____, 거래처 status 는 ____ 가 된다.
- 이력이 없는 거래처는 실제 DELETE 된다.

## FR-PARTNER-006 거래처 검색

개념 빈칸:

- 검색 조건이 이름/사업자번호/유형으로 늘어나면 단순 메서드 이름 쿼리가 ____ 질 수 있다.

구현 TODO:

- Controller: `GET /api/partners?keyword=best&type=SUPPLIER`
- Repository:
  - 단순 버전: `findByNameContainingOrBusinessNumberContaining(...)`
  - 확장 버전: `@Query` 또는 Specification/Querydsl
- Service:
  - keyword blank 면 전체
  - keyword trim

테스트 TODO:

- 이름 일부로 검색하면 해당 거래처만 나온다.

## FR-PARTNER-007 페이징

개념 빈칸:

- Spring Data 의 `Page` 는 content 뿐 아니라 totalElements, totalPages 같은 ____ 정보를 가진다.
- `Page.map` 을 쓰면 페이징 메타 정보가 ____ 된다.

구현 TODO:

- Controller 파라미터: `Pageable pageable`
- 기본값: `@PageableDefault(size = ____, sort = "id")`
- 정렬 허용 컬럼 정책: ____

테스트 TODO:

- `page=1, size=10` 요청 시 두 번째 페이지가 조회된다.
- 허용하지 않는 sort 컬럼은 ____.

---

# 3. 카테고리 / 품목 관리 기능

## FR-CAT-001 카테고리 등록

개념 빈칸:

- 카테고리명은 사용자 식별성이 높으므로 DB 에 ____ 제약을 둔다.

구현 TODO:

- Request DTO: `name`, `description`
- Service:
  - ADMIN 권한 확인
  - `categoryRepository.existsBy____(name)`
  - `Category.create(name, description)`
- Controller: `POST /api/____`

테스트 TODO:

- 중복 카테고리명은 `DUPLICATE_CATEGORY_NAME` 으로 응답한다.

## FR-CAT-002 카테고리 목록 조회

개념 빈칸:

- 카테고리는 품목보다 수가 적어 MVP 에서는 ____ 조회로 충분하다.
- 그러나 카테고리가 100개를 넘어가면 ____ 적용을 고려한다.

구현 TODO:

- Controller: `GET /api/categories`
- Service: `categoryRepository.findAll(Sort.by("name").____())`
- Response: `categoryId`, `name`, `description`, `itemCount`(선택)

테스트 TODO:

- 로그인 사용자는 USER/ADMIN/MANAGER 모두 조회 가능하다.

## FR-CAT-003 카테고리 상세 조회

개념 빈칸:

- 카테고리 상세에 소속 품목까지 포함하면 N+1 문제를 피하기 위해 ____ 또는 EntityGraph 를 사용할 수 있다.
- 또는 소속 품목을 ____ API 로 분리해 페이징을 적용한다.

구현 TODO:

- Repository:
  - `categoryRepository.findById(categoryId)`
  - 소속 품목: `itemRepository.findByCategory_Id(categoryId, pageable)`
- Response: 카테고리 정보 + 소속 품목 요약 DTO
- Controller: `GET /api/categories/{categoryId}`

테스트 TODO:

- 없는 카테고리 ID 는 HTTP ____.
- 상세 응답에 item summary 목록이 포함된다.

## FR-CAT-004 카테고리 정보 수정

개념 빈칸:

- 전체 갱신은 ____, 부분 갱신은 ____ HTTP 메서드가 자연스럽다.
- 이름 변경 시에도 중복 검증은 ____.

구현 TODO:

- Controller: `PUT /api/categories/{categoryId}` 또는 `PATCH /api/categories/{categoryId}`
- Service:
  - ADMIN 권한 확인
  - 카테고리 조회
  - 새 이름이 기존과 다르면 중복 확인
  - `category.update(name, description)`

테스트 TODO:

- 다른 카테고리가 이미 쓰는 이름으로 변경하면 HTTP ____.

## FR-CAT-005 카테고리 삭제

개념 빈칸:

- 소속 품목이 있는 카테고리를 삭제하면 품목의 FK 가 ____ 될 수 있다.

구현 TODO:

- Repository: `itemRepository.countBy____(categoryId)`
- Service: count > 0 이면 `CATEGORY_HAS_ITEMS`

테스트 TODO:

- 품목이 있는 카테고리 삭제 시 HTTP ____.

## FR-ITEM-001 품목 등록

개념 빈칸:

- 품목코드는 ____ 식별자이므로 DB unique 와 Service 중복 검사를 함께 둔다.
- 품목은 반드시 하나의 ____ 에 속해야 한다.
- 단가는 `____` 타입으로 저장해 부동소수점 오차를 막는다.

구현 TODO:

- Request DTO: `itemCode`, `name`, `categoryId`, `unit`, `unitPrice`, `safetyStock`
- Repository:
  - `itemRepository.existsBy____(itemCode)`
  - `categoryRepository.findById(categoryId)`
- Service:
  - ADMIN 권한 확인
  - 품목코드 중복 확인
  - 카테고리 존재 확인
  - `Item.create(category, itemCode, name, unit, unitPrice, safetyStock)`
- Controller: `POST /api/____`
- Response: `itemId`, `itemCode`, `name`, `categoryName`, `unitPrice`, `status`

테스트 TODO:

- 중복 품목코드는 HTTP ____ 와 `DUPLICATE_ITEM_CODE`.
- 존재하지 않는 카테고리는 HTTP ____ 와 `CATEGORY_NOT_FOUND`.

## FR-ITEM-002 품목 목록 조회

개념 빈칸:

- 목록 조회는 데이터가 많을 수 있으므로 ____ 를 적용한다.
- Entity 목록을 그대로 응답하지 않고 `Page<ItemResponse>` 로 ____ 한다.

구현 TODO:

- Controller: `GET /api/items?page=0&size=20&sort=itemCode,asc`
- Service: `itemRepository.findAll(pageable)` → `page.____(ItemResponse::from)`
- Response DTO 에는 BigDecimal unitPrice 가 포함된다 (JSON 직렬화 시 문자열로 가야 안전한지 검토).

테스트 TODO:

- size=2 로 조회하면 응답 content 크기는 최대 ____ 이다.
- 로그인하지 않은 사용자 조회는 HTTP ____.

## FR-ITEM-003 품목 상세 조회

개념 빈칸:

- 상세 조회는 PathVariable 로 ____ 를 받는다.
- LAZY 로 묶인 Category 는 DTO 변환 시점에 접근되므로 트랜잭션이 살아 있어야 한다. 또는 ____ 으로 함께 로드한다.

구현 TODO:

- Repository: `findById(itemId)` 또는 상세 전용 fetch join.
- Service:
  - 품목 없으면 `ErrorCode.____`
  - Service 안에서 DTO 변환
- Controller: `GET /api/items/{____}`

테스트 TODO:

- 없는 품목 ID 조회 시 HTTP ____.
- 응답에 카테고리명과 단가가 포함된다.

## FR-ITEM-004 품목 정보 수정

개념 빈칸:

- 수정 시 Service 가 setter 를 직접 부르기보다 Entity 의 ____ 메서드를 쓴다.
- 카테고리 변경은 새 categoryId 가 실제 존재하는지 먼저 ____ 한다.

구현 TODO:

- Request DTO: `name`, `categoryId`, `unit`, `unitPrice`, `safetyStock`
- Service:
  - 품목 조회
  - categoryId 가 있으면 카테고리 조회 + `item.changeCategory(category)`
  - `item.updateProfile(...)`
- Controller: `PUT /api/items/{itemId}`

테스트 TODO:

- 존재하지 않는 카테고리로 변경하면 HTTP ____.
- 수정 후 상세 조회에서 변경값이 ____ 된다.

## FR-ITEM-005 품목 단종 처리

개념 빈칸:

- 단종은 hard delete 대신 ____ 로 처리해 과거 발주/수주 라인을 보존한다.

구현 TODO:

- Entity: `ItemStatus.DISCONTINUED`
- 도메인 메서드: `item.____()`
- Controller: `PATCH /api/items/{itemId}/discontinue`

테스트 TODO:

- 단종 후 신규 발주 라인에 포함하면 HTTP ____.

---

## FR-ITEM-006 품목 검색

개념 빈칸:

- 검색 조건이 이름/코드/카테고리로 늘어나면 단순 메서드 이름 쿼리가 ____ 질 수 있다.
- 검색 결과도 목록이므로 ____ 를 유지한다.

구현 TODO:

- Controller: `GET /api/items?keyword=usb&categoryId=3`
- Repository:
  - 단순 버전: `findByNameContainingOrItemCodeContaining(...)`
  - 확장 전략은 [FR-PARTNER-006 거래처 검색](#fr-partner-006-거래처-검색)의 `@Query` 또는 Specification/Querydsl 선택 기준을 재사용
- Service:
  - keyword 가 blank 면 전체 조회
  - keyword 를 trim 한다.

테스트 TODO:

- 이름 일부로 검색하면 해당 품목만 나온다.
- 카테고리 조건과 keyword 조건을 함께 주면 두 조건을 ____ 한다.

## FR-ITEM-007 페이징

공통 `Page` 개념, `Pageable` 파라미터, 기본값, 허용하지 않는 sort 검증은 [FR-PARTNER-007 페이징](#fr-partner-007-페이징)을 단일 기준으로 사용합니다. 여기에는 품목에만 다른 계약을 적습니다.

구현 TODO:

- 허용 sort 컬럼: `id`, `itemCode`, `name`, ____
- 검색 조건과 `Pageable`을 같은 Repository 호출에 전달하는 방법: ____
- `Page<Item>`을 `PageResponse<ItemSummary>`로 변환하는 위치: ____

테스트 TODO:

- keyword·categoryId 필터 후에도 `totalElements`와 `totalPages`가 필터 결과를 기준으로 계산된다.
- 품목에서 허용하지 않는 sort 컬럼은 공통 오류 계약으로 거절된다.

---

# 4. 발주(구매) 기능

## FR-PO-001 발주서 작성

개념 빈칸:

- 발주서는 ____ + ____ 의 헤더-라인 구조다.
- 새 발주의 초기 상태는 `PurchaseOrderStatus.____`.
- 헤더와 라인은 ____ 트랜잭션으로 묶어야 한다.
- 거래처 유형이 SUPPLIER 또는 ____ 인지 검증한다.

구현 TODO:

- Request DTO: `partnerId`, `orderDate`, `dueDate`, `lines: List<LineDto>`
  - LineDto: `itemId`, `quantity`, `unitPrice`
- Service:
  - 현재 사용자 ID 확인 (`writerId`)
  - 거래처 조회 + 유형 검증
  - 라인 검증:
    - 라인 ≥ 1 (`EMPTY_ORDER_LINES`)
    - 각 라인 품목 존재 + 상태 ACTIVE
    - 수량 > 0
  - 라인별 `lineAmount = quantity × unitPrice`
  - 헤더 `totalAmount = sum(lineAmount)`
  - 발주번호 채번
  - 참조 구현은 `PurchaseOrder`에 다른 aggregate의 `partnerId`, `writerId` 저장
  - 참조 구현은 라인에 `itemId`를 저장하고 헤더-라인만 연관관계/cascade 사용
  - starter의 `@ManyToOne` 대안과 차이는 [DESIGN_DECISIONS.md](./DESIGN_DECISIONS.md)에 따라 비교
- Controller: `POST /api/purchase-orders`

테스트 TODO:

- 라인이 0개면 HTTP ____.
- 단종 품목 라인은 HTTP ____.
- CUSTOMER 전용 거래처는 HTTP ____.
- 작성 성공 시 status 는 ____.

## FR-PO-002 발주 요청 (submit)

개념 빈칸:

- DRAFT 와 REQUESTED 를 분리하면 ____ 저장 기능을 자연스럽게 표현할 수 있다.
- 발주 요청은 `DRAFT → ____` 전이다.

구현 TODO:

- Service:
  - 작성자 본인 확인
  - 현재 상태가 DRAFT 인지 확인
  - 라인 비어있지 않은지 재확인
  - `po.submit()`
- Controller: `PATCH /api/purchase-orders/{poId}/submit`

테스트 TODO:

- DRAFT 발주는 submit 성공.
- 이미 REQUESTED 발주를 다시 submit 하면 HTTP ____.

## FR-PO-003 내 발주서 목록

개념 빈칸:

- IDOR 공격을 막으려면 URL 의 writerId 보다 인증 사용자 기반 ____ 조건을 쓴다.

구현 TODO:

- Repository: `findByWriterId(currentUserId, pageable)`
- Service: current user 의 발주만 조회
- Controller: `GET /api/purchase-orders/____`

테스트 TODO:

- A 사용자가 B 사용자의 발주를 목록에서 볼 수 ____.

## FR-PO-004 발주서 상세 조회

개념 빈칸:

- 상세 조회는 본인 또는 ADMIN/MANAGER만 가능하도록 ____ 검증을 한다.
- 응답에 헤더와 라인을 함께 담을 때 nested DTO 구조를 쓰면 ____ 한 번에 끝낼 수 있다.

구현 TODO:

- Repository: `findById(poId)` 또는 라인까지 fetch join.
- Service:
  - 발주 없으면 `PURCHASE_ORDER_NOT_FOUND`
  - 본인 여부(`writerId == currentUserId`) 또는 ADMIN/MANAGER 여부 확인
- Controller: `GET /api/purchase-orders/{poId}`

테스트 TODO:

- 작성자가 아닌 USER 는 HTTP ____.
- ADMIN/MANAGER는 다른 사용자의 발주도 조회 가능하다.
- 응답에 lines 배열이 헤더와 함께 포함된다.

## FR-PO-005 발주서 취소

개념 빈칸:

- 취소는 본인의 ____, ____, ____ 상태 발주만 허용한다.
- 취소를 별도 상태로 둘 경우 enum 에 ____ 를 추가한다.

구현 TODO:

- Entity: `po.cancelByOwner(currentUserId)`
- Service:
  - 본인 여부 확인 (도메인 메서드 내부에서)
  - 상태 검증 (`DRAFT`, `REQUESTED`, `APPROVED`)
- Controller: `PATCH /api/purchase-orders/{poId}/cancel`

테스트 TODO:

- APPROVED 발주를 본인이 취소하면 상태가 ____가 된다.
- RECEIVED 발주를 본인이 취소 시도하면 HTTP ____.
- 타인의 DRAFT 발주 취소 시도는 HTTP ____.

## FR-PO-006 ~ 007 발주 승인/반려

개념 빈칸:

- 승인/반려는 상태 전이이므로 같은 요청을 두 번 보내도 데이터가 이상해지지 않도록 ____ 을 검증한다.
- 동시에 두 관리자가 승인하면 ____ 락 또는 ____ 락을 고려한다.
- 반려 사유는 빈 문자열이 아니어야 하므로 `@____` 를 적용한다.

구현 TODO:

- Service:
  - ADMIN 또는 MANAGER 권한 확인
  - 발주 조회
  - `po.approve(approverUserId)` 또는 `po.reject(approverUserId, reason)`
- Controller:
  - `PATCH /api/admin/purchase-orders/{poId}/approve`
  - `PATCH /api/admin/purchase-orders/{poId}/reject`

테스트 TODO:

- REQUESTED 발주 승인 성공.
- 이미 승인된 발주 재승인은 HTTP ____.
- 반려 사유 blank 는 HTTP ____.

## FR-PO-008 입고 처리

개념 빈칸:

- 입고는 `APPROVED → ____` 전이다.
- 입고 시 향후 ____ 반영(증가)이 자연스럽다.

구현 TODO:

- Service:
  - ADMIN 또는 MANAGER 권한 확인
  - 발주 조회
  - 상태 APPROVED 검증
  - `po.receive()`, `receivedAt = now()`
  - 같은 트랜잭션에서 라인별 재고 +quantity
  - 같은 품목의 최초 재고 생성 경합을 막기 위한 잠금 순서 결정
- Controller: `PATCH /api/admin/purchase-orders/{poId}/receive`

테스트 TODO:

- APPROVED 가 아닌 발주 입고 시도는 HTTP ____.

## FR-PO-009 관리자 발주 목록

개념 빈칸:

- 관리자 발주 목록은 개인의 `/my` 와 달리 전체 발주를 ____ 조건으로 조회한다.

구현 TODO:

- Controller: `GET /api/admin/purchase-orders`
- Query param: `status`, `partnerId`, `from`, `to`
- 권한: `@PreAuthorize("hasAnyRole('____', '____')")`
- Service:
  - status + partnerId 조합 분기
  - 조건이 없으면 `findAll(pageable)`

테스트 TODO:

- USER 가 `/api/admin/purchase-orders` 호출하면 HTTP ____.

---

# 5. 공지사항 기능

## FR-NOTICE-001 공지 등록

개념 빈칸:

- 공지 등록은 ____ 권한만 가능하다.
- 제목과 내용은 `@____` 로 필수 검증한다.

구현 TODO:

- Request DTO: `title`, `content`, `important`
- Service:
  - ADMIN 권한 확인
  - writerId 저장
  - `viewCount = ____`
- Controller: `POST /api/notices`

테스트 TODO:

- USER 가 공지 등록하면 HTTP ____.

## FR-NOTICE-002 공지 목록 조회

개념 빈칸:

- 공지 목록은 로그인 사용자 모두가 볼 수 있지만, 등록/수정/삭제는 ____ 만 가능하다.
- 중요 공지 우선 정렬은 `important ____ , createdAt ____`.

구현 TODO:

- Repository: `findAll(pageable)` with Sort
- Service: 기본 정렬을 important desc + createdAt desc 로 구성
- Controller: `GET /api/notices`
- Response: 목록 DTO 는 긴 `content` 대신 ____ 를 둘 수 있다.

테스트 TODO:

- important=true 공지가 일반 공지보다 먼저 나온다.

## FR-NOTICE-003 공지 상세 조회

개념 빈칸:

- GET 상세 조회에서 조회수를 증가시키면 검색 봇/미리보기 요청으로 조회수가 ____ 수 있다.
- 원자적 조회수 증가는 `UPDATE notice SET view_count = view_count + ____` 형태가 안전하다.

구현 TODO:

- Service:
  - 공지 조회
  - 조회수 증가 정책 선택: 상세 조회 안에서 증가 또는 별도 `PATCH /view`
  - 상세 DTO 반환
- Repository:
  - `@Modifying @Query` 로 조회수 증가
- Controller: `GET /api/notices/{noticeId}`

테스트 TODO:

- 상세 조회 후 viewCount 가 1 증가한다.

## FR-NOTICE-004 공지 수정

개념 빈칸:

- 수정 권한은 작성자 여부보다 PRD 기준 ____ 권한이 핵심이다.

구현 TODO:

- Request DTO: 등록 DTO의 `title`, `content`, `important` 계약을 재사용
- Service:
  - ADMIN 권한 확인
  - 공지 조회
  - `notice.update(...)`
- Controller: `PUT /api/notices/{noticeId}`

테스트 TODO:

- USER 수정 시도는 HTTP ____.
- 제목 blank 는 HTTP ____.

## FR-NOTICE-005 공지 삭제

개념 빈칸:

- 공지 삭제는 실제 삭제 또는 `deleted` 플래그를 두는 ____ delete 중 선택할 수 있다.

구현 TODO:

- Service:
  - ADMIN 권한 확인
  - 공지 조회
  - delete 또는 soft delete
- Controller: `DELETE /api/notices/{noticeId}`

테스트 TODO:

- 삭제 후 목록에 노출되지 않는다.

## FR-NOTICE-006 중요 공지 표시

개념 빈칸:

- `important` 는 Boolean 이지만 정렬에서는 true 를 먼저 두기 위해 ____ 정렬을 사용한다.

구현 TODO:

- Entity: `Boolean important`
- Repository/Service sort:
  - `Sort.by(Sort.Direction.____, "important").and(Sort.by(Sort.Direction.____, "createdAt"))`
- 화면: 중요 공지는 badge 또는 상단 고정 영역으로 표시할 수 있다.

테스트 TODO:

- important=false 최신글보다 important=true 이전글이 먼저 나오는지 확인한다.

---

# 6. 수주(판매) 기능

## FR-SO-001 수주서 작성

개념 빈칸:

- 수주서도 발주서와 같은 ____-____ 구조다.
- 거래처 유형이 CUSTOMER 또는 ____ 인지 검증한다.
- 새 수주의 초기 상태는 `SalesOrderStatus.____`.

구현 TODO:

- Request DTO: `partnerId`, `orderDate`, `shipDate`, `lines: List<LineDto>`
- Service:
  - 현재 사용자 조회
  - 거래처 조회 + 유형 검증
  - 라인 검증 (≥ 1, 품목 존재, 수량 > 0)
  - 총금액 계산
  - 수주번호 채번
  - `SalesOrder.create(...)`
- Controller: `POST /api/sales-orders`

테스트 TODO:

- 라인 0개는 HTTP ____.
- SUPPLIER 전용 거래처는 HTTP ____.

## FR-SO-002 수주 확정

개념 빈칸:

- 확정은 `DRAFT → ____` 전이다.

구현 TODO:

- Service:
  - 작성자 본인 확인
  - 상태 DRAFT 검증
  - 라인 재확인
  - `so.confirm()`
- Controller: `PATCH /api/sales-orders/{soId}/confirm`

## FR-SO-003 내 수주서 목록

개념 빈칸:

- "내 수주 목록"은 내가 ____ 한 수주서 목록이다.
- 처리자로서의 목록은 별도 `pending` API 로 분리한다.

구현 TODO:

- Repository: `findByWriterId(currentUserId, pageable)`
- Service: current user 를 writerId 로 사용
- Controller: `GET /api/sales-orders/____`

테스트 TODO:

- A 작성 수주서 목록에 B 작성 수주서는 보이지 않는다.

## FR-SO-005 수주서 상세 조회

개념 빈칸:

- 상세 조회는 작성자, 처리자(매니저) 또는 ____ 만 허용한다.
- ADMIN 전체 조회 허용은 Service 에서 role 기반으로 ____ 해야 한다.

구현 TODO:

- Service:
  - 수주서 조회
  - currentUserId 가 writerId 또는 managerId 인지 확인
  - currentRole 이 ADMIN 이면 전체 조회 허용
  - 아니면 `ErrorCode.____`
- Controller: `GET /api/sales-orders/{soId}`

테스트 TODO:

- 작성자도 처리자도 아니고 ADMIN 도 아닌 사용자 접근은 HTTP ____.
- 상세 응답에 lines 배열이 포함된다.

## FR-SO-006 ~ 008 수주 출고/완료/취소

개념 빈칸:

- 상태 전이:
  - `CONFIRMED → ____` (출고)
  - `____ → COMPLETED` (완료)
  - `DRAFT/CONFIRMED/SHIPPED → ____` (취소)
- 매니저 권한은 ____ 또는 ADMIN.
- 취소 사유는 ____.

구현 TODO:

- Service:
  - 권한 확인
  - 상태 검증
  - 도메인 메서드 호출: `so.ship()`, `so.complete()`, `so.cancel(reason)`
- Controller:
  - `PATCH /api/sales-orders/{soId}/ship`
  - `PATCH /api/sales-orders/{soId}/complete`
  - `PATCH /api/sales-orders/{soId}/cancel`

테스트 TODO:

- CONFIRMED 가 아닌 상태에서 ship 호출은 HTTP ____.
- 취소 사유 blank 는 HTTP ____.
- 완료된 수주를 취소하면 HTTP ____.

## FR-SO-009 수주 상태 조회

개념 빈칸:

- 상태값은 `DRAFT`, `CONFIRMED`, `SHIPPED`, `COMPLETED`, `CANCELED` 를 ____ 으로 관리한다.
- 목록에서 status 조건을 받으면 통계/관리 화면에서 ____ 필터가 가능하다.

구현 TODO:

- Query param: `status=CONFIRMED`
- Repository: `findByStatus(status, pageable)` 또는 관리자 조건별 메서드
- Response: 상태 enum 문자열 + 상태 변경 일시(`confirmedAt`, `shippedAt`, `completedAt`) 포함

테스트 TODO:

- status=CONFIRMED 요청 시 출고된 수주는 제외된다.
- 종료 상태(`COMPLETED`/`CANCELED`)에서 추가 전이를 시도하면 HTTP ____.

## FR-SO-004 처리 대기 수주서

개념 빈칸:

- "처리 대기"는 보통 `____` 상태의 수주서를 의미한다.

구현 TODO:

- Repository: `findByStatus(SalesOrderStatus.CONFIRMED, pageable)`
- 권한: `@PreAuthorize("hasAnyRole('MANAGER','ADMIN')")`
- Controller: `GET /api/sales-orders/pending`

---

# 7. 기능별 공통 테스트 체크리스트

각 기능을 구현할 때 아래 체크리스트를 최소 1개 이상 채우세요.

| 분류 | 질문 | 내 답 |
|---|---|---|
| 성공 | 정상 요청의 HTTP status 는? | ____ |
| 검증 | 필수값 누락 시 어떤 ErrorCode 인가? | ____ |
| 권한 | USER/ADMIN/MANAGER 중 누가 가능한가? | ____ |
| 소유자 | 본인 데이터만 봐야 하는가? | ____ |
| 상태 | 허용되는 상태 전이는? | ____ |
| 트랜잭션 | 실패 시 함께 rollback 되어야 하는 데이터는? | ____ |
| DTO | 응답에서 숨겨야 할 필드는? | ____ |
| 동시성 | 중복 승인/중복 등록 같은 race 가 있는가? | ____ |
| 도메인 규칙 | 거래처 유형/품목 상태 검증을 어디서 하는가? | ____ |
| 헤더-라인 | 라인 검증을 헤더 저장 전/후 중 어디서 하는가? | ____ |

공통 매트릭스를 작성한 뒤에는 같은 질문을 다시 반복하지 않고 [12단계: 완료 정의로 끝내기](#12단계-완료-정의로-끝내기)의 체크리스트로 누락 여부를 최종 확인합니다.

---

# 8. macOS 단일 호스트 배포·운영 워크북

목표 흐름:

```text
폴더 준비 → Homebrew → Java 21 + Maven → JAR 빌드·직접 실행
→ launchd LaunchAgent 등록 → 파일 로그 확인 → DB 연결
→ 업데이트·롤백 → 최종 점검
```

이 절은 macOS 로그인 사용자 한 명이 학습·포트폴리오용 SCM 서버를 운영하는 시나리오입니다. `LaunchAgent`는 **그 사용자가 로그인한 뒤** 실행됩니다. 로그인 전부터 항상 떠 있어야 하는 서버라면 시스템 관리자 권한과 별도 보안 설계가 필요한 `LaunchDaemon` 영역이므로 이 절의 범위를 벗어납니다.

> 빌드 도구 구분: Java 21은 Java 17 대상으로 빌드된 현재 JAR를 실행할 수 있습니다. Maven은 요청한 개발 도구 체인을 익히기 위해 설치·검증하지만, **이 저장소에는 `pom.xml`이 없고 Gradle Wrapper가 정식 빌드 도구**입니다. 따라서 이 저장소에서 `mvn package`를 실행하지 말고 `./gradlew bootJar`를 사용합니다. Maven 프로젝트라면 `mvn -B clean verify package`가 대응 명령입니다.

## 8.1 폴더 준비

소스 저장소와 배포 산출물을 분리합니다. 아래 `$HOME/Library/Application Support/scm-system`은 운영 호스트의 예시 경로이며 Git에 추가하지 않습니다. 새 터미널을 열 때마다 먼저 공통 변수를 다시 선언합니다.

```bash
export REPO_HOME="$HOME/Documents/scm-system"
export SCM_HOME="$HOME/Library/Application Support/scm-system"
export AGENT_PLIST="$HOME/Library/LaunchAgents/com.example.scm.plist"
export USER_DOMAIN="gui/$(id -u)"

mkdir -p "$SCM_HOME"/{backups,bin,config,logs,releases}
mkdir -p "$HOME/Library/LaunchAgents"
chmod 700 "$SCM_HOME" "$SCM_HOME/backups" "$SCM_HOME/config"

git clone <REPOSITORY_URL> "$REPO_HOME" # 최초 1회만 실행
cd "$REPO_HOME"
git rev-parse --show-toplevel
git status --short
```

폴더 역할:

| 경로 | 역할 | 보존 정책 |
|---|---|---|
| `~/Documents/scm-system` | 소스, 테스트, 빌드 입력 | Git으로 관리 |
| `$SCM_HOME/releases/<버전>/` | 버전별 실행 JAR | 최근 정상 버전 최소 1개 유지 |
| `$SCM_HOME/current` | 현재 릴리스 심볼릭 링크 | 업데이트 시 원자적으로 교체 |
| `$SCM_HOME/config/scm.env` | DB 등 비밀 환경변수 | `chmod 600`, Git 금지 |
| `$SCM_HOME/bin/run-scm.sh` | LaunchAgent 실행 래퍼 | 절대 경로 사용 |
| `$SCM_HOME/logs/` | 표준 출력·오류 로그 | 용량/보존 기간 관리 |
| `$SCM_HOME/backups/` | 업데이트 전 DB dump와 checksum | 권한 제한, 복구 시험 후 보존 |

체크:

- [ ] 소스와 실행 JAR가 같은 폴더에 뒤섞이지 않았다.
- [ ] 공백이 있는 경로를 항상 큰따옴표로 감쌌다.
- [ ] DB 비밀번호 파일이 Git 추적 대상이 아니다.

## 8.2 Homebrew 설치와 확인

먼저 Command Line Tools와 기존 Homebrew를 확인합니다.

```bash
xcode-select -p || xcode-select --install
command -v brew || true
```

Homebrew가 없다면 `https://brew.sh/`의 현재 공식 설치 명령을 직접 확인한 뒤 실행합니다. 설치 프로그램이 마지막에 출력하는 `brew shellenv` 명령을 `~/.zprofile`에 반영해야 새 터미널에서도 `brew`를 찾을 수 있습니다. 기본 prefix는 Apple Silicon이 `/opt/homebrew`, Intel Mac이 `/usr/local`이므로 경로를 하드코딩하기보다 아래처럼 확인합니다.

```bash
brew --version
brew --prefix
brew doctor
```

## 8.3 Java 21 + Maven 준비

```bash
brew update
brew install openjdk@21 maven

export JAVA_HOME="$(/usr/libexec/java_home -v 21)"
export PATH="$JAVA_HOME/bin:$PATH"

java -version
javac -version
mvn -version
./gradlew --version
```

`/usr/libexec/java_home -v 21`이 JDK를 찾지 못하면 Homebrew formula 안내에 나온 JDK 심볼릭 링크 명령을 확인합니다. 현재 formula는 다음 형태를 안내하지만, 실제 `$HOMEBREW_PREFIX`는 `brew --prefix`로 확인합니다.

```bash
sudo ln -sfn "$(brew --prefix)/opt/openjdk@21/libexec/openjdk.jdk" \
  /Library/Java/JavaVirtualMachines/openjdk-21.jdk
```

셸을 다시 열어도 Java 21을 사용하려면 `~/.zprofile`에 `JAVA_HOME`과 `PATH` 설정을 추가합니다. LaunchAgent는 대화형 셸 설정을 자동으로 읽는다고 가정하지 않으므로, 뒤의 실행 래퍼에서도 `JAVA_HOME`을 명시합니다.

확인 기준:

- [ ] `java -version`과 `javac -version`의 major가 21이다.
- [ ] `mvn -version`의 Java home도 JDK 21을 가리킨다.
- [ ] `./gradlew --version`의 JVM도 JDK 21이다.
- [ ] 이 저장소의 `build.gradle`은 source/target 17이며, 이는 실행 JDK 21과 모순되지 않음을 설명할 수 있다.

## 8.4 JAR 빌드와 직접 실행

배포 전에 테스트와 저장소 경계 검증을 통과시킵니다.

```bash
cd "$REPO_HOME"
./scripts/verify-repository-boundary.sh
./gradlew clean portfolioCheck

ls -lh build/libs/*.jar
export JAR_PATH="$(find build/libs -maxdepth 1 -type f -name '*.jar' -print -quit)"
test -n "$JAR_PATH"
env -u SPRING_PROFILES_ACTIVE java -jar "$JAR_PATH"
```

다른 터미널에서 직접 실행 상태를 확인합니다.

```bash
curl -sS -o /dev/null -w '%{http_code}\n' http://127.0.0.1:8080/login
lsof -nP -iTCP:8080 -sTCP:LISTEN
```

### 8.4.1 릴리스 스테이징 공통 절차

`Ctrl-C`로 정상 종료한 뒤 JAR를 버전별 릴리스 폴더로 복사합니다. 릴리스 ID에는 생성 시각과 commit SHA를 함께 넣어 소스와 산출물을 다시 연결할 수 있게 합니다. 이 블록은 최초 설치와 8.9 업데이트에서 함께 사용하는 단일 스테이징 절차입니다.

```bash
export RELEASE_ID="$(date +%Y%m%d-%H%M%S)-$(git rev-parse --short HEAD)"
mkdir -p "$SCM_HOME/releases/$RELEASE_ID"
cp "$JAR_PATH" \
  "$SCM_HOME/releases/$RELEASE_ID/scm-system.jar"
(cd "$SCM_HOME/releases/$RELEASE_ID" && \
  shasum -a 256 scm-system.jar > scm-system.jar.sha256)
```

### 8.4.2 최초 릴리스 활성화

최초 설치에서만 아래처럼 `current` 링크를 만듭니다. 업데이트에서는 기존 `current`를 `previous`로 보존한 뒤 전환해야 하므로 8.9 절을 따릅니다.

```bash
ln -sfn "$SCM_HOME/releases/$RELEASE_ID" "$SCM_HOME/current.next"
mv -h "$SCM_HOME/current.next" "$SCM_HOME/current"
readlink "$SCM_HOME/current"
test -f "$SCM_HOME/current/scm-system.jar"
```

`current`가 특정 릴리스를 가리키게 하면 plist를 매번 수정하지 않고도 업데이트·롤백할 수 있습니다.

## 8.5 LaunchAgent 실행 파일과 설정 준비

먼저 `$SCM_HOME/config/scm.env`를 만듭니다. 최초 LaunchAgent 검증은 요청 순서에 맞춰 외부 DB 없이 H2로 실행합니다. 값은 `zsh`가 읽을 수 있는 `KEY=value` 형식으로 작성하고, 공백이나 셸 특수문자는 안전하게 인용합니다. 이 파일은 실행 코드이므로 소유자 외에는 읽거나 수정할 수 없게 합니다.

```bash
SPRING_PROFILES_ACTIVE=
SCM_SEED_ENABLED=true
SERVER_PORT=8080
HIBERNATE_SQL_LOG_LEVEL=warn
```

빈 `SPRING_PROFILES_ACTIVE`는 로그인 셸에 남아 있을 수 있는 `mysql` 값을 덮어써 최초 검증이 H2로 실행되게 합니다. MySQL 전환은 8.8에서 연결과 schema 계약을 확인한 뒤 수행합니다.

```bash
chmod 600 "$SCM_HOME/config/scm.env"
```

다음 내용으로 `$SCM_HOME/bin/run-scm.sh`를 준비합니다. 문서의 `__USER__`는 `whoami` 결과로 바꾸며 plist와 스크립트에는 `$HOME`이나 `~` 대신 절대 경로를 사용합니다.

```bash
#!/bin/zsh
set -eu
set -a
source "/Users/__USER__/Library/Application Support/scm-system/config/scm.env"
set +a

export JAVA_HOME="$(/usr/libexec/java_home -v 21)"
exec "$JAVA_HOME/bin/java" \
  -Duser.timezone=Asia/Seoul \
  -jar "/Users/__USER__/Library/Application Support/scm-system/current/scm-system.jar"
```

```bash
chmod 700 "$SCM_HOME/bin/run-scm.sh"
zsh -n "$SCM_HOME/bin/run-scm.sh"
```

다음 plist를 `$HOME/Library/LaunchAgents/com.example.scm.plist`에 준비합니다.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN"
  "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
  <key>Label</key>
  <string>com.example.scm</string>
  <key>ProgramArguments</key>
  <array>
    <string>/Users/__USER__/Library/Application Support/scm-system/bin/run-scm.sh</string>
  </array>
  <key>WorkingDirectory</key>
  <string>/Users/__USER__/Library/Application Support/scm-system/current</string>
  <key>RunAtLoad</key>
  <true/>
  <key>KeepAlive</key>
  <dict>
    <key>SuccessfulExit</key>
    <false/>
  </dict>
  <key>ProcessType</key>
  <string>Background</string>
  <key>StandardOutPath</key>
  <string>/Users/__USER__/Library/Application Support/scm-system/logs/scm.out.log</string>
  <key>StandardErrorPath</key>
  <string>/Users/__USER__/Library/Application Support/scm-system/logs/scm.err.log</string>
</dict>
</plist>
```

```bash
chmod 600 "$AGENT_PLIST"
plutil -lint "$AGENT_PLIST"
```

`KeepAlive.SuccessfulExit=false`는 비정상 종료 때 재시작하되 정상 종료를 무한 재시작하지 않게 합니다. 반복 실패하면 로그가 빠르게 커질 수 있으므로 먼저 직접 실행에 성공한 JAR만 등록합니다.

## 8.6 launchd LaunchAgent 등록

```bash
plutil -lint "$AGENT_PLIST"
if launchctl print "$USER_DOMAIN/com.example.scm" >/dev/null 2>&1; then
  launchctl bootout "$USER_DOMAIN/com.example.scm"
fi
launchctl enable "$USER_DOMAIN/com.example.scm"
launchctl bootstrap "$USER_DOMAIN" "$AGENT_PLIST"
launchctl print "$USER_DOMAIN/com.example.scm"

for attempt in {1..30}; do
  curl -fsS -o /dev/null http://127.0.0.1:8080/login && break
  sleep 1
done
curl -fsS -o /dev/null http://127.0.0.1:8080/login
```

`RunAtLoad=true`이므로 `bootstrap` 뒤에 프로세스가 시작됩니다. 수정한 plist가 반영되지 않으면 `bootout → bootstrap` 순서로 다시 등록합니다. 설정 파일이나 JAR만 바꾼 경우에는 `kickstart -k`로 재시작할 수 있지만, 현재 프로세스를 종료하므로 요청을 받는 사용자가 없는지 먼저 확인합니다.

체크:

- [ ] plist의 모든 경로가 실제 절대 경로다.
- [ ] `plutil -lint`가 `OK`다.
- [ ] `launchctl print`에 label, program, last exit status가 보인다.
- [ ] 로그인 사용자 세션에서 실행된다는 LaunchAgent의 한계를 이해한다.

## 8.7 파일 로그 확인

```bash
tail -n 100 "$SCM_HOME/logs/scm.out.log"
tail -n 100 "$SCM_HOME/logs/scm.err.log"
tail -F "$SCM_HOME/logs/scm.out.log" "$SCM_HOME/logs/scm.err.log"

grep -En "Started ScmApplication|ERROR|Exception|Caused by" \
  "$SCM_HOME/logs/scm.out.log" "$SCM_HOME/logs/scm.err.log"
du -h "$SCM_HOME/logs/"*.log
```

확인할 순서:

1. Java 경로 또는 권한 오류가 `scm.err.log`에 있는지 확인합니다.
2. `Started ScmApplication`과 실제 시작 시간을 확인합니다.
3. 포트 충돌(`Address already in use`) 여부를 확인합니다.
4. DB 연결, Flyway migration, Hibernate validate 오류를 확인합니다.
5. 비밀번호·세션 ID 같은 비밀값이 로그에 출력되지 않는지 확인합니다.

launchd의 stdout/stderr 파일은 자동 회전 정책이 아닙니다. 학습 호스트에서도 파일 크기를 주기적으로 확인하고, 운영 환경에서는 별도 로그 회전·수집 정책을 설계합니다.

## 8.8 DB 연결

앞 단계까지는 재시작 시 데이터가 사라지는 H2 in-memory로 LaunchAgent 자체를 검증했습니다. 이제 MySQL 서버를 준비하고 연결 시험을 통과한 뒤 `mysql` 프로필로 전환합니다. DB가 준비되기 전에 프로필부터 바꾸면 `KeepAlive`가 실패한 앱을 반복 재시작할 수 있습니다.

학습용 DB는 저장소의 Compose에서 `db` 서비스만 실행할 수 있습니다. Docker Desktop이 필요하며 `scm/scm`은 로컬 데모 전용 계정이므로 외부에 노출되는 운영 환경에서 사용하지 않습니다.

```bash
cd "$REPO_HOME"
docker compose config --quiet
docker compose up -d --wait db
docker compose ps db
docker compose exec db mysql -uscm -p -e \
  "SELECT VERSION(), CURRENT_USER(), DATABASE();" scm
```

외부 MySQL 8.0을 사용한다면 DBA가 DB와 최소 권한 계정을 먼저 만들고, 호스트에서 MySQL 8.0 client를 준비해 연결합니다. 비밀번호는 명령 인자에 쓰지 말고 `-p` 프롬프트로 입력합니다.

```bash
brew install mysql-client@8.0
export PATH="$(brew --prefix mysql-client@8.0)/bin:$PATH"

mysql -h <DB_HOST> -P <DB_PORT> -u <DB_USER> -p -e \
  "SELECT VERSION(), CURRENT_USER(), DATABASE();" <DB_NAME>
```

연결이 성공하면 `$SCM_HOME/config/scm.env`를 아래처럼 교체합니다. `<...>` 값은 실제 접속 정보로 바꾸고 유효한 `zsh` 문자열로 인용합니다. Compose의 `db`만 사용한다면 host는 `127.0.0.1`, user/password는 데모 설정인 `scm`/`scm`입니다.

```bash
SPRING_PROFILES_ACTIVE=mysql
DB_HOST=127.0.0.1
DB_PORT=3306
DB_NAME=scm
DB_USER=scm
DB_PASSWORD='<DB_PASSWORD>'
SCM_SEED_ENABLED=false
SERVER_PORT=8080
HIBERNATE_SQL_LOG_LEVEL=warn
```

애플리케이션을 전환하기 전에 같은 환경변수로 MySQL 전용 schema 계약 테스트를 실행합니다. `source`하는 파일은 신뢰하는 소유자만 수정할 수 있어야 합니다.

```bash
chmod 600 "$SCM_HOME/config/scm.env"
set -a
source "$SCM_HOME/config/scm.env"
set +a

cd "$REPO_HOME"
./gradlew mysqlSchemaTest

launchctl kickstart -k "$USER_DOMAIN/com.example.scm"
for attempt in {1..30}; do
  curl -fsS -o /dev/null http://127.0.0.1:8080/login && break
  sleep 1
done
curl -fsS -o /dev/null http://127.0.0.1:8080/login
grep -En "mysql|Flyway|Started ScmApplication|ERROR|Exception" \
  "$SCM_HOME/logs/scm.out.log" "$SCM_HOME/logs/scm.err.log"
```

설정 연결:

| 환경변수 | Spring 설정 | 의미 |
|---|---|---|
| `SPRING_PROFILES_ACTIVE=mysql` | `application-mysql.yml` 활성화 | MySQL/Flyway 사용 |
| `DB_HOST`, `DB_PORT` | JDBC URL | DB 주소와 포트 |
| `DB_NAME` | JDBC URL | 스키마 이름 |
| `DB_USER`, `DB_PASSWORD` | datasource 인증 | 최소 권한 계정 사용 |
| `SCM_SEED_ENABLED=false` | 시드 설정 | 운영형 기본 계정 생성 방지 |

MySQL 프로필에서는 Flyway가 `src/main/resources/db/migration/mysql`의 버전 SQL을 적용하고 Hibernate는 `ddl-auto=validate`로 매핑을 검증합니다. 애플리케이션 계정에는 필요한 DML과 승인된 migration 권한만 부여하고 root 계정을 넣지 않습니다. 조직에서는 migration 계정과 런타임 DML 계정을 분리하는 방안도 검토합니다. 비밀번호는 plist나 저장소가 아니라 권한이 제한된 `scm.env`에 둡니다.

DB 실패를 구분하는 질문:

- `Connection refused`인가, 인증 실패인가, DB 이름 오류인가? ____
- Flyway checksum/version 충돌인가, Hibernate schema validation 실패인가? ____
- H2로 잘못 기동되어 데이터가 사라진 것처럼 보이는가? ____

## 8.9 업데이트와 롤백

업데이트는 **DB 백업·복구 가능성 확인 → 배포 commit 고정 → 빌드·테스트 → 새 릴리스 생성 → 링크 전환 → 재시작 → smoke test** 순서로 진행합니다. 실행 중인 `current/scm-system.jar`를 덮어쓰지 않습니다.

먼저 DB dump를 만들고 빈 파일이 아닌지와 checksum을 확인합니다. `<BACKUP_USER>`는 백업에 필요한 읽기 권한을 가진 계정입니다. 비밀번호는 `-p` 프롬프트로 입력합니다.

```bash
set -a
source "$SCM_HOME/config/scm.env"
set +a
export PATH="$(brew --prefix mysql-client@8.0)/bin:$PATH"

export BACKUP_FILE="$SCM_HOME/backups/scm-$(date +%Y%m%d-%H%M%S).sql"
mysqldump -h "$DB_HOST" -P "$DB_PORT" -u <BACKUP_USER> -p \
  --single-transaction --routines --triggers "$DB_NAME" > "$BACKUP_FILE"
test -s "$BACKUP_FILE"
chmod 600 "$BACKUP_FILE"
shasum -a 256 "$BACKUP_FILE"
```

Compose 데모 DB라면 컨테이너 안의 `mysqldump`를 사용할 수도 있습니다.

```bash
cd "$REPO_HOME"
export BACKUP_FILE="$SCM_HOME/backups/scm-$(date +%Y%m%d-%H%M%S).sql"
docker compose exec -T db sh -c \
  'exec mysqldump -uscm -p"$MYSQL_PASSWORD" --single-transaction scm' \
  > "$BACKUP_FILE"
test -s "$BACKUP_FILE"
chmod 600 "$BACKUP_FILE"
shasum -a 256 "$BACKUP_FILE"
```

백업은 생성 성공만으로 충분하지 않습니다. 운영 DB가 아닌 격리된 복구용 DB에서 아래 형태로 import하고 핵심 테이블의 행 수와 애플리케이션 기동을 확인합니다.

```bash
mysql -h <RESTORE_HOST> -P <RESTORE_PORT> -u <RESTORE_USER> -p \
  <RESTORE_DB> < "$BACKUP_FILE"
```

그다음 배포할 tag 또는 commit을 정확히 고정합니다. 배포 호스트의 작업 트리가 더러우면 임의로 stash/reset하지 말고 중단합니다.

```bash
cd "$REPO_HOME"
if test -n "$(git status --porcelain)"; then
  echo "배포 중단: 작업 트리에 미커밋 변경이 있습니다." >&2
  exit 1
fi

git fetch --all --tags --prune
export DEPLOY_REF="<TAG_OR_COMMIT>"
git switch --detach "$DEPLOY_REF"
export DEPLOY_COMMIT="$(git rev-parse HEAD)"
git show -s --format='%H %cI %s' "$DEPLOY_COMMIT"

./scripts/verify-repository-boundary.sh
./gradlew clean portfolioCheck

export JAR_PATH="$(find build/libs -maxdepth 1 -type f -name '*.jar' -print -quit)"
test -n "$JAR_PATH"
```

이어서 [8.4.1 릴리스 스테이징 공통 절차](#841-릴리스-스테이징-공통-절차)의 명령 블록을 같은 셸에서 실행합니다. `RELEASE_ID`와 새 JAR checksum이 만들어진 것을 확인한 뒤에만 아래 링크 전환을 진행합니다.

```bash
export PREVIOUS_RELEASE="$(readlink "$SCM_HOME/current")"
test -f "$PREVIOUS_RELEASE/scm-system.jar"
ln -sfn "$PREVIOUS_RELEASE" "$SCM_HOME/previous.next"
mv -h "$SCM_HOME/previous.next" "$SCM_HOME/previous"

ln -sfn "$SCM_HOME/releases/$RELEASE_ID" "$SCM_HOME/current.next"
mv -h "$SCM_HOME/current.next" "$SCM_HOME/current"
launchctl kickstart -k "$USER_DOMAIN/com.example.scm"
```

업데이트 후 `/login`, 로그인, 핵심 조회, DB 쓰기 한 건을 smoke test하고 로그와 Flyway 상태를 확인합니다. 실패하면 자동으로 계속 진행하지 말고 `previous`가 가리키는 직전 정상 릴리스로 링크를 되돌립니다.

```bash
curl -fsS -o /dev/null http://127.0.0.1:8080/login
grep -En "Started ScmApplication|ERROR|Exception|Flyway" \
  "$SCM_HOME/logs/scm.out.log" "$SCM_HOME/logs/scm.err.log"

export ROLLBACK_RELEASE="$(readlink "$SCM_HOME/previous")"
test -f "$ROLLBACK_RELEASE/scm-system.jar"
ln -sfn "$ROLLBACK_RELEASE" "$SCM_HOME/current.next"
mv -h "$SCM_HOME/current.next" "$SCM_HOME/current"
launchctl kickstart -k "$USER_DOMAIN/com.example.scm"
curl -fsS -o /dev/null http://127.0.0.1:8080/login
```

중요한 한계:

- JAR 롤백과 DB 롤백은 같은 일이 아닙니다. 이미 적용한 Flyway migration이 구버전 JAR와 호환되지 않으면 JAR만 되돌려도 복구되지 않습니다.
- 운영 migration은 가능하면 이전·새 애플리케이션이 모두 동작하는 **하위 호환 확장 → 데이터 이행 → 나중에 제거** 순서로 설계합니다.
- 파괴적 DDL 전에 DB 백업과 격리 환경 복구 리허설을 수행합니다. Flyway 이력 행이나 checksum을 임의 수정하지 않습니다.
- 애플리케이션 기동이 migration을 적용한 뒤 실패할 수도 있습니다. 배포 전에 하위 호환성을 검증하고, 실패 시 어떤 DB 복구가 필요한지 별도로 판단합니다.
- 배포 호스트에서는 exact tag/commit만 빌드하고 임의 코드를 수정하지 않습니다.

## 8.10 최종 점검

```bash
launchctl print "$USER_DOMAIN/com.example.scm"
readlink "$SCM_HOME/current"
if test -f "$SCM_HOME/current/scm-system.jar.sha256"; then
  (cd "$SCM_HOME/current" && shasum -a 256 -c scm-system.jar.sha256)
else
  shasum -a 256 "$SCM_HOME/current/scm-system.jar"
fi

export APP_PID="$(launchctl print "$USER_DOMAIN/com.example.scm" | \
  awk '/pid =/{print $3; exit}')"
test -n "$APP_PID"
export JAVA_HOME="$(/usr/libexec/java_home -v 21)"
"$JAVA_HOME/bin/jcmd" "$APP_PID" VM.version

curl -sS -o /dev/null -w 'login=%{http_code}\n' \
  http://127.0.0.1:8080/login
lsof -nP -iTCP:8080 -sTCP:LISTEN

tail -n 50 "$SCM_HOME/logs/scm.out.log"
tail -n 50 "$SCM_HOME/logs/scm.err.log"
```

최종 체크리스트:

- [ ] `current`가 의도한 commit의 릴리스를 가리킨다.
- [ ] 현재 JAR checksum이 배포 시 기록한 값과 일치한다.
- [ ] LaunchAgent 상태와 last exit status가 정상이다.
- [ ] LaunchAgent PID의 JVM이 Java 21이며 8080 포트가 한 프로세스만 사용한다.
- [ ] `/login`이 정상 HTTP 응답을 반환한다.
- [ ] MySQL 프로필, DB 이름, 사용자, Flyway 상태가 의도와 일치한다.
- [ ] 로그인, 목록 조회, 발주 핵심 흐름의 smoke test가 성공한다.
- [ ] stdout/stderr 로그에 반복 예외와 비밀값 노출이 없다.
- [ ] 직전 정상 릴리스와 DB 백업 위치를 알고 실제 롤백 명령을 설명할 수 있다.
- [ ] 격리된 DB에서 최근 백업의 복구 시험을 완료했다.
- [ ] 재로그인 또는 재부팅 이후 LaunchAgent 동작 조건을 확인했다.

운영 실습 기록:

| 항목 | 내 기록 |
|---|---|
| 배포 commit / release ID | ____ |
| JAR SHA-256 (`shasum -a 256`) | ____ |
| 실행 Java / 빌드 도구 버전 | ____ |
| 활성 profile / DB endpoint | ____ |
| smoke test 결과 | ____ |
| 직전 정상 release | ____ |
| DB backup / 복구 확인 | ____ |
| 발견한 장애와 해결 근거 | ____ |

공식 참고 자료:

- https://docs.brew.sh/Installation
- https://formulae.brew.sh/formula/openjdk@21
- https://formulae.brew.sh/formula/maven
- https://formulae.brew.sh/formula/mysql-client@8.0
- https://docs.spring.io/spring-boot/3.5/system-requirements.html
- https://developer.apple.com/library/archive/documentation/MacOSX/Conceptual/BPSystemStartup/Chapters/CreatingLaunchdJobs.html
- https://docs.docker.com/reference/cli/docker/compose/up/
- https://dev.mysql.com/doc/refman/8.0/en/using-mysqldump.html
