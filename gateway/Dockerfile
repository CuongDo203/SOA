# Stage 1: use maven for building
FROM maven:3.8.5-openjdk-17-slim as build
WORKDIR /app
COPY . ./gateway
WORKDIR /app/gateway
RUN mvn clean package -DskipTests

# Stage 2: use OpenJDK for running
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/gateway/target/*.jar gateway.jar
EXPOSE 8888
ENTRYPOINT ["java","-jar", "gateway.jar"]