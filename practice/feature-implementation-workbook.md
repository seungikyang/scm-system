# 기능 구현과 4지 선다 워크북

이 문서는 `scm_system_PRD_TRD.md` 의 모든 기능 요구사항을 구현 단위로 다시 쪼개는 학습용 워크북입니다.

기존 `practice/problems.md` 와 `practice/starter/` 가 코드 조각 중심이라면, 이 문서는
요구사항을 구현 카드로 바꾼 뒤 **계약 → Entity/DTO → Repository → Service → Controller
→ 테스트** 순서로 작은 기능 하나를 완성하게 합니다. 완성한 뒤에는 실제 요청이 흐르는
**화면/API → Controller → Service → Repository → Entity/DB** 순서로 다시 추적합니다.
구현 순서와 실행 순서를 섞지 않는 것이 이 워크북의 핵심입니다.

## 사용 규칙

1. [권장 학습 트랙](./README.md#학습-트랙)에서 현재 진행할 모듈을 하나 고른다.
2. 해당 기능의 `개념 4지 선다`과 입력·출력·권한·실패 계약을 먼저 확인한다.
3. 계층·업무 규칙의 4지 선다에서 답을 고른 뒤 정답·해설을 확인한다.
4. Given/When/Then으로 제시한 4지 선다에서 기대 결과를 선택한다.
5. 막히면 PRD/TRD 절 번호와 `practice/starter` 파일을 다시 본다.
6. 공통 계층·검색·페이징 계약은 한 절만 기준으로 삼고, 기능별 절에서는 차이점과 검증 근거를 문항으로 확인한다.

핵심 구현(사용자·마스터·품목·발주·입고 재고)은 현재 테스트와 [DESIGN_DECISIONS.md](./DESIGN_DECISIONS.md)를 기준으로 비교합니다. 공지·수주도 현재 `src/`에 참조 구현이 있습니다. 확장 과제에서는 요구사항, 상태 표, 실패 테스트를 먼저 작성하고 `SalesAndNoticeIntegrationTest`와 비교합니다.

## 공통 구현 지도

아래 객관식 문제를 먼저 풀면 모든 기능의 구현 방향이 흔들리지 않습니다.

| 계층 | 책임 | 경계: 넣지 말아야 할 것 | 4지 선다 |
|---|---|---|---|
| Controller | HTTP 요청/응답, 인증 사용자 주입, DTO 검증 | 중복 조회, 상태 전이, 직접 SQL/Repository 호출 | [W270 선택지·정답·해설](workbook-questions.md#w270) |
| Service | 비즈니스 규칙, 트랜잭션, 권한/소유자 검증 | Servlet 응답 조작, 화면 HTML 생성 | [W051 선택지·정답·해설](workbook-questions.md#w051) |
| Repository | DB 조회/저장, 조건 검색, 페이징 | HTTP status와 사용자 메시지 결정 | [W136 선택지·정답·해설](workbook-questions.md#w136) |
| Entity | 식별자, 연관관계, 상태값, 도메인 메서드 | Controller DTO 의존, 세션 접근 | [W271 선택지·정답·해설](workbook-questions.md#w271) |
| DTO | 요청/응답 계약, Validation | 영속성 생명주기와 비즈니스 상태 변경 | [W137 선택지·정답·해설](workbook-questions.md#w137) |
| Exception | 공통 에러 응답 | 기능마다 제각각인 오류 형식 | [W069 선택지·정답·해설](workbook-questions.md#w069) |
| Test | 성공/실패/권한/상태 전이 검증 | 구현 세부사항에만 묶인 검증 | [W051 선택지·정답·해설](workbook-questions.md#w051) |

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
| `PasswordConfig` | 외부 라이브러리 객체를 `@Bean`으로 등록 | [W226 선택지·정답·해설](workbook-questions.md#w226) |
| `WebMvcConfig` | MVC 확장 지점에 Interceptor와 ArgumentResolver 연결 | [W227 선택지·정답·해설](workbook-questions.md#w227) |
| `ItemService` | 생성자 주입으로 Repository 등 협력 객체를 받음 | [W228 선택지·정답·해설](workbook-questions.md#w228) |
| `ItemRepository` | 런타임 프록시가 Repository 구현을 제공 | [W229 선택지·정답·해설](workbook-questions.md#w229) |

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


### W223. Spring 프록시 방식에서 new로 직접 만든 Service의 특징은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=223)

1. 자동 트랜잭션 프록시를 거치지 않는다
2. 자동으로 ApplicationContext에 등록된다
3. 모든 final 필드가 자동 주입된다
4. JPA가 HTTP 세션을 생성한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 자동 트랜잭션 프록시를 거치지 않는다

컨테이너 관리 객체의 프록시 호출과 직접 생성한 객체의 호출을 구분합니다.

</details>



### W224. 기본 프록시 방식의 같은 객체 내부 Transactional 호출은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=224)

1. 항상 별도 트랜잭션을 생성한다
2. 자동으로 다른 서버에 위임된다
3. 프록시를 우회해 내부 메서드의 새 트랜잭션 설정이 적용되지 않을 수 있다
4. 모든 checked exception을 자동 제거한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 프록시를 우회해 내부 메서드의 새 트랜잭션 설정이 적용되지 않을 수 있다

외부에서 프록시를 통한 호출 구조와 테스트로 경계를 확인합니다.

</details>



### W225. 세 번째 입고 라인에서 RuntimeException이 발생하면 기대 결과는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=225)

1. 앞선 두 재고만 증가하고 상태는 RECEIVED로 남는다
2. 예외가 발생한 라인만 자동 삭제된다
3. 모든 과거 발주까지 삭제된다
4. 발주 상태와 앞선 재고 변경까지 함께 롤백된다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 발주 상태와 앞선 재고 변경까지 함께 롤백된다

입고 상태·재고 반영을 같은 트랜잭션으로 묶어 부분 반영을 막습니다.

</details>



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

4지 선다에서 확인한 규칙은 실행 가능한 참조 구현에서 다시 확인할 수 있습니다. 프로그램 구현은 **요구사항을 관찰 가능한 계약으로 바꾸고, 계약을 지키는 최소 수직 흐름을 만든 뒤, 실패 사례로 경계를 검증하는 작업**입니다. 아래 순서를 기능마다 반복합니다.

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

| 항목 | 확인할 계약 | 품목 등록 예시 | 4지 선다 |
|---|---|---|---|
| 행위자 | 권한 범위 | `ADMIN` | [W079 선택지](workbook-questions.md#w079) |
| 진입점 | HTTP method와 URL | `POST /api/items` | [W077 선택지](workbook-questions.md#w077) |
| 입력 | 필수값·형식·범위 | 코드·이름·카테고리·단위·단가·안전재고 | [W074 선택지](workbook-questions.md#w074) |
| 출력 | status와 응답 DTO | `201 Created`, `ItemDetailView` | [W389 선택지](workbook-questions.md#w389) |
| 비즈니스 규칙 | 입력 형식 외 업무 검증 | 품목코드 중복 금지, 카테고리 존재 | [W040 선택지](workbook-questions.md#w040) |
| 상태 변화 | 초기 엔티티 상태 | 새 품목은 `ACTIVE` | [W377 선택지](workbook-questions.md#w377) |
| 트랜잭션 | 성공·실패를 함께 처리할 범위 | 검증 후 품목 저장 | [W039 선택지](workbook-questions.md#w039) |
| 실패 계약 | ErrorCode와 HTTP status | 권한 없음, 중복 코드, 카테고리 없음 | [W070 선택지](workbook-questions.md#w070) |
| 증거 | 완료를 증명하는 테스트 | 성공·검증·권한·중복·참조 무결성 테스트 | [W401 선택지](workbook-questions.md#w401) |

요구사항이 모호하면 코딩으로 숨기지 말고 [DESIGN_DECISIONS.md](./DESIGN_DECISIONS.md)에 선택과 이유를 기록합니다. 예를 들어 “삭제”가 실제 행 삭제인지 `INACTIVE`/`DISCONTINUED` 전환인지 먼저 정해야 Repository와 API가 흔들리지 않습니다.

구현 카드 4지 선다 확인:

### W079. 관리자 전용 품목 변경을 보호하는 방법은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=79)

1. URL에 admin을 쓰면 자동으로 보호된다
2. HTML 버튼을 숨기기만 한다
3. 인증된 사용자의 ADMIN 역할을 서버 가드에서 검사한다
4. 품목 코드에 ADMIN을 붙인다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 인증된 사용자의 ADMIN 역할을 서버 가드에서 검사한다

경로 이름과 UI만으로는 권한을 보장할 수 없습니다.

</details>


### W074. NotBlank·Positive·Size·Pattern의 역할 조합은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=74)

1. 공백 금지·양수·길이/개수·정규식 검증
2. DB 생성·로그인·정렬·출고 처리
3. 모두 같은 문자열 길이 검사
4. 모두 권한 검사

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 공백 금지·양수·길이/개수·정규식 검증

각 어노테이션은 적용 타입과 검사하는 입력 조건이 다릅니다.

</details>


### W161. DTO·Entity·Service·DB 검증 책임의 조합은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=161)

1. 화면 글꼴·CSS·이미지·북마크
2. 모두 HTTP 쿠키 생성
3. 모두 같은 역할 문자열 비교만
4. 입력 형식·불변식·업무 흐름·최종 무결성

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 입력 형식·불변식·업무 흐름·최종 무결성

검증은 서로 다른 진입 경로·업무 경계·경쟁 상태를 방어합니다.

</details>


### W068. HTTP status와 비즈니스 ErrorCode의 관계는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=68)

1. 둘 다 화면 글꼴을 지정한다
2. ErrorCode는 TCP 포트다
3. status가 400이면 모든 오류 원인이 같다
4. status는 HTTP 결과 분류, code는 구체적인 업무 오류를 나타낸다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · status는 HTTP 결과 분류, code는 구체적인 업무 오류를 나타낸다

동일한 HTTP 상태 안에도 중복·상태 오류 등 다른 업무 코드가 있습니다.

</details>


### W039. 여러 저장 작업의 트랜잭션 경계는 어디에 두는가?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=39)

1. CSS 선택자에 둔다
2. HTML 버튼 이름에 둔다
3. 업무 작업을 묶는 Service의 프록시 호출 메서드에 둔다
4. 각 SQL 로그 줄에 둔다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 업무 작업을 묶는 Service의 프록시 호출 메서드에 둔다

Service 경계에서 여러 Repository 작업이 함께 성공·실패하도록 묶습니다.

</details>


### W401. 검증 단계가 제공해야 하는 근거는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=401)

1. 성공이라고 적은 문장만
2. 아직 실행하지 않은 테스트 이름만
3. 라이브러리 버전 문자열만
4. 적절한 테스트의 실행 명령과 실제 결과

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 적절한 테스트의 실행 명령과 실제 결과

실행한 검사와 결과를 기록합니다. 환경상 실행하지 못한 검증은 성공으로 기록하지 않습니다.

</details>


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


### W158. 기본 요청 처리 의존 방향은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=158)

1. Domain → Controller → Session
2. Repository → HTML → Controller
3. CSS → PasswordEncoder → SQL
4. Controller → Service → Repository, Service → Domain

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · Controller → Service → Repository, Service → Domain

업무 도메인을 HTTP 표현과 세션으로부터 독립시키는 방향입니다.

</details>



### W159. Entity가 피해야 할 의존의 예는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=159)

1. Controller·HttpSession·응답 DTO
2. 업무 상태 enum
3. BigDecimal
4. LocalDate

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · Controller·HttpSession·응답 DTO

도메인 타입 사용과 외부 표현·인프라 책임의 결합을 구분합니다.

</details>



### W158. 기본 요청 처리 의존 방향은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=158)

1. Domain → Controller → Session
2. Repository → HTML → Controller
3. CSS → PasswordEncoder → SQL
4. Controller → Service → Repository, Service → Domain

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · Controller → Service → Repository, Service → Domain

업무 도메인을 HTTP 표현과 세션으로부터 독립시키는 방향입니다.

</details>



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


### W051. REQUESTED가 아닌 발주의 승인·반려를 막는 설계는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=51)

1. 도메인 전이 메서드에 상태 가드를 둔다
2. 화면 버튼만 숨긴다
3. 승인 후 작성자 이메일을 바꾼다
4. DTO 필드 이름을 바꾸면 충분하다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 도메인 전이 메서드에 상태 가드를 둔다

Service 인가와 별도로 도메인 메서드가 상태 불변식을 보호합니다.

</details>



### W161. DTO·Entity·Service·DB 검증 책임의 조합은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=161)

1. 화면 글꼴·CSS·이미지·북마크
2. 모두 HTTP 쿠키 생성
3. 모두 같은 역할 문자열 비교만
4. 입력 형식·불변식·업무 흐름·최종 무결성

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 입력 형식·불변식·업무 흐름·최종 무결성

검증은 서로 다른 진입 경로·업무 경계·경쟁 상태를 방어합니다.

</details>



### W070. 도메인별 오류를 enum으로 모으는 장점은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=70)

1. 예외가 절대 발생하지 않는다
2. 코드·상태·기본 메시지의 일관성을 유지한다
3. 모든 오류를 200으로 바꾼다
4. DB unique 검사가 없어져도 된다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 코드·상태·기본 메시지의 일관성을 유지한다

Service와 예외 처리기가 같은 오류 계약을 공유합니다.

</details>



### W087. 발주 통합 테스트의 정상 흐름은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=87)

1. 로그인 → 작성 → 입고 → 요청
2. 비로그인 → 완료 → 생성
3. 승인 → 반려 → 재승인 → 삭제
4. 로그인 → 작성 → 요청 → 승인 → 입고

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 로그인 → 작성 → 요청 → 승인 → 입고

이전 응답의 ID와 각 역할의 세션·CSRF 조건을 이어 사용합니다.

</details>



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


### W172. 인증(Authentication)이 확인하는 것은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=172)

1. 업무 기능 실행 권한만
2. 사용자가 주장한 신원과 인증 증거의 일치
3. 주문의 현재 재고만
4. CSS 적용 여부

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 사용자가 주장한 신원과 인증 증거의 일치

인증은 신원을 확인하고 인가는 기능·데이터 접근 권한을 판단합니다.

</details>



### W173. 인가(Authorization)가 판단하는 것은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=173)

1. JDK 설치 여부
2. 문자열의 UTF-8 바이트 수
3. 인증된 사용자가 기능·데이터에 접근할 권한
4. 비밀번호 자체의 평문 값

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 인증된 사용자가 기능·데이터에 접근할 권한

로그인 성공만으로 모든 데이터에 접근할 수 있는 것은 아닙니다.

</details>



### W124. 본인 발주 목록의 소유권 기준은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=124)

1. 요청 쿼리 writerId를 인증 없이 신뢰한다
2. 클라이언트 이름만 비교한다
3. 현재 인증 사용자의 ID로 작성자 조건을 적용한다
4. 모든 사용자에게 전체 목록을 반환한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 현재 인증 사용자의 ID로 작성자 조건을 적용한다

URL 값 조작에 의한 IDOR를 서버 인증 정보 기반 조회로 방지합니다.

</details>



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

1단계 구현 카드는 코딩 전 계약을 정하는 양식이고, 아래 표는 구현 후 증거만 기록합니다. 선택형 문항으로 파일·명령·결과의 검증 기준을 확인합니다.

아래 4지 선다에서 검증 기준을 선택하고 정답·해설을 확인합니다.

### W171. PR 설명의 기본 네 가지 내용은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=171)

1. 비밀번호·salt·token·cookie
2. 색상·글꼴·이미지만
3. 파일명·파일명·파일명·파일명
4. 무엇·왜·검증·영향 범위

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 무엇·왜·검증·영향 범위

변경 결과와 이유·테스트·마이그레이션 등 영향 정보를 검토자가 이해할 수 있게 적습니다.

</details>


### W166. 트러블슈팅 문서를 작성하는 올바른 순서는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=166)

1. 문제 원인 없이 기술명만 나열한다
2. 실패한 모든 증거를 삭제한다
3. 해결하지 않은 문제를 성공했다고 적는다
4. 재현 가능한 문제·원인·해결·배운 점과 검증을 남긴다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 재현 가능한 문제·원인·해결·배운 점과 검증을 남긴다

독자가 같은 조건에서 확인할 수 있는 근거를 함께 기록합니다.

</details>


### W401. 검증 단계가 제공해야 하는 근거는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=401)

1. 성공이라고 적은 문장만
2. 아직 실행하지 않은 테스트 이름만
3. 라이브러리 버전 문자열만
4. 적절한 테스트의 실행 명령과 실제 결과

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 적절한 테스트의 실행 명령과 실제 결과

실행한 검사와 결과를 기록합니다. 환경상 실행하지 못한 검증은 성공으로 기록하지 않습니다.

</details>


### W263. Connection refused와 DB 인증 실패를 구분하는 기준은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=263)

1. 항상 JPA enum 선언 순서를 바꾼다
2. 브라우저 북마크를 삭제한다
3. 리스닝·주소·포트 연결과 사용자·비밀번호·권한 증거를 각각 확인한다
4. Flyway SQL 전체를 무조건 삭제한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 리스닝·주소·포트 연결과 사용자·비밀번호·권한 증거를 각각 확인한다

네트워크 접속 실패와 서버 접속 후 인증 거절은 원인 범위가 다릅니다.

</details>


### W261. 오류를 계층별로 추적하는 올바른 방법은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=261)

1. 모든 오류를 DB 비밀번호 탓으로 결론 낸다
2. 로그 없이 재설치만 반복한다
3. 예외 응답을 모두 성공으로 바꾼다
4. 요청 매핑·인가·입력·업무·DB·DTO 단계의 실제 실패 지점을 확인한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 요청 매핑·인가·입력·업무·DB·DTO 단계의 실제 실패 지점을 확인한다

마지막으로 성공한 단계와 실패 증거를 연결하면 원인 범위를 줄일 수 있습니다.

</details>


### W402. 루프의 설명 단계에서 이해를 확인하는 방식은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=402)

1. 선택한 설계와 대안·검증 근거를 코드와 대조한다
2. 기술 이름만 나열한다
3. 실패 사례를 모두 감춘다
4. 검증하지 않은 기능을 구현했다고 주장한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 선택한 설계와 대안·검증 근거를 코드와 대조한다

선택 이유는 실제 구현과 증거로 뒷받침합니다. 객관식 정답·해설을 실제 코드에 적용해 이해를 확인합니다.

</details>


### W403. 루프의 정리 단계에서 함께 맞출 대상은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=403)

1. 문서만 삭제하고 코드와의 차이를 숨긴다
2. 임시 빌드 결과를 유일한 원본으로 삼는다
3. 코드와 문서·HTML 목차·변경 이력·관련 링크
4. 모든 변경을 검증 없이 게시한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 코드와 문서·HTML 목차·변경 이력·관련 링크

산출물이 같은 동작을 설명하고 파일 링크가 실제 자료를 가리키도록 정리합니다.

</details>



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

개념 4지 선다:


### W172. 인증(Authentication)이 확인하는 것은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=172)

1. 업무 기능 실행 권한만
2. 사용자가 주장한 신원과 인증 증거의 일치
3. 주문의 현재 재고만
4. CSS 적용 여부

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 사용자가 주장한 신원과 인증 증거의 일치

인증은 신원을 확인하고 인가는 기능·데이터 접근 권한을 판단합니다.

</details>



### W173. 인가(Authorization)가 판단하는 것은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=173)

1. JDK 설치 여부
2. 문자열의 UTF-8 바이트 수
3. 인증된 사용자가 기능·데이터에 접근할 권한
4. 비밀번호 자체의 평문 값

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 인증된 사용자가 기능·데이터에 접근할 권한

로그인 성공만으로 모든 데이터에 접근할 수 있는 것은 아닙니다.

</details>



### W094. 로그인 비밀번호 검증에 사용하는 메서드는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=94)

1. passwordEncoder.encode(raw).equals(encoded)만 사용
2. 비밀번호 문자열을 응답에 출력
3. 이메일만 있으면 로그인 성공
4. passwordEncoder.matches(raw, encoded)

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · passwordEncoder.matches(raw, encoded)

BCrypt의 salt 때문에 재인코딩 결과 문자열의 단순 비교를 사용하지 않습니다.

</details>



### W097. 로그인 실패 메시지를 통일하는 이유는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=97)

1. 모든 실패를 성공으로 바꾸기 위해서다
2. 비밀번호 해시를 숨기지 않기 위해서다
3. 이메일 존재 여부를 이용한 계정 열거를 줄이기 위해서다
4. 로그인 속도를 항상 두 배로 만들기 위해서다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 이메일 존재 여부를 이용한 계정 열거를 줄이기 위해서다

사용자에게 계정 존재 여부를 직접 구분해 알려 주지 않습니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

### W094. 로그인 비밀번호 검증에 사용하는 메서드는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=94)

1. passwordEncoder.encode(raw).equals(encoded)만 사용
2. 비밀번호 문자열을 응답에 출력
3. 이메일만 있으면 로그인 성공
4. passwordEncoder.matches(raw, encoded)

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · passwordEncoder.matches(raw, encoded)

BCrypt의 salt 때문에 재인코딩 결과 문자열의 단순 비교를 사용하지 않습니다.

</details>
### W260. 로그인 성공 시 현재 세션에 저장되는 인증 값은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=260)

1. LOGIN_USER 키의 LoginUser 객체
2. 평문 password 문자열
3. Item Entity 전체
4. 사용자가 요청한 임의 role 문자열

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · LOGIN_USER 키의 LoginUser 객체

SessionConst.LOGIN_USER를 사용해 인증된 사용자 ID·이름·이메일·역할을 보관합니다.

</details>
### W283. FR-USER-001 로그인: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? Given 등록된 사용자, When 틀린 비밀번호로 로그인, Then HTTP ____ 와 공통 에러 응답을 받는다.

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=283)

1. 200
2. 401
3. 403
4. 400

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 400

현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

</details>
## FR-USER-002 로그아웃

개념 4지 선다:


### W174. 세션 로그아웃의 핵심 처리는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=174)

1. 서버의 해당 세션 인증 상태를 폐기한다
2. 브라우저에서 화면만 닫는다
3. 사용자의 모든 주문을 삭제한다
4. 평문 비밀번호를 응답한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 서버의 해당 세션 인증 상태를 폐기한다

현재 서버 세션을 invalidate해 이후 보호 요청의 인증 상태를 제거합니다.

</details>



### W175. 무상태 JWT의 즉시 강제 로그아웃을 구현하려면?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=175)

1. 클라이언트 토큰 삭제만으로 유출 복사본까지 무효화한다
2. JWT의 exp 검사를 제거한다
3. payload의 role을 암호 없이 믿는다
4. 폐기 목록·토큰 버전 등 서버 확인 정책을 추가한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 폐기 목록·토큰 버전 등 서버 확인 정책을 추가한다

클라이언트 삭제는 탈취된 복사본을 폐기하지 못합니다. 짧은 TTL은 노출 시간을 줄일 뿐 즉시 폐기는 별도 대책입니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

### W284. FR-USER-002 로그아웃: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? Given 로그인된 세션, When 로그아웃, Then 이후 보호 API 호출은 HTTP ____ 이다.

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=284)

1. 200
2. 401
3. 400
4. 403

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 401

보호 API에 인증 세션이 없으면 AUTHENTICATION_REQUIRED로 401을 응답합니다.

</details>
## FR-USER-003 내 정보 조회

개념 4지 선다:


### W176. 내 정보 조회에서 사용자를 정하는 안전한 기준은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=176)

1. URL에서 받은 임의의 userId만
2. 서버가 인증한 현재 사용자 ID
3. 요청 본문의 isAdmin만
4. 브라우저 북마크 이름

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 서버가 인증한 현재 사용자 ID

인증 주체의 ID를 사용해 다른 사용자 정보 조회 우회를 막습니다.

</details>



### W177. 응답 DTO에서 제외해야 할 정보는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=177)

1. 비밀번호 해시·내부 토큰 등 민감정보
2. 공개 품목 이름
3. 공개 카테고리 이름
4. 공지 제목

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 비밀번호 해시·내부 토큰 등 민감정보

평문뿐 아니라 비밀번호 해시와 내부 인증 자료도 응답 계약에서 제외합니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

### W285. FR-USER-003 내 정보 조회: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? Given 로그인하지 않은 사용자, When 내 정보 조회, Then HTTP ____.

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=285)

1. 200
2. 400
3. 403
4. 401

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 401

보호 API에 인증 세션이 없으면 AUTHENTICATION_REQUIRED로 401을 응답합니다.

</details>
### W177. 응답 DTO에서 제외해야 할 정보는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=177)

1. 비밀번호 해시·내부 토큰 등 민감정보
2. 공개 품목 이름
3. 공개 카테고리 이름
4. 공지 제목

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 비밀번호 해시·내부 토큰 등 민감정보

평문뿐 아니라 비밀번호 해시와 내부 인증 자료도 응답 계약에서 제외합니다.

</details>
## FR-USER-004 비밀번호 변경

개념 4지 선다:


### W178. 비밀번호 변경 전에 현재 비밀번호를 다시 검증하는 이유는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=178)

1. 새 비밀번호를 평문 저장하기 위해서다
2. 모든 세션을 ADMIN으로 바꾸기 위해서다
3. 탈취·공유된 로그인 세션의 피해를 줄이기 위해서다
4. 이메일 중복 검사를 없애기 위해서다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 탈취·공유된 로그인 세션의 피해를 줄이기 위해서다

중요 작업에 추가 인증 증거를 요구하되 새 비밀번호 해시 저장도 필요합니다.

</details>



### W098. 비밀번호 변경의 올바른 처리 순서는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=98)

1. 새 비밀번호를 평문 저장하고 이전 값을 응답한다
2. 현재 비밀번호 검증 후 새 비밀번호를 해시해 저장한다
3. 현재 비밀번호 확인 없이 누구나 변경한다
4. 세션 ID를 새 비밀번호로 저장한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 현재 비밀번호 검증 후 새 비밀번호를 해시해 저장한다

형식 검증과 현재 비밀번호 일치 확인·새 비밀번호 보호를 함께 수행합니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

### W286. FR-USER-004 비밀번호 변경: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 현재 비밀번호가 틀리면 HTTP ____.

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=286)

1. 200
2. 400
3. 401
4. 403

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 400

현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

</details>
### W276. 새 비밀번호를 기존 값과 다르게 변경한 뒤 이전 비밀번호의 matches 결과는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=276)

1. false
2. true
3. null
4. ADMIN

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · false

기존과 다른 새 비밀번호를 저장했다는 전제입니다. 새 해시에 이전 비밀번호가 일치하면 안 됩니다.

</details>
## FR-USER-005 권한 구분

개념 4지 선다:


### W253. USER·ADMIN·MANAGER를 정의하는 프로젝트 타입은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=253)

1. UserRole enum
2. PartnerType enum
3. ItemStatus enum
4. PurchaseOrderStatus enum

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · UserRole enum

사용자 역할과 거래처 유형·품목 상태·주문 상태를 별도 enum으로 구분합니다.

</details>



### W007. UserRole을 EnumType.STRING으로 저장하는 이유는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=7)

1. enum 선언 순서가 바뀌어도 저장된 이름의 의미를 유지하기 위해서다
2. 권한 검사가 자동으로 생기기 때문이다
3. DB 용량이 항상 ORDINAL보다 작기 때문이다
4. 알 수 없는 enum 값도 모두 허용하기 때문이다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · enum 선언 순서가 바뀌어도 저장된 이름의 의미를 유지하기 위해서다

STRING은 이름을 저장합니다. enum 이름 변경에는 별도 마이그레이션이 필요합니다.

</details>



### W079. 관리자 전용 품목 변경을 보호하는 방법은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=79)

1. URL에 admin을 쓰면 자동으로 보호된다
2. HTML 버튼을 숨기기만 한다
3. 인증된 사용자의 ADMIN 역할을 서버 가드에서 검사한다
4. 품목 코드에 ADMIN을 붙인다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 인증된 사용자의 ADMIN 역할을 서버 가드에서 검사한다

경로 이름과 UI만으로는 권한을 보장할 수 없습니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

### W287. FR-USER-005 권한 구분: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? USER 가 품목 등록 API 를 호출하면 HTTP ____.

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=287)

1. 403
2. 200
3. 400
4. 401

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 403

로그인된 사용자에게 해당 역할·소유권이 없으면 ACCESS_DENIED로 403을 응답합니다.

</details>
### W288. 유효한 CONFIRMED 수주·충분한 재고 등 다른 조건이 모두 충족될 때 MANAGER의 출고 API 성공 응답은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=288)

1. 400
2. 401
3. 200
4. 403

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 200

유효한 업무 조건을 갖춘 현재 출고 API는 200 OK를 반환합니다.

</details>
---

# 2. 거래처 관리 기능

## FR-PARTNER-001 거래처 등록

개념 4지 선다:


### W012. 사업자번호 unique 제약의 목적은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=12)

1. 서로 다른 사업체의 번호를 같게 만든다
2. 같은 사업체가 중복 거래처로 등록되는 것을 막는다
3. 공급사만 자동으로 고객사로 바꾼다
4. 사업자번호를 세션 ID로 사용한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 같은 사업체가 중복 거래처로 등록되는 것을 막는다

업무 식별자에 대한 최종 중복 방어선입니다.

</details>



### W014. PartnerType과 canSupply·canBuy 헬퍼의 목적은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=14)

1. DB 연결 비밀번호를 생성한다
2. 공급·판매 가능 여부를 도메인 규칙으로 모은다
3. INACTIVE 거래처도 항상 주문을 허용한다
4. 고객사와 공급사의 의미를 제거한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 공급·판매 가능 여부를 도메인 규칙으로 모은다

SUPPLIER·CUSTOMER·BOTH의 업무 의미를 헬퍼에 모으면 조건 중복을 줄일 수 있습니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

### W289. FR-PARTNER-001 거래처 등록: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 중복 사업자번호는 HTTP ____ 와 `DUPLICATE_BUSINESS_NUMBER`.

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=289)

1. 200
2. 401
3. 400
4. 403

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 400

현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

</details>
### W290. FR-PARTNER-001 거래처 등록: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? USER 권한 등록 시도는 HTTP ____.

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=290)

1. 200
2. 400
3. 401
4. 403

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 403

로그인된 사용자에게 해당 역할·소유권이 없으면 ACCESS_DENIED로 403을 응답합니다.

</details>
## FR-PARTNER-002 거래처 목록 조회

개념 4지 선다:


### W043. Pageable의 계층 전달 흐름은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=43)

1. Controller → Service → Repository
2. Entity → CSS → Session
3. DB → README → 브라우저 쿠키
4. Repository → 비밀번호 해시

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · Controller → Service → Repository

페이지·크기·정렬 조건을 필요한 검증을 거쳐 조회 계층으로 전달합니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

### W277. SUPPLIER만 조회하는 거래처 유형 필터에서 CUSTOMER 전용 거래처는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=277)

1. 반드시 포함된다
2. 자동으로 SUPPLIER가 된다
3. 삭제된다
4. 제외된다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 제외된다

조건에 맞는 거래처만 조회하며 조회가 데이터를 변경하지 않습니다.

</details>
## FR-PARTNER-003 거래처 상세 조회

개념 4지 선다:


### W251. 거래처 상세 경로 /api/partners/{id}의 PathVariable은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=251)

1. 조회할 거래처 ID
2. 사용자 권한 문자열
3. 공지 조회수
4. 페이지 총 개수

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 조회할 거래처 ID

조회 대상 ID를 받아 존재 여부와 접근 조건을 확인합니다.

</details>



### W109. 거래처 상세에 주문 통계를 포함할지 판단할 기준은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=109)

1. 통계가 있으면 인증을 제거한다
2. 항상 모든 주문 엔티티를 그대로 직렬화한다
3. 통계 계산이 DB unique를 대체한다
4. 화면 요구와 조회 비용·응답 크기를 함께 고려한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 화면 요구와 조회 비용·응답 크기를 함께 고려한다

통계를 같은 응답에 넣거나 별도 API로 나누는 선택을 명시합니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

### W291. FR-PARTNER-003 거래처 상세 조회: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 없는 거래처 ID 조회 시 HTTP ____.

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=291)

1. 200
2. 404
3. 400
4. 401

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 404

현재 ErrorCode의 대상 미존재 오류는 404입니다. 접근 권한 등 선행 조건은 충족했다고 가정합니다.

</details>
## FR-PARTNER-004 거래처 정보 수정

개념 4지 선다:


### W122. setter 대신 도메인 메서드를 사용하는 장점은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=122)

1. 모든 SQL이 없어지고 DB가 불필요해진다
2. 권한 검사가 무조건 자동 생성된다
3. 모든 필드를 외부에 무제한 공개한다
4. 업무 의미와 불변식 검증을 변경 지점에 모은다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 업무 의미와 불변식 검증을 변경 지점에 모은다

changeCategory·discontinue 같은 메서드는 허용된 상태 변경을 표현합니다.

</details>



### W099. 사업자번호 exists 검사와 unique 제약을 함께 두는 이유는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=99)

1. 사전 안내와 동시 등록의 최종 무결성을 각각 보장한다
2. exists 검사로 모든 경쟁 상태가 제거된다
3. unique 제약으로 화면 입력 형식까지 검증한다
4. 중복 번호를 일부러 두 번 저장한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 사전 안내와 동시 등록의 최종 무결성을 각각 보장한다

사전 조회 이후 다른 트랜잭션이 저장할 수 있으므로 DB 제약이 필요합니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

### W274. PUT·PATCH 입력 정책과 허용 필드를 기록할 문서는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=274)

1. 비밀번호 해시 컬럼
2. Gradle 다운로드 캐시
3. 실제 코드와 동기화한 API 명세
4. 브라우저 세션 쿠키

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 실제 코드와 동기화한 API 명세

입력·응답·권한·오류·수정 의미를 독자가 재현할 수 있게 API 계약에 적습니다.

</details>
### W292. FR-PARTNER-004 거래처 정보 수정: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 다른 거래처와 같은 사업자번호로 변경하면 HTTP ____.

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=292)

1. 400
2. 200
3. 401
4. 403

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 400

현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

</details>
## FR-PARTNER-005 거래처 삭제 / 비활성화

개념 4지 선다:


### W100. 이력이 있는 거래처를 삭제 요청할 때 현재 정책은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=100)

1. 주문 라인까지 모두 삭제한다
2. 사용자 계정 비밀번호를 초기화한다
3. 항상 DB 테이블 전체를 삭제한다
4. INACTIVE로 바꿔 이력을 보존한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · INACTIVE로 바꿔 이력을 보존한다

참조 구현의 거래처 삭제는 비활성화 정책입니다.

</details>



### W100. 이력이 있는 거래처를 삭제 요청할 때 현재 정책은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=100)

1. 주문 라인까지 모두 삭제한다
2. 사용자 계정 비밀번호를 초기화한다
3. 항상 DB 테이블 전체를 삭제한다
4. INACTIVE로 바꿔 이력을 보존한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · INACTIVE로 바꿔 이력을 보존한다

참조 구현의 거래처 삭제는 비활성화 정책입니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

### W278. ADMIN이 현재 거래처 삭제 API로 비활성화를 성공하면 응답과 상태는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=278)

1. 204 / INACTIVE
2. 200 / ACTIVE
3. 403 / INACTIVE
4. 201 / DRAFT

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 204 / INACTIVE

PartnerApiController는 noContent, Service는 비활성화 상태 변경을 수행합니다.

</details>
## FR-PARTNER-006 거래처 검색

개념 4지 선다:


### W136. 복잡해진 메서드 이름 쿼리를 정리하는 기준은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=136)

1. 메서드 이름을 무한히 늘린다
2. Controller에 SQL 문자열을 모두 붙인다
3. 가독성·조합 조건·조인 요구에 따라 Query나 Specification을 선택한다
4. 조회 조건을 문서에서 삭제한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 가독성·조합 조건·조인 요구에 따라 Query나 Specification을 선택한다

명시 쿼리와 동적 조건 추상화의 목적에 맞게 선택합니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

## FR-PARTNER-007 페이징

개념 4지 선다:


### W138. Page.map이 보존하는 정보는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=138)

1. 평문 비밀번호
2. 모든 서버 세션
3. 페이지 메타 정보와 변환된 content
4. SQL 수정 권한

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 페이지 메타 정보와 변환된 content

전체 개수와 페이지 수를 잃지 않고 Entity를 DTO로 바꿉니다.

</details>



### W138. Page.map이 보존하는 정보는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=138)

1. 평문 비밀번호
2. 모든 서버 세션
3. 페이지 메타 정보와 변환된 content
4. SQL 수정 권한

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 페이지 메타 정보와 변환된 content

전체 개수와 페이지 수를 잃지 않고 Entity를 DTO로 바꿉니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

### W045. 클라이언트 정렬을 허용할 때 필요한 정책은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=45)

1. 허용 필드와 기본 정렬을 명시하고 검증한다
2. 모든 입력을 SQL 문자열에 그대로 붙인다
3. 사용자가 임의의 DB 컬럼을 삭제하게 한다
4. 정렬을 허용하면 페이징을 제거한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 허용 필드와 기본 정렬을 명시하고 검증한다

서버 고정과 제한적 사용자 선택 중 요구에 맞게 정하되 안정적인 기준을 둡니다.

</details>
### W279. 정렬 허용 목록을 구현하는 과제에서 금지된 sort 입력의 올바른 처리는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=279)

1. SQL에 그대로 연결한다
2. 서버 관리자 권한을 부여한다
3. 입력 오류로 거절한다
4. 모든 데이터를 삭제한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 입력 오류로 거절한다

이 문제는 허용 목록 검증을 구현하는 설계 과제를 전제로 하며 현재 자동 바인딩 자체가 이를 보장하지는 않습니다.

</details>
---

# 3. 카테고리 / 품목 관리 기능

## FR-CAT-001 카테고리 등록

개념 4지 선다:


### W009. 카테고리명에 unique 제약을 두는 목적은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=9)

1. 모든 카테고리가 같은 이름을 갖게 하기 위해서다
2. 품목 조회를 무조건 한 쿼리로 줄이기 위해서다
3. 동일한 이름의 카테고리 중복을 차단하기 위해서다
4. 관리자 권한을 부여하기 위해서다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 동일한 이름의 카테고리 중복을 차단하기 위해서다

사용자에게 식별되는 이름의 중복은 Service 검사와 DB 제약으로 막습니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

## FR-CAT-002 카테고리 목록 조회

개념 4지 선다:


### W106. 소수 카테고리 전체 조회가 대규모에서 달라져야 하는 이유는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=106)

1. 1000개가 되면 모든 데이터가 자동 삭제된다
2. 응답 크기·메모리·지연이 늘어 페이징을 고려해야 한다
3. List 반환은 Java에서 항상 금지되어 있다
4. 전체 조회면 DB 연결이 필요 없다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 응답 크기·메모리·지연이 늘어 페이징을 고려해야 한다

데이터 규모와 화면 요구에 맞춰 전체 조회 또는 페이지 계약을 선택합니다.

</details>



### W106. 소수 카테고리 전체 조회가 대규모에서 달라져야 하는 이유는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=106)

1. 1000개가 되면 모든 데이터가 자동 삭제된다
2. 응답 크기·메모리·지연이 늘어 페이징을 고려해야 한다
3. List 반환은 Java에서 항상 금지되어 있다
4. 전체 조회면 DB 연결이 필요 없다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 응답 크기·메모리·지연이 늘어 페이징을 고려해야 한다

데이터 규모와 화면 요구에 맞춰 전체 조회 또는 페이지 계약을 선택합니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

## FR-CAT-003 카테고리 상세 조회

개념 4지 선다:


### W105. 카테고리 상세의 품목 조회에서 N+1을 줄이는 방법은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=105)

1. LAZY만 붙이면 항상 한 쿼리다
2. 각 품목마다 카테고리를 다시 조회한다
3. DTO로 바꾸기만 하면 모든 SQL이 사라진다
4. 필요한 관계를 fetch join·EntityGraph 또는 별도 일괄 조회로 가져온다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 필요한 관계를 fetch join·EntityGraph 또는 별도 일괄 조회로 가져온다

조회 계획을 의도적으로 정해야 하며 컬렉션 fetch와 페이징의 제약도 고려합니다.

</details>



### W111. 카테고리 상세에 모든 품목을 중첩할 때 고려할 것은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=111)

1. 응답 크기와 N+1, 필요하면 별도 품목 페이징이다
2. JSON이면 조회 비용이 없다
3. nested DTO면 SQL이 자동 최적화된다
4. 모든 품목의 비밀번호를 포함한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 응답 크기와 N+1, 필요하면 별도 품목 페이징이다

응답 구조와 조회 계획은 함께 검토해야 합니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

### W293. FR-CAT-003 카테고리 상세 조회: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 없는 카테고리 ID 는 HTTP ____.

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=293)

1. 404
2. 200
3. 400
4. 401

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 404

현재 ErrorCode의 대상 미존재 오류는 404입니다. 접근 권한 등 선행 조건은 충족했다고 가정합니다.

</details>
## FR-CAT-004 카테고리 정보 수정

개념 4지 선다:


### W108. 전체 수정과 부분 수정의 HTTP 메서드 조합은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=108)

1. GET / TRACE
2. OPTIONS / HEAD
3. PUT / PATCH
4. DELETE / CONNECT

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · PUT / PATCH

PUT은 전체 표현 교체, PATCH는 부분 변경 계약에 사용합니다.

</details>



### W103. 카테고리명 중복 검사의 다층 조합은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=103)

1. 화면 label만 표시
2. DB 연결 포트를 비교
3. Service existsByName과 DB unique
4. 엔티티 createdAt만 비교

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · Service existsByName과 DB unique

사용자 오류 안내와 경쟁 요청의 무결성을 서로 보완합니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

### W294. FR-CAT-004 카테고리 정보 수정: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 다른 카테고리가 이미 쓰는 이름으로 변경하면 HTTP ____.

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=294)

1. 200
2. 401
3. 400
4. 403

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 400

현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

</details>
## FR-CAT-005 카테고리 삭제

개념 4지 선다:


### W104. 소속 품목이 있는 카테고리 삭제의 현재 정책은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=104)

1. 품목을 함께 무조건 삭제한다
2. CATEGORY_HAS_ITEMS로 거부한다
3. 품목 카테고리를 존재하지 않는 ID로 바꾼다
4. 모든 품목을 단종 상태로 자동 변경한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · CATEGORY_HAS_ITEMS로 거부한다

현재 구현은 소속 품목이 있으면 삭제를 차단합니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

### W295. FR-CAT-005 카테고리 삭제: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 품목이 있는 카테고리 삭제 시 HTTP ____.

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=295)

1. 200
2. 400
3. 401
4. 403

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 400

현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

</details>
## FR-ITEM-001 품목 등록

개념 4지 선다:


### W040. 품목코드 중복을 안전하게 차단하는 조합은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=40)

1. 화면의 경고 문구만 표시
2. Service 사전 검사와 DB unique 제약
3. Repository exists 검사만으로 동시성까지 보장
4. 등록 이후 이름만 비교

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · Service 사전 검사와 DB unique 제약

사전 검사는 친절한 오류를, unique 제약은 경쟁 요청의 최종 방어를 제공합니다.

</details>



### W179. 품목이 반드시 속해야 하는 마스터는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=179)

1. HttpSession
2. Notice
3. PasswordEncoder
4. Category

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · Category

Item은 하나의 카테고리를 참조하며 저장·변경 때 존재 여부를 검증합니다.

</details>



### W021. 금액을 BigDecimal로 계산하는 이유는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=21)

1. double이 모든 십진 금액을 정확히 표현하기 때문이다
2. 십진 금액의 정밀도와 반올림 정책을 명시하기 위해서다
3. BigDecimal은 비교와 반올림이 전혀 필요 없기 때문이다
4. 문자열 연결만으로 금액 합계를 계산하기 위해서다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 십진 금액의 정밀도와 반올림 정책을 명시하기 위해서다

BigDecimal을 적절한 생성·스케일·반올림 정책과 함께 사용합니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

### W296. FR-ITEM-001 품목 등록: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 중복 품목코드는 HTTP ____ 와 `DUPLICATE_ITEM_CODE`.

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=296)

1. 200
2. 401
3. 403
4. 400

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 400

현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

</details>
### W297. FR-ITEM-001 품목 등록: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 존재하지 않는 카테고리는 HTTP ____ 와 `CATEGORY_NOT_FOUND`.

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=297)

1. 200
2. 404
3. 400
4. 401

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 404

현재 ErrorCode의 대상 미존재 오류는 404입니다. 접근 권한 등 선행 조건은 충족했다고 가정합니다.

</details>
## FR-ITEM-002 품목 목록 조회

개념 4지 선다:


### W043. Pageable의 계층 전달 흐름은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=43)

1. Controller → Service → Repository
2. Entity → CSS → Session
3. DB → README → 브라우저 쿠키
4. Repository → 비밀번호 해시

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · Controller → Service → Repository

페이지·크기·정렬 조건을 필요한 검증을 거쳐 조회 계층으로 전달합니다.

</details>



### W137. ItemResponse.from(item) 정적 팩토리의 역할은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=137)

1. 엔티티에서 공개할 응답 필드를 일관되게 변환한다
2. 새 엔티티를 항상 DB에 저장한다
3. 사용자 비밀번호를 복호화한다
4. 모든 페이지를 삭제한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 엔티티에서 공개할 응답 필드를 일관되게 변환한다

DTO 생성·매핑 책임을 한곳에 모으는 패턴입니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

### W280. 다른 기본·최대 크기 정책이 개입하지 않을 때 size=2 페이지의 content 최대 개수는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=280)

1. 1
2. 2
3. 20
4. 전체 행 수

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 2

마지막 페이지는 2보다 작을 수 있지만 요청한 페이지 크기를 넘지 않습니다.

</details>
### W298. FR-ITEM-002 품목 목록 조회: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 로그인하지 않은 사용자 조회는 HTTP ____.

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=298)

1. 200
2. 400
3. 403
4. 401

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 401

보호 API에 인증 세션이 없으면 AUTHENTICATION_REQUIRED로 401을 응답합니다.

</details>
## FR-ITEM-003 품목 상세 조회

개념 4지 선다:


### W250. 품목 상세 경로 /api/items/{id}의 PathVariable이 나타내는 것은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=250)

1. 로그인 사용자 비밀번호
2. HTTP 상태 코드
3. DB 서버 포트
4. 조회할 품목 ID

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 조회할 품목 ID

인증·역할 확인과 별도로 조회 대상 식별자를 해석합니다.

</details>



### W120. OSIV=false에서 LAZY 관계를 DTO로 변환할 위치는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=120)

1. 트랜잭션 안에서 필요한 관계를 로드해 Service에서 변환한다
2. 브라우저가 JPA 프록시를 조회하게 한다
3. 트랜잭션이 끝난 뒤 모든 관계를 처음 읽는다
4. JSON 응답에 EntityManager를 넣는다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 트랜잭션 안에서 필요한 관계를 로드해 Service에서 변환한다

영속성 컨텍스트가 종료된 뒤의 지연 로딩을 피하도록 조회와 변환을 설계합니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

### W299. FR-ITEM-003 품목 상세 조회: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 없는 품목 ID 조회 시 HTTP ____.

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=299)

1. 404
2. 200
3. 400
4. 401

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 404

현재 ErrorCode의 대상 미존재 오류는 404입니다. 접근 권한 등 선행 조건은 충족했다고 가정합니다.

</details>
## FR-ITEM-004 품목 정보 수정

개념 4지 선다:


### W122. setter 대신 도메인 메서드를 사용하는 장점은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=122)

1. 모든 SQL이 없어지고 DB가 불필요해진다
2. 권한 검사가 무조건 자동 생성된다
3. 모든 필드를 외부에 무제한 공개한다
4. 업무 의미와 불변식 검증을 변경 지점에 모은다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 업무 의미와 불변식 검증을 변경 지점에 모은다

changeCategory·discontinue 같은 메서드는 허용된 상태 변경을 표현합니다.

</details>



### W121. 품목의 카테고리를 바꾸기 전에 할 일은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=121)

1. 존재하지 않는 ID도 그대로 저장한다
2. 새 카테고리 존재를 확인하고 도메인 변경 메서드를 호출한다
3. 모든 과거 라인을 삭제한다
4. 카테고리 ID를 서버 포트로 바꾼다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 새 카테고리 존재를 확인하고 도메인 변경 메서드를 호출한다

참조 무결성 확인과 의미 있는 변경 동작을 분리해 수행합니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

### W300. FR-ITEM-004 품목 정보 수정: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 존재하지 않는 카테고리로 변경하면 HTTP ____.

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=300)

1. 200
2. 400
3. 404
4. 401

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 404

현재 ErrorCode의 대상 미존재 오류는 404입니다. 접근 권한 등 선행 조건은 충족했다고 가정합니다.

</details>
### W281. 품목 수정이 정상 커밋된 뒤 상세 재조회에서 기대할 결과는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=281)

1. 항상 이전 값만 보인다
2. 수정한 품목이 자동 삭제된다
3. 변경된 값이 보인다
4. 모든 품목이 같은 값이 된다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 변경된 값이 보인다

트랜잭션 커밋 뒤 재조회로 저장 결과를 확인합니다.

</details>
## FR-ITEM-005 품목 단종 처리

개념 4지 선다:


### W020. 단종 시 삭제 대신 DISCONTINUED를 사용하는 이유는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=20)

1. 신규 주문에 단종 품목을 강제로 추가하기 위해서다
2. 과거 주문 라인의 참조와 거래 이력을 보존하기 위해서다
3. 재고를 무조건 0으로 만들기 위해서다
4. DB 백업 없이 테이블을 초기화하기 위해서다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 과거 주문 라인의 참조와 거래 이력을 보존하기 위해서다

단종은 신규 사용 제한이며 과거 데이터를 파괴하는 삭제와 다릅니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

### W301. FR-ITEM-005 품목 단종 처리: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 단종 후 신규 발주 라인에 포함하면 HTTP ____.

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=301)

1. 200
2. 401
3. 400
4. 403

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 400

현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

</details>
---

## FR-ITEM-006 품목 검색

개념 4지 선다:


### W136. 복잡해진 메서드 이름 쿼리를 정리하는 기준은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=136)

1. 메서드 이름을 무한히 늘린다
2. Controller에 SQL 문자열을 모두 붙인다
3. 가독성·조합 조건·조인 요구에 따라 Query나 Specification을 선택한다
4. 조회 조건을 문서에서 삭제한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 가독성·조합 조건·조인 요구에 따라 Query나 Specification을 선택한다

명시 쿼리와 동적 조건 추상화의 목적에 맞게 선택합니다.

</details>



### W043. Pageable의 계층 전달 흐름은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=43)

1. Controller → Service → Repository
2. Entity → CSS → Session
3. DB → README → 브라우저 쿠키
4. Repository → 비밀번호 해시

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · Controller → Service → Repository

페이지·크기·정렬 조건을 필요한 검증을 거쳐 조회 계층으로 전달합니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

### W252. 카테고리와 keyword를 함께 제한하는 품목 검색의 결합은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=252)

1. 두 조건을 무조건 OR로 결합
2. 카테고리 조건 AND keyword 조건
3. 카테고리 조건을 항상 무시
4. keyword를 비밀번호로 저장

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 카테고리 조건 AND keyword 조건

동일 카테고리 안에서 이름·코드 조건을 검색하려면 범위 조건과 검색 조건을 AND로 묶습니다.

</details>
## FR-ITEM-007 페이징

공통 `Page` 개념, `Pageable` 파라미터, 기본값, 허용하지 않는 sort 검증은 [FR-PARTNER-007 페이징](#fr-partner-007-페이징)을 단일 기준으로 사용합니다. 여기에는 품목에만 다른 계약을 적습니다.

4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

### W245. 품목 정렬을 id·itemCode·name·createdAt으로 제한하는 과제에서 허용할 필드는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=245)

1. password
2. salt
3. createdAt
4. sessionId

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · createdAt

이 문제는 명시한 정렬 허용 정책을 전제로 합니다. 현재 Controller 기본 정렬은 createdAt 내림차순이며 허용 목록 검증은 별도 구현 과제입니다.

</details>
### W273. Specification 검색 조건과 페이징을 함께 전달하는 호출은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=273)

1. findAll(passwordEncoder, session)
2. deleteAll(pageable)
3. findById(cssColor)
4. findAll(specification, pageable)

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · findAll(specification, pageable)

JpaSpecificationExecutor의 조건과 Pageable을 함께 사용해 필터된 페이지를 조회합니다.

</details>
### W120. OSIV=false에서 LAZY 관계를 DTO로 변환할 위치는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=120)

1. 트랜잭션 안에서 필요한 관계를 로드해 Service에서 변환한다
2. 브라우저가 JPA 프록시를 조회하게 한다
3. 트랜잭션이 끝난 뒤 모든 관계를 처음 읽는다
4. JSON 응답에 EntityManager를 넣는다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 트랜잭션 안에서 필요한 관계를 로드해 Service에서 변환한다

영속성 컨텍스트가 종료된 뒤의 지연 로딩을 피하도록 조회와 변환을 설계합니다.

</details>
---

# 4. 발주(구매) 기능

## FR-PO-001 발주서 작성

개념 4지 선다:


### W185. 주문 헤더-라인 구조의 구성은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=185)

1. 비밀번호와 세션 쿠키
2. 카테고리 한 개와 CSS 색상
3. JDK와 Gradle 캐시
4. 주문 공통 정보와 여러 품목·수량·단가 라인

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 주문 공통 정보와 여러 품목·수량·단가 라인

헤더는 거래처·작성일 같은 공통 정보, 라인은 개별 주문 항목을 표현합니다.

</details>



### W023. 신규 발주를 만드는 팩토리의 초기 상태는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=23)

1. DRAFT
2. RECEIVED
3. APPROVED
4. CANCELED

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · DRAFT

작성과 요청·승인·입고를 구분하기 위해 초깃값을 DRAFT로 설정합니다.

</details>



### W046. 발주 헤더와 라인을 같은 트랜잭션으로 저장하는 이유는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=46)

1. 헤더 저장 후 실패한 라인은 영구 누락시킨다
2. 조회 속도가 항상 정확히 두 배가 된다
3. 트랜잭션이 외래키를 없애 준다
4. 라인 실패 시 헤더까지 롤백해 부분 주문을 방지한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 라인 실패 시 헤더까지 롤백해 부분 주문을 방지한다

주문 구성 요소는 함께 성공하거나 함께 실패해야 합니다.

</details>



### W048. 발주 거래처 유형 검증은 어디에서 수행해야 하는가?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=48)

1. 화면 필터만으로 충분하다
2. Service에서 SUPPLIER 또는 BOTH인지 검사한다
3. 브라우저 북마크에서 검사한다
4. DB 포트 번호에서 검사한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · Service에서 SUPPLIER 또는 BOTH인지 검사한다

화면 조건은 우회 가능하므로 업무 계층에서 거래처 유형을 확인합니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

### W302. FR-PO-001 발주서 작성: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 라인이 0개면 HTTP ____.

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=302)

1. 200
2. 401
3. 403
4. 400

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 400

현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

</details>
### W303. FR-PO-001 발주서 작성: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 단종 품목 라인은 HTTP ____.

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=303)

1. 400
2. 200
3. 401
4. 403

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 400

현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

</details>
### W304. FR-PO-001 발주서 작성: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? CUSTOMER 전용 거래처는 HTTP ____.

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=304)

1. 200
2. 400
3. 401
4. 403

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 400

현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

</details>
### W023. 신규 발주를 만드는 팩토리의 초기 상태는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=23)

1. DRAFT
2. RECEIVED
3. APPROVED
4. CANCELED

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · DRAFT

작성과 요청·승인·입고를 구분하기 위해 초깃값을 DRAFT로 설정합니다.

</details>
## FR-PO-002 발주 요청 (submit)

개념 4지 선다:


### W060. 수주 작성과 확정 API를 분리하는 장점은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=60)

1. 확정 전에 자동으로 출고한다
2. 트랜잭션을 모두 제거한다
3. 임시 작성과 최종 확정의 상태·검증 시점을 구분한다
4. 권한 검사를 브라우저에만 둔다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 임시 작성과 최종 확정의 상태·검증 시점을 구분한다

현재 프로젝트는 작성과 confirm 상태 전이를 구분합니다.

</details>



### W181. 발주 결재 요청의 정상 전이는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=181)

1. DRAFT → RECEIVED
2. DRAFT → REQUESTED
3. APPROVED → DRAFT
4. RECEIVED → REQUESTED

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · DRAFT → REQUESTED

작성자가 유효한 DRAFT 발주를 요청하며 이미 요청된 상태를 재요청하면 상태 오류가 됩니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

### W305. FR-PO-002 발주 요청 (submit): 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 이미 REQUESTED 발주를 다시 submit 하면 HTTP ____.

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=305)

