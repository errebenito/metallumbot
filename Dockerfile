FROM maven:3-eclipse-temurin-24@sha256:a137a467ec89b5713d0be817b55bdba6b4d6ef16e3d05565a79bc08d8e775a1c AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests && cp /home/app/target/*.jar /home/app/app.jar

FROM eclipse-temurin:25@sha256:284a5db779923fde52120978df1dc7596ddc6a0a8a46d214465e6c0fa9f40df2
COPY --from=build /home/app/app.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
