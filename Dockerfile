# 1단계: Build with Gradle
FROM eclipse-temurin:17-jdk-jammy AS build

WORKDIR /app

# gradle 관련 파일만 먼저 복사
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle settings.gradle ./

# 실행 권한 부여
RUN chmod +x gradlew

# 의존성 캐시를 위해 먼저 빌드 (실패해도 통과)
RUN ./gradlew build -x test || true

# 나머지 소스 전체 복사
COPY . .

# 최종 빌드
RUN ./gradlew clean build -x test --stacktrace --info

# 2단계: Run
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