1. 200
2. 400
3. 401
4. 403

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 400

현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

</details>
## FR-PO-003 내 발주서 목록

개념 4지 선다:


### W124. 본인 발주 목록의 소유권 기준은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=124)

1. 요청 쿼리 writerId를 인증 없이 신뢰한다
2. 클라이언트 이름만 비교한다
3. 현재 인증 사용자의 ID로 작성자 조건을 적용한다
4. 모든 사용자에게 전체 목록을 반환한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 현재 인증 사용자의 ID로 작성자 조건을 적용한다

URL 값 조작에 의한 IDOR를 서버 인증 정보 기반 조회로 방지합니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

### W282. A 사용자의 내 발주 목록에 B가 작성한 발주는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=282)

1. 포함되지 않는다
2. 모두 포함된다
3. 자동으로 A의 발주가 된다
4. 비밀번호로 정렬된다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 포함되지 않는다

현재 인증 사용자 ID를 작성자 조건으로 사용해야 합니다.

</details>
## FR-PO-004 발주서 상세 조회

개념 4지 선다:


### W126. 발주 상세 조회를 허용하는 현재 정책은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=126)

1. 작성자 또는 ADMIN/MANAGER
2. 아무 비로그인 사용자
3. 작성자가 아닌 USER만
4. 공급사 이메일을 입력한 사람 모두

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 작성자 또는 ADMIN/MANAGER

