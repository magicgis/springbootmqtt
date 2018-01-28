package com.koakh.mqtt.moquette.broker.broker;

import com.koakh.mqtt.moquette.broker.config.SpringContextProvider;
import io.moquette.spi.security.IAuthenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;

public class SpringAuthenticationWrapper implements IAuthenticator {

  private static final Logger LOG = LoggerFactory.getLogger(SpringAuthenticationWrapper.class);

  private boolean securityEnabled = true;

  @Override
  public boolean checkValid(String client, String userName, byte[] passWord) {
    //public boolean checkValid(String s, byte[] bytes) {
    if (securityEnabled) {
      try {
        IAuthenticator authenticator = SpringContextProvider.getApplicationContext().getBean(IAuthenticator.class);
        return authenticator.checkValid(client, userName, passWord);
      } catch (BeansException e) {
        LOG.info("Could not load a IAuthenticator bean, disabling authenticator security");
        securityEnabled = false;
      }
    }

    //default is always valid
    return true;
  }
}
