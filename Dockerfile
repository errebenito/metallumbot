FROM maven:3-eclipse-temurin-25@sha256:1736dd5300fd49322a4059b0c2b7791f7a4c0c25916efbbef3d6b92a3a7beea4 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests && cp /home/app/target/*.jar /home/app/app.jar

FROM eclipse-temurin:25@sha256:bdccc9761169b39da130bf829e6c30206eda16f2a87d86bd6774a0b2a5f8d33c
COPY --from=build /home/app/app.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
