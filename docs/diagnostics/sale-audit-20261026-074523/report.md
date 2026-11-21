# SCM 판매·출시 준비도 점검

검사일: 2026-10-26, Asia/Seoul. **종합 판정: NEEDS_EVIDENCE — 판매·출시 판단에 필요한 증거가 부족하다.** 제품 소스와 설정은 수정하지 않았다.

| 영역 | 판정 | 판단 근거 |
|---|---|---|
| 법적 판매 조건 | **REVIEW** | 판매 조건 미정, 코드·문항 권리 귀속 미확인, MySQL 드라이버 재배포 조건과 개인정보 처리 문서 검토 필요 |
| 품질·보안 | **REVIEW** | 기존 테스트 265개 통과. 의존성 취약점 10건의 적용성, 데모 배포 설정, DB 통신과 운영 복구 검증이 남아 있음 |
| 시장성 | **UNKNOWN** | 유료 고객·인터뷰·가격·비용 증거 없음. 학습 상품과 업무용 SCM 중 판매할 상품도 미정 |

취약한 버전의 포함은 확인했지만 이 앱에서의 악용을 재현하지 않았다. 특정 기능·배포 환경을 전제로 한 위험을 확정된 침해나 판매 금지로 확대하지 않아 REVIEW로 판정했다. 현재 결과로 출시 준비가 끝났다고 볼 수 없다. 이 보고서는 법률 인증이나 매출 보장이 아니다.

## 1. 검사 대상과 재현 기준

- PROJECT와 Git 루트: `/Users/seoowoo/Documents/scm-system`. 현재 폴더에서 범위를 확대하지 않았다.
- HEAD: `de6caec941bd9fd8efcc0bb2a56cb4be7cb80967`, 브랜치: `agent/scm-workbook-loop-root-guard`.
- 다수의 수정·삭제·미추적 파일이 있는 **현재 작업 트리**를 점검했다. HEAD만의 점검 결과가 아니다. 시작 상태는 [git-status-before.txt](git-status-before.txt), 파일 해시는 [scope.json](scope.json), 종료 대조는 [source-integrity.json](source-integrity.json)에 있다.
- 포함: `src/`의 Java 웹 앱·API, `practice/sw-quiz/` 학습 기록 코드와 문항 구조, 관련 문서·테스트·Gradle·CI·컨테이너 설정. 개인 풀이 공간 `practice/workspace/`는 제외했다.
- README의 명시된 목적은 Java/Spring SI 취업 준비용 **SCM 실전 워크북과 실행 가능한 참조 구현**이다. 거래처·품목·발주·수주·재고·공지 기능과 3,000제/403제 문제집을 제공한다. 문항 구조 검증과 전체 문항의 사실·교육적 품질 심사는 다르다.
- 명시된 학습 대상은 신입·주니어 개발자다. 앱 안의 업무 역할은 USER/ADMIN/MANAGER다. **판매 국가, 상용 제공 방식(SaaS/다운로드/온프레미스), 상용 고객 유형(B2B/B2C), 판매 가격은 UNKNOWN**이다. 한국어 문서나 Docker 설정으로 이를 추정하지 않았다.
- OUTPUT은 저장소 `AGENTS.md`의 정식 문서 경로 규칙에 맞춰 `docs/diagnostics/sale-audit-20261026-074523/`로 정했다. 제품 루트나 임시 구현 경로에 보고서 원본을 두지 않았다.

기존 로컬 실행 JAR `build/libs/scm-system-0.0.1-SNAPSHOT.jar`의 SHA-256은 `b916037424f4dbb7e1543e16f54aa8adcda44b36d216c29283ab367fe9454d47`이다. 파일 시각은 2026-10-24T01:21:53Z이다. 이번 테스트가 다시 컴파일한 앱 클래스 160개와 현재 리소스를 JAR와 비교했으며 차이는 없었다. 기존 JAR에는 라이브러리 85개가 있다. 현재 Gradle 의존성 조회에서도 Tomcat/Jackson의 해당 버전을 확인했다. 다만 이 JAR가 실제 판매·운영 배포물인지, 배포 이미지·환경변수·프록시까지 같은지는 UNKNOWN이다. [배포물 근거](artifact.json), [현재 의존성](runtime-dependencies.log).

