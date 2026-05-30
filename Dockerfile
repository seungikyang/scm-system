# syntax=docker/dockerfile:1

# =========================================================================
# Build stage — JDK 17 + Gradle 8.5 이미지에서 빌드.
# 로컬에 JDK 17 / Gradle / gradle-wrapper.jar 가 없어도 컨테이너 안에서 빌드된다.
# =========================================================================
FROM gradle:8.5-jdk17 AS build
WORKDIR /workspace

# 1) 의존성 레이어 캐시: 빌드 스크립트만 먼저 복사해 의존성 워밍업
COPY settings.gradle build.gradle ./
RUN gradle dependencies --no-daemon > /dev/null 2>&1 || true

# 2) 소스 복사 후 실행 가능 jar 빌드 (테스트는 스킵 — 필요 시 -x test 제거)
COPY src ./src
RUN gradle bootJar --no-daemon -x test

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
