package com.koakh.mqtt.moquette.broker;

import com.koakh.mqtt.moquette.broker.broker.MoquetteServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

/**
 * Note : Not using explicity @Import(SharedConfigurationReference.class)
 * and Working AutoWiring with Library Module, the trick seems to be that
 * the class SharedConfigurationReference must exist, and this put AutoWire Work,
 * If Delete SharedConfigurationReference class it Crashs again trying to AutoWire
 * Library Beans, like Mongo Repos etc
 */

/**
 * scanBasePackages : Require for Multi-Module Projects, to scan other base packages
 * else it cant found beans other modules on boot
 */
@SpringBootApplication(scanBasePackages = "com.koakh.mqtt.moquette")
public class Application {

  private static final Logger LOG = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) throws IOException {

    SpringApplication application = new SpringApplication(Application.class);
    final ApplicationContext context = application.run(args);

    MoquetteServer server = context.getBean(MoquetteServer.class);

    server.start();

    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        server.stop();
        LOG.info("Moquette Server stopped");
      }
    });
  }
}
