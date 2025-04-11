FROM maven:3-eclipse-temurin-24@sha256:6bf2a11df114fe600c8a10dfe9d41d8890388ac31fa5f0bd6f6fade9a09b49d4 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM eclipse-temurin:24@sha256:6faba772a142333e91f3df7fd9c14393e85b2a01289c03ef03c2df69be06f9ce
COPY --from=build /home/app/target/metallumbot-0.0.1.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
