FROM maven:3-eclipse-temurin-24@sha256:b4c8715547caf969fa75600d42c7a0521bcc0c12d7251a48cc2052a0bcf96f7b AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests && cp /home/app/target/*.jar /home/app/app.jar

FROM eclipse-temurin:24@sha256:ce014b65ae578a2dc7009d15cb4fe9bf160103be265112c3590c95ffdd9002ce
COPY --from=build /home/app/app.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
