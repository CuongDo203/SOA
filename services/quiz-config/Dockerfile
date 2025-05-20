# Stage 1: use maven for building
FROM maven:3.8.5-openjdk-17-slim as build
WORKDIR /app
COPY . ./quiz-config
WORKDIR /app/quiz-config
RUN mvn clean package -DskipTests

# Stage 2: use OpenJDK for running
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/quiz-config/target/*.jar quiz-config.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar", "quiz-config.jar"]