역할과 작성자 소유권을 인증된 사용자 기준으로 검사합니다.

</details>



### W247. 헤더와 라인 DTO를 함께 반환할 때의 장점은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=247)

1. 하나의 응답이면 반드시 SQL도 한 번만 실행된다
2. nested 구조면 비밀번호를 포함해도 안전하다
3. 서비스 트랜잭션이 필요 없어지게 된다
4. 화면이 한 상세 응답에서 필요한 구성 정보를 얻는다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 화면이 한 상세 응답에서 필요한 구성 정보를 얻는다

HTTP 요청 횟수와 SQL 조회 횟수는 다릅니다. 응답 구조와 조회 계획을 따로 설계합니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

### W306. FR-PO-004 발주서 상세 조회: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 작성자가 아닌 USER 는 HTTP ____.

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=306)

1. 200
2. 400
3. 403
4. 401

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 403

로그인된 사용자에게 해당 역할·소유권이 없으면 ACCESS_DENIED로 403을 응답합니다.

</details>
## FR-PO-005 발주서 취소

개념 4지 선다:


### W125. 작성자 발주 취소가 허용되는 현재 상태 조합은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=125)

1. RECEIVED·CANCELED·REJECTED
2. RECEIVED만
3. DRAFT·REQUESTED·APPROVED
4. 모든 상태

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · DRAFT·REQUESTED·APPROVED

