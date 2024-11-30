package com.iit.oop.eventticketservice.event.observer;

import com.iit.oop.eventticketservice.model.TicketConfig;
import com.iit.oop.eventticketservice.service.limiter.TicketCounter;
import com.iit.oop.eventticketservice.simulation.Simulator;
import com.iit.oop.eventticketservice.simulation.TicketPool;
import com.iit.oop.eventticketservice.util.shell.ShellLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class ConfigObserver implements DomainEventObserver<TicketConfig> {
    private static final Logger log = LoggerFactory.getLogger(ConfigObserver.class);
    private final Lock lock = new ReentrantLock();
    private final ShellLogger shellLogger = ShellLogger.getInstance();
    private final Simulator simulator;

    @Autowired
    public ConfigObserver(Simulator simulator) {
        this.simulator = simulator;
    }

    @Override
    public void onDomainEvent(TicketConfig domainObject) {
        restartSimulation(domainObject);
    }

    private void restartSimulation(TicketConfig domainObject) {
        lock.lock();
        try {
            if (simulator.isRunning()) {
                var msg = "Ticket configuration change detected. Restarting simulation...";
                log.info(msg);
                shellLogger.notice(msg);
                // stop the current simulation and start a new one
                simulator.stop();
                // reset ticket counter and ticket pool configuration
                resetTicketCounter(domainObject);
                resetTicketPool(domainObject);

                simulator.simulate();
            }
        } finally {
            lock.unlock();
        }
    }

    private void resetTicketPool(TicketConfig config) {
        TicketPool.getInstance().reset(config);
    }

    private void resetTicketCounter(TicketConfig config) {
        TicketCounter.getInstance().reset(config);
    }
}