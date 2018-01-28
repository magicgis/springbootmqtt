package com.koakh.mqtt.moquette.broker.enums;

public enum AclMode {

  // Get Enum from String with EnumType.valueOf("VALUE");

  TOPIC("topic"),
  PATTERN("pattern");

  private String value;

  AclMode(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }
}
