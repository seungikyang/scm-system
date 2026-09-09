# syntax=docker/dockerfile:1

# =========================================================================
# Build stage — JDK 17 + Gradle 9.7.1 이미지에서 빌드.
# 로컬에 JDK 17 / Gradle / gradle-wrapper.jar 가 없어도 컨테이너 안에서 빌드된다.
# =========================================================================
FROM gradle:9.7.1-jdk17-noble AS build
WORKDIR /workspace

# 1) 의존성 레이어 캐시: 빌드 스크립트만 먼저 복사해 의존성 워밍업
COPY settings.gradle build.gradle ./
RUN gradle dependencies --no-daemon > /dev/null 2>&1 || true

# 2) 제품 코드와 워크북을 복사해 구조·기능 테스트 후 실행 가능 jar 빌드
COPY src ./src
COPY practice ./practice
COPY index.html ./index.html
COPY docs ./docs
# HTML 목차의 로컬 링크를 검사하는 테스트에 필요한 루트 문서도 함께 제공한다.
COPY README.md .gitignore history.html scm_system_PRD_TRD.md ./
# 워크북 테스트가 읽는 버전 계약 파일도 동일한 경로로 제공한다.
COPY gradle/wrapper/gradle-wrapper.properties ./gradle/wrapper/gradle-wrapper.properties
# .git이 없는 이미지에서는 테스트와 JAR 빌드를 실행한다.
# Git 추적 경계 검사는 저장소 루트의 portfolioCheck 및 CI verify 단계에서 수행한다.
RUN gradle test bootJar --no-daemon

# =========================================================================
# Runtime stage — 슬림 JRE 17 로 실행.
# =========================================================================
FROM eclipse-temurin:17-jre AS runtime
WORKDIR /app
ENV TZ=Asia/Seoul \
    JAVA_OPTS=""

# build 스테이지에서 만든 bootJar 하나만 복사 (plain jar 는 build.gradle 에서 비활성)
COPY --from=build /workspace/build/libs/*.jar app.jar

EXPOSE 8080

# SPRING_PROFILES_ACTIVE 등 환경변수로 프로필/DB 주입 (기본: H2 in-memory)
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
