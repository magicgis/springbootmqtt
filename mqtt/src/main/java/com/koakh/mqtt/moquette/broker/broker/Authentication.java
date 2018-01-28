package com.koakh.mqtt.moquette.broker.broker;

import com.koakh.mqtt.moquette.broker.service.UserService;
import io.moquette.spi.security.IAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;

@Component
public class Authentication implements IAuthenticator {

  UserService userService;

  @Autowired
  public Authentication(UserService userService) {
    this.userService = userService;
  }

  @Override
  public boolean checkValid(String clientId, String username, byte[] password) {
    //Added DBAuthenticator & Test with H2 DB
    //https://github.com/mackristof/moquette/commit/cad1165071693b19c83b73c76dd9ec0c65713d64#diff-b2986be988f4a8480d9bba829c8bd187R34
    // Used to check password with [echo -n "password28" | sha256sum]
    //MessageDigest digest = MessageDigest.getInstance(SHA_256);
    //String dbPassword = "6f5ea1c4acc7a563ea8cb3381a55f0183a2394d679ebb7db8312e047bbf87e0d";
    //String hashPassword = new String(Hex.encodeHex(digest.digest(password)));

    try {
      return userService.checkValidUser(username, password);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      return false;
    }
  }
}