현재 PurchaseOrder는 입고 후 작성자 취소를 허용하지 않습니다.

</details>



### W254. 발주 취소가 성공하면 설정되는 상태는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=254)

1. RECEIVED
2. CONFIRMED
3. SHIPPED
4. CANCELED

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · CANCELED

현재 발주는 작성자의 DRAFT·REQUESTED·APPROVED 취소만 허용합니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

### W254. 발주 취소가 성공하면 설정되는 상태는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=254)

1. RECEIVED
2. CONFIRMED
3. SHIPPED
4. CANCELED

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · CANCELED

현재 발주는 작성자의 DRAFT·REQUESTED·APPROVED 취소만 허용합니다.

</details>
### W307. FR-PO-005 발주서 취소: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? RECEIVED 발주를 본인이 취소 시도하면 HTTP ____.

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=307)

1. 400
2. 200
3. 401
4. 403

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 400

현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

</details>
### W308. FR-PO-005 발주서 취소: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 타인의 DRAFT 발주 취소 시도는 HTTP ____.

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=308)

1. 200
2. 400
3. 401
4. 403

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 403

로그인된 사용자에게 해당 역할·소유권이 없으면 ACCESS_DENIED로 403을 응답합니다.

</details>
## FR-PO-006 ~ 007 발주 승인/반려

