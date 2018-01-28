package com.koakh.mqtt.moquette.datalibrary.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@ConfigurationProperties("service")
public class ServiceProperties {

  /**
   * A message for the service.
   * using ConfigurationProperties/application.properties service.message
   */
  private String message;

  public String getMessage() {

    return message;
  }

  public void setMessage(String message) {

    this.message = message;
  }
}
