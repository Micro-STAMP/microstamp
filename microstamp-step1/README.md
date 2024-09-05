# MicroSTAMP Step 1

MicroSTAMP Step 1 - a Spring Boot microservice for Step 1 of STPA.

## Requirements

To build and run the application, you'll need:

- [JDK 21](https://www.oracle.com/java/technologies/downloads/#java21)
- [MySQL 8.0](https://dev.mysql.com/downloads/mysql) or higher

## Steps to Execute

1. **Clone the Project**: Clone this repository to your local machine.
2. **Configure `application.properties`**: Set up your SQL configurations in the `application.properties` file located
   in the `microstamp-step1-microservice/src/main/resources` directory. By default, it assumes port 3306 and "step1" as
   the database name. Ensure to provide the correct URL, port, username, and password for your SQL database.
3. **Configure `myFlywayConfig.conf`**: Set up your SQL configurations in the `myFlywayConfig.conf` file located in
   the `microstamp-step1-microservice` directory. The configuration should reflect the same SQL settings as in the
   `application.properties` file
4. **Create SQL Database**: Create the required SQL database as configured in the `application.properties` file.
5. **Build the Project**:
    - Open a terminal.
    - Navigate to the root directory of the project.
    - Execute the following command:
        ```
        ./mvnw clean install
        ```
    - On Windows, use:  
        ```
        mvnw clean install
        ```
    - If you encounter a permission denied error on Unix-based systems, you may need to give execute permission to the `mvnw` file:
        ```
        chmod +x mvnw
        ```
6. **Run the Project**:      
    - **Method 1**: Run with `mvnw spring-boot:run`
      - Execute the following command in the root directory of the project:
        ```
        ./mvnw spring-boot:run -pl microstamp-step1-microservice
        ```
      - On Windows, use:
        ```
        mvnw spring-boot:run -pl microstamp-step1-microservice
        ```
    - **Method 2**: Run with `java -jar`
      - Navigate to the `microstamp-step1-microservice/target` directory.
      - Execute the following command:
        ```
        java -jar microstamp-step1-microservice-1.0.0.jar
        ```

## Access Points

Once the project is running, you can access the following endpoints:

- **API Documentation**: [http://localhost:8091/swagger](http://localhost:8091/swagger) - Swagger API documentation.
- **Guest Visualization**: [http://localhost:8091/guests](http://localhost:8091/guests) - Visualization for guests, with
  some example projects.
- **Login Page**: [http://localhost:8091/login](http://localhost:8091/login) - Any other route should redirect to the
  login page.
    - **Credentials**:
        - **Admin**:
            - Username: admin
            - Password: admin123
        - **Guest**:
            - Username: guest
            - Password: guest123

## Optional: Build and Run in IntelliJ IDEA

These steps are optional and only necessary if you prefer to build and run the project in IntelliJ IDEA. During our testing, we used IntelliJ IDEA Community Edition version 2024.1.1. If you don't have IntelliJ IDEA installed, you can download it from the [official JetBrains website](https://www.jetbrains.com/idea/download/other.html)

1. **Open Project in IntelliJ IDEA**:
    - Launch IntelliJ IDEA.
    - Click on "Open" and navigate to the root directory of the cloned project.
    - Select the project directory and click "Open" to load the project into IntelliJ IDEA.

2. **Update Maven Dependencies**:
    - Maven dependencies should automatically update when you open the project. However, if they do not, you can manually update them by:
        - Right-clicking on the project root directory in the IntelliJ project explorer.
        - Selecting "Maven" > "Reload project" to force Maven to update dependencies.

3. **Enable Annotation Processing**:
    - Lombok is used in this project to reduce boilerplate code. Therefore, annotation processing needs to be enabled:
        - Go to File > Settings > Build, Execution, Deployment > Compiler > Annotation Processors.
        - Check the box for "Enable annotation processing".

4. **Run the Application**:
    - Navigate to the `Step1Application.java` file located at `microstamp-step1-microservice\src\main\java\microstamp\step1\Step1Application.java`.
    - Right-click on the `Step1Application.java` file.
    - Select "Run `Step1Application`" to execute the Spring Boot application within IntelliJ IDEA.

## Cleaning Build Files

To clean the build files, execute the following command:
```
./mvnw clean
```

On Windows, use:
```
mvnw clean
```