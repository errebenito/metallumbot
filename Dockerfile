FROM maven:3.9-eclipse-temurin-8@sha256:e258ca43360980e4e3f0a349292793b9e099d1fe3f52e6ffadd4f8349e8ccbd8 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM eclipse-temurin:21@sha256:a957683d27491213d9d68ef461aa9fae0183aedaf13c661758dc8b8d37bdb5e6
COPY --from=build /home/app/target/metallumbot-0.0.1.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
