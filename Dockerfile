FROM maven:3-eclipse-temurin-25@sha256:3d35095b456f61758a4b3f62c746bffd369d5cabaf6c04ea4387a96dc6640972 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests && cp /home/app/target/*.jar /home/app/app.jar

FROM eclipse-temurin:25@sha256:3a35a62d3ece346cfde11b08fafdc3f99d3946d31d5ebc15d678b44a67c3b9ec
COPY --from=build /home/app/app.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
