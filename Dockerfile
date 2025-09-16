FROM maven:3-eclipse-temurin-24@sha256:b4c8715547caf969fa75600d42c7a0521bcc0c12d7251a48cc2052a0bcf96f7b AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests && cp /home/app/target/*.jar /home/app/app.jar

FROM eclipse-temurin:24@sha256:4db3878ad4059c6bdc6c24ad64de8138ceab7f94abefea4e5466667fc9df2ae5
COPY --from=build /home/app/app.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
