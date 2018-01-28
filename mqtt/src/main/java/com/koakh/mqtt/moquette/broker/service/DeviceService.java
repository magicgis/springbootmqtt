package com.koakh.mqtt.moquette.broker.service;

import com.koakh.mqtt.moquette.broker.config.ConfigProperties;
import com.koakh.mqtt.moquette.broker.enums.DeviceType;
import com.koakh.mqtt.moquette.broker.models.Device;
import com.koakh.mqtt.moquette.broker.models.User;
import com.koakh.mqtt.moquette.broker.repository.DeviceRepository;
import com.koakh.mqtt.moquette.broker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import static com.koakh.mqtt.moquette.broker.config.Constants.MOKE_CREATE_DEVICES_AND_CLIENTS;
import static com.koakh.mqtt.moquette.broker.config.Constants.MOKE_SHOW_SEED_DATA_ADD;

@Service
public class DeviceService {

  private DeviceRepository deviceRepository;
  private UserRepository userRepository;
  private ConfigProperties config;

  @Autowired
  public DeviceService(DeviceRepository deviceRepository, UserRepository userRepository, ConfigProperties config) {

    this.deviceRepository = deviceRepository;
    this.userRepository = userRepository;
    this.config = config;
  }

  /**
   * SeedData
   *
   * @throws NoSuchAlgorithmException
   */
  public void seedData() throws NoSuchAlgorithmException {

    deviceRepository.deleteAll();

    if (MOKE_CREATE_DEVICES_AND_CLIENTS) {
      User kapa = userRepository.findByUserName(config.getMokeUsers().get(0));
      User zila = userRepository.findByUserName(config.getMokeUsers().get(1));
      addDevice("5ccf7f30123a", "esp8266-1", DeviceType.SOLAR_MONITOR, kapa, config.getTopicBasePrefix());
      addDevice("a020a61737eb", "esp8266-2", DeviceType.SOLAR_MONITOR, kapa, config.getTopicBasePrefix());
      addDevice("a020a61737ea", "esp8266-3", DeviceType.SOLAR_MONITOR, zila, config.getTopicBasePrefix());
      addDevice("5ccf7fa2bf6a", "esp8266-4", DeviceType.SOLAR_MONITOR, zila, config.getTopicBasePrefix());
      if (MOKE_SHOW_SEED_DATA_ADD) {
        // Fetch all Records
        System.out.println("Devices found with findAll():");
        for (Device device : deviceRepository.findAll()) {
          System.out.println(String.format("  device: [%s]", device.getFriendlyName()));
        }
        System.out.println();
      }
    }
  }

  /**
   * Add model Helper
   *
   * @param deviceId
   * @param friendlyName
   * @param deviceType
   * @param topicBasePrefix
   * @param user
   * @return
   */
  private Device addDevice(String deviceId, String friendlyName, DeviceType deviceType, User user, String topicBasePrefix) {

    if (deviceRepository.findByDeviceId(deviceId) != null) {
      return deviceRepository.findByDeviceId(deviceId);
    } else {
      Device device = new Device(deviceId, friendlyName, deviceType, user, topicBasePrefix);
      deviceRepository.save(device);
      // Must init List
      if (user.getDevices() == null) {
        user.setDevices(new ArrayList<>());
      }
      // Add device to User
      user.getDevices().add(device);
      userRepository.save(user);
      return device;
    }
  }

  /**
   * Get Device by Id
   *
   * @param id
   * @return
   */
  public Device getById(String id) {

    try {
      return deviceRepository.findOne(id);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  /**
   * Get Device by deviceId
   *
   * @param deviceId
   * @return
   */
  public Device getByDeviceId(String deviceId) {

    try {
      return deviceRepository.findByDeviceId(deviceId);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  /**
   * Get or create if not exists, always return it
   *
   * @param deviceId
   * @return
   */
  public Device getOrCreateByDeviceId(String deviceId, String friendlyName, DeviceType deviceType, User user, String topicBasePrefix) {

    try {
      Device device = getByDeviceId(deviceId);
      if (device == null) {
        device = new Device(deviceId, friendlyName, deviceType, user, topicBasePrefix);
        deviceRepository.save(device);
        // Must init List
        if (user.getDevices() == null) {
          user.setDevices(new ArrayList<>());
        }
        user.getDevices().add(device);
        userRepository.save(user);
        return device;
      } else {
        return device;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }
}