개념 4지 선다:


### W051. REQUESTED가 아닌 발주의 승인·반려를 막는 설계는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=51)

1. 도메인 전이 메서드에 상태 가드를 둔다
2. 화면 버튼만 숨긴다
3. 승인 후 작성자 이메일을 바꾼다
4. DTO 필드 이름을 바꾸면 충분하다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 도메인 전이 메서드에 상태 가드를 둔다

Service 인가와 별도로 도메인 메서드가 상태 불변식을 보호합니다.

</details>



### W184. 경쟁 상태 변경을 다루는 두 가지 DB 잠금 방식은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=184)

1. 낙관적 락과 비관적 락
2. HTML 락과 CSS 락
3. 세션 글꼴과 북마크
4. 문자열 escape와 URL encoding

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 낙관적 락과 비관적 락

버전 충돌 감지와 DB 잠금은 서로 다른 전략이며 경쟁 수준과 업무 요구로 선택합니다.

</details>



### W074. NotBlank·Positive·Size·Pattern의 역할 조합은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=74)

1. 공백 금지·양수·길이/개수·정규식 검증
2. DB 생성·로그인·정렬·출고 처리
3. 모두 같은 문자열 길이 검사
4. 모두 권한 검사

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 공백 금지·양수·길이/개수·정규식 검증

