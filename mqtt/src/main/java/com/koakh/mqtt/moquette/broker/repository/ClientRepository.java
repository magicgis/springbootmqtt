package com.koakh.mqtt.moquette.broker.repository;

import com.koakh.mqtt.moquette.broker.models.Client;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClientRepository extends MongoRepository<Client, String> {

  Client findByClientId(String clientId);
}
