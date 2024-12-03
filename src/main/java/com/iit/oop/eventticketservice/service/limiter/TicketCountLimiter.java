package com.iit.oop.eventticketservice.service.limiter;

import com.iit.oop.eventticketservice.simulation.participant.ParticipantHandler;
import com.iit.oop.eventticketservice.util.shell.ShellLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TicketCountLimiter implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(TicketCountLimiter.class);
    private final TicketCounter ticketCounter;
    private final ShellLogger logger;
    private final ParticipantHandler participantHandler;

    public TicketCountLimiter(ParticipantHandler participantHandler,TicketCounter ticketCounter, ShellLogger logger) {
        this.participantHandler = participantHandler;
        this.ticketCounter = ticketCounter;
        this.logger = logger;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            if (!ticketCounter.isProducedBelowLimit()) {
                log.warn("Ticket count has reached the total limit");
                logger.warn("Ticket count has reached the total limit");
                stopSimulation();
                break;
            }
            try {
                Thread.sleep(800); // Sleep for 800 millisecond before checking again
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("TicketCountLimiter thread was interrupted {}", e.getMessage());
            }
        }
    }

    private void stopSimulation() {
        logger.fatal("Ticket count has reached the total ticket limit. Stopping the simulation...");
        this.participantHandler.stopAll();
    }
}
