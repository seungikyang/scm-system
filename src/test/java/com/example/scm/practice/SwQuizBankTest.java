package com.example.scm.practice;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

/** 객관식 원본과 브라우저 데이터의 3000문항 및 기존 풀이 기록 호환성을 검증한다. */
class SwQuizBankTest {
    private static final Path DIRECTORY = Path.of("practice", "sw-quiz");
    private static final int QUESTION_COUNT = 3000;

    @Test
    void bankHas3000UniqueQuestionsWithFourChoicesAndBalancedAnswers() throws IOException {
        Bank bank = readBank();
        assertThat(bank.version()).isNotBlank();
        assertThat(bank.categories()).hasSize(20);
        assertThat(bank.questions()).hasSize(QUESTION_COUNT);
        Set<Integer> ids = new HashSet<>();
        Set<String> prompts = new HashSet<>();
        Set<String> categories = new HashSet<>();
        for (Category category : bank.categories()) {
            assertThat(categories.add(category.id())).isTrue();
            assertThat(category.name()).isNotBlank();
            assertThat(DIRECTORY.resolve(category.reference()).normalize()).exists();
            assertThat(bank.questions().stream().filter(q -> q.category().equals(category.id())).count()).isEqualTo(150);
        }
        for (Question question : bank.questions()) {
            assertThat(question.id()).isBetween(1, QUESTION_COUNT);
            assertThat(ids.add(question.id())).isTrue();
            assertThat(prompts.add(question.prompt())).isTrue();
            assertThat(question.prompt()).isNotBlank();
            assertThat(question.category()).isIn(categories);
            assertThat(question.options()).hasSize(4).doesNotHaveDuplicates().allSatisfy(choice -> assertThat(choice).isNotBlank());
            assertThat(question.answer()).isBetween(0, 3);
            assertThat(question.explanation()).isNotBlank();
        }
        for (int index = 0; index < 4; index++) {
            final int answerIndex = index;
            assertThat(bank.questions().stream().filter(q -> q.answer() == answerIndex).count()).isEqualTo(750);
        }
    }

    @Test
    void expansionPreservesExisting2000QuestionsChoicesAndStorageKey() throws Exception {
        Bank bank = readBank();
        assertThat(bank.version()).isEqualTo("2026-10-03.v1");
        StringBuilder legacy = new StringBuilder();
        for (Question q : bank.questions().subList(0, 2000)) {
            legacy.append(q.id()).append('\n').append(q.category()).append('\n')
                    .append(q.prompt()).append('\n');
            q.options().forEach(option -> legacy.append(option).append('\n'));
            legacy.append(q.answer()).append('\n').append(q.explanation()).append('\n');
        }
        // 확장 전 2000문항 전체의 해시: 번호·보기·정답이 바뀌면 저장된 풀이가 잘못 해석될 수 있다.
        byte[] digest = MessageDigest.getInstance("SHA-256").digest(legacy.toString().getBytes(StandardCharsets.UTF_8));
        assertThat(HexFormat.of().formatHex(digest))
                .isEqualTo("c8996645c31d9ced961ed4b3993e719ef2d14f14ed2305583f58389361dc4423");
    }

    @Test
    void runtimeQuestionTextAnswerAndExplanationMatchEditableSource() throws IOException {
        List<String> rows = Files.readAllLines(DIRECTORY.resolve("questions.tsv")).stream()
                .filter(line -> !line.isBlank() && !line.startsWith("#")).toList();
        Bank bank = readBank();
        assertThat(rows).hasSize(QUESTION_COUNT);
        for (int i = 0; i < rows.size(); i++) {
            String[] fields = rows.get(i).split("\t", -1);
            Question question = bank.questions().get(i);
            assertThat(fields).hasSize(7);
            assertThat(question.id()).isEqualTo(i + 1);
            assertThat(question.category()).isEqualTo(fields[0]);
            assertThat(question.prompt()).isEqualTo(fields[1]);
            assertThat(question.options().get(question.answer())).isEqualTo(fields[2]);
            assertThat(question.options()).containsExactlyInAnyOrder(fields[2], fields[3], fields[4], fields[5]);
            assertThat(question.explanation()).isEqualTo(fields[6]);
        }
        assertThat(DIRECTORY.resolve("index.html")).exists();
        assertThat(DIRECTORY.resolve("quiz.js")).exists();
        assertThat(DIRECTORY.resolve("quiz.css")).exists();
    }

