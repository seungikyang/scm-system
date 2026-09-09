#!/usr/bin/env bash
set -euo pipefail

script_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd -P)"
repository_root="$(cd "${script_dir}/.." && pwd -P)"
git_root="$(git -C "${repository_root}" rev-parse --show-toplevel)"
current_directory="$(pwd -P)"

if [[ "${repository_root}" != "${git_root}" ]]; then
    echo "ERROR: 검증 스크립트가 저장소 루트 바로 아래에 있지 않습니다." >&2
    exit 1
fi

if [[ "${current_directory}" != "${repository_root}" ]]; then
    echo "ERROR: 저장소 루트에서 실행하세요: ${repository_root}" >&2
    exit 1
fi

violations=()
while IFS= read -r -d '' path; do
    case "${path}" in
        .omc/*|.playwright-cli/*|.gradle/*|build/*|bin/*|out/*|output/*|_workspace/*|_workspace_prev/*|practice/workspace/*|build-report.json)
            violations+=("${path}")
            ;;
    esac
done < <(
    git -C "${repository_root}" ls-files -z
    git -C "${repository_root}" diff --cached --name-only -z
)

if (( ${#violations[@]} > 0 )); then
    echo "ERROR: 임시 영역의 파일은 프로그램 변경으로 추적할 수 없습니다." >&2
    printf ' - %s\n' "${violations[@]}" >&2
    exit 1
fi

echo "Repository boundary OK: ${repository_root}"
