package com.koakh.mqtt.moquette.broker.repository;

import com.koakh.mqtt.moquette.broker.models.Acl;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AclRepository extends MongoRepository<Acl, String> {

  Acl findAclByRule(String rule);

  List<Acl> findAclByRuleStartingWithOrRuleStartingWith(String rule1, String rule2);

  List<Acl> findAclByRuleIs(String rule);

  // Must Loop, [topic], [topic/kapa], [topic/kapa/esp8266-dev1] < Finded Here, all after is valid
  // ex
  // rule topic acme/kapa/esp8266-dev1
  // Subscribed
  //      topic acme/kapa/esp8266-dev1 < Valid
  //      topic acme/kapa/esp8266-dev1/subtopic/iot < Valid
  //      topic acme/kapa/esp8266-dev1/subtopic/led < Valid
  List<Acl> findAclByRuleIsStartingWithAndUserId(String rule, String userId);
}
