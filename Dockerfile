FROM maven:3-eclipse-temurin-24@sha256:ffc9c851d7352f5035ab041051b193af24ac4a486cd78c71a46afb96f3b1c8f7 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests && cp /home/app/target/*.jar /home/app/app.jar

FROM eclipse-temurin:24@sha256:d8100f03ed9fd3d1c7239b29e33f8ffe7b1268a70618d1d5074a30df202f4fd6
COPY --from=build /home/app/app.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
