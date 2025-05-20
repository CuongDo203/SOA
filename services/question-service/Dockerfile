# Stage 1: use maven for building
FROM maven:3.8.5-openjdk-17-slim as build
WORKDIR /app
COPY . ./question-service
WORKDIR /app/question-service
RUN mvn clean package -DskipTests

# Stage 2: use OpenJDK for running
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/question-service/target/*.jar question-service.jar
EXPOSE 9004
ENTRYPOINT ["java","-jar", "question-service.jar"]