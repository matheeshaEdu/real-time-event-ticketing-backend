package com.uow.eventticketservice.controller.simulation;

import com.uow.eventticketservice.dto.response.ResponseMessageDto;
import com.uow.eventticketservice.simulation.Simulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/simulation")
public class SimulationController {
    Simulator simulator;

    @Autowired
    public SimulationController(Simulator simulator) {
        this.simulator = simulator;
    }

    @GetMapping("/start")
    public ResponseEntity<Object> startSimulation() {
        simulator.simulate();
        return ResponseEntity.ok(new ResponseMessageDto("200 OK", "Simulation started"));
    }

    @GetMapping("/stop")
    public ResponseEntity<Object> stopSimulation() {
        simulator.stop();
        return ResponseEntity.ok(new ResponseMessageDto("200 OK", "Simulation stopped"));
    }

    @GetMapping("/status")
    public ResponseEntity<Object> isRunning() {
        return ResponseEntity.ok(simulator.isRunning());
    }

}
