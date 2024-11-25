package com.iit.oop.eventticketservice.cli.shellprocess.simulation;

import com.iit.oop.eventticketservice.cli.shellprocess.ShellProcess;
import com.iit.oop.eventticketservice.simulation.Simulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StopSimulationProcess implements ShellProcess {
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
