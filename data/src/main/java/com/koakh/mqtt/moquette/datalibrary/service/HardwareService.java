package com.koakh.mqtt.moquette.datalibrary.service;

import com.koakh.mqtt.moquette.broker.models.Hardware;
import com.koakh.mqtt.moquette.datalibrary.repository.HardwareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Service
public class HardwareService {

  private final boolean MOKE_SHOW_SEED_DATA_ADD = true;
  private HardwareRepository hardwareRepository;

  @Autowired
  public HardwareService(HardwareRepository hardwareRepository) {

    this.hardwareRepository = hardwareRepository;
  }

  /**
   * SeedData
   *
   * @throws NoSuchAlgorithmException
   */
  public void seedData() {

    hardwareRepository.deleteAll();

    addHardware("Hardware#1", "Description#1");
    addHardware("Hardware#2", "Description#2");
    addHardware("Hardware#3", "Description#3");
    addHardware("Hardware#4", "Description#4");
    if (MOKE_SHOW_SEED_DATA_ADD) {
      // Fetch all Records
      System.out.println("Hardware found with findAll():");
      for (Hardware hardware : hardwareRepository.findAll()) {
        System.out.println(String.format("  hardware: [%s]", hardware.getName()));
      }
      System.out.println();
    }
  }

  /**
   * Add model Helper
   *
   * @param name
   * @param description
   * @return
   */
  public Hardware addHardware(String name, String description) {

    if (hardwareRepository.findByName(name) != null) {
      return hardwareRepository.findByName(name);
    } else {
      Hardware hardware = new Hardware(name, description);
      hardwareRepository.save(hardware);
      return hardware;
    }
  }

  public List<Hardware> findAll() {
    return hardwareRepository.findAll();
  }

  public Optional<Hardware> findOne(String id) {
    return Optional.of(hardwareRepository.findOne(id));
  }

  public Optional<Hardware> findByName(String name) {
    return Optional.of(hardwareRepository.findByName(name));
  }
}