## 2. 먼저 해결할 사항

| 순서 | 필요한 결정·조치 | 근거 | 책임자 |
|---|---|---|---|
| 1 | Tomcat/Jackson 보안 수정이 포함된 호환 의존성 조합으로 검증하거나, CVE별 사용 경로와 완화 근거를 남긴 뒤 담당자가 적용성을 판정 | 아래 취약점 표, `build.gradle:5`, JAR·runtimeClasspath | 미정 |
| 2 | 업무용으로 제공한다면 데모 시드·DB 암호·공개 포트·DB TLS·실행 사용자 설정을 운영 배포 조건에 맞게 정리하고 검증 | `docker-compose.yml:16`, `:20`, `:43`, `application-mysql.yml:8`, `Dockerfile:30` | 미정 |
| 3 | 판매할 상품과 국가·배포 방식·고객 유형을 확정하고 코드·문항 권리 및 MySQL 재배포 조건을 검토 | README, PRD, JAR `mysql-connector-j-9.7.0.jar!/LICENSE` | 미정 |
| 4 | 개인정보 처리 흐름과 약관·환불·지원 범위를 확정하고, 격리 MySQL·복구·배포 검증 증거 확보 | User/Partner 엔티티, 운영 문서, 미실행 검사 | 미정 |
| 5 | 정의한 고객에게 유료 파일럿으로 지불 의사를 확인하고 실제 원가를 측정 | 현재 고객·가격·원가 자료 없음 | 미정 |

코드 수정은 수행하지 않았다. 의존성 조정 시에는 빌드 설정과 기존 Java/JSON 통합 테스트, 운영 설정 조정 시에는 배포 파일·문서·보안 테스트를 함께 확인해야 한다.

## 3. 품질·보안 근거

### 실행한 검사

| 검사 | 실제 결과 | 한계 |
|---|---|---|
| `./gradlew --offline test --no-daemon --rerun-tasks` | 종료 0. 34개 테스트 클래스, 250건, 실패·오류·건너뜀 0 | 설정에서 `mysql` 태그 제외. H2와 실제 MySQL의 동작을 동일시하지 않음 |
| `node --test src/test/js/study-state.test.cjs` | 종료 0. 15건 통과, 실패·건너뜀 0 | 실제 브라우저 이벤트·파일 가져오기 전체 흐름은 미실행 |
| `./scripts/verify-repository-boundary.sh` | 종료 0 | 검사 당시 추적·스테이징 경계 검사 |
| JaCoCo | **UNKNOWN** | 기존 플러그인·태스크 없음. 새 의존성 설치나 설정 변경 안 함 |
| Trivy 소스 검사 | SBOM, `license --license-full`, `vuln,secret,misconfig` 각각 종료 0 | 소스 SBOM 의존성 0개. 이것을 취약점 없음으로 해석하지 않음 |
| Trivy 기존 JAR 검사 | `rootfs build/libs`로 SBOM·취약점·라이선스 각각 실행. 85개 구성 요소, CVE 10건 | JAR 라이선스 전용 결과에는 패키지 판정이 없어 별도 원문 추출 검사로 보완 |
| JAR 라이선스 원문 검사 | 중첩 JAR의 LICENSE/NOTICE/COPYING 138개를 진단용 추출, `license --license-full` 78개 탐지 | 탐지 이름은 계약 해석이나 의무 이행 증거가 아님 |

도구: OpenJDK `17.0.21.0.2`, Gradle Wrapper `9.7.1`, Trivy `0.76.1`. Node 등 정확한 버전은 [tools.json](tools.json)에 있다. 테스트 시작·종료 시각과 명령·종료 상태는 [test-execution.json](test-execution.json), 실제 집계는 [tests.json](tests.json), 커버리지 누락 소스 목록은 [coverage.json](coverage.json)에 기록했다. 이번 실행 이후 생성된 XML만 사용했고 [원본 XML](test-results/test/)을 보존했다. 커버리지 분모·비율은 미측정이므로 null이며 임의 합격선을 적용하지 않았다.

