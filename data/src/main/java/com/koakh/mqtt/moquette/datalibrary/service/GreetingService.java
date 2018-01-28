package com.koakh.mqtt.moquette.datalibrary.service;

import com.koakh.mqtt.moquette.datalibrary.config.ServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GreetingService {

  private final ServiceProperties serviceProperties;

  /**
   * To make this service configurable in a standard Spring Boot idiom (with application.properties)
   * you can also add a @ConfigurationProperties class: ServiceProperties
   */
  @Autowired
  public GreetingService(ServiceProperties serviceProperties) {

    this.serviceProperties = serviceProperties;
  }

  public String message() {

    return this.serviceProperties.getMessage();
  }
}
