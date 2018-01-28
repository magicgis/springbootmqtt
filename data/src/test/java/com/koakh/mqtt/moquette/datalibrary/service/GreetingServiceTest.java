package com.koakh.mqtt.moquette.datalibrary.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * In the sample above we have configured the service.message for the test using the default attribute of
 * the @SpringBootTest annotation. It is not advisable to put application.properties in a library because
 * there might be a clash at runtime in the application that uses it (only one application.properties is ever
 * loaded from the classpath). You could put application.properties in the test classpath, but not include
 * it in the jar, for instance by placing it in src/test/resources.
 */
@RunWith(SpringRunner.class)
@SpringBootTest({"service.message=Hello"})
public class GreetingServiceTest {

  @Autowired
  private GreetingService greetingService;

  @Test
  public void contextLoads() {

    assertThat(greetingService.message())
      .isNotNull()
      .contains("Hello");
  }

  @SpringBootApplication
  static class TestConfiguration {
  }
}
