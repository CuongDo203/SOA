# Stage 1: use maven for building
FROM maven:3.8.5-openjdk-17-slim as build
WORKDIR /app
COPY . ./student-service
WORKDIR /app/student-service
RUN mvn clean package -DskipTests

# Stage 2: use OpenJDK for running
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/student-service/target/*.jar student-service.jar
EXPOSE 9001
ENTRYPOINT ["java","-jar", "student-service.jar"]