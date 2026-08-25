package com.example.scm.practice;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("학습 워크북 구조 테스트")
class WorkbookStructureTest {

    private static final Pattern MODULE_DIRECTORY = Pattern.compile("(\\d{2})-.+");
    private static final Pattern SECTION_HEADING = Pattern.compile("(?m)^## (\\d+)\\.");
    private static final Pattern HTML_MODULE = Pattern.compile("data-module=\"(\\d{2})\"");
    private static final Pattern HTML_LINK = Pattern.compile("href=\"([^\"]+)\"");
    private static final Set<Integer> EXPECTED_MODULES =
            IntStream.rangeClosed(0, 39).boxed().collect(Collectors.toCollection(TreeSet::new));

    @Test
    @DisplayName("00~39 starter 모듈이 빠짐없이 있고 각 모듈에 학습 빈칸이 있다")
    void starterModules_areComplete() throws IOException {
        Path starter = Path.of("practice", "starter");
        Set<Integer> actual = new TreeSet<>();

        try (var directories = Files.list(starter)) {
            for (Path directory : directories.filter(Files::isDirectory).toList()) {
                Matcher matcher = MODULE_DIRECTORY.matcher(directory.getFileName().toString());
                assertThat(matcher.matches()).as("starter 디렉터리 이름: %s", directory).isTrue();
                actual.add(Integer.parseInt(matcher.group(1)));

                String content;
                try (var files = Files.walk(directory)) {
                    content = files.filter(Files::isRegularFile)
                            .map(this::read)
                            .collect(Collectors.joining("\n"));
                }
                assertThat(content).as("%s의 학습 빈칸", directory).contains("____");
            }
        }
        assertThat(actual).isEqualTo(EXPECTED_MODULES);
    }

    @Test
    @DisplayName("문제와 정답 방향 문서가 00~39 모듈을 동일하게 추적한다")
    void problemsAndAnswers_trackSameModules() throws IOException {
        assertThat(moduleSections(Path.of("practice", "problems.md")))
                .isEqualTo(EXPECTED_MODULES);
        assertThat(moduleSections(Path.of("practice", "answers.md")))
                .isEqualTo(EXPECTED_MODULES);
    }

    @Test
    @DisplayName("학습 시작·설계 기준·참조 코드 연결 안내가 제공된다")
    void learningGuides_arePresent() throws IOException {
        String readme = Files.readString(Path.of("practice", "README.md"));
        assertThat(readme)
                .contains("./gradlew practiceInit")
                .contains("./gradlew practiceStatus")
                .contains("핵심 트랙")
                .contains("확장 트랙")
                .contains("실패 사례 3개")
                .contains("../index.html")
                .contains("LOOP_ENGINEERING.md");

        assertThat(Path.of("index.html")).exists();
        assertThat(Path.of("docs", "INDEX.md")).exists();
        assertThat(Path.of("docs", "LOOP_ENGINEERING.md")).exists();
        assertThat(Path.of("docs", "PORTFOLIO_GUIDE.md")).exists();
        assertThat(Path.of("practice", "LEARNING_NOTES.template.md")).exists();
        assertThat(Path.of("practice", "DESIGN_DECISIONS.md")).exists();
        assertThat(Path.of("practice", "REFERENCE_MAP.md")).exists();
        assertThat(Files.readString(Path.of(".gitignore"))).contains("practice/workspace/");
    }

    @Test
    @DisplayName("코드와 워크북이 최신 안정 빌드 기준을 함께 사용한다")
    void buildAndWorkbook_useCurrentStableBaseline() throws IOException {
        String build = Files.readString(Path.of("build.gradle"));
        String wrapper = Files.readString(
                Path.of("gradle", "wrapper", "gradle-wrapper.properties"));
        String workbook = Files.readString(
                Path.of("practice", "feature-implementation-workbook.md"));

        assertThat(build)
                .contains("id 'org.springframework.boot' version '4.1.1'")
                .contains("id 'io.spring.dependency-management' version '1.1.7'")
                .contains("spring-boot-starter-webmvc")
                .contains("spring-boot-starter-webmvc-test");
        assertThat(wrapper).contains("gradle-9.7.1-bin.zip");
        assertThat(workbook)
                .contains("Spring Boot 4.1.1")
                .contains("Gradle Wrapper 9.7.1")
                .contains("Jackson 3");
    }

