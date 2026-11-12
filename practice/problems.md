# 모듈별 4지 선다 문제

40개 모듈의 403문항을 모두 선택지 4개·정답 1개·해설로 제공합니다.

[브라우저 문제집](sw-quiz/index.html?bank=workbook) · [전체 전환 문제집](workbook-questions.md) · [정답표](answers.md)

모듈 번호와 starter 경로는 고정 식별자입니다. [권장 학습 트랙](README.md#학습-트랙)의 순서로 4지 선다를 풉니다.

<a id="module-00"></a>
## 0. 빌드와 애플리케이션 설정 (TRD 3.1, 3.2)

- [선택형 학습 파일](starter/00-build-config/build.gradle.fragment)
- [선택형 학습 파일](starter/00-build-config/application.yml.fragment)

<a id="w001"></a>
### W001. Spring Web·JPA·Validation·Thymeleaf·H2의 역할 조합은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=1)

1. HTTP 처리·영속성·입력 검증·서버 HTML·DB
2. 모두 비밀번호 암호화 전용이다
3. JPA는 HTML, Thymeleaf는 DB를 담당한다
4. H2는 HTTP 라우팅 도구다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · HTTP 처리·영속성·입력 검증·서버 HTML·DB

각 의존성은 요청 처리부터 저장과 화면 생성까지 서로 다른 책임을 담당합니다.

</details>


<a id="w002"></a>
### W002. 현재 프로젝트의 Java와 Jakarta API 사용 방식은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=2)

1. Java 8과 javax.persistence만 사용한다
2. Java 17 대상 컴파일과 jakarta 패키지를 사용한다
3. JavaScript로 Java 소스를 컴파일한다
4. JDK 없이 javac을 직접 실행한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · Java 17 대상 컴파일과 jakarta 패키지를 사용한다

정식 기준은 build.gradle이며 Servlet·Validation·Persistence에는 jakarta 이름 공간을 사용합니다.

</details>


<a id="w003"></a>
### W003. MySQL 프로필에서 ddl-auto=validate의 의미는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=3)

1. 기동할 때 모든 데이터를 삭제한다
2. 자동으로 누락된 컬럼을 추가한다
3. Flyway 이력을 무시하고 테이블을 생성한다
4. 스키마와 엔티티 매핑의 일치 여부를 검사한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 스키마와 엔티티 매핑의 일치 여부를 검사한다

이 프로젝트의 MySQL 스키마 변경은 Flyway가 맡고 Hibernate는 validate로 확인합니다.

</details>


<a id="w004"></a>
### W004. 기본 H2 콘솔을 필요할 때만 켜는 방법은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=4)

1. 항상 공개하고 인증을 제거한다
2. ddl-auto를 update로 바꾸면 콘솔이 켜진다
3. H2_CONSOLE_ENABLED=true로 실행한다
4. questions.js에서 콘솔 포트를 설정한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · H2_CONSOLE_ENABLED=true로 실행한다

application.yml은 콘솔을 기본 비활성화하고 환경 변수로 활성화합니다.

</details>


<a id="w323"></a>
### W323. ddl-auto 옵션의 의미 조합으로 올바른 것은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=323)

1. validate는 모든 테이블 삭제, none은 매번 생성
2. update는 마이그레이션 이력과 롤백을 완전히 관리한다
3. create-drop은 영속 운영 데이터 보존 전용이다
4. create는 새 스키마 생성, create-drop은 종료 시 삭제, validate는 매핑 검사, none은 자동 DDL 없음

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · create는 새 스키마 생성, create-drop은 종료 시 삭제, validate는 매핑 검사, none은 자동 DDL 없음

update는 매핑에 맞춘 스키마 변경을 시도합니다. 현재 운영형 MySQL은 Flyway와 validate를 사용합니다.

</details>


<a id="w324"></a>
### W324. 현재 MySQL 프로필 실행에 필요한 DB 관련 의존성은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=324)

1. MySQL Connector/J와 Flyway MySQL 지원
2. H2 드라이버만
3. Thymeleaf만
4. Mockito만

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · MySQL Connector/J와 Flyway MySQL 지원

프로젝트는 MySQL JDBC 드라이버와 Flyway의 MySQL 모듈을 runtimeOnly로 선언합니다.

</details>


<a id="w325"></a>
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


<a id="w326"></a>
### W326. 현재 Boot 4 프로젝트의 JSON mapper와 MVC 테스트 의존성 조합은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=326)

1. javax.jackson과 spring-boot-starter-css
2. mysql.mapper와 thymeleaf-test-only
3. java.sql.ObjectMapper와 gradle-session
4. tools.jackson.databind와 spring-boot-starter-webmvc-test

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · tools.jackson.databind와 spring-boot-starter-webmvc-test

정식 소스 import와 build.gradle에서 실제 패키지·starter를 확인합니다.

</details>


<a id="w360"></a>
### W360. H2 콘솔을 활성화한 상태에서도 관리자 인증이 필요한 이유는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=360)

1. DB 조회·변경 기능이 데이터 노출·변조 경로가 될 수 있기 때문이다
2. 콘솔은 정적 CSS만 제공하기 때문이다
3. 콘솔 주소를 알면 누구나 운영 데이터를 바꿔야 하기 때문이다
4. 콘솔이 비밀번호 해시를 자동으로 복원하기 때문이다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · DB 조회·변경 기능이 데이터 노출·변조 경로가 될 수 있기 때문이다

기본 비활성화와 활성화 시 서버 접근 통제를 함께 적용합니다.

</details>


<a id="w364"></a>
### W364. 학습용 설정에서 Hibernate SQL의 줄바꿈·들여쓰기를 활성화하는 값은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=364)

1. hibernate.format_sql: false
2. server.port: 3306
3. hibernate.format_sql: true
4. spring.flyway.enabled: false

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · hibernate.format_sql: true

format_sql은 SQL 표시 형식을 조절합니다. 출력 여부나 DB 스키마 변경 설정과는 구분합니다.

</details>


<a id="w365"></a>
### W365. 학습용 application.yml의 spring.jpa.show-sql 빈칸에 SQL 콘솔 출력을 켜는 값은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=365)

1. false
2. validate
3. true
4. create-drop

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · true

show-sql=true는 SQL 콘솔 출력을 켭니다. 로깅 프레임워크의 org.hibernate.SQL 설정과 구분하고 운영 로그 정책에 맞춰 사용합니다.

</details>


<a id="w366"></a>
### W366. Hibernate의 org.hibernate.orm.jdbc.bind에서 바인딩 값 상세 로그를 켜는 수준은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=366)

1. TRACE
2. OFF
3. ERROR
4. WARN

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · TRACE

JDBC 파라미터 바인딩 상세 로그는 TRACE 수준으로 확인합니다. 실제 개인정보·비밀번호 등이 로그에 노출되지 않도록 학습 환경에서 필요한 범위만 사용합니다. 참조: https://docs.hibernate.org/orm/7.0/logging/logging.html

</details>


<a id="w367"></a>
### W367. 현재 Boot 4 빌드에서 서버의 MVC·REST 요청 처리를 위한 starter 이름은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=367)

1. spring-boot-starter-data-jpa
2. spring-boot-starter-validation
3. spring-boot-starter-thymeleaf
4. spring-boot-starter-webmvc

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · spring-boot-starter-webmvc

루트 build.gradle은 Spring MVC 요청 처리에 spring-boot-starter-webmvc를 사용합니다.

</details>


<a id="w368"></a>
### W368. JPA·Hibernate와 Spring Data Repository를 사용하는 현재 빌드의 starter는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=368)

1. spring-boot-starter-validation
2. spring-boot-starter-data-jpa
3. spring-boot-starter-thymeleaf
4. spring-boot-starter-security

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · spring-boot-starter-data-jpa

spring-boot-starter-data-jpa는 영속성 구현과 Spring Data JPA 사용을 위한 의존성입니다.

</details>


<a id="w369"></a>
### W369. 현재 빌드에서 jakarta.validation 기반 입력 검증을 제공하는 starter는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=369)

1. spring-boot-starter-thymeleaf
2. spring-boot-starter-security
3. spring-boot-starter-validation
4. spring-boot-starter-data-jpa

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · spring-boot-starter-validation

입력 DTO 제약과 검증 구현을 사용하도록 spring-boot-starter-validation을 선언합니다. 실제 요청 검증을 적용할 지점도 지정해야 합니다.

</details>


<a id="w370"></a>
### W370. 현재 빌드에서 서버의 HTML 템플릿 렌더링을 위한 starter는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=370)

1. spring-boot-starter-data-jpa
2. spring-boot-starter-security
3. spring-boot-starter-validation
4. spring-boot-starter-thymeleaf

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · spring-boot-starter-thymeleaf

Thymeleaf starter는 서버에서 모델 값으로 HTML 템플릿을 렌더링하는 구성을 제공합니다.

</details>


<a id="w371"></a>
### W371. 현재 빌드에서 SecurityFilterChain·CSRF·보안 헤더를 구성하는 starter는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=371)

1. spring-boot-starter-security
2. spring-boot-starter-thymeleaf
3. spring-boot-starter-validation
4. spring-boot-starter-data-jpa

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · spring-boot-starter-security

Security starter로 필터 기반 보안을 구성합니다. 현재 프로젝트의 로그인·업무 인가는 세션 인터셉터와 서비스 가드도 사용합니다.

</details>


<a id="w372"></a>
### W372. Gradle runtimeOnly에 선언하는 학습용 H2 JDBC 드라이버 좌표는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=372)

1. com.mysql:mysql-connector-j
2. com.h2database:h2
3. org.flywaydb:flyway-mysql
4. org.springframework.security:spring-security-test

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · com.h2database:h2

기본 인메모리 학습 DB의 JDBC 드라이버는 com.h2database:h2입니다. Boot 4의 H2 콘솔 자동 설정 모듈은 별도 의존성입니다.

</details>


<a id="module-01"></a>
## 1. User 엔티티 (TRD 3.3.1, 3.6.1)

- [선택형 학습 파일](starter/01-user-entity/User.entity.java)

<a id="w005"></a>
### W005. JPA 엔티티와 기본키 선언의 역할 조합은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=5)

1. Entity는 DTO, Table은 URL, Id는 세션이다
2. Id는 모든 필드를 암호화한다
3. Entity는 영속 객체, Table은 테이블, Id는 키, GeneratedValue는 키 생성 전략이다
4. GeneratedValue는 모든 문자열을 자동 채운다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · Entity는 영속 객체, Table은 테이블, Id는 키, GeneratedValue는 키 생성 전략이다

매핑 어노테이션은 객체와 테이블·기본키의 관계를 정의합니다.

</details>


<a id="w006"></a>
### W006. 로그인 계정 저장 정책으로 올바른 것은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=6)

1. 이메일은 매번 중복 등록하고 비밀번호는 평문 저장한다
2. 이메일은 중복을 막고 비밀번호는 BCrypt 해시로 저장한다
3. 비밀번호 해시를 로그인 응답에 공개한다
4. 이메일 unique가 비밀번호를 암호화한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 이메일은 중복을 막고 비밀번호는 BCrypt 해시로 저장한다

식별자 중복 방지와 비밀번호 보호는 서로 다른 보안·무결성 책임입니다.

</details>


<a id="w007"></a>
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


<a id="w008"></a>
### W008. createdAt과 updatedAt을 자동 기록하려면?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=8)

1. 필드 이름만 있으면 DB가 항상 자동 기록한다
2. 모든 조회에서 createdAt을 현재 시각으로 바꾼다
3. 컨트롤러가 비밀번호로 날짜를 계산한다
4. 감사 어노테이션·리스너와 JPA Auditing 설정을 연결한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 감사 어노테이션·리스너와 JPA Auditing 설정을 연결한다

참조 구현은 BaseTimeEntity와 JpaAuditingConfig로 생성·수정 시각을 관리합니다.

</details>


<a id="w234"></a>
### W234. User와 Partner를 분리하는 이유는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=234)

1. 모든 거래처가 반드시 로그인해야 하기 때문이다
2. 로그인·권한 계정과 공급·고객 거래처의 책임·생명주기가 다르기 때문이다
3. User가 있으면 Partner는 저장할 수 없기 때문이다
4. 사용자 비밀번호를 거래처 번호로 쓰기 위해서다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 로그인·권한 계정과 공급·고객 거래처의 책임·생명주기가 다르기 때문이다

거래처 마스터는 로그인 계정과 다른 업무 개념입니다.

</details>


<a id="w253"></a>
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


<a id="w373"></a>
### W373. User 클래스를 JPA 엔티티로 선언하는 애너테이션은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=373)

1. @Service
2. @Entity
3. @RestController
4. @Configuration

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · @Entity

@Entity는 JPA 관리 대상 타입을 선언합니다. 서비스·컨트롤러·설정 클래스 표시는 목적이 다릅니다.

</details>


<a id="module-02"></a>
## 2. Category 엔티티 / Partner 엔티티 (TRD 3.3.2 ~ 3.3.3, 3.5.2 ~ 3.5.3, 3.6.2)

- [선택형 학습 파일](starter/02-category-entity/Category.entity.java)
- [선택형 학습 파일](starter/02-category-entity/Partner.entity.java)

<a id="w009"></a>
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


<a id="w010"></a>
### W010. Category→Item 컬렉션을 추가할 때의 올바른 판단은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=10)

1. 탐색 편의와 양방향 동기화 비용을 함께 고려한다
2. 양방향 관계면 모든 N+1이 자동 사라진다
3. 단방향 관계는 JPA에서 금지되어 있다
4. mappedBy가 있는 쪽이 항상 외래키를 갱신한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 탐색 편의와 양방향 동기화 비용을 함께 고려한다

양방향 매핑은 탐색을 돕지만 관계 양쪽을 일관되게 관리해야 합니다.

</details>


<a id="w011"></a>
### W011. DB 문자열 컬럼 길이와 입력 검증의 관계는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=11)

1. 모든 컬럼을 VARCHAR(1)로 통일한다
2. 컬럼 길이가 있으면 NotBlank는 필요 없다
3. DB 길이는 비밀번호 해시 알고리즘을 결정한다
4. 도메인 최대 길이에 맞춰 DB 컬럼과 DTO 제약을 정한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 도메인 최대 길이에 맞춰 DB 컬럼과 DTO 제약을 정한다

DB 저장 한도와 사용자 입력 계약을 함께 맞춰 예측 가능한 검증을 제공합니다.

</details>


<a id="w012"></a>
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


<a id="w013"></a>
### W013. 사업자번호 정규화를 설계할 때 올바른 기준은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=13)

1. 화면에서만 하이픈을 제거하고 서버는 원문을 비교한다
2. 조회마다 서로 다른 규칙으로 번호를 바꾼다
3. 정규화하면 DB unique가 불필요해진다
4. 중복 비교와 저장 전에 동일한 정규화 규칙을 적용한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 중복 비교와 저장 전에 동일한 정규화 규칙을 적용한다

어느 계층에 두더라도 모든 입력 경로의 비교·저장 표현이 같아야 합니다.

</details>


<a id="w014"></a>
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


<a id="w015"></a>
### W015. PartnerStatus의 올바른 값 조합은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=15)

1. ACTIVE / INACTIVE
2. DRAFT / RECEIVED
3. ADMIN / USER
4. ASC / DESC

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · ACTIVE / INACTIVE

거래처 활성 여부는 주문 상태나 사용자 역할과 다른 enum입니다.

</details>


<a id="w016"></a>
### W016. 거래처를 비활성화한 뒤 과거 주문은 어떻게 관리하는가?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=16)

1. 과거 주문과 라인을 모두 자동 삭제한다
2. 모든 과거 주문의 작성자를 바꾼다
3. 과거 이력은 보존하고 신규 주문 사용을 차단한다
4. 비활성화하면 외래키 검사가 사라진다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 과거 이력은 보존하고 신규 주문 사용을 차단한다

상태 변경으로 신규 사용을 제한하면서 거래 이력을 유지합니다.

</details>


<a id="w361"></a>
### W361. SUPPLIER·CUSTOMER·BOTH enum과 두 boolean 역할 플래그를 비교하면?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=361)

1. 두 플래그는 어떤 조합도 자동으로 항상 유효하다
2. enum은 허용 조합을 명시하고 플래그는 조합 유효성 정책을 별도로 정해야 한다
3. enum은 DB에 저장할 수 없다
4. BOTH를 저장하면 공지 관리자 권한이 생긴다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · enum은 허용 조합을 명시하고 플래그는 조합 유효성 정책을 별도로 정해야 한다

현재 업무에서 허용하는 역할 조합과 확장 요구에 맞춰 표현 방식을 선택합니다.

</details>


<a id="w374"></a>
### W374. 현재 참조 구현의 거래처 등록 API에서 partnerType이 null이면 올바른 처리는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=374)

1. 항상 BOTH로 바꿔 저장한다
2. 사용자 역할을 거래처 유형으로 저장한다
3. null을 문자열 NULL로 저장한다
4. 필수 입력 검증으로 거부한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 필수 입력 검증으로 거부한다

PartnerCreateRequest는 partnerType에 @NotNull을 선언합니다. starter의 기본값 선택 예시는 대안 연습이므로 현재 API 정책과 혼동하지 않습니다.

</details>


<a id="w375"></a>
### W375. 별도 상태를 지정하지 않은 신규 거래처의 현재 초기 상태는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=375)

1. ACTIVE
2. INACTIVE
3. DRAFT
4. DISCONTINUED

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · ACTIVE

Partner 생성자는 상태를 지정하지 않았을 때 PartnerStatus.ACTIVE를 사용합니다.

</details>


<a id="w376"></a>
### W376. 재비활성화를 예외로 거부하는 starter 가드에서 검사할 거래처 상태는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=376)

1. ACTIVE
2. DRAFT
3. INACTIVE
4. RECEIVED

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · INACTIVE

해당 starter의 IllegalStateException 가드는 이미 INACTIVE인지 검사하는 연습입니다. 현재 실행되는 Partner.deactivate는 INACTIVE 재대입을 허용하므로 전제를 구분합니다.

</details>


<a id="module-03"></a>
## 3. Item 엔티티 (TRD 3.3.4, 3.5.4, 3.6.3)

- [선택형 학습 파일](starter/03-item-entity/Item.entity.java)

<a id="w017"></a>
### W017. 여러 Item이 하나의 Category에 속하는 매핑은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=17)

1. 모든 Item에 OneToOne을 사용한다
2. Item과 Category를 같은 기본키로 강제한다
3. Item에서 ManyToOne과 category 외래키를 사용한다
4. 카테고리를 HTTP 세션으로만 저장한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · Item에서 ManyToOne과 category 외래키를 사용한다

여러 품목이 같은 카테고리를 참조하는 N:1 관계입니다.

</details>


<a id="w018"></a>
### W018. Category 연관관계를 LAZY로 두는 이유는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=18)

1. 필요한 조회에서 관계 로딩 범위를 선택하기 위해서다
2. 모든 연관 객체를 항상 즉시 조회하기 위해서다
3. 트랜잭션 밖에서도 추가 조회를 항상 보장하기 위해서다
4. 외래키 제약을 해제하기 위해서다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 필요한 조회에서 관계 로딩 범위를 선택하기 위해서다

LAZY는 불필요한 로딩을 줄이지만 DTO 변환 시 영속성 컨텍스트와 조회 계획이 필요합니다.

</details>


<a id="w019"></a>
### W019. ItemStatus의 올바른 값 조합은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=19)

1. SUPPLIER / CUSTOMER
2. REQUESTED / REJECTED
3. USER / MANAGER
4. ACTIVE / DISCONTINUED

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · ACTIVE / DISCONTINUED

품목은 운영 상태와 단종 상태를 별도로 표현합니다.

</details>


<a id="w020"></a>
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


<a id="w021"></a>
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


<a id="w179"></a>
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


<a id="w377"></a>
### W377. 신규 품목의 현재 초기 상태는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=377)

1. DISCONTINUED
2. REQUESTED
3. CANCELED
4. ACTIVE

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · ACTIVE

Item 생성 및 등록은 신규 품목을 ACTIVE로 시작합니다. 주문 상태 enum과 품목 상태 enum을 구분합니다.

</details>


<a id="w378"></a>
### W378. 이미 단종된 품목을 예외로 거부하는 starter 가드가 비교할 상태는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=378)

1. DISCONTINUED
2. ACTIVE
3. INACTIVE
4. APPROVED

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · DISCONTINUED

해당 starter의 재단종 거부 전제에서는 DISCONTINUED를 비교합니다. 현재 실행되는 Item.discontinue는 상태 재대입을 허용하므로 참조 구현과 대안 가드를 구분합니다.

</details>


<a id="module-04"></a>
## 4. PurchaseOrder 엔티티와 라인 (TRD 3.3.5, 3.3.6, 3.6.4)

- [선택형 학습 파일](starter/04-purchase-order-entity/PurchaseOrder.entity.java)

<a id="w022"></a>
### W022. 발주일과 납기일에 LocalDate를 사용하는 이유는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=22)

1. 나노초 정밀도의 이벤트 시각을 표현하기 위해서다
2. DB 연결 만료 시간을 나타내기 위해서다
3. 모든 날짜를 자동으로 오늘로 바꾸기 위해서다
4. 시각·시간대 없이 업무 날짜만 표현하기 위해서다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 시각·시간대 없이 업무 날짜만 표현하기 위해서다

날짜만 필요한 업무 필드와 승인·입고의 시각 필드를 구분합니다.

</details>


<a id="w023"></a>
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


<a id="w024"></a>
### W024. 승인자와 반려 사유를 기록하는 올바른 시점은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=24)

1. 모든 조회에서 승인자와 사유를 덮어쓴다
2. 작성할 때 반려 사유를 항상 확정한다
3. 승인 전이에서 승인자를, 반려 전이에서 사유를 기록한다
4. 입고 후 작성자를 승인자로 바꾼다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 승인 전이에서 승인자를, 반려 전이에서 사유를 기록한다

상태와 감사 정보는 해당 전이가 성공할 때 함께 기록해야 합니다.

</details>


<a id="w025"></a>
### W025. writerId와 approverId를 분리하는 이유는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=25)

1. 작성과 승인 행위자의 책임·이력을 구분하기 위해서다
2. 두 값이 항상 같도록 강제하기 위해서다
3. 작성자의 로그인 비밀번호를 기록하기 위해서다
4. 권한 검사를 생략하기 위해서다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 작성과 승인 행위자의 책임·이력을 구분하기 위해서다

행위자 필드는 역할이 다르며 ID 분리 자체가 인가를 대신하지는 않습니다.

</details>


<a id="w026"></a>
### W026. cascade ALL과 orphanRemoval의 효과와 주의점은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=26)

1. 모든 거래처까지 자동 삭제한다
2. 트랜잭션 없이 부분 저장을 방지한다
3. 라인 생명주기를 헤더에 묶고 컬렉션 제거 시 삭제 영향을 고려한다
4. 연관관계 모든 조회를 항상 하나의 SQL로 만든다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 라인 생명주기를 헤더에 묶고 컬렉션 제거 시 삭제 영향을 고려한다

cascade·고아 삭제와 트랜잭션 원자성은 별개의 기능입니다.

</details>


<a id="w027"></a>
### W027. totalAmount와 lineAmount 합계의 불변식을 지키는 방법은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=27)

1. 브라우저가 보낸 totalAmount를 검증 없이 저장한다
2. 모든 변경 경로가 동일한 서버 재계산 규칙을 사용하게 한다
3. 헤더 금액을 라인과 무관한 난수로 정한다
4. DB 조회 때마다 작성자를 바꾼다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 모든 변경 경로가 동일한 서버 재계산 규칙을 사용하게 한다

도메인 또는 Service의 배치는 설계 선택이지만 금액 계산은 서버에서 일관되어야 합니다.

</details>


<a id="w181"></a>
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


<a id="w185"></a>
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


<a id="w231"></a>
### W231. 주문에서 거래처·품목·사용자를 ID로 참조하는 설계 이유는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=231)

1. ID만 저장하면 모든 무결성이 자동 보장된다
2. JPA 연관관계는 기술적으로 사용할 수 없다
3. 과거 주문을 자동 삭제하기 위해서다
4. 집합 간 결합·로딩 범위를 줄이고 Service에서 존재·권한을 검증한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 집합 간 결합·로딩 범위를 줄이고 Service에서 존재·권한을 검증한다

ID 참조는 선택이며 탐색·참조 무결성 확인 책임을 명시적으로 관리해야 합니다.

</details>


<a id="w233"></a>
### W233. 발주 헤더와 라인을 같은 aggregate로 관리하는 이유는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=233)

1. 라인·총금액·상태 불변식을 주문 단위로 일관되게 관리하기 위해서다
2. 모든 거래처를 같은 엔티티로 합치기 위해서다
3. 모든 라인을 독립 트랜잭션으로 저장하기 위해서다
4. 권한 검사를 생략하기 위해서다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 라인·총금액·상태 불변식을 주문 단위로 일관되게 관리하기 위해서다

주문 전체가 일관된 구성·금액·생명주기 경계를 갖게 합니다.

</details>


<a id="w254"></a>
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


<a id="w271"></a>
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


<a id="w379"></a>
### W379. Partner 객체를 연관 매핑하는 starter에서 여러 발주가 하나의 거래처를 참조하는 애너테이션은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=379)

1. @OneToMany
2. @OneToOne
3. @ManyToOne
4. @ManyToMany

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · @ManyToOne

객체 연관을 사용하는 이 starter의 발주→거래처는 다대일 관계입니다. 현재 실행 엔티티는 연관 객체 대신 partnerId를 저장하는 별도 설계를 사용합니다.

</details>


<a id="w380"></a>
### W380. 현재 발주 도메인에서 취소를 허용하는 상태 집합은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=380)

1. RECEIVED·REJECTED·CANCELED
2. DRAFT·REQUESTED·APPROVED
3. DRAFT·RECEIVED·CANCELED
4. 모든 발주 상태

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · DRAFT·REQUESTED·APPROVED

PurchaseOrder.requireCancelable은 진행 중인 세 상태만 허용하며 입고 완료와 종료 상태의 취소를 막습니다.

</details>


<a id="module-05"></a>
## 5. Notice 엔티티 (TRD 3.3.7, 3.5.7)

- [선택형 학습 파일](starter/05-notice-entity/Notice.entity.java)

<a id="w028"></a>
### W028. 공지 본문에 TEXT 계열 컬럼을 사용하는 이유는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=28)

1. HTML을 자동으로 안전하게 정화하기 위해서다
2. 조회수 잠금을 자동 생성하기 위해서다
3. 공지 제목의 unique를 보장하기 위해서다
4. 긴 본문을 저장할 수 있는 컬럼 용량을 확보하기 위해서다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 긴 본문을 저장할 수 있는 컬럼 용량을 확보하기 위해서다

저장 용량과 XSS 방어는 별도로 고려해야 합니다.

</details>


<a id="w029"></a>
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


<a id="w030"></a>
### W030. 조회수의 읽기-수정-저장을 동시에 수행하면 생길 수 있는 문제는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=30)

1. 모든 요청의 증가분이 자동으로 두 배가 된다
2. 조회가 항상 무한 루프가 된다
3. 여러 요청의 증가분이 덮어써져 유실될 수 있다
4. unique 제약이 조회수를 암호화한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 여러 요청의 증가분이 덮어써져 유실될 수 있다

원자적 UPDATE view_count = view_count + 1로 증가분 유실을 방지할 수 있습니다.

</details>


<a id="w228"></a>
### W228. ItemService의 생성자 주입에 대한 설명은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=228)

1. Repository·관련 Service 등 필요한 협력 객체를 외부에서 받는다
2. 호출 때마다 비밀번호를 HTTP URL에서 읽는다
3. 필요한 Repository를 모두 static 전역으로 직접 생성한다
4. final 필드는 Spring 주입을 금지한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · Repository·관련 Service 등 필요한 협력 객체를 외부에서 받는다

RequiredArgsConstructor와 final 필드를 통해 필수 협력 객체를 주입합니다.

</details>


<a id="w381"></a>
### W381. 새 공지를 생성할 때 조회수의 초기값은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=381)

1. 1L
2. -1L
3. Long.MAX_VALUE
4. 0L

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 0L

아직 조회되지 않은 공지는 조회수 0으로 시작합니다. 조회수의 원자적 증가는 별도 조회 처리 규칙입니다.

</details>


<a id="module-06"></a>
## 6. SalesOrder 엔티티와 라인 (TRD 3.3.8, 3.6.5)

- [선택형 학습 파일](starter/06-sales-order-entity/SalesOrder.entity.java)

<a id="w031"></a>
### W031. 수주의 정상 처리 상태 흐름은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=31)

1. DRAFT → CONFIRMED → SHIPPED → COMPLETED
2. DRAFT → RECEIVED → REQUESTED → COMPLETED
3. SHIPPED → DRAFT → APPROVED
4. COMPLETED → CONFIRMED → DRAFT

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · DRAFT → CONFIRMED → SHIPPED → COMPLETED

취소 가능 상태는 별도로 관리하며 COMPLETED·CANCELED는 종료 상태입니다.

</details>


<a id="w032"></a>
### W032. 작성자와 처리자 ID가 같을 수 있는지 판단할 때의 기준은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=32)

1. 필드가 둘이면 JPA가 자동으로 동일인을 금지한다
2. 같은 ID면 무조건 관리자 권한이 생긴다
3. 처리자 필드가 있으면 소유권 검사를 생략한다
4. 역할·소유권 정책을 명시하며 ID 분리만으로 동일인을 금지하지 않는다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 역할·소유권 정책을 명시하며 ID 분리만으로 동일인을 금지하지 않는다

행위자 동일 여부는 명시적인 업무 정책이며 기술 매핑에서 자동으로 결정되지 않습니다.

</details>


<a id="w033"></a>
### W033. shippedAt과 completedAt이 초기에는 null일 수 있는 이유는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=33)

1. 모든 날짜 필드는 항상 null이어야 하기 때문이다
2. 작성자 비밀번호를 숨기기 때문이다
3. 아직 출고·완료 이벤트가 발생하지 않았기 때문이다
4. 재고 부족을 표현하는 필드이기 때문이다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 아직 출고·완료 이벤트가 발생하지 않았기 때문이다

상태 전이 시 해당 이벤트 시각을 함께 설정합니다.

</details>


<a id="w034"></a>
### W034. 수주 라인을 헤더 생명주기에 묶는 설계는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=34)

1. 각 라인을 사용자 세션에만 저장한다
2. 헤더의 라인 관계와 cascade·삭제 정책을 명시한다
3. 품목 마스터까지 헤더 삭제에 연결한다
4. 라인을 저장할 때마다 별도 수주 헤더를 생성한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 헤더의 라인 관계와 cascade·삭제 정책을 명시한다

라인은 주문 구성 요소이며 품목·거래처 마스터와 같은 삭제 정책을 쓰지 않습니다.

</details>


<a id="w180"></a>
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


<a id="w256"></a>
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


<a id="module-07"></a>
## 7. Repository 메서드 쿼리 (TRD 3.7.4, 3.8.2)

- [선택형 학습 파일](starter/07-repository/ItemRepository.java)

<a id="w035"></a>
### W035. existsByItemCode와 findByItemCode의 차이는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=35)

1. 앞은 존재 여부, 뒤는 품목 조회를 표현한다
2. 둘 다 모든 주문을 삭제한다
3. exists는 페이지 목록을 반환한다
4. find는 HTTP 경로를 등록한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 앞은 존재 여부, 뒤는 품목 조회를 표현한다

Spring Data는 메서드 이름의 접두사와 속성 조건으로 쿼리를 구성합니다.

</details>


<a id="w036"></a>
### W036. findByNameContainingOrItemCodeContaining의 조건 의미는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=36)

1. 이름과 코드가 모두 숫자여야 한다
2. 두 조건을 항상 AND로 결합한다
3. 품목을 코드 순서로 자동 삭제한다
4. 이름 또는 코드가 주어진 문자열을 포함한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 이름 또는 코드가 주어진 문자열을 포함한다

Or는 OR 결합이고 Containing은 부분 문자열 검색을 표현합니다.

</details>


<a id="w037"></a>
### W037. Pageable을 받는 Repository의 자연스러운 반환 타입은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=37)

1. HttpSession
2. PasswordEncoder
3. void만 가능
4. Page<Item>

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · Page<Item>

Page는 데이터와 총 개수·페이지 수 같은 메타 정보를 제공합니다.

</details>


<a id="w038"></a>
### W038. 카테고리 ID로 품목을 제한하는 Repository 조건은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=38)

1. category.id를 기준으로 조회 조건을 만든다
2. 품목 이름을 사용자 비밀번호와 비교한다
3. 항상 전체 품목을 반환한다
4. 카테고리 ID를 HTTP 포트로 쓴다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · category.id를 기준으로 조회 조건을 만든다

findByCategoryId 같은 속성 경로 조건 또는 Specification을 사용합니다.

</details>


<a id="w229"></a>
### W229. ItemRepository의 직접 구현 클래스가 없어도 호출 가능한 이유는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=229)

1. 브라우저가 Java 인터페이스를 실행한다
2. Spring Data가 Repository 프록시 구현을 제공한다
3. JPA가 HTML을 컴파일한다
4. 인터페이스는 항상 구현 없이 JVM에서 실행된다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · Spring Data가 Repository 프록시 구현을 제공한다

컨테이너가 등록한 런타임 프록시가 쿼리 메서드를 처리합니다.

</details>


<a id="w252"></a>
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


<a id="w273"></a>
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


<a id="module-08"></a>
## 8. 품목 등록 Service (TRD 3.8.2)

- [선택형 학습 파일](starter/08-item-register/ItemService.register.java)

<a id="w039"></a>
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


<a id="w040"></a>
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


<a id="w041"></a>
### W041. 품목 등록 시 카테고리 존재를 확인하는 주된 계층은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=41)

1. CSS
2. 브라우저 제목
3. Service
4. Gradle 캐시

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · Service

외부 입력의 형식 검증과 실제 참조 데이터 존재 확인은 구분합니다.

</details>


<a id="w042"></a>
### W042. 기본 트랜잭션 정책에서 RuntimeException이 전파되면?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=42)

1. 이미 저장한 헤더만 항상 남는다
2. 모든 checked exception도 예외 없이 자동 롤백된다
3. 정적 HTML 파일이 삭제된다
4. 해당 트랜잭션 작업이 롤백된다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 해당 트랜잭션 작업이 롤백된다

기본 롤백은 RuntimeException·Error이며 checked exception 정책은 별도 지정할 수 있습니다.

</details>


<a id="w223"></a>
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


<a id="w224"></a>
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


<a id="w382"></a>
### W382. 이미 등록된 품목코드로 신규 품목을 저장하려 할 때 현재 업무 오류 코드는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=382)

1. ITEM_NOT_FOUND
2. DUPLICATE_ITEM_CODE
3. CATEGORY_HAS_ITEMS
4. INSUFFICIENT_STOCK

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · DUPLICATE_ITEM_CODE

품목코드 중복은 DUPLICATE_ITEM_CODE로 처리합니다. 존재 여부 검사와 DB UNIQUE 제약을 함께 사용해 경쟁 저장도 보호합니다.

</details>


<a id="module-09"></a>
## 9. 품목 검색 + 페이징 (TRD 3.5.4, 2.5.4)

- [선택형 학습 파일](starter/09-item-search/ItemService.search.java)

<a id="w043"></a>
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


<a id="w044"></a>
### W044. Page<Item>을 응답 DTO 페이지로 바꾸는 방법은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=44)

1. page.toString()만 반환
2. page.map(ItemResponse::from)
3. 전체 페이지를 첫 품목 하나로 바꿈
4. 페이지마다 데이터베이스를 초기화

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · page.map(ItemResponse::from)

map은 content를 변환하면서 페이지 메타 정보를 유지합니다.

</details>


<a id="w045"></a>
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


<a id="w245"></a>
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


<a id="w277"></a>
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


<a id="w278"></a>
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


<a id="w279"></a>
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


<a id="w280"></a>
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


<a id="w281"></a>
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


<a id="w282"></a>
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


<a id="w328"></a>
### W328. Transactional(readOnly=true)에 대한 올바른 설명은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=328)

1. DB 쓰기를 모든 환경에서 무조건 금지한다
2. 인증 없는 조회를 항상 허용한다
3. 읽기 의도를 표현하고 일부 최적화를 돕지만 쓰기 차단 보안 경계는 아니다
4. 조회 결과가 자동으로 불변 객체가 된다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 읽기 의도를 표현하고 일부 최적화를 돕지만 쓰기 차단 보안 경계는 아니다

읽기 힌트·최적화 효과는 구현에 의존합니다. 수정 메서드는 쓰기 트랜잭션을 사용합니다.

</details>


<a id="w329"></a>
### W329. Page.map과 직접 PageImpl을 만드는 방식의 차이는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=329)

1. PageImpl은 JPA 엔티티를 무조건 삭제한다
2. map은 기존 메타 정보를 보존하고 직접 구성은 총 개수·Pageable을 정확히 전달해야 한다
3. map은 첫 원소만 남긴다
4. PageImpl은 HTTP 세션을 요구한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · map은 기존 메타 정보를 보존하고 직접 구성은 총 개수·Pageable을 정확히 전달해야 한다

수동 구성에서 전체 개수와 페이징 기준을 빠뜨리면 응답 메타 정보가 어긋날 수 있습니다.

</details>


<a id="module-10"></a>
## 10. 발주서 작성 Service (TRD 3.8.3, 3.10.1)

- [선택형 학습 파일](starter/10-purchase-order-create/PurchaseOrderService.create.java)

<a id="w046"></a>
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


<a id="w047"></a>
### W047. 신규 발주 라인의 기본 검증 흐름은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=47)

1. 수량만 확인하고 없는 품목도 허용한다
2. 단종 품목을 우선 선택한다
3. 품목 존재·활성 상태·양수 수량을 확인한다
4. 금액 문자열 길이만 검사한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 품목 존재·활성 상태·양수 수량을 확인한다

존재·품목 상태·수량은 서버에서 모두 확인해야 합니다.

</details>


<a id="w048"></a>
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


<a id="w049"></a>
### W049. 동시 발주번호 채번의 안전한 전략은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=49)

1. count + 1만 읽으면 항상 유일하다
2. 사용자 이름만 발주번호로 쓴다
3. 같은 번호를 허용하고 나중에 주문을 삭제한다
4. DB unique와 충돌 재시도 등 동시성 대책을 결합한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · DB unique와 충돌 재시도 등 동시성 대책을 결합한다

참조 구현은 최대 번호와 unique 충돌의 독립 트랜잭션 재시도를 사용합니다.

</details>


<a id="w050"></a>
### W050. 발주 금액 계산 책임의 필수 조건은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=50)

1. 클라이언트 금액을 그대로 신뢰한다
2. 서버에서 수량×단가와 라인 합계를 재계산한다
3. 화면에 보이면 서버에는 저장하지 않는다
4. 승인자 ID를 금액으로 사용한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 서버에서 수량×단가와 라인 합계를 재계산한다

도메인·Service 배치보다 입력 변조에도 동일한 계산 규칙이 적용되는 것이 핵심입니다.

</details>


<a id="w267"></a>
### W267. 신규 주문의 헤더를 저장하기 전에 해야 할 일은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=267)

1. 헤더만 먼저 독립 커밋하고 오류 라인을 버린다
2. 클라이언트 금액을 그대로 저장한다
3. 라인 존재·품목·수량·단가 등 입력과 업무 규칙을 검증한다
4. 검증은 상세 화면을 볼 때만 한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 라인 존재·품목·수량·단가 등 입력과 업무 규칙을 검증한다

사전 검증과 주문 전체 트랜잭션으로 부분 저장과 잘못된 주문을 방지합니다.

</details>


<a id="w383"></a>
### W383. 품목 라인이 없는 신규 발주 작성에 대응하는 현재 업무 오류 코드는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=383)

1. EMPTY_ORDER_LINES
2. ITEM_NOT_FOUND
3. INVALID_DATE_RANGE
4. DUPLICATE_ITEM_CODE

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · EMPTY_ORDER_LINES

PurchaseOrderService는 라인이 비어 있는 요청을 EMPTY_ORDER_LINES로 거부합니다. 헤더만 저장되는 부분 주문을 만들지 않습니다.

</details>


<a id="module-11"></a>
## 11. 발주 승인/반려 Service (TRD 3.8.3, 3.10.2)

- [선택형 학습 파일](starter/11-purchase-order-approval/PurchaseOrderService.approval.java)

<a id="w051"></a>
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


<a id="w052"></a>
### W052. 반려 사유가 공백이면 어떻게 처리하는가?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=52)

1. 자동으로 승인한다
2. RECEIVED로 바꾼다
3. 검증 오류로 거절하고 상태를 변경하지 않는다
4. 공백을 그대로 저장해 반려한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 검증 오류로 거절하고 상태를 변경하지 않는다

필수 사유는 DTO 및 도메인·Service 검증으로 보장합니다.

</details>


<a id="w053"></a>
### W053. approvedAt과 approverId는 언제 함께 기록하는가?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=53)

1. 발주 목록 조회마다
2. 로그아웃할 때
3. 유효한 승인 상태 전이가 성공할 때
4. 프로그램 종료할 때

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 유효한 승인 상태 전이가 성공할 때

상태와 행위자·시각이 같은 업무 처리 안에서 일관되게 변경되어야 합니다.

</details>


<a id="w054"></a>
### W054. 발주 승인·반려·입고가 허용되는 역할은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=54)

1. ADMIN 또는 MANAGER
2. USER만
3. 모든 비로그인 사용자
4. 거래처 유형이 CUSTOMER인 사용자만

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · ADMIN 또는 MANAGER

현재 참조 구현은 ADMIN과 MANAGER를 허용하며 USER는 거절합니다.

</details>


<a id="w055"></a>
### W055. 입고 상태 변경과 재고 증가를 같은 트랜잭션으로 묶는 이유는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=55)

1. 상태만 성공하고 재고 실패는 무시하기 위해서다
2. 조회수를 증가시키기 위해서다
3. 권한을 USER로 바꾸기 위해서다
4. 둘 중 하나가 실패하면 전체를 되돌리기 위해서다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 둘 중 하나가 실패하면 전체를 되돌리기 위해서다

입고 이벤트와 모든 재고 반영은 원자적인 업무 작업입니다.

</details>


<a id="w056"></a>
### W056. 같은 발주를 두 관리자가 동시에 승인하면 필요한 대응은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=56)

1. 둘 다 승인 이력을 덮어써도 무방하다
2. 버전 충돌을 감지해 중복 상태 변경을 차단한다
3. 인터넷 속도가 빠르면 충돌이 없다
4. 각 요청이 다른 DB를 초기화한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 버전 충돌을 감지해 중복 상태 변경을 차단한다

PurchaseOrder의 Version 낙관적 락이 경쟁 상태 변경을 감지합니다.

</details>


<a id="w182"></a>
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


<a id="w183"></a>
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


<a id="w184"></a>
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


<a id="w225"></a>
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


<a id="module-12"></a>
## 12. 공지사항 Service (TRD 3.8.4)

- [선택형 학습 파일](starter/12-notice-service/NoticeService.java)

<a id="w057"></a>
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


<a id="w058"></a>
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


<a id="w059"></a>
### W059. 공지 상세 조회에서 조회수를 증가시키는 설계의 주의점은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=59)

1. GET이면 DB 변경이 기술적으로 불가능하다
2. 조회수는 보안 권한을 대체한다
3. 별도 메서드로 나누면 동시성 검사가 자동 완성된다
4. 미리보기·반복 GET도 증가시킬 수 있고 원자성을 보장해야 한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 미리보기·반복 GET도 증가시킬 수 있고 원자성을 보장해야 한다

현재 구현은 상세 조회에서 원자적 UPDATE를 수행합니다. 별도 API는 다른 설계 선택입니다.

</details>


<a id="w255"></a>
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


<a id="w395"></a>
### W395. 없는 공지 ID의 삭제를 404로 정의한 API에서 알맞은 처리 흐름은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=395)

1. 존재 여부와 관계없이 항상 201을 반환한다
2. 없는 공지를 자동으로 새로 만든다
3. 기존 전체 공지를 삭제한다
4. 미존재를 NOTICE_NOT_FOUND로 처리하고 존재하는 공지를 삭제한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 미존재를 NOTICE_NOT_FOUND로 처리하고 존재하는 공지를 삭제한다

현재 공지 삭제는 대상을 조회하고 미존재를 NOTICE_NOT_FOUND로 거부합니다. 대안 exists 검사에서도 조회와 삭제 사이의 경쟁 상황을 별도로 고려합니다.

</details>


<a id="module-13"></a>
## 13. 수주서 작성 Service (TRD 3.8.5)

- [선택형 학습 파일](starter/13-sales-order-create/SalesOrderService.create.java)

<a id="w060"></a>
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


<a id="w061"></a>
### W061. 수주 확정 전 확인해야 할 조건은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=61)

1. DRAFT 상태와 권한·소유권 등 확정 정책을 검사한다
2. 수주 ID만 있으면 무조건 허용한다
3. 이미 COMPLETED이면 다시 확정한다
4. 처리자 비밀번호를 주문 금액으로 쓴다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · DRAFT 상태와 권한·소유권 등 확정 정책을 검사한다

현재 구현의 정책과 상태 가드를 함께 적용해야 합니다.

</details>


<a id="w062"></a>
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


<a id="w063"></a>
### W063. 신규 주문 라인에 단종 품목이 오면?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=63)

1. 신규 라인에 무조건 허용한다
2. 품목을 자동 삭제한다
3. ITEM_DISCONTINUED로 거절한다
4. 거래처를 관리자 계정으로 변경한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · ITEM_DISCONTINUED로 거절한다

단종은 신규 사용 제한이며 과거 라인은 보존합니다.

</details>


<a id="w186"></a>
### W186. 현재 수주 confirm을 실행할 수 있는 사용자 기준은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=186)

1. 수주 작성자
2. 아무 비로그인 사용자
3. 작성자가 아닌 USER
4. 요청 본문에 임의 ID를 넣은 사람

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 수주 작성자

SalesOrderService.confirm은 로그인과 작성자 ID를 확인합니다. ADMIN이라는 이유만으로 타인 수주 confirm을 허용하지 않습니다.

</details>


<a id="module-14"></a>
## 14. 수주 출고/완료/취소 Service (TRD 3.8.5, 3.10.4)

- [선택형 학습 파일](starter/14-sales-order-decision/SalesOrderService.decision.java)

<a id="w064"></a>
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


<a id="w065"></a>
### W065. 수주 처리자 검증의 올바른 기준은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=65)

1. 클라이언트가 보낸 managerId만 신뢰한다
2. 현재 인증 사용자의 역할과 해당 수주의 접근 정책을 확인한다
3. 매니저 문자열이 요청 본문에 있으면 허용한다
4. 주문 ID가 짝수면 권한을 부여한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 현재 인증 사용자의 역할과 해당 수주의 접근 정책을 확인한다

역할 검증과 소유권·담당 정책은 서버에서 인증 사용자 기준으로 적용합니다.

</details>


<a id="w066"></a>
### W066. shippedAt과 completedAt을 설정하는 시점은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=66)

1. 각각 출고·완료 상태 전이가 성공할 때
2. 목록 조회마다 둘 다 갱신한다
3. 수주 생성 시 둘 다 완료 시각으로 기록한다
4. 로그인 실패 시 기록한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 각각 출고·완료 상태 전이가 성공할 때

업무 이벤트 발생 시각은 그 상태 변경과 같은 작업에서 기록합니다.

</details>


<a id="w067"></a>
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


<a id="w189"></a>
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


<a id="w259"></a>
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


<a id="w363"></a>
### W363. 수주 출고 시 재고 차감을 처리하는 올바른 업무 경계는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=363)

1. 서버에서 출고 상태 변경과 모든 라인 재고 차감을 같은 트랜잭션으로 묶는다
2. 출고 상태만 먼저 커밋하고 재고 실패를 무시한다
3. 브라우저 계산값을 재고로 그대로 저장한다
4. 재고 부족이어도 항상 출고를 완료한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 서버에서 출고 상태 변경과 모든 라인 재고 차감을 같은 트랜잭션으로 묶는다

현재 Service는 출고 상태·품목 잠금·재고 차감을 조정합니다. 한 라인 실패 시 부분 출고가 남지 않게 합니다.

</details>


<a id="module-15"></a>
## 15. ErrorCode 와 응답 모델 (TRD 3.9)

- [선택형 학습 파일](starter/15-error-model/ErrorCodeAndResponse.java)

<a id="w068"></a>
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


<a id="w069"></a>
### W069. 공통 ErrorResponse의 기본 필드 조합은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=69)

1. status·code·message·timestamp
2. password·salt·sessionId·token
3. HTML·CSS·font·color
4. writerId·unitPrice·categoryId만

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · status·code·message·timestamp

공통 응답은 결과·오류 식별자·메시지·발생 시각으로 구성합니다.

</details>


<a id="w070"></a>
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


<a id="w330"></a>
### W330. BusinessException을 RuntimeException으로 정의하는 효과는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=330)

1. 예외가 전혀 발생하지 않게 된다
2. 모든 SQL 오류를 성공으로 바꾼다
3. 항상 스택 트레이스를 사용자에게 공개한다
4. 기본 트랜잭션 롤백 대상이 되며 호출부의 checked 예외 선언을 요구하지 않는다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 기본 트랜잭션 롤백 대상이 되며 호출부의 checked 예외 선언을 요구하지 않는다

업무 오류 계약을 예외 처리기로 전달하며 트랜잭션 기본 롤백 정책과 맞춥니다.

</details>


<a id="w331"></a>
### W331. LocalDateTime을 서로 다른 시간대 시스템 사이에서 사용할 때 주의할 점은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=331)

1. 시간대·오프셋이 없어 실제 시점을 해석하는 기준을 정해야 한다
2. UTC 정보가 항상 자동 포함된다
3. 모든 날짜가 같은 실제 시점을 뜻한다
4. JSON으로 보내면 시간대가 항상 자동 복원된다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 시간대·오프셋이 없어 실제 시점을 해석하는 기준을 정해야 한다

이벤트의 절대 시점을 전달할 때 Instant·오프셋 또는 명시한 시간대 정책을 고려합니다.

</details>


<a id="w384"></a>
### W384. 현재 ErrorResponse에서 오류 발생 시각을 담는 timestamp 필드의 Java 타입은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=384)

1. LocalDate
2. Duration
3. LocalDateTime
4. Period

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · LocalDateTime

ErrorResponse는 LocalDateTime.now()로 timestamp를 생성합니다. 시간대가 필요한 외부 시스템 연동에서는 별도의 시점 표현 정책을 정합니다.

</details>


<a id="module-16"></a>
## 16. GlobalExceptionHandler (TRD 3.9)

- [선택형 학습 파일](starter/16-global-handler/GlobalExceptionHandler.java)

<a id="w071"></a>
### W071. RestControllerAdvice의 역할은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=71)

1. 모든 엔티티를 자동 삭제한다
2. HTML의 CSS를 컴파일한다
3. 세션 쿠키를 DB 기본키로 바꾼다
4. 여러 API 컨트롤러의 예외를 공통 응답으로 처리한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 여러 API 컨트롤러의 예외를 공통 응답으로 처리한다

ExceptionHandler와 함께 API 예외 처리 책임을 모읍니다.

</details>


<a id="w072"></a>
### W072. 업무·입력 검증·예상 밖 예외를 구분하는 이유는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=72)

1. 모든 예외의 스택을 사용자에게 공개한다
2. 검증 실패를 성공으로 응답한다
3. 사용자 오류와 내부 오류를 서로 맞는 계약으로 응답한다
4. 업무 예외가 나면 테스트를 삭제한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 사용자 오류와 내부 오류를 서로 맞는 계약으로 응답한다

알려진 ErrorCode와 검증 메시지를 처리하고 내부 오류 정보는 노출을 제한합니다.

</details>


<a id="w073"></a>
### W073. DTO 검증 오류에서 필드별 메시지를 모으는 방법은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=73)

1. 비밀번호 해시를 메시지로 출력한다
2. JSESSIONID를 오류 코드로 쓴다
3. 모든 필드를 SQL 문자열로 연결한다
4. BindingResult의 FieldError를 순회해 필드와 메시지를 추출한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · BindingResult의 FieldError를 순회해 필드와 메시지를 추출한다

검증 예외의 바인딩 결과에 필드 이름과 기본 메시지가 들어 있습니다.

</details>


<a id="w275"></a>
### W275. 필수 DTO 입력 검증 실패의 현재 공통 업무 코드는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=275)

1. ACCESS_DENIED
2. INVALID_INPUT
3. ITEM_NOT_FOUND
4. INTERNAL_ERROR

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · INVALID_INPUT

현재 API 예외 처리기는 입력 검증 실패를 400과 INVALID_INPUT으로 전달합니다.

</details>


<a id="w332"></a>
### W332. RestControllerAdvice와 ControllerAdvice의 차이는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=332)

1. 후자는 예외를 처리할 수 없다
2. 전자는 Repository만 처리한다
3. 전자는 ResponseBody 의미를 포함하며 후자는 View 등 다른 응답 방식도 사용할 수 있다
4. 둘 다 DB 스키마를 생성한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 전자는 ResponseBody 의미를 포함하며 후자는 View 등 다른 응답 방식도 사용할 수 있다

응답 표현과 적용 대상에 맞춰 API·Web 예외 처리기를 구분합니다.

</details>


<a id="w333"></a>
### W333. 예상 밖 예외의 상세 메시지 공개가 위험한 이유는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=333)

1. 모든 메시지가 자동으로 BCrypt 해시가 되기 때문이다
2. 오류 메시지가 DTO 검증을 대신하기 때문이다
3. 내부 구조·SQL·경로·비밀정보가 노출될 수 있다
4. 세션 ID를 공개해야만 오류를 진단할 수 있기 때문이다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 내부 구조·SQL·경로·비밀정보가 노출될 수 있다

사용자에게 안정적인 오류 계약을 응답하고 서버에서 필요한 진단 정보를 관리합니다.

</details>


<a id="w334"></a>
### W334. 컨트롤러 그룹별로 Advice를 나누는 방법은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=334)

1. basePackages·annotations 등 적용 범위를 명시하고 우선순위를 검토한다
2. 어노테이션 이름만 다르게 바꾼다
3. 같은 타입의 핸들러를 무제한 중복 선언한다
4. 모든 컨트롤러가 직접 스택을 출력한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · basePackages·annotations 등 적용 범위를 명시하고 우선순위를 검토한다

예외 처리 대상·응답 형식·Advice 간 적용 순서를 일관되게 구성합니다.

</details>


<a id="w385"></a>
### W385. 예외 처리기에서 HTTP 상태와 오류 DTO 본문을 함께 구성하는 반환 타입은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=385)

1. HttpSession
2. Pageable
3. ResponseEntity<ErrorResponse>
4. EntityManager

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · ResponseEntity<ErrorResponse>

ResponseEntity.status(...).body(...)로 상태와 본문을 지정할 수 있습니다. 도메인 예외의 HTTP 변환은 API 처리기의 책임입니다.

</details>


<a id="w386"></a>
### W386. 현재 ApiExceptionHandler가 예상 밖 서버 예외를 기록하는 로그 수준은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=386)

1. TRACE
2. ERROR
3. DEBUG
4. INFO

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · ERROR

예상 밖 예외는 log.error로 요청 경로와 예외 증거를 남깁니다. 클라이언트 응답에는 내부 상세를 노출하지 않습니다.

</details>


<a id="module-17"></a>
## 17. DTO 검증 (TRD 3.7, 비기능 검증)

- [선택형 학습 파일](starter/17-dto-validation/ItemCreateRequest.java)

<a id="w074"></a>
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


<a id="w075"></a>
### W075. MVC 요청 DTO의 Valid 검증은 주로 언제 수행되는가?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=75)

1. CSS를 읽는 순간 수행된다
2. 컨트롤러 인자 바인딩·해석 과정에서 수행된다
3. DB 백업할 때만 수행된다
4. 모든 Repository 메서드가 무조건 자동 실행한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 컨트롤러 인자 바인딩·해석 과정에서 수행된다

다른 호출 경로에는 Service 검증 등 추가 방어가 필요할 수 있습니다.

</details>


<a id="w076"></a>
### W076. 요청 DTO와 응답 DTO를 분리하는 이유는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=76)

1. 모든 응답에 비밀번호를 넣기 위해서다
2. DTO가 DB 잠금을 대체하기 때문이다
3. 입력 허용 필드와 출력 공개 필드의 계약이 다르기 때문이다
4. JPA 엔티티를 두 번 저장하기 위해서다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 입력 허용 필드와 출력 공개 필드의 계약이 다르기 때문이다

각 경계에 필요한 데이터와 검증 정책을 독립적으로 관리합니다.

</details>


<a id="w327"></a>
### W327. MVC 요청 DTO에서 Valid가 빠지면 생길 수 있는 문제는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=327)

1. 해당 입력 경계의 Bean Validation이 실행되지 않아 잘못된 값이 다음 계층으로 갈 수 있다
2. DB unique가 자동 삭제된다
3. 모든 입력이 무조건 404가 된다
4. 모든 Service 검증도 자동으로 제거된다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 해당 입력 경계의 Bean Validation이 실행되지 않아 잘못된 값이 다음 계층으로 갈 수 있다

Valid의 호출 지점과 별도로 Service·도메인 검증이 있는지 확인해야 합니다.

</details>


<a id="w335"></a>
### W335. NotNull·NotEmpty·NotBlank의 차이는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=335)

1. 세 가지가 모두 동일한 숫자 범위 검사다
2. NotNull은 빈 문자열도 항상 거절한다
3. NotEmpty는 공백 문자열을 항상 거절한다
4. null 금지·null과 빈 값 금지·문자열 공백까지 금지

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · null 금지·null과 빈 값 금지·문자열 공백까지 금지

문자열·컬렉션 등 적용 타입과 공백 처리 조건을 구분합니다.

</details>


<a id="w387"></a>
### W387. BigDecimal 단가에서 0을 허용하고 음수만 금지하는 제약은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=387)

1. @Positive
2. @NotBlank
3. @Past
4. @DecimalMin("0.00")

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · @DecimalMin("0.00")

@DecimalMin의 기본 inclusive=true는 0.00도 허용합니다. 필수 입력이면 @NotNull과 함께 사용하고 자릿수도 별도로 제한합니다.

</details>


<a id="module-18"></a>
## 18. ItemController (TRD 3.7.4)

- [선택형 학습 파일](starter/18-controller-item/ItemController.java)

<a id="w077"></a>
### W077. 품목 REST API의 기본 HTTP 메서드 조합은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=77)

1. 조회 POST·등록 GET·수정 TRACE
2. 모든 기능을 GET만으로 처리
3. 모든 기능을 OPTIONS만으로 처리
4. 조회 GET·등록 POST·전체 수정 PUT

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 조회 GET·등록 POST·전체 수정 PUT

리소스 경로와 행위에 맞는 HTTP 계약을 정합니다. 부분 수정은 PATCH를 고려합니다.

</details>


<a id="w078"></a>
### W078. PathVariable·RequestBody·RequestParam의 입력 위치는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=78)

1. 경로 값·요청 본문·쿼리/폼 파라미터
2. 비밀번호·CSS·DB 스키마
3. 모두 HTTP 응답 본문
4. 모두 서버 환경 변수

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 경로 값·요청 본문·쿼리/폼 파라미터

서로 다른 요청 데이터 위치를 컨트롤러 파라미터에 바인딩합니다.

</details>


<a id="w079"></a>
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


<a id="w250"></a>
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


<a id="w336"></a>
### W336. 모든 수정 필드가 nullable일 때 필요한 입력 정책은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=336)

1. null이면 항상 DB 전체를 삭제한다
2. 필드 누락과 명시적 null·삭제 의도를 구분하고 수정 의미를 정한다
3. 모든 입력 누락을 무조건 200으로 처리한다
4. nullable이면 업무 검증이 필요 없다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 필드 누락과 명시적 null·삭제 의도를 구분하고 수정 의미를 정한다

전체 교체와 부분 변경 계약을 구분하고 필요한 값의 검증을 명시합니다.

</details>


<a id="w337"></a>
### W337. 전달된 선택 필드만 바꾸는 수정 DTO는 어떤 계약에 가까운가?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=337)

1. GET 조회
2. PATCH 부분 변경
3. HEAD 조회
4. TRACE 진단

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · PATCH 부분 변경

이 문제는 선택 필드만 변경하는 정책을 전제로 하며 DTO의 nullable 여부만으로 PUT·PATCH가 결정되지는 않습니다.

</details>


<a id="module-19"></a>
## 19. PurchaseOrderController (TRD 3.7.5)

- [선택형 학습 파일](starter/19-controller-purchase/PurchaseOrderController.java)

<a id="w080"></a>
### W080. 일반 발주 API와 관리자 API 경로를 나누어도 필요한 것은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=80)

1. 경로 이름만 있으면 누구나 접근해도 된다
2. 관리자 경로에서 실제 역할 인가를 수행한다
3. 관리자 API는 세션을 제거한다
4. 모든 API에 같은 작성자 ID를 저장한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 관리자 경로에서 실제 역할 인가를 수행한다

경로 분리는 계약을 명확히 하지만 실행 권한은 서버 검사로 보장합니다.

</details>


<a id="w081"></a>
### W081. approve 동작을 별도 PATCH 경로로 표현할 때의 특징은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=81)

1. PATCH이면 DB 상태가 자동 검증된다
2. PATCH는 응답 본문을 가질 수 없다
3. 업무 명령을 명확히 표현하지만 상태 전이·중복 요청 정책이 필요하다
4. URL만으로 낙관적 락이 생성된다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 업무 명령을 명확히 표현하지만 상태 전이·중복 요청 정책이 필요하다

행위 중심 경로의 편의와 상태·멱등 정책을 함께 설계합니다.

</details>


<a id="w082"></a>
### W082. 현재 프로젝트에서 로그인 사용자 정보를 받는 방식은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=82)

1. CurrentUser ArgumentResolver가 LoginUser를 주입한다
2. 요청 본문의 userId를 인증 결과로 신뢰한다
3. Thymeleaf가 DB에서 비밀번호를 공개한다
4. Principal을 사용하면 항상 현재 세션 구현과 동일하다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · CurrentUser ArgumentResolver가 LoginUser를 주입한다

현재 인증 구조는 세션과 LoginUser·CurrentUser이며 다른 인증 방식의 인자와 구분해야 합니다.

</details>


<a id="w388"></a>
### W388. API 발주 반려 요청의 사유를 JSON DTO로 읽는 컨트롤러 인자 애너테이션은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=388)

1. @RequestBody
2. @PathVariable
3. @RequestHeader
4. @CookieValue

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · @RequestBody

반려 사유 DTO는 JSON 본문에서 읽고 @Valid 등으로 입력을 검증합니다. 발주 ID는 별도의 경로 변수입니다.

</details>


<a id="module-20"></a>
## 20. 보안 흐름 (TRD 3.11)

- [선택형 학습 파일](starter/20-security-session/SecurityFlow.md)

<a id="w083"></a>
### W083. 세션 로그인 흐름의 올바른 순서는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=83)

1. 세션에 평문 비밀번호 저장·검증 생략
2. 계정 조회·비밀번호 검증·세션 ID 변경·로그인 정보 저장
3. 로그아웃 후 승인자 ID를 로그인 ID로 사용
4. 브라우저가 역할을 선택하면 서버가 그대로 승인

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 계정 조회·비밀번호 검증·세션 ID 변경·로그인 정보 저장

서버가 인증한 정보만 세션에 보관하고 세션 고정 공격을 방지합니다.

</details>


<a id="w084"></a>
### W084. 현재 보안 책임 분담은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=84)

1. Security가 모든 세션 사용자 인가를 자동으로 맡는다
2. Thymeleaf만 모든 인가를 수행한다
3. Repository가 세션 쿠키를 발급한다
4. Interceptor·Service는 인증/인가, Security는 CSRF·헤더를 맡는다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · Interceptor·Service는 인증/인가, Security는 CSRF·헤더를 맡는다

현재 참조 구현과 Spring Security가 전체 인증을 맡는 대안 설계를 구분합니다.

</details>


<a id="w085"></a>
### W085. 인증·인가를 Spring Security로 옮길 때 필요한 정리는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=85)

1. 기존 검사와 새 검사를 목적 없이 모두 추가한다
2. 기존 세션 인터셉터와 사용자 해석의 중복 책임을 재설계한다
3. Service의 업무 소유권 검사도 전부 삭제한다
4. 모든 API를 permitAll로 바꾼다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 기존 세션 인터셉터와 사용자 해석의 중복 책임을 재설계한다

새 인증 원천과 기존 계약을 맞추면서 필요한 Service 업무 인가는 유지합니다.

</details>


<a id="w086"></a>
### W086. 서버 세션을 사용하지 않는 JWT 인증의 특징은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=86)

1. 토큰이 있으면 서명과 만료 검사가 필요 없다
2. 로그아웃 시 모든 복제 토큰이 자동 무효화된다
3. 매 요청 토큰을 검증하고 폐기·권한 변경 정책을 별도로 설계한다
4. payload에 비밀번호를 평문으로 넣어야 한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 매 요청 토큰을 검증하고 폐기·권한 변경 정책을 별도로 설계한다

무상태 인증이라도 키 관리·만료·재사용·폐기 문제를 해결해야 합니다.

</details>


<a id="w173"></a>
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


<a id="w175"></a>
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


<a id="w190"></a>
### W190. HttpOnly·Secure·SameSite의 역할 조합은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=190)

1. DB 암호화·페이징·정렬
2. JWT 복호화·트랜잭션·캐시 삭제
3. 로그인 우회·관리자 부여·조회수 증가
4. JS 쿠키 접근 제한·HTTPS 전송 제한·교차 사이트 전송 제어

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · JS 쿠키 접근 제한·HTTPS 전송 제한·교차 사이트 전송 제어

각 쿠키 속성은 다른 공격 경로를 줄입니다. HTTPS 설정과 SameSite 정책은 사용 흐름에 맞춰 정합니다.

</details>


<a id="w191"></a>
### W191. 세션 기반 인증을 여러 서버로 확장할 때 필요한 고려는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=191)

1. 세션 공유 저장소나 라우팅 전략과 장애·운영 비용
2. 각 서버가 세션을 따로 갖더라도 모든 요청이 항상 인증된다
3. DB unique만 추가하면 세션이 자동 공유된다
4. 사용자를 모두 로그아웃시키면 확장 문제가 사라진다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 세션 공유 저장소나 라우팅 전략과 장애·운영 비용

서버별 메모리 세션은 요청 서버가 달라지면 인증 상태를 찾지 못할 수 있습니다.

</details>


<a id="w192"></a>
### W192. 독립 쿠키 저장소를 쓰는 두 브라우저에서 같은 사용자가 로그인하면?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=192)

1. 반드시 같은 세션 ID가 발급된다
2. 사용자가 같으면 쿠키를 주고받는다
3. 보통 각각 별도 세션이 만들어진다
4. 두 번째 브라우저는 JPA 엔티티가 된다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 보통 각각 별도 세션이 만들어진다

특별한 동시 세션 제한 정책이 없다면 브라우저별 쿠키·서버 세션이 독립적입니다.

</details>


<a id="w193"></a>
### W193. 세션을 Redis에 저장하는 장점과 비용은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=193)

1. 별도 저장소 없이 세션이 자동 공유된다
2. 모든 CSRF 검사가 불필요해진다
3. 여러 서버가 세션을 공유하지만 저장소 운영·네트워크 비용이 생긴다
4. 비밀번호 해시를 저장할 필요가 없어진다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 여러 서버가 세션을 공유하지만 저장소 운영·네트워크 비용이 생긴다

중앙 세션 저장소는 확장을 돕지만 가용성·TTL·네트워크·장애 대응을 요구합니다.

</details>


<a id="w194"></a>
### W194. 기본 역할 접두사에서 hasRole('ADMIN')과 hasAuthority('ROLE_ADMIN')은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=194)

1. 같은 권한 문자열을 검사한다
2. hasRole은 USER만 검사한다
3. hasAuthority는 로그인이 필요 없다
4. 두 표현식이 모두 비밀번호를 검사한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 같은 권한 문자열을 검사한다

기본 ROLE_ 접두사를 전제로 합니다. 커스텀 접두사 정책이면 기준을 다시 확인해야 합니다.

</details>


<a id="w195"></a>
### W195. CSRF 보호 생략 여부를 판단할 올바른 기준은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=195)

1. REST API라는 이름이면 무조건 꺼도 된다
2. 브라우저가 인증 정보를 자동 첨부하는 방식과 공격 경로를 확인한다
3. 쿠키 기반 로그인에는 CSRF 위험이 없다
4. JWT를 쿠키에 넣으면 항상 CSRF가 사라진다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 브라우저가 인증 정보를 자동 첨부하는 방식과 공격 경로를 확인한다

쿠키·HTTP 인증 등의 자동 전송 여부와 실제 인증 방식을 기준으로 판단합니다.

</details>


<a id="w196"></a>
### W196. PreAuthorize 사용을 위해 필요한 대표 설정은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=196)

1. EnableJpaAuditing만
2. EntityScan만
3. NotBlank만
4. EnableMethodSecurity

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · EnableMethodSecurity

어노테이션을 붙였다는 사실만으로 메서드 보안이 활성화되지는 않습니다.

</details>


<a id="w197"></a>
### W197. 일반적인 JWT 인증 클레임 조합은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=197)

1. 평문 password·DB root 비밀번호
2. sub·exp와 필요한 역할 정보
3. CSS 색상·글꼴·이미지 주소만
4. 모든 JPA 엔티티 전체

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · sub·exp와 필요한 역할 정보

payload는 인코딩된 것이며 비밀 저장소가 아닙니다. 서명·발급자·대상·만료도 검증해야 합니다.

</details>


<a id="w198"></a>
### W198. AccessToken의 만료 시간을 짧게 두는 주된 목적은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=198)

1. 유출 토큰이 사용될 수 있는 시간을 줄인다
2. 토큰 서명 검사를 없앤다
3. 즉시 폐기를 별도 정책 없이 보장한다
4. 모든 권한 변경을 자동 반영한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 유출 토큰이 사용될 수 있는 시간을 줄인다

짧은 TTL은 노출 기간을 줄이지만 즉시 폐기와 갱신 정책은 별도로 설계해야 합니다.

</details>


<a id="w199"></a>
### W199. RefreshToken 도입 시 추가로 다룰 위험은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=199)

1. JPA 기본키 길이만
2. Thymeleaf 글꼴만
3. SQL 정렬 오름차순만
4. 장기 토큰 탈취·재사용과 안전한 회전·폐기

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 장기 토큰 탈취·재사용과 안전한 회전·폐기

갱신 토큰은 수명이 길 수 있어 저장 보호·회전·재사용 감지·폐기 정책이 중요합니다.

</details>


<a id="w200"></a>
### W200. 토큰 서명 비밀키를 소스와 분리하는 이유는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=200)

1. 환경 변수면 모든 공격을 자동 방어한다
2. 키가 공개되어도 서명이 항상 안전하다
3. 저장소 노출을 줄이고 환경별 관리·회전을 가능하게 한다
4. payload를 CSS로 바꾸기 위해서다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 저장소 노출을 줄이고 환경별 관리·회전을 가능하게 한다

환경 변수 또는 비밀 저장소를 사용하되 로그·권한·배포 노출도 관리해야 합니다.

</details>


<a id="w201"></a>
### W201. JWT에 담긴 역할 변경을 즉시 반영하려면?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=201)

1. 매 요청 권한 조회나 토큰 버전·폐기 등 별도 정책이 필요하다
2. DB 역할을 바꾸면 발급된 JWT 내용도 자동 바뀐다
3. exp만 삭제하면 즉시 반영된다
4. payload의 문자열을 브라우저가 수정해 보내면 된다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 매 요청 권한 조회나 토큰 버전·폐기 등 별도 정책이 필요하다

서명된 기존 토큰은 재발급·만료 또는 서버 확인 정책 없이는 그대로 유효할 수 있습니다.

</details>


<a id="w202"></a>
### W202. 모바일 API에서 Authorization Bearer 토큰을 쓰는 특징은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=202)

1. 토큰이 있으면 HTTPS가 필요 없다
2. 모바일이면 만료 검사를 생략한다
3. 명시 헤더로 전송하고 기기 저장·유출 위험을 따로 관리한다
4. 쿠키는 모든 모바일 환경에서 기술적으로 금지된다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 명시 헤더로 전송하고 기기 저장·유출 위험을 따로 관리한다

전송 편의와 안전한 저장·수명·갱신 정책을 구분해 판단합니다.

</details>


<a id="w272"></a>
### W272. 세션 고정 공격의 전형적인 흐름은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=272)

1. 공격자가 아는 세션 ID를 피해자가 인증 후에도 쓰게 한다
2. DB unique로 중복 품목을 차단한다
3. 승인 시 Version이 증가한다
4. Thymeleaf가 문자열을 escape한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 공격자가 아는 세션 ID를 피해자가 인증 후에도 쓰게 한다

로그인 성공 시 세션 ID를 변경해 인증 전 ID의 재사용 위험을 줄입니다.

</details>


<a id="module-21"></a>
## 21. 통합 테스트 흐름 (TRD 3.12)

- [선택형 학습 파일](starter/21-test-flow/PurchaseFlowTest.java)

<a id="w087"></a>
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


<a id="w088"></a>
### W088. 다음 요청에 필요한 생성 ID는 어떻게 얻는가?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=88)

1. 응답 JSON을 파싱해 ID 필드를 추출한다
2. 현재 시각을 무조건 ID로 사용한다
3. DB 전체 데이터를 삭제해 ID를 고정한다
4. 비밀번호 해시를 ID로 쓴다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 응답 JSON을 파싱해 ID 필드를 추출한다

실제 응답 계약을 따라 ID를 추출해야 테스트가 생성 데이터와 연결됩니다.

</details>


<a id="w089"></a>
### W089. USER가 관리자 발주 API를 호출하는 테스트의 기대는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=89)

1. 모든 요청에 200을 기대한다
2. 관리자 세션으로 바꾸고 USER 테스트라고 부른다
3. 권한 실패 시 발주 상태가 변경되어야 한다
4. 인증·CSRF 조건을 갖춘 요청에서도 403을 확인한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 인증·CSRF 조건을 갖춘 요청에서도 403을 확인한다

권한 실패와 CSRF 실패를 구분해 인가 경계를 검증합니다.

</details>


<a id="w232"></a>
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


<a id="w283"></a>
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


<a id="w284"></a>
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


<a id="w285"></a>
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


<a id="w286"></a>
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


<a id="w287"></a>
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


<a id="w288"></a>
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


<a id="w289"></a>
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


<a id="w290"></a>
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


<a id="w291"></a>
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


<a id="w292"></a>
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


<a id="w293"></a>
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


<a id="w294"></a>
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


<a id="w295"></a>
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


<a id="w296"></a>
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


<a id="w297"></a>
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


<a id="w298"></a>
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


<a id="w299"></a>
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


<a id="w300"></a>
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


<a id="w301"></a>
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


<a id="w302"></a>
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


<a id="w303"></a>
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


<a id="w304"></a>
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


<a id="w305"></a>
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


<a id="w306"></a>
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


<a id="w307"></a>
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


<a id="w308"></a>
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


<a id="w309"></a>
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


<a id="w310"></a>
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


<a id="w311"></a>
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


<a id="w312"></a>
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


<a id="w313"></a>
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


<a id="w314"></a>
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


<a id="w315"></a>
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


<a id="w316"></a>
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


<a id="w317"></a>
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


<a id="w318"></a>
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


<a id="w319"></a>
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


<a id="w320"></a>
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


<a id="w321"></a>
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


<a id="w322"></a>
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


<a id="w338"></a>
### W338. 기본 Spring 트랜잭션 테스트의 종료 동작에 대한 설명은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=338)

1. 모든 서버 요청과 모든 스레드의 변경이 무조건 함께 롤백된다
2. 테스트의 DB 변경은 항상 영구 커밋된다
3. Transactional이 assertion을 대신한다
4. 테스트 트랜잭션은 기본 롤백되지만 다른 스레드·독립 트랜잭션은 별도 범위다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 테스트 트랜잭션은 기본 롤백되지만 다른 스레드·독립 트랜잭션은 별도 범위다

테스트 격리 범위와 실제 서버·비동기·REQUIRES_NEW 트랜잭션을 구분해야 합니다.

</details>


<a id="w339"></a>
### W339. MockMvc와 WebTestClient를 비교한 올바른 설명은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=339)

1. MockMvc는 반드시 외부 MySQL을 요구한다
2. WebTestClient는 JSON을 검사할 수 없다
3. MockMvc는 MVC 테스트 환경, WebTestClient는 HTTP 클라이언트 방식이며 지원 서버·바인딩 방식이 다르다
4. 둘 다 Java 소스 컴파일러다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · MockMvc는 MVC 테스트 환경, WebTestClient는 HTTP 클라이언트 방식이며 지원 서버·바인딩 방식이 다르다

현재 프로젝트는 MockMvc 중심이며 테스트 목적과 서버 기동 방식에 맞춰 도구를 선택합니다.

</details>


<a id="w340"></a>
### W340. JsonPath와 ObjectMapper로 응답을 검사하는 방식의 차이는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=340)

1. 경로 기반 필드 추출과 구조·타입 기반 역직렬화의 차이다
2. 둘 다 비밀번호를 복호화한다
3. JsonPath는 모든 데이터를 DB에 저장한다
4. ObjectMapper는 HTTP 역할을 자동 검사한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 경로 기반 필드 추출과 구조·타입 기반 역직렬화의 차이다

간단한 필드 추출과 DTO 전체 계약 검증 중 목적에 맞는 방식을 사용합니다.

</details>


<a id="w389"></a>
### W389. 현재 발주 생성 API가 정상적으로 새 주문을 만들었을 때 HTTP 상태는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=389)

1. 200 OK
2. 204 No Content
3. 201 Created
4. 403 Forbidden

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 201 Created

새 발주 생성은 201을 반환하고 생성 ID를 제공해 이후 조회·상태 변경 테스트가 사용할 수 있게 합니다.

</details>


<a id="module-22"></a>
## 22. 문서화 (TRD 3.15 ~ 3.17)

- [선택형 학습 파일](starter/22-documentation/PortfolioDocs.md)

<a id="w090"></a>
### W090. README에 포함할 내용의 올바른 묶음은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=90)

1. 비밀번호 해시와 운영 비밀키만 담는다
2. 기능 이름 한 단어만 담는다
3. 목적·기술·기능·구조·DB·API·실행 등 독자의 시작 정보를 담는다
4. 실행 명령을 모두 제거한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 목적·기술·기능·구조·DB·API·실행 등 독자의 시작 정보를 담는다

개요와 실제로 재현 가능한 실행 절차·문서 연결이 필요합니다.

</details>


<a id="w091"></a>
### W091. ERD 표기 도구를 선택하는 기준은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=91)

1. 갱신·diff·공유·시각 전달 요구를 함께 고려한다
2. 가장 유료인 도구를 무조건 고른다
3. 표기 도구가 DB 무결성을 자동 보장한다
4. 도구 이름만 쓰고 관계를 생략한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 갱신·diff·공유·시각 전달 요구를 함께 고려한다

텍스트와 시각 도구에는 유지 비용과 표현력의 차이가 있습니다.

</details>


<a id="w092"></a>
### W092. 트러블슈팅 기록의 기본 순서는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=92)

1. 해결 → 비밀번호 → CSS → 주문번호
2. 문제 → 원인 → 해결 → 배운 점
3. 기술 이름만 나열
4. 실패 기록을 모두 삭제

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 문제 → 원인 → 해결 → 배운 점

재현 조건·원인·해결 및 검증을 남겨 같은 문제를 다시 분석할 수 있게 합니다.

</details>


<a id="w093"></a>
### W093. Markdown과 자동 생성 API 문서의 올바른 비교는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=93)

1. 자동 문서는 모든 업무 정책을 완벽히 작성한다
2. 수동 문서는 설명·diff에 유리하고 자동 문서도 의미·정책 검토가 필요하다
3. Markdown이면 코드와 절대 어긋나지 않는다
4. 둘 다 인증 조건을 적으면 안 된다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 수동 문서는 설명·diff에 유리하고 자동 문서도 의미·정책 검토가 필요하다

어떤 방식이든 실제 입력·응답·권한·오류 계약과 동기화해야 합니다.

</details>


<a id="w236"></a>
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


<a id="w237"></a>
### W237. ERD를 실제 구현과 맞출 때 필요한 것은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=237)

1. 그림 색만 맞춘다
2. 모든 관계를 무조건 OneToOne으로 표시한다
3. 엔티티·마이그레이션의 키·관계·nullable·제약을 대조한다
4. 모든 SQL 파일을 삭제한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 엔티티·마이그레이션의 키·관계·nullable·제약을 대조한다

업무 관계와 실제 JPA 매핑·DB 외래키는 구분해 정확히 표시합니다.

</details>


<a id="w238"></a>
### W238. 현재 API 인증 조건을 문서에 쓸 때 올바른 방식은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=238)

1. 모든 API가 JWT Bearer라고 가정한다
2. 운영 비밀키를 문서에 복사한다
3. 관리자 API라는 URL 이름만 적는다
4. 세션 쿠키·로그인·역할과 현재 CSRF 정책을 명시한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 세션 쿠키·로그인·역할과 현재 CSRF 정책을 명시한다

현재 참조 구현은 세션 기반이므로 인증 방식과 예외 정책을 실제 코드와 맞춥니다.

</details>


<a id="w239"></a>
### W239. 텍스트 ERD와 시각 ERD를 함께 사용할 때의 올바른 설명은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=239)

1. 그림을 만들면 DB 스키마가 자동 변경된다
2. 텍스트는 diff·갱신, 시각 자료는 관계 전달에 유리하지만 동기화가 필요하다
3. 텍스트 ERD에는 기본키를 쓸 수 없다
4. 시각 ERD는 관계 검증이 불필요하다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 텍스트는 diff·갱신, 시각 자료는 관계 전달에 유리하지만 동기화가 필요하다

도구에 대한 취향보다 변경 추적과 독자의 이해 요구를 기준으로 판단합니다.

</details>


<a id="w265"></a>
### W265. README의 화면 캡처·데모 자료가 해야 할 역할은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=265)

1. 실제 구현과 재현 가능한 기능 흐름을 보여 준다
2. 구현하지 않은 기능을 완성했다고 보여 준다
3. 운영 비밀키와 비밀번호 해시를 공개한다
4. 오류 화면을 숨기기만 한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 실제 구현과 재현 가능한 기능 흐름을 보여 준다

시연 자료는 현재 기능과 실행 방법을 독자가 확인하는 증거입니다.

</details>


<a id="w274"></a>
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


<a id="module-23"></a>
## 23. 인증 — 로그인 / 로그아웃 / 내 정보 / 비밀번호 변경 (PRD 2.5.1, TRD 3.7.1, 3.11)

- [선택형 학습 파일](starter/23-auth-login/AuthService.login.java)
- [선택형 학습 파일](starter/23-auth-login/AuthController.java)

<a id="w094"></a>
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


<a id="w095"></a>
### W095. 세션에 저장할 로그인 정보의 올바른 예는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=95)

1. 평문 비밀번호와 DB root 비밀번호
2. 클라이언트가 보낸 임의의 ADMIN 문자열
3. 서버가 검증한 사용자 ID·이름·역할의 LoginUser
4. 모든 엔티티와 연관관계 전체

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 서버가 검증한 사용자 ID·이름·역할의 LoginUser

인증된 사용자 정보만 최소한으로 보관합니다.

</details>


<a id="w096"></a>
### W096. 세션 고정 공격을 줄이기 위한 ID 변경 시점은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=96)

1. 로그인 인증이 성공할 때
2. 비밀번호 검증 전에 항상 관리자 권한을 부여할 때
3. 발주 입고 후에만
4. 프로그램 종료 후

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 로그인 인증이 성공할 때

인증 전 알려진 세션 ID가 인증 후에도 유지되는 것을 방지합니다.

</details>


<a id="w097"></a>
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


<a id="w098"></a>
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


<a id="w172"></a>
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


<a id="w174"></a>
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


<a id="w176"></a>
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


<a id="w178"></a>
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


<a id="w260"></a>
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


<a id="w276"></a>
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


<a id="w341"></a>
### W341. Spring Security가 인증을 담당하는 설계에서 로그인 진입점을 공개해야 하는 이유는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=341)

1. 로그인 응답에 비밀번호를 공개하기 위해서다
2. 모든 관리자 업무 API도 공개해야 하기 때문이다
3. 세션 ID 고정 공격을 허용하기 위해서다
4. 인증 전 사용자가 자격 증명을 제출할 수 있어야 하기 때문이다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 인증 전 사용자가 자격 증명을 제출할 수 있어야 하기 때문이다

permitAll은 로그인 진입점 접근 허용이며 자격 증명 검증과 업무 API 인가를 생략한다는 뜻이 아닙니다.

</details>


<a id="w342"></a>
### W342. 응답의 사용자 ID 노출과 IDOR 방어의 관계는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=342)

1. ID를 반환하면 모든 API가 반드시 취약하다
2. ID를 숨기는 것보다 대상별 서버 소유권·권한 검사가 핵심이다
3. ID만 알면 타인 데이터 변경을 허용해야 한다
4. 숫자 ID를 UUID로 바꾸면 인가가 불필요하다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · ID를 숨기는 것보다 대상별 서버 소유권·권한 검사가 핵심이다

식별자의 예측 가능성과 별도로 모든 데이터 접근에서 인증 주체의 권한을 검증합니다.

</details>


<a id="w344"></a>
### W344. 204와 200+메시지 응답을 고를 때 올바른 원칙은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=344)

1. 204는 반드시 JSON 메시지를 포함한다
2. 200이면 반드시 DB를 수정해야 한다
3. 204는 본문 없이, 200은 정의한 본문 계약으로 일관되게 사용한다
4. 204는 권한 거절만 표현한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 204는 본문 없이, 200은 정의한 본문 계약으로 일관되게 사용한다

현재 로그아웃·거래처 비활성화는 204이며 다른 계약을 택하면 클라이언트와 문서를 맞춥니다.

</details>


<a id="w345"></a>
### W345. 비밀번호 변경 뒤 기존 세션·토큰에 대한 안전한 판단은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=345)

1. DB 비밀번호 해시 변경이 모든 JWT를 자동 폐기한다
2. 현재 세션 정보만 바뀌면 모든 복사 토큰이 사라진다
3. 기존 세션 ID를 공개해 사용자가 비교하게 한다
4. 탈취 가능성과 요구에 맞춰 기존 인증 폐기·재인증 정책을 명시한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 탈취 가능성과 요구에 맞춰 기존 인증 폐기·재인증 정책을 명시한다

현재 구현에 없는 일괄 폐기를 이미 제공한다고 가정하지 않고 별도 정책과 구현을 검증합니다.

</details>


<a id="w346"></a>
### W346. 이메일 정규화 정책을 적용할 때 필요한 일관성은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=346)

1. 가입·저장·조회에서 같은 규칙을 사용하고 업무상의 대소문자 정책을 명시한다
2. 화면에서만 소문자로 바꾸면 모든 조회가 일치한다
3. 기존 DB 값과 무관하게 매번 다른 규칙을 쓴다
4. 정규화가 비밀번호 검증을 대신한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 가입·저장·조회에서 같은 규칙을 사용하고 업무상의 대소문자 정책을 명시한다

현재 AuthService는 입력 이메일로 조회합니다. 정규화 도입은 모든 경로와 기존 데이터를 함께 맞추는 과제입니다.

</details>


<a id="w347"></a>
### W347. 기존 비밀번호와 같은 새 비밀번호를 거절하는 정책의 의미는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=347)

1. 같은 비밀번호를 거절하면 모든 이전 토큰이 자동 폐기된다
2. 변경이 실제 새 인증 값으로 이어지게 하지만 재사용 정책만으로 탈취가 해결되지는 않는다
3. BCrypt salt가 같아져야만 변경할 수 있다
4. 로그인 없는 사용자도 타인 비밀번호를 바꿀 수 있다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 변경이 실제 새 인증 값으로 이어지게 하지만 재사용 정책만으로 탈취가 해결되지는 않는다

새 비밀번호 길이·강도·유출 여부·인증 폐기 정책과 구분해 검토합니다.

</details>


<a id="module-24"></a>
## 24. 거래처 Service / 카테고리 Service — CRUD + 정책 (PRD 2.5.2, 2.5.3, TRD 3.7.2, 3.7.3)

- [선택형 학습 파일](starter/24-partner-service/PartnerService.java)
- [선택형 학습 파일](starter/24-partner-service/CategoryService.java)

<a id="w099"></a>
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


<a id="w100"></a>
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


<a id="w101"></a>
### W101. 거래처 유형 변경 시 올바른 판단은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=101)

1. 과거 이력과 신규 주문 허용 정책을 명시해 일관되게 검증한다
2. 과거 발주를 모두 수주로 자동 변환한다
3. 모든 유형 변경을 JPA가 자동으로 허가·거부한다
4. HTTP status만 바꾸면 이력 무결성이 보장된다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 과거 이력과 신규 주문 허용 정책을 명시해 일관되게 검증한다

과거 참조를 보존하고 변경 뒤 신규 사용 정책을 구분해야 합니다.

</details>


<a id="w102"></a>
### W102. 이름·사업자번호·유형 검색 조건이 늘면 적절한 방법은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=102)

1. 모든 결과를 브라우저에서만 필터한다
2. 검색할 때 DB를 초기화한다
3. 정적 비밀번호를 검색어로 쓴다
4. 조건별 Specification을 구성해 필요한 조건을 결합한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 조건별 Specification을 구성해 필요한 조건을 결합한다

Repository 검색 추상화에 선택 조건을 모아 조합 폭증을 줄입니다.

</details>


<a id="w103"></a>
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


<a id="w104"></a>
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


<a id="w105"></a>
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


<a id="w106"></a>
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


<a id="w246"></a>
### W246. 카테고리명·품목코드·사업자번호 unique의 공통 의미는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=246)

1. 업무 식별 값의 중복을 DB에서 차단한다
2. 사용자 권한을 자동 부여한다
3. 모든 참조 데이터를 물리 삭제한다
4. 모든 값에 자동으로 비밀번호 해시를 만든다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 업무 식별 값의 중복을 DB에서 차단한다

각 식별자의 의미는 다르지만 최종 중복 방어라는 DB 제약의 역할은 같습니다.

</details>


<a id="w248"></a>
### W248. INACTIVE 거래처의 신규 발주·수주 사용을 어디에서 막는가?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=248)

1. CSS에서 버튼 색상만 변경
2. 거래처를 조회한 Service 업무 검증
3. 브라우저 주소 표시줄
4. README의 제목

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 거래처를 조회한 Service 업무 검증

화면 우회를 막기 위해 서버 업무 계층에서 거래처 상태를 검사합니다.

</details>


<a id="w348"></a>
### W348. 품목이 있는 카테고리를 거절 또는 기본 카테고리로 이관할 때의 비교는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=348)

1. 이관하면 모든 품목의 과거 주문을 삭제해야 한다
2. 거절 정책은 외래키와 무관하다
3. 거절은 현재 참조 보호, 이관은 대상·일괄 변경·트랜잭션 정책이 필요하다
4. 두 방법 모두 권한 검사가 필요 없다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 거절은 현재 참조 보호, 이관은 대상·일괄 변경·트랜잭션 정책이 필요하다

현재는 삭제 거절이며 이관은 별도 업무 정책·구현·테스트가 필요한 대안입니다.

</details>


<a id="w349"></a>
### W349. 카테고리 목록 캐시를 사용할 때 필요한 무효화 시점은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=349)

1. 조회 때 비밀번호 해시를 캐시 키로 공개한다
2. 등록·수정·삭제 성공 후 해당 조회 캐시를 갱신하거나 제거한다
3. 캐시가 있으면 DB 변경은 언제나 즉시 반영된다
4. 테이블을 삭제해 캐시를 비운다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 등록·수정·삭제 성공 후 해당 조회 캐시를 갱신하거나 제거한다

트랜잭션 성공 시점과 캐시 갱신·만료·일관성 요구를 함께 설계합니다.

</details>


<a id="w350"></a>
### W350. 과거 고객 이력 때문에 BOTH→SUPPLIER를 금지하는 정책이라면?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=350)

1. 브라우저가 드롭다운만 숨기면 충분하다
2. JPA enum 이름이 모든 이력을 자동 검증한다
3. DB 포트가 같으면 무조건 허용한다
4. Service가 관련 이력과 변경 정책을 검사한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · Service가 관련 이력과 변경 정책을 검사한다

이 문제는 명시한 변경 금지 정책을 전제로 하며 현재 참조 구현의 정책과 구분합니다.

</details>


<a id="w390"></a>
### W390. 이미 사용 중인 이름으로 카테고리를 등록하려 할 때 업무 오류 코드는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=390)

1. CATEGORY_NOT_FOUND
2. CATEGORY_HAS_ITEMS
3. PARTNER_TYPE_MISMATCH
4. DUPLICATE_CATEGORY_NAME

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · DUPLICATE_CATEGORY_NAME

이름 중복은 DUPLICATE_CATEGORY_NAME입니다. 미존재와 참조 중인 카테고리의 삭제 금지는 서로 다른 오류입니다.

</details>


<a id="w391"></a>
### W391. 카테고리 상세에 소속 품목 페이지를 포함하는 starter에서 적절한 조회 방식은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=391)

1. 모든 카테고리의 전체 품목을 읽고 화면에서 자른다
2. 카테고리 ID로 별도 품목 페이지를 조회하고 DTO로 변환한다
3. 각 품목마다 같은 카테고리를 다시 조회한다
4. 컬렉션 fetch join이면 페이징 위험이 모두 사라진다고 가정한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 카테고리 ID로 별도 품목 페이지를 조회하고 DTO로 변환한다

해당 starter는 findByCategory_Id(categoryId, pageable) 결과를 DTO로 매핑합니다. 현재 상세 API의 계약과 소속 품목을 포함하는 대안 연습을 구분합니다.

</details>


<a id="module-25"></a>
## 25. 거래처 Controller / 카테고리 Controller + DTO (TRD 3.7.2, 3.7.3)

- [선택형 학습 파일](starter/25-partner-controller/PartnerController.java)
- [선택형 학습 파일](starter/25-partner-controller/CategoryController.java)

<a id="w107"></a>
### W107. PreAuthorize와 Service 가드를 함께 설계할 때 올바른 설명은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=107)

1. 메서드 보안 활성화가 필요하며 Service 업무 인가도 별도로 검토한다
2. 어노테이션 문구만 쓰면 현재 프로젝트에 무조건 작동한다
3. Service 가드가 있으면 어떤 경로도 인증 없이 공개한다
4. 두 검사는 항상 모든 코드에서 동일하게 중복해야 한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 메서드 보안 활성화가 필요하며 Service 업무 인가도 별도로 검토한다

현재 구현은 Service 가드 중심이며 대안의 PreAuthorize에는 EnableMethodSecurity 등이 필요합니다.

</details>


<a id="w108"></a>
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


<a id="w109"></a>
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


<a id="w110"></a>
### W110. 소규모 카테고리 목록에 자연스러운 응답과 확장 방향은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=110)

1. 언제나 비밀번호 배열을 반환한다
2. 카테고리 목록은 무조건 void만 반환한다
3. List로 시작하되 규모가 커지면 Page 계약을 고려한다
4. 페이징을 쓰면 카테고리를 저장할 수 없다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · List로 시작하되 규모가 커지면 Page 계약을 고려한다

목록 규모와 사용자 경험에 맞춰 반환 계약을 정합니다.

</details>


<a id="w111"></a>
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


<a id="w112"></a>
### W112. CATEGORY_HAS_ITEMS의 현재 HTTP 응답은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=112)

1. 200 OK
2. 400 Bad Request
3. 401 Unauthorized
4. 500 Internal Server Error

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 400 Bad Request

현재 ErrorCode는 업무 삭제 금지 규칙을 400으로 응답합니다.

</details>


<a id="w113"></a>
### W113. 카테고리 변경·조회 권한의 현재 정책은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=113)

1. 변경은 ADMIN, 조회는 로그인 사용자에게 허용한다
2. 변경은 USER만, 조회는 비로그인만 허용한다
3. 모든 변경을 공개한다
4. MANAGER만 모든 카테고리를 조회한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 변경은 ADMIN, 조회는 로그인 사용자에게 허용한다

조회와 변경은 서로 다른 권한 범위를 갖습니다.

</details>


<a id="w251"></a>
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


<a id="module-26"></a>
## 26. 공지 Controller + DTO (TRD 3.7.6)

- [선택형 학습 파일](starter/26-notice-controller/NoticeController.java)

<a id="w114"></a>
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


<a id="w115"></a>
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


<a id="w116"></a>
### W116. PageableDefault의 역할은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=116)

1. DB 전체 데이터를 삭제한다
2. 모든 사용자를 ADMIN으로 만든다
3. 요청이 생략한 페이지 크기·정렬 기본값을 지정한다
4. 외래키 관계를 자동 변경한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 요청이 생략한 페이지 크기·정렬 기본값을 지정한다

기본값 설정과 최대 크기·허용 정렬 검증은 별도로 설계합니다.

</details>


<a id="module-27"></a>
## 27. 수주 Controller + DTO — my / pending / confirm / ship / complete (TRD 3.7.7)

- [선택형 학습 파일](starter/27-sales-controller/SalesOrderController.java)

<a id="w117"></a>
### W117. 수주 my와 처리 대기 목록의 현재 권한 차이는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=117)

1. my는 모든 사용자의 비밀번호 목록이다
2. 처리 대기는 비로그인에게만 허용한다
3. URL 이름만으로 서버 인가가 보장된다
4. my는 본인 목록, 처리 대기는 ADMIN/MANAGER용이다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · my는 본인 목록, 처리 대기는 ADMIN/MANAGER용이다

목록 대상과 실행 권한을 Service에서 함께 확인합니다.

</details>


<a id="w118"></a>
### W118. confirm·ship·complete·cancel 경로의 설계 특징은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=118)

1. 경로 이름이 전이 검증을 자동 수행한다
2. 업무 명령이 명확하지만 각 상태 전이 계약을 검사해야 한다
3. confirm 호출은 무조건 재고를 차감한다
4. cancel은 종료 상태에서도 항상 허용된다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 업무 명령이 명확하지만 각 상태 전이 계약을 검사해야 한다

명령형 API에는 권한·사유·상태·중복 호출 정책이 필요합니다.

</details>


<a id="w119"></a>
### W119. 활성화된 메서드 보안에서 hasAnyRole('MANAGER','ADMIN')의 의미는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=119)

1. 두 역할을 동시에 가져야만 허용한다
2. USER 역할만 허용한다
3. 두 역할 중 하나라도 있으면 허용한다
4. 로그인 없이 항상 허용한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 두 역할 중 하나라도 있으면 허용한다

일반적인 ROLE_ 접두사 규칙을 적용하며 현재 Service 가드 구조와 구분합니다.

</details>


<a id="module-28"></a>
## 28. 품목 상세 / 수정 / 단종 처리 (PRD 2.5.4)

- [선택형 학습 파일](starter/28-item-detail-update/ItemService.detail.java)

<a id="w120"></a>
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


<a id="w121"></a>
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


<a id="w122"></a>
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


<a id="w123"></a>
### W123. 품목 soft delete와 hard delete의 차이는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=123)

1. soft delete는 상태로 제한하면서 이력을 보존하고 조회 조건 관리가 필요하다
2. soft delete는 DB 행을 항상 물리 삭제한다
3. hard delete는 모든 이력을 무조건 보존한다
4. 두 방법 모두 신규 주문 검증이 필요 없다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · soft delete는 상태로 제한하면서 이력을 보존하고 조회 조건 관리가 필요하다

보존 정책과 데이터 정리·조회 복잡도의 차이를 고려합니다.

</details>


<a id="w351"></a>
### W351. 단종 품목을 다시 ACTIVE로 바꾸는 기능을 추가할 때 필요한 것은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=351)

1. 상태만 바꾸면 모든 업무 요구가 자동 충족된다
2. 모든 과거 주문을 다시 작성한다
3. 재활성화 허용 조건·권한·이력 의미를 명시하고 검증한다
4. 현재 API가 제공한다고 검증 없이 가정한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 재활성화 허용 조건·권한·이력 의미를 명시하고 검증한다

재활성화는 정책 선택입니다. 현재 단종 기능과 추가 기능 요구를 구분합니다.

</details>


<a id="w392"></a>
### W392. 단종 재요청을 거부하는 대안 Service에서 품목 단종 오류로 매핑할 현재 ErrorCode는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=392)

1. ITEM_DISCONTINUED
2. DUPLICATE_ITEM_CODE
3. CATEGORY_HAS_ITEMS
4. INSUFFICIENT_STOCK

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · ITEM_DISCONTINUED

단종을 오류로 다루는 가정에서는 ITEM_DISCONTINUED가 해당 의미를 나타냅니다. 현재 ItemService의 재단종 처리와 대안 연습의 전제를 구분합니다.

</details>


<a id="module-29"></a>
## 29. 발주 — 내 목록 / 상세 / 취소 + 관리자 목록 (PRD 2.5.5)

- [선택형 학습 파일](starter/29-purchase-my-cancel/PurchaseOrderService.my.java)

<a id="w124"></a>
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


<a id="w125"></a>
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


<a id="w126"></a>
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


<a id="w127"></a>
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


<a id="w128"></a>
### W128. status와 partnerId가 선택 조건일 때 올바른 검색 방식은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=128)

1. 값이 없으면 항상 모든 주문을 삭제한다
2. 전달된 조건만 만들어 AND로 결합한다
3. 항상 둘 중 하나만 무조건 무시한다
4. 조건이 둘이면 페이징이 금지된다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 전달된 조건만 만들어 AND로 결합한다

Specification 등의 조합으로 여러 선택 조건을 일관되게 처리합니다.

</details>


<a id="w129"></a>
### W129. 발주 목록에 from/to 기간을 추가할 때 필요한 것은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=129)

1. 기존 소유권 검사를 제거한다
2. from을 비밀번호 필드로 저장한다
3. 날짜 범위를 검증하고 조회 조건·API·테스트를 함께 확장한다
4. 모든 날짜를 문자열 연결만으로 비교한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 날짜 범위를 검증하고 조회 조건·API·테스트를 함께 확장한다

기간 검색의 경계와 누락 값 정책을 명시하고 다른 검색 조건과 조합합니다.

</details>


<a id="w352"></a>
### W352. 반복되는 역할·작성자 접근 검사를 헬퍼로 모을 때의 입력은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=352)

1. 서버가 인증한 사용자 정보와 대상의 소유권 정보
2. 요청 본문의 isAdmin만
3. 브라우저 색상만
4. DB 비밀번호만

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 서버가 인증한 사용자 정보와 대상의 소유권 정보

대상별 정책을 공통화하되 현재 역할과 실제 대상 관계를 서버 값으로 판단합니다.

</details>


<a id="module-30"></a>
## 30. 수주 — my / pending / detail 권한 검사 (PRD 2.5.7)

- [선택형 학습 파일](starter/30-sales-lists/SalesOrderService.lists.java)

<a id="w130"></a>
### W130. 수주 상세에 권한 없는 USER가 접근하면?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=130)

1. 작성자 ID를 방문자 ID로 바꾼다
2. ACCESS_DENIED로 거절한다
3. 수주를 자동 출고한다
4. 모든 비밀번호를 반환한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · ACCESS_DENIED로 거절한다

현재 구현은 작성자 또는 ADMIN/MANAGER에 조회를 허용합니다. 과거 담당자 제한 과제와 구분합니다.

</details>


<a id="w131"></a>
### W131. 담당자별 pending 조회를 설계할 때 위임의 영향은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=131)

1. 담당자 기준이면 위임된 ID로 조회 조건·권한 정책을 일치시켜야 한다
2. 위임하면 모든 수주가 자동 완료된다
3. 담당자 ID가 페이징 크기가 된다
4. 어떤 조회도 위임의 영향을 받지 않는다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 담당자 기준이면 위임된 ID로 조회 조건·권한 정책을 일치시켜야 한다

현재 참조 구현의 역할 기반 처리 대기 목록과 담당자별 대안 정책을 구분합니다.

</details>


<a id="w132"></a>
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


<a id="w187"></a>
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


<a id="w188"></a>
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


<a id="w257"></a>
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


<a id="w258"></a>
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


<a id="w353"></a>
### W353. 현재 담당자 미지정 CONFIRMED 수주는 누가 처리 대기 목록에서 조회하는가?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=353)

1. ADMIN 또는 MANAGER의 역할 기반 목록에 포함될 수 있다
2. 반드시 아무에게도 보이지 않는다
3. 모든 비로그인 사용자에게만 보인다
4. 작성자가 바뀌어야만 보인다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · ADMIN 또는 MANAGER의 역할 기반 목록에 포함될 수 있다

현재 pending은 상태로 조회하고 역할을 검사합니다. 담당자별 제한 목록은 별도 대안 정책입니다.

</details>


<a id="module-31"></a>
## 31. Repository — User / Partner / Category / PurchaseOrder / Notice / SalesOrder (TRD 3.5)

- [선택형 학습 파일](starter/31-repositories-all/AllRepositories.java)

<a id="w133"></a>
### W133. findByPartner_BusinessNumber의 속성 경로 의미는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=133)

1. HTTP 쿠키 이름 조건이다
2. 연관된 partner의 businessNumber 조건이다
3. 테이블 이름을 BusinessNumber로 변경한다
4. 모든 파트너를 삭제한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 연관된 partner의 businessNumber 조건이다

중첩 속성 탐색 구분자를 사용해 연관관계의 속성을 조회합니다.

</details>


<a id="w134"></a>
### W134. JPQL orderDate BETWEEN :from AND :to의 일반적 경계는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=134)

1. 양 끝을 모두 제외한다
2. 날짜와 무관하게 모든 행을 삭제한다
3. from만 사용하고 to는 무시한다
4. 양 끝 날짜를 포함하는 범위 조건이다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 양 끝 날짜를 포함하는 범위 조건이다

날짜 범위 조건과 from≤to 검증을 함께 적용합니다.

</details>


<a id="w135"></a>
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


<a id="w136"></a>
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


<a id="w230"></a>
### W230. 입고 이력이 없는 품목도 재고 화면에 표시하려면?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=230)

1. Item을 기준으로 Stock을 LEFT JOIN하고 없는 재고를 0으로 처리한다
2. Stock 테이블만 조회한다
3. INNER JOIN만 사용한다
4. 입고 이력이 없으면 품목을 삭제한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · Item을 기준으로 Stock을 LEFT JOIN하고 없는 재고를 0으로 처리한다

재고 행이 없는 품목을 누락하지 않도록 품목 중심 조회와 null 처리로 구성합니다.

</details>


<a id="w354"></a>
### W354. 파생 쿼리 메서드의 속성과 DB 컬럼 이름의 관계는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=354)

1. DB 컬럼 이름만을 Java 메서드로 무조건 그대로 파싱한다
2. 컬럼명 변경은 HTTP 경로만 변경한다
3. 엔티티 속성을 기준으로 파싱하며 Column 매핑이 DB 이름에 연결한다
4. 속성명을 바꿔도 파생 쿼리는 항상 자동 수정된다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 엔티티 속성을 기준으로 파싱하며 Column 매핑이 DB 이름에 연결한다

속성명을 바꾸면 메서드 경로를 함께 고쳐야 하며 DB 컬럼만 변경하는 경우 매핑을 대조합니다.

</details>


<a id="w355"></a>
### W355. 연관관계가 있는 모델의 findByPartner_Id 같은 조회는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=355)

1. 항상 모든 엔티티를 메모리에 조회한다
2. partner.id 속성 경로를 조건으로 해석하며 실제 SQL은 매핑·최적화에 따라 결정된다
3. 어떤 경우에도 SQL JOIN이 발생하지 않는다
4. 무조건 세션 쿠키를 검색한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · partner.id 속성 경로를 조건으로 해석하며 실제 SQL은 매핑·최적화에 따라 결정된다

속성 경로와 실제 SQL JOIN 유무를 단정하지 말고 매핑과 쿼리 로그를 확인합니다.

</details>


<a id="module-32"></a>
## 32. 응답 DTO 매핑 패턴 (TRD 3.16.3)

- [선택형 학습 파일](starter/32-response-dto-mapping/ResponseDtoMapping.java)

<a id="w137"></a>
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


<a id="w138"></a>
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


<a id="w139"></a>
### W139. 주문 헤더-라인 응답 DTO의 자연스러운 구조는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=139)

1. 비밀번호 배열 안에 헤더를 둔다
2. 각 라인이 모든 사용자 엔티티를 포함한다
3. 모든 데이터를 CSV 문자열 한 글자로 바꾼다
4. 헤더 DTO 안에 라인 DTO 목록을 둔다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 헤더 DTO 안에 라인 DTO 목록을 둔다

응답 계약에 필요한 헤더·라인 필드를 명시해 중첩 구조로 구성합니다.

</details>


<a id="w140"></a>
### W140. Entity와 DTO 의존 방향의 올바른 원칙은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=140)

1. Entity가 Controller와 Session을 직접 호출한다
2. Entity가 HTTP 응답 DTO에 의존하지 않게 한다
3. DTO 사용 시 Entity도 반드시 DTO를 import한다
4. Repository가 HTML 템플릿을 생성한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · Entity가 HTTP 응답 DTO에 의존하지 않게 한다

도메인 모델은 외부 표현 계약의 변경으로부터 독립시키는 것이 좋습니다.

</details>


<a id="w141"></a>
### W141. record DTO와 일반 class DTO의 차이는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=141)

1. record는 JPA 연관관계를 자동 저장한다
2. class는 필드를 가질 수 없다
3. record면 내부 컬렉션도 항상 깊은 불변이다
4. record는 구성 요소·접근자·값 동등성 등을 간결하게 선언한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · record는 구성 요소·접근자·값 동등성 등을 간결하게 선언한다

record의 참조 재대입 제한과 참조하는 객체의 변경 가능성은 별개입니다.

</details>


<a id="w142"></a>
### W142. 매핑 라이브러리를 도입할 적절한 기준은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=142)

1. 필드가 하나여도 무조건 도입해야 한다
2. 반복 매핑의 규모·복잡도와 디버깅·의존 비용을 비교한다
3. 도입하면 모든 업무 검증이 자동 생긴다
4. 매핑 도구가 인증·인가를 대신한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 반복 매핑의 규모·복잡도와 디버깅·의존 비용을 비교한다

단순 팩토리로 충분한지, 도구가 유지 비용을 줄이는지 판단합니다.

</details>


<a id="w177"></a>
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


<a id="w247"></a>
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


<a id="module-33"></a>
## 33. 핵심 Bean 설정 — JPA Auditing / PasswordEncoder / WebMvcConfig (TRD 3.5, 3.11)

- [선택형 학습 파일](starter/33-config-beans/ConfigBeans.java)

<a id="w143"></a>
### W143. JPA Auditing 활성화가 빠지면 생길 수 있는 문제는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=143)

1. 모든 엔티티가 자동 ADMIN이 된다
2. 모든 날짜가 SQL 기본키가 된다
3. 감사 어노테이션이 붙어도 시각 자동 기록이 동작하지 않을 수 있다
4. Hibernate가 서버 포트를 바꾼다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 감사 어노테이션이 붙어도 시각 자동 기록이 동작하지 않을 수 있다

감사 리스너와 EnableJpaAuditing 등 관련 설정이 연결되어야 합니다.

</details>


<a id="w144"></a>
### W144. 비밀번호에 BCrypt를 사용하는 이유는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=144)

1. salt와 조절 가능한 연산 비용으로 대량 추측 비용을 높인다
2. 평문을 누구나 쉽게 복원할 수 있다
3. 빠른 SHA-256 한 번이면 항상 더 안전하다
4. 같은 비밀번호가 항상 같은 해시를 만들게 한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · salt와 조절 가능한 연산 비용으로 대량 추측 비용을 높인다

비밀번호 해시는 빠른 일반 해시와 목적이 다르며 BCrypt는 matches로 검증합니다.

</details>


<a id="w145"></a>
### W145. WebMvcConfigurer의 두 등록 메서드 역할은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=145)

1. 둘 다 DB 스키마를 생성한다
2. 둘 다 CSS를 압축한다
3. addInterceptors는 요청 검사, addArgumentResolvers는 인자 해석 등록이다
4. 둘 다 BCrypt 비용을 변경한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · addInterceptors는 요청 검사, addArgumentResolvers는 인자 해석 등록이다

MVC 요청 처리의 확장 지점을 설정에 연결합니다.

</details>


<a id="w146"></a>
### W146. 세션 인터셉터와 Security 책임을 정리해야 하는 이유는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=146)

1. 인증 원천·리다이렉트·예외 정책 충돌을 피하기 위해서다
2. Security를 추가하면 Service 검사가 금지된다
3. 항상 모든 기능에 같은 검사를 세 번 추가해야 한다
4. 둘을 같이 사용하면 HTTP 요청이 불가능하다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 인증 원천·리다이렉트·예외 정책 충돌을 피하기 위해서다

현재 구조는 역할을 분담하고 전체 인증 이전 때는 중복 계약을 조정해야 합니다.

</details>


<a id="w226"></a>
### W226. PasswordConfig의 Bean이 구현하는 비밀번호 해시·검증 인터페이스는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=226)

1. Pageable
2. PasswordEncoder
3. HttpSession
4. PurchaseOrder

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · PasswordEncoder

BCryptPasswordEncoder는 PasswordEncoder 인터페이스를 구현합니다. 현재 설정의 반환 타입과 Service 주입 타입은 BCryptPasswordEncoder입니다.

</details>


<a id="w356"></a>
### W356. 현재 프로젝트에서 PasswordEncoder를 Bean으로 명시하는 이유는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=356)

1. Security starter가 없으면 Java에서 BCrypt를 호출할 수 없기 때문이다
2. Bean을 만들면 비밀번호가 평문으로 자동 저장되기 때문이다
3. EnableJpaAuditing이 모든 암호화 객체를 자동 생성하기 때문이다
4. 알고리즘·설정과 주입할 객체를 애플리케이션이 명확히 선택하기 위해서다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 알고리즘·설정과 주입할 객체를 애플리케이션이 명확히 선택하기 위해서다

자동 설정으로 원하는 Encoder가 항상 생긴다고 가정하지 않고 PasswordConfig의 실제 등록을 확인합니다.

</details>


<a id="w357"></a>
### W357. 감사 사용자 필드와 시각 필드에 대한 설명은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=357)

1. CreatedDate는 AuditorAware가 없으면 항상 비밀번호가 된다
2. createdBy가 없으면 createdAt도 기술적으로 절대 기록할 수 없다
3. 모든 감사 필드는 기본 ADMIN으로 기록된다
4. CreatedBy·LastModifiedBy는 감사 사용자 공급이 필요하고 시각 감사와 별도다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · CreatedBy·LastModifiedBy는 감사 사용자 공급이 필요하고 시각 감사와 별도다

사용자 감사에는 AuditorAware 등의 정책이 필요하며 현재 시각 감사 구성과 구분합니다.

</details>


<a id="w393"></a>
### W393. springdoc를 추가하고 문서 공개를 승인한 설계에서 기본 Swagger UI·API 문서 경로 조합은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=393)

1. /css/**와 /js/**
2. /swagger-ui/**와 /v3/api-docs/**
3. /api/items/**와 /api/partners/**
4. /h2-console/**와 /api/users/**

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · /swagger-ui/**와 /v3/api-docs/**

기본 Swagger UI와 OpenAPI 문서 경로입니다. 현재 프로젝트가 해당 라이브러리를 이미 제공한다는 뜻은 아니며 운영 공개 여부는 별도 권한 정책으로 결정합니다. 참조: https://springdoc.org/

</details>


<a id="module-34"></a>
## 34. 세션 인터셉터 + @CurrentUser ArgumentResolver (TRD 3.11.1)

- [선택형 학습 파일](starter/34-current-user-interceptor/CurrentUserAndInterceptor.java)

<a id="w147"></a>
### W147. CurrentUser ArgumentResolver의 역할은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=147)

1. 클라이언트의 임의 역할을 인증 없이 승인한다
2. 모든 엔티티를 JSON으로 저장한다
3. 비밀번호를 브라우저 주소에 붙인다
4. 세션 로그인 정보를 컨트롤러 파라미터 값으로 해석한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 세션 로그인 정보를 컨트롤러 파라미터 값으로 해석한다

supportsParameter와 resolveArgument로 지원 파라미터를 찾고 LoginUser를 제공합니다.

</details>


<a id="w148"></a>
### W148. Servlet Filter와 MVC Interceptor의 실행 순서는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=148)

1. Interceptor가 모든 Filter보다 먼저 실행된다
2. Filter가 MVC에 앞서고 Interceptor는 Controller 호출 전후에 동작한다
3. 둘은 DB 커밋 후에만 실행된다
4. 둘은 Java 컴파일 시에만 실행된다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · Filter가 MVC에 앞서고 Interceptor는 Controller 호출 전후에 동작한다

Filter는 Servlet 계층, Interceptor는 MVC 핸들러 처리 계층에 있습니다.

</details>


<a id="w149"></a>
### W149. CurrentUser resolver가 없다면 반복될 수 있는 코드는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=149)

1. 브라우저 CSS 다운로드 코드
2. Gradle 의존성 선언
3. DB 초기화 SQL만
4. 컨트롤러마다 세션 로그인 정보를 꺼내 확인하는 코드

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 컨트롤러마다 세션 로그인 정보를 꺼내 확인하는 코드

사용자 해석을 공통화하면 컨트롤러의 반복과 일관성 문제를 줄입니다.

</details>


<a id="w150"></a>
### W150. 세션에서 JWT로 바꿀 때 바뀌는 부분은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=150)

1. 모든 도메인 상태 전이를 삭제한다
2. ItemStatus를 UserRole로 바꾼다
3. 인증 정보 추출·토큰 검증·사용자 해석과 폐기 정책이다
4. 재고 테이블을 HTML로 바꾼다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 인증 정보 추출·토큰 검증·사용자 해석과 폐기 정책이다

인증 원천은 달라져도 업무 권한과 소유권 규칙은 계속 필요합니다.

</details>


<a id="w227"></a>
### W227. WebMvcConfig의 보호 경로·예외 경로에 대한 현재 설명은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=227)

1. 모든 admin 경로가 문자열만으로 자동 보호된다
2. H2 전용 서블릿도 MVC 인터셉터만으로 항상 보호된다
3. 전반적 로그인 검사에서 로그인·정적 리소스 등을 제외한다
4. 정적 CSS마다 DB 트랜잭션을 생성한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 전반적 로그인 검사에서 로그인·정적 리소스 등을 제외한다

H2 콘솔 서블릿의 실제 보호는 SecurityConfig에서 별도 처리합니다.

</details>


<a id="w343"></a>
### W343. SessionAttribute와 커스텀 CurrentUser의 차이는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=343)

1. 전자는 지정 세션 속성 바인딩, 후자는 사용자 해석 정책을 공통 resolver로 추상화한다
2. 둘 다 무조건 JWT 서명만 검사한다
3. 전자는 Repository 구현을 생성한다
4. 후자는 DB 비밀번호를 자동 암호화한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 전자는 지정 세션 속성 바인딩, 후자는 사용자 해석 정책을 공통 resolver로 추상화한다

현재 CurrentUser는 LoginUser·미인증 처리 등 공통 정책을 컨트롤러 인자 해석에 모읍니다.

</details>


<a id="w358"></a>
### W358. ArgumentResolver가 던진 업무 예외를 Advice로 처리하려면?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=358)

1. 어떤 Filter 예외도 MVC Advice가 항상 처리한다
2. resolver가 HTML을 직접 출력해야 한다
3. MVC 예외 처리 흐름·Advice 적용 범위·해당 핸들러가 맞아야 한다
4. 예외를 null로 바꾸면 오류 계약이 유지된다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · MVC 예외 처리 흐름·Advice 적용 범위·해당 핸들러가 맞아야 한다

컨트롤러 인자 해석 단계의 예외도 처리할 수 있으나 Filter 등 다른 실행 경계와 구분합니다.

</details>


<a id="w362"></a>
### W362. AuthenticationPrincipal과 현재 커스텀 CurrentUser의 인증 원천 차이는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=362)

1. 둘 다 언제나 같은 세션 속성에 자동 연결된다
2. 전자는 JPA 영속성 컨텍스트다
3. 후자는 반드시 JWT를 사용한다
4. 전자는 Security 인증 principal, 후자는 현재 세션 LoginUser 해석을 사용한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 전자는 Security 인증 principal, 후자는 현재 세션 LoginUser 해석을 사용한다

인증 구조를 옮길 때 컨트롤러 인자 계약과 인증 원천을 함께 맞춰야 합니다.

</details>


<a id="module-35"></a>
## 35. Thymeleaf 화면 (PRD 2.7)

- [선택형 학습 파일](starter/35-thymeleaf-views/ThymeleafViews.md)

<a id="w151"></a>
### W151. Thymeleaf 화면과 정적 리소스의 기본 위치는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=151)

1. 모든 템플릿을 gradle 캐시에 보관한다
2. templates와 static 아래에서 역할별로 분리한다
3. 엔티티 package에 CSS만 둔다
4. 모든 화면을 DB 비밀번호 컬럼에 넣는다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · templates와 static 아래에서 역할별로 분리한다

서버 렌더링 템플릿과 직접 제공하는 CSS·JS 리소스의 위치를 구분합니다.

</details>


<a id="w152"></a>
### W152. 사용자 입력을 표시할 때 안전한 기본 Thymeleaf 속성은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=152)

1. HTML escape를 수행하는 th:text
2. 항상 th:utext
3. 사용자 입력을 script 태그로 평가
4. HTML을 모두 비밀번호 필드에 표시

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · HTML escape를 수행하는 th:text

th:utext는 신뢰·정화된 HTML을 의도적으로 표시할 때만 신중히 사용합니다.

</details>


<a id="w153"></a>
### W153. Spring Security와 Thymeleaf의 th:action POST 폼에서 CSRF는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=153)

1. 모든 fetch 요청에 토큰이 자동으로 붙는다
2. 통합된 토큰 hidden input을 사용하고 JS 요청은 토큰을 명시 전달한다
3. 폼이면 CSRF 검사가 항상 비활성화된다
4. 브라우저가 토큰을 비밀번호로 해시한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 통합된 토큰 hidden input을 사용하고 JS 요청은 토큰을 명시 전달한다

서버 폼 통합과 일반 HTML·JavaScript 요청의 토큰 전달 방식을 구분합니다.

</details>


<a id="w154"></a>
### W154. Thymeleaf Fragment 재사용의 기본 조합은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=154)

1. th:fragment 정의와 th:replace 사용
2. th:password와 th:session
3. SQL unique와 CSS color
4. JPA Id와 RequestBody

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · th:fragment 정의와 th:replace 사용

공통 레이아웃을 조각으로 정의해 여러 화면에서 재사용합니다.

</details>


<a id="w155"></a>
### W155. 동적 주문 라인 입력에서 중요한 것은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=155)

1. 브라우저 총금액을 검증 없이 신뢰한다
2. 삭제된 라인의 비밀번호를 전송한다
3. 동적 줄 추가가 DB 트랜잭션을 대신한다
4. 추가·삭제 후 입력 name 인덱스와 서버 검증을 유지한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 추가·삭제 후 입력 name 인덱스와 서버 검증을 유지한다

사용성 보조 JS와 서버 바인딩·업무 검증이 함께 맞아야 합니다.

</details>


<a id="w156"></a>
### W156. RestController와 Controller의 기본 응답 차이는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=156)

1. 둘 다 항상 DB 엔티티를 저장한다
2. Controller는 JSON 응답을 절대 만들 수 없다
3. RestController는 본문 응답, Controller는 View 이름 반환 등을 사용한다
4. RestController는 Service를 호출할 수 없다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · RestController는 본문 응답, Controller는 View 이름 반환 등을 사용한다

웹 화면과 API는 같은 Service를 공유하며 표현 방식을 나눌 수 있습니다.

</details>


<a id="w203"></a>
### W203. CSRF 보호가 활성화된 폼 변경 요청에 토큰이 없으면?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=203)

1. 항상 201로 저장된다
2. 보통 403으로 거절된다
3. 비밀번호가 자동 변경된다
4. JPA가 무조건 404를 반환한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 보통 403으로 거절된다

보호 대상 메서드·경로와 정책을 전제로 합니다. 현재 API 예외 정책과 일반 원칙을 구분합니다.

</details>


<a id="w204"></a>
### W204. th:replace와 th:insert의 차이는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=204)

1. 둘 다 SQL UPDATE를 실행한다
2. replace는 항상 escape를 제거한다
3. insert는 로그인 세션을 발급한다
4. replace는 대상 요소를 교체하고 insert는 내부에 조각을 넣는다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · replace는 대상 요소를 교체하고 insert는 내부에 조각을 넣는다

호스트 요소의 유지 여부가 달라 최종 HTML 구조에 영향을 줍니다.

</details>


<a id="w205"></a>
### W205. 동적 폼 라인의 name 인덱스를 올바르게 관리해야 하는 이유는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=205)

1. Spring 바인더가 각 라인의 필드를 올바르게 구성하도록 하기 위해서다
2. 관리자 권한을 자동 생성하기 위해서다
3. BigDecimal을 String으로 강제하기 위해서다
4. DB unique를 제거하기 위해서다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · Spring 바인더가 각 라인의 필드를 올바르게 구성하도록 하기 위해서다

중복·누락된 인덱스는 잘못된 값 매핑이나 바인딩 실패로 이어질 수 있습니다.

</details>


<a id="w206"></a>
### W206. 라인 추가마다 서버 화면을 다시 요청하는 방식의 비용은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=206)

1. 브라우저 네트워크 요청이 사라진다
2. 서버 검증이 항상 불필요해진다
3. 라인 추가가 무조건 SQL 삭제가 된다
4. 추가 왕복 지연과 입력 상태 보존 처리가 필요하다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 추가 왕복 지연과 입력 상태 보존 처리가 필요하다

JS 보조는 화면 내 편집을 돕지만 서버의 검증·계산 책임은 유지해야 합니다.

</details>


<a id="w207"></a>
### W207. 같은 도메인의 화면과 API를 함께 운영할 때 좋은 구조는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=207)

1. 두 컨트롤러에 업무 규칙을 각각 복사한다
2. 표현 컨트롤러를 나누고 업무 Service를 공유한다
3. View 컨트롤러가 API 컨트롤러를 직접 HTTP 호출한다
4. 모든 엔티티에 HTML을 저장한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 표현 컨트롤러를 나누고 업무 Service를 공유한다

중복되는 업무 로직을 공통 Service에 모으고 각 표현 계약을 분리합니다.

</details>


<a id="w208"></a>
### W208. Thymeleaf와 SPA를 비교할 때 올바른 설명은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=208)

1. Thymeleaf에서는 JavaScript를 사용할 수 없다
2. SPA에서는 서버 인가가 필요 없다
3. 서버 HTML은 초기 구성에 단순하고 복잡한 상호작용에는 클라이언트 코드가 추가된다
4. 서버 HTML이면 네트워크 지연이 없다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 서버 HTML은 초기 구성에 단순하고 복잡한 상호작용에는 클라이언트 코드가 추가된다

화면 복잡도·팀 기술·렌더링 방식·운영 비용에 맞춰 선택합니다.

</details>


<a id="w209"></a>
### W209. Thymeleaf의 자연 템플릿이 의미하는 것은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=209)

1. HTML 형태를 유지해 브라우저에서 시안을 열어 볼 수 있다
2. 서버 처리 없이 모든 th 속성이 실행된다
3. JSP와 Thymeleaf는 같은 컴파일 파일이다
4. 템플릿은 반드시 DB에 저장되어야 한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · HTML 형태를 유지해 브라우저에서 시안을 열어 볼 수 있다

브라우저의 정적 미리보기와 서버의 표현식 렌더링은 다릅니다.

</details>


<a id="w210"></a>
### W210. th:object와 th:field가 폼에서 돕는 것은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=210)

1. SQL 락과 unique 생성
2. 폼 객체·필드 이름·값·검증 오류의 바인딩
3. 로그인 권한 자동 승인
4. DB 비밀번호 복호화

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 폼 객체·필드 이름·값·검증 오류의 바인딩

객체 기반 폼 필드 구성을 돕고 서버에서 DTO 검증을 수행합니다.

</details>


<a id="w211"></a>
### W211. Spring Boot의 HTTP 상태별 HTML 오류 페이지를 두는 대표 위치는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=211)

1. gradle/wrapper/password.txt
2. static/db/root.sql
3. practice/workspace에만 보관
4. templates/error/404.html 같은 경로

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · templates/error/404.html 같은 경로

기본 오류 처리에 맞는 상태별 템플릿을 사용하거나 현재 WebExceptionHandler 계약에 맞게 구성합니다.

</details>


<a id="w212"></a>
### W212. 현재 화면에서 역할별 메뉴를 보여 주는 것의 보안 의미는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=212)

1. 메뉴를 숨기면 URL 직접 호출도 차단된다
2. sec:authorize는 별도 통합 없이 항상 사용 가능하다
3. 사용 편의를 위한 표시이며 실제 API·Service 인가가 필요하다
4. CSS display:none이 DB 권한을 보장한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 사용 편의를 위한 표시이며 실제 API·Service 인가가 필요하다

현재 화면 조건과 서버 인가를 구분합니다. Security dialect 방식은 관련 통합과 인증 정보를 요구합니다.

</details>


<a id="w394"></a>
### W394. Thymeleaf 템플릿이 items와 page를 모델 변수로 읽게 하는 컨트롤러 처리는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=394)

1. HttpSession.invalidate로 세션을 지운다
2. Pageable의 크기를 0으로 만든다
3. Model.addAttribute로 이름과 값을 등록한다
4. EntityManager.clear로 모델을 생성한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · Model.addAttribute로 이름과 값을 등록한다

컨트롤러가 Model에 이름으로 등록한 값을 템플릿에서 참조합니다. 영속성 컨텍스트나 세션 종료 기능과 구분합니다.

</details>


<a id="module-36"></a>
## 36. 패키지 구조와 계층 책임 (TRD 3.2.2)

- [선택형 학습 파일](starter/36-package-architecture/PackageArchitecture.md)

<a id="w157"></a>
### W157. 계층 우선과 도메인 우선 패키지의 올바른 비교는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=157)

1. 둘 다 권한 검사를 자동 생성한다
2. 도메인 우선이면 클래스가 필요 없다
3. 전자는 기술 계층별, 후자는 기능별 응집을 우선한다
4. 계층 우선이면 순환 의존이 불가능하다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 전자는 기술 계층별, 후자는 기능별 응집을 우선한다

규모와 변경 단위에 맞춰 선택하며 패키지 전략만으로 좋은 설계가 보장되지는 않습니다.

</details>


<a id="w158"></a>
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


<a id="w159"></a>
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


<a id="w160"></a>
### W160. Controller-fat 안티 패턴의 예는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=160)

1. Controller가 요청 DTO를 검증한다
2. Controller가 Repository를 직접 호출하며 업무 상태를 변경한다
3. Controller가 Service를 호출한다
4. Controller가 View 이름을 반환한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · Controller가 Repository를 직접 호출하며 업무 상태를 변경한다

HTTP 표현 책임과 업무 처리 책임을 분리합니다.

</details>


<a id="w213"></a>
### W213. Repository가 DTO 타입을 참조할 수 있는 정당한 예는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=213)

1. HttpSession을 직접 관리하는 경우
2. HTML 폼을 렌더링하는 경우
3. 비밀번호를 응답하는 경우
4. 조회 전용 projection 계약을 사용하는 경우

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 조회 전용 projection 계약을 사용하는 경우

모든 DTO 의존을 기계적으로 금지하지 않고 조회 모델의 목적과 결합을 검토합니다.

</details>


<a id="w214"></a>
### W214. Anemic Domain의 전형적인 특징은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=214)

1. Entity가 상태 전이 가드를 캡슐화한다
2. Entity는 데이터·setter만 갖고 업무 규칙이 Service에 집중된다
3. Service가 Controller HTTP 응답을 몰라도 된다
4. 도메인 메서드를 단위 테스트한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · Entity는 데이터·setter만 갖고 업무 규칙이 Service에 집중된다

업무 규칙의 표현과 변경 지점을 검토해 의미 있는 도메인 동작으로 옮길 수 있습니다.

</details>


<a id="w215"></a>
### W215. 너무 큰 Service를 리팩터링하는 적절한 방향은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=215)

1. 모든 Service를 하나의 더 큰 클래스에 합친다
2. Controller로 업무 규칙을 모두 옮긴다
3. 응집된 업무·정책 단위로 책임을 분리한다
4. 테스트를 삭제해 파일을 줄인다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 응집된 업무·정책 단위로 책임을 분리한다

도메인 서비스·정책 객체 등으로 응집도와 의존 방향을 개선하고 동작을 검증합니다.

</details>


<a id="w216"></a>
### W216. A Service→B Service→A Service 순환을 개선하는 방향은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=216)

1. 공통 업무 책임과 호출 방향을 재설계한다
2. 필드 이름만 바꾸면 순환이 사라진다
3. 항상 Lazy만 붙이고 설계 검토를 끝낸다
4. 두 Service가 서로의 Controller를 호출하게 한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 공통 업무 책임과 호출 방향을 재설계한다

순환은 책임·경계 문제의 신호일 수 있어 객체 관계와 흐름을 정리해야 합니다.

</details>


<a id="w217"></a>
### W217. 현재 저장소의 주요 패키지 전략은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=217)

1. 모든 클래스가 단일 파일에 있다
2. controller·service·repository·domain 중심의 계층 우선
3. 각 클래스마다 독립 저장소를 쓴다
4. 모든 기능을 CSS 폴더에 둔다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · controller·service·repository·domain 중심의 계층 우선

기본 계층을 쉽게 찾을 수 있지만 한 기능 변경이 여러 패키지에 걸칠 수 있습니다.

</details>


<a id="w218"></a>
### W218. 기능별 패키지로 옮기는 기준은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=218)

1. 변경 단위·응집도·의존 관계를 검토하고 점진적으로 이동한다
2. 패키지만 옮기면 모든 순환이 자동 해결된다
3. 테스트 없는 대규모 이름 변경만 수행한다
4. Feature-first는 모든 규모에 항상 유일한 정답이다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 변경 단위·응집도·의존 관계를 검토하고 점진적으로 이동한다

패키지 구조는 도구이며 실제 책임과 검증된 동작을 유지해야 합니다.

</details>


<a id="w219"></a>
### W219. 자주 import되는 공통 패키지의 결합도를 평가할 때는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=219)

1. import 개수만 적으면 무조건 좋은 설계다
2. 많이 사용되면 모두 복사해서 별도 정의한다
3. 공통 패키지에 모든 업무 로직을 넣는다
4. 사용 이유·변경 파급·순환과 불필요한 책임을 확인한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 사용 이유·변경 파급·순환과 불필요한 책임을 확인한다

공통 타입의 필요성과 과도한 결합을 실제 변경·의존 방향으로 평가합니다.

</details>


<a id="w270"></a>
### W270. Controller가 업무 처리를 위임해야 하는 계층은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=270)

1. HTML 템플릿
2. 브라우저 localStorage
3. Gradle 캐시
4. Service

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · Service

Controller는 HTTP 계약을 처리하고 Service가 업무 흐름·트랜잭션·인가를 조정합니다.

</details>


<a id="module-37"></a>
## 37. 비즈니스 규칙 다층 검증 매트릭스 (TRD 3.8, 3.11)

- [선택형 학습 파일](starter/37-business-rules/BusinessRulesChecklist.md)

<a id="w161"></a>
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


<a id="w162"></a>
### W162. 여러 계층에 검증을 두는 이유는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=162)

1. 검증이 많으면 트랜잭션이 필요 없어진다
2. 서로 다른 우회·호출 경로와 경쟁 요청을 방어하기 위해서다
3. 중복되는 모든 코드를 무조건 유지하기 위해서다
4. 화면 검증이 DB 무결성을 대체하기 때문이다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 서로 다른 우회·호출 경로와 경쟁 요청을 방어하기 위해서다

같은 정책을 일관되게 유지하면서 계층마다 다른 위험을 다룹니다.

</details>


<a id="w163"></a>
### W163. exists 검사 이후 같은 값이 다른 요청에서 저장되면?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=163)

1. DB unique 제약이 최종 중복 저장을 차단한다
2. exists 결과가 모든 트랜잭션에 영구 고정된다
3. 화면 색이 바뀌면 중복이 제거된다
4. 뒤 요청은 무조건 다른 값을 자동 생성한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · DB unique 제약이 최종 중복 저장을 차단한다

사전 조회와 저장 사이의 경쟁 상태는 DB 제약으로 다룹니다.

</details>


<a id="w164"></a>
### W164. 도메인 IllegalStateException을 API ErrorCode로 바꾸는 위치는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=164)

1. Entity가 HttpServletResponse를 직접 조작한다
2. CSS에서 HTTP status를 바꾼다
3. Service 등 명시한 경계에서 일관된 업무 예외로 변환한다
4. 브라우저가 DB 예외를 그대로 출력한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · Service 등 명시한 경계에서 일관된 업무 예외로 변환한다

현재 Service는 도메인 상태 실패를 INVALID_STATUS 등 안정적인 계약으로 변환합니다.

</details>


<a id="w220"></a>
### W220. 단가 0 이상 규칙의 다층 방어는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=220)

1. 브라우저 placeholder만 설정한다
2. 금액이 음수여도 서버는 저장한다
3. DTO의 DecimalMin과 도메인·Service의 값 검증을 맞춘다
4. 모든 금액을 비밀번호 필드로 옮긴다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · DTO의 DecimalMin과 도메인·Service의 값 검증을 맞춘다

외부 형식 검증과 다른 호출 경로의 불변식 보호를 함께 고려합니다.

</details>


<a id="w221"></a>
### W221. 주문 라인 최소 개수 규칙의 입력 1차 방어는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=221)

1. CSS margin 설정
2. DTO의 Size(min=1)와 중첩 입력 검증
3. RequestParam의 기본 포트 변경
4. 조회수 내림차순

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · DTO의 Size(min=1)와 중첩 입력 검증

입력 경계에서 검증하고 Service·도메인에서도 필요한 불변식을 보장합니다.

</details>


<a id="w222"></a>
### W222. 과거 공급 이력 때문에 BOTH→CUSTOMER를 금지하는 정책이라면 어디서 검사하는가?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=222)

1. 화면 select에서만 선택지를 숨긴다
2. 브라우저 localStorage만 확인한다
3. 엔티티가 HttpSession을 직접 읽는다
4. 관련 이력을 조회하는 Service 정책에서 검증한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 관련 이력을 조회하는 Service 정책에서 검증한다

이 문제는 해당 금지 정책을 전제로 합니다. 이력 조회가 필요한 업무 정책을 Service에서 조정합니다.

</details>


<a id="w249"></a>
### W249. 상태 규칙을 Service에만 두면 어떤 경로에서 깨질 수 있는가?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=249)

1. DTO에 NotBlank가 있으면 절대 깨지지 않는다
2. DB가 모든 주문 상태 전이를 자동 판단한다
3. 다른 호출부가 도메인 메서드를 직접 사용하면서 검증을 빠뜨릴 수 있다
4. 모든 경로가 반드시 같은 컨트롤러를 거친다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 다른 호출부가 도메인 메서드를 직접 사용하면서 검증을 빠뜨릴 수 있다

중요 불변식을 도메인 동작에 모으면 다른 진입 경로에서도 같은 규칙을 보호합니다.

</details>


<a id="w268"></a>
### W268. 컨트롤러에만 역할 검사를 두고 Service를 보호하지 않으면?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=268)

1. DB unique가 모든 인가를 대신한다
2. 메뉴 표시 조건이 모든 호출을 차단한다
3. 컨트롤러 없는 호출은 Java에서 불가능하다
4. 다른 진입 경로가 Service를 호출할 때 업무 인가를 우회할 수 있다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 다른 진입 경로가 Service를 호출할 때 업무 인가를 우회할 수 있다

서버 업무 경계에서 필요한 인가를 보장해야 합니다.

</details>


<a id="module-38"></a>
## 38. 트러블슈팅 워크북 (TRD 3.16)

- [선택형 학습 파일](starter/38-troubleshooting/Troubleshooting.md)

<a id="w165"></a>
### W165. 승인 중복·부분 저장·민감정보·LAZY·유형 오류·채번 경쟁을 다룰 때의 기준은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=165)

1. 모든 문제를 화면 새로 고침으로만 해결한다
2. 각 실패 경계를 재현하고 상태·트랜잭션·DTO·조회·검증·동시성 대책을 검증한다
3. 예외를 전부 무시한다
4. 비밀번호를 응답에 추가해 원인을 찾는다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 각 실패 경계를 재현하고 상태·트랜잭션·DTO·조회·검증·동시성 대책을 검증한다

실패 종류마다 다른 원인과 대책이 있으며 회귀 테스트로 근거를 남깁니다.

</details>


<a id="w166"></a>
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


<a id="w167"></a>
### W167. 본인이 겪은 장애를 포트폴리오에 쓰는 올바른 방식은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=167)

1. 겪지 않은 장애를 해결했다고 꾸민다
2. 다른 사람 수치를 자기 결과로 복사한다
3. 실제 재현·관찰·선택·검증 근거로 작성한다
4. 원인과 해결을 비밀번호 값으로 대신한다

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 실제 재현·관찰·선택·검증 근거로 작성한다

개인 기록의 내용은 사람마다 다르지만 증거에 근거한 작성 기준은 같습니다.

</details>


<a id="w261"></a>
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


<a id="w262"></a>
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


<a id="w263"></a>
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


<a id="w264"></a>
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


<a id="module-39"></a>
## 39. 면접 카드 + 커밋 + PR 템플릿 (TRD 3.18 ~ 3.20)

- [선택형 학습 파일](starter/39-interview-and-commit/InterviewAndCommit.md)

<a id="w168"></a>
### W168. 짧은 면접 기술 답변의 좋은 구성은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=168)

1. 문제·선택 이유·대안·코드나 테스트 근거를 연결한다
2. 라이브러리 이름만 외운다
3. 실패 조건을 모두 숨긴다
4. 실제 구현과 다른 정책을 확정 사실로 말한다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 문제·선택 이유·대안·코드나 테스트 근거를 연결한다

기술 선택을 구체적인 업무 상황과 검증에 연결합니다.

</details>


<a id="w169"></a>
### W169. 버그 수정·리팩터링·문서·테스트의 커밋 타입 조합은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=169)

1. feat·feat·feat·feat
2. style·docs·fix·chore
3. fix·refactor·docs·test
4. chore·style·test·refactor

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · fix·refactor·docs·test

feat는 기능, fix는 버그, refactor는 구조, docs는 문서, test는 테스트 변경을 표현합니다.

</details>


<a id="w170"></a>
### W170. 커밋 본문에서 선택 이유를 설명하는 목적은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=170)

1. diff만으로 보이지 않는 동기와 판단을 전달하기 위해서다
2. 비밀키를 공유하기 위해서다
3. 모든 변경 파일 내용을 다시 복사하기 위해서다
4. 테스트 실패를 숨기기 위해서다

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · diff만으로 보이지 않는 동기와 판단을 전달하기 위해서다

변경 내용은 diff로 확인하고 본문은 이유와 필요한 맥락을 전달합니다.

</details>


<a id="w171"></a>
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


<a id="w235"></a>
### W235. 이 프로젝트의 학습 목적을 가장 잘 설명한 것은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=235)

1. 이미지 편집만 제공한다
2. 문제집이 MySQL 서버를 대체한다
3. 비밀번호를 평문으로 공유하는 도구다
4. SCM 마스터·주문 상태·권한·재고 흐름을 Java/Spring으로 구현하고 검증한다

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · SCM 마스터·주문 상태·권한·재고 흐름을 Java/Spring으로 구현하고 검증한다

실제 기능과 코드·테스트 근거에 맞게 프로젝트 목적을 설명합니다.

</details>


<a id="w240"></a>
### W240. 이미 승인된 발주 재승인을 차단한 커밋의 적절한 타입은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=240)

1. fix
2. style
3. docs
4. chore

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · fix

오류 동작을 수정했으므로 fix 타입이 자연스럽습니다.

</details>


<a id="w241"></a>
### W241. 동작을 유지하며 상태 전이 코드를 도메인 메서드로 옮긴 타입은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=241)

1. feat
2. refactor
3. docs
4. style

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · refactor

기능·결과를 유지하는 구조 개선은 refactor입니다.

</details>


<a id="w242"></a>
### W242. README 실행 방법을 보완한 커밋의 타입은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=242)

1. docs
2. fix
3. test
4. feat

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · docs

문서 변경을 나타내는 docs 타입을 사용합니다.

</details>


<a id="w243"></a>
### W243. 발주 승인 권한 실패 테스트를 추가한 커밋의 타입은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=243)

1. style
2. feat
3. test
4. docs

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · test

동작 검증용 테스트 변경은 test입니다.

</details>


<a id="w244"></a>
### W244. 구체적인 버그 수정 커밋 메시지로 적절한 것은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=244)

1. update
2. wip
3. fix: 아무거나 수정
4. fix: REQUESTED가 아닌 발주의 승인 요청 차단

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · fix: REQUESTED가 아닌 발주의 승인 요청 차단

변경 결과·조건·목적을 이해할 수 있는 구체적인 제목이 검토와 추적을 돕습니다.

</details>


<a id="w266"></a>
### W266. 자기소개 문단에 넣을 적절한 기술 설명은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=266)

1. 겪지 않은 장애 수치를 임의로 넣는다
2. 실제 구현한 주문·권한·재고 규칙과 검증 근거를 자신의 역할에 맞게 요약한다
3. 모든 팀원의 성과를 본인 성과로 말한다
4. 기술명만 나열하고 동작은 설명하지 않는다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 실제 구현한 주문·권한·재고 규칙과 검증 근거를 자신의 역할에 맞게 요약한다

개인 표현은 달라도 사실에 맞는 역할·선택·검증을 연결하는 기준은 같습니다.

</details>


<a id="w269"></a>
### W269. 객관식 학습 완료를 확인하는 적절한 방법은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=269)

1. 답 번호만 외우면 모든 구현이 검증된다
2. 오답을 다시 풀고 해설의 규칙을 실제 코드·테스트와 대조한다
3. 정답 화면을 본 횟수만 센다
4. 문제 데이터를 임의로 삭제한다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 오답을 다시 풀고 해설의 규칙을 실제 코드·테스트와 대조한다

객관식 채점과 코드 구현의 검증은 각각의 학습·확인 절차를 갖습니다.

</details>


<a id="w359"></a>
### W359. 출고지별 재고로 수주 기능을 확장하는 요구에서 적절한 작업은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=359)

1. 출고지 문자열만 화면에 추가하고 서버는 그대로 둔다
2. 재고 키·출고지 권한·예약/차감·동시성·테스트 계약을 함께 설계한다
3. 재고 부족 검사를 삭제한다
4. 모든 창고의 재고를 무조건 같은 값으로 만든다

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 재고 키·출고지 권한·예약/차감·동시성·테스트 계약을 함께 설계한다

추가 요구의 데이터·권한·상태·트랜잭션 경계를 명시해 실제 구현과 검증을 연결합니다.

</details>


<a id="w396"></a>
### W396. 학습 루프의 목표 단계에서 먼저 정할 내용은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=396)

1. 취업 역량이나 사용자 가치와 연결된 한 문장 목표
2. 검증되지 않은 구현의 성공 선언
3. 기존 데이터의 무조건 삭제
4. 변경할 파일 수를 최대로 늘리는 계획

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 취업 역량이나 사용자 가치와 연결된 한 문장 목표

목표는 기능 수보다 증명할 역량이나 개선할 가치와 연결되어야 합니다.

</details>


<a id="w397"></a>
### W397. 루프의 기준선 단계에서 확보할 증거는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=397)

1. 현재 코드·문서·관련 테스트와 실제 동작
2. 예상만으로 작성한 최종 성공 보고
3. 원인 확인 전의 임시 오류 숨김
4. 모든 참조 구현의 삭제

<details>
<summary>정답·해설 보기</summary>

**정답: 1번** · 현재 코드·문서·관련 테스트와 실제 동작

기준선은 변경 전 동작과 근거를 확인해 이후 검증의 출발점을 만듭니다.

</details>


<a id="w398"></a>
### W398. 계약 단계에서 함께 정리할 요소는?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=398)

1. 화면 색상만
2. 성공 메시지의 글꼴만
3. 입력·출력·권한·상태·실패 조건
4. 개발자의 개인 비밀번호

<details>
<summary>정답·해설 보기</summary>

**정답: 3번** · 입력·출력·권한·상태·실패 조건

규칙표와 API·상태 계약을 통해 성공 경로와 실패 경로의 기대를 명확히 합니다.

</details>


<a id="w399"></a>
### W399. 실패 증거 단계의 목적에 맞는 산출물은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=399)

1. 실패를 지운 로그만
2. 정답 번호를 외운 횟수
3. 검증 없는 완료 표시
4. 잘못된 구현을 잡는 테스트나 재현 절차

<details>
<summary>정답·해설 보기</summary>

**정답: 4번** · 잘못된 구현을 잡는 테스트나 재현 절차

테스트나 재현 절차는 오류 조건을 실제로 드러내고 수정 효과를 확인할 근거가 됩니다.

</details>


<a id="w400"></a>
### W400. 계약을 확인한 뒤 최소 변경 단계에서 선택할 작업은?

[브라우저에서 풀기](sw-quiz/index.html?bank=workbook&question=400)

1. 문제와 무관한 모든 기능의 전면 재작성
2. 계약을 만족하는 필요한 코드·문서 변경
3. 테스트와 오류 처리를 일괄 제거
4. 사용자의 개인 풀이를 무조건 덮어쓰기

<details>
<summary>정답·해설 보기</summary>

**정답: 2번** · 계약을 만족하는 필요한 코드·문서 변경

변경 범위를 계약에 맞춰 좁혀 결과를 검토하고 검증할 수 있게 합니다.

</details>


<a id="w401"></a>
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


<a id="w402"></a>
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


<a id="w403"></a>
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
