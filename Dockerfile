FROM maven:3.9-eclipse-temurin-8@sha256:ef780f7a7ae080cd281d498bd95a6cbe09799100fc93f29d0c7880c1caf5bb26 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM eclipse-temurin:21@sha256:1b04dc7cd430939b9444293463363201011379364c4176746a771959f27d90bd
COPY --from=build /home/app/target/metallumbot-0.0.1.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
