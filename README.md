# SCM 프로그램과 SW 기술 4지선다 문제집 실행 방법

> [프로그램 실행](#a-scm-프로그램-실행) · [SW 기술 3000제 실행](#b-sw-기술-4지선다-문제집-실행) · [상세 실행 방법](#4-실행-방법) · [학습 목차](index.html)

이 문서는 저장소 **루트 폴더의 `README.md`**입니다. SCM 업무 프로그램과 SW 기술 4지선다 3,000문항 문제집의 **준비 → 실행 → 사용 → 종료** 방법을 안내합니다.

처음 사용하는 경우 아래 **바로 실행하기**의 명령부터 따라 하세요. 운영체제별 상세 안내는 [A. SCM 프로그램 실행](#a-scm-프로그램-실행)과 [B. SW 기술 4지선다 문제집 실행](#b-sw-기술-4지선다-문제집-실행)에 있습니다.

## 바로 실행하기

명령은 `README.md`, `gradlew`, `build.gradle`이 있는 **scm-system 루트 폴더**에서 실행합니다. 현재 macOS 작업 폴더는 아래 경로입니다.

```bash
cd scm-system
```

다른 컴퓨터에서는 내려받은 `scm-system` 폴더를 터미널에서 엽니다. Windows PowerShell에서는 파일 탐색기로 해당 폴더를 연 뒤 **터미널에서 열기**를 선택할 수 있습니다. 다음 파일이 보이면 올바른 위치입니다.

```text
scm-system/
├── README.md                   # 이 실행 안내
├── gradlew                     # macOS / Linux 프로그램 실행
├── gradlew.bat                 # Windows 프로그램 실행
├── build.gradle
├── index.html                  # 전체 학습 목차
└── practice/sw-quiz/
    ├── index.html              # SW 기술 3000제 시작 화면
    ├── questions.js            # SW 기술 문제 데이터
    ├── workbook-questions.js   # SCM 모듈 문제 데이터
    ├── study-state.js          # 진도·답안 저장과 복습 상태
    ├── quiz.js
    └── quiz.css
```

아래 실행 명령은 **macOS에서 저장소 루트 폴더를 연 터미널** 기준입니다.

| 실행 대상 | 준비물 | 실행 명령 | 실행 후 열 주소·파일 | 종료 방법 |
|---|---|---|---|---|
| SCM 프로그램 | JDK 17 이상 | `./gradlew bootRun` | [로그인 화면](http://localhost:8080/login) | 실행한 터미널에서 `Ctrl+C` |
| SW 기술 4지선다 3000제 | 웹 브라우저 | `open practice/sw-quiz/index.html` | [문제집 시작 화면](practice/sw-quiz/index.html) → **SW 기술 · 3,000제** 선택 | 브라우저 탭 닫기 |
| SCM 모듈 4지 선다 403제 | 웹 브라우저 | `open practice/sw-quiz/index.html` | **나의 문제집 → SCM 워크북 · 403제** 선택 또는 [웹 워크북 열기](practice/sw-quiz/index.html?bank=workbook) | 브라우저 탭 닫기 |

**문제 풀이 화면은 `practice/sw-quiz/index.html`입니다.** 루트의 [`index.html`](index.html)은 전체 학습 목차이며, 목차의 문제집 링크로도 풀이 화면에 들어갈 수 있습니다.

웹 문제집은 **과목별 학습·보기 선택·정오답과 해설·오답 재풀이·진도 자동 저장**을 제공합니다. 선택 중인 보기와 마지막 학습 조건도 복원되며, 하단의 **기록 내보내기 / 가져오기**로 기기 간 기록을 옮길 수 있습니다. [이용 안내](practice/README.md#시작하기)

현재 Mac에서는 아래 명령을 그대로 복사해 실행할 수 있습니다. Windows·Linux 명령과 설치 확인은 각각 [A](#a-scm-프로그램-실행), [B](#b-sw-기술-4지선다-문제집-실행)에 있습니다.

**① SCM 업무 프로그램 시작**

```bash
cd scm-system
./gradlew bootRun
```

1. 터미널에 `Started ScmApplication`이 표시될 때까지 기다립니다. 처음에는 필요한 파일을 다운로드할 수 있습니다.
2. 브라우저에서 **[http://localhost:8080/login](http://localhost:8080/login)**을 엽니다.
3. 이메일 **`user@scm.com`**, 비밀번호 **`password1!`**로 로그인합니다. 관리자 기능은 **`admin@scm.com`**과 같은 비밀번호로 확인합니다.
4. 사용하는 동안 터미널을 켜 두고, 종료하려면 해당 터미널에서 **`Ctrl+C`**를 누릅니다.

기본 H2 학습 모드는 별도 데이터베이스 설치 없이 데모 계정을 생성합니다. 데이터는 프로그램을 다시 시작하면 초기화됩니다.

**② SW 기술 4지선다 문제집 열기**

SCM 프로그램이 실행 중이면 **새 터미널 탭이나 창**에서 아래 명령을 입력합니다. 파일 탐색기에서 `practice/sw-quiz/index.html`을 더블클릭해도 됩니다.

```bash
cd scm-system
open practice/sw-quiz/index.html
```

1. 브라우저의 **나의 문제집 → SW 기술 · 3,000제**를 선택합니다.
2. 학습할 과목을 고르고 **학습 시작하기 / 이어서 학습하기**를 누릅니다.
3. **보기 1~4번 중 하나 → 정답 확인** 순서로 선택해 채점 결과와 해설을 확인합니다.
4. 학습을 마치면 탭을 닫습니다. 다음에는 **같은 브라우저로 같은 파일**을 열어 이어서 풉니다.

문제집은 **20개 과목 × 150문항**이며, Java·DB·SCM 서버 실행 없이 웹 브라우저만으로 사용할 수 있습니다. 문제집 파일을 연 터미널은 닫아도 됩니다. 학습 진도와 선택 중인 보기는 브라우저에 자동 저장되며, 다른 브라우저나 기기로 옮길 때는 **기록 내보내기 / 가져오기**를 사용합니다.

**함께 사용하는 경우:** SCM은 `8080` 포트의 업무 화면으로, 문제집은 별도로 연 HTML 파일로 이용합니다. 문제집을 파일 대신 로컬 웹 주소로 열려면 아래 [B의 HTTP 실행 안내](#b-sw-기술-4지선다-문제집-실행)를 따라 `8000` 포트를 사용합니다.

### A. SCM 프로그램 실행

**1. JDK를 확인합니다.**

```bash
java -version
javac -version
```

두 명령이 모두 실행되고 Java 버전이 17 이상이어야 합니다. JDK 설치가 필요하면 [시작 가이드](docs/GETTING_STARTED.md)를 참고하세요. Gradle은 저장소의 Wrapper를 사용하므로 별도로 설치하지 않아도 됩니다.

**2. 운영체제에 맞는 명령으로 프로그램을 시작합니다.**

macOS / Linux:

```bash
./gradlew bootRun
```

Windows PowerShell:

```powershell
.\gradlew.bat bootRun
```

Windows 명령 프롬프트:

```bat
gradlew.bat bootRun
```

처음 실행하면 Gradle과 라이브러리를 다운로드하므로 인터넷 연결이 필요합니다. 터미널에 `Started ScmApplication`이 출력될 때까지 기다리고, 실행 중에는 터미널을 켜 둡니다.

**3. 브라우저에서 [로그인 화면](http://localhost:8080/login)을 엽니다.**

기본 H2 학습 모드는 별도 DB 설치 없이 실행되며 아래 데모 계정을 생성합니다.

| 역할 | 이메일 | 비밀번호 |
|---|---|---|
| 일반 사용자 | `user@scm.com` | `password1!` |
| 관리자 | `admin@scm.com` | `password1!` |
| 매니저 | `manager@scm.com` | `password1!` |

로그인 후 거래처·품목·발주·수주·재고·공지 화면을 이용할 수 있습니다. 관리자·매니저는 수주관리와 발주·수주 통계도 확인할 수 있습니다. 기본 H2 데이터는 프로그램 재시작 시 초기화됩니다.

**4. 종료할 때는 실행한 터미널에서 `Ctrl+C`를 누릅니다.**

8080 포트를 이미 사용 중이면 아래처럼 실행하고 http://localhost:18080/login에 접속합니다.

```bash
# macOS / Linux
./gradlew bootRun --args='--server.port=18080'
```

```powershell
# Windows PowerShell
.\gradlew.bat bootRun --args="--server.port=18080"
```

JAR 파일이나 Docker로 실행하는 방법은 [4. 실행 방법](#4-실행-방법)에 있습니다.

### B. SW 기술 4지선다 문제집 실행

**1. 파일 탐색기에서 [practice/sw-quiz/index.html](practice/sw-quiz/index.html)을 더블클릭합니다.**

Chrome·Edge·Firefox·Safari 같은 브라우저로 열면 됩니다. Java·DB·SCM 서버 실행이 필요 없으며, 저장소를 받은 뒤에는 인터넷 없이 풀 수 있습니다.

터미널에서 열려면 운영체제에 맞는 명령을 사용합니다.

macOS:

```bash
open practice/sw-quiz/index.html
```

Windows PowerShell:

```powershell
Start-Process .\practice\sw-quiz\index.html
```

Windows 명령 프롬프트:

```bat
start "" practice\sw-quiz\index.html
```

Linux 데스크톱:

```bash
xdg-open practice/sw-quiz/index.html
```

**2. ‘나의 문제집’에서 `SW 기술 · 3,000제`를 선택합니다.**

메뉴는 PC에서는 왼쪽, 모바일에서는 화면 위쪽에 있습니다. 이전에 SCM 워크북을 풀었다면 **나의 문제집**에서 SW 기술 문제집으로 바꿉니다.

**3. 과목을 고르고 보기 하나를 클릭해 풉니다.**

- **과목별 학습**: Java·Spring·JPA·SQL 등 20개 과목의 진도를 확인하고 **학습 시작하기 / 이어서 학습하기**를 누릅니다.
- **문제 풀기**: **학습 과목**과 **풀이 범위**를 고르고 보기 1~4번 중 하나를 클릭합니다. **정답 확인**을 누르면 정오답과 해설이 나옵니다.
- **다음 문제 / 문항 바로가기**: 순서대로 풀거나 원하는 문항으로 이동합니다.
- **오답 노트**: 정답과 해설을 가린 채 틀린 문제를 다시 풉니다. 다시 맞힌 문항은 복습 대상에서 빠집니다.
- **북마크 / 무작위 순서**: 다시 볼 문제를 모으거나 풀이 순서를 섞습니다.

**4. 진도는 자동 저장됩니다. 학습을 마치면 탭을 닫습니다.**

다음에 **같은 브라우저로 같은 파일**을 열고 **이어서 학습하기**를 누릅니다. 제출한 답안·진도·북마크뿐 아니라 선택 중인 보기·마지막 문항·과목·검색 조건·무작위 순서도 복원됩니다.

다른 기기나 브라우저로 옮기려면 하단의 **기록 내보내기**로 JSON 파일을 보관한 뒤, 옮길 곳에서 **같은 종류·버전의 문제집**을 열고 **가져오기**를 누릅니다. 가져오기는 확인 후 현재 문제집의 기록을 교체합니다.

`practice/sw-quiz/` 폴더 전체를 유지하고 브라우저의 JavaScript를 활성화하세요. 문제 데이터를 생성하거나 추가 패키지를 설치할 필요는 없습니다.

**SCM 모듈 403제를 풀려면** **나의 문제집 → SCM 워크북 · 403제**를 고릅니다. [루트 학습 목차](index.html)에서 **웹 문제집 시작하기** 또는 모듈별 **4지 선다**를 눌러도 워크북이 열립니다.

**파일을 직접 열기 어려우면 로컬 웹 주소로 실행합니다(선택).** Python 3가 설치된 환경에서 저장소 루트에서 아래 명령을 실행합니다.

```bash
python3 -m http.server 8000 --bind 127.0.0.1
```

브라우저에서 [SW 기술 문제집](http://127.0.0.1:8000/practice/sw-quiz/index.html)을 엽니다. 이 방법은 학습하는 동안 터미널을 켜 두고, 종료할 때 `Ctrl+C`를 누릅니다. Windows 명령과 포트 변경은 [11.2](#112-로컬-http-서버로-열기선택)를 참고하세요. 문제집 서버는 8000 포트, SCM 프로그램은 기본 8080 포트를 사용합니다.

파일 직접 열기와 HTTP 주소는 기록 저장 공간이 다릅니다. 실행 방법을 바꿀 때는 **기록 내보내기 / 가져오기**로 진도를 옮기세요.

### 정상 실행 확인

| 실행 대상 | 정상 실행 시 확인할 내용 |
|---|---|
| SCM 프로그램 | 터미널에 `Started ScmApplication`이 표시되고, 로그인 화면에서 데모 계정으로 로그인할 수 있습니다. |
| SW 기술 문제집 | **나의 문제집**에 **SW 기술 · 3,000제**가 선택되고, **풀이 완료**에 `0 / 3,000` 또는 기존 풀이 수가 표시됩니다. 선택지 4개 중 하나를 고르고 **정답 확인**을 누르면 정오답과 해설이 나옵니다. |

### 실행이 안 될 때

| 증상 | 확인·해결 방법 |
|---|---|
| Java를 찾을 수 없거나 버전 오류 | `java -version`으로 JDK 17 이상인지 확인합니다. 설치 안내는 [시작 가이드](docs/GETTING_STARTED.md)를 참고하세요. |
| macOS/Linux에서 `./gradlew` 실행 권한 오류 | 저장소 루트에서 `chmod +x gradlew` 실행 후 다시 시작합니다. |
| 8080 포트 사용 중 | `./gradlew bootRun --args='--server.port=18080'`으로 실행하고 http://localhost:18080/login에 접속합니다. Windows는 `gradlew.bat bootRun --args="--server.port=18080"`입니다. |
| 브라우저에서 프로그램에 연결할 수 없음 | 실행 터미널을 유지하고 `Started ScmApplication` 출력과 접속 포트를 확인합니다. |
| 문제집 화면은 열리지만 문항이 안 보임 | `practice/sw-quiz/` 폴더 전체를 유지하고 브라우저 JavaScript를 활성화합니다. 아래 [HTTP 실행 방법](#112-로컬-http-서버로-열기선택)도 사용할 수 있습니다. |
| 문제집에 ‘저장 불가’가 표시됨 | 현재 화면에서 계속 풀 수 있습니다. 하단 **기록 내보내기**로 보관하고 브라우저 저장 설정을 확인하거나 [로컬 HTTP 서버](#112-로컬-http-서버로-열기선택)로 엽니다. |
| 다른 브라우저나 주소에서 이전 진도가 안 보임 | 기존 브라우저·주소에서 **기록 내보내기** 후 새 브라우저·주소의 같은 문제집에서 **가져오기**를 누릅니다. |

---

## 학습 자료 구성과 코드 읽기

이 저장소의 1차 목적은 Java/Spring 기반 SI·백엔드 취업을 준비하면서 직접 구현하고 설명하는 **SCM 실전 워크북**입니다. 거래처/카테고리/품목 마스터와 발주(구매)의 작성 → 결재 요청 → 승인/반려 → 입고 → 재고 부족 조회 흐름을 학습 문제와 실행 가능한 참조 구현으로 함께 제공합니다.

> SI 실무에서 자주 등장하는 SCM/구매 관리 흐름을 학습·구현한 신입 포트폴리오 프로젝트입니다.

> **이 저장소는 세 갈래로 구성됩니다.**
> - **`index.html` — 학습 시작점.** HTML 목차에서 트랙과 모듈을 선택하고 브라우저에 진도를 저장합니다.
> - **`practice/` — 주 학습 공간.** 요구사항·설정·업무 규칙을 선택지 4개와 정답·해설로 학습합니다 → [`practice/README.md`](practice/README.md).
> - **`src/` · `docs/` — 실행 가능한 참조 구현과 증거.** 풀이 후 비교하고 테스트·설계 결정을 확인하는 포트폴리오 결과물입니다.
>
> 학습 순서는 **모듈 선택 → 4지 선다 풀이 → 채점·해설 → 오답 복습 → 참조 구현 확인**입니다. 참조 코드를 먼저 베끼는 방식은 권장하지 않습니다.

코드를 처음 읽는다면 각 Java 파일 맨 위의 **쉬운 역할 설명**부터 읽으세요. 요청은 **Controller(접수 창구) → Service(업무 담당자) → Repository(자료 저장·조회 창구) → 데이터베이스(전자 장부)** 순서로 처리됩니다. 먼저 [프로그램 시작점](src/main/java/com/example/scm/ScmApplication.java)을 보고, [로그인 화면 담당자](src/main/java/com/example/scm/controller/web/LoginController.java)와 [로그인 검사](src/main/java/com/example/scm/service/AuthService.java)를 이어 읽으세요. 그다음 [수주 처리](src/main/java/com/example/scm/service/SalesOrderService.java)와 [재고 장부](src/main/java/com/example/scm/domain/Stock.java)의 단계별 주석을 읽으면 주문과 창고 수량이 어떻게 연결되는지 알 수 있습니다.

화면 파일의 `<!-- ... -->`와 JavaScript의 `// ...`도 설명 주석입니다. [주문 입력 화면 코드](src/main/resources/static/js/purchase-order-form.js)는 물건 줄 추가·삭제를, [문제집 코드](practice/sw-quiz/quiz.js)는 선택·채점·오답 복습·브라우저 기록 저장을 설명합니다. 주석은 읽는 사람을 위한 안내이며 화면의 기능을 실행하는 명령은 아닙니다.

어려운 용어는 관련 코드 옆에서 풀어 설명했습니다. **트랜잭션**은 여러 변경을 함께 저장하거나 함께 되돌리는 묶음, **잠금**은 같은 장부를 고치는 작업의 차례를 정하는 장치, **DTO**는 입력·결과를 담는 전달 양식입니다. 함수 위의 역할 설명을 읽고, 함수 안의 검사 → 계산 → 저장 → 결과 주석을 따라가세요. 재고 계산과 문제집 선택지 번호에는 작은 숫자 예시도 넣었습니다.

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
11. [SW 기술 4지선다 3000제 실행](#11-sw-기술-4지선다-3000제-실행)
12. [SCM 모듈 4지 선다 워크북 실행](#12-scm-모듈-4지-선다-워크북-실행)

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

처음이라면 [초보자를 위한 기술 안내](docs/TECHNOLOGY_GUIDE.md)에서 **각 기술을 왜 사용하고 어떻게 연결하는지** 먼저 읽어 보세요. 품목 조회·수정과 발주 입고를 실제 코드까지 따라갑니다.

| 구분 | 기술 |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.1.1 / Spring Framework 7.0.x |
| JSON | Jackson 3 (`tools.jackson`) |
| ORM | Spring Data JPA (Hibernate) |
| View | Thymeleaf (순수 프래그먼트, layout-dialect 미사용) |
| DB | H2 (기본, in-memory) / MySQL 8.4 LTS + Flyway (프로필) |
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
| 수주(SalesOrder) | 작성(헤더+라인·서버 금액 계산), 확정, 내 목록, 처리 대기, 상세, 출고(-재고), 완료, 사유 입력 취소(출고 후 재고 복원) |
| 공지사항(Notice) | 관리자 등록/수정/삭제, 목록(제목 검색·페이징·중요 공지 우선), 상세·조회수 |
| 수주 관리 | ADMIN/MANAGER 전체 수주 조회, 상태·고객사·수주일 복합 검색, 출고 후 완료 처리 대기 조회 |
| 대시보드 | 거래처/품목/카테고리 수, 발주 대기·내 발주·부족 재고 집계, ADMIN/MANAGER 기간별 발주·수주 상태/금액 통계 |

> 발주 작성 시 `totalAmount`/`lineAmount`는 **클라이언트 입력을 신뢰하지 않고 서버에서 재계산**한다(`lineAmount = quantity × unitPrice`, `totalAmount = Σ lineAmount`).

## 4. 실행 방법

> JDK가 준비되어 있으면 위의 [A. SCM 프로그램 실행](#a-scm-프로그램-실행) 절차를 사용하세요. Docker Desktop을 사용하는 경우 [4.4 Docker](#44-docker로-실행-jdk-설치-불필요)로 컨테이너 안에서 빌드·실행할 수도 있습니다.

### 4.1 요구사항 (로컬 실행 시)

> **처음 설치부터 차근차근** 하고 싶다면 [`docs/GETTING_STARTED.md`](docs/GETTING_STARTED.md)를 먼저 여세요.

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

기본 테스트에는 H2의 MySQL 호환 모드로 Flyway V1/V2 자동 적용 → JPA 스키마 검증 순서를
확인하는 테스트도 포함됩니다. 실제 MySQL 고유 동작은 `mysqlSchemaTest`로 별도 확인합니다.

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
- DB 연결 암호화는 `DB_SSL_MODE`(기본 `PREFERRED`: 서버가 지원하면 TLS 사용)로 정합니다. 다른 서버의 실제 DB에 연결할 때는 `DB_SSL_MODE=VERIFY_IDENTITY`와 `DB_ALLOW_PUBLIC_KEY_RETRIEVAL=false`를 지정해 인증서 확인까지 강제하세요.
- MySQL 스키마는 [`db/migration/mysql`](src/main/resources/db/migration/mysql) 아래의 Flyway 버전 스크립트만 변경하며, Hibernate는 `ddl-auto=validate`로 매핑 일치 여부만 확인합니다.
- 이전 `ddl-auto=update` 버전에서 만든 Docker 볼륨에는 Flyway 이력이 없습니다. 데모 데이터라면 `docker compose down -v`로 한 번 초기화한 뒤 다시 기동하세요. 보존할 데이터가 있다면 임의로 baseline 처리하지 말고 백업·스키마 대조 후 마이그레이션해야 합니다.
- 기본 H2와 데모용 Docker Compose는 동일한 시드 계정·데이터를 생성합니다(MySQL 볼륨이 유지되면 최초 1회).
- 일반 MySQL 프로필은 알려진 기본 계정 생성을 막기 위해 시드가 기본 비활성입니다. 데모용 `docker-compose.yml`만 `SCM_SEED_ENABLED=true`를 명시합니다. 실제 운영에서는 이 값을 켜지 마세요.

### 4.5 JAR 파일로 실행

Gradle로 실행 파일을 한 번 만든 뒤 Java로 실행할 수도 있습니다. 저장소 루트에서 다음 명령을 순서대로 실행합니다.

macOS / Linux:

```bash
./gradlew bootJar
java -jar build/libs/scm-system-0.0.1-SNAPSHOT.jar
```

Windows PowerShell:

```powershell
.\gradlew.bat bootJar
java -jar .\build\libs\scm-system-0.0.1-SNAPSHOT.jar
```

`Started ScmApplication`이 출력되면 http://localhost:8080/login에 접속하고 [시드 계정](#5-시드-계정)으로 로그인합니다. 기본 H2 모드로 실행되며, 중지는 `Ctrl+C`입니다. 8080 포트를 사용 중이면 Java 명령 끝에 `--server.port=18080`을 붙이고 http://localhost:18080/login에 접속합니다.

JAR 파일은 `build/libs/`에 생성되는 빌드 결과입니다. 프로젝트 버전을 바꾸면 파일명도 바뀌므로 해당 폴더의 실제 이름을 사용하세요. 이 JAR는 SCM 업무 프로그램을 실행합니다. SW 3000제는 아래 [문제집 실행 절차](#11-sw-기술-4지선다-3000제-실행)에 따라 브라우저에서 엽니다.

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
| `GET /sales-orders/new`, `/sales-orders/my` | 수주 작성·내 수주 목록 | 로그인 |
| `GET /sales-orders/pending` | 처리 대기 수주 | ADMIN/MANAGER |
| `GET /admin/sales-orders` | 전체 수주 검색·완료 처리 대기 조회 | ADMIN/MANAGER |
| `GET /sales-orders/{id}` | 수주 상세·확정·취소·출고·완료 | 작성자 또는 ADMIN/MANAGER |
| `GET /notices`, `/notices/{id}` | 공지 목록·상세 | 로그인 |
| `GET /notices/new`, `/notices/{id}/edit` | 공지 등록·수정 | ADMIN |
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
├── domain          # User, Partner, Category, Item, PurchaseOrder, PurchaseOrderLine, SalesOrder, SalesOrderLine, Notice, Stock
│   └── enums       # UserRole, PartnerType, PartnerStatus, ItemStatus, PurchaseOrderStatus, SalesOrderStatus
├── dto             # auth / user / partner / category / item / purchaseorder / salesorder / notice / stock 하위 패키지
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
| 수주 작성·확정 | ⭕(확정은 작성자) | ⭕(확정은 작성자) | ⭕(확정은 작성자) |
| 수주 출고·완료 / 처리 대기 조회 | ❌ | ⭕ | ⭕ |
| 전체 수주 검색 / 발주·수주 금액 통계 | ❌ | ⭕ | ⭕ |
| 수주 취소·상세 | ⭕(작성자) | ⭕ | ⭕ |
| 공지 조회 | ⭕ | ⭕ | ⭕ |
| 공지 등록·수정·삭제 | ❌ | ⭕ | ❌ |
| 재고 현황 / 안전재고 부족 조회 | ⭕ | ⭕ | ⭕ |

> 발주 승인/반려/입고는 PRD 본문(3.11.3, ADMIN 한정)과 달리 **ADMIN + MANAGER**로 확정되었습니다. 사유는 [9. 제약](#9-현재-구현-범위--제약) 및 [`docs/TROUBLESHOOTING.md`](docs/TROUBLESHOOTING.md)의 PRD-구현 차이표를 참고하세요.

## 9. 현재 구현 범위 / 제약

### 구현된 모듈

- 기반/공통 인프라(인증·예외·페이징·감사), 마스터(거래처·카테고리·품목), 발주(PurchaseOrder), 입고와 재고 조회, 수주(SalesOrder), 공지사항(Notice).

### 현재 제약

- 수주는 `DRAFT → CONFIRMED → SHIPPED → COMPLETED`로 처리합니다. `COMPLETED`와 `CANCELED`은 종료 상태이며, 출고 후 취소는 출고 수량을 재고에 복원합니다.
- 발주서 라인 수정 API는 제공하지 않습니다(수정이 필요하면 취소 후 재작성).
- 관리자/매니저 대시보드 통계는 주문일 기준으로 조회하며, 기간 생략 시 이번 달 1일~오늘입니다. 상태는 조회 시점의 현재 상태이고 취소·반려 금액도 각각 표시합니다(확정 매출 통계가 아님).

### 빌드 환경

- `./gradlew portfolioCheck --no-daemon`으로 전체 테스트, 저장소 경계, 실행 가능한 JAR를 검증합니다. 실제 MySQL 검증은 별도 DB를 준비한 뒤 `mysqlSchemaTest`로 실행합니다.
- 로컬에 JDK 17이 없으면 [Docker](#44-docker로-실행-jdk-설치-불필요)의 멀티스테이지 빌드를 사용할 수 있습니다.

## 10. 문서

| 문서 | 내용 |
|---|---|
| [`index.html`](index.html) | 40개 모듈 HTML 목차, 검색·필터, 브라우저 진도 관리 |
| [`docs/GETTING_STARTED.md`](docs/GETTING_STARTED.md) | 처음 시작하는 사람용 설치 → 실행 → 화면 투어 가이드 |
| [`docs/TECHNOLOGY_GUIDE.md`](docs/TECHNOLOGY_GUIDE.md) | 기술별 사용 이유·동작 원리, 실제 요청 흐름과 코드, 확인 실습 |
| [`docs/INDEX.md`](docs/INDEX.md) | 처음 읽는 순서와 문서 동기화 규칙 |
| [`docs/GLOSSARY.md`](docs/GLOSSARY.md) | SCM 도메인·Spring 용어 사전(구현 기준) |
| [`docs/LOOP_ENGINEERING.md`](docs/LOOP_ENGINEERING.md) | 학습·기능·문서·포트폴리오 반복 루프와 완료 조건 |
| [`docs/PORTFOLIO_GUIDE.md`](docs/PORTFOLIO_GUIDE.md) | 3분 데모, 이력서 문장, 면접 질문과 제출 체크리스트 |
| [`docs/API_SPEC.md`](docs/API_SPEC.md) | REST API 명세(인증/사용자/마스터/발주/재고), 공통 에러 형식, ErrorCode 표 |
| [`docs/ERD.md`](docs/ERD.md) | 엔티티/테이블 관계, 컬럼 요약, enum 정의 |
| [`docs/STATE_MACHINE.md`](docs/STATE_MACHINE.md) | 발주 상태 전이표(T1~T8), 다이어그램, 금지 전이 |
| [`docs/TROUBLESHOOTING.md`](docs/TROUBLESHOOTING.md) | 빌드 환경, OSIV/낙관적 락/채번 이슈, PRD-구현 차이표, QA MINOR 4건 |
| [`history.html`](history.html) | 프로젝트 선택과 구현 과정을 시간순으로 기록한 히스토리 |

> 요구사항·설계와 면접 4지 선다는 [`docs/scm_system_PRD_TRD.md`](docs/scm_system_PRD_TRD.md)에서 확인합니다. 현재 API·상태 계약은 해당 구현 문서를 기준으로 합니다.


## 11. SW 기술 4지선다 3000제 실행

SCM 모듈 문제집과 함께 **20개 분야 × 150문항 = 3,000문항**의 객관식 문제집을 제공합니다. 모든 문항은 선택지 4개·정답 1개·해설·프로젝트 참조 자료를 포함합니다. 추가 계정이나 패키지 설치는 필요 없습니다. 기존 2,000문항에 분야별 50문항을 추가했습니다. 코드 해석·계산 연습·상황 판단·설정·설계·보안 규칙으로 복습합니다. 기존 1~2000번의 문제·보기 순서와 브라우저 저장 키를 유지해 기존 풀이 기록을 이어서 사용합니다.

**SCM 프로그램을 실행하지 않아도 문제집을 풀 수 있습니다.** Java·Spring·JPA·데이터베이스·보안·테스트 등 이 프로젝트에서 사용하는 SW 기술을 다룹니다. 저장소의 `practice/sw-quiz/index.html`이 문제집 시작 화면이며, 루트 `index.html`은 모듈별 4지 선다 학습 목차입니다.

### 11.1 브라우저로 파일 직접 열기

파일 탐색기에서 [`practice/sw-quiz/index.html`](practice/sw-quiz/index.html)을 더블클릭하거나, 저장소 루트에서 운영체제에 맞는 명령을 실행합니다.

```bash
# macOS
open practice/sw-quiz/index.html

# Linux 데스크톱
xdg-open practice/sw-quiz/index.html
```

Windows 명령 프롬프트:

```bat
start "" practice\sw-quiz\index.html
```

Windows PowerShell:

```powershell
Start-Process .\practice\sw-quiz\index.html
```

`index.html`, `questions.js`, `workbook-questions.js`, `study-state.js`, `quiz.js`, `quiz.css`가 같은 폴더에 있어야 합니다. `practice/sw-quiz/` 폴더 전체를 유지하세요. 외부 CDN과 서버 통신을 사용하지 않아 저장소를 받은 뒤에는 인터넷 연결 없이 풀 수 있습니다. 브라우저의 JavaScript는 활성화되어 있어야 합니다.

### 11.2 로컬 HTTP 서버로 열기(선택)

브라우저가 로컬 파일 저장을 제한하거나 일정한 접속 주소로 사용하려면 Python 3로 정적 서버를 실행합니다. SCM 서버와 독립적으로 실행됩니다.

```bash
# 저장소 루트에서 실행
python3 -m http.server 8000 --bind 127.0.0.1
```

브라우저에서 아래 주소를 엽니다.

| 문제집 | 접속 주소 |
|---|---|
| SW 기술 3,000제 | http://127.0.0.1:8000/practice/sw-quiz/index.html |
| SCM 워크북 403제 | http://127.0.0.1:8000/practice/sw-quiz/index.html?bank=workbook |
| 전체 학습 목차 | http://127.0.0.1:8000/index.html |

Windows에서 `python3` 명령이 없으면 다음을 사용합니다.

```powershell
py -3 -m http.server 8000 --bind 127.0.0.1
```

학습 중에는 서버를 실행한 터미널을 켜 두고, 종료할 때 `Ctrl+C`를 누릅니다. 포트 8000을 사용 중이면 명령의 8000을 8001 등으로 바꾸고 접속 주소의 포트도 맞춥니다. 같은 진도를 자동으로 불러오려면 같은 브라우저에서 주소의 호스트와 포트도 유지하세요.

### 11.3 문제 풀이와 복습

1. **나의 문제집**에서 **SW 기술 · 3,000제**를 선택합니다.
2. **과목별 학습**에서 과목별 완료율·정답·오답·미풀이를 확인하고 과목을 고릅니다. **문제 풀기**의 **학습 과목 / 풀이 범위**에서도 조건을 고를 수 있습니다.
3. 선택지 하나를 클릭하거나 숫자 `1`~`4`를 누른 뒤 **정답 확인**을 누릅니다.
4. 정답·내 선택·해설을 읽고 **워크북 참조 자료 보기**에서 관련 코드나 문서를 확인합니다.
5. **다음 문제** 또는 **문항 바로가기**로 이동합니다. **다시 풀기**로 재도전하면 최근 제출 결과가 기록됩니다. 마지막 문항에서는 **학습 마치기**를 누릅니다.
6. **오답 노트**에서 정답을 가린 채 다시 풉니다. 다시 맞힌 문항은 복습 대상에서 제외됩니다. 채점 직후에는 해설을 읽을 수 있도록 현재 문항이 유지됩니다.
7. **북마크**, **무작위 순서**, **문제 검색**을 활용합니다. 검색은 질문·보기·문항 번호를 대상으로 합니다.

답안·진도·북마크·마지막 문항·선택 중인 보기·학습 조건은 **같은 브라우저와 같은 접속 주소**에 자동 저장됩니다. 다음 학습에서는 **이어서 학습하기**를 누릅니다. 파일 직접 열기와 HTTP 접속은 서로 다른 저장 공간을 사용하며, 다른 기기·브라우저로 자동 공유되지는 않습니다.

기록을 옮기거나 보관하려면 하단 **기록 내보내기**로 JSON 파일을 저장합니다. 대상 브라우저에서 같은 종류·버전의 문제집을 열고 **가져오기**를 누르면 확인 후 기록을 교체합니다. 시크릿 모드나 브라우저 데이터 삭제 전에도 파일로 보관할 수 있습니다. 하단 **초기화**는 확인 후 현재 문제집의 기록만 지웁니다.

### 11.4 출제 분야와 원본 관리

| 분야 | 문항 수 | 분야 | 문항 수 |
|---|---:|---|---:|
| Java 기본 문법 | 150 | Repository·검색·페이징 | 150 |
| 객체 설계와 Lombok | 150 | 트랜잭션과 영속성 | 150 |
| 컬렉션·Stream·Optional | 150 | 동시성과 잠금 | 150 |
| 금액·날짜·자료형 | 150 | 인증·인가·세션·CSRF | 150 |
| Spring DI와 계층 | 150 | Thymeleaf 화면과 폼 | 150 |
| Boot 설정과 실행 | 150 | JavaScript와 브라우저 | 150 |
| Spring MVC 요청 처리 | 150 | SQL·H2·MySQL·Flyway | 150 |
| HTTP·REST·JSON | 150 | Gradle·Docker·운영 | 150 |
| DTO·입력 검증·예외 | 150 | JUnit·Mockito·통합 테스트 | 150 |
| JPA 엔티티와 매핑 | 150 | SCM 상태·권한·재고 | 150 |

문항 원본은 [`questions.tsv`](practice/sw-quiz/questions.tsv), 브라우저용 데이터는 [`questions.js`](practice/sw-quiz/questions.js)입니다. 원본을 수정한 경우에만 Python 3로 아래 명령을 실행합니다. **일반 문제 풀이에는 이 생성 과정이 필요 없습니다.**

```bash
python3 practice/sw-quiz/build_bank.py
./gradlew test --tests com.example.scm.practice.SwQuizBankTest
```

생성기는 3,000문항, 중복 질문, 선택지 중복, 분야별 150문항을 검사합니다. 정답 위치는 1~4번에 각각 750개씩 분포합니다. 자료는 프로젝트 학습용이며 특정 국가시험의 기출문제를 복제한 것이 아닙니다.

## 12. SCM 모듈 4지 선다 워크북 실행

모듈별 학습 항목 171개와 코드 TODO·표준 학습 루프·기능 개념·테스트 기대 결과·보안·화면·설계·트러블슈팅·면접 질문을 **4지 선다 워크북 403문항**으로 전환했습니다. 여러 자료에 반복되는 주제는 같은 문항으로 연결합니다. 코드 주석에 남아 있던 질문 79곳과 설계·테스트 표도 선택형으로 연결했습니다. 기존 363문항의 번호·선택지·풀이 기록을 유지한 채 40문항을 추가했습니다. 모든 문항에 선택지 4개·정답 1개·해설이 있습니다. 설계 선택 문제는 질문에 명시한 전제와 현재 구현을 기준으로 풉니다.

1. 저장소 루트에서 `open practice/sw-quiz/index.html`을 실행하거나 해당 HTML 파일을 더블클릭합니다.
2. **나의 문제집**에서 **SCM 워크북 · 403제**를 선택합니다.
3. **과목별 학습**에서 모듈을 골라 답을 제출합니다. 정답·해설, 오답 노트, 북마크, 무작위 순서, 진도 자동 저장과 기록 내보내기·가져오기를 사용할 수 있습니다.

Windows·Linux 파일 열기는 [11.1](#111-브라우저로-파일-직접-열기)의 명령을 그대로 사용합니다. [로컬 HTTP 서버](#112-로컬-http-서버로-열기선택)를 실행했다면 http://127.0.0.1:8000/practice/sw-quiz/index.html?bank=workbook 으로 바로 접속할 수 있습니다. Java·SCM 서버·DB는 필요 없습니다.

기존 SW 기술 3000제와 전환 워크북의 풀이 기록은 별도로 저장됩니다. 문서의 **브라우저에서 풀기** 링크는 해당 문제로 바로 이동합니다. 문제집을 선택해 전환하면 마지막 문제와 제출 기록을 복원합니다.

- [모듈별 4지 선다 문제](practice/problems.md): 40개 모듈의 403문항
- [전체 전환 문제집](practice/workbook-questions.md): 403문항과 펼쳐 보는 정답·해설
- [정답표](practice/answers.md): 브라우저와 동일한 정답 번호·해설
- [PRD/TRD 면접 4지 선다](docs/INTERVIEW_QUIZ.md): 원본 3.19의 12개 면접 주제
- [기능 워크북](practice/feature-implementation-workbook.md): 개념·테스트 질문의 객관식 연결

40개 모듈의 학습 파일 45개를 모두 선택형 문제집으로 생성합니다. 기능 워크북의 구현 답안·빈칸 카드·기록 표도 선택형 문항으로 전환했습니다. 학습 기록은 선택 번호·채점 결과·북마크로 확인합니다. PRD/TRD 면접 문항은 [면접 문제 모음](docs/INTERVIEW_QUIZ.md)에서 풉니다. 초기 요구사항 문서도 [docs/scm_system_PRD_TRD.md](docs/scm_system_PRD_TRD.md)로 옮겨 면접 질문 12개를 4지 선다로 통일했고, [기술 안내의 이해 확인](docs/TECHNOLOGY_GUIDE.md#9-기술-이해-4지-선다-확인)도 선택형 문항으로 제공합니다.

문항 수정은 `practice/sw-quiz/`의 `module_choices.tsv`, `extra_choices.tsv`, `test_choices.tsv`, `remaining_choices.tsv`에서 합니다. `conversion_map.json`은 원문과 문항의 연결 및 고정 문항 순서를 관리합니다. 새 문항 키는 `questionOrder` 마지막에 추가합니다. 브라우저·Markdown·정답표를 함께 갱신하고 검증하려면 다음을 실행합니다.

```bash
python3 practice/sw-quiz/build_workbook_bank.py
./gradlew test --tests com.example.scm.practice.SwQuizBankTest
```
