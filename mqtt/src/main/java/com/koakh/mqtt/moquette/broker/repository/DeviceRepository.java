package com.koakh.mqtt.moquette.broker.repository;

import com.koakh.mqtt.moquette.broker.models.Device;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeviceRepository extends MongoRepository<Device, String> {

  Device findByDeviceId(String deviceId);

  Device findByFriendlyName(String friendlyName);
}
