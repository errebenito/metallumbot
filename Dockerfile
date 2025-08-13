FROM maven:3-eclipse-temurin-24@sha256:354cd43a14395656e15a7505f7abe2d9aca57757e236917f5d7d5d1a8b7b8828 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests && cp /home/app/target/*.jar /home/app/app.jar

FROM eclipse-temurin:24@sha256:f7ebd9bfc1093b25a4526ca818e00c4be14140c54b34289d0e86025ec1f7eaec
COPY --from=build /home/app/app.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
