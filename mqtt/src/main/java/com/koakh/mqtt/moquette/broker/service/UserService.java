package com.koakh.mqtt.moquette.broker.service;

import com.koakh.mqtt.moquette.broker.config.ConfigProperties;
import com.koakh.mqtt.moquette.broker.models.User;
import com.koakh.mqtt.moquette.broker.repository.UserRepository;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.koakh.mqtt.moquette.broker.config.Constants.MOKE_SHOW_SEED_DATA_ADD;
import static com.koakh.mqtt.moquette.broker.config.Constants.MOKE_USER_PASS_POSTFIX;
import static org.apache.commons.codec.digest.MessageDigestAlgorithms.SHA_256;

@Service
public class UserService {

  private UserRepository userRepository;
  private ConfigProperties config;

  @Autowired
  public UserService(UserRepository userRepository, ConfigProperties config) {

    this.userRepository = userRepository;
    this.config = config;
  }

  /**
   * SeedData
   *
   * @throws NoSuchAlgorithmException
   */
  public void seedData() throws NoSuchAlgorithmException {

    try {
      userRepository.deleteAll();
      //addUser(MOKE_USER_1, String.format("%s%s", Constants.MOKE_USER_1, Constants.MOKE_USER_PASS_POSTFIX));
      for (String user : config.getMokeUsers()) {
        addUser(user, String.format("%s%s", user, MOKE_USER_PASS_POSTFIX));
      }
      if (MOKE_SHOW_SEED_DATA_ADD) {
        // Fetch all Records
        System.out.println("Users found with findAll():");
        for (User user : userRepository.findAll()) {
          System.out.println(String.format("  userName: [%s], passWord: [%s]", user.getUserName(), user.getPassWord()));
        }
        System.out.println();
      }
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
  }

  /**
   * Add user helper, add User to Repo
   *
   * @param userName
   * @param passWord
   * @return
   * @throws NoSuchAlgorithmException
   */
  private User addUser(String userName, String passWord) throws NoSuchAlgorithmException {

    try {
      if (userRepository.findByUserName(userName) != null) {
        return userRepository.findByUserName(userName);
      } else {
        User user = new User(userName, getHashedPassWord(passWord));
        userRepository.save(user);
        return user;
      }
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }

    return null;
  }

  /**
   * Get User by Id
   *
   * @param id
   * @return
   */
  public User getById(String id) {

    try {
      return userRepository.findOne(id);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  /**
   * Get User by UserName
   *
   * @param userName
   * @return
   */
  public User getByUserName(String userName) {

    try {
      return userRepository.findByUserName(userName);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  /**
   * Hash the Password, from string
   *
   * @param passWord
   * @return
   * @throws NoSuchAlgorithmException
   */
  private String getHashedPassWord(String passWord) throws NoSuchAlgorithmException {
    MessageDigest digest = MessageDigest.getInstance(SHA_256);
    // Used to check password with [echo -n "password28" | sha256sum]
    //String hashPassword = new String(Hex.encodeHex(digest.digest("password28".getBytes(StandardCharsets.UTF_8))));
    return new String(Hex.encodeHex(digest.digest(passWord.getBytes(StandardCharsets.UTF_8))));
  }

  /**
   * Check Login/Password
   *
   * @param userName
   * @param passWord
   * @return
   * @throws NoSuchAlgorithmException
   */
  public boolean checkValidUser(String userName, byte[] passWord) throws NoSuchAlgorithmException {
    MessageDigest digest = MessageDigest.getInstance(SHA_256);
    String hashPassword = new String(Hex.encodeHex(digest.digest(passWord)));
    User user = getByUserName(userName);
    return (user != null && user.getPassWord().equals(hashPassword));
  }
}