    @Test
    @DisplayName("기능 워크북이 Spring 상세 흐름과 macOS 배포 운영 절차를 제공한다")
    void featureWorkbook_coversSpringAndMacDeployment() throws IOException {
        String workbook = Files.readString(
                Path.of("practice", "feature-implementation-workbook.md"));

        assertThat(workbook)
                .contains("Spring Framework 상세 이해")
                .contains("현재 기준 버전과 호환성 경계")
                .contains("Spring Boot 4.1.1")
                .contains("Spring Framework 7.0.x")
                .contains("Gradle Wrapper 9.7.1")
                .contains("Spring Boot 4에서 반드시 이해할 변경")
                .contains("Jackson 3")
                .contains("spring-boot-starter-webmvc-test")
                .contains("ApplicationContext")
                .contains("DispatcherServlet")
                .contains("self-invocation")
                .contains("워크북 요구사항을 프로그램으로 만드는 기술")
                .contains("요구사항을 구현 카드로 바꾸기")
                .contains("수직 슬라이스")
                .contains("입력 검증과 비즈니스 검증을 분리하기")
                .contains("트랜잭션과 동시성을 별도로 설계하기")
                .contains("인증·인가·소유권을 각각 검증하기")
                .contains("완료 정의로 끝내기")
                .contains("폴더 준비 → Homebrew → Java 21 + Maven → JAR 빌드·직접 실행")
                .contains("launchd LaunchAgent 등록 → 파일 로그 확인 → DB 연결")
                .contains("업데이트·롤백 → 최종 점검")
                .contains("./gradlew bootJar")
                .contains("com.example.scm.plist")
                .contains("SCM_SEED_ENABLED=true")
                .contains("SPRING_PROFILES_ACTIVE=mysql")
                .contains("docker compose up -d --wait db")
                .contains("./gradlew mysqlSchemaTest")
                .contains("mysqldump")
                .contains("scm-system.jar.sha256")
                .contains("ROLLBACK_RELEASE")
                .contains("Flyway migration");
    }

    @Test
    @DisplayName("발주 핵심 정책이 워크북 전반에서 현재 참조 구현과 일치한다")
    void purchaseOrderDecisions_matchReferenceImplementation() throws IOException {
        String decisions = Files.readString(Path.of("practice", "DESIGN_DECISIONS.md"));
        String featureWorkbook = Files.readString(
                Path.of("practice", "feature-implementation-workbook.md"));
        String answers = Files.readString(Path.of("practice", "answers.md"));

        assertThat(decisions)
                .contains("{DRAFT, REQUESTED, APPROVED} → CANCELED")
                .contains("`ADMIN` 또는 `MANAGER`")
                .contains("재고 증가가 같은 트랜잭션");
        assertThat(featureWorkbook)
                .contains("본인 + DRAFT/REQUESTED/APPROVED")
                .contains("ADMIN/MANAGER, 상태 변경 + 재고 증가 원자성")
                .doesNotContain("본인 + DRAFT/REQUESTED 상태만");
        assertThat(answers)
                .contains("`DRAFT`, `REQUESTED`, `APPROVED`")
                .contains("승인·반려·입고는 `ADMIN` 또는 `MANAGER`");
    }

    @Test
    @DisplayName("HTML 학습 목차가 00~39 모듈을 정확히 한 번씩 추적한다")
    void htmlStudyGuide_tracksAllModules() throws IOException {
        Matcher matcher = HTML_MODULE.matcher(Files.readString(Path.of("index.html")));
        List<Integer> modules = new ArrayList<>();
        while (matcher.find()) {
            modules.add(Integer.parseInt(matcher.group(1)));
        }

        assertThat(modules).hasSize(EXPECTED_MODULES.size());
        assertThat(new TreeSet<>(modules)).isEqualTo(EXPECTED_MODULES);
    }

    @Test
    @DisplayName("HTML 학습 목차의 모든 로컬 파일 링크가 실제 대상을 가리킨다")
    void htmlStudyGuide_localLinksExist() throws IOException {
        Matcher matcher = HTML_LINK.matcher(Files.readString(Path.of("index.html")));
        while (matcher.find()) {
            String href = matcher.group(1);
            if (href.startsWith("#") || href.startsWith("http://")
                    || href.startsWith("https://") || href.startsWith("mailto:")) {
                continue;
            }

            String filePart = href.split("#", 2)[0];
            assertThat(Path.of(filePart))
                    .as("index.html 로컬 링크: %s", href)
                    .exists();
        }
    }

    private Set<Integer> moduleSections(Path path) throws IOException {
        Matcher matcher = SECTION_HEADING.matcher(Files.readString(path));
        Set<Integer> sections = new TreeSet<>();
        while (matcher.find()) {
            sections.add(Integer.parseInt(matcher.group(1)));
        }
        return sections;
    }

    private String read(Path path) {
        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new IllegalStateException("워크북 파일을 읽을 수 없습니다: " + path, e);
        }
    }
}
