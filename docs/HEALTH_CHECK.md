# 프로그램 동작 점검 기록

점검일: 2026-09-06. 저장소 루트의 기존 작업본을 기준으로 검증하고 발견한 오류를 수정했다.

## 수정한 내용

| 영역 | 확인한 문제 | 반영한 수정 |
|---|---|---|
| HTTP 입력 검증 | 깨진 JSON, 잘못된 경로 ID·발주 상태가 500으로 응답 | API는 `400 INVALID_INPUT`, 화면은 HTTP 400 오류 화면으로 처리 |
| 발주 행 검증 | `lines: [null]` 요청이 검증을 통과해 서비스에서 예외 발생 | 목록 원소에 `@NotNull`, 서비스 직접 호출에도 null 행 검증 추가 |
| 금액 입력·표시 | 서버는 소수 2자리를 지원하지만 브라우저는 정수 입력만 허용하고 금액을 정수로 반올림 | 품목·발주 단가 입력은 `step=0.01`, 품목·카테고리·발주 금액 표시는 소수 2자리로 통일 |
| H2 콘솔 | 활성화해도 콘솔 자동 설정 모듈이 없어 404 발생 | `spring-boot-h2console` 추가, 별도 서블릿에도 적용되는 Security 필터에서 ADMIN 세션 검사 |
| Flyway 시작 처리 | Flyway 라이브러리만 포함되어 자동 마이그레이션이 실행되지 않고 JPA 검증이 테이블 누락으로 실패 | `spring-boot-starter-flyway`로 자동 설정 포함, V1 적용 후 JPA 검증되는지 테스트 추가 |
| Docker 빌드 | `.git` 없는 이미지에서 Git 경계 검사 실행, 워크북 테스트용 Wrapper 설정·루트 문서 누락 | 이미지에서 `test bootJar` 실행, Wrapper 버전 설정과 목차 링크 대상 문서 복사, 개인 풀이 공간은 빌드 컨텍스트에서 제외 |

수정 이유는 예외 처리기, 발주 DTO·서비스, 보안 설정, 금액 폼, Gradle·Docker 설정 및 새 테스트의 주석에 남겼다. HTTP 계약은 [API_SPEC.md](./API_SPEC.md)에 반영했다.

## 자동 검증

기존 테스트 96건에 회귀 테스트 19건을 추가해 **115건 성공, 실패·오류·건너뜀 0건**을 확인했다. 실제 MySQL 전용 태그 테스트는 이 수에 포함되지 않는다.

| 검증 | 결과 |
|---|---|
| `./gradlew portfolioCheck --no-daemon` | 전체 115건 통과, 실행 JAR 생성 및 저장소 경계 검사 성공 |
| 입력 검증 테스트 | 경로·검색 조건·JSON 오류, null 발주 행 및 저장 방지 검증 |
| 금액 화면 테스트 | `0.25`, `1,234.56` 단가와 라인 합계가 각 화면에서 소수 2자리로 표시됨 |
| H2 콘솔 실제 HTTP 테스트 | 변경한 콘솔 경로에서 비로그인·USER·MANAGER는 403, ADMIN은 콘솔 HTML 200 |
| Flyway 시작 테스트 | H2 MySQL 호환 모드에서 정식 MySQL V1 스크립트 자동 적용 → JPA `validate` 성공 |
| 최종 실행 JAR | Flyway 활성화·JPA `validate` 상태로 직접 기동, 시드 로그인·대시보드·재고 요약·H2 콘솔 200 확인 |
| 기존 발주·재고 테스트 | 상태 전이, 입고와 재고의 원자성, 동시 처리 검증 통과 |
| `docker compose config --quiet` | Compose 설정 문법·구성 검사 통과 |
| `./gradlew mysqlSchemaTest --no-daemon` | 별도 MySQL 8.4 컨테이너에서 V1 마이그레이션 및 JPA 매핑 계약 테스트 1건 통과 |
| `docker build` | 빌드 이미지 내부의 전체 115개 테스트 및 실행 JAR 생성 통과, 런타임 이미지 생성 성공 |
| Docker 런타임 + MySQL | 컨테이너 로그인·대시보드 200, 앞서 저장한 입고 완료 발주와 총 재고 6개 조회 성공 |

실행 JAR를 로컬 H2로 기동하고 로그인, 대시보드, 거래처·카테고리·품목 목록/상세/등록 폼, 재고, 발주 작성/목록, 내 정보, H2 콘솔의 HTTP 응답도 확인했다. 재현했던 잘못된 입력은 모두 400으로 응답했다.

## 브라우저 업무 흐름 검증

Playwright로 실제 Chromium에서 다음 흐름을 수행했다.

1. ADMIN 계정으로 웹 폼 로그인.
2. 발주 행 추가 → 중간 행 삭제 후 남은 행의 `lines[1].*` 인덱스와 입력 유효성 확인.
3. `ITM-001` 3개 × `12.34`, `ITM-002` 2개 × `0.25`로 발주 작성.
4. 상세 화면에서 라인 금액 `37.02`, `0.50` 및 총금액 `37.52` 확인.
5. 결재 요청 → 승인 → 입고를 실행하고 최종 `RECEIVED` 상태 확인.
6. 재고 화면과 API에서 품목별 현재고 3개·2개, 총 현재고 5개 확인.
7. 같은 발주에 입고 API를 다시 호출하면 `400 INVALID_STATUS`로 거부되고 총 재고가 5개로 유지됨을 확인.
8. 웹 로그아웃 후 로그인 화면으로 이동 확인.

실제 MySQL 프로필의 JAR에서도 USER 발주 작성 → 결재 요청 → ADMIN 승인·입고를 API로
검증했다. 단가 `1.25` × 4개와 표준단가 `90.00` × 2개의 총액은 `185.00`, 입고 후
재고는 4개·2개였다. USER의 승인 요청은 403, 순서가 잘못된 입고와 중복 입고는 400으로
거부되고 총 재고는 6개로 유지됐다.

## 검증 환경과 한계

- Java 17.0.20.1, Gradle Wrapper 9.7.1, Spring Boot 4.1.1.
- Docker Desktop의 초기 기동을 기다린 뒤 엔진 연결을 확인했다. CLI 기본 context는 원래의 `default`로 복원하고, 검증 명령은 `--context desktop-linux`를 명시해 실행했다.
- MySQL 검증은 전용 컨테이너와 DB에서 수행했다. 검증용 애플리케이션 프로세스·컨테이너·네트워크는 종료 후 정리했다.
- H2의 MySQL 호환 모드 검증은 실제 MySQL의 잠금·문자 정렬·SQL 동작까지 보장하지 않는다. 이번 점검에서는 별도 MySQL 8.4 컨테이너의 빈 DB에 실제 스키마 테스트도 수행했다. 이후에는 기존 CI의 `mysql-schema` 작업 또는 아래 명령으로 반복할 수 있다.

```bash
docker compose up -d --wait db
./gradlew mysqlSchemaTest --no-daemon
docker build -t scm-system .
```

## 참고한 공식 문서

- [Spring Boot H2 Web Console](https://docs.spring.io/spring-boot/reference/data/sql.html#data.sql.h2-web-console): 콘솔 모듈과 경로 설정, CSRF·프레임 설정.
- [Spring Boot Database Initialization](https://docs.spring.io/spring-boot/how-to/data-initialization.html): Flyway starter와 자동 마이그레이션, JPA 초기화 순서.
