package com.koakh.mqtt.moquette.broker.service;

import com.koakh.mqtt.moquette.broker.config.ConfigProperties;
import com.koakh.mqtt.moquette.broker.enums.AclMode;
import com.koakh.mqtt.moquette.broker.enums.AclPrivileges;
import com.koakh.mqtt.moquette.broker.models.Acl;
import com.koakh.mqtt.moquette.broker.models.User;
import com.koakh.mqtt.moquette.broker.repository.AclRepository;
import io.moquette.spi.impl.subscriptions.Token;
import io.moquette.spi.impl.subscriptions.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import static com.koakh.mqtt.moquette.broker.config.Constants.FORMAT_MAIN_PATTERN;
import static com.koakh.mqtt.moquette.broker.config.Constants.MOKE_SHOW_SEED_DATA_ADD;

@Service
public class AclService {

  private static final Logger LOG = LoggerFactory.getLogger(AclService.class);
  private static final boolean DEBUG = false;
  private static String MATCHED_RULE_FORMAT = "Matched Rule in Acl [{}]";

  private AclRepository aclRepository;
  private UserService userService;
  private ConfigProperties config;

  @Autowired
  public AclService(AclRepository aclRepository, UserService userService, ConfigProperties config) {
    this.aclRepository = aclRepository;
    this.userService = userService;
    this.config = config;
  }

