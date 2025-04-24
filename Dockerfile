FROM maven:3-eclipse-temurin-24@sha256:3ffc3d9cb2b7c14e09768641b15b1eac54fee1226c63de704263db0212030e7f AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM eclipse-temurin:24@sha256:e24c562541702563d1bcd91fb38a86737be0f0e8dc4919b03c626d53f23d327d
COPY --from=build /home/app/target/metallumbot-0.0.1.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
