package com.koakh.mqtt.moquette.broker.broker;

import com.koakh.mqtt.moquette.broker.Application;
import com.koakh.mqtt.moquette.broker.enums.AclPrivileges;
import com.koakh.mqtt.moquette.broker.service.AclService;
import io.moquette.spi.impl.subscriptions.Topic;
import io.moquette.spi.security.IAuthorizator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Authorization implements IAuthorizator {

  private static final Logger LOG = LoggerFactory.getLogger(Application.class);

  private AclService aclService;

  @Autowired
  public Authorization(AclService aclService) {

    this.aclService = aclService;
  }

  @Override
  public boolean canWrite(Topic topic, String userName, String client) {

    return aclService.canReadWriteTopic(topic, userName, client, AclPrivileges.WRITE);
  }

  @Override
  public boolean canRead(Topic topic, String userName, String client) {

    return aclService.canReadWriteTopic(topic, userName, client, AclPrivileges.READ);
  }
}
