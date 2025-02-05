FROM maven:3-eclipse-temurin-23@sha256:4b73cb55de6b4db26aff9c43abfda836aac2fc3ac009ac3f0b9412557fa27498 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM eclipse-temurin:23@sha256:447add45ed9d3afda106cc30ac1a56bf2884200f379401d689d11f596d1b844a
COPY --from=build /home/app/target/metallumbot-0.0.1.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
