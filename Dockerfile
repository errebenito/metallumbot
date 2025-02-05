FROM maven:3-eclipse-temurin-23@sha256:4b73cb55de6b4db26aff9c43abfda836aac2fc3ac009ac3f0b9412557fa27498 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM eclipse-temurin:23@sha256:88c3219e82c0fe5cd732eb60e552a54ffccf5f16e9295dbff2182d3df011d4e0
COPY --from=build /home/app/target/metallumbot-0.0.1.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
