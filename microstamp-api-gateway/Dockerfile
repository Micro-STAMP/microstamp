# Etapa de build usando Maven e OpenJDK 21
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app
COPY pom.xml ./
COPY src ./src

COPY mvnw ./
COPY .mvn ./.mvn

RUN chmod +x mvnw
RUN sed -i 's/\r$//' mvnw
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8000

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
