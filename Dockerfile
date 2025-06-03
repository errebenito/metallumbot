FROM maven:3-eclipse-temurin-24@sha256:a13d3969087457f33a4e19db666b7f936e31734d7c1f08b6965afe9ebfc87840 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests && cp /home/app/target/*.jar /home/app/app.jar

FROM eclipse-temurin:24@sha256:2ea5b846a3f41e46a1ad3110f5a023482adcc4f5cae79bc7b082a12e6f756dd9
COPY --from=build /home/app/app.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
