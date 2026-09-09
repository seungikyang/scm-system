# SCM 저장소 작업 규칙

이 파일의 규칙은 저장소 전체에 적용한다.

## 저장소 루트 강제

- 파일을 읽거나 변경하기 전에 `git rev-parse --show-toplevel`로 저장소 루트를 확인하고 그 디렉터리에서 작업한다.
- 프로그램·테스트·문서의 정식 변경은 이 저장소 루트 아래의 표준 경로(`src/`, `docs/`, `practice/`, `.github/`, 루트 설정 파일)에만 작성한다.
- `/tmp`, 시스템 임시 디렉터리, `.omc/`, `.playwright-cli/`, `.gradle/`, `build/`, `bin/`, `out/`, `output/`, `_workspace/`, `_workspace_prev/`에서 프로그램을 구현하거나 원본을 유지하지 않는다.
- 임시 영역은 재생성 가능한 로그·캐시·스크린샷·진단 결과에만 사용한다. 보존할 결과는 검토 후 저장소의 정식 경로로 옮긴다.
- `practice/workspace/`는 Git에서 제외된 개인 풀이 공간이다. 정식 프로그램 소스를 작성하지 않되, 사용자의 명시적 지시 없이 삭제하거나 덮어쓰지 않는다.
- 커밋 전 `./scripts/verify-repository-boundary.sh`를 실행한다. 실패하면 경계를 위반한 파일을 먼저 정리한다.
- 무시된 임시 파일을 `git add -f`로 강제 추가하지 않는다.
