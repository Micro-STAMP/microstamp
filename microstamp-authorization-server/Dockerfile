# Etapa de build usando Maven e OpenJDK 21
FROM openjdk:21-jdk-slim AS build

WORKDIR /app
COPY pom.xml ./
COPY src ./src

COPY mvnw ./
COPY .mvn ./.mvn

RUN chmod +x mvnw
RUN sed -i 's/\r$//' mvnw
RUN ./mvnw clean package -DskipTests

FROM openjdk:21-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8001

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
