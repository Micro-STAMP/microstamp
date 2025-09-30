# <img src="assets/images/microstamp-logo.png?raw=true" alt="MicroSTAMP Logo"  width="55%" style="min-width: 272px;">

**_Microservices for STPA (System-Theoretic Process Analysis)_**

<br>

[![GitHub Stars](https://img.shields.io/github/stars/Micro-STAMP/microstamp?style=flat-square&color=f8efd4&label=Stars&labelColor=555555)](https://github.com/Micro-STAMP/microstamp/stargazers)
[![GitHub Forks](https://img.shields.io/github/forks/Micro-STAMP/microstamp?style=flat-square&color=f8efd4&label=Forks&labelColor=555555)](https://github.com/Micro-STAMP/microstamp/fork)
[![GitHub Issues](https://img.shields.io/github/issues/Micro-STAMP/microstamp?style=flat-square&color=f8efd4&label=Issues&labelColor=555555)](https://github.com/Micro-STAMP/microstamp/issues)
[![License: MIT](https://img.shields.io/badge/License-MIT-f8efd4?style=flat-square&labelColor=555555)](https://opensource.org/licenses/MIT)

## Description

This repository contains the source code for **MicroSTAMP**, a free and open-source **STPA** compliant tool based on a **microservices architecture**.

**MicroSTAMP** currently supports all four steps of STPA, including two different approaches for Step 4: the Handbook Approach, which follows the standard definition from the [STPA Handbook](https://psas.scripts.mit.edu/home/get_file.php?name=STPA_Handbook.pdf), and the Formal Approach, based on [Formally Developing Loss Scenarios](https://youtu.be/hp-KBjIBmrI?si=anK9-GYa8eFk1_2C), which introduces a formalized method for identifying loss scenarios.

Beyond step support, MicroSTAMP integrates a unified front-end to access all services, offers preloaded example analyses, and enables PDF export for each step with embedded navigation hyperlinks. Its microservices-based architecture also provides scalability, service discovery, and secure access through OAuth2 and OpenID.

## Table of Contents

-   [Architecture Overview](#architecture-overview)
-   [MicroSTAMP Microservices](#microstamp-microservices)
-   [How to Run MicroSTAMP](#how-to-run-microstamp)
-   [Developers Team](#developers-team)
-   [Publications](#publications)
-   [Talks](#talks)
-   [Supporters](#supporters)
-   [Contributing Guidelines](#contributing-guidelines)
-   [Partnership Opportunities](#partnership-opportunities)
-   [Contact Information](#contact-information)

## Architecture Overview

<figure>
    <img src="assets/images/microstamp_architecture.png?raw=true" alt="MicroSTAMP Architecture">
    <figcaption><strong>Figure: MicroSTAMP architecture.</strong></figcaption>
</figure>

The MicroSTAMP architecture is based on independent **microservices**, each responsible for a specific part of the STPA technique or for supporting the system infrastructure. This design promotes scalability, reusability and easy integration with other STPA tools. Microservices communicate via REST through the API Gateway; each STPA step service manages its own data store to keep bounded contexts and improve modularity and traceability.

**Key components**

-   **MicroSTAMP UI**: unified front-end that consumes the microservice APIs.
-   **API Gateway**: single entry point that routes requests to services.
-   **Authorization Server**: handles authentication/authorization (OAuth2/OpenID).
-   **Service Registry**: service discovery (registers service instances for inter-service communication).
-   **STPA Step Microservices**: microservice for the STPA steps; each has its own database:
    -   Step 1 — Define Purpose of the Analysis
    -   Step 2 — Model the Control Structure
    -   Step 3 — Identify Unsafe Control Actions (Context Table + Rule-based approach)
    -   Step 4 — Identify Loss Scenarios (Handbook)
    -   Step 4 — Identify Loss Scenarios (Formal Approach)

### Ports

| Microservice                    | Port |
| ------------------------------- | ---- |
| microstamp-step1                | 8101 |
| microstamp-step2                | 8102 |
| microstamp-step3                | 8103 |
| microstamp-step4                | 8104 |
| microstamp-step4-new            | 8105 |
| microstamp-api-gateway          | 8000 |
| microstamp-authorization-server | 8001 |
| microstamp-service-registry     | 8002 |

| User Interface | Port |
| -------------- | ---- |
| microstamp-ui  | 3000 |

### Technologies

#### Microservices

<div style="display: flex; gap: 7px; flex-wrap: wrap;">
    <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=6DB33F&labelColor=070707" alt="Spring Boot">
    <img src="https://img.shields.io/badge/Spring%20Cloud-6DB33F?style=for-the-badge&logo=spring&logoColor=6DB33F&labelColor=070707" alt="Spring Cloud">
    <img src="https://img.shields.io/badge/Java-E84135?style=for-the-badge&logo=openjdk&logoColor=E84135&labelColor=070707" alt="Java">
    <img src="https://img.shields.io/badge/Maven-c71a36?style=for-the-badge&logo=apache-maven&logoColor=913C76&labelColor=070707" alt="Maven">
    <img src="https://img.shields.io/badge/MySQL-316192?style=for-the-badge&logo=mysql&logoColor=316192&labelColor=070707" alt="MySQL">
    <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=2496ED&labelColor=070707" alt="Docker">
</div>

#### User Interface

<div style="display: flex; gap: 7px; flex-wrap: wrap;">
    <img src="https://img.shields.io/badge/React-0175AA?style=for-the-badge&logo=react&logoColor=0175AA&labelColor=070707" alt="React">
    <img src="https://img.shields.io/badge/TypeScript-007ACC?style=for-the-badge&logo=typescript&logoColor=007ACC&labelColor=070707" alt="TypeScript">
    <img src="https://img.shields.io/badge/CSS-1572b6?style=for-the-badge&logo=css&logoColor=1572b6&labelColor=070707" alt="CSS">
    <img src="https://img.shields.io/badge/Node-43853D?style=for-the-badge&logo=node.js&logoColor=43853D&labelColor=070707" alt="Node">
</div>
<br>

[⬆️ Back to Top](#table-of-contents)

## MicroSTAMP Microservices

MicroSTAMP is composed of 8 core microservices: 3 services that handle the underlying architecture, and 5 STPA step services that each implement a specific step of the STPA technique (including one for each step 4 approach).

For all microservices, you can access the specific source code by visiting each service's dedicated directory.

| Microservice                                 | Description                                                                                                                    | Link                                                                                                        |
| -------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------ | ----------------------------------------------------------------------------------------------------------- |
| **MicroSTAMP Service Registry**              | Registers instances of the microservices for discovery and communication.                                                      | [Service Registry](https://github.com/Micro-STAMP/microstamp/tree/main/microstamp-service-registry)         |
| **MicroSTAMP API Gateway**                   | The main entry point, delegating requests to the corresponding microservices.                                                  | [API Gateway](https://github.com/Micro-STAMP/microstamp/tree/main/microstamp-api-gateway)                   |
| **MicroSTAMP Authorization Server**          | Controls authorization with OAuth2 and authentication with OpenID.                                                             | [Authorization Server](https://github.com/Micro-STAMP/microstamp/tree/main/microstamp-authorization-server) |
| **MicroSTAMP STPA Step 1**                   | Implements the **Define Purpose of the Analysis** step.                                                                        | [STPA Step 1](https://github.com/Micro-STAMP/microstamp/tree/main/microstamp-step1)                         |
| **MicroSTAMP STPA Step 2**                   | Implements the **Model the Control Structure** step.                                                                           | [STPA Step 2](https://github.com/Micro-STAMP/microstamp/tree/main/microstamp-step2)                         |
| **MicroSTAMP STPA Step 3**                   | Implements the **Identify Unsafe Control Actions** step, using context tables and rules.                                       | [STPA Step 3](https://github.com/Micro-STAMP/microstamp/tree/main/microstamp-step3)                         |
| **MicroSTAMP STPA Step 4 (Handbook)**        | Implements the **Identify Loss Scenarios** step using the standard STPA Handbook Approach.                                     | [STPA Step 4 (Handbook)](https://github.com/Micro-STAMP/microstamp/tree/main/microstamp-step4)              |
| **MicroSTAMP STPA Step 4 (Formal Approach)** | Implements the **Identify Loss Scenarios** step using the [Formal Approach](https://youtu.be/hp-KBjIBmrI?si=AqTFvtEHjV3ZX-Ra). | [STPA Step 4 (Formal Approach)](https://github.com/Micro-STAMP/microstamp/tree/main/microstamp-step4-new)   |

Additionally, you can find the directory for the MicroSTAMP User Interface here: [MicroSTAMP UI](https://github.com/Micro-STAMP/microstamp/tree/main/microstamp-ui).

[⬆️ Back to Top](#table-of-contents)

## How to Run MicroSTAMP

This section provides instructions for running MicroSTAMP using Docker, which is the recommended approach for most users.

Before proceeding, clone the project repository to your local machine:

<details>
<summary><strong>Clone the Project</strong></summary>

1.  Open a terminal or command prompt.
2.  Navigate to the directory where you want to clone the project.
3.  Clone the project repository using the following command:

    ```
    git clone https://github.com/Micro-STAMP/microstamp.git
    ```

</details>

<p></p>

Once the repository is cloned, follow the Docker setup instructions below.

<p></p>

<details>
<summary><strong>Run MicroSTAMP with Docker</strong></summary>
This option runs MicroSTAMP using Docker, which simplifies the setup process by containerizing all services.

#### Set up Docker

Ensure Docker and Docker Compose are installed and running on your machine. If not, download and install them from the official Docker website:

-   [Download Docker](https://docs.docker.com/get-started/get-docker/)
-   [Download Docker Compose](https://docs.docker.com/compose/install/)

#### Run the Docker Compose file

1.  Open a terminal or command prompt.
2.  Navigate to the root directory of the cloned project.
3.  Run the following command to start all services:
    ```
    docker-compose up --build
    ```

This command will build and start all the microservices, the MySQL databases, and the UI.

#### Access the Application

-   The user interface will be available at: `http://127.0.0.1:3000`.
-   The microservices will be running on their respective ports as defined in the [Architecture Overview](#architecture-overview).
    <br>

> Note for Development:
> If you prefer to run MicroSTAMP locally for development purposes, you can set up the environment using Maven and MySQL. The project > requires JDK 21, MySQL 8.0+, Apache Maven, and Node.js. For detailed local setup instructions or if you encounter any issues running the application, please contact our development team.

</details>

<p></p>

Once MicroSTAMP is running, you can explore the application using a pre-configured guest account.

<p></p>

<details>
<summary><strong>Exploring MicroSTAMP with the Guest User</strong></summary>

We created a user with some example analyses to provide a global picture of how MicroSTAMP supports STPA.
The user **guest** has some pre-stored STPA analyses and control structure from Step 2 of STPA.

<figure>
  <img src="assets/images/login-form-guest-user.png" alt="Login Form Page">
	<figcaption><strong>Figure: Authenticating with the user guest.</strong></figcaption>
</figure>

<p></p><br>

<figure>
  <img src="assets/images/page-with-all-analysis-user-guest.png" alt="Analysis Page">
	<figcaption><strong>Figure: Pre-stored STPA analyses page from user guest.</strong></figcaption>
</figure>

</details>

<br>

[⬆️ Back to Top](#table-of-contents)

## Developers Team

<div style="display: flex; gap: 12px; flex-wrap: wrap">
    <a href="https://github.com/JoaoHugo" target="_blank">
        <img src="https://img.shields.io/badge/João Hugo-f8efd4?style=for-the-badge&logo=Github&logoColor=f8efd4&labelColor=555555" alt="João Hugo">
    </a>
    <a href="https://github.com/gabriel-piva" target="_blank">
        <img src="https://img.shields.io/badge/Gabriel Piva-f8efd4?style=for-the-badge&logo=Github&logoColor=f8efd4&labelColor=555555" alt="Gabriel Piva">
    </a>
    <a href="https://github.com/gabriel-francelino" target="_blank">
        <img src="https://img.shields.io/badge/Gabriel Francelino-f8efd4?style=for-the-badge&logo=Github&logoColor=f8efd4&labelColor=555555" alt="Gabriel Francelino">
    </a>
    <a href="https://github.com/pagliares" target="_blank">
        <img src="https://img.shields.io/badge/Rodrigo Martins Pagliares-f8efd4?style=for-the-badge&logo=Github&logoColor=f8efd4&labelColor=555555" alt="Rodrigo Martins Pagliares">
    </a>
    <a href="https://github.com/felliperey" target="_blank">
        <img src="https://img.shields.io/badge/Fellipe Rey-f8efd4?style=for-the-badge&logo=Github&logoColor=f8efd4&labelColor=555555" alt="Fellipe Rey">
    </a>
    <a href="https://github.com/gabriel-nadalin" target="_blank">
        <img src="https://img.shields.io/badge/Gabriel Nadalin-f8efd4?style=for-the-badge&logo=Github&logoColor=f8efd4&labelColor=555555" alt="Gabriel Nadalin">
    </a>
    <a href="https://github.com/ThiagoFranco0202" target="_blank">
        <img src="https://img.shields.io/badge/Thiago Franco-f8efd4?style=for-the-badge&logo=Github&logoColor=f8efd4&labelColor=555555" alt="Thiago Franco">
    </a>
</div>
<br>

[⬆️ Back to Top](#table-of-contents)

## Publications

Maimone, João Hugo Marinho, Thiago Franco de Carvalho Dias, Fellipe Guilherme Rey de Souza, and Rodrigo Martins Pagliares.
"**MicroSTAMP: Microservices for Steps 1 and 2 of the System-Theoretic Process Analysis (STPA) Technique**."
In _International Conference on Information Technology-New Generations_, pp. 469-476. Cham: Springer Nature Switzerland, 2024.

 <a href="https://link.springer.com/chapter/10.1007/978-3-031-56599-1_59" target="_blank">
   <img src="assets/images/conference-paper.png" alt="Springer Chapter" width="65%">
 </a>

<br>

[⬆️ Back to Top](#table-of-contents)

## Talks

1. Maimone, João Hugo Marinho, Thiago Franco de Carvalho Dias, Fellipe Guilherme Rey de Souza, and Rodrigo Martins Pagliares. "**MicroSTAMP: Microservices for Steps 1 and 2 of the System-Theoretic Process Analysis (STPA) Technique**." In International Conference on Information Technology-New Generations, Las Vegas, NV, USA. April, 2024.

<figure style="width: 100%">
  <img src="assets/images/vegas-talk.png" width=249 height=278 alt="Rodrigo Martins Pagliares at ITNG 2024" />
  <figcaption style="font-size: small;"><strong>Figure: Rodrigo Martins Pagliares at ITNG 2024</strong></figcaption>
</figure>

<p></p><br>

2. "**MicroSTAMP: Towards a Free and Open-Source STPA Compliant Web Tool Based on Microservices Architecture**", STAMP Workshop, MIT Partnership for Systems Approaches to Safety and Security (PSASS), September, 2024.

 <a href="https://psas.scripts.mit.edu/home/2024-stamp-workshop-program-virtual/">
     <img src="assets/images/mit_presentation_2024.png" width=420  alt="MIT Presentation 2024">
 </a>

 <br>

Watch our presentation at MIT STAMP Workshop 2024:
<a href="https://youtu.be/G0o1CJsMk-U?si=U1P8XGSKYSLd5Uc7" target="_blank"><strong>Presentation Link</strong></a>

 <p></p><br>

3. "**MicroSTAMP: A Free and Open-Source Compliant Tool for STPA Using Microservices Architecture**", STAMP Workshop, MIT Partnership for Systems Approaches to Safety and Security (PSASS), September, 2025.

 <a href="https://psas.scripts.mit.edu/home/2025-stamp-workshop-program/">
     <img src="assets/images/mit_presentation_2025.png" width=420  alt="MIT Presentation 2025">
 </a>

[⬆️ Back to Top](#table-of-contents)

## Supporters

We gratefully acknowledge the support of these organizations that have contributed to the development of MicroSTAMP:

<div style="display: flex; gap: 8px; flex-wrap: wrap">
    <a href="https://www.unifal-mg.edu.br/" target="_blank">
       <img src="https://img.shields.io/badge/UNIFAL_MG-00629B?style=for-the-badge&logo=university&logoColor=white" alt="UNIFAL MG">
    </a>
    <a href="https://www.unifal-mg.edu.br/nti/" target="_blank">
        <img src="https://img.shields.io/badge/NTI_UNIFAL-008037?style=for-the-badge&logo=computer&logoColor=white" alt="NTI UNIFAL">
    </a>
</div>

<br>

[⬆️ Back to Top](#table-of-contents)

## Contributing Guidelines

Feel free to contribute to the MicroSTAMP project!  
If you find any bugs, have suggestions for improvements, or would like to add new features, please open an issue or submit a pull request.  
You can also contact us directly via email — contributions of any kind are appreciated.

[⬆️ Back to Top](#table-of-contents)

## Partnership Opportunities

We **welcome partnerships** with organizations interested in advancing open-source safety analysis tools.  
Possible contributions include:

-   Funding to support ongoing development
-   Technical expertise to enhance the project
-   Collaborative research using MicroSTAMP
-   Sponsorship of new features or improvements

If your organization is interested in exploring any form of collaboration, please reach out to us directly through the [Contact Information](#contact-information).

[⬆️ Back to Top](#table-of-contents)

## Contact Information

Rodrigo Martins Pagliares<br>
rodrigo.pagliares@unifal-mg.edu.br<br>
Universidade Federal de Alfenas - UNIFAL<br>
Computer Science Department<br>

[⬆️ Back to Top](#table-of-contents)
