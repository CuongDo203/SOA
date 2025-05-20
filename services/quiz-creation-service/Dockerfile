# Stage 1: use maven for building
FROM maven:3.8.5-openjdk-17-slim as build
WORKDIR /app
COPY . ./quiz-creation-service
WORKDIR /app/quiz-creation-service
RUN mvn clean package -DskipTests

# Stage 2: use OpenJDK for running
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/quiz-creation-service/target/*.jar quiz-creation-service.jar
EXPOSE 9000
ENTRYPOINT ["java","-jar", "quiz-creation-service.jar"]
