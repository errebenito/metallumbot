FROM maven:3.9-eclipse-temurin-8@sha256:2a4211e92187358a4c4b7442da7d9e7a0fc4b11c3ffd912033548b49dc5ab122 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM eclipse-temurin:21@sha256:2dbc9d6e45e878ef98d0d918e8d591ddfc3ae1a73a27d76d743affc93a2c13d1
COPY --from=build /home/app/target/metallumbot-0.0.1.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
