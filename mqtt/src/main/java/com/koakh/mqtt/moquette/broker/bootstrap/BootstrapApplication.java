package com.koakh.mqtt.moquette.broker.bootstrap;

import com.koakh.mqtt.moquette.broker.config.ConfigProperties;
import com.koakh.mqtt.moquette.broker.service.*;
import com.koakh.mqtt.moquette.datalibrary.service.GreetingService;
import com.koakh.mqtt.moquette.datalibrary.service.HardwareService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;

@Component
public class BootstrapApplication implements ApplicationListener<ContextRefreshedEvent> {

  private static final Logger LOG = LoggerFactory.getLogger(BootstrapApplication.class);

  private ConfigProperties config;
  private MongoService mongoService;
  private UserService userService;
  private AclService aclService;
  private DeviceService deviceService;
  private ClientService clientService;
  private HardwareService hardwareService;
  private GreetingService greetingService;

  @Autowired
  public BootstrapApplication(
    ConfigProperties config, MongoService mongoService,
    UserService userService, AclService aclService,
    DeviceService deviceService, ClientService clientService,
    HardwareService hardwareService, GreetingService greetingService
  ) {
    this.config = config;
    this.mongoService = mongoService;
    this.userService = userService;
    this.aclService = aclService;
    this.deviceService = deviceService;
    this.clientService = clientService;
    this.hardwareService = hardwareService;
    this.greetingService = greetingService;
  }

  @Override
  public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

    // Test Multi Module Bean
    this.greetingService.message();

    try {
      // DropIt
      if (config.isDropMongoDatabase()) {
        mongoService.dropDatabase();
      }
      // SeedData
      userService.seedData();
      aclService.seedData();
      deviceService.seedData();
      clientService.seedData();
      hardwareService.seedData();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
  }
}
