FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app

COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle settings.gradle ./
COPY src/ ./src/

RUN chmod +x gradlew
RUN ./gradlew build -x test || true

RUN ./gradlew clean build -x test --stacktrace --no-daemon --warning-mode all

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
