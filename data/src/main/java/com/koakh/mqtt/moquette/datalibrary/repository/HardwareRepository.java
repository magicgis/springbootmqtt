package com.koakh.mqtt.moquette.datalibrary.repository;

import com.koakh.mqtt.moquette.broker.models.Hardware;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HardwareRepository extends MongoRepository<Hardware, String> {
  Hardware findByName(String name);
}
