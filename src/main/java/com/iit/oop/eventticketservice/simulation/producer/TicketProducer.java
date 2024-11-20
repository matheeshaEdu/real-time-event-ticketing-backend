package com.iit.oop.eventticketservice.simulation.producer;

import com.iit.oop.eventticketservice.model.Ticket;
import com.iit.oop.eventticketservice.model.Vendor;
import com.iit.oop.eventticketservice.simulation.AbstractTicketHandler;
import com.iit.oop.eventticketservice.simulation.TicketPool;
import com.iit.oop.eventticketservice.simulation.Timer;
import com.iit.oop.eventticketservice.util.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A TicketProducer is a Runnable that produces tickets at a random interval.
 */
public class TicketProducer extends AbstractTicketHandler implements Producer, Runnable {
    private static final TicketPool ticketPool = TicketPool.getInstance();
    private static final Logger log = LoggerFactory.getLogger(TicketProducer.class);
    private final Timer timer = new Timer();
    private final Vendor vendor;
    private final Ticket ticket;

    public TicketProducer(Vendor vendor, Ticket ticket) {
        this.vendor = vendor;
        this.ticket = ticket;
    }

    /**
     * Produce a ticket and add it to the pool.
     */
    @Override
    public void produce() {
        ticketPool.addTicket(ticket);
        log.info("Vendor {} | added ticket: {}", vendor.getName(), ticket);
    }

    /**
     * Run the producer thread.
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            long start = System.currentTimeMillis();
            try {
                // Generate a random delay between 0 and PRODUCE_TIME
                int interval = timer.getRandomDelay(Global.PRODUCE_TIME);
                delayFor(interval);
                // Produce a ticket
                produce();
                log.info("Vendor {} | produced ticket: {}", vendor.getName(), ticket);
                // Calculate the remaining time to wait
                long end = System.currentTimeMillis();
                long remainingWait = Math.max(0, Global.PRODUCE_TIME - (end - start));
                // Delay for the remaining time
                delayFor(remainingWait);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
