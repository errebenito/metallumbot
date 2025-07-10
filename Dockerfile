FROM maven:3-eclipse-temurin-24@sha256:5646b07013676a8bca32d92b25aa6454bed6e70177b342b29d35f9d838c1ccda AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests && cp /home/app/target/*.jar /home/app/app.jar

FROM eclipse-temurin:24@sha256:2636260b048fae28e5af5aa88fa8184d4ea78e65e2e69e9c1066e0a24d3da7d4
COPY --from=build /home/app/app.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
