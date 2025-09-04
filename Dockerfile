FROM maven:3-eclipse-temurin-24@sha256:947042085934ad4c6ac8a1252c076c7fbe2401a0ee0dcd939c05cf23c583a1c7 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests && cp /home/app/target/*.jar /home/app/app.jar

FROM eclipse-temurin:24@sha256:b881f21feee2a3b76442b13662d6c70c9c33e5453b6bd0aac415a17973fb368b
COPY --from=build /home/app/app.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
