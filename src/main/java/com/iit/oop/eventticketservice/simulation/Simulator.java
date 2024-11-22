package com.iit.oop.eventticketservice.simulation;

import com.iit.oop.eventticketservice.exception.ConfigNotFoundException;
import com.iit.oop.eventticketservice.service.config.ConfigManager;
import com.iit.oop.eventticketservice.service.limiter.TicketCountLimiter;
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
    private final DataInit dataInit;
    private final ParticipantHandler participantHandler = new ParticipantHandler();
    private final ConfigManager configManager = ConfigManager.getInstance();
    private final ShellLogger shellLogger = ShellLogger.getInstance();

    @Autowired
    public Simulator(DataInit dataInit) {
        this.dataInit = dataInit;
    }

    /**
     * Simulate vendor and customer interactions
     */
    public void simulate() {
        int buyingRate;
        int sellingRate;
        try {
             buyingRate = configManager.getConfig().getCustomerRetrievalRate();
             sellingRate = configManager.getConfig().getTicketReleaseRate();
        } catch (ConfigNotFoundException e) {
            log.error("Error while retrieving configuration: {}", e.getMessage());
            shellLogger.fatal("Could not start simulation | Error while retrieving configuration: " + e.getMessage());
            return;
        }
        dataInit.init();
        startTicketLimiter();
        participantHandler.startSimulation(sellingRate, buyingRate);
    }

    private void startTicketLimiter() {
        // start ticket count limiter
        Thread ticketCountLimiter = new Thread(new TicketCountLimiter(participantHandler));
        ticketCountLimiter.setName("TicketCountLimiter");
        ticketCountLimiter.start();
    }

    public void stopSimulation() {
        participantHandler.stopAll();
    }
}
