FROM java:8
VOLUME /tmp
ADD koakh-mqtt-moquette-api-*.jar mqtt-api.jar
RUN bash -c 'touch /mqtt-api.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/mqtt-api.jar"]
EXPOSE 8080
