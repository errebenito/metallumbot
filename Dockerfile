FROM maven:3-eclipse-temurin-23@sha256:6df31244c87c5107dd2c3f62f5634e5cb3cddedbb99b07cb8778284f576c2646 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM eclipse-temurin:23@sha256:c4cdba05514ac169c6c4f7545368747c5bb8a447c87c2c2dea60adcd082f27ba
COPY --from=build /home/app/target/metallumbot-0.0.1.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
