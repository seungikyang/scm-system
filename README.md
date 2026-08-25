# Supply Chain Management System (SCM) · Career Workbook

> **먼저 열기:** [`index.html`](index.html) — 40개 학습 모듈, 검색·필터, 진도 체크와 취업 준비 자료를 한 화면에서 제공하는 HTML 목차입니다.

이 저장소의 1차 목적은 Java/Spring 기반 SI·백엔드 취업을 준비하면서 직접 구현하고 설명하는 **SCM 실전 워크북**입니다. 거래처/카테고리/품목 마스터와 발주(구매)의 작성 → 결재 요청 → 승인/반려 → 입고 → 재고 부족 조회 흐름을 학습 문제와 실행 가능한 참조 구현으로 함께 제공합니다.

> SI 실무에서 자주 등장하는 SCM/구매 관리 흐름을 학습·구현한 신입 포트폴리오 프로젝트입니다.

> **이 저장소는 세 갈래로 구성됩니다.**
> - **`index.html` — 학습 시작점.** HTML 목차에서 트랙과 모듈을 선택하고 브라우저에 진도를 저장합니다.
> - **`practice/` — 주 학습 공간.** 요구사항과 핵심 빈칸(`TODO`/`____`)을 직접 채우며 설계 이유까지 기록합니다 → [`practice/README.md`](practice/README.md).
> - **`src/` · `docs/` — 실행 가능한 참조 구현과 증거.** 풀이 후 비교하고 테스트·설계 결정을 확인하는 포트폴리오 결과물입니다.
>
> 학습 순서는 **문제 예측 → 직접 구현 → 실패 테스트 → 참조 구현 비교 → 면접 설명**입니다. 참조 코드를 먼저 베끼는 방식은 권장하지 않습니다.

---

## 목차

