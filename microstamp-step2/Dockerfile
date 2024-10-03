# Etapa de build usando Maven e OpenJDK 21
FROM openjdk:21-jdk-slim AS build

WORKDIR /app

COPY pom.xml ./
COPY microstamp-step2-data/pom.xml ./microstamp-step2-data/
COPY microstamp-step2-dto/pom.xml ./microstamp-step2-dto/
COPY microstamp-step2-microservice/pom.xml ./microstamp-step2-microservice/

COPY ./microstamp-step2-data ./microstamp-step2-data/
COPY ./microstamp-step2-dto ./microstamp-step2-dto/
COPY ./microstamp-step2-microservice ./microstamp-step2-microservice/

COPY mvnw ./
COPY .mvn ./.mvn

RUN chmod +x mvnw
RUN sed -i 's/\r$//' mvnw
RUN ./mvnw clean package -DskipTests

FROM openjdk:21-jdk
WORKDIR /app
COPY --from=build /app/microstamp-step2-microservice/target/*.jar app.jar
EXPOSE 8090

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
