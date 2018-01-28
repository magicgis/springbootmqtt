package com.koakh.mqtt.moquette.broker.repository;

import com.koakh.mqtt.moquette.broker.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

  User findById(String id);

  User findByUserName(String userName);
}
