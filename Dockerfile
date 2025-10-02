FROM maven:3-eclipse-temurin-25@sha256:b09452ce820b05ec8caac67bdc0492ad6a5bfc8237469cb3ac12043ef70ecba1 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests && cp /home/app/target/*.jar /home/app/app.jar

FROM eclipse-temurin:25@sha256:21f6c51087c4fa7775b802c9d2ca7e3319eedf32e95ee94cac44a6d0f543a83f
COPY --from=build /home/app/app.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
