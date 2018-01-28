package com.koakh.mqtt.moquette.api.controller;

import com.koakh.mqtt.moquette.broker.models.Hardware;
import com.koakh.mqtt.moquette.datalibrary.service.HardwareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class HardwareController {

  private HardwareService hardwareService;

  @Autowired
  public HardwareController(HardwareService hardwareService) {

    this.hardwareService = hardwareService;
  }

  /**
   * Get Hardware Endpoint
   *
   * @param id
   * @return
   * @see http://localhost:8084/hardware/5a517957682200d685c3b3ad
   */
  @RequestMapping("/hardware/{id}")
  public ResponseEntity<Hardware> hardware(@PathVariable("id") String id) {

    Optional<Hardware> hardwareOptional = hardwareService.findOne(id);

    if (hardwareOptional.isPresent()) {
      return new ResponseEntity<Hardware>(hardwareOptional.get(), HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }
}
