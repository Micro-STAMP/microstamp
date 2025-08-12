FROM openjdk:21-jdk-slim AS build

WORKDIR /app

COPY pom.xml ./
COPY microstamp-step4-new-data/pom.xml ./microstamp-step4-new-data/
COPY microstamp-step4-new-dto/pom.xml ./microstamp-step4-new-dto/
COPY microstamp-step4-new-microservice/pom.xml ./microstamp-step4-new-microservice/

COPY ./microstamp-step4-new-data ./microstamp-step4-new-data/
COPY ./microstamp-step4-new-dto ./microstamp-step4-new-dto/
COPY ./microstamp-step4-new-microservice ./microstamp-step4-new-microservice/

COPY mvnw ./
COPY .mvn ./.mvn

RUN chmod +x mvnw
RUN sed -i 's/\r$//' mvnw
RUN ./mvnw clean package -DskipTests

FROM openjdk:21-jdk
WORKDIR /app
COPY --from=build /app/microstamp-step4-new-microservice/target/*.jar app.jar
EXPOSE 8084

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
