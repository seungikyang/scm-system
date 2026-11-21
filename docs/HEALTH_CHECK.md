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


## 2026-10-03 수주·공지 구현 검증

- 수주 작성·확정·목록·상세·출고·완료·사유 입력 취소와 공지 CRUD·검색·페이징·중요 공지 정렬·조회수를 API/웹 화면으로 구현했습니다.
- `SalesAndNoticeIntegrationTest` 17개에서 권한·CSRF·입력 검증, 금액 계산, 재고 부족 시 전체 롤백, 중복 품목, 동시 채번, 동시 출고·취소, 동시 공지 조회와 수정 중 조회수 보존을 검증했습니다.
- `./gradlew portfolioCheck --no-daemon`: 기본 테스트 132개 성공, 실패/오류/건너뜀 0, bootJar 생성, 저장소 경계 검사 성공.
- 별도 테스트용 MySQL 8.4.11 컨테이너에서 `mysqlSchemaTest`: 1개 성공. Flyway V1/V2 자동 적용 후 Hibernate 스키마 검증 성공. 기존 데이터베이스는 사용하지 않았습니다.
- `git diff --check` 및 `./scripts/verify-repository-boundary.sh` 성공. 개인 풀이 공간과 starter 빈칸은 유지했습니다.


## 2026-10-10 학습 자료 전체를 4지 선다로 통일

- 코드 답안을 직접 작성하던 40개 모듈의 학습 파일 45개를 모두 선택지 4개·정답·해설이 있는 문제집으로 재작성했습니다. 생성기는 TSV와 원문 연결표에서 전체 학습 파일을 재생성합니다.
- 기능 워크북의 구현 답안 블록 45곳, 빈칸 카드 1곳, 기록 표 2곳을 선택형 확인 문항으로 전환했습니다. 학습 기록 양식도 문항 선택·채점·복습 방식으로 바꿨습니다.
- HTML 목차의 40개 모듈 링크가 `?bank=workbook&module=NN`으로 해당 모듈을 엽니다. 브라우저 문제집은 모듈 URL 선택을 지원하고 문제집 전환 시 모듈 조건을 초기화합니다.
- `practiceStatus`는 모듈별 문항 수를, `practiceCheck`는 선택지·정답·분야 연결 검사를 제공합니다. 두 명령의 실제 실행을 확인했습니다.
- `docs/scm_system_PRD_TRD.md`에 요구사항·설계와 면접 12문항의 선택형 학습본을 제공하고 학습 목차·문서 지도·README를 연결했습니다. SCM 모듈 403문항과 SW 기술 2,000문항의 기존 풀이 데이터는 유지했습니다.
- `./gradlew test bootJar --no-daemon`: 전체 기본 테스트 145개 성공, 실패·오류·건너뜀 0, 실행 가능한 JAR 생성. 최종 문서 갱신 후 워크북 관련 테스트 17개도 성공했습니다.
- Chrome에서 40개 모듈의 필터·4개 선택지·목차 링크, 채점·기록 복원, 문제집 전환, 모듈 번호 처리와 390px 화면을 확인했습니다.
- 생성기 재실행 전후 56개 출력 파일이 동일했습니다. 선택형 문항 밖의 서술·빈칸 답안 0개, JavaScript 구문·Git 공백·저장소 경계 검사 성공.

## 2026-10-10 남은 서술형 학습 질문의 4지 선다 전환

- starter의 TODO·구현 중 질문 79곳을 4개 선택지와 정답·해설 링크로 전환했습니다. 요구사항 계약 표·공통 테스트 표·표준 루프 질문도 선택형 문항으로 연결했습니다.
- 설정값·오류 처리·학습 루프 문항 40개를 추가해 전환 워크북을 403문항으로 갱신했습니다. 기존 363문항의 번호·보기·정답·해설과 저장 키는 해시 비교로 호환성을 확인했습니다.
- `remaining_choices.tsv`와 `conversion_map.json`의 `inlineQuestions`에서 추가 문항·출처를 관리합니다. 생성기는 Markdown 코드 예시를 포함한 주석을 동기화하며, 재생성 전후 54개 출력 파일의 해시가 동일했습니다.
- `./gradlew test --tests com.example.scm.practice.SwQuizBankTest --tests com.example.scm.practice.WorkbookStructureTest --no-daemon`: 16개 성공, 실패·오류·건너뜀 0. 선택지·정답·문서 일치와 남은 서술형 질문 감사를 포함합니다.
- Chrome 파일 직접 열기에서 403문항 표시, 신규 문항 채점·해설·검색·분야 필터·북마크 저장, 기존 기록 복원, 390px 화면과 SW 기술 2,000제 전환을 확인했습니다.
- JavaScript 구문 검사, `git diff --check`, 저장소 경계 검사 성공.

## 2026-10-10 SW 기술 4지선다 2,000제 확장 검증