각 어노테이션은 적용 타입과 검사하는 입력 조건이 다릅니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

### W309. FR-PO-006 ~ 007 발주 승인/반려: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 이미 승인된 발주 재승인은 HTTP ____.

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=309)

1. 200
2. 401
3. 403
4. 400

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 400

현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

</details>
### W310. FR-PO-006 ~ 007 발주 승인/반려: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 반려 사유 blank 는 HTTP ____.

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=310)

1. 200
2. 401
3. 400
4. 403

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 400

현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

</details>
## FR-PO-008 입고 처리

개념 4지 선다:


### W182. 발주 입고의 정상 전이는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=182)

1. DRAFT → RECEIVED
2. REQUESTED → APPROVED
3. APPROVED → RECEIVED
4. CANCELED → RECEIVED

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · APPROVED → RECEIVED

입고는 승인된 발주만 가능하며 재고 증가와 같은 트랜잭션에서 처리합니다.

</details>



### W183. 발주 입고가 성공하면 재고는 어떻게 변하는가?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=183)

1. 각 라인 수량만큼 항상 감소한다
2. 현재고가 무조건 0이 된다
3. 안전재고가 자동 삭제된다
4. 각 라인의 입고 수량만큼 증가한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 각 라인의 입고 수량만큼 증가한다

StockService가 입고 수량을 반영하며 중복 입고와 경쟁 요청을 방어합니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

### W311. FR-PO-008 입고 처리: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? APPROVED 가 아닌 발주 입고 시도는 HTTP ____.

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=311)

1. 200
2. 400
3. 401
4. 403

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 400

현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

</details>
## FR-PO-009 관리자 발주 목록

개념 4지 선다:


### W127. 관리자 발주 목록의 기본 검색 조건은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=127)

1. 비밀번호와 세션 쿠키
2. CSS 색상과 글꼴
3. 조회수와 JWT 서명
4. 상태와 거래처 조건

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 상태와 거래처 조건

현재 관리자 목록은 status·partnerId 선택 조건을 사용합니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

### W312. FR-PO-009 관리자 발주 목록: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? USER 가 `/api/admin/purchase-orders` 호출하면 HTTP ____.

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=312)

1. 403
2. 200
3. 400
4. 401

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 403

로그인된 사용자에게 해당 역할·소유권이 없으면 ACCESS_DENIED로 403을 응답합니다.

</details>
---

# 5. 공지사항 기능

## FR-NOTICE-001 공지 등록

개념 4지 선다:


### W057. 공지 등록·수정·삭제의 권한을 보장하는 곳은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=57)

1. 화면 버튼 표시 여부만 확인한다
2. Service에서 ADMIN 여부를 검증한다
3. 공지 제목에 ADMIN이 있으면 허용한다
4. 조회수에 비례해 권한을 부여한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · Service에서 ADMIN 여부를 검증한다

여러 호출 경로에서 동일한 권한 규칙을 지켜야 합니다.

</details>



### W074. NotBlank·Positive·Size·Pattern의 역할 조합은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=74)

1. 공백 금지·양수·길이/개수·정규식 검증
2. DB 생성·로그인·정렬·출고 처리
3. 모두 같은 문자열 길이 검사
4. 모두 권한 검사

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 공백 금지·양수·길이/개수·정규식 검증

각 어노테이션은 적용 타입과 검사하는 입력 조건이 다릅니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

### W313. FR-NOTICE-001 공지 등록: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? USER 가 공지 등록하면 HTTP ____.

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=313)

