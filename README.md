# MicroSTAMP - Microservices for STPA (System-Theoretic Process Analysis)

## Description

This repository contains the source code for MicroSTAMP,  a web application based on microservices architecture to support STPA.  Out of the 4 steps of STPA, MicroSTAMP supports steps 1, 2, and 3. We estimate to release the microservice implementation for Step 4 by **January, 2025**.

## Table of Contents
 
 -   [Architecture Overview](#architecture-overview)
 -   [End Users Guide](#end-users-guide)

## Architecture Overview

### MicroSTAMP microservices

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
