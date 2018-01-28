# Notes

## Used Links

- [Java MQTT lightweight broker http://andsel.github.io/moquette/]https://github.com/andsel/moquette()

- [Moquette Documentation](http://andsel.github.io/moquette/documentation.html)

- [Configuring and Testing Mosquitto MQTT Topic Restrictions](http://www.steves-internet-guide.com/topic-restriction-mosquitto-configuration/)

- [Mongo Mappings](https://docs.spring.io/spring-data/mongodb/docs/1.3.3.RELEASE/reference/html/mapping-chapter.html)

## Queries

db.getCollection('acl').find({rule: /write/}, {_id: 0, rule: 1})
wildcard : /write/
projection: 
    {_id: 0, rule: 1} : 0 disable/1 enabled 

- [Ascending/Descending Sort](https://docs.mongodb.com/v3.4/reference/method/cursor.sort/)

Descending Sort
    use -1 ex .sort({"localDateTime":-1})
    db.getCollection('solarMonitor').find({}).sort({"localDateTime":-1}).limit(100);
with projection
    db.getCollection('solarMonitor').find({}, {_id: 0, localDateTime: 1, deviceId: 1, hardwareId: 1, lux: 1, temp: 1, humi: 1, curr: 1, volt: 1, watt: 1}).sort({"localDateTime":-1}).limit(10);
Filter hardwareId
    db.getCollection('solarMonitor').find({'hardwareId' : 'e7d716ed-kapa-0002-b1a4-4887375db7b2'}, {}).sort({"localDateTime":-1}).limit(10);

## MongoDb Problem and Solutions

- [MongoDB create unique index: E11000 duplicate key error collection dup key:{:null}](https://www.opentechguides.com/askotg/question/16/mongodb-create-unique-index-e11000-duplicate-key-error-collection-dup-keynull)

"E11000 duplicate key error collection"

> Fix droping database or all records that have this previous index used, some documts are using a previous index 

## Requirements

- [Install Docker]()https://wiki.deepin.org/index.php?title=Docker)

Build Docker Image

```
sudo mvn clean package docker:build

[INFO] Building image mqtt/moquette-spring
Step 1/6 : FROM java:8
...
Successfully built 76a315e84e42
Successfully tagged mqtt/moquette-spring:latest
[INFO] Built mqtt/moquette-spring
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 03:12 min
[INFO] Finished at: 2017-12-05T22:30:48Z
[INFO] Final Memory: 47M/403M

**Check Image Build**

```shell
udo docker image ls
REPOSITORY                            TAG                 IMAGE ID            CREATED             SIZE
koakh/koakh-mqtt-moquette-broker      latest              0da4282de488        3 seconds ago       703MB
java                                  8                   d23bdf5b1b1b        11 months ago       643MB
```

**Test Container**

```shell
//sudo docker run mqtt/moquette-spring
sudo docker run -p 1883:1883 -p 8083:8083 -p 9090:9090 -e CONFIG_PROPERTIES_DROP_MONGO_DATABASE=true -e SPRING_DATA_MONGODB_DATABASE=springboot-moquette-dev-env-docker koakh/koakh-mqtt-moquette-broker
```

- [Push images to Docker Cloud](https://docs.docker.com/docker-cloud/builds/push-images/)

```shell
export DOCKER_ID_USER="koakh"
export DOCKER_IMAGE="koakh-mqtt-moquette-broker"
sudo docker login
Login with your Docker ID to push and pull images from Docker Hub. If you don't have a Docker ID, head over to https://hub.docker.com to create one.
Username: koakh
Password: kkl......
Login Succeeded
```

**Tag Image**

```shell
sudo docker tag $DOCKER_ID_USER/$DOCKER_IMAGE $DOCKER_ID_USER/$DOCKER_IMAGE
```

**Push Image**

```shell
sudo docker push $DOCKER_ID_USER/$DOCKER_IMAGE
R_ID_USER/$DOCKER_IMAGE
The push refers to a repository [docker.io/koakh/koakh-mqtt-moquette-broker]
1d26592d9673: Pushing [======>                                            ]  3.605MB/29.83MB
8e3ad012fb23: Pushing [========>                                          ]  4.916MB/29.83MB
...
```

Post https://registry-1.docker.io/v2/koakh/koakh-spring-boot-mqtt-server/blobs/uploads/: net/http: TLS handshake timeout

**Environment Variables**

- SPRING_DATA_MONGODB_URI: mongodb://192.168.1.1:27017
- SPRING_DATA_MONGODB_DATABASE: springboot-moquette-dev
- CONFIG_PROPERTIES_DROP_MONGO_DATABASE: true|false

## Auth

https://www.javatips.net/api/moquette-master/broker/src/main/java/io/moquette/spi/impl/security/FileAuthenticator.java
echo -n "yourpassword" | sha256sum

## Fix java.lang.UnsupportedOperationException: direct buffer

- [java.lang.UnsupportedOperationException: direct buffer]()https://github.com/netty/netty/issues/5208)

changed `new String(message.getPayload().array()` to `new String(ByteBufUtil.getBytes(message.getPayload()), Charset.forName("UTF-8"))` 
to prevent error `java.lang.UnsupportedOperationException: direct buffer` in 

`main/java/com/oberasoftware/moquette/wrapper/broker/BrokerInterceptor.java` -> `public void onPublish(InterceptPublishMessage message)`

## Sample Payload

publish topic: iot/kapa/esp8266-1/5ccf7f30123a/solarmonitor/payload

```json
{
 "deviceId" : "a020a6173277", 
 "deviceType": "SOLAR_MONITOR",
 "mac" : "A0:20:A6:17:32:77", 
 "ip" : "192.168.1.149", 
 "utc" : "1514580503", 
 "utcUptime" : "26221", 
 "lux" : "54612", 
 "temp" : "0.00", 
 "humi" : "0.00", 
 "curr" : "0", 
 "volt" : "0", 
 "watt" : "-1"
}
```