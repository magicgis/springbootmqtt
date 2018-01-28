package com.koakh.mqtt.moquette.datalibrary.service;

import com.koakh.mqtt.moquette.broker.models.Hardware;
import com.koakh.mqtt.moquette.datalibrary.repository.HardwareRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class HardwareServiceTest {

  private HardwareService hardwareService;

  @Mock
  private HardwareRepository hardwareRepository;

  @Before
  public void setUp() throws Exception {

    MockitoAnnotations.initMocks(this);

    hardwareService = new HardwareService(hardwareRepository);
  }

  @Test
  public void seedDataHardware() throws Exception {

    //given

    //when

    //then
  }


  @Test
  public void findAllHardware() throws Exception {

    //given
    ArrayList<Hardware> hardwareSet = new ArrayList<>();
    Hardware hardware1 = new Hardware("hardware#1", "description#1");
    hardwareSet.add(hardware1);

    Hardware hardware2 = new Hardware("hardware#2", "description#2");
    hardwareSet.add(hardware2);

    when(hardwareRepository.findAll()).thenReturn(hardwareSet);

    //when
    List<Hardware> hardwareFound = hardwareService.findAll();

    //then
    assertEquals(2, hardwareFound.size());
    verify(hardwareRepository, times(1)).findAll();
  }

  @Test
  public void findOneHardware() throws Exception {

    //given
    Hardware hardware = new Hardware("hardware#1", "description#1");

    when(hardwareRepository.findOne(hardware.getId())).thenReturn(hardware);

    //when
    Hardware hardwareFound = hardwareService.findOne(hardware.getId()).get();

    //then
    assertEquals(hardware.getId(), hardwareFound.getId());
    assertEquals(hardware.getUuid(), hardwareFound.getUuid());
    assertEquals(hardware.getName(), hardwareFound.getName());
    assertEquals(hardware.getDescription(), hardwareFound.getDescription());
    verify(hardwareRepository, times(1)).findOne(hardware.getId());
  }

  @Test
  public void findByNameHardware() throws Exception {

    //given
    Hardware hardware = new Hardware("hardware#1", "description#1");

    when(hardwareRepository.findByName(hardware.getName())).thenReturn(hardware);

    //when
    Hardware hardwareFound = hardwareService.findByName(hardware.getName()).get();

    //then
    assertEquals(hardware.getName(), hardwareFound.getName());
    verify(hardwareRepository, times(1)).findByName(hardware.getName());
  }

  @Test
  public void addHardwareIfNotExist() throws Exception {

    //given
    Hardware hardware = new Hardware("hardware#1", "description#1");

    when(hardwareRepository.findByName(hardware.getName())).thenReturn(null);
    when(hardwareRepository.save(hardware)).thenReturn(hardware);

    //when
    Hardware hardwareFound = hardwareService.addHardware(hardware.getName(), hardware.getDescription());

    //then
    assertEquals(hardware.getName(), hardwareFound.getName());
    verify(hardwareRepository, times(1)).findByName(hardwareFound.getName());
    verify(hardwareRepository, times(1)).save(hardwareFound);
  }

  @Test
  public void addHardwareIfExist() throws Exception {

    //given
    Hardware hardware = new Hardware("hardware#1", "description#1");

    when(hardwareRepository.findByName(hardware.getName())).thenReturn(hardware);
    when(hardwareRepository.save(hardware)).thenReturn(hardware);

    //when
    Hardware hardwareFound = hardwareService.addHardware(hardware.getName(), hardware.getDescription());

    //then
    assertEquals(hardware.getName(), hardwareFound.getName());
    verify(hardwareRepository, times(2)).findByName(hardwareFound.getName());
    verify(hardwareRepository, times(0)).save(hardwareFound);
  }
}
