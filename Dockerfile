FROM openjdk:17-jdk-slim
LABEL authors="amine"
WORKDIR /app
COPY target/votely_backend.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
