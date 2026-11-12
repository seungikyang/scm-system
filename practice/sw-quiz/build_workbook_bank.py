#!/usr/bin/env python3
"""서술형 전환 문항의 TSV 원본에서 브라우저·Markdown 문제집을 함께 만든다."""
import csv
import json
import os
import random
import re
from pathlib import Path

DIRECTORY = Path(__file__).resolve().parent
ROOT = DIRECTORY.parent.parent
MAP_PATH = DIRECTORY / "conversion_map.json"


def rows(name, columns):
    # 원본에서 한 줄을 한 문항으로 읽습니다. 칸 수가 틀리거나 비어 있으면 잘못된 문항 생성을 멈춥니다.
    with (DIRECTORY / name).open(encoding="utf-8", newline="") as source:
        result = [row for row in csv.reader(source, delimiter="\t")
                  if row and not row[0].startswith("#")]
    for row in result:
        if len(row) != columns or not all(field.strip() for field in row):
            raise ValueError(f"{name}: {columns}개 비어 있지 않은 열이 필요합니다: {row!r}")
    return result


def read_questions():
    # 연결표는 '어느 원래 질문이 어느 객관식 문항으로 바뀌었는지'와 분야·문항 순서를 기록합니다.
    layout = json.loads(MAP_PATH.read_text(encoding="utf-8"))
    module_sources = [s for s in layout["sources"] if s["source"] == "practice/problems.md"]
    module_rows = rows("module_choices.tsv", 6)
    if len(module_rows) != 171 or len(module_sources) != len(module_rows):
        raise ValueError("모듈 원문 171항목과 전환 문항이 일치해야 합니다.")
    raw = [(source["key"], str(source["module"]), *row)
           for source, row in zip(module_sources, module_rows)]
    raw.extend(rows("extra_choices.tsv", 8))
    raw.extend(rows("test_choices.tsv", 8))
    raw.extend(rows("remaining_choices.tsv", 8))
    keys = [row[0] for row in raw]
    if len(set(keys)) != len(keys):
        raise ValueError("문항 키가 중복됩니다.")
    if len({row[2] for row in raw}) != len(raw):
        raise ValueError("질문이 중복됩니다.")
    module_ids = {m["id"] for m in layout["modules"]}
    for source in layout["sources"]:
        if source["key"] not in keys or not source["original"].strip():
            raise ValueError(f"전환 연결이 없는 원문: {source}")
        if not (ROOT / source["source"]).is_file() or source["line"] < 1:
            raise ValueError(f"출처 파일 또는 원래 행 번호 오류: {source}")
    # 문항을 추가하더라도 기존 번호·보기 순서·저장된 답안의 의미를 유지한다.
    order = layout.get("questionOrder", keys)
    if len(order) != len(set(order)) or set(order) != set(keys):
        raise ValueError("conversion_map.json의 questionOrder에 모든 문항 키가 한 번씩 있어야 합니다.")
    raw_by_key = {row[0]: row for row in raw}
    raw = [raw_by_key[key] for key in order]
    positions = []
    for block in range((len(raw) + 3) // 4):
        # 네 문항씩 정답 위치를 한 번씩 사용합니다. 일정한 시작값 덕분에 다시 생성해도 순서가 같습니다.
        choices = [0, 1, 2, 3]
        random.Random(20261005 + block).shuffle(choices)
        positions.extend(choices)
    questions = []
    by_key = {}
    for index, (key, module, prompt, correct, wrong1, wrong2, wrong3, explanation) in enumerate(raw):
        if int(module) not in module_ids or len({correct, wrong1, wrong2, wrong3}) != 4:
            raise ValueError(f"분야 또는 4개 선택지 오류: {key}")
        options = [wrong1, wrong2, wrong3]
        options.insert(positions[index], correct)
        # index는 0부터, 사람이 읽는 id와 W번호는 1부터 셉니다. answer는 선택지의 0~3 위치입니다.
        q = dict(id=index + 1, code=f"W{index + 1:03}", category=f"module-{int(module):02}",
                 prompt=prompt, options=options, answer=positions[index], explanation=explanation,
                 reference=f"../workbook-questions.md#w{index + 1:03}")
        questions.append(q)
        by_key[key] = q
    categories = [dict(id=f"module-{m['id']:02}", name=f"{m['id']:02} · {m['title']}",
                       reference=f"../problems.md#module-{m['id']:02}") for m in layout["modules"]]
    bank = dict(version="2026-10-05.workbook.v1", categories=categories, questions=questions)
    return layout, bank, by_key


def relative(from_path, target):
    # 문서가 있는 폴더를 출발점으로 링크를 만듭니다. 예: ../는 한 단계 위 폴더로 가라는 뜻입니다.
    return Path(os.path.relpath(ROOT / target, (ROOT / from_path).parent)).as_posix()


def render_question(q, path):
    # 한 문항의 자료로 질문·보기·숨겨 둔 해설을 만듭니다. details는 눌렀을 때 펼치는 HTML 상자입니다.
    play = relative(path, "practice/sw-quiz/index.html") + f"?bank=workbook&question={q['id']}"
    parts = [f'<a id="{q["code"].lower()}"></a>', f'### {q["code"]}. {q["prompt"]}', "",
             f"[브라우저에서 풀기]({play})", ""]
    parts.extend(f"{i + 1}. {option}" for i, option in enumerate(q["options"]))
    parts.extend(["", "<details>", "<summary>정답·해설 보기</summary>", "",
                  f'**정답: {q["answer"] + 1}번** · {q["options"][q["answer"]]}', "",
                  q["explanation"], "", "</details>", ""])
    return "\n".join(parts) + "\n"


def render_starter_banks(layout, by_key):
    """40개 모듈의 모든 학습 파일을 답을 선택하는 문제집으로 생성한다."""
    for module in layout["modules"]:
        for filename in module["files"]:
            path = "practice/" + filename
            keys = {key for key, q in by_key.items() if q["category"] == f'module-{module["id"]:02}'}
            keys.update(source["key"] for source in layout["sources"] if source["source"] == path)
            for doc in layout["documents"] + layout.get("commentDocuments", []):
                if doc["path"] == path:
                    keys.update(doc["keys"])
            questions = [by_key[key] for key in layout["questionOrder"] if key in keys]
            play = relative(path, "practice/sw-quiz/index.html") + f'?bank=workbook&module={module["id"]:02}'
            if Path(path).suffix == ".md":
                text = [f'# {module["title"]} · 4지 선다', "",
                        f'[이 모듈을 브라우저에서 풀기]({play}) · [전체 문제집]({relative(path, "practice/workbook-questions.md")})', "",
                        "각 문제에서 1~4번 중 하나를 고르고 정답·해설을 확인합니다.", ""]
                text.extend(render_question(q, path) for q in questions)
            else:
                prefix = "#" if path.endswith(".yml.fragment") else "//"
                text = [f'{prefix} {module["title"]} · 4지 선다',
                        f'{prefix} 브라우저에서 풀기: {play}',
                        f'{prefix} 각 문제에서 1~4번 중 하나를 고릅니다. 정답·해설 보기: practice/answers.md', ""]
                for q in questions:
                    text.append(f'{prefix} 선택형 [{q["code"]}] {q["prompt"]}')
                    text.extend(f'{prefix}    {i + 1}. {option}' for i, option in enumerate(q["options"]))
                    text.extend([f'{prefix}    정답·해설: practice/workbook-questions.md#{q["code"].lower()}',
                                 f'{prefix}    정답: {q["answer"] + 1}번 · {q["explanation"]}', ""])
            (ROOT / path).write_text("\n".join(text).rstrip() + "\n", encoding="utf-8")


def build():
    # 같은 bank 자료에서 브라우저용 파일과 문서용 문제집·정답표를 만들어 내용이 서로 맞게 합니다.
    layout, bank, by_key = read_questions()
    js = "// TSV 원본에서 생성: python3 practice/sw-quiz/build_workbook_bank.py\n"
    js += "window.SCM_WORKBOOK_QUIZ_BANK = " + json.dumps(bank, ensure_ascii=False, indent=2) + ";\n"
    (DIRECTORY / "workbook-questions.js").write_text(js, encoding="utf-8")
    book_path = "practice/workbook-questions.md"
    book = [f'# SCM 모듈 4지 선다 워크북 · {len(bank["questions"])}문항', "",
            "[브라우저에서 채점·오답 복습](sw-quiz/index.html?bank=workbook) · [모듈별 문제](problems.md) · [정답표](answers.md)", "",
            "모듈 문제·기능 개념·테스트 기대 결과·보안·설계·면접 질문을 객관식으로 전환했습니다. 각 문항은 선택지 4개와 정답 1개·해설을 포함합니다. 설계 선택 문항은 질문의 전제와 현재 구현을 기준으로 풉니다.", "",
            "문항은 TSV에서 관리하며, 원문 위치와 문항의 대응은 [전환 연결표](sw-quiz/conversion_map.json)에 있습니다. 연결표의 행 번호는 변환 전 원문 기준입니다.", ""]
    for category in bank["categories"]:
        book.extend([f'## {category["name"]}', ""])
        for q in bank["questions"]:
            if q["category"] == category["id"]:
                book.append(render_question(q, book_path))
    (ROOT / book_path).write_text("\n".join(book).rstrip() + "\n", encoding="utf-8")
    problems_path = "practice/problems.md"
    problems = ["# 모듈별 4지 선다 문제", "",
                f'40개 모듈의 {len(bank["questions"])}문항을 모두 선택지 4개·정답 1개·해설로 제공합니다.', "",
                "[브라우저 문제집](sw-quiz/index.html?bank=workbook) · [전체 전환 문제집](workbook-questions.md) · [정답표](answers.md)", "",
                "모듈 번호와 starter 경로는 고정 식별자입니다. [권장 학습 트랙](README.md#학습-트랙)의 순서로 4지 선다를 풉니다.", ""]
    for module in layout["modules"]:
        problems.extend([f'<a id="module-{module["id"]:02}"></a>', f'## {module["id"]}. {module["title"]}', ""])
        for filename in module["files"]:
            problems.append(f"- [선택형 학습 파일]({filename})")
        problems.append("")
        for q in bank["questions"]:
            if q["category"] == f'module-{module["id"]:02}':
                problems.append(render_question(q, problems_path))
    (ROOT / problems_path).write_text("\n".join(problems).rstrip() + "\n", encoding="utf-8")
    answers = ["# 객관식 정답·해설", "", "[먼저 문제 풀기](sw-quiz/index.html?bank=workbook) · [전체 문항](workbook-questions.md)", "",
               "브라우저·Markdown 문제집과 같은 TSV에서 생성한 정답표입니다. 보기 순서와 정답 번호가 동일합니다.", ""]
    for module in layout["modules"]:
        answers.extend([f'## {module["id"]}. {module["title"]}', ""])
        for q in bank["questions"]:
            if q["category"] == f'module-{module["id"]:02}':
                answers.extend([f'- [{q["code"]}](workbook-questions.md#{q["code"].lower()}) · **{q["answer"] + 1}번**: {q["options"][q["answer"]]}',
                                f'  - 해설: {q["explanation"]}'])
        answers.append("")
    (ROOT / "practice/answers.md").write_text("\n".join(answers).rstrip() + "\n", encoding="utf-8")
    for doc in layout["documents"]:
        path = doc["path"]
        play = relative(path, "practice/sw-quiz/index.html") + "?bank=workbook"
        text = [f'# {doc["title"]}', "", f"[브라우저 문제집]({play}) · [전체 객관식 워크북]({relative(path, book_path)})", "",
                "서술형·개념 확인 질문을 4지 선다로 전환했습니다. 한 문제에 하나의 답을 고른 뒤 해설을 확인하세요.", ""]
        for key in doc["keys"]:
            text.append(render_question(by_key[key], path))
        (ROOT / path).write_text("\n".join(text).rstrip() + "\n", encoding="utf-8")
    # 안내·기능 문서에 삽입된 문항도 동일한 정답 번호로 동기화한다.
    by_code = {q["code"]: q for q in bank["questions"]}
    for path in ("practice/feature-implementation-workbook.md", "practice/README.md", "docs/PORTFOLIO_GUIDE.md",
                 *layout.get("embeddedDocuments", [])):
        target = ROOT / path

        def update(match):
            question = by_code[match[1]]
            rendered = render_question(question, path)
            return re.sub(r'^<a id="[^"]+"></a>\n', "", rendered).rstrip()

        content = re.sub(r"### (W\d+)\. [^\n]+\n.*?</details>", update,
                         target.read_text(encoding="utf-8"), flags=re.S)
        target.write_text(content.rstrip() + "\n", encoding="utf-8")
    render_starter_banks(layout, by_key)
    print(f'객관식 워크북 {len(bank["questions"])}문항, 원문 연결 {len(layout["sources"])}항목 생성 완료')


if __name__ == "__main__":
    build()
