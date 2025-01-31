FROM maven:3-eclipse-temurin-23@sha256:16ca9394c92809a7d0725c360274c26adab3d8dd60b2f26769b3eae7c12355ca AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM eclipse-temurin:23@sha256:62250fb1bd2b41c97a688973cd7c08fe5fe2f8c7b5fb2ee4dc22f009870476b5
COPY --from=build /home/app/target/metallumbot-0.0.1.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
