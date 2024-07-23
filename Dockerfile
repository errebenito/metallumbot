FROM maven:3.9-eclipse-temurin-8@sha256:dc0b58305a83c88f26af7bdd9ae87d45b5416c99e893a1a6745a736bc5adf646 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM eclipse-temurin:21@sha256:4c39387727544fbaf021cebfe21b984b9724810f90e4bd86b9aa19c7bbbe2294
COPY --from=build /home/app/target/metallumbot-0.0.1.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