테스트는 기본 H2 메모리 DB와 별도 H2 마이그레이션 DB를 사용한다. 테스트 프로세스에서 `SPRING_`, `DB_`, `SCM_`, `H2_CONSOLE_` 환경변수를 제거해 외부 DB 설정이 주입되는 것을 피했다. `mysqlSchemaTest`는 실제 DB 마이그레이션과 데이터 쓰기를 수행하므로 전용 검사 DB가 지정되지 않은 이번 점검에서 실행하지 않았다.

### 의존성 취약점

아래 등급은 **Trivy 등급**이다. 실행 JAR와 현재 의존성 트리에서 버전 포함을 확인했다. 수정 버전은 스캐너에 표시된 해당 이슈의 수정 기준이며, 전체 보안 이슈가 해결되는 최신 버전이라는 뜻은 아니다.

| CVE | 패키지·현재 버전 | 등급 | 스캐너의 같은 계열 수정 버전 |
|---|---|---|---|
| CVE-2026-65182 | tomcat-embed-core 11.0.24 | CRITICAL | 11.0.25 |
| CVE-2026-65905 | tomcat-embed-core 11.0.24 | CRITICAL | 11.0.25 |
| CVE-2026-68525 | tomcat-embed-core 11.0.24 | CRITICAL | 11.0.25 |
| CVE-2026-89407 | jackson-core 3.1.5 | HIGH | 3.1.7 |
| CVE-2026-89425 | jackson-core 3.1.5 | HIGH | 3.1.7 |
| CVE-2026-68497 | jackson-databind 3.1.5 | HIGH | 3.1.6 |
| CVE-2026-91776 | jackson-databind 3.1.5 | HIGH | 3.1.7 |
| CVE-2026-91777 | jackson-databind 3.1.5 | HIGH | 3.1.7 |
| CVE-2026-19032 | jackson-databind 3.1.5 | MEDIUM | 3.1.6 |
| CVE-2026-83557 | jackson-databind 3.1.5 | MEDIUM | 3.1.6 |

적용성에 대한 현재 판단은 다음과 같다.

