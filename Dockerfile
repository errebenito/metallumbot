FROM maven:3-eclipse-temurin-24@sha256:3ffc3d9cb2b7c14e09768641b15b1eac54fee1226c63de704263db0212030e7f AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM eclipse-temurin:24@sha256:0907c8846ee238198f94a5679475858165a248823c691f947a27bc08f105e177
COPY --from=build /home/app/target/metallumbot-0.0.1.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
