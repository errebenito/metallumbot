FROM maven:3-eclipse-temurin-24@sha256:4ef311e03b532ad891aaaf01d2310b0c881d63dd627d7fef8acec1d7cf4ed314 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests && cp /home/app/target/*.jar /home/app/app.jar

FROM eclipse-temurin:24@sha256:e6e42934acd34e08fb93fa0815e8fb952ae21a9c0214005241dfaca7fde6b1a5
COPY --from=build /home/app/app.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
