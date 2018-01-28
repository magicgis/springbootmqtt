package com.koakh.mqtt.moquette.broker.config;

public class Constants {

  public static final boolean MOKE_CREATE_DEVICES_AND_CLIENTS = true;
  public static final boolean MOKE_SHOW_SEED_DATA_ADD = true;
  public static final String MOKE_USER_PASS_POSTFIX = "28";
  // Domain Device
  // iot/zila/esp8266-3
  public static final String HOMIE_BASE_TOPIC = "%s/%s/%s";
  // %basetopic%/%deviceid%/%devicetype%/payload
  // iot/zila/esp8266-3/a020a61737ea/solarmonitor/payload
  public static final String SUBSCRIBE_PAYLOAD = "%s/%s/%s/payload";
  //topic|pattern privileges topic ex [topic read $SYS/#], [patern readwrite acme/%c/%u], etc
  //@Value("${mqtt.acl.main_pattern}")
  public static String FORMAT_MAIN_PATTERN = "%s %s %s";
  //topic topicprefix/username/subtopiv
  //@Value("${mqtt.acl.client_user_pattern:topic %s/%s}")
  public static String FORMAT_COMPOSE_USER_PATTERN = "topic %s/%s";

  //Todo: not need clientId is always the value from payload
  // ClientId Format ClientPrefix-DeviceId ex koakh-iot-a020a6173277
  //public static final String CLIENT_ID_FORMAT = "%s-%s";
}
