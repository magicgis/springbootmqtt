package com.koakh.mqtt.moquette.broker.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "config_properties")
public class ConfigProperties {

  private boolean dropMongoDatabase;
  private String topicBasePrefix;
  private String clientPrefix;
  private List<String> mokeUsers;
}
