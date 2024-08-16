# Microstamp Service Registry

This repository contains the source code for the MicroSTAMP Service Registry using Sprint Cloud Netflix Eureka Server. Service registry and discovery play an important role when running multiple instances of services and we need a mechanism to call other services without hard coding their host names or port numbers.

With MicroSTAMP Service Registry we need to only configure an ID for each microservice that register itself as client with the MicroSTAMP Service Registry. In that sense, we don't have to bother about the hostname and port of the microservices instances that we want to communicate.

In addition to that, in cloud environments, service instances may come up and go down any time. Hence, we need some automatic service registration and discovery mechanism as the provide in this repository.

Basically all MicroSTAMP microservices (microstamp-api-gateway, microstamp-stpa-step1, microstamp-stpa-step2, microstamp-stpa-step3, microstamp-stpa-step4 register themselves to  Service Registry, and Service Registry tracks all the MicroSTAMP microservices and its instances. We can use service Registry in order to see what are the microservices are up and what are the microservices down.
