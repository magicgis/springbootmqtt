package com.koakh.mqtt.moquette.api.bootstrap;

import com.koakh.mqtt.moquette.broker.models.Hardware;
import com.koakh.mqtt.moquette.datalibrary.repository.HardwareRepository;
import com.koakh.mqtt.moquette.datalibrary.service.GreetingService;
import com.koakh.mqtt.moquette.datalibrary.service.HardwareService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class BootstrapApplication implements ApplicationListener<ContextRefreshedEvent> {

  private static final Logger LOG = LoggerFactory.getLogger(BootstrapApplication.class);

  private final GreetingService greetingService;
  private final HardwareRepository hardwareRepository;
  private final HardwareService hardwareService;
  @Value("${spring.data.mongodb.uri}")
  private String mongoUri;

  @Autowired
  public BootstrapApplication(
    GreetingService greetingService,
    HardwareRepository hardwareRepository,
    HardwareService hardwareService
  ) {
    this.greetingService = greetingService;
    this.hardwareRepository = hardwareRepository;
    this.hardwareService = hardwareService;
  }

  @Override
  public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

    // Test service from Module Library
    this.greetingService.message();

    // TestService from Module Library
    hardwareService.seedData();

    // Test Repository from Module Library
    Hardware hardware = new Hardware("Hardware#5", "Description#5");
    hardwareRepository.save(hardware);
    System.out.println(hardwareRepository.findByName("Hardware#5").getName());

    // Test properties @Value
    System.out.println(mongoUri);
  }
}
