package com.uow.eventticketservice.service.cli.process.simulation;

import com.uow.eventticketservice.core.ticket.TicketCounter;
import com.uow.eventticketservice.service.cli.process.Process;
import com.uow.eventticketservice.util.shell.ShellLogger;

public class SimulationStats implements Process {
    TicketCounter ticketCounter;
    ShellLogger logger;

    public SimulationStats(TicketCounter ticketCounter, ShellLogger logger) {
        this.ticketCounter = ticketCounter;
        this.logger = logger;
    }

    @Override
    public void execute() {
        logger.verbose("Total tickets produced: " + ticketCounter.getProducedTicketCount());
        logger.verbose("Total tickets consumed: " + ticketCounter.getConsumedTicketCount());
    }
}