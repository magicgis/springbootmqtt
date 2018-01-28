package com.koakh.mqtt.moquette.broker.broker;

import com.koakh.mqtt.moquette.broker.config.SpringContextProvider;
import io.moquette.spi.impl.subscriptions.Topic;
import io.moquette.spi.security.IAuthorizator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;

public class SpringAuthorizationWrapper implements IAuthorizator {

  private static final Logger LOG = LoggerFactory.getLogger(SpringAuthorizationWrapper.class);

  private boolean securityEnabled = true;

  @Override
  public boolean canWrite(Topic topic, String userName, String client) {
    IAuthorizator authenticator = getAuthorized();
    return authenticator == null || authenticator.canWrite(topic, userName, client);
  }

  @Override
  public boolean canRead(Topic topic, String userName, String client) {
    IAuthorizator authenticator = getAuthorized();
    return authenticator == null || authenticator.canRead(topic, userName, client);
  }

  private IAuthorizator getAuthorized() {
    if (securityEnabled) {
      try {
        return SpringContextProvider.getApplicationContext().getBean(IAuthorizator.class);
      } catch (BeansException e) {
        LOG.info("Could not load a IAuthenticator bean, disabling authenticator security");
        securityEnabled = false;
      }
    }

    //default is always null and disabled
    return null;
  }
}
