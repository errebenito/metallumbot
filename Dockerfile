FROM maven:3-eclipse-temurin-24@sha256:354cd43a14395656e15a7505f7abe2d9aca57757e236917f5d7d5d1a8b7b8828 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests && cp /home/app/target/*.jar /home/app/app.jar

FROM eclipse-temurin:24@sha256:12ded611adf81135d72a57ab0c1837178e17fb6ccdecb88f5e3b85e5c45c078c
COPY --from=build /home/app/app.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
