FROM maven:3-eclipse-temurin-24@sha256:312fa2399c102b3cb171e1467b87f6cb8aac647c2b8ee63f1eb6ffefc0374b04 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests && cp /home/app/target/*.jar /home/app/app.jar

FROM eclipse-temurin:24@sha256:f5ce146dcdee1dc1b234f2f3a5baed0143f6fea2a32141d77a4e5656546f814d
COPY --from=build /home/app/app.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
