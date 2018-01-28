package com.koakh.mqtt.moquette.datalibrary.model;

import com.koakh.mqtt.moquette.broker.models.Hardware;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HardwareTest {

  private final String ID = "4e4e214e-d958-01b8-8ea4-36b0cb3810a9";
  private final String NAME = "name";
  private final String DESCRIPTION = "description";

  private Hardware hardware;

  @Before
  public void setUp() {

    hardware = new Hardware();
  }

  @Test
  public void getId() throws Exception {

    hardware.setId(ID);
    assertEquals(ID, hardware.getId());
  }

  @Test
  public void getName() throws Exception {

    hardware.setName(NAME);
    assertEquals(NAME, hardware.getName());
  }

  @Test
  public void getDescription() throws Exception {

    hardware.setDescription(DESCRIPTION);
    assertEquals(DESCRIPTION, hardware.getDescription());
  }
}