    private Bank readBank() throws IOException {
        return readBank("questions.js", "window.SCM_QUIZ_BANK = ");
    }

    @Test
    void convertedWorkbookCoversOriginalTopicsWithFourChoicesAndExistingReferences() throws IOException {
        Bank bank = readBank("workbook-questions.js", "window.SCM_WORKBOOK_QUIZ_BANK = ");
        ConversionMap mapping = JsonMapper.builder().build().readValue(
                Files.readString(DIRECTORY.resolve("conversion_map.json")), ConversionMap.class);
        assertThat(bank.categories()).hasSize(40);
        assertThat(bank.questions()).hasSize(mapping.questionOrder().size());
        assertThat(mapping.questionOrder()).doesNotHaveDuplicates();
        Map<String, String[]> editable = new java.util.HashMap<>();
        List<String> moduleRows = Files.readAllLines(DIRECTORY.resolve("module_choices.tsv")).stream()
                .filter(line -> !line.isBlank() && !line.startsWith("#")).toList();
        assertThat(moduleRows).hasSize(171);
        for (int i = 0; i < moduleRows.size(); i++) {
            String[] fields = moduleRows.get(i).split("\t", -1);
            assertThat(fields).hasSize(6);
            editable.put("m%03d".formatted(i + 1), fields);
        }
        for (String name : List.of("extra_choices.tsv", "test_choices.tsv", "remaining_choices.tsv")) {
            for (String row : Files.readAllLines(DIRECTORY.resolve(name))) {
                if (row.isBlank() || row.startsWith("#")) continue;
                String[] fields = row.split("\t", -1);
                assertThat(fields).hasSize(8);
                assertThat(editable.put(fields[0], java.util.Arrays.copyOfRange(fields, 2, 8))).isNull();
            }
        }
        assertThat(editable.keySet()).containsExactlyInAnyOrderElementsOf(mapping.questionOrder());
        assertThat(mapping.sources().stream().filter(s -> s.source().equals("practice/problems.md"))).hasSize(171);
        assertThat(mapping.sources()).allSatisfy(source -> {
            assertThat(source.key()).isIn(mapping.questionOrder());
            assertThat(source.original()).isNotBlank();
            assertThat(source.line()).isPositive();
            assertThat(Path.of(source.source())).exists();
        });
        Set<String> prompts = new HashSet<>();
        Set<String> ids = new HashSet<>();
        Set<String> categories = new HashSet<>();
        bank.categories().forEach(c -> categories.add(c.id()));
        for (int i = 0; i < bank.questions().size(); i++) {
            Question q = bank.questions().get(i);
            String[] fields = editable.get(mapping.questionOrder().get(i));
            assertThat(q.prompt()).isEqualTo(fields[0]);
            assertThat(q.options().get(q.answer())).isEqualTo(fields[1]);
            assertThat(q.options()).containsExactlyInAnyOrder(fields[1], fields[2], fields[3], fields[4]);
            assertThat(q.explanation()).isEqualTo(fields[5]);
            assertThat(q.id()).isEqualTo(i + 1);
            assertThat(q.code()).isEqualTo("W%03d".formatted(i + 1));
            assertThat(ids.add(q.code())).isTrue();
            assertThat(prompts.add(q.prompt())).isTrue();
            assertThat(q.category()).isIn(categories);
            assertThat(q.options()).hasSize(4).doesNotHaveDuplicates()
                    .allSatisfy(option -> assertThat(option).isNotBlank());
            assertThat(q.answer()).isBetween(0, 3);
            assertThat(q.explanation()).isNotBlank();
            String[] reference = q.reference().split("#", 2);
            Path target = DIRECTORY.resolve(reference[0]).normalize();
            assertThat(target).exists();
            assertThat(Files.readString(target)).contains("id=\"" + reference[1] + "\"");
        }
        long minimum = Long.MAX_VALUE, maximum = 0;
        for (int i = 0; i < 4; i++) {
            final int answer = i;
            long count = bank.questions().stream().filter(q -> q.answer() == answer).count();
            minimum = Math.min(minimum, count);
            maximum = Math.max(maximum, count);
        }
        assertThat(maximum - minimum).isLessThanOrEqualTo(1);
        assertThat(Files.readString(DIRECTORY.resolve("index.html"))).contains("workbook-questions.js", "bank-select");
    }