1. 403
2. 200
3. 400
4. 401

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 403

로그인된 사용자에게 해당 역할·소유권이 없으면 ACCESS_DENIED로 403을 응답합니다.

</details>
## FR-NOTICE-002 공지 목록 조회

개념 4지 선다:


### W057. 공지 등록·수정·삭제의 권한을 보장하는 곳은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=57)

1. 화면 버튼 표시 여부만 확인한다
2. Service에서 ADMIN 여부를 검증한다
3. 공지 제목에 ADMIN이 있으면 허용한다
4. 조회수에 비례해 권한을 부여한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · Service에서 ADMIN 여부를 검증한다

여러 호출 경로에서 동일한 권한 규칙을 지켜야 합니다.

</details>



### W029. 중요 공지를 먼저, 같은 중요도에서 최신 공지를 보여 주는 정렬은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=29)

1. important ASC, createdAt ASC
2. important DESC, createdAt DESC
3. viewCount ASC만 적용
4. 제목 길이만 오름차순

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · important DESC, createdAt DESC

참조 구현은 중요도·생성시각·ID 내림차순으로 순서를 안정화합니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

### W115. 공지 목록에 전체 content를 담을 때의 비용은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=115)

1. 응답 크기가 무조건 줄어든다
2. DB unique가 없어도 된다
3. 목록 조회수가 반드시 0이 된다
4. 긴 본문 때문에 페이로드와 전송·렌더링 비용이 커질 수 있다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 긴 본문 때문에 페이로드와 전송·렌더링 비용이 커질 수 있다

목록 요약과 상세 본문 응답을 분리할 수 있습니다.

</details>
## FR-NOTICE-003 공지 상세 조회

개념 4지 선다:


### W114. GET에서 조회수를 늘릴 때 주의할 점은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=114)

1. GET은 기술적으로 쓰기를 절대 수행하지 못한다
2. 재요청·봇·캐시가 조회수 의미에 영향을 줄 수 있다
3. 조회수 증가가 인증을 대체한다
4. PATCH를 쓰면 중복 요청이 항상 사라진다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 재요청·봇·캐시가 조회수 의미에 영향을 줄 수 있다

별도 증가 API도 가능한 대안이며 어느 방식이든 집계 의미와 원자성을 정해야 합니다.

</details>



### W135. 조회수 원자적 증가 쿼리의 핵심은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=135)

1. DB에서 viewCount = viewCount + 1을 직접 실행한다
2. 조회한 값을 브라우저에서 1 더해 덮어쓴다
3. 항상 viewCount=1로 저장한다
4. 공지 ID를 조회수로 바꾼다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · DB에서 viewCount = viewCount + 1을 직접 실행한다

Modifying Query와 트랜잭션에서 DB가 증가 연산을 수행해 유실을 줄입니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

## FR-NOTICE-004 공지 수정

개념 4지 선다:


### W057. 공지 등록·수정·삭제의 권한을 보장하는 곳은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=57)

1. 화면 버튼 표시 여부만 확인한다
2. Service에서 ADMIN 여부를 검증한다
3. 공지 제목에 ADMIN이 있으면 허용한다
4. 조회수에 비례해 권한을 부여한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · Service에서 ADMIN 여부를 검증한다

여러 호출 경로에서 동일한 권한 규칙을 지켜야 합니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

### W314. FR-NOTICE-004 공지 수정: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? USER 수정 시도는 HTTP ____.

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=314)

1. 200
2. 400
3. 401
4. 403

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 403

로그인된 사용자에게 해당 역할·소유권이 없으면 ACCESS_DENIED로 403을 응답합니다.

</details>
### W315. FR-NOTICE-004 공지 수정: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 제목 blank 는 HTTP ____.

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=315)

1. 200
2. 400
3. 401
4. 403

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 400

현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

</details>
## FR-NOTICE-005 공지 삭제

개념 4지 선다:


### W255. 현재 공지 삭제 API의 저장 정책은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=255)

1. 모든 공지를 비활성화 플래그로만 처리한다
2. ADMIN 권한을 확인하고 해당 공지 행을 실제 삭제한다
3. 아무 USER가 전체 공지를 삭제한다
4. 공지 삭제 시 발주를 함께 삭제한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · ADMIN 권한을 확인하고 해당 공지 행을 실제 삭제한다

NoticeService.delete는 권한 확인 후 delete를 호출합니다. soft delete는 대안 설계입니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

## FR-NOTICE-006 중요 공지 표시

개념 4지 선다:


### W058. 중요 공지 우선 목록의 핵심 정렬은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=58)

1. important 내림차순
2. important 오름차순
3. 작성자 이메일 길이순
4. 조회수 0인 공지만 선택

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · important 내림차순

중요도가 같으면 생성시각과 ID 등으로 순서를 안정화합니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

---

# 6. 수주(판매) 기능

## FR-SO-001 수주서 작성

개념 4지 선다:


### W185. 주문 헤더-라인 구조의 구성은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=185)

1. 비밀번호와 세션 쿠키
2. 카테고리 한 개와 CSS 색상
3. JDK와 Gradle 캐시
4. 주문 공통 정보와 여러 품목·수량·단가 라인

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 주문 공통 정보와 여러 품목·수량·단가 라인

헤더는 거래처·작성일 같은 공통 정보, 라인은 개별 주문 항목을 표현합니다.

</details>



### W062. 신규 수주의 거래처 유형 검증은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=62)

1. SUPPLIER 전용만 허용한다
2. Service에서 CUSTOMER 또는 BOTH를 허용한다
3. 브라우저 필터만 검사한다
4. 거래처 유형을 무시한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · Service에서 CUSTOMER 또는 BOTH를 허용한다

수주는 판매 흐름이므로 고객 역할이 있는 거래처를 요구합니다.

</details>



### W180. 새 수주의 초기 상태는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=180)

1. COMPLETED
2. DRAFT
3. SHIPPED
4. CANCELED

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · DRAFT

작성 후 confirm을 통해 CONFIRMED로 전이합니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

### W316. FR-SO-001 수주서 작성: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 라인 0개는 HTTP ____.

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=316)

1. 200
2. 401
3. 400
4. 403

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 400

현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

</details>
### W317. FR-SO-001 수주서 작성: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? SUPPLIER 전용 거래처는 HTTP ____.

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=317)

1. 200
2. 401
3. 403
4. 400

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 400

현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

</details>
## FR-SO-002 수주 확정

개념 4지 선다:


### W256. 수주 확정의 정상 상태 전이는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=256)

1. CONFIRMED → SHIPPED
2. SHIPPED → COMPLETED
3. DRAFT → CONFIRMED
4. COMPLETED → DRAFT

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · DRAFT → CONFIRMED

확정과 출고·완료는 별도 명령과 검증 경계를 갖습니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

## FR-SO-003 내 수주서 목록

개념 4지 선다:


### W257. 내 수주 목록에 포함되는 주문의 기준은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=257)

1. 모든 고객사의 전체 발주
2. 다른 사용자만 작성한 수주
3. 현재 사용자가 작성한 수주
4. 공지 조회수 0인 수주

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 현재 사용자가 작성한 수주

인증 사용자 ID의 작성자 조건을 사용해 본인 목록을 조회합니다.

</details>


- 처리자로서의 목록은 별도 `pending` API 로 분리한다.

4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

## FR-SO-005 수주서 상세 조회

개념 4지 선다:


### W187. 현재 수주 상세 조회가 허용되는 사용자는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=187)

1. 모든 비로그인 사용자
2. 작성자 또는 ADMIN/MANAGER
3. 작성자가 아닌 USER만
4. 공지 작성자만

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 작성자 또는 ADMIN/MANAGER

현재 requireAccess는 작성자와 두 관리 역할을 허용합니다. 과거 과제의 담당자 제한과 구분합니다.

</details>



### W132. ADMIN 전체 수주 조회를 Service에 노출할 때 필요한 계약은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=132)

1. 요청 본문의 isAdmin=true만 신뢰한다
2. 관리자 계정 비밀번호를 검색 조건으로 공개한다
3. 항상 전체 엔티티 그래프를 응답한다
4. 인증 사용자 정보와 검색·페이지 조건을 받아 관리자 권한을 확인한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 인증 사용자 정보와 검색·페이지 조건을 받아 관리자 권한을 확인한다

검색 계약과 실행 권한을 서버에서 함께 검증합니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

### W318. 작성자가 아닌 USER가 타인의 수주 상세 API를 조회할 때 응답은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=318)

1. 200
2. 400
3. 403
4. 401

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 403

로그인된 사용자에게 해당 역할·소유권이 없으면 ACCESS_DENIED로 403을 응답합니다.

</details>
## FR-SO-006 ~ 008 수주 출고/완료/취소

개념 4지 선다:

- 상태 전이:

### W064. 수주 출고·완료 상태 전이의 올바른 규칙은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=64)

1. DRAFT에서 바로 완료한다
2. COMPLETED에서 다시 출고한다
3. CANCELED에서 재고를 차감한다
4. CONFIRMED에서 출고하고 SHIPPED에서 완료한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · CONFIRMED에서 출고하고 SHIPPED에서 완료한다

종료 상태 추가 전이는 차단하고 취소 가능 상태를 따로 검증합니다.

</details>



### W064. 수주 출고·완료 상태 전이의 올바른 규칙은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=64)

1. DRAFT에서 바로 완료한다
2. COMPLETED에서 다시 출고한다
3. CANCELED에서 재고를 차감한다
4. CONFIRMED에서 출고하고 SHIPPED에서 완료한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · CONFIRMED에서 출고하고 SHIPPED에서 완료한다

종료 상태 추가 전이는 차단하고 취소 가능 상태를 따로 검증합니다.

</details>



### W189. 현재 수주 취소의 올바른 정책은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=189)

1. 비로그인 누구나 완료 수주를 취소한다
2. 작성자 또는 ADMIN/MANAGER가 허용 상태에서 사유를 입력한다
3. 사유를 넣으면 모든 종료 상태도 취소한다
4. USER가 URL ID만 바꾸면 타인 수주 취소가 가능하다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 작성자 또는 ADMIN/MANAGER가 허용 상태에서 사유를 입력한다

DRAFT·CONFIRMED·SHIPPED에서만 취소하며 출고 후 취소는 재고를 복원합니다.

</details>



### W259. 현재 수주 출고·완료 처리가 허용되는 역할은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=259)

1. USER만
2. 비로그인만
3. SUPPLIER 또는 CUSTOMER
4. ADMIN 또는 MANAGER

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · ADMIN 또는 MANAGER

역할 검증 뒤 허용 상태·재고 등 업무 조건도 검사합니다.

</details>



### W067. 수주 취소 사유 정책은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=67)

1. 모든 취소는 사유 없이 처리한다
2. 사유가 있으면 완료 상태도 무조건 취소한다
3. 공백이 아닌 사유를 요구한다
4. 사유를 비밀번호 필드에 저장한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 공백이 아닌 사유를 요구한다

사유 검증과 취소 가능한 상태 검증을 모두 수행합니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

