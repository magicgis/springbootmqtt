package com.koakh.mqtt.moquette.broker.service;

import com.koakh.mqtt.moquette.broker.config.ConfigProperties;
import com.koakh.mqtt.moquette.broker.models.Client;
import com.koakh.mqtt.moquette.broker.models.Device;
import com.koakh.mqtt.moquette.broker.models.User;
import com.koakh.mqtt.moquette.broker.repository.ClientRepository;
import com.koakh.mqtt.moquette.broker.repository.DeviceRepository;
import com.koakh.mqtt.moquette.broker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static com.koakh.mqtt.moquette.broker.config.Constants.MOKE_CREATE_DEVICES_AND_CLIENTS;
import static com.koakh.mqtt.moquette.broker.config.Constants.MOKE_SHOW_SEED_DATA_ADD;

@Service
public class ClientService {

  private ClientRepository clientRepository;
  private DeviceRepository deviceRepository;
  private UserRepository userRepository;
  private ConfigProperties config;

  @Autowired
  public ClientService(ClientRepository clientRepository, DeviceRepository deviceRepository, UserRepository userRepository, ConfigProperties config) {

    this.clientRepository = clientRepository;
    this.deviceRepository = deviceRepository;
    this.userRepository = userRepository;
    this.config = config;
  }

  /**
   * SeedData
   */
  public void seedData() {

    clientRepository.deleteAll();

    if (MOKE_CREATE_DEVICES_AND_CLIENTS) {
      Device esp8266_1 = deviceRepository.findByFriendlyName("esp8266-1");
      Device esp8266_2 = deviceRepository.findByFriendlyName("esp8266-2");
      Device esp8266_3 = deviceRepository.findByFriendlyName("esp8266-3");
      Device esp8266_4 = deviceRepository.findByFriendlyName("esp8266-4");
      addClient(esp8266_1);
      addClient(esp8266_2);
      addClient(esp8266_3);
      addClient(esp8266_4);
      if (MOKE_SHOW_SEED_DATA_ADD) {
        // fetch all Clients
        System.out.println("Clients found with findAll():");
        for (Client client : clientRepository.findAll()) {
          System.out.println(String.format("  client: [%s]", client.getClientId()));
        }
        System.out.println();
      }
    }
  }

  /**
   * Add model Helper
   *
   * @param device
   * @return
   */
  private Client addClient(Device device) {

    User user = userRepository.findByUserName(device.getUser().getUserName());

    if (clientRepository.findByClientId(device.getDeviceId()) != null) {
      return clientRepository.findByClientId(device.getDeviceId());
    } else {
      Client client = new Client(device.getDeviceId(), device.getUser(), device);
      clientRepository.save(client);
      // Must init List
      if (user.getClients() == null) {
        user.setClients(new ArrayList<>());
      }
      // Add client to User
      user.getClients().add(client);
      userRepository.save(user);
      // Assign client device (reverse link)
      device.setClient(client);
      deviceRepository.save(device);
      return client;
    }
  }

  /**
   * Get Device by Id
   *
   * @param id
   * @return
   */
  public Client getById(String id) {

    try {
      return clientRepository.findOne(id);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  /**
   * Get Client by clientId
   *
   * @param clientId
   * @return
   */
  public Client getByClientId(String clientId) {

    try {
      return clientRepository.findByClientId(clientId);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  /**
   * Get or create if not exists, always return it
   *
   * @param clientId
   * @return
   */
  public Client getOrCreateByClientId(String clientId, User user, Device device) {

    try {
      Client client = getByClientId(clientId);
      if (client == null) {
        client = new Client(clientId, user, device);
        clientRepository.save(client);
        // Update reverse link
        device.setClient(client);
        deviceRepository.save(device);
        // Must init List
        if (user.getClients() == null) {
          user.setClients(new ArrayList<>());
        }
        user.getClients().add(client);
        userRepository.save(user);
        return client;
      } else {
        return client;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }
}
