package com.uow.eventticketservice.event.observer;

import com.uow.eventticketservice.model.TicketConfig;
import com.uow.eventticketservice.service.limiter.TicketCounter;
import com.uow.eventticketservice.simulation.Simulator;
import com.uow.eventticketservice.simulation.TicketPool;
import com.uow.eventticketservice.util.shell.ShellLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConfigObserver implements DomainEventObserver<TicketConfig> {
    private static final Logger log = LoggerFactory.getLogger(ConfigObserver.class);
    private final ShellLogger shellLogger = ShellLogger.getInstance();
    private final Simulator simulator;

    @Autowired
    public ConfigObserver(Simulator simulator) {
        this.simulator = simulator;
    }

    @Override
    public void onDomainEvent(TicketConfig domainObject) {
        stopSimulation();
        // reset ticket counter and ticket pool configuration
        resetTicketCounter(domainObject);
        resetTicketPool(domainObject);
        startSimulation();
    }

    private void stopSimulation() {
        if (simulator.isRunning()) {
            var msg = "Ticket configuration change detected. Stopping simulation...";
            log.info(msg);
            shellLogger.notice(msg);
            // stop the current simulation and start a new one
            simulator.stop();
            simulator.simulate();
        }
    }

    private void startSimulation() {
        if (!simulator.isRunning()) {
            simulator.simulate();
        }
    }

    private void resetTicketPool(TicketConfig config) {
        TicketPool.getInstance().reset(config);
    }

    private void resetTicketCounter(TicketConfig config) {
        TicketCounter.getInstance().reset(config);
    }
}