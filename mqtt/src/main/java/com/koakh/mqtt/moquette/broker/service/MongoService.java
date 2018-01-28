package com.koakh.mqtt.moquette.broker.service;

import com.mongodb.Mongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.stereotype.Service;

@Service
public class MongoService {

  private Mongo mongo;
  private MongoDbFactory mongoDbFactory;

  @Autowired
  public MongoService(Mongo mongo, MongoDbFactory mongoDbFactory) {
    this.mongo = mongo;
    this.mongoDbFactory = mongoDbFactory;
  }

  public void dropDatabase() {
    if (mongoDbFactory.getDb() != null) {
      mongo.dropDatabase(mongoDbFactory.getDb().getName());
    }
  }
}
