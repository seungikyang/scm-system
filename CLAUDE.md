# scm-system

Java Spring Boot 기반 공급망 관리 시스템(SCM) — 거래처/품목/카테고리/발주/수주/공지. 상세 요구사항은 `scm_system_PRD_TRD.md` 참조. 스택: Java 17 + Spring Boot + Spring Data JPA + Thymeleaf + H2/MySQL + Gradle + JUnit 5.

## 하네스: SCM 개발 (SDLC)

**목표:** 요구사항 분석 → 설계 → 구현(백엔드 + Thymeleaf 화면) → QA → 문서화를 전문 에이전트 팀으로 조율해, PRD/TRD를 정합성 있는 구현으로 만든다.

**트리거:** SCM 기능 구현·설계·개발 작업(예: "발주 기능 만들어줘", "거래처 모듈 구현", "수주 화면 추가", "API 설계")이나 그 후속 요청("다시 실행", "재실행", "수정", "보완", "OO만 다시", "결과 개선") 시 `scm-sdlc-orchestrator` 스킬을 사용하라. 단순 단일 질문은 직접 응답 가능.

**변경 이력:**
| 날짜 | 변경 내용 | 대상 | 사유 |
|------|----------|------|------|
| 2026-05-30 | 초기 구성 (에이전트 6, 스킬 7) | 전체 | SDLC 전체 + 풀스택(Spring Boot/Thymeleaf) 하네스 구축 |
