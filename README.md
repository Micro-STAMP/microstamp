# MicroSTAMP - Microservices for STPA (System-Theoretic Process Analysis)

## Description

This repository contains the source code for MicroSTAMP,  a web application based on microservices architecture to support STPA.  Out of the 4 steps of STPA, MicroSTAMP supports steps 1, 2, and 3. We estimate to release the microservice implementation for Step 4 by **January, 2025**.

## Table of Contents
 
 -   [Architecture Overview](#architecture-overview)
 -   [End Users Guide](#end-users-guide)
 -   [Developers Guide](#developers-guide)
 -   [MicroSTAMP Microservices](#microstamp-microservices)
 -   [Publications](#publications)
 -   [Talk](#talk)
 -   [Contact Information](#contact-information)

## Architecture Overview

 <img src="assets/images/microstamp-architecture.png?raw=true" alt="MicroSTAMP Architecture">

### Ports 

| Microservice | Port |
|--|--|
| microstamp-step1 | 8091 |
| microstamp-step2 | 8090 |
| microstamp-step3 | 8080 |
| microstamp-service-registry | 8761|
| microstamp-authorization-server | 9000 |
| microstamp-api-gateway | 9191 |

### Technologies 

<div style="display: flex; gap: 7px; flex-wrap: wrap;">
    <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=spring&logoColor=6DB33F&labelColor=070707" alt="Spring Boot">
    <img src="https://img.shields.io/badge/Spring%20Cloud-6DB33F?style=for-the-badge&logo=spring&logoColor=white&labelColor=070707" alt="Spring Cloud">
    <img src="https://img.shields.io/badge/Java-E84135?style=for-the-badge&logo=openjdk&logoColor=E84135&labelColor=070707" alt="Java">
    <img src="https://img.shields.io/badge/Maven-c71a36?style=for-the-badge&logo=apache-maven&logoColor=913C76&labelColor=070707" alt="Maven">
    <img src="https://img.shields.io/badge/MySQL-316192?style=for-the-badge&logo=mysql&logoColor=316192&labelColor=070707" alt="MySQL">
    <img src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=FF6C37&labelColor=070707" alt="Postman">
    <img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=85EA2D&labelColor=070707" alt="Swagger">
    <img src="https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=react&logoColor=white&labelColor=070707" alt="React">
    <img src="https://img.shields.io/badge/Flyway-004B87?style=for-the-badge&logo=flyway&logoColor=white&labelColor=070707" alt="Flyway">
    <img src="https://img.shields.io/badge/Tomcat-F8DC75?style=for-the-badge&logo=apache-tomcat&logoColor=black&labelColor=070707" alt="Apache Tomcat">
    <img src="https://img.shields.io/badge/IntelliJ-000000?style=for-the-badge&logo=intellij-idea&logoColor=white&labelColor=070707" alt="IntelliJ">

<img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white&labelColor=070707" alt="Docker">
</div>

## End Users Guide

### Prerequisites

To build and run the application as an end-user, you’ll need:

- **Docker**

### Installation

The easiest way to run **microstamp** is using Docker.

#### Step 1: Install Docker and Docker Compose

Ensure you have Docker and Docker Compose installed on your machine:

- **Docker**: Download and install Docker from the [official Docker website](https://www.docker.com/).
- **Docker Compose**: It usually comes installed with Docker Desktop. You can check the version by running:


    docker-compose --version

#### Step 2: Open a Terminal

Navigate to the directory where the `docker-compose.yaml` file is located. You can do this using the `cd` command in your terminal:

	cd /path/to/directory

#### Step 3: Run Docker Compose

Execute the following command to start the services defined in the `docker-compose.yaml` file:

	docker-compose up

This command will:

-   Build the Docker images if they are not already built.
-   Start all the containers defined in the `docker-compose.yaml`.

#### Additional Options

**Run in detached mode** (in the background):

	docker-compose up -d

**View logs**:

	docker-compose logs -f

**Stop the containers**:

	docker-compose down

## Developers Guide

#### Prerequisites

To build and run the application as a software developer, you'll need:
- [JDK 21](https://www.oracle.com/java/technologies/downloads/#java21)
- [MySQL 8.0](https://dev.mysql.com/downloads/mysql) or higher
- IntelliJ IDEA Ultimate Edition (That's our IDE of choice, but you are free to use other IDEs as well).

#### Running MicroSTAMP within IntelliJ Idea Ultimate Edition

We use IntelliJ IDEA 2023.3.5 Ultimate Edition in this example of how to use IntelliJ to run MicroSTAMP. I what follows we provide the steps needed to get MicroSTAMP up an running.

**Step 1 - Run the MicrostampServiceRegistryApplication microservice**

There several ways to run a microservice with IntelliJ. One possible way is shown in Figure 1.
<figure>
  <img src="assets/images/running-service-registry-within-intellij.png?raw=true" alt="Running the service registry">
	<figcaption>Figure 1: Running the service registry microservice.</figcaption>
</figure>
<p></p>
 As shown in the console output of Figure 2, the MicrostampServiceRegistryApplication runs on Tomcat, port 8761.
<p></p>

<figure>
  <img src="assets/images/console-log-after-running-service-registry-within-intellij.png?raw=true" alt="Service registry running on port 8761">
	<figcaption>Figure 2: The service registry runs on port 8761.</figcaption>
</figure>
<p></p>

We can access the service registry administration page (Spring Eureka) by the URL `http://localhost:8761/`. At this moment, no microservice instances are registered with the service registry.
<p></p>

<figure>
  <img src="assets/images/accessing-eureka-landing=page.png?raw=true" alt="Spring Eureka">
  <figcaption>Figure 3: The service registry administration page.</figcaption>
</figure>
<p></p>

## MicroSTAMP microservices 

### 1. MicroSTAMP Service Registry

The MicroSTAMP Service Registry is implemented with Sprint Cloud Netflix Eureka Server. Service registry and discovery play an important role when running multiple instances of services and we need a mechanism to call other services without hard coding their host names or port numbers.

With MicroSTAMP Service Registry we need to only configure an ID for each microservice that register itself as client with the MicroSTAMP Service Registry. In that sense, we don't have to bother about the hostname and port of the microservices instances that we want to communicate.

In addition to that, in cloud environments, service instances may come up and go down any time. Hence, we need some automatic service registration and discovery mechanism as the provided in this microservice.

Basically all MicroSTAMP microservices (microstamp-api-gateway, microstamp-stpa-step1, microstamp-stpa-step2, microstamp-stpa-step3, microstamp-stpa-step4) register themselves to  Service Registry, and Service Registry tracks all the MicroSTAMP microservices and its instances. We can use service Registry in order to see what are the microservices are up and what are the microservices down.

## Publications

Maimone, João Hugo Marinho, Thiago Franco de Carvalho Dias, Fellipe Guilherme Rey de Souza, and Rodrigo Martins Pagliares. "***MicroSTAMP: Microservices for Steps 1 and 2 of the System-Theoretic Process Analysis (STPA) Technique**.*" In _International Conference on Information Technology-New Generations_, pp. 469-476. Cham: Springer Nature Switzerland, 2024.

## Talk

Rodrigo Martins Pagliares, João Hugo Marinho Maimone, Thiago Franco de Carvalho Dias, Gabriel Piva Pereira, Gabriel Francelino Nascimento, and Fellipe Guilherme Rey de Souza  (Universidade Federal de Alfenas, UNIFAL-MG - Brazil) , Gabriel Kusumota Nadalin  (Universidade Federal de São Carlos, UFSCAR - Brazil) "***MicroSTAMP: Towards a Free and Open-Source STPA Compliant Web Tool Based on Microservices Architecture***", 2024.

https://psas.scripts.mit.edu/home/2024-stamp-workshop-program-virtual/


## Contact Information

<address>
Rodrigo Martins Pagliares<br>
rodrigo.pagliares@unifal-mg.edu.br<br>
Universidade Federal de Alfenas - UNIFAL<br>
Computer Science Department<br>
Av. Jovino Fernandes Sales, 2600 – Santa Clara – Alfenas/MG  - Brazil<br>
CEP: 37133-840<br>
Prédio C – 3º andar (Building C, third floor)<br> 
</address>
