FROM maven:3.9-eclipse-temurin-8@sha256:e258ca43360980e4e3f0a349292793b9e099d1fe3f52e6ffadd4f8349e8ccbd8 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM eclipse-temurin:21@sha256:4c39387727544fbaf021cebfe21b984b9724810f90e4bd86b9aa19c7bbbe2294
COPY --from=build /home/app/target/metallumbot-0.0.1.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
