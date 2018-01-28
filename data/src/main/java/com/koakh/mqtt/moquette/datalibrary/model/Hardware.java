package com.koakh.mqtt.moquette.broker.models;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
// Without this we get StackOverflowError, circular relationships
@ToString(exclude = {"id"})
@Document(collection = "hardware")
public class Hardware {

  @Id
  private String id;
  @Indexed(unique = true)
  private UUID uuid;
  private String name;
  private String description;

  // Jackson required
  public Hardware() {
  }

  public Hardware(String name, String description) {
    this.uuid = UUID.randomUUID();
    this.name = name;
    this.description = description;
  }
}
