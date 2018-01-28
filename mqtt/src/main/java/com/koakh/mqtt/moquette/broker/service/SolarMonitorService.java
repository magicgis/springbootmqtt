package com.koakh.mqtt.moquette.broker.service;

import com.koakh.mqtt.moquette.broker.models.SolarMonitor;
import com.koakh.mqtt.moquette.broker.repository.SolarMonitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SolarMonitorService {

  private SolarMonitorRepository solarMonitorRepository;

  @Autowired
  public SolarMonitorService(SolarMonitorRepository solarMonitorRepository) {
    this.solarMonitorRepository = solarMonitorRepository;
  }

  public SolarMonitor addSolarMonitor(SolarMonitor solarMonitor) {

    try {
      return solarMonitorRepository.save(solarMonitor);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
