FROM maven:3-eclipse-temurin-24@sha256:71d01d5ff1f1181c30d35e385ffb2817c83bee09e43051d1d132d05841fedaec AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests && cp /home/app/target/*.jar /home/app/app.jar

FROM eclipse-temurin:24@sha256:ce014b65ae578a2dc7009d15cb4fe9bf160103be265112c3590c95ffdd9002ce
COPY --from=build /home/app/app.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
