FROM maven:3.9.6-eclipse-temurin-22@sha256:6999511d285ea595b0eb59dc0514dfad50534118ba400a40138a15653b54e44c AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM eclipse-temurin:21@sha256:2dbc9d6e45e878ef98d0d918e8d591ddfc3ae1a73a27d76d743affc93a2c13d1
COPY --from=build /home/app/target/metallumbot-0.0.1.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
