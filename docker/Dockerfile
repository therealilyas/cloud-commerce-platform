# syntax=docker/dockerfile:1.7
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /workspace
ARG SERVICE
COPY . .
RUN chmod +x mvnw && ./mvnw -B -ntp -pl "${SERVICE}" -am package -DskipTests

FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
WORKDIR /app
ARG SERVICE
COPY --from=build "/workspace/${SERVICE}/target/${SERVICE}-0.1.0-SNAPSHOT.jar" app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar"]
