FROM maven:3-eclipse-temurin-25@sha256:7182e2ce1934e55f801c6c32c0a85184439d9d2de67847f7a06c3c5055a628b4 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests && cp /home/app/target/*.jar /home/app/app.jar

FROM eclipse-temurin:25@sha256:954b6f1a5610dd42d4ff220be2d928aed6e314b8a240590c5c4bbeee2dcde121
COPY --from=build /home/app/app.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
