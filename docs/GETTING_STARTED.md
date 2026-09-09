# 처음 시작하기 (Getting Started)

> [HTML 학습 목차](../index.html) · [문서 지도](./INDEX.md) · [워크북 사용법](../practice/README.md)

Java/Spring이 처음인 사람이 **설치 → 실행 → 화면 확인**까지 막히지 않게 돕는 가이드입니다. 이미 개발 환경이 있다면 3단계부터 읽어도 됩니다.

---

## 목차

1. [준비물 설치](#1-준비물-설치)
2. [프로젝트 열기](#2-프로젝트-열기)
3. [애플리케이션 첫 실행](#3-애플리케이션-첫-실행)
4. [10분 화면 투어: 발주 전체 흐름 따라하기](#4-10분-화면-투어-발주-전체-흐름-따라하기)
5. [테스트 실행해 보기](#5-테스트-실행해-보기)
6. [JDK 설치 없이 Docker로 실행](#6-jdk-설치-없이-docker로-실행)
7. [첫 실행에서 자주 겪는 문제](#7-첫-실행에서-자주-겪는-문제)
8. [다음 단계](#8-다음-단계)

---

## 1. 준비물 설치

필요한 것은 두 가지뿐입니다.

| 준비물 | 버전 | 확인 명령 |
|---|---|---|
| JDK | 17 이상 | `java -version` |
| Git | 최신 | `git --version` |

Gradle은 **설치하지 않아도 됩니다.** 저장소의 Gradle Wrapper(`gradlew`)가 알아서 지정된 버전을 내려받습니다. Wrapper는 "이 프로젝트가 정한 빌드 도구 버전을 모두가 똑같이 쓰게 해 주는 장치"라고만 우선 이해하면 충분합니다.

### JDK 설치

**macOS (Homebrew):**

```bash
brew install --cask temurin@17
java -version   # openjdk version "17.x" 확인 (터미널 새로 열고 실행)
```

**Windows:** [adoptium.net](https://adoptium.net)에서 Temurin 17(LTS)을 내려받아 설치하거나:

```powershell
winget install EclipseAdoptium.Temurin.17.JDK
java -version
```

설치 후에도 `java -version`이 안 나오면 터미널을 새로 열고, 그래도 안 되면 아래 [7번 문제](#7-첫-실행에서-자주-겪는-문제)를 확인하세요.

## 2. 프로젝트 열기

```bash
git clone <저장소 주소>
cd scm-system
```

**IntelliJ IDEA Community(무료)** 를 추천합니다.

1. `File → Open`으로 `scm-system` 폴더를 열습니다(build.gradle 선택이 아니라 폴더 자체를 엽니다).
2. 우측 하단에서 Gradle 동기화가 끝날 때까지 기다립니다(처음엔 수 분 걸립니다).
3. 좌측 프로젝트 트리에서 `src/main/java/com/example/scm/ScmApplication.java`를 찾으세요. 이것이 시작점입니다.

> IDE 없이 터미널만으로도 아래의 모든 명령을 실행할 수 있습니다.

## 3. 애플리케이션 첫 실행

```bash
./gradlew bootRun        # macOS / Linux
gradlew.bat bootRun      # Windows
```

처음 실행은 의존성 내려받기 때문에 몇 분 걸립니다. 로그에 `Started ScmApplication`이 보이면 성공입니다.

브라우저에서 <http://localhost:8080> 에 접속하면 로그인 화면으로 이동합니다. 기동 시 시드 데이터가 자동 생성되므로 바로 아래 계정으로 들어갈 수 있습니다.

| 이메일 | 비밀번호 | 권한 |
|---|---|---|
| `admin@scm.com` | `password1!` | ADMIN |
| `manager@scm.com` | `password1!` | MANAGER |
| `user@scm.com` | `password1!` | USER |

먼저 `user@scm.com`(일반 사용자)으로 로그인하세요. 권한에 따라 보이는 메뉴가 다른 것이 이 프로젝트의 핵심 개념 중 하나입니다.

종료는 터미널에서 `Ctrl + C`입니다. H2 in-memory DB를 쓰므로 재시작하면 데이터가 시드 상태로 초기화됩니다(학습용이라 반복 실험에 좋습니다).

## 4. 10분 화면 투어: 발주 전체 흐름 따라하기

코드를 읽기 전에 먼저 손으로 전체 흐름을 경험하세요. 이 한 바퀴가 10~11, 29번 모듈과 관리자 승인 흐름의 큰 그림이 됩니다.

1. **대시보드(`/`)** — 거래처·품목·카테고리 수와 대기 발주 수가 보입니다.
2. **발주작성(`/purchase-orders/new`)** — 공급사와 품목을 골라 수량을 넣고 작성합니다. 단가를 비워두면 품목 표준단가가 적용됩니다.
3. **내 발주(`/purchase-orders/my`)** — 방금 만든 발주가 `DRAFT` 상태로 보입니다.
4. 발주번호를 클릭해 상세로 이동 → **결재요청** 버튼 → 상태가 `REQUESTED`로 바뀝니다.
5. 로그아웃 후 `admin@scm.com`(ADMIN)으로 다시 로그인 → **관리자발주(`/admin/purchase-orders`)** 에서 대기 발주를 **승인**합니다. 상태가 `APPROVED`가 됩니다.
6. 같은 화면에서 **입고** 처리 → 상태가 `RECEIVED`가 되고 재고가 증가합니다.
7. **재고(`/stocks`)** — 입고한 품목의 현재고가 늘어난 것을 확인합니다. "안전재고 이하만 보기" 체크로 부족 품목 조회도 눌러보세요.
8. (선택) `user@scm.com`으로 다시 로그인해 보세요. 관리자발주 메뉴가 보이지 않는 것 = USER는 승인 권한이 없다는 뜻입니다.

이 흐름에서 상태는 `DRAFT → REQUESTED → APPROVED → RECEIVED` 순서로만 움직였습니다. 상태 전이 규칙 전체는 [STATE_MACHINE.md](./STATE_MACHINE.md)에 정리되어 있습니다.

## 5. 테스트 실행해 보기

```bash
./gradlew test
```

`BUILD SUCCESSFUL`이 나오면 환경이 정상입니다. 이 저장소의 학습 완료 기준이 "정상 사례 1개 + 실패 사례 3개를 표현할 수 있다"이므로, 테스트 코드(`src/test/java`)도 함께 읽게 됩니다.

## 6. JDK 설치 없이 Docker로 실행

JDK 설치가 어렵거나 회사 PC 제약이 있으면 Docker Desktop만으로 실행할 수 있습니다.

```bash
docker build -t scm-system .
docker run --rm -p 8080:8080 scm-system
```

MySQL 영속 DB까지 쓰려면 `docker compose up --build`를 실행하세요. 자세한 옵션은 [README 4.4절](../README.md#44-docker로-실행-jdk-설치-불필요)을 참고합니다.

## 7. 첫 실행에서 자주 겪는 문제

| 증상 | 원인과 해결 |
|---|---|
| `command not found: java` | JDK 미설치 또는 PATH 문제. 1단계를 다시 확인하고 터미널을 새로 엽니다. |
| `Permission denied: ./gradlew` (macOS/Linux) | `chmod +x gradlew` 로 실행 권한을 부여합니다. |
| `Port 8080 was already in use` | 다른 프로그램이 8080을 씁니다. 해당 프로세스를 종료하거나 `SERVER_PORT=8081 ./gradlew bootRun` 으로 포트를 바꿉니다. |
| `Unsupported class file major version` | JDK 버전이 17 미만입니다. `java -version`으로 확인하고 17 이상을 설치합니다. |
| Gradle 동기화가 계속 실패 | 네트워크/프록시 문제일 수 있습니다. `~/.gradle` 폴더를 지우고 다시 동기화해 봅니다. |
| 로그인이 안 된다 | 시드는 기동 시 1회 생성됩니다. MySQL 프로필이 아니라면 재기동하면 초기화되어 다시 생성됩니다. |

그 외 심화 이슈(동시성, OSIV 등)는 [TROUBLESHOOTING.md](./TROUBLESHOOTING.md)에 기록되어 있습니다.

## 8. 다음 단계

환경이 돌아가면 이제 진짜 학습 시작입니다.

코드를 읽기 전에 [초보자를 위한 기술 안내](./TECHNOLOGY_GUIDE.md)의 1~4절을 읽어 보세요. Java·Spring·DB가 각각 필요한 이유와 품목 목록 요청이 화면으로 돌아오는 과정을 설명합니다. 저장·로그인을 공부할 때는 5~7절, 직접 확인할 때는 8절로 이어집니다.

1. [루트 index.html](../index.html)을 열어 진도 관리를 켠다.
2. `./gradlew practiceInit` 으로 개인 풀이 공간을 만든다.
3. 처음이면 핵심 경로 `00 → 01 → 02 → 03 → 04 → 07`부터 진행한다.
   Spring/JPA 경험이 충분하면 기반 경로를 자가 점검한 뒤 **10 발주 작성**으로 이동한다.
4. 용어가 낯설면 [GLOSSARY.md](./GLOSSARY.md)를 옆에 두고 읽는다.

학습 순서와 완료 기준은 [practice/README.md](../practice/README.md)에 있습니다.
