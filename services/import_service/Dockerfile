# Stage 1: use maven for building
FROM maven:3.8.5-openjdk-17-slim as build
WORKDIR /app
COPY . ./import_service
WORKDIR /app/import_service
RUN mvn clean package -DskipTests

# Stage 2: use OpenJDK for running
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/import_service/target/*.jar import-service.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar", "import-service.jar"]