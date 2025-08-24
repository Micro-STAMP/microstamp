FROM openjdk:21-jdk-slim AS build

WORKDIR /app

COPY pom.xml ./
COPY microstamp-step4-data/pom.xml ./microstamp-step4-data/
COPY microstamp-step4-dto/pom.xml ./microstamp-step4-dto/
COPY microstamp-step4-microservice/pom.xml ./microstamp-step4-microservice/

COPY ./microstamp-step4-data ./microstamp-step4-data/
COPY ./microstamp-step4-dto ./microstamp-step4-dto/
COPY ./microstamp-step4-microservice ./microstamp-step4-microservice/

COPY mvnw ./
COPY .mvn ./.mvn

RUN chmod +x mvnw
RUN sed -i 's/\r$//' mvnw
RUN ./mvnw clean package -DskipTests

FROM openjdk:21-jdk
WORKDIR /app
COPY --from=build /app/microstamp-step4-microservice/target/*.jar app.jar
EXPOSE 8104

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