  /**
   * SeedData
   *
   * @throws NoSuchAlgorithmException
   */
  public void seedData() throws NoSuchAlgorithmException {

    try {
      aclRepository.deleteAll();
      // Global
      // patern readwrite acme/%u/#
      addRule(AclMode.PATTERN, AclPrivileges.READWRITE, String.format("%s/%%u/#", config.getTopicBasePrefix()));
      // patern readwrite acme/%u/%c/# (user before client, one use can have many clients)
      addRule(AclMode.PATTERN, AclPrivileges.READWRITE, String.format("%s/%%u/%%c/#", config.getTopicBasePrefix()));
      // pattern write $SYS/broker/connection/%c/state WITHOUT #
      addRule(AclMode.PATTERN, AclPrivileges.WRITE, "$SYS/broker/connection/%c/state");
      // topic read $SYS/#
      addRule(AclMode.PATTERN, AclPrivileges.READ, "$SYS/#");
      // User
      //addRule(composeUserTopicRule(MOKE_USER_1, "esp8266-dev1/#"), userService.getByUserName(MOKE_USER_1));
      for (String user : config.getMokeUsers()) {
        addRule(composeUserTopicRule(user, String.format("%s/esp8266/#", user)), userService.getByUserName(user));
      }
      if (MOKE_SHOW_SEED_DATA_ADD) {
        // Fetch all Records
        System.out.println("Acl found with findAll():");
        for (Acl acl : aclRepository.findAll()) {
          System.out.println(String.format("  rule: [%s]", acl.getRule()));
        }
        System.out.println();
      }
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
  }

  /**
   * Add Global Pattern Rules
   *
   * @param aclMode
   * @param privileges
   * @param topic
   * @return
   * @throws NoSuchAlgorithmException
   */
  private Acl addRule(AclMode aclMode, AclPrivileges privileges, String topic) throws NoSuchAlgorithmException {

    try {
      String rule = String.format(FORMAT_MAIN_PATTERN, aclMode.toString(), privileges.toString(), topic);
      return addRule(rule, null);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }

    return null;
  }

  /**
   * Add User Topic Rule Helper, to add Rules to Repo
   *
   * @param rule
   * @param user
   * @return
   * @throws NoSuchAlgorithmException
   */
  private Acl addRule(String rule, User user) throws NoSuchAlgorithmException {

    try {
      if (aclRepository.findAclByRule(rule) != null) {
        return aclRepository.findAclByRule(rule);
      } else {
        Acl acl = new Acl(rule);
        if (user != null) acl.setUser(user);
        aclRepository.save(acl);
        return acl;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  /**
   * Helper to compose user topic for moke data
   *
   * @param userName
   * @param subTopic
   * @return
   */
  private String composeUserTopicRule(String userName, String subTopic) {
    return String.format("topic %s/%s", config.getTopicBasePrefix(), subTopic);
  }

  /**
   * Proxy Method to get Privileges for canRead and canWrite
   *
   * @param topic
   * @param userName
   * @param client
   */
  public boolean canReadWriteTopic(Topic topic, String userName, String client, AclPrivileges privileges) {

    // Check Patterns Rules
    if (checkGlobalPatternRules(topic, userName, client, privileges)) return true;
    // Check Topic Rules
    return checkUserTopicRules(topic, userName, client);

  }

  /**
   * Process GLOBAL pattern rules
   *
   * @param topic
   * @param userName
   * @param client
   * @param privileges
   * @return
   */
  private boolean checkGlobalPatternRules(Topic topic, String userName, String client, AclPrivileges privileges) {

    String startRule1 = String.format("%s %s ", AclMode.PATTERN, AclPrivileges.READWRITE.toString());
    String startRule2 = String.format("%s %s ", AclMode.PATTERN, privileges.toString());
    List<Acl> acl = aclRepository.findAclByRuleStartingWithOrRuleStartingWith(startRule1, startRule2);

    for (Acl currentAcl : acl) {
      // Remove [pattern read|write|readwrite ] from acl Rule
      String topicMatcher = currentAcl.getRule().replace(startRule1, "").replace(startRule2, "");
      // Replace Client and Username with Placeholders [acme/kapa/esp8266-dev1] to [acme/%u/esp8266-dev1] or [acme/%c/%u/esp8266-dev1]
      String topicWithPlaceHolders = topic.toString().replace(client, "%c").replace(userName, "%u");
      if (DEBUG)
        LOG.info(String.format("topicMatcher: [%s], topicWithPlaceHolders: [%s]", topicMatcher, topicWithPlaceHolders));

      if (topicMatcher.endsWith("/#")) {
        String matchRule = topicMatcher.replace("/#", "");
        if (topicWithPlaceHolders.startsWith(matchRule)) {
          if (DEBUG) LOG.info(MATCHED_RULE_FORMAT, matchRule);
          return true;
        }
      } else {
        if (topicWithPlaceHolders.equals(topicMatcher)) {
          if (DEBUG) LOG.info(MATCHED_RULE_FORMAT, topicMatcher);
          return true;
        }
      }
    }

    // No Matched Rule
    return false;
  }

  /**
   * Process USER Topic Rules
   *
   * @param topic
   * @param userName
   * @param client
   * @return
   */
  private boolean checkUserTopicRules(Topic topic, String userName, String client) {

    // Get User
    User user = userService.getByUserName(userName);
    // get list of tokens
    List<Token> tokenList = topic.getTokens();

    // Compose rule, one level, after the other
    String composedRule = String.format("%s ", AclMode.TOPIC.toString());
    boolean isFirstToken = true;
    for (Token token : tokenList) {
      composedRule = composedRule.concat(String.format("%s%s", (isFirstToken ? "" : "/"), token.toString()));
      isFirstToken = false;
      // Rule Must end with /#
      String searchRule = composedRule.concat("/#");
      if (DEBUG) LOG.debug("Try match Rule [{}]", searchRule);
      List<Acl> acl = aclRepository.findAclByRuleIsStartingWithAndUserId(searchRule, user.getId());
      // Check if Match Rule, if not first one, this is always true
      if (acl.toArray().length > 0) {
        if (DEBUG) LOG.info(MATCHED_RULE_FORMAT, searchRule);
        return true;
      }
    }
    // No Matched Rule
    return false;
  }

  /**
   *
   * @param acl
   * @param rule
   * @return
   */
  //private boolean checkRule(List<Acl> acl, String rule) {
  //
  //  // Check if Acl has any rule starter by Topic or Patern
  //  for (Acl currentAcl : acl) {
  //    if (currentAcl.getRule().startsWith(rule)) {
  //      LOG.info("Matched [{}] Rule in Acl [{}]", currentAcl.getRule());
  //      return true;
  //    }
  //  }
  //  return false;
  //}
}
