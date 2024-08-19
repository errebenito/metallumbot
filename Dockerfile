FROM maven:3.9-eclipse-temurin-8@sha256:ef780f7a7ae080cd281d498bd95a6cbe09799100fc93f29d0c7880c1caf5bb26 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM eclipse-temurin:21@sha256:a957683d27491213d9d68ef461aa9fae0183aedaf13c661758dc8b8d37bdb5e6
COPY --from=build /home/app/target/metallumbot-0.0.1.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
