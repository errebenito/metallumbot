FROM maven:3-eclipse-temurin-23@sha256:501cfb5619a609f5184c4c3298bf8d9e536830607e4f4d9042f22f7263a4a543 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM eclipse-temurin:23@sha256:03e625846505889c773808dab9325090827d0c7782f81c5c20ab5a9fe31398a9
COPY --from=build /home/app/target/metallumbot-0.0.1.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
