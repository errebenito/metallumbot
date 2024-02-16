FROM maven:3.9.5-eclipse-temurin-21@sha256:2bbd188fef2384cf93181bb992b75542a2837afdd5c4c2274cebcae71cd05156 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM eclipse-temurin:21@sha256:807041737b0c8c8d462c6d0343458c04449bcf86809f184b3a22243244a659b3
COPY --from=build /home/app/target/metallumbot-0.0.1.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
