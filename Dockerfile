FROM maven:3-eclipse-temurin-24@sha256:211a2781febdefb97d3c3aa2a44f2e12264dea1714a627ab11220788babf0101 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests && cp /home/app/target/*.jar /home/app/app.jar

FROM eclipse-temurin:24@sha256:0dacf7d90f5adf2e16a07b3cd06b5574d258acd4e5f215a5e2a9f1ad4c18fe6f
COPY --from=build /home/app/app.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
