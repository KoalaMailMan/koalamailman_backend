# build
FROM gradle:8.8-jdk17 AS builder
WORKDIR /src
COPY . .
RUN gradle clean bootJar -x test

# run
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /src/build/libs/*-SNAPSHOT.jar app.jar
ENV TZ=Asia/Seoul JAVA_OPTS="-Dspring.profiles.active=prod" SERVER_PORT=8080
EXPOSE 8080
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
