start cmd /k mvnw spring-boot:run -pl microstamp-service-registry

start cmd /k mvnw -T 5 spring-boot:run -pl microstamp-api-gateway,microstamp-authorization-server,microstamp-step1/microstamp-step1-microservice,microstamp-step2/microstamp-step2-microservice,microstamp-step3

cd microstamp-ui

npm i && npm run dev