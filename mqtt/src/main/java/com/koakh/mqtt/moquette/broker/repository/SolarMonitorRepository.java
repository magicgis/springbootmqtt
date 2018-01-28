package com.koakh.mqtt.moquette.broker.repository;

import com.koakh.mqtt.moquette.broker.models.SolarMonitor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
//@RepositoryRestResource(collectionResourceRel = "solarmonitor", path = "solarmonitor")
public interface SolarMonitorRepository extends MongoRepository<SolarMonitor, String> {

  List<SolarMonitor> findByUserId(@Param("userId") String userId);

  List<SolarMonitor> findByClientId(@Param("clientId") String clientId);

  List<SolarMonitor> findByDeviceId(@Param("deviceId") String hardwareId);
}
