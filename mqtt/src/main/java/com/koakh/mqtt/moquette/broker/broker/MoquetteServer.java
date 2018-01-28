package com.koakh.mqtt.moquette.broker.broker;

import io.moquette.interception.InterceptHandler;
import io.moquette.server.Server;
import io.moquette.server.config.*;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.mqtt.MqttMessageBuilders;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttQoS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Component
public class MoquetteServer {

  private static final Logger LOG = LoggerFactory.getLogger(MoquetteServer.class);

  @Value("${mqtt.serverport:1883}")
  private int serverPort;

  @Value("${mqtt.host:0.0.0.0}")
  private String host;

  @Value("${mqtt.websocket_port:8080}")
  private int websocketPort;

  @Value("${mqtt.use_file_config:false}")
  private boolean useFileConfig;

  private Server mqttBroker;
  private MemoryConfig config;

  private BrokerInterceptor brokerInterceptor;

  @Autowired
  public MoquetteServer(BrokerInterceptor brokerInterceptor) {
    this.brokerInterceptor = brokerInterceptor;
  }

  public void start() throws IOException {

    LOG.info("Starting MQTT on host: {} and port: {} with websocket port: {}", host, serverPort, websocketPort);

    // Use FileConfig
    if (useFileConfig) {
      IResourceLoader classpathLoader = new ClasspathResourceLoader();
      final IConfig config = new ResourceLoaderConfig(classpathLoader);
    }
    //Or use Custom MemConfig
    else {
      config = new MemoryConfig(new Properties());
      config.setProperty("port", Integer.toString(serverPort));
      config.setProperty("websocket_port", Integer.toString(websocketPort));
      config.setProperty("host", host);
      config.setProperty("authenticator_class", SpringAuthenticationWrapper.class.getName());
      config.setProperty("authorizator_class", SpringAuthorizationWrapper.class.getName());
    }

    // Boot Broker
    mqttBroker = new Server();
    // Without InterceptHandler
    // mqttBroker.startServer(config);
    // With BrokerInterceptor InterceptHandler
    List<? extends InterceptHandler> userHandlers = Collections.singletonList(/*new BrokerInterceptor()*/brokerInterceptor);
    mqttBroker.startServer(config, userHandlers);

    LOG.info("Moquette started successfully");

    System.out.println("Broker started press [CTRL+C] to stop");
    //Bind  a shutdown hook
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      System.out.println("Stopping broker");
      mqttBroker.stopServer();
      System.out.println("Broker stopped");
    }));

    try {
      Thread.sleep(20000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    //Todo : Publish messag after 20sec
    System.out.println("Before self publish");
    MqttPublishMessage message = MqttMessageBuilders.publish()
      .topicName("acme/kapa/mqtt-spy/topic")
      .retained(true)
      //qos(MqttQoS.AT_MOST_ONCE);
      //qos(MqttQoS.AT_LEAST_ONCE);
      .qos(MqttQoS.EXACTLY_ONCE)
      .payload(Unpooled.copiedBuffer("Hello World!!".getBytes()))
      .build();

    //Todo
    mqttBroker.internalPublish(message, "INTRLPUB");
    System.out.println("After self publish");

    //Todo
    //mqttBroker.getSubscriptions()
    //mqttBroker.getConnectionsManager().getConnectedClientIds()
  }

  public void stop() {

    mqttBroker.stopServer();
  }
}
