package com.koakh.mqtt.moquette.broker.models;

import com.koakh.mqtt.moquette.broker.enums.DeviceType;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
// Without this we get StackOverflowError, circular relationships
@ToString(exclude = {"id", "user", "client", "device"})
@Document(collection = "solarMonitor")
public class SolarMonitor {

  @Id
  private String id;
  private LocalDateTime localDateTime = LocalDateTime.now();
  private long timeStampMillis = Instant.now().toEpochMilli();
  private LocalDate date;
  // Auth
  @Indexed
  @DBRef
  private User user;
  @Indexed
  @DBRef
  private Device device;
  @Indexed
  @DBRef
  private Client client;
  // From PayLoad
  @Indexed
  // Require to map Payload to pojo
  private String deviceId;
  private DeviceType deviceType;
  private String mac;
  private String ip;
  private long utc;
  private long utcUptime;
  private int lux;
  private float temp;
  private float humi;
  private int curr;
  private int volt;
  private int watt;

  // Jackson required
  public SolarMonitor() {
  }
}
