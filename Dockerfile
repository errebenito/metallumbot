FROM maven:3.9.8-eclipse-temurin-22@sha256:e5bd7f0b304a72b5dcf8845834d815e13cacae497699f95928cfd112147393f1 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM eclipse-temurin:21@sha256:30cda4693fbb923f2145a0a4e23d03e75b0477df5daccd12049e372e4337aa59
COPY --from=build /home/app/target/metallumbot-0.0.1.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
