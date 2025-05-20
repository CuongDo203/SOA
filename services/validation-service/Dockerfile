# Stage 1: use maven for building
FROM maven:3.8.5-openjdk-17-slim as build
WORKDIR /app
COPY . ./validation-service
WORKDIR /app/validation-service
RUN mvn clean package -DskipTests

# Stage 2: use OpenJDK for running
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/validation-service/target/*.jar validation-service.jar
EXPOSE 8091
ENTRYPOINT ["java","-jar", "validation-service.jar"]