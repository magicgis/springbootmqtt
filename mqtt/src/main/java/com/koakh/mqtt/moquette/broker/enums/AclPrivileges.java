package com.koakh.mqtt.moquette.broker.enums;

public enum AclPrivileges {

  // Get Enum from String with EnumType.valueOf("VALUE");

  READ("read"),
  WRITE("write"),
  READWRITE("readwrite");

  private String value;

  AclPrivileges(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }
}
