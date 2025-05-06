FROM maven:3-eclipse-temurin-24@sha256:a13d3969087457f33a4e19db666b7f936e31734d7c1f08b6965afe9ebfc87840 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM eclipse-temurin:24@sha256:4563384c85adc9de147a84f68607023b399b10ab5bac23e30e022635dfbcedda
COPY --from=build /home/app/target/metallumbot-0.0.1.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
