# Stage 1: use maven for building
FROM maven:3.8.5-openjdk-17-slim as build
WORKDIR /app
COPY . ./quiz-service
WORKDIR /app/quiz-service
RUN mvn clean package -DskipTests

# Stage 2: use OpenJDK for running
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/quiz-service/target/*.jar quiz-service.jar
EXPOSE 9005
ENTRYPOINT ["java","-jar", "quiz-service.jar"]