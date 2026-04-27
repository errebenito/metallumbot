FROM maven:3-eclipse-temurin-26@sha256:2afabfe1611261e4cc04d49c7819309c07653997b78157f44d86acc200a414f1 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM eclipse-temurin:25@sha256:73e5bce2b6ff85ea4539923d017d56e5239a10f3cbb29a6fe8125595f2a01f79
COPY --from=build /home/app/target/metallumbot-1.0.0.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
