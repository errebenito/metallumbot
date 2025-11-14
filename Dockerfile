FROM maven:3-eclipse-temurin-25@sha256:59df1b90e8047cba731164b3a12284a01860bc7dd9826f15f8b7ae981ef7b291 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests && cp /home/app/target/*.jar /home/app/app.jar

FROM eclipse-temurin:25@sha256:11d2909cab32d13eabaaa9b3bc2618a82c4749d19eb4dc2ad57eb268612a8cc7
COPY --from=build /home/app/app.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
