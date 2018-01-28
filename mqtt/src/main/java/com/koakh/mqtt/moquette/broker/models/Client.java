package com.koakh.mqtt.moquette.broker.models;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
// Without this we get StackOverflowError, circular relationships
@ToString(exclude = {"id", "user", "device"})
@Document(collection = "client")
public class Client {

  @Id
  private String id;
  @Indexed(unique = true)
  private String clientId;
  @DBRef
  private User user;
  @DBRef
  private Device device;

  // Jackson required
  public Client() {
  }

  public Client(String clientId, User user, Device device) {
    this.clientId = clientId;
    this.user = user;
    this.device = device;
  }
}
