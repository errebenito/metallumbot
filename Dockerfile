FROM --platform=linux/amd64 maven:3-eclipse-temurin-24@sha256:a13d3969087457f33a4e19db666b7f936e31734d7c1f08b6965afe9ebfc87840 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests && cp /home/app/target/*.jar /home/app/app.jar

FROM --platform=linux/amd64 eclipse-temurin:24@sha256:24459ad9b750de00da0d7aacc9ac2caf2320349663283cc53571df65f934789d
COPY --from=build /home/app/app.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
