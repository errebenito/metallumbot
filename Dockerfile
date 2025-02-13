FROM maven:3-eclipse-temurin-23@sha256:6df31244c87c5107dd2c3f62f5634e5cb3cddedbb99b07cb8778284f576c2646 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM eclipse-temurin:23@sha256:d6a2addf734d4d748b7a67e1cf18a3ef5032a1678b919973959ba2a891222444
COPY --from=build /home/app/target/metallumbot-0.0.1.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
