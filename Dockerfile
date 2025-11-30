FROM maven:3.9.11-eclipse-temurin-21 as build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/websocket_demo-0.0.1-SNAPSHOT.jar websocket_demo.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "websocket_demo.jar"]
