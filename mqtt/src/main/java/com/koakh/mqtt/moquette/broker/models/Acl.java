package com.koakh.mqtt.moquette.broker.models;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
// Without this we get StackOverflowError, circular relationships
@ToString(exclude = {"id", "user"})
@Document(collection = "acl")
public class Acl {

  @Id
  private String id;
  private String rule;
  @DBRef
  private User user;

  // Jackson required
  public Acl() {
  }

  public Acl(String rule) {
    this.rule = rule;
  }
}
