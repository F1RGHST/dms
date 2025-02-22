FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/storage-0.0.1.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
