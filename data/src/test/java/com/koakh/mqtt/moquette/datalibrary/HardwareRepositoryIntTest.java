package com.koakh.mqtt.moquette.datalibrary;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Testing in Spring Boot
 * http://www.baeldung.com/spring-boot-testing
 *
 * @RunWith(SpringRunner.class) is used to provide a bridge between Spring Boot test features and JUnit.
 * Whenever we are using any Spring Boot testing features in out JUnit tests, this annotation will be required.
 * @DataJpaTest provides some standard setup needed for testing the persistence layer:
 */

@RunWith(SpringRunner.class)
@DataJpaTest
public class HardwareRepositoryIntTest {

  @Autowired
  private TestEntityManager entityManager;

  //@Autowired
  //private HardwareRepository hardwareRepository;

  //@Autowired
  //private HardwareService hardwareService;

  @Test
  public void whenFindByName_thenReturnEmployee() {

    //// given
    //Hardware hardware = new Hardware("Hardware#1", "Description#1");
    //entityManager.persist(hardware);
    //entityManager.flush();
    //
    //// when
    //Hardware found = hardwareRepository.findByName(hardware.getName());
    //
    ////import static org.junit.Assert.assertEquals;
    //
    //// then
    //assertThat(found.getName())
    //  .isEqualTo(hardware.getName());
  }
}