    @Test
    void allMarkdownChoicesAndAnswersMatchPlayableWorkbook() throws IOException {
        Bank bank = readBank("workbook-questions.js", "window.SCM_WORKBOOK_QUIZ_BANK = ");
        List<Path> documents = new java.util.ArrayList<>(List.of(
                Path.of("practice/problems.md"), Path.of("practice/workbook-questions.md"),
                Path.of("practice/feature-implementation-workbook.md"), Path.of("practice/README.md"),
                Path.of("docs/PORTFOLIO_GUIDE.md")));
        ConversionMap mapping = JsonMapper.builder().build().readValue(
                Files.readString(DIRECTORY.resolve("conversion_map.json")), ConversionMap.class);
        mapping.documents().forEach(d -> documents.add(Path.of(d.path())));
        mapping.embeddedDocuments().forEach(path -> documents.add(Path.of(path)));
        Pattern pattern = Pattern.compile("### W(\\d+)\\. ([^\\n]+)\\n.*?</details>", Pattern.DOTALL);
        for (Path path : documents) {
            Matcher matcher = pattern.matcher(Files.readString(path));
            int matches = 0;
            while (matcher.find()) {
                matches++;
                Question q = bank.questions().get(Integer.parseInt(matcher.group(1)) - 1);
                assertThat(matcher.group(2)).as(path + " " + q.code()).isEqualTo(q.prompt());
                for (int i = 0; i < 4; i++) {
                    assertThat(matcher.group()).contains("\n" + (i + 1) + ". " + q.options().get(i) + "\n");
                }
                assertThat(matcher.group()).contains("**정답: " + (q.answer() + 1) + "번**", q.explanation());
            }
            assertThat(matches).as(path.toString()).isPositive();
        }
        String answers = Files.readString(Path.of("practice/answers.md"));
        for (Question q : bank.questions()) {
            assertThat(answers).contains("[" + q.code() + "](workbook-questions.md#" + q.code().toLowerCase()
                    + ") · **" + (q.answer() + 1) + "번**: " + q.options().get(q.answer()));
        }
        for (CommentDocument doc : mapping.commentDocuments()) {
            String content = Files.readString(Path.of(doc.path()));
            assertThat(content).doesNotContain("// A:", "# A:");
            for (String key : doc.keys()) {
                Question q = bank.questions().get(mapping.questionOrder().indexOf(key));
                assertThat(content).contains("[" + q.code() + "] " + q.prompt());
                for (int i = 0; i < 4; i++) {
                    assertThat(content).contains("    " + (i + 1) + ". " + q.options().get(i));
                }
                assertThat(content).contains("정답·해설: practice/workbook-questions.md#" + q.code().toLowerCase());
            }
        }
    }

