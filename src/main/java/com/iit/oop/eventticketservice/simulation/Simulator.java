package com.iit.oop.eventticketservice.simulation;

import com.iit.oop.eventticketservice.exception.ConfigNotFoundException;
import com.iit.oop.eventticketservice.model.TicketConfig;
import com.iit.oop.eventticketservice.service.config.ConfigManager;
import com.iit.oop.eventticketservice.service.limiter.TicketCountLimiter;
import com.iit.oop.eventticketservice.service.limiter.TicketCounter;
import com.iit.oop.eventticketservice.simulation.data.DataInit;
import com.iit.oop.eventticketservice.simulation.participant.ParticipantHandler;
import com.iit.oop.eventticketservice.util.shell.ShellLogger;
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
    private final ShellLogger shellLogger;
    private final TicketCounter ticketCounter;
    private Thread ticketCountLimiter;

    @Autowired
    public Simulator(DataInit dataInit) {
        dataInit.init();
        this.shellLogger = ShellLogger.getInstance();
        this.ticketCounter = TicketCounter.getInstance();
        this.participantHandler = new ParticipantHandler(ticketCounter);
    }

    /**
     * Simulate vendor and customer interactions
     */
    public void simulate() {
        TicketConfig config = ConfigManager.getInstance().getConfig();
        int buyingRate;
        int sellingRate;
        try {
            buyingRate = config.getCustomerRetrievalRate();
            sellingRate = config.getTicketReleaseRate();
        } catch (ConfigNotFoundException e) {
            log.error("Error while retrieving configuration: {}", e.getMessage());
            shellLogger.fatal("Could not start simulation | Error while retrieving configuration: " + e.getMessage());
            return;
        }
        startTicketLimiter();
        participantHandler.startSimulation(sellingRate, buyingRate);
    }

    private void startTicketLimiter() {
        // start ticket count limiter
        ticketCountLimiter = new Thread(new TicketCountLimiter(
                participantHandler, ticketCounter, shellLogger
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