- 기본 SW 기술 문제집에 1,500문항을 추가해 20개 분야 × 100문항 = 2,000문항으로 확장했습니다. 각 문항은 선택지 4개·정답 1개·해설을 포함합니다.
- 추가 문항은 분야별 15개 주제를 개념 확인·상황 판단·원리 설명·적용 사례·오개념 점검으로 복습하도록 구성했습니다.
- 기존 1~500번의 질문·보기·정답·해설과 저장 키를 유지했습니다. 확장 전 전체 데이터의 SHA-256 비교로 기존 풀이 기록 호환성을 검증합니다.
- 생성기와 테스트가 원본 TSV 일치, 고유 질문 2,000개, 분야별 100개, 정답 위치별 500개를 검증합니다. 화면·목차·실행 안내를 2,000제 기준으로 갱신했습니다.
- `./gradlew test --tests com.example.scm.practice.SwQuizBankTest --tests com.example.scm.practice.WorkbookStructureTest --no-daemon`: 14개 성공, 실패·오류·건너뜀 0.
- Chrome에서 파일 직접 열기(`file://`), 기존 기록 복원, 2,000번 채점·북마크 저장·새로고침 복원, 검색, 분야 필터, 오답 복습, 무작위 순서, 390px 화면과 별도 363문항 워크북 전환을 확인했습니다.

## 2026-10-03 SW 기술 4지선다 500제와 실행 안내

- 루트 README에 SCM 프로그램, Docker, 4지선다 문제집 직접 열기, 선택적인 Python 정적 서버, Windows/macOS/Linux 명령을 정리했습니다.
- 20개 분야에 각각 25문항을 작성했습니다. 각 문항은 질문·선택지 4개·정답 1개·해설을 포함하며 분야별 프로젝트 참조 링크를 제공합니다.
- 원본 questions.tsv와 생성 questions.js를 분리했습니다. 생성기와 SwQuizBankTest가 500문항, 고유 질문/ID, 중복 없는 선택지, 분야 분포, 정답 분포(각 위치 125개), 해설 및 원본 일치를 검증합니다.
- `SwQuizBankTest` 2개와 `WorkbookStructureTest` 9개 통과. JavaScript 구문 검사와 저장소 경계 검사 통과.
- Playwright 브라우저에서 HTTP로 채점·재도전·오답 필터·북마크·검색·무작위 순서·새로고침 후 기록 복원·390px 모바일 화면을 확인했습니다. 외부 CDN이나 fetch 의존성이 없는 정적 script 구성이며, 파일 직접 열기 절차도 README에 안내했습니다.
- `./gradlew bootRun --args='--server.port=18080' --no-daemon`으로 프로그램 기동 후 `/login` HTTP 200 확인. 검증용 프로세스는 종료했습니다.

## 2026-10-03 수주 관리·주문 통계 확장 검증

- ADMIN/MANAGER의 전체 수주 조회 화면/API를 추가했습니다. 상태·고객사·수주일 복합 필터, 페이지 이동 시 조건 유지, 출고된 수주의 완료 처리 진입을 제공합니다.
- 관리자/매니저 대시보드에 주문일 기준 기간별 발주·수주 상태별 건수와 금액 통계 및 JSON API를 추가했습니다. 기간 기본값은 이번 달 1일~오늘이며, 주문이 없는 상태는 0으로 반환합니다.
- 수주 목록의 고객사/작성자 표시값을 페이지 단위로 일괄 조회해 행별 추가 조회를 줄였습니다.
- `OrderManagementIntegrationTest` 5개 성공: 타 작성자의 출고 수주 완료 처리, 복합 검색/경계일/페이징, 여러 라인 주문의 헤더 단위 금액 집계, 빈 통계/기간 기본값, 인증·권한·잘못된 기간과 형식 오류.
- `./gradlew portfolioCheck --no-daemon`: 기본 테스트 139개 성공(실패/오류/건너뜀 0), bootJar 생성, 저장소 경계 검사 성공.
- `git diff --check` 및 `./scripts/verify-repository-boundary.sh` 성공. 이번 추가 기능 검증은 H2 기반이며 실제 MySQL 테스트는 재실행하지 않았습니다. 스키마 변경은 없습니다.

## 2026-10-24 과목별 이어풀기 완성

- 중단 전 추가된 JavaScript 회귀 테스트에서 과목별 마지막 문항 복원 7건이 실패했습니다. `study-state.js`에 과목별 위치 검증·복원을 구현하고, `quiz.js`의 과목 카드·선택 메뉴·모듈 링크와 연결했습니다.
- 과목별 위치가 없는 기존 기록은 `currentId`로 마지막 과목의 위치를 복원합니다. 기존 저장 키·답안·북마크를 유지하고, 소속 과목과 문항 번호가 맞지 않는 위치 기록은 제외합니다.
- `node --test src/test/js/study-state.test.cjs`: 15건 통과. GitHub Actions의 기존 `verify` 작업에도 같은 검사를 추가했습니다.
- Playwright의 실제 Chromium에서 SW 3000제·SCM 워크북 403제의 과목 카드·선택 메뉴 전환, 미제출 보기 보존, 새로고침, 모듈 링크의 위치 복원, 명시한 문항 링크 우선순위와 문제집별 기록 분리를 확인했습니다. HTTP 주소에서 검증했습니다.
- `./gradlew portfolioCheck --no-daemon`: Java 테스트 250건, JAR 생성, 저장소 경계 검사 통과. `git diff --check`와 `./scripts/verify-repository-boundary.sh`도 통과했습니다.
- MySQL·Docker 실행, 파일을 직접 연 브라우저와 원격 GitHub Actions 실행은 이번 검증에 포함하지 않았습니다.