### W319. FR-SO-006 ~ 008 수주 출고/완료/취소: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? CONFIRMED 가 아닌 상태에서 ship 호출은 HTTP ____.

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=319)

1. 400
2. 200
3. 401
4. 403

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 400

현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

</details>
### W320. FR-SO-006 ~ 008 수주 출고/완료/취소: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 취소 사유 blank 는 HTTP ____.

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=320)

1. 200
2. 400
3. 401
4. 403

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 400

현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

</details>
### W321. FR-SO-006 ~ 008 수주 출고/완료/취소: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 완료된 수주를 취소하면 HTTP ____.

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=321)

1. 200
2. 401
3. 400
4. 403

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 400

현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

</details>
## FR-SO-009 수주 상태 조회

개념 4지 선다:


### W271. 주문 상태를 임의 문자열 대신 enum으로 관리하는 목적은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=271)

1. 모든 상태 전이가 자동 허용된다
2. DB 잠금이 필요 없어지게 한다
3. 허용 상태 집합을 명시하고 잘못된 상태 표현을 줄인다
4. 사용자에게 관리자 권한을 부여한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 허용 상태 집합을 명시하고 잘못된 상태 표현을 줄인다

enum 정의만으로 전이 규칙이 지켜지지는 않습니다. 도메인 메서드의 상태 가드도 필요합니다.

</details>



### W258. 수주 목록의 status 검색 조건은 무엇을 제한하는가?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=258)

1. 사용자의 비밀번호 길이
2. 조회 시점의 수주 상태
3. 거래처 사업자번호 형식
4. 웹 브라우저 버전

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 조회 시점의 수주 상태

상태 조건은 작성자·거래처·기간·페이지 조건과 요구에 맞게 조합합니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

### W322. FR-SO-009 수주 상태 조회: 다음 API 검증 시나리오의 HTTP 빈칸에 알맞은 값은? 종료 상태(`COMPLETED`/`CANCELED`)에서 추가 전이를 시도하면 HTTP ____.

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=322)

1. 200
2. 400
3. 401
4. 403

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 400

현재 ErrorCode의 입력·중복·상태 등 해당 업무 오류는 400입니다. 다른 인증·CSRF·존재 조건은 정상이라고 가정합니다.

</details>
## FR-SO-004 처리 대기 수주서

개념 4지 선다:


### W188. 현재 처리 대기 수주 목록의 상태 조건은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=188)

1. DRAFT만
2. COMPLETED만
3. CONFIRMED
4. CANCELED만

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · CONFIRMED

현재 SalesOrderService.pending은 출고 전 CONFIRMED 목록을 조회합니다. 완료 대기는 별도 SHIPPED 목록입니다.

</details>



4지 선다 구현 확인: 위 문항에서 선택한 규칙을 정답·해설과 대조합니다.

---

# 7. 기능별 공통 테스트 체크리스트

아래 대표 4지 선다 문항을 풀고 선택지·정답·해설의 규칙을 기능별 테스트와 대조합니다.

| 분류 | 4지 선다 문항 |
|---|---|
| 성공 | [W389. 현재 발주 생성 API가 정상적으로 새 주문을 만들었을 때 HTTP 상태는?](workbook-questions.md#w389) |
| 검증 | [W275. 필수 DTO 입력 검증 실패의 현재 공통 업무 코드는?](workbook-questions.md#w275) |
| 권한 | [W113. 카테고리 변경·조회 권한의 현재 정책은?](workbook-questions.md#w113) |
| 소유자 | [W124. 본인 발주 목록의 소유권 기준은?](workbook-questions.md#w124) |
| 상태 | [W051. REQUESTED가 아닌 발주의 승인·반려를 막는 설계는?](workbook-questions.md#w051) |
| 트랜잭션 | [W046. 발주 헤더와 라인을 같은 트랜잭션으로 저장하는 이유는?](workbook-questions.md#w046) |
| DTO | [W177. 응답 DTO에서 제외해야 할 정보는?](workbook-questions.md#w177) |
| 동시성 | [W056. 같은 발주를 두 관리자가 동시에 승인하면 필요한 대응은?](workbook-questions.md#w056) |
| 도메인 규칙 | [W161. DTO·Entity·Service·DB 검증 책임의 조합은?](workbook-questions.md#w161) |
| 헤더-라인 | [W267. 신규 주문의 헤더를 저장하기 전에 해야 할 일은?](workbook-questions.md#w267) |

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


### W263. Connection refused와 DB 인증 실패를 구분하는 기준은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=263)

1. 항상 JPA enum 선언 순서를 바꾼다
2. 브라우저 북마크를 삭제한다
3. 리스닝·주소·포트 연결과 사용자·비밀번호·권한 증거를 각각 확인한다
4. Flyway SQL 전체를 무조건 삭제한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 리스닝·주소·포트 연결과 사용자·비밀번호·권한 증거를 각각 확인한다

네트워크 접속 실패와 서버 접속 후 인증 거절은 원인 범위가 다릅니다.

</details>



### W262. Flyway 검증 실패와 Hibernate validate 실패의 차이는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=262)

1. 전자는 마이그레이션 이력, 후자는 엔티티와 실제 스키마 일치를 확인한다
2. 둘 다 비밀번호를 해시한다
3. 둘 다 CSRF 토큰 만료를 뜻한다
4. 두 실패는 모두 로그아웃하면 해결된다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 전자는 마이그레이션 이력, 후자는 엔티티와 실제 스키마 일치를 확인한다

checksum·version 문제와 매핑·컬럼·타입 불일치를 서로 다른 검증 경계에서 진단합니다.

</details>



### W264. 영속 데이터가 사라진 듯 보일 때 먼저 확인할 설정은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=264)

1. HTML의 글꼴만 확인한다
2. 활성 프로필과 실제 JDBC URL이 임시 H2인지 확인한다
3. 문제집 정답 위치만 확인한다
4. README 파일 확장자만 확인한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 활성 프로필과 실제 JDBC URL이 임시 H2인지 확인한다

기본 in-memory H2는 재시작 시 데이터가 초기화됩니다. 영속 MySQL 연결과 구분해야 합니다.

</details>



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

아래 4지 선다에서 검증 기준을 선택하고 정답·해설을 확인합니다.

### W325. 워크북에 의존성 버전을 따로 복사해 관리하지 않는 이유는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=325)

1. 버전이 있으면 JDK가 실행되지 않기 때문이다
2. 루트 빌드·Wrapper 설정과 문서의 버전 불일치를 줄이기 위해서다
3. 문서의 모든 버전이 항상 자동으로 변경되기 때문이다
4. 라이브러리 업데이트가 검증을 필요로 하지 않기 때문이다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 루트 빌드·Wrapper 설정과 문서의 버전 불일치를 줄이기 위해서다

코드의 정식 설정을 기준으로 문서를 동기화하고 변경된 조합을 검증합니다.

</details>


### W264. 영속 데이터가 사라진 듯 보일 때 먼저 확인할 설정은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=264)

1. HTML의 글꼴만 확인한다
2. 활성 프로필과 실제 JDBC URL이 임시 H2인지 확인한다
3. 문제집 정답 위치만 확인한다
4. README 파일 확장자만 확인한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 활성 프로필과 실제 JDBC URL이 임시 H2인지 확인한다

기본 in-memory H2는 재시작 시 데이터가 초기화됩니다. 영속 MySQL 연결과 구분해야 합니다.

</details>


### W232. H2와 실제 MySQL의 차이를 검증하는 방법은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=232)

1. H2 테스트만으로 모든 MySQL 동작을 확정한다
2. MySQL 버전 문자열만 문서에 쓴다
3. 실제 MySQL에서 mysqlSchemaTest 등 프로필별 검증을 수행한다
4. 테스트 없이 ddl-auto=update로 바꾼다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 실제 MySQL에서 mysqlSchemaTest 등 프로필별 검증을 수행한다

Flyway 적용과 JPA validate를 실제 MySQL에서 별도로 확인합니다.

</details>


### W236. 기본 SCM 프로그램의 로그인 주소와 실행 명령은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=236)

1. python3 questions.tsv / localhost:3306/login
2. node quiz.css / localhost:443/db
3. ./gradlew bootRun / http://localhost:8080/login
4. git status / localhost:8000/mysql

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · ./gradlew bootRun / http://localhost:8080/login

기본 서버 포트는 8080이며 문제집의 정적 서버 8000과 구분합니다.

</details>


### W263. Connection refused와 DB 인증 실패를 구분하는 기준은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=263)

1. 항상 JPA enum 선언 순서를 바꾼다
2. 브라우저 북마크를 삭제한다
3. 리스닝·주소·포트 연결과 사용자·비밀번호·권한 증거를 각각 확인한다
4. Flyway SQL 전체를 무조건 삭제한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 리스닝·주소·포트 연결과 사용자·비밀번호·권한 증거를 각각 확인한다

네트워크 접속 실패와 서버 접속 후 인증 거절은 원인 범위가 다릅니다.

</details>


### W262. Flyway 검증 실패와 Hibernate validate 실패의 차이는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=262)

1. 전자는 마이그레이션 이력, 후자는 엔티티와 실제 스키마 일치를 확인한다
2. 둘 다 비밀번호를 해시한다
3. 둘 다 CSRF 토큰 만료를 뜻한다
4. 두 실패는 모두 로그아웃하면 해결된다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 전자는 마이그레이션 이력, 후자는 엔티티와 실제 스키마 일치를 확인한다

checksum·version 문제와 매핑·컬럼·타입 불일치를 서로 다른 검증 경계에서 진단합니다.

</details>


### W401. 검증 단계가 제공해야 하는 근거는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=401)

1. 성공이라고 적은 문장만
2. 아직 실행하지 않은 테스트 이름만
3. 라이브러리 버전 문자열만
4. 적절한 테스트의 실행 명령과 실제 결과

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 적절한 테스트의 실행 명령과 실제 결과

실행한 검사와 결과를 기록합니다. 환경상 실행하지 못한 검증은 성공으로 기록하지 않습니다.

</details>


### W166. 트러블슈팅 문서를 작성하는 올바른 순서는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=166)

1. 문제 원인 없이 기술명만 나열한다
2. 실패한 모든 증거를 삭제한다
3. 해결하지 않은 문제를 성공했다고 적는다
4. 재현 가능한 문제·원인·해결·배운 점과 검증을 남긴다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 재현 가능한 문제·원인·해결·배운 점과 검증을 남긴다

독자가 같은 조건에서 확인할 수 있는 근거를 함께 기록합니다.

</details>



공식 참고 자료:

- https://docs.brew.sh/Installation
- https://formulae.brew.sh/formula/openjdk@21
- https://formulae.brew.sh/formula/maven
- https://formulae.brew.sh/formula/mysql-client@8.0
- https://docs.spring.io/spring-boot/3.5/system-requirements.html
- https://developer.apple.com/library/archive/documentation/MacOSX/Conceptual/BPSystemStartup/Chapters/CreatingLaunchdJobs.html
- https://docs.docker.com/reference/cli/docker/compose/up/
- https://dev.mysql.com/doc/refman/8.0/en/using-mysqldump.html
