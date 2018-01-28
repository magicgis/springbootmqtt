package com.koakh.mqtt.moquette.broker.models;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
// Without this we get StackOverflowError, circular relationships
@ToString(exclude = {"id", "devices", "clients"})
@Document(collection = "user")
public class User {

  @Id
  private String id;
  @Indexed(unique = true)
  private String userName;
  private String passWord;
  @DBRef
  private List<Device> devices;
  @DBRef
  private List<Client> clients;

  // Jackson required
  public User() {
  }

  public User(String userName, String passWord) {
    this.userName = userName;
    this.passWord = passWord;
  }
}