    @Test
    void allRemainingInlineQuestionsHaveChoicesAndNoEssayPromptsRemain() throws IOException {
        Bank bank = readBank("workbook-questions.js", "window.SCM_WORKBOOK_QUIZ_BANK = ");
        ConversionMap mapping = JsonMapper.builder().build().readValue(
                Files.readString(DIRECTORY.resolve("conversion_map.json")), ConversionMap.class);
        assertThat(mapping.inlineQuestions()).hasSize(79);
        assertThat(bank.questions()).hasSize(403);
        for (InlineQuestion source : mapping.inlineQuestions()) {
            Question q = bank.questions().get(mapping.questionOrder().indexOf(source.key()));
            String content = Files.readString(Path.of(source.path()));
            assertThat(content).as(source.path()).doesNotContain(source.original());
            if (source.path().endsWith(".md")) {
                assertThat(content).contains("### " + q.code() + ". " + q.prompt());
            } else {
                assertThat(content).contains("선택형 [" + q.code() + "] " + q.prompt());
            }
            for (int i = 0; i < 4; i++) {
                assertThat(content).contains((i + 1) + ". " + q.options().get(i));
            }
            assertThat(content).contains(source.path().endsWith(".md") ? "**정답: " + (q.answer() + 1) + "번**"
                    : "정답·해설: practice/workbook-questions.md#" + q.code().toLowerCase());
        }
        Pattern questionComment = Pattern.compile("^\\s*(?://|<!--|#\\s+TODO).*\\?.*$");
        try (var files = Files.walk(Path.of("practice", "starter"))) {
            for (Path file : files.filter(Files::isRegularFile).toList()) {
                for (String line : Files.readAllLines(file)) {
                    if (questionComment.matcher(line).matches() && !line.contains("브라우저에서 풀기:")) {
                        assertThat(line).as(file.toString()).containsPattern("\\[W\\d+\\]");
                    }
                }
            }
        }
        String feature = Files.readString(Path.of("practice", "feature-implementation-workbook.md"));
        assertThat(feature).doesNotContain("| 내 답 |", "| 결정할 질문 |");
        String contract = feature.substring(feature.indexOf("| 항목 | 확인할 계약 |"),
                feature.indexOf("요구사항이 모호하면 코딩으로 숨기지 말고"));
        for (String row : contract.lines().filter(line -> line.startsWith("|")).toList()) {
            assertThat(row.chars().filter(c -> c == '|').count()).isEqualTo(5);
        }
        assertThat(Files.readString(Path.of("docs", "LOOP_ENGINEERING.md")))
                .doesNotContain("| 단계 | 질문 | 필수 산출물 |");
    }

    @Test
    void learningFilesContainOnlyChoiceQuestionsAndNoWrittenAnswerForms() throws IOException {
        try (var files = Files.walk(Path.of("practice", "starter"))) {
            for (Path file : files.filter(Files::isRegularFile).toList()) {
                String content = Files.readString(file);
                if (file.toString().endsWith(".md")) {
                    String instructions = content.replaceAll("(?ms)^### W\\d+\\. .*?</details>", "");
                    assertThat(instructions).as(file.toString()).doesNotContain("____", "TODO", "코드 구현 실습", "코드 빈칸");
                } else {
                    assertThat(content).as(file.toString()).contains("선택형 [W", "정답·해설:");
                    for (String line : content.lines().toList()) {
                        assertThat(line.isBlank() || line.startsWith("//") || line.startsWith("#"))
                                .as(file + ": " + line).isTrue();
                    }
                }
            }
        }
        for (String file : List.of("practice/feature-implementation-workbook.md", "practice/LEARNING_NOTES.template.md")) {
            String instructions = Files.readString(Path.of(file)).replaceAll("(?ms)^### W\\d+\\. .*?</details>", "");
            assertThat(instructions).as(file).doesNotContain("____", "TODO", "구현 카드 빈칸:", "| 내 기록 |", "1분 면접 답변:");
        }
        assertThat(Files.readString(Path.of("practice/README.md")))
                .contains("각 문항에서 1~4번 중 하나를 선택합니다.", "오답 복습")
                .doesNotContain("workspace의 `TODO`와 `____`를 채웁니다", "1분 면접 답변");
    }

