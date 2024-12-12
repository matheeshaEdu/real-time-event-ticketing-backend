package com.uow.eventticketservice.event.observer;

import com.uow.eventticketservice.exception.MaxThreadCountExceed;
import com.uow.eventticketservice.model.TicketConfig;
import com.uow.eventticketservice.core.ticket.TicketCounter;
import com.uow.eventticketservice.service.simulation.Simulator;
import com.uow.eventticketservice.core.ticket.TicketPool;
import com.uow.eventticketservice.util.log.DualLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Observer for the TicketConfig domain object.
 */
@Component
public class ConfigObserver implements DomainEventObserver<TicketConfig> {
    private static final Logger log = LoggerFactory.getLogger(ConfigObserver.class);
    private final DualLogger dualLogger;
    private final Simulator simulator;

    @Autowired
    public ConfigObserver(Simulator simulator) {
        this.simulator = simulator;
        this.dualLogger = new DualLogger(log);
    }

    /**
     * Reset the ticket counter and ticket pool configuration when the TicketConfig domain object changes.
     *
     * @param domainObject the TicketConfig domain object
     */
    @Override
    public void onDomainEvent(TicketConfig domainObject) {
        var hadSimulation = stopSimulation();
        // reset ticket counter and ticket pool configuration
        resetTicketCounter(domainObject);
        resetTicketPool(domainObject);
        if (hadSimulation) {
            startSimulation();
        }
    }

    /**
     * Stop the current simulation.
     * @return true if the simulation was stopped, false otherwise
     */
    private boolean stopSimulation() {
        if (simulator.isRunning()) {
            dualLogger.logAndInfo("Stopping the current simulation...");
            // stop the current simulation and start a new one
            simulator.stop();
            return true;
        }
        return false;
    }

    /**
     * Start the simulation.
     */
    private void startSimulation() {
        if (!simulator.isRunning()) {
            try {
                simulator.simulate();
            } catch (MaxThreadCountExceed e) {
                // if the maximum thread count is exceeded, log the error and
                // stop the current simulation
                if (simulator.isRunning()) {
                    simulator.stop();
                }
            }
        }
    }

    /**
     * Reset the ticket pool configuration.
     *
     * @param config the TicketConfig object
     */
    private void resetTicketPool(TicketConfig config) {
        TicketPool.getInstance().reset(config.getMaxTicketCapacity());
    }

    /**
     * Reset the ticket counter configuration.
     *
     * @param config the TicketConfig object
     */
    private void resetTicketCounter(TicketConfig config) {
        TicketCounter.getInstance().reset(config);
    }
}