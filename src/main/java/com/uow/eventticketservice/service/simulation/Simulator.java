package com.uow.eventticketservice.service.simulation;

import com.uow.eventticketservice.exception.ConfigNotFoundException;
import com.uow.eventticketservice.exception.MaxThreadCountExceed;
import com.uow.eventticketservice.model.TicketConfig;
import com.uow.eventticketservice.core.config.ConfigManager;
import com.uow.eventticketservice.service.limiter.TicketCountLimiter;
import com.uow.eventticketservice.service.limiter.TicketCounter;
import com.uow.eventticketservice.service.simulation.data.DataInit;
import com.uow.eventticketservice.service.simulation.participant.ParticipantHandler;
import com.uow.eventticketservice.util.log.DualLogger;
import com.uow.eventticketservice.util.shell.ShellLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Simulate the event ticket service
 */
@Component
public class Simulator {
    private static final Logger log = LoggerFactory.getLogger(Simulator.class);
    private final ParticipantHandler participantHandler;
    private final DualLogger dualLogger;
    private final TicketCounter ticketCounter;
    private Thread ticketCountLimiter;

    @Autowired
    public Simulator(DataInit dataInit) {
        dataInit.init();
        this.dualLogger = new DualLogger(log);
        this.ticketCounter = TicketCounter.getInstance();
        this.participantHandler = new ParticipantHandler(ticketCounter);
    }

    /**
     * Simulate vendor and customer interactions
     */
    public void simulate() throws MaxThreadCountExceed {
        TicketConfig config = ConfigManager.getInstance().getConfig();
        int buyingRate;
        int sellingRate;
        try {
            buyingRate = config.getCustomerRetrievalRate();
            sellingRate = config.getTicketReleaseRate();
        } catch (ConfigNotFoundException e) {
            dualLogger.logAndFailure("Failed to retrieve configuration"+ e.getMessage());
            return;
        }
        startTicketLimiter();
        try {
            participantHandler.startSimulation(sellingRate, buyingRate);
        } catch (MaxThreadCountExceed e) {
            stopTicketLimiter();
            throw e;
        }
    }

    private void startTicketLimiter() {
        // start ticket count limiter
        ticketCountLimiter = new Thread(new TicketCountLimiter(
                participantHandler, ticketCounter, ShellLogger.getInstance()
        ));
        ticketCountLimiter.setName("TicketCountLimiter");
        ticketCountLimiter.start();
    }

    public void stop() {
        participantHandler.stopAll();
        stopTicketLimiter();
    }

    public boolean isRunning() {
        return participantHandler.isRunning();
    }

    private void stopTicketLimiter() {
        if (ticketCountLimiter != null) {
            ticketCountLimiter.interrupt();
        }
    }
}
