FROM maven:3-eclipse-temurin-25@sha256:069d20bee5d7bf7bb3788e6c223cb78d7e521a4697568afcc49b0d371bec6d71 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests && cp /home/app/target/*.jar /home/app/app.jar

FROM eclipse-temurin:25@sha256:c39999d5331eacc2ca5d0e571cdf841e5c18cdd195762e7b0a05bc6fa7c85a96
COPY --from=build /home/app/app.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
