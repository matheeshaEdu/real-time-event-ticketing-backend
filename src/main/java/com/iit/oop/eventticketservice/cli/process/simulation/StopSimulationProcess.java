package com.iit.oop.eventticketservice.cli.process.simulation;

import com.iit.oop.eventticketservice.cli.process.Process;
import com.iit.oop.eventticketservice.simulation.Simulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StopSimulationProcess implements Process {
    private final Simulator simulator;

    @Autowired
    public StopSimulationProcess(Simulator simulator) {
        this.simulator = simulator;
    }

    @Override
    public void execute() {
        simulator.stop();
    }
}
