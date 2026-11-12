# Ponytail · REA 점검 및 수정 — 2026-10-23

현재 작업 트리의 SCM 소스를 검토하고, 실제 MySQL 테스트와 REA 브라우저 재현으로 확인한 결함을 수정했다. 기존 변경과 개인 풀이 공간은 보존했으며 커밋은 만들지 않았다.

## 확인한 결함

| 문제 | 수정 전 | 수정 후 | 검증 |
| --- | --- | --- | --- |
| MySQL에서 최신 재고를 읽지 못함 | 주문 조회 후 다른 입고가 커밋되어도 일반 재고 조회는 이전 스냅샷을 읽음. 최초 입고는 중복 키 오류, 기존 재고 입고·출고 취소는 낙관적 잠금 충돌, 출고는 잘못된 재고 부족 오류 | 재고 변경 전용 `findByItemIdForUpdate`를 추가해 입고·출고·출고 취소에서 최신 재고를 잠가 읽음. 기존 품목 잠금 순서와 트랜잭션 유지 | 기존 `MySqlStockSnapshotIntegrationTest` 4건: 수정 전 전부 실패 → 수정 후 전부 통과 |
| 하위 경로 배포에서 공지 저장 실패 | `/scm/notices/new`에서 저장하면 `/notices`로 전송되어 404 발생. 수정 폼도 같은 URL 생성 방식 | 공지 등록·수정 action을 기존 Thymeleaf `@{/...}` 방식으로 생성 | REA N1/N2, 루트·`/scm` 통합 테스트 |
| 하위 경로 배포에서 수주 페이지 이동 실패, 표시 건수 누락 | 내 수주·처리 대기 수주에서 다음 페이지가 `/scm`과 `size`를 누락. 하위 경로에서는 404, 루트에서는 기본 표시 건수로 변경 | 두 목록의 페이지 링크에 애플리케이션 경로·표시 건수 유지. 내 수주의 상태 필터도 유지 | REA S1/S2, 루트·`/scm` 통합 테스트 |

재고 변경은 `StockRepository`, `PurchaseOrderService.receive`, `SalesOrderService.ship/cancel`과 관련 단위 테스트에만 적용했다. 일반 재고 조회, HTTP API 계약, DB 스키마는 변경하지 않았다. URL 수정은 `notice/form.html`, `salesorder/list.html` 두 템플릿에 적용했다. 새로운 의존성은 없다.

## REA 실행 증거

REA의 `capture_browser_scenario`가 임시 Chrome 156 브라우저에서 로그인·공지 입력·저장·수정·페이지 이동을 직접 수행했다. 서버는 Java 17의 `bootRun`, H2 메모리 DB, `127.0.0.1:18082/scm`을 사용했다. 페이지 이동 확인용 수주 2건은 이 별도 H2 서버의 API로 생성·확정했다.

| 기호 | 관찰 | Evidence ID |
| --- | --- | --- |
| N1 | 수정 전 공지 저장: `POST /notices` → 404 | `ev_04f65a9d96b154b682592580dd25ac31ebf2989ca8ca453534c4fb716ee7172c` |
| N2 | 수정 후 공지 등록·수정: `/scm/notices...`로 POST → 302 → 상세 200, 수정 제목 표시 | `ev_aaa54d3278387e7f9c0f14d10056d1ba06e98f4adb58df01965a49352e73f86b` |
| S1 | 수정 전 두 수주 목록의 다음 페이지: `/sales-orders/...` → 404, `size` 누락 | `ev_eeb2df6ecf681594c01298e8bc500a7fa94cee7c79d0da9c55fca5040e878e8d` |
| S2 | 수정 후 두 목록: `/scm/sales-orders/...?size=1&page=1` → 200, 현재 페이지 2와 주문 1행 표시 | `ev_301be96dbe56a4367d6c245cc2ab4e868169ca2b1e51b8c01ffc09526a5001f6` |

네 캡처 모두 `completeness.status=complete`, 선언한 단계 성공, `cleanup=terminated-owned-process`를 확인했다. [증거 요약](diagnostics/rea-2026-10-23-evidence-summary.json)에 요청 상태·이동 경로·DOM 관찰값·제한을 보존했다. 로그인 값·쿠키·CSRF 값은 요약에서 제외했다. 원본 캡처와 수정 전 실패 XML은 Git에서 제외되는 `build/diagnostics/`의 재생성 가능한 진단 결과다.

## 검증 결과와 범위

- `./gradlew portfolioCheck`: Java 테스트 **250건 통과**, 실행 가능한 JAR 생성 및 저장소 경계 검사 통과.
- 별도 MySQL 8.4.11 컨테이너에서 `./gradlew mysqlSchemaTest`: **5건 통과**. 마이그레이션/JPA 계약 1건과 재고 스냅샷 회귀 4건.
- `node --test src/test/js/study-state.test.cjs`: **12건 통과**.
- 추가한 웹 회귀 테스트 4건: 공지 등록·수정 폼 및 저장, 두 수주 목록의 페이지 이동을 루트와 `/scm`에서 확인. 수정 전 3건 실패 → 수정 후 전부 통과.
- `./scripts/verify-repository-boundary.sh`, `git diff --check`: 통과.

소스 검토 범위는 주문·재고·인증·요청 검증과 웹 URL 생성이다. 실제 MySQL 검증은 주문 조회로 스냅샷을 만든 뒤 별도 트랜잭션의 입고 커밋을 끼워 넣는 순서를 고정해 수행했다. H2 브라우저 캡처와 구분되는 서버 통합 테스트 근거다. 점검용 서버·MySQL 컨테이너·브라우저는 종료했다.

운영 데이터, 실제 부하, Docker 앱 이미지 실행은 검증하지 않았다. REA 캡처는 관찰 구간의 요청만 포함하며 요청 본문·응답 본문·요청 발생 스택은 수집하지 않았다. 전체 프로그램에 결함이 없다는 의미는 아니다. 생성한 JAR의 실행 검증도 이번 브라우저 캡처에 포함되지 않는다.
