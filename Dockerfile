FROM maven:3-eclipse-temurin-24@sha256:2c10549695075f05694ee1e62b9e66acf377095f6c32f435daf2ed2c3cb187eb AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM eclipse-temurin:24@sha256:feb94a726b3a921ab7c3c07ba0a7ff8a278c4e1708b1da159ff5a06a520d6cab
COPY --from=build /home/app/target/metallumbot-0.0.1.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