    @Test
    void studyDocumentsHaveNoUnconvertedEssayQuestionsOrWrittenAnswerInstructions() throws IOException {
        ConversionMap mapping = JsonMapper.builder().build().readValue(
                Files.readString(DIRECTORY.resolve("conversion_map.json")), ConversionMap.class);
        assertThat(mapping.embeddedDocuments())
                .contains("docs/scm_system_PRD_TRD.md", "docs/TECHNOLOGY_GUIDE.md");
        List<Source> interviewSources = mapping.sources().stream()
                .filter(source -> source.source().equals("docs/scm_system_PRD_TRD.md")).toList();
        assertThat(interviewSources).hasSize(12);
        String requirements = Files.readString(Path.of("docs/scm_system_PRD_TRD.md"));
        for (Source source : interviewSources) {
            assertThat(requirements).doesNotContain(source.original());
        }

        // 설명 문서의 질문형 제목과 실제 선택형 문항을 제외한 학습 지시를 검사한다.
        // 개인 풀이 공간은 검사·수정 대상에서 제외한다.
        Pattern essayQuestion = Pattern.compile("(?m)^\\s*Q\\s*\\d+[.:]\\s+.*");
        Pattern writtenAnswer = Pattern.compile(
                "서술(?:하시오|하세요)|설명하시오|작성(?:하시오|해 보세요)|(?:자신의 말로 )?적어\\s*보세요");
        for (Path directory : List.of(Path.of("docs"), Path.of("practice"))) {
            try (var files = Files.walk(directory)) {
                for (Path file : files.filter(Files::isRegularFile)
                        .filter(path -> path.toString().endsWith(".md"))
                        .filter(path -> !path.startsWith(Path.of("practice", "workspace"))).toList()) {
                    String instructions = Files.readString(file)
                            .replaceAll("(?ms)^### W\\d+\\. .*?</details>", "");
                    assertThat(essayQuestion.matcher(instructions).find()).as(file + " 서술형 질문").isFalse();
                    assertThat(writtenAnswer.matcher(instructions).find()).as(file + " 서술형 답안 지시").isFalse();
                }
            }
        }
    }

    @Test
    void convertedWorkbookExpansionPreservesExisting363AnswersAndStorageKey() throws Exception {
        Bank bank = readBank("workbook-questions.js", "window.SCM_WORKBOOK_QUIZ_BANK = ");
        assertThat(bank.version()).isEqualTo("2026-10-05.workbook.v1");
        StringBuilder legacy = new StringBuilder();
        for (Question q : bank.questions().subList(0, 363)) {
            legacy.append(q.id()).append('\n').append(q.category()).append('\n')
                    .append(q.prompt()).append('\n');
            q.options().forEach(option -> legacy.append(option).append('\n'));
            legacy.append(q.answer()).append('\n').append(q.explanation()).append('\n');
        }
        byte[] digest = MessageDigest.getInstance("SHA-256").digest(legacy.toString().getBytes(StandardCharsets.UTF_8));
        assertThat(HexFormat.of().formatHex(digest))
                .isEqualTo("b208c315dc7fea095d8a23a52e5636e279991088c6046247e7fe33728b66f4f7");
    }

    private Bank readBank(String file, String marker) throws IOException {
        String script = Files.readString(DIRECTORY.resolve(file));
        assertThat(script).contains(marker);
        String json = script.substring(script.indexOf(marker) + marker.length()).trim();
        assertThat(json).endsWith(";");
        return JsonMapper.builder().build().readValue(json.substring(0, json.length() - 1), Bank.class);
    }

    record Bank(String version, List<Category> categories, List<Question> questions) {}
    record Category(String id, String name, String reference) {}
    record Question(int id, String category, String prompt, List<String> options, int answer, String explanation,
                    String code, String reference) {}
    record Source(String key, Integer module, String source, int line, String original, String section) {}
    record Module(int id, String title, List<String> files) {}
    record CodeExample(String section, String language, String content) {}
    record Document(String path, String title, List<String> keys, List<CodeExample> codeExamples) {}
    record CommentDocument(String path, List<String> keys) {}
    record InlineQuestion(String path, int line, String original, String key) {}
    record ConversionMap(List<Module> modules, List<Source> sources, List<Document> documents,
                         Map<String, String> featureReplacements, Map<String, String> portfolioReplacements,
                         Map<String, String> readmeReplacements, List<String> questionOrder,
                         List<CommentDocument> commentDocuments, List<InlineQuestion> inlineQuestions,
                         List<String> embeddedDocuments) {}
}