- Tomcat의 세 항목은 선언적 Servlet 제약, DIGEST, FORM 인증 등의 조건이 있다. [Apache 공지](https://tomcat.apache.org/security-11.html)는 이들을 각각 Important/Low/Low로 분류하며 Trivy의 CRITICAL과 다르다. 앱은 자체 세션·인터셉터를 사용하고 Spring Security의 폼 로그인도 비활성화한다(`SecurityConfig.java:41`). 현재 코드에서 해당 Tomcat 인증 방식을 구성한 근거를 찾지 못했다. 이를 앱 로그인 우회가 확인된 것으로 보고하지 않는다. 같은 공식 공지에는 11.0.27까지 후속 수정이 있어 11.0.25만을 최종 목표로 삼아서는 안 된다.
- [CVE-2026-89425 공급사 공지](https://github.com/FasterXML/jackson-core/security/advisories/GHSA-7hhh-6rmp-j9qf)는 `createParser(DataInput)` 경로에 한정한다. 앱에서 직접 사용하는 경로는 확인하지 못했다. 일반 HTTP JSON 입력이 곧 이 경로라는 주장은 하지 않는다.
- [CVE-2026-89407 공급사 공지](https://github.com/FasterXML/jackson-core/security/advisories/GHSA-p6pp-m3f8-5c89)는 특정 숫자 판별 경로를 설명한다. 나머지 항목도 타입·파서 사용 조건에 따른 검토가 필요하다. 앱별 공격 재현이나 모든 전이 호출 분석은 하지 않았다.

[원본 취약점 근거](trivy-jar-vuln.json), [실행 JAR SBOM](trivy-jar-sbom.cdx.json). 나머지 CVE의 공급사 링크는 원본 References에 보존했으며 모두의 원문을 별도 정독한 것은 아니다. 이 10건은 완전한 취약점 목록이라는 보장이 없다.

Trivy 취약점 DB UpdatedAt은 `2026-10-25T19:10:35Z`, Java DB는 `2026-10-25T00:59:20Z`이다. 다운로드 시각·정책 번들 digest는 각 `trivy-*-metadata.json`에 있다. DB와 검사 규칙 데이터만 OUTPUT 캐시에 받았고 제품 코드나 의존성을 업로드하지 않았다. `--offline-scan`은 의존성 식별 API 요청을 막으며 DB 다운로드까지 막는 옵션은 아니다. [Trivy 공식 지원 범위](https://trivy.dev/docs/latest/coverage/language/)에 따라 filesystem에서 빠지는 JAR를 rootfs로 별도 확인했다.

### 코드·운영 설정 검토

| 발견 | 영향·필요 조치 | 근거 |
|---|---|---|
| 데모 배포는 공개된 관리자 계정을 만든다 | compose는 시드를 켜고 앱·DB 포트를 호스트에 게시한다. 외부에 업무용으로 제공하려면 별도 계정 발급·암호 주입·접근 범위 검증이 필요하다. 로컬 학습 기본값 자체를 침해 사건으로 보지는 않는다 | `docker-compose.yml:16`, `:20`, `:43`, `DataInitializer.java:41` |
| MySQL 프로필이 TLS를 끈다 | `useSSL=false&allowPublicKeyRetrieval=true`. 원격·신뢰할 수 없는 네트워크의 실제 데이터에는 TLS와 인증서 검증을 포함한 연결 조건을 정해야 한다 | `src/main/resources/application-mysql.yml:8` |
| 기본 H2 데이터는 영속 보관되지 않는다 | 재기동 시 업무 데이터가 보존되는 배포 방식과 백업·복원 절차를 따로 확정해야 한다. README는 학습 모드의 초기화를 명시한다 | `application.yml:5`, `:12`, README 실행 안내 |
| Docker 실행 사용자를 지정하지 않는다 | Trivy DS-0002 HIGH. 런타임 이미지의 root 실행 가능성을 줄이는 설정 검토 필요. 실제 운영 런타임 override는 미확인 | `Dockerfile:30`, [설정 검사](trivy-security.json) |
| 이미지 HEALTHCHECK 없음 | Trivy DS-0026 LOW. 실제 호스팅의 상태 확인·장애 감지 설정을 확인해야 한다. 이 항목만으로 장애를 입증하지 않는다 | Dockerfile, `docs/HEALTH_CHECK.md` |
| 로그인 반복 제한·다른 세션 철회 근거 없음 | 앱 내 제한은 확인하지 못했다. 프록시 제한·계정 운영 방식, 비밀번호 변경 후 다른 세션 처리 요구사항 검토 필요 | `AuthService.java:43`, `UserService.java:40`, `UserApiController.java:38` |
| 복구·감사 이력의 운영 증거 없음 | 주문 상태 시각과 JPA 시간 필드는 있지만 DB 백업 복원, 장애 시 재처리·감사 보관 검증을 대신하지 않는다 | PurchaseOrder/SalesOrder/BaseTimeEntity, 운영 문서 |

확인한 보호 장치도 있다. 로그인 직후 세션 ID 교체, BCrypt 해시, 웹 CSRF와 H2 콘솔 ADMIN 제한, 서비스의 역할·작성자 검사, 서버 금액 재계산과 상한 검증, 정렬 허용 목록, 트랜잭션과 잠금을 확인했다. 권한·입력 경계·발주 동시성·재고 복원 등을 다루는 기존 테스트가 통과했다. API의 CSRF 제외만으로 취약하다고 단정하지 않았으며 JSON 입력과 메서드 조건을 함께 읽었다. 정독과 기존 테스트는 침투 테스트나 실제 MySQL 경합·복구 검증을 대체하지 않는다.

학습 기록은 localStorage에 보관하며, 가져오기에는 5MB 제한·문제집 버전 확인·사용자 확인·상태 정규화가 있다(`practice/sw-quiz/quiz.js:360`). 표시에는 textContent를 사용한다. 관련 상태 함수 테스트 15개는 통과했지만 브라우저의 파일 입출력·접근성·다중 기기 저장은 직접 실행하지 않았다.

소스 비밀키 탐지는 **제외한 파일을 뺀 범위에서 0건**이었다. `.env*`, 개인키·인증정보 파일, `.git`, `.gradle`, 개인 풀이, 기존 진단 폴더와 OUTPUT은 검사에서 제외했다. 원본 비밀키 JSON은 저장하지 않았고 발견 시 파일 위치·줄·규칙·등급만 남기도록 결과를 제한했다. 비밀 파일이나 Git 과거 이력이 안전하다는 뜻은 아니다. 프로젝트 전용 Trivy 설정·ignore·secret 규칙 파일은 발견되지 않았다.

### 기존 연결 결과

- SonarQube: 조회는 성공했지만 프로젝트 0개. `.sonarlint/connectedMode.json`, 루트 빌드·Sonar·CI에서 프로젝트 키도 확인되지 않았다. 따라서 이슈·Hotspots·커버리지·Quality Gate와 analysis ID/커밋 대응은 모두 UNKNOWN이다. 키 추측·상태 변경·새 분석 업로드는 하지 않았다.
- JetBrains: `codex mcp list`에는 enabled이지만 현재 세션의 실제 도구 목록에는 호출 가능한 도구가 없다. IDE 검사는 미실행이며 기존 Wrapper로 테스트했다.
- GitHub: 공개 저장소 메타데이터와 HEAD에 대한 PR 이벤트 CI 조회에서 [성공한 실행](https://github.com/seungikyang/scm-system/actions/runs/34370979507)을 확인했다. 도구는 PR 이벤트·첫 페이지만 반환하며 현재 미커밋 작업 트리의 검증 증거가 아니다. 원격 CI를 다시 실행하지 않았다.

상세는 [connections.json](connections.json). MCP 연결 성공과 검사 성공을 구분했다.

## 4. 법적 판매 조건

판매 국가·배포·고객 유형이 미정이므로 관할 법률의 적용·준수 여부는 확정할 수 없다. 아래 한국 법령은 **한국에서 해당 형태로 판매·처리하는 경우의 조건부 검토 기준**이며 판매 국가를 한국으로 가정한 결과가 아니다.

| 자산·데이터 | 확인한 내용 | 남은 증거·조치 |
|---|---|---|
| 자체 Java/JS/CSS·문서·문항 | 저장소에 구현과 문제 데이터가 있으나 자체 제품 LICENSE·양도 계약·문항 출처 및 사용 허락 증거를 확인하지 못함 | 작성자·고용/외주 계약·원자료 권리 확인. LICENSE 부재가 권리자의 판매를 금지한다는 뜻은 아님 |
| Spring/Tomcat/Jackson 등 | SBOM에서 Apache-2.0 등 탐지, JAR에 LICENSE/NOTICE 다수 존재 | 배포 방식에 따른 고지·라이선스 원문·수정 고지의 실제 제공 여부 확인. [Apache 원문](https://www.apache.org/licenses/LICENSE-2.0) |
| MySQL Connector/J 9.7.0 | 실제 중첩 JAR LICENSE는 GPLv2와 추가 허락·Universal FOSS Exception을 명시. 여러 라이선스 탐지는 번들 구성품도 포함 | 닫힌 소스 다운로드/온프레미스 배포라면 예외 적용·GPL 이행·상용 계약 필요 여부를 법률 담당자가 검토. 계약 미제공. [Oracle 안내](https://www.mysql.com/about/legal/licensing/oem/) |
| Hibernate Core 7.4.5.Final | 실제 `META-INF/LICENSE.txt`는 Apache-2.0 | 과거 Hibernate 버전을 근거로 LGPL이라고 판정하지 않았음. 현재 원문에 맞춰 고지 검토 |
| Logback·Jakarta 등 | EPL/LGPL/Classpath exception 등의 표기가 함께 탐지됨 | 선택 가능한 라이선스와 제3자 포함 고지를 원문별로 구분. 이름만으로 판매 금지 또는 모든 조건 동시 적용으로 해석하지 않음 |
| 라이선스 자동 미식별 5개 | h2, antlr4-runtime, aspectjweaver, flyway-mysql, thymeleaf | H2 공식 페이지는 MPL-2.0 또는 EPL-1.0을 안내. 나머지 포함 버전 원문·고지 이행 증거는 추가 확인 필요. [H2 원문](https://www.h2database.com/html/license.html) |
| Javadoc 생성물 | build 아래에서 GPL-2.0-with-classpath-exception, MIT 탐지 | 생성 문서 범위의 탐지다. 제품 전체의 라이선스로 확대하지 않음 |
| 이미지·폰트·유료 SDK·모델·외부 API | `src/main/resources`에 별도 이미지/폰트 파일은 확인되지 않음. 검토한 앱 경로에 결제·모델·유료 외부 API 연동 근거 없음 | 저장소 밖 자산·고객 제공 자료·운영 계약의 부재를 입증한 것은 아님. 전체 문항의 저작권·사실성 전수 검토 미실행 |

85개 구성 요소와 원문 위치는 [artifact.json](artifact.json), [원문 추출 인덱스](license-evidence-index.json), [라이선스 탐지](trivy-extracted-licenses.json)에 있다. 스캐너의 restricted/HIGH 등급은 법률 위반 판정이 아니다. GPL은 유료 판매와 함께 의무를 검토해야 한다. [GNU 공식 FAQ](https://www.gnu.org/licenses/gpl-faq.en.html).

User는 이메일·이름·비밀번호 해시, Partner는 담당자명·전화·이메일·주소를 저장한다. 거래처가 법인이라는 이유로 담당자 정보를 모두 비개인정보로 취급할 수는 없다. 처리 근거·보유 기간·파기 절차·책임자·위탁/국외 이전·권리 요청 절차를 뒷받침하는 현재 제품 문서·계약을 확인하지 못했다.

- 한국법이 적용되는 개인정보 처리라면 [개인정보 보호법 제30조](https://www.law.go.kr/lsLinkCommonInfo.do?lsJoLnkSeq=1006185717)의 처리방침 공개와 [제21조](https://law.go.kr/LSW/lsLinkCommonInfo.do?lsJoLnkSeq=1029334977)의 불필요한 개인정보 파기·법정 보존 예외를 실제 흐름에 대조해야 한다. 확인한 원문 시행일은 2026-09-11이다.
- 해외 호스팅·지원·로그 처리가 있다면 이전 국가·수탁자·처리 근거부터 확인해야 한다. [제28조의8 제2항](https://law.go.kr/lsLinkCommonInfo.do?lsJoLnkSeq=1033215841)은 동의를 근거로 이전할 때의 고지 항목을 정한다. 모든 국외 이전이 동의만을 근거로 해야 한다고 해석하지 않았다. 실제 인프라·위탁 계약은 UNKNOWN이다.
- 한국 소비자 대상 통신판매라면 사업자/거래조건 표시, 신고 해당 여부, 환불·자동갱신·해지 경로를 검토해야 한다. [전자상거래법 제17조](https://www.law.go.kr/LSW/lsSideInfoP.do?lsiSeq=282793&joNo=0017&joBrNo=00&docCls=jo&urlMode=lsScJoRltInfoR), 시행 2026-07-21 원문에서 청약철회와 디지털콘텐츠의 예외·사전 조치 조건을 확인했다. 다운로드 제공만으로 일괄 환불 불가라고 판단하지 않는다. 본 제품의 판매 화면·결제/판매 계약은 제공되지 않았다.

사업자·세금·통신판매 신고의 실제 이행, 수집 근거와 위탁 계약 해석, 규제 업종 적용 여부는 법률·세무 담당자의 확인이 필요하다. 특허·상표 검색과 권리 귀속 실사는 수행하지 않았으며 UNKNOWN이다. 책임자는 모두 미정이다.

## 5. 시장성과 수익성

문서에 근거한 주 고객 후보는 Java/Spring 학습자다. 업무용 SCM은 별도의 판매 가설이다. 개발자 학습 상품의 가치는 업무 흐름·상태 전이·테스트를 설명하는 데 있고, 기업 업무 도구는 지속 운영·복구·지원·업무 적합성을 구매한다. 이를 같은 시장으로 합쳐 수요가 검증됐다고 판단하지 않았다.

공개 공식 페이지에서 다음 3개 대안을 조사했고, Odoo는 추가 시도했지만 본문 접근에 실패했다. 확인 시각·정확한 URL과 접근 실패는 [sources.json](sources.json)에 있다. 조사 시점은 2026-10-26이며 통화 변환은 하지 않았다.

| 대안·비교 가설 | 공식 기능·제공 내용 | 가격·청구 단위·세금 | 약관 확인 |
|---|---|---|---|
| 이카운트 ERP / 업무용 | 구매·판매·재고 외 회계·인사·생산 등 | KRW 40,000/월, 사용자 무제한. 연 선납 KRW 440,000, 최초 가입비 KRW 200,000. VAT 별도 | 공식 가입 약관 제9·10조의 결제·환불·해지 본문 확인 |
| Zoho Inventory Standard / 업무용 | 구매·수주·창고·상품·출고 관리 | 미국 페이지 USD 29/조직/월 **연 결제**, 연 USD 348. 월 500주문·3사용자·2위치, 세금 별도. 월 결제 단가는 미확인 | 글로벌 약관과 환불 본문 확인. 지역별 계약은 별도 확인 필요 |
| 인프런 「실전! 스프링 부트와 JPA 활용1」 / 학습용 | 웹 앱 예제 개발, 36강·7시간44분, 질문 답변, 무제한 수강 | KRW 88,000/강의 일회성 정가. 신규가입 할인 KRW 66,000 표시. 세금 구분 미확인 | 공식 약관 링크 확인, 본문은 시간 초과로 UNKNOWN |
| Odoo / 업무용 추가 비교 | 공식 가격·약관 본문 미확인 | UNKNOWN. 검색 요약의 가격을 확정값으로 사용하지 않음 | 가격·Enterprise 약관 모두 접근 실패 |

근거: [이카운트 가격](https://www.ecount.com/kr/ecount/join/pricing)·[약관](https://www.ecount.com/kr/ecount/join/sign-up), [Zoho 가격](https://www.zoho.com/us/inventory/pricing/)·[약관](https://www.zoho.com/terms.html?zredirect=f)·[환불](https://www.zoho.com/refundpolicy.html), [인프런 강의](https://www.inflearn.com/course/스프링부트-JPA-활용-1).

ERP 월 구독과 개인 강의 일회성 가격은 직접 순위를 매길 단위가 아니다. Zoho의 연 결제 월 환산값을 월 결제 가격으로 표시하지 않았다. 이카운트의 가입비와 Zoho의 좌석·사용량 제한도 함께 봐야 한다. 현재 저장소는 SCM의 제한된 업무 흐름과 학습 설명을 제공하므로 위 제품과 기능이 같다고 주장할 근거는 없다. 학습용 판매를 우선 검증한다는 의견은 문서의 목적에 근거한 추론이며 고객 조사 결과가 아니다.

### 실제 지불 의사와 원가

고객 인터뷰, 유료 파일럿, 구매 전환, 재구매·유지율, 지원 투입 시간, 실제 판매 가격, 인프라·결제·API·환불·고객 획득 비용 자료는 제공되지 않았다. 코드에서 결제 연동을 찾지 못했다는 사실이 판매가 없었다는 증명은 아니다. 현재 자료로 시장성 PASS나 실제 이익률을 부여할 수 없다.

아래는 민감도 설명을 위한 **전부 가정인 SaaS 월 단위 시나리오**다. 실제 요금 제안·견적·업계 평균이 아니다. 세전 순매출 40,000원/고객·월, 인프라 5,000원/고객·월, 결제+환불 충당 5%, 고객 획득비 월 배분 3,000원, 지원 인건비 25,000원/시간, 공통 고정비 100,000원/월로 놓았다. 제품의 실제 배포 방식은 여전히 UNKNOWN이다.

| 고객당 월 지원 시간 가정 | 고객당 공헌이익 | 공통 고정비만의 손익분기 고객 수 |
|---|---|---|
| 0.25시간 | 23,750원 | 5명 |
| 0.6시간 | 15,000원 | 7명 |
| 1.2시간 | 0원 | 이 조건에서는 도달 불가 |

계산: `40,000 - 5,000 - 40,000×5% - 3,000 - 지원시간×25,000`. 개발비·보안/법무비·세금·해지로 인한 매출 감소는 포함하지 않았다. 교육 다운로드 상품이라면 플랫폼 수수료·콘텐츠 제작·버전 지원의 별도 모형이 필요하다. 실제 입력을 확보한 뒤에만 수익성을 판단해야 한다.

## 6. 미실행·변경·추가 승인 범위

| 항목 | 상태·이유 |
|---|---|
| 실제 MySQL 테스트·경합 검증 | 미실행. 전용 검사 DB 미지정. Flyway/테스트가 쓰기를 하므로 임의 DB에 접속하지 않음 |
| JaCoCo 커버리지 | 미실행. 기존 설정 없음. 130개 Java 소스의 측정 누락을 coverage.json에 기록 |
| SonarQube 이슈·Hotspots·Gate | 미실행. 프로젝트 목록 0개·키 없음. 최신 분석과 작업 트리 대응 불가 |
| JetBrains IDE 검사 | 미실행. 설정 목록과 실제 호출 가능한 도구가 다름 |
| 새 bootJar·전체 빌드·Docker 이미지 검사 | 미실행. 기존 JAR만 검사. Docker 시작/중지, 이미지 빌드·pull, 실제 배포·OS 패키지 검증 안 함 |
| 브라우저 E2E·접근성·부하·장애/백업 복구 | 미실행. 코드 정독·기존 테스트 통과로 대체하지 않음 |
| 전체 문항 내용·권리 전수 심사, 특허·상표·계약 실사 | 미실행. 제공 자료와 전문 검토 부족 |
| 실제 고객 조사·결제·광고·게시 | 미실행. 공개 자료만 조사. 고객 연락과 외부 쓰기 없음 |
| Inflearn 약관·Odoo 가격/약관 | 본문 접근 실패, UNKNOWN |

이번에 추가한 것은 OUTPUT 안의 보고서·진단 결과·라이선스 원문 사본뿐이다. 제품 소스·manifest·lockfile·기존 설정은 바꾸지 않았다. 테스트는 통상적인 `build/`, `.gradle/`, 기존 Gradle 사용자 캐시에 생성물을 남겼으며 테스트 의존성은 offline 모드로만 사용했다. Trivy DB·규칙 캐시는 OUTPUT에만 작성했다. 보고서에 필요한 DB 메타데이터를 보존하고 큰 재생성 가능 Trivy 캐시는 검사 종료 후 제거했다. 원본 JAR는 덮어쓰지 않았다.

도구 설치·업데이트·연결 등록·인증 복구·설정 변경·운영 DB 접근·배포·외부 쓰기는 하지 않았다. 다음 조치 중 새로 필요해질 작업은 제품 수정, JaCoCo 설정/의존성 추가, 지정한 격리 DB의 마이그레이션·테스트 쓰기, 전체 빌드/컨테이너 작업, Sonar 분석 업로드다. **이번 결과물 완성에 추가 승인은 필요하지 않았으며 승인 요청을 남기지 않았다.** 이후 수행 시 제품·명령·쓰기 위치·전송 정보·비용을 구체화해야 한다.

구조화된 발견, 상태·심각도·근거·확인 시각·대상 해시·도구·한계·조치·미정 책임자는 [findings.json](findings.json)에 있다. 모든 영역이 근거를 갖춰 PASS일 때만 READY_FOR_HUMAN_REVIEW를 쓸 수 있으며, 이번 결과는 그 조건을 충족하지 않는다.
