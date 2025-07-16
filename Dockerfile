FROM maven:3-eclipse-temurin-24@sha256:5646b07013676a8bca32d92b25aa6454bed6e70177b342b29d35f9d838c1ccda AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests && cp /home/app/target/*.jar /home/app/app.jar

FROM eclipse-temurin:24@sha256:0dacf7d90f5adf2e16a07b3cd06b5574d258acd4e5f215a5e2a9f1ad4c18fe6f
COPY --from=build /home/app/app.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
