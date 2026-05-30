# Supply Chain Management System (SCM)

Java Spring Boot 기반 공급망 관리 시스템(SCM)입니다.
거래처/카테고리/품목 마스터 관리와 발주(구매)의 작성 → 결재 요청 → 승인/반려 → 입고(재고 반영) → 취소에 이르는 다단계 상태 전이 프로세스를 구현했습니다.

> SI 실무에서 자주 등장하는 SCM/구매 관리 흐름을 학습·구현한 신입 포트폴리오 프로젝트입니다.

> **이 저장소는 두 갈래로 구성됩니다.**
> - **`src/` · `docs/` — 실제로 동작하는 SCM 구현(포트폴리오 결과물).** Docker 또는 JDK 17로 바로 실행할 수 있습니다 → [실행 방법](#4-실행-방법).
> - **`practice/` — 같은 요구사항을 직접 빈칸(`TODO`/`____`)을 채우며 익히는 학습 워크북.** 완성 코드를 베끼지 않고 손으로 구현해보는 용도입니다 → [`practice/README.md`](practice/README.md).
>
> 즉 동작하는 구현과 공부용 워크북이 **분리되어 공존**합니다.

---

## 목차

1. [프로젝트 소개](#1-프로젝트-소개)
2. [기술 스택](#2-기술-스택)
3. [주요 기능](#3-주요-기능)
4. [실행 방법](#4-실행-방법)
5. [시드 계정](#5-시드-계정)
6. [화면 경로](#6-화면-경로)
7. [패키지 구조](#7-패키지-구조)
8. [권한 매트릭스 요약](#8-권한-매트릭스-요약)
9. [현재 구현 범위 / 제약](#9-현재-구현-범위--제약)
10. [문서](#10-문서)

---

## 1. 프로젝트 소개

- 거래처/카테고리/품목 마스터를 CRUD로 관리하고, 이를 참조해 발주서(헤더 + 라인)를 작성한다.
- 발주는 6개 상태(`DRAFT`/`REQUESTED`/`APPROVED`/`REJECTED`/`RECEIVED`/`CANCELED`)를 가지며, 상태 전이는 도메인 메서드로 캡슐화해 잘못된 전이를 차단한다.
- 입고(`RECEIVED`) 시 라인별 재고(`Stock`)를 같은 트랜잭션에서 증가시켜 정합성을 보장한다.
- 인증은 세션 기반(Spring Security 풀 적용 없이 BCrypt만 사용)이며, 권한은 `USER`/`ADMIN`/`MANAGER` 3등급이다.

## 2. 기술 스택

| 구분 | 기술 |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2.5 |
| ORM | Spring Data JPA (Hibernate) |
| View | Thymeleaf (순수 프래그먼트, layout-dialect 미사용) |
| DB | H2 (기본, in-memory) / MySQL (프로필) |
| 인증 | 세션 + `spring-security-crypto`(BCrypt) |
| Build | Gradle |
| Test | JUnit 5 + Mockito |
| 컨테이너 | Docker (멀티스테이지 빌드) / docker-compose (app + MySQL) |
| 기타 | Lombok, Bean Validation |

## 3. 주요 기능

| 영역 | 기능 |
|---|---|
| 인증/세션 | 로그인, 로그아웃, 내 정보 조회, 비밀번호 변경 |
| 거래처(Partner) | 등록/수정/상세/목록(검색·페이징)/비활성화 |
| 카테고리(Category) | 등록/수정/상세(소속 품목 포함)/목록/삭제(소속 품목 있으면 차단) |
| 품목(Item) | 등록/수정/상세/목록(검색·페이징)/단종 처리 |
| 발주(PurchaseOrder) | 작성(헤더+라인 동시 저장, 서버 금액 재계산), 결재 요청, 승인, 반려(사유 필수), 입고(+재고 반영), 취소 |
| 발주 조회 | 내 발주 목록, 발주 상세(헤더+라인+타임라인), 관리자 발주 목록(상태/거래처 필터) |
| 대시보드 | 거래처/품목/카테고리 수, 발주 대기(REQUESTED) 수, 내 발주 수 집계 |

> 발주 작성 시 `totalAmount`/`lineAmount`는 **클라이언트 입력을 신뢰하지 않고 서버에서 재계산**한다(`lineAmount = quantity × unitPrice`, `totalAmount = Σ lineAmount`).

## 4. 실행 방법

> **가장 쉬운 방법은 [4.4 Docker](#44-docker로-실행-jdk-설치-불필요)입니다.** 로컬에 JDK 17/Gradle을 설치하지 않아도 컨테이너 안에서 빌드·실행됩니다(이 저장소가 JDK 8 환경에서 생성되어 로컬 빌드가 어려운 경우 특히 권장).

### 4.1 요구사항 (로컬 실행 시)

- **JDK 17 필수** (Spring Boot 3.2.5는 JDK 17 이상에서만 동작)
- Gradle 8.5 (또는 IDE의 Gradle 통합)
- Docker로 실행할 경우 위 요구사항 없이 Docker Desktop만 있으면 됩니다.

### 4.2 gradle-wrapper.jar 생성 (최초 1회)

이 저장소에는 `gradle-wrapper.jar` 바이너리가 포함되어 있지 않습니다(생성 환경 제약).
`gradlew`/`gradlew.bat`/`gradle-wrapper.properties`는 이미 있으므로, 아래 중 하나로 wrapper jar를 생성하세요.

```bash
# 로컬에 Gradle 8.5가 설치된 경우
gradle wrapper --gradle-version 8.5
```

또는 IntelliJ IDEA / Eclipse에서 **Gradle 프로젝트로 import**하면 wrapper jar가 자동 생성됩니다.

### 4.3 애플리케이션 실행

```bash
./gradlew bootRun        # macOS / Linux
gradlew.bat bootRun      # Windows
```

| 항목 | 값 |
|---|---|
| 애플리케이션 URL | http://localhost:8080 |
| 로그인 화면 | http://localhost:8080/login |
| H2 콘솔 | http://localhost:8080/h2-console |
| H2 JDBC URL | `jdbc:h2:mem:scm` (user: `sa`, password: 빈 값) |

- 기본 DB는 **H2 in-memory**이며, `ddl-auto=create`로 기동 시 스키마를 새로 생성합니다(데이터는 재기동 시 초기화).
- 기동 시 `DataInitializer`가 시드 데이터를 1회 생성합니다(이미 있으면 skip).

### 4.4 Docker로 실행 (JDK 설치 불필요)

JDK 17/Gradle이 없어도 됩니다. **Docker Desktop만 실행**되어 있으면 멀티스테이지 Dockerfile이 `gradle:8.5-jdk17` 이미지에서 빌드하고 슬림 JRE 17로 구동합니다.

**(A) H2 in-memory — 가장 간단 (외부 DB 불필요)**

```bash
docker build -t scm-system .
docker run --rm -p 8080:8080 scm-system
```

**(B) MySQL 영속 DB — docker-compose (앱 + MySQL)**

```bash
docker compose up --build        # http://localhost:8080 , MySQL 영속 저장
docker compose down              # 중지 (DB 볼륨 유지)
docker compose down -v           # 중지 + DB 초기화
```

| 항목 | 값 |
|---|---|
| 애플리케이션 URL | http://localhost:8080 |
| 활성 프로필 | (A) 기본(H2) / (B) `mysql` (`SPRING_PROFILES_ACTIVE=mysql`, compose가 주입) |
| MySQL 접속(B) | host `localhost:3306`, db `scm`, user `scm` / pw `scm` (root pw `root`) |

- MySQL 프로필 설정은 [`src/main/resources/application-mysql.yml`](src/main/resources/application-mysql.yml)에 있으며 `DB_HOST`/`DB_PORT`/`DB_NAME`/`DB_USER`/`DB_PASSWORD` 환경변수로 재정의됩니다.
- 시드 계정·데이터는 H2/MySQL 모두 동일하게 기동 시 생성됩니다(MySQL은 볼륨이 유지되면 최초 1회).

## 5. 시드 계정

비밀번호는 세 계정 모두 **`password1!`** 입니다.

| 이메일 | 비밀번호 | 권한 | 이름 |
|---|---|---|---|
| `admin@scm.com` | `password1!` | ADMIN | 관리자 |
| `manager@scm.com` | `password1!` | MANAGER | 매니저 |
| `user@scm.com` | `password1!` | USER | 홍길동 |

함께 생성되는 마스터 시드: 거래처 4건(공급사 2 / 고객사 1 / BOTH 1), 카테고리 3건(전자부품·포장재·원자재), 품목 8건(ACTIVE 7 + 단종 시연용 `ITM-999` 1).

## 6. 화면 경로

| 경로 | 화면 | 접근 |
|---|---|---|
| `GET /login` | 로그인 | 전체 |
| `GET /` | 대시보드 | 로그인 |
| `GET /me` | 내 정보 | 로그인 |
| `GET /partners` | 거래처 목록(검색·페이징) | 로그인(등록/수정은 ADMIN) |
| `GET /partners/new`, `/{id}/edit` | 거래처 등록·수정 폼 | ADMIN |
| `GET /partners/{id}` | 거래처 상세 | 로그인 |
| `GET /categories` | 카테고리 목록 | 로그인(등록/수정/삭제는 ADMIN) |
| `GET /categories/{id}` | 카테고리 상세(소속 품목) | 로그인 |
| `GET /items` | 품목 목록(검색·페이징) | 로그인(등록/수정/단종은 ADMIN) |
| `GET /items/{id}` | 품목 상세 | 로그인 |
| `GET /purchase-orders/new` | 발주서 작성 | 로그인 |
| `GET /purchase-orders/my` | 내 발주 목록 | 로그인 |
| `GET /purchase-orders/{poId}` | 발주 상세 | 작성자 본인 또는 ADMIN/MANAGER |
| `GET /admin/purchase-orders` | 관리자 발주 목록(승인 관리) | ADMIN/MANAGER |

> REST API 전체 명세는 [`docs/API_SPEC.md`](docs/API_SPEC.md)를 참고하세요.

## 7. 패키지 구조

```text
com.example.scm
├── ScmApplication.java
├── common
│   ├── auth        # LoginUser(record), @CurrentUser, CurrentUserArgumentResolver, LoginInterceptor, Authz, SessionConst
│   ├── entity      # BaseTimeEntity (createdAt/updatedAt 감사)
│   ├── exception   # ErrorCode, BusinessException, ApiExceptionHandler, WebExceptionHandler
│   └── response    # ErrorResponse, PageResponse
├── config          # WebMvcConfig, JpaAuditingConfig, PasswordConfig
├── controller
│   ├── api         # @RestController — /api/** (JSON)
│   └── web         # @Controller — Thymeleaf 화면 + 폼
├── domain          # User, Partner, Category, Item, PurchaseOrder, PurchaseOrderLine, Stock
│   └── enums       # UserRole, PartnerType, PartnerStatus, ItemStatus, PurchaseOrderStatus
├── dto             # auth / user / partner / category / item / purchaseorder 하위 패키지
├── repository      # Spring Data JPA 리포지토리 (+ spec: Specification)
├── service         # 비즈니스 로직 (@Transactional), OrderNumberGenerator
└── init            # DataInitializer (시드)
```

- **두 컨트롤러 레이어**: `controller.api`(REST/JSON)와 `controller.web`(Thymeleaf)이 **동일 Service**를 호출한다(로직 중복 없음).
- **OSIV off**(`open-in-view: false`): 모든 표시값(거래처명/품목명/작성자명 등)은 Service 트랜잭션 안에서 DTO에 채워 내려준다.
- **교차 애그리거트 참조 = ID(Long) 보관**: 발주는 `partnerId`/`writerId`/`itemId`를 ID로만 보관하고, 모듈 내부 헤더-라인(`PurchaseOrder` ↔ `PurchaseOrderLine`)만 JPA 연관관계로 매핑한다.

## 8. 권한 매트릭스 요약

| 액션 | USER | ADMIN | MANAGER |
|---|:---:|:---:|:---:|
| 마스터(거래처/카테고리/품목) 등록·수정·삭제 | ❌ | ⭕ | ❌ |
| 마스터 조회 | ⭕ | ⭕ | ⭕ |
| 발주 작성 / 결재 요청 / 취소 | ⭕(작성자 본인) | ⭕(작성자 본인) | ⭕(작성자 본인) |
| 내 발주 목록 / 상세(본인) | ⭕ | ⭕ | ⭕ |
| 관리자 발주 목록 / 상세(전체) | ❌ | ⭕ | ⭕ |
| 발주 승인 / 반려 / 입고 | ❌ | ⭕ | ⭕ |

> 발주 승인/반려/입고는 PRD 본문(3.11.3, ADMIN 한정)과 달리 **ADMIN + MANAGER**로 확정되었습니다. 사유는 [9. 제약](#9-현재-구현-범위--제약) 및 [`docs/TROUBLESHOOTING.md`](docs/TROUBLESHOOTING.md)의 PRD-구현 차이표를 참고하세요.

## 9. 현재 구현 범위 / 제약

### 구현된 모듈 (1차)

- 기반/공통 인프라(인증·예외·페이징·감사), 마스터(거래처·카테고리·품목), 발주(PurchaseOrder) + 재고(Stock) 반영.

### 미구현 (후속)

- **수주(SalesOrder)**, **공지사항(Notice)** 모듈은 미구현입니다(PRD에는 정의되어 있으나 1차 범위 외).
- 재고(Stock) 조회 화면/API는 미구현입니다(입고 시 증가 로직만 존재).
- 발주서 라인 수정 API는 제공하지 않습니다(수정이 필요하면 취소 후 재작성).

### 빌드 환경 주의

- **이 저장소는 JDK 8 환경에서 생성되어 로컬 빌드가 검증되지 않았습니다.** 코드는 Java 17 사양으로 작성되었고, 임포트/타입/시그니처는 수기 검증을 거쳤습니다(QA 정적 검증 결과 BLOCKER/MAJOR 0건).
- **권장: [Docker](#44-docker로-실행-jdk-설치-불필요)로 실행하면 로컬에 JDK 17/Gradle/`gradle-wrapper.jar`가 없어도 컨테이너 안에서 빌드·실행되어 이 제약을 우회합니다.**
- 로컬에서 직접 빌드하려면: JDK 17 설치 후 `gradle-wrapper.jar`를 [4.2](#42-gradle-wrapperjar-생성-최초-1회)의 절차로 생성한 뒤 `./gradlew bootRun`(또는 `compileJava`/`test`)을 실행하세요.

## 10. 문서

| 문서 | 내용 |
|---|---|
| [`docs/API_SPEC.md`](docs/API_SPEC.md) | REST API 명세(인증/사용자/거래처/카테고리/품목/발주), 공통 에러 형식, ErrorCode 표 |
| [`docs/ERD.md`](docs/ERD.md) | 엔티티/테이블 관계, 컬럼 요약, enum 정의 |
| [`docs/STATE_MACHINE.md`](docs/STATE_MACHINE.md) | 발주 상태 전이표(T1~T8), 다이어그램, 금지 전이 |
| [`docs/TROUBLESHOOTING.md`](docs/TROUBLESHOOTING.md) | 빌드 환경, OSIV/낙관적 락/채번 이슈, PRD-구현 차이표, QA MINOR 4건 |

> 원본 요구사항/설계는 [`scm_system_PRD_TRD.md`](scm_system_PRD_TRD.md)에 있습니다(원본 보존, 본 문서들은 구현 실제 기준으로 작성됨).
