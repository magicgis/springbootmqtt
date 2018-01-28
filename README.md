#  SpringBootMQTTBrokerWithMoquetteStarter

(based on moquette-spring-docker)

This is a spring bootified version of the Moquette MQTT library ([https://github.com/andsel/moquette](https://github.com/andsel/moquette))

Moquette has been wrapped to use spring-boot instead of having to run it standalone. This allows the option to have the 
authentication and authorization classes to be used as spring beans. Also by using spring-boot it is very simple to build a
docker container from this.

In order to build ensure you have docker-toolbox installed and simple run the build:

Standard build: ```mvn clean install```
Build docker container: ```mvn clean package docker:build```

**Running container**

There is already a docker container available on Docker hub: [https://hub.docker.com/r/renarj/mqtt/](https://hub.docker.com/r/renarj/mqtt/)
In order to run it use the following command:

```docker run -d -p 1883:1883 renarj/mqtt:latest```

## Technical notes

The dockerfile is contained in the src\main\resources and relies on the spring-boot repackage stap in the maven build to create a single jar file.

## Creating your own authorization or authentication wrappers

Implement your own Spring beans annotated with @Component and make sure they are present in a (sub)package of 'com.oberasoftware.moquette.wrapper'

## Koakh project Notes

Above notes are keeped from orginal author repo (thanks m8s)

This project is based/started on moquette and awesome work of above developers, thanks guys for this awesome moquette project and excelent [renarj Repo](https://github.com/renarj/moquette-spring-docker) repository

Added to **renarj Repo** 

[x] Mongo Service, Repositories
[X] Mongo Users and Acls
[X] Service Based
[X] IoT Ready, with Domain models Users, Acls, Clients, Devices and SolarMonitor
[X] Automatically creates Devices and Clients based on MQTT Publish messages
[X] DataSeed users and acls
[X] Multi Module Project, with Library @Beans Wired in Api and Broker     
[X] many other things...

**Some Moked Acls**

```
pattern readwrite iot/%u/#
pattern readwrite iot/%u/%c/#
pattern write $SYS/broker/connection/%c/state
pattern read $SYS/#
topic iot/[USER]/esp8266/#
```

## Docker

Docker Public Repository at [koakh/koakh-mqtt-moquette-broker](https://hub.docker.com/r/koakh/koakh-mqtt-moquette-broker/)

## End Notes

This project stops here, and leave it has a Starter for next IOTProject, this way we have only a module Broker, With Repositories, Services etc, before splitting into diferent modules, but ready to split :) 