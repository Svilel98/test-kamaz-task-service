FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/task-service-*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=deploy", "app.jar"]