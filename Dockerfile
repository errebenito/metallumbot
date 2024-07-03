FROM maven:3.9-eclipse-temurin-8@sha256:1dd37f0daa27e6047a8e372912dbe0fc035cba3d57e7fb5745fed9f551052513 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM eclipse-temurin:21@sha256:3f9bfce63186b9ded168250c8e350631fd643ad00afab5986cf8a7cf79f3b043
COPY --from=build /home/app/target/metallumbot-0.0.1.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
