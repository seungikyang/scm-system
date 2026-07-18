# SCM 문서 지도

> [HTML 학습 목차](../index.html) · [프로젝트 README](../README.md) · [루프 엔지니어링](./LOOP_ENGINEERING.md) · [취업 포트폴리오](./PORTFOLIO_GUIDE.md)

이 저장소는 실행 가능한 SCM 구현과 직접 채우는 취업용 워크북을 함께 제공한다. 처음 방문했다면 아래 순서로 읽는다.

## 1. 처음 시작할 때

| 순서 | 문서 | 목적 |
|---|---|---|
| 1 | [`../index.html`](../index.html) | 전체 학습 목차, 모듈 검색, 진도 관리 |
| 2 | [`../README.md`](../README.md) | 구현 범위와 실행 방법 |
| 3 | [`../practice/README.md`](../practice/README.md) | 워크북 사용법과 학습 트랙 |
| 4 | [`LOOP_ENGINEERING.md`](./LOOP_ENGINEERING.md) | 반복 학습·개선 운영 규칙 |
| 5 | [`PORTFOLIO_GUIDE.md`](./PORTFOLIO_GUIDE.md) | 데모, 이력서, 면접 준비 |

## 2. 구현 계약

| 문서 | 기준 내용 | 갱신 시점 |
|---|---|---|
| [`API_SPEC.md`](./API_SPEC.md) | 실제 제공하는 REST API와 에러 형식 | Controller/DTO/권한 변경 시 |
| [`ERD.md`](./ERD.md) | 현재 엔티티, Flyway 테이블과 스키마 변경 규칙 | Entity/Repository/DB 변경 시 |
| [`STATE_MACHINE.md`](./STATE_MACHINE.md) | 발주 상태 전이와 권한 | 상태·권한 규칙 변경 시 |
| [`TROUBLESHOOTING.md`](./TROUBLESHOOTING.md) | 장애, 동시성, 구현 차이 | 원인과 해결을 확인했을 때 |
| [`../practice/DESIGN_DECISIONS.md`](../practice/DESIGN_DECISIONS.md) | 참조 구현의 설계 선택 | 대안 중 정책을 확정할 때 |

## 3. 학습 자료

| 문서 | 사용법 |
|---|---|
| [`../practice/problems.md`](../practice/problems.md) | 모듈별 문제와 학습 목표를 먼저 읽는다. |
| [`../practice/feature-implementation-workbook.md`](../practice/feature-implementation-workbook.md) | 요구사항을 계층·상태·테스트로 분해한다. |
| [`../practice/answers.md`](../practice/answers.md) | 구현 후 판단 방향을 비교한다. 정답 코드가 아니다. |
| [`../practice/REFERENCE_MAP.md`](../practice/REFERENCE_MAP.md) | 문제와 실제 코드·테스트를 연결한다. |
| [`../practice/LEARNING_NOTES.template.md`](../practice/LEARNING_NOTES.template.md) | 자신의 설계·실패·설명을 기록한다. |

## 4. 원본과 이력

| 문서 | 성격 |
|---|---|
| [`../scm_system_PRD_TRD.md`](../scm_system_PRD_TRD.md) | 최초 제품·기술 요구사항. 구현과 차이가 있어도 원본을 보존한다. |
| [`../history.html`](../history.html) | 프로젝트 선택부터 구현까지의 시간순 기록이다. |

## 5. 문서 동기화 규칙

- API가 바뀌면 `API_SPEC`과 관련 워크북 문제를 함께 확인한다.
- 엔티티가 바뀌면 `ERD`, `DESIGN_DECISIONS`, 통합 테스트를 확인한다.
- 상태 또는 권한이 바뀌면 `STATE_MACHINE`, API 권한표, Service 테스트를 함께 갱신한다.
- 구현 범위가 바뀌면 `README`, 이 문서, `index.html`의 상태 배지를 함께 갱신한다.
- 원본 PRD/TRD와 다르게 결정했다면 `TROUBLESHOOTING`에 차이와 이유를 남긴다.
