package com.koakh.mqtt.moquette.broker.broker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koakh.mqtt.moquette.broker.config.ConfigProperties;
import com.koakh.mqtt.moquette.broker.models.Client;
import com.koakh.mqtt.moquette.broker.models.Device;
import com.koakh.mqtt.moquette.broker.models.SolarMonitor;
import com.koakh.mqtt.moquette.broker.models.User;
import com.koakh.mqtt.moquette.broker.service.ClientService;
import com.koakh.mqtt.moquette.broker.service.DeviceService;
import com.koakh.mqtt.moquette.broker.service.SolarMonitorService;
import com.koakh.mqtt.moquette.broker.service.UserService;
import io.moquette.interception.AbstractInterceptHandler;
import io.moquette.interception.messages.*;
import io.netty.buffer.ByteBufUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;

@Component
class BrokerInterceptor extends AbstractInterceptHandler {

  private static final Logger LOG = LoggerFactory.getLogger(BrokerInterceptor.class);
  private static final boolean SHOW_INFO = false;

  private SolarMonitorService solarMonitorService;
  private UserService userService;
  private DeviceService deviceService;
  private ClientService clientService;
  private ConfigProperties config;

  //Todo
  //@Value("${mqtt.publisher_listener_id:moquette-publisher}")
  private String publisherId = "publisher";

  @Autowired
  public BrokerInterceptor(SolarMonitorService solarMonitorService, UserService userService, DeviceService deviceService, ClientService clientService, ConfigProperties config) {
    this.solarMonitorService = solarMonitorService;
    this.userService = userService;
    this.deviceService = deviceService;
    this.clientService = clientService;
    this.config = config;
  }

  @Override
  public String getID() {
    return publisherId;
  }

  @Override
  public Class<?>[] getInterceptedMessageTypes() {
    return super.getInterceptedMessageTypes();
  }

  @Override
  public void onConnect(InterceptConnectMessage msg) {
    super.onConnect(msg);
  }

  @Override
  public void onDisconnect(InterceptDisconnectMessage msg) {
    super.onDisconnect(msg);
  }

  @Override
  public void onConnectionLost(InterceptConnectionLostMessage msg) {
    super.onConnectionLost(msg);
  }

  @Override
  public void onPublish(InterceptPublishMessage message) {

    // Extract Content
    String content = new String(ByteBufUtil.getBytes(message.getPayload()), Charset.forName("UTF-8"));

    // Show Debug Info
    if (SHOW_INFO) System.out.println(
      String.format("Broker intercepted message - topic: [%s] from [%s/%s] : content: [%s]"
        , message.getTopicName()
        , message.getUsername()
        , message.getClientID()
        , content
      ));

    try {
      // Init Jackson ObjectMapper
      ObjectMapper mapper = new ObjectMapper();
      // Map content payload to domain object
      //User user = mapper.readValue(content, User.class);
      SolarMonitor solarMonitor = mapper.readValue(content, SolarMonitor.class);

      // Get domain Objects
      User user = userService.getByUserName(message.getUsername());
      // Get or Crete domain Objects
      Device device = deviceService.getOrCreateByDeviceId(message.getClientID(), "friendlyName", solarMonitor.getDeviceType(), user, config.getTopicBasePrefix());
      Client client = clientService.getOrCreateByClientId(message.getClientID(), user, device);

      // Assign properties to domain model
      solarMonitor.setUser(user);
      solarMonitor.setDevice(device);
      solarMonitor.setClient(client);

      // Persist SolarMonitor
      solarMonitorService.addSolarMonitor(solarMonitor);
    } catch (IOException e) {
      LOG.error(e.getLocalizedMessage());
    }
  }

  @Override
  public void onSubscribe(InterceptSubscribeMessage msg) {
    super.onSubscribe(msg);
  }

  @Override
  public void onUnsubscribe(InterceptUnsubscribeMessage msg) {
    super.onUnsubscribe(msg);
  }

  @Override
  public void onMessageAcknowledged(InterceptAcknowledgedMessage msg) {
    super.onMessageAcknowledged(msg);
  }
}