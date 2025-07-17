FROM maven:3-eclipse-temurin-24@sha256:211a2781febdefb97d3c3aa2a44f2e12264dea1714a627ab11220788babf0101 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests && cp /home/app/target/*.jar /home/app/app.jar

FROM eclipse-temurin:24@sha256:e6e42934acd34e08fb93fa0815e8fb952ae21a9c0214005241dfaca7fde6b1a5
COPY --from=build /home/app/app.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
