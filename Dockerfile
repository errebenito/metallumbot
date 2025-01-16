FROM maven:3.9.9-eclipse-temurin-23@sha256:323693fcac06d3c25f430032884273307a5c37663e7d54506b5096047b380cdd AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM eclipse-temurin:23@sha256:eb69195844ddf4a69d68d19b2bea1e6b0ef2865f5475243f3a1fb132a474601d
COPY --from=build /home/app/target/metallumbot-0.0.1.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
