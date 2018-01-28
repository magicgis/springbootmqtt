package com.koakh.mqtt.moquette.broker.enums;

public enum DeviceType {

  // Get Enum from String with EnumType.valueOf("VALUE");

  SOLAR_MONITOR("solarmonitor");

  private String value;

  DeviceType(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }
}
