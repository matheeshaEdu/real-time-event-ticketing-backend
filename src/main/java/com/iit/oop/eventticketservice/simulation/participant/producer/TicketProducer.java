package com.iit.oop.eventticketservice.simulation.participant.producer;

import com.iit.oop.eventticketservice.event.observer.TicketThresholdMonitor;
import com.iit.oop.eventticketservice.event.subject.DomainEventPublisher;
import com.iit.oop.eventticketservice.event.subject.Subject;
import com.iit.oop.eventticketservice.model.Ticket;
import com.iit.oop.eventticketservice.model.Vendor;
import com.iit.oop.eventticketservice.simulation.participant.AbstractTicketHandler;
import com.iit.oop.eventticketservice.simulation.TicketPool;
import com.iit.oop.eventticketservice.simulation.Timer;
import com.iit.oop.eventticketservice.util.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A TicketProducer is a Runnable that produces tickets at a random interval.
 */
public class TicketProducer extends AbstractTicketHandler implements Producer, Runnable {
    private static final Logger log = LoggerFactory.getLogger(TicketProducer.class);
    private final TicketPool ticketPool;
    private final Timer timer;
    private final Vendor vendor;
    private final Ticket ticket;
    private final Subject<Integer> subject;

    public TicketProducer(Timer timer, TicketPool ticketPool, Vendor vendor, Ticket ticket) {
        this.timer = timer;
        this.ticketPool = ticketPool;
        this.vendor = vendor;
        this.ticket = ticket;
        this.subject = new DomainEventPublisher<>();

        initObservers();
    }

    /**
     * Initialize the observers.
     */
    public void initObservers() {
        TicketThresholdMonitor ticketThresholdMonitor = new TicketThresholdMonitor();
        this.subject.addObserver(ticketThresholdMonitor);
    }

    /**
     * Produce a ticket and add it to the pool.
     */
    @Override
    public void produce() {
        ticketPool.addTicket(ticket);
        subject.notifyObservers(1);
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
