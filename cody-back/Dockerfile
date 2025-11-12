# =================================
# 1단계: Build Stage
# =================================
FROM gradle:8.10-jdk21 AS builder

WORKDIR /app

# Gradle Wrapper 파일 복사
COPY gradlew .
COPY gradle gradle

# Gradle 캐시 활용을 위해 의존성 관련 파일 먼저 복사
COPY build.gradle settings.gradle ./

# 의존성 다운로드 (캐시 레이어 생성)
RUN gradle dependencies --no-daemon || return 0

# 전체 소스 복사
COPY src src

# 애플리케이션 빌드 (테스트 제외)
RUN gradle clean build -x test --no-daemon

# JAR 파일명 확인 및 정리
RUN ls -la /app/build/libs/

# =================================
# 2단계: Runtime Stage
# =================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# 비root 유저 생성 (보안 강화)
RUN addgroup -S spring && adduser -S spring -G spring

# 빌드 결과물 복사
COPY --from=builder /app/build/libs/cody-wardrobe-*.jar app.jar

# 소유권 변경
RUN chown spring:spring app.jar

# 비root 유저로 전환
USER spring:spring

# 환경 변수 설정
ENV SPRING_PROFILES_ACTIVE=prod \
    JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC"

# 8080 포트 오픈
EXPOSE 8080

# Health check 추가
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# 실행 명령어
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]

