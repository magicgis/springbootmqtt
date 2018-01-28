package com.koakh.mqtt.moquette.broker.models;

import com.koakh.mqtt.moquette.broker.enums.DeviceType;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.koakh.mqtt.moquette.broker.config.Constants.HOMIE_BASE_TOPIC;
import static com.koakh.mqtt.moquette.broker.config.Constants.SUBSCRIBE_PAYLOAD;

@Data
// Without this we get StackOverflowError, circular relationships
@ToString(exclude = {"id", "user", "client"})
@Document(collection = "device")
public class Device {

  @Id
  private String id;

  // Store Homie Configuration
  @Indexed(unique = true)
  private String deviceId;
  private DeviceType deviceType;
  private String baseTopic;
  private boolean useAuthentication;
  private String username;
  private String password;
  @Indexed(unique = true)
  private String friendlyName;
  // Helper for subscribe payload, to get it in frontend
  private String subscribePayload;
  // User Owner
  @DBRef
  private User user;
  @DBRef
  private Client client;

  // Jackson required
  public Device() {
  }

  public Device(String deviceId, String friendlyName, DeviceType deviceType, User user, String topicBasePrefix) {
    this.deviceId = deviceId;
    this.friendlyName = friendlyName;
    this.deviceType = deviceType;
    this.useAuthentication = true;
    this.username = user.getUserName();
    this.password = user.getPassWord();
    this.user = user;
    baseTopic = String.format(HOMIE_BASE_TOPIC, topicBasePrefix, user.getUserName(), friendlyName);
    subscribePayload = String.format(SUBSCRIBE_PAYLOAD, baseTopic, deviceId, deviceType.toString());
  }
}
