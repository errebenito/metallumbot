FROM maven:3-eclipse-temurin-24@sha256:ffc9c851d7352f5035ab041051b193af24ac4a486cd78c71a46afb96f3b1c8f7 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests && cp /home/app/target/*.jar /home/app/app.jar

FROM eclipse-temurin:24@sha256:26e6e80ea0f6372b08a42fae267682a41e49c9e68b7a8fa6a682e9e7b0433a11
COPY --from=build /home/app/app.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
