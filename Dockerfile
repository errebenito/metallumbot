FROM maven:3-eclipse-temurin-24@sha256:6bf2a11df114fe600c8a10dfe9d41d8890388ac31fa5f0bd6f6fade9a09b49d4 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM eclipse-temurin:24@sha256:e24c562541702563d1bcd91fb38a86737be0f0e8dc4919b03c626d53f23d327d
COPY --from=build /home/app/target/metallumbot-0.0.1.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
