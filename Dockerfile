FROM maven:3-eclipse-temurin-23@sha256:0d61a0f5bfb22f3cc6b22132e273e9aea73c11d7ad2dfbf71485d776242a87f3 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM eclipse-temurin:23@sha256:88c3219e82c0fe5cd732eb60e552a54ffccf5f16e9295dbff2182d3df011d4e0
COPY --from=build /home/app/target/metallumbot-0.0.1.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
