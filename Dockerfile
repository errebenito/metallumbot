FROM maven:3.9-eclipse-temurin-8@sha256:2a4211e92187358a4c4b7442da7d9e7a0fc4b11c3ffd912033548b49dc5ab122 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM eclipse-temurin:21@sha256:3f9bfce63186b9ded168250c8e350631fd643ad00afab5986cf8a7cf79f3b043
COPY --from=build /home/app/target/metallumbot-0.0.1.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
