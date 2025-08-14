FROM maven:3-eclipse-temurin-24@sha256:a0e710861a83d7d38642538e8a3c4780f6b57662e7d94b0db632581141015ad7 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests && cp /home/app/target/*.jar /home/app/app.jar

FROM eclipse-temurin:24@sha256:12ded611adf81135d72a57ab0c1837178e17fb6ccdecb88f5e3b85e5c45c078c
COPY --from=build /home/app/app.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
