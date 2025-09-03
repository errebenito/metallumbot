FROM maven:3-eclipse-temurin-24@sha256:db74cfbb321f7c1c0ffa735d188dc4215a279e8f677fd62aed4a955606face85 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests && cp /home/app/target/*.jar /home/app/app.jar

FROM eclipse-temurin:24@sha256:58b370f13243c751854e8a52630a57468e906717050187e29addbaba28a170fb
COPY --from=build /home/app/app.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
