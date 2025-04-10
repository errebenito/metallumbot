FROM maven:3-eclipse-temurin-24@sha256:6bf2a11df114fe600c8a10dfe9d41d8890388ac31fa5f0bd6f6fade9a09b49d4 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM eclipse-temurin:24@sha256:b7dd43fc21ac75bab46451f1bf111935d7d13f4109f6463356242fbe1ed00aed
COPY --from=build /home/app/target/metallumbot-0.0.1.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
