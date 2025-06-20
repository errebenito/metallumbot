FROM maven:3-eclipse-temurin-24@sha256:e15fadb0fd97a07bb5c529ec8a78ec39c7cc5048dea50dfaebc5f5e55a0fed94 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests && cp /home/app/target/*.jar /home/app/app.jar

FROM eclipse-temurin:24@sha256:26e6e80ea0f6372b08a42fae267682a41e49c9e68b7a8fa6a682e9e7b0433a11
COPY --from=build /home/app/app.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
