FROM java:8
VOLUME /tmp
ADD koakh-mqtt-moquette-broker-*.jar mqtt-svc.jar
RUN bash -c 'touch /mqtt-svc.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/mqtt-svc.jar"]
EXPOSE 1883