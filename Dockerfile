FROM maven:3.9.5-eclipse-temurin-21@sha256:2bbd188fef2384cf93181bb992b75542a2837afdd5c4c2274cebcae71cd05156 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM eclipse-temurin:21@sha256:3e7d577a0977d784d9bef0a0d000ea4e7dba6c0dfa7dcfc3babe53ac721b4222
COPY --from=build /home/app/target/metallumbot-0.0.1.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
