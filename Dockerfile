FROM maven:3.9-eclipse-temurin-8@sha256:d2a7f2a3c90e62dec9da7949328256022be13c503b2e9ffcc69cfd44252cd698 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM eclipse-temurin:21@sha256:b18dabf509aeeb700d9525cdebf6bdbededb06536c6e233a3a21e6fb04d2be8c
COPY --from=build /home/app/target/metallumbot-0.0.1.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
