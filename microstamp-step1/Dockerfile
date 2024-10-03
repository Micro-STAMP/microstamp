# Etapa de build usando Maven e OpenJDK 21
FROM openjdk:21-jdk-slim AS build

WORKDIR /app

COPY pom.xml ./
COPY microstamp-step1-data/pom.xml ./microstamp-step1-data/
COPY microstamp-step1-dto/pom.xml ./microstamp-step1-dto/
COPY microstamp-step1-microservice/pom.xml ./microstamp-step1-microservice/

COPY ./microstamp-step1-data ./microstamp-step1-data/
COPY ./microstamp-step1-dto ./microstamp-step1-dto/
COPY ./microstamp-step1-microservice ./microstamp-step1-microservice/

COPY mvnw ./
COPY .mvn ./.mvn

RUN chmod +x mvnw
RUN sed -i 's/\r$//' mvnw
RUN ./mvnw clean package -DskipTests

FROM openjdk:21-jdk
WORKDIR /app
COPY --from=build /app/microstamp-step1-microservice/target/*.jar app.jar
EXPOSE 8091

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
