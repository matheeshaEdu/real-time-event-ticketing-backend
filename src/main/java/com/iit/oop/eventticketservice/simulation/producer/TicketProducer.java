package com.iit.oop.eventticketservice.simulation.producer;

import com.iit.oop.eventticketservice.model.Ticket;
import com.iit.oop.eventticketservice.model.Vendor;
import com.iit.oop.eventticketservice.simulation.TicketPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Time;

public class TicketProducer implements Producer, Runnable {
    private static final TicketPool ticketPool = TicketPool.getInstance();
    private static final Logger log = LoggerFactory.getLogger(TicketProducer.class);
    private static final long SLEEP_TIME = 60000;
    private final Vendor vendor;
    private final Ticket ticket;

    public TicketProducer(Vendor vendor, Ticket ticket) {
        this.vendor = vendor;
        this.ticket = ticket;
    }

    @Override
    public void produce() {
        ticketPool.addTicket(ticket);
        log.info("Vendor {} | added ticket: {}", vendor.getName(), ticket);
    }

    @Override
    public void run() {
        // start time
        Time startTime = new Time(System.currentTimeMillis());
        produce();
        // end time
        Time endTime = new Time(System.currentTimeMillis());
        // calculate time taken
        long timeTaken = endTime.getTime() - startTime.getTime();
        try {
            Thread.sleep(SLEEP_TIME - timeTaken);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
