#!/usr/bin/env python3
"""정식 TSV 원본으로 file://에서도 읽을 수 있는 문제 데이터를 생성한다."""
import csv
import json
import random
from pathlib import Path

# __file__은 이 프로그램의 파일 경로입니다. 어느 폴더에서 실행해도 옆의 원본 자료를 찾게 합니다.
DIRECTORY = Path(__file__).resolve().parent
CATEGORIES = [
    ("java", "Java 기본 문법", "../../docs/TECHNOLOGY_GUIDE.md"),
    ("oop", "객체 설계와 Lombok", "../../src/main/java/com/example/scm/domain/PurchaseOrder.java"),
    ("collections", "컬렉션·Stream·Optional", "../../src/main/java/com/example/scm/service/PurchaseOrderService.java"),
    ("money", "금액·날짜·자료형", "../../src/main/java/com/example/scm/dto/purchaseorder/PurchaseOrderCreateRequest.java"),
    ("di", "Spring DI와 계층", "../../docs/TECHNOLOGY_GUIDE.md"),
    ("boot", "Boot 설정과 실행", "../../src/main/resources/application.yml"),
    ("mvc", "Spring MVC 요청 처리", "../../src/main/java/com/example/scm/controller/web/PurchaseOrderWebController.java"),
    ("rest", "HTTP·REST·JSON", "../../docs/API_SPEC.md"),
    ("validation", "DTO·입력 검증·예외", "../../src/main/java/com/example/scm/common/exception/ApiExceptionHandler.java"),
    ("entity", "JPA 엔티티와 매핑", "../../docs/ERD.md"),
    ("repository", "Repository·검색·페이징", "../../src/main/java/com/example/scm/repository/StockRepository.java"),
    ("transaction", "트랜잭션과 영속성", "../../src/main/java/com/example/scm/service/PurchaseOrderPersistenceService.java"),
    ("concurrency", "동시성과 잠금", "../../src/main/java/com/example/scm/service/SalesOrderService.java"),
    ("security", "인증·인가·세션·CSRF", "../../src/main/java/com/example/scm/config/SecurityConfig.java"),
    ("thymeleaf", "Thymeleaf 화면과 폼", "../../src/main/resources/templates/purchaseorder/form.html"),
    ("browser", "JavaScript와 브라우저", "../../src/main/resources/static/js/purchase-order-form.js"),
    ("database", "SQL·H2·MySQL·Flyway", "../../src/main/resources/db/migration/mysql/V1__baseline_schema.sql"),
    ("build", "Gradle·Docker·운영", "../../build.gradle"),
    ("testing", "JUnit·Mockito·통합 테스트", "../../src/test/java/com/example/scm/controller/SalesAndNoticeIntegrationTest.java"),
    ("scm", "SCM 상태·권한·재고", "../../docs/STATE_MACHINE.md"),
]
QUESTION_COUNT = 3000
QUESTIONS_PER_CATEGORY = 150
LEGACY_QUESTION_COUNT = 2000

def build():
    questions = []
    # 이전 확장 구간의 난수 시드를 유지해 1~2000번의 보기와 제출 기록을 보존합니다.
    positions = []
    for count, seed in ((500, 20261003), (1500, 20261010),
                        (QUESTION_COUNT - LEGACY_QUESTION_COUNT, 20261017)):
        segment = [0, 1, 2, 3] * (count // 4)
        random.Random(seed).shuffle(segment)
        positions.extend(segment)
    with (DIRECTORY / "questions.tsv").open(encoding="utf-8", newline="") as source:
        # 코드의 큰따옴표는 문항 내용입니다. CSV 인용 규칙으로 제거하지 않습니다.
        for row in csv.reader(source, delimiter="\t", quoting=csv.QUOTE_NONE):
            # TSV는 탭으로 칸을 나눈 글 파일입니다. 한 줄에서 질문·정답·오답 3개·해설을 읽습니다.
            if not row or row[0].startswith("#"):
                continue
            if len(row) != 7:
                raise ValueError(f"7개 열이 필요합니다: {row!r}")
            category, prompt, correct, *rest = row
            distractors, explanation = rest[:3], rest[3]
            if category not in {c[0] for c in CATEGORIES}:
                raise ValueError(f"알 수 없는 분야: {category}")
            if not all(field.strip() for field in row) or len(set([correct, *distractors])) != 4:
                raise ValueError(f"빈 값 또는 중복 선택지: {prompt}")
            if len(questions) >= QUESTION_COUNT:
                raise ValueError(f"{QUESTION_COUNT}문제를 초과했습니다.")
            position = positions[len(questions)]
            options = list(distractors)
            # 오답 3개 사이의 지정된 자리에 정답을 넣어 선택지 4개를 완성합니다.
            options.insert(position, correct)
            questions.append(dict(id=len(questions) + 1, category=category, prompt=prompt,
                                  options=options, answer=position, explanation=explanation))
    if len(questions) != QUESTION_COUNT or len({q['prompt'] for q in questions}) != QUESTION_COUNT:
        raise ValueError(f"서로 다른 질문 {QUESTION_COUNT}개가 필요합니다.")
    for category, _, _ in CATEGORIES:
        count = sum(q['category'] == category for q in questions)
        if count != QUESTIONS_PER_CATEGORY:
            raise ValueError(f"{category}: {QUESTIONS_PER_CATEGORY}문제가 필요하지만 {count}개입니다.")
    # version은 풀이 기록 호환성을 나타냅니다. 기존 문항을 유지한 추가 확장이므로 키를 유지합니다.
    bank = dict(version="2026-10-03.v1", categories=[dict(id=c, name=n, reference=r) for c, n, r in CATEGORIES],
                questions=questions)
    target = DIRECTORY / "questions.js"
    # 자료를 JavaScript 파일로 감싸면 브라우저가 서버 없이도 script 태그로 읽을 수 있습니다.
    target.write_text("// questions.tsv에서 생성. 수정 후 python3 practice/sw-quiz/build_bank.py 실행.\n"
                      + "window.SCM_QUIZ_BANK = " + json.dumps(bank, ensure_ascii=False, indent=2) + ";\n", encoding="utf-8")
    print(f"{QUESTION_COUNT}문제 생성 완료: {target.relative_to(DIRECTORY.parent.parent)}")

if __name__ == "__main__":
    build()
