FROM maven:3-eclipse-temurin-23@sha256:6df31244c87c5107dd2c3f62f5634e5cb3cddedbb99b07cb8778284f576c2646 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM eclipse-temurin:24@sha256:bc2dc9715b2276d103827ff2d42a231bc0455be3bfac757f1a9185945333b0d9
COPY --from=build /home/app/target/metallumbot-0.0.1.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