0. [저장소 작업 경계](#저장소-작업-경계)
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

## 저장소 작업 경계

프로그램·테스트·정식 문서는 임시 복사본이 아니라 **이 Git 저장소 루트**에서만 변경합니다.

```bash
cd "$(git rev-parse --show-toplevel)"
git config core.hooksPath .githooks       # clone마다 1회: pre-commit 경계 검사 활성화
./scripts/verify-repository-boundary.sh   # 수동 검사
./gradlew verifyRepositoryBoundary        # Gradle/CI 검사
```

`.omc/`, `.playwright-cli/`, `.gradle/`, `build/`, `bin/`, `out/`, `output/`, `_workspace*`, `tmp/`, `temp/`는 로그·캐시·스크린샷 같은 재생성 가능한 임시 산출물 전용입니다. 이 경로의 파일은 Git에서 제외되며, 보존할 결과만 검토 후 `src/`, `docs/`, `practice/`, `.github/` 또는 루트 설정 파일로 옮깁니다. `practice/workspace/`는 Git에서 제외된 개인 풀이 공간이므로 자동 정리하지 않습니다. 에이전트 세부 규칙은 [`AGENTS.md`](AGENTS.md)를 따릅니다.

---

## 1. 프로젝트 소개

- 거래처/카테고리/품목 마스터를 CRUD로 관리하고, 이를 참조해 발주서(헤더 + 라인)를 작성한다.
- 발주는 6개 상태(`DRAFT`/`REQUESTED`/`APPROVED`/`REJECTED`/`RECEIVED`/`CANCELED`)를 가지며, 상태 전이는 도메인 메서드로 캡슐화해 잘못된 전이를 차단한다.
- 입고(`RECEIVED`) 시 라인별 재고(`Stock`)를 같은 트랜잭션에서 증가시키고, 현재고·안전재고·부족수량을 화면/API로 조회한다.
- 인증은 세션 기반이고 인터셉터·Service가 인증/인가를 담당합니다. Spring Security는 웹 CSRF·보안 헤더를, BCrypt는 비밀번호 해시를 담당하며 권한은 `USER`/`ADMIN`/`MANAGER` 3등급입니다.

## 2. 기술 스택

| 구분 | 기술 |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.1.1 / Spring Framework 7.0.x |
| JSON | Jackson 3 (`tools.jackson`) |
| ORM | Spring Data JPA (Hibernate) |
| View | Thymeleaf (순수 프래그먼트, layout-dialect 미사용) |
| DB | H2 (기본, in-memory) / MySQL + Flyway (프로필) |
| 인증/보안 | 세션 + 인터셉터 인가, Spring Security CSRF/보안 헤더, BCrypt |
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
| 재고(Stock) | 운영 품목 현재고, 안전재고 이하 필터, 부족수량·상태, 요약 API |
| 대시보드 | 거래처/품목/카테고리 수, 발주 대기·내 발주·부족 재고 집계 |

> 발주 작성 시 `totalAmount`/`lineAmount`는 **클라이언트 입력을 신뢰하지 않고 서버에서 재계산**한다(`lineAmount = quantity × unitPrice`, `totalAmount = Σ lineAmount`).

## 4. 실행 방법

> **가장 쉬운 방법은 [4.4 Docker](#44-docker로-실행-jdk-설치-불필요)입니다.** 로컬에 JDK 17/Gradle을 설치하지 않아도 컨테이너 안에서 빌드·실행됩니다.

### 4.1 요구사항 (로컬 실행 시)

- **JDK 17 이상** (Spring Boot 4.1.1의 최소 요구사항, macOS 배포 워크북은 Java 21 사용)
- 저장소에 포함된 Gradle Wrapper 9.7.1 (`./gradlew`)
- Docker로 실행할 경우 위 요구사항 없이 Docker Desktop만 있으면 됩니다.

### 4.2 빌드 및 테스트

Gradle Wrapper가 포함되어 있어 시스템 Gradle 설치 없이 빌드할 수 있습니다.

```bash
./gradlew clean build
./gradlew portfolioCheck   # 전체 테스트 + 실행 가능한 bootJar 품질 게이트
./gradlew mysqlSchemaTest  # 실행 중인 MySQL에서 Flyway + JPA validate 검증
```

### 4.3 애플리케이션 실행

```bash
./gradlew bootRun        # macOS / Linux
gradlew.bat bootRun      # Windows
```

| 항목 | 값 |
|---|---|
| 애플리케이션 URL | http://localhost:8080 |
| 로그인 화면 | http://localhost:8080/login |
| H2 콘솔 | 기본 비활성(아래 환경변수로 명시적으로 활성화) |
| H2 JDBC URL | `jdbc:h2:mem:scm` (user: `sa`, password: 빈 값) |

- 기본 DB는 **H2 in-memory**이며, `ddl-auto=create`로 기동 시 스키마를 새로 생성합니다(데이터는 재기동 시 초기화).
- 기동 시 `DataInitializer`가 시드 데이터를 1회 생성합니다(이미 있으면 skip).
- H2 콘솔이 필요하면 `H2_CONSOLE_ENABLED=true ./gradlew bootRun`으로 실행하세요. 애플리케이션 ADMIN 로그인 후 `/h2-console`에 접근할 수 있습니다.

### 4.4 Docker로 실행 (JDK 설치 불필요)

JDK 17/Gradle이 없어도 됩니다. **Docker Desktop만 실행**되어 있으면 멀티스테이지 Dockerfile이 `gradle:9.7.1-jdk17-noble` 이미지에서 빌드하고 슬림 JRE 17로 구동합니다.

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
- MySQL 스키마는 [`db/migration/mysql`](src/main/resources/db/migration/mysql) 아래의 Flyway 버전 스크립트만 변경하며, Hibernate는 `ddl-auto=validate`로 매핑 일치 여부만 확인합니다.
- 이전 `ddl-auto=update` 버전에서 만든 Docker 볼륨에는 Flyway 이력이 없습니다. 데모 데이터라면 `docker compose down -v`로 한 번 초기화한 뒤 다시 기동하세요. 보존할 데이터가 있다면 임의로 baseline 처리하지 말고 백업·스키마 대조 후 마이그레이션해야 합니다.
- 기본 H2와 데모용 Docker Compose는 동일한 시드 계정·데이터를 생성합니다(MySQL 볼륨이 유지되면 최초 1회).
- 일반 MySQL 프로필은 알려진 기본 계정 생성을 막기 위해 시드가 기본 비활성입니다. 데모용 `docker-compose.yml`만 `SCM_SEED_ENABLED=true`를 명시합니다. 실제 운영에서는 이 값을 켜지 마세요.

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
| `GET /stocks` | 현재고·안전재고 부족 조회 | 로그인 |
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
├── dto             # auth / user / partner / category / item / purchaseorder / stock 하위 패키지
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
| 재고 현황 / 안전재고 부족 조회 | ⭕ | ⭕ | ⭕ |

> 발주 승인/반려/입고는 PRD 본문(3.11.3, ADMIN 한정)과 달리 **ADMIN + MANAGER**로 확정되었습니다. 사유는 [9. 제약](#9-현재-구현-범위--제약) 및 [`docs/TROUBLESHOOTING.md`](docs/TROUBLESHOOTING.md)의 PRD-구현 차이표를 참고하세요.

## 9. 현재 구현 범위 / 제약

### 구현된 모듈 (1차)

- 기반/공통 인프라(인증·예외·페이징·감사), 마스터(거래처·카테고리·품목), 발주(PurchaseOrder), 입고와 재고 조회.

### 미구현 (후속)

- **수주(SalesOrder)**, **공지사항(Notice)** 모듈은 미구현입니다(PRD에는 정의되어 있으나 1차 범위 외).
- 발주서 라인 수정 API는 제공하지 않습니다(수정이 필요하면 취소 후 재작성).

### 빌드 환경

- 이 작업 환경에서는 Gradle 데몬의 로컬 소켓 생성이 제한되어 JUnit Platform으로 기본 테스트 93개를 직접 실행했습니다. GitHub CI가 `portfolioCheck`, MySQL 마이그레이션, 컨테이너 빌드를 각각 검증합니다.
- 로컬에 JDK 17이 없으면 [Docker](#44-docker로-실행-jdk-설치-불필요)의 멀티스테이지 빌드를 사용할 수 있습니다.

## 10. 문서

| 문서 | 내용 |
|---|---|
| [`index.html`](index.html) | 40개 모듈 HTML 목차, 검색·필터, 브라우저 진도 관리 |
| [`docs/INDEX.md`](docs/INDEX.md) | 처음 읽는 순서와 문서 동기화 규칙 |
| [`docs/LOOP_ENGINEERING.md`](docs/LOOP_ENGINEERING.md) | 학습·기능·문서·포트폴리오 반복 루프와 완료 조건 |
| [`docs/PORTFOLIO_GUIDE.md`](docs/PORTFOLIO_GUIDE.md) | 3분 데모, 이력서 문장, 면접 질문과 제출 체크리스트 |
| [`docs/API_SPEC.md`](docs/API_SPEC.md) | REST API 명세(인증/사용자/마스터/발주/재고), 공통 에러 형식, ErrorCode 표 |
| [`docs/ERD.md`](docs/ERD.md) | 엔티티/테이블 관계, 컬럼 요약, enum 정의 |
| [`docs/STATE_MACHINE.md`](docs/STATE_MACHINE.md) | 발주 상태 전이표(T1~T8), 다이어그램, 금지 전이 |
| [`docs/TROUBLESHOOTING.md`](docs/TROUBLESHOOTING.md) | 빌드 환경, OSIV/낙관적 락/채번 이슈, PRD-구현 차이표, QA MINOR 4건 |
| [`history.html`](history.html) | 프로젝트 선택과 구현 과정을 시간순으로 기록한 히스토리 |

> 원본 요구사항/설계는 [`scm_system_PRD_TRD.md`](scm_system_PRD_TRD.md)에 있습니다(원본 보존, 본 문서들은 구현 실제 기준으로 작성됨).
