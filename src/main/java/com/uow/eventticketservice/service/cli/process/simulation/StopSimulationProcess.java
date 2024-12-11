package com.uow.eventticketservice.service.cli.process.simulation;

import com.uow.eventticketservice.service.cli.process.Process;
import com.uow.eventticketservice.service.simulation.Simulator;
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
