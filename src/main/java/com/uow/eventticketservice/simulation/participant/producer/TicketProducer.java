package com.uow.eventticketservice.simulation.participant.producer;

import com.uow.eventticketservice.event.observer.DomainEventObserver;
import com.uow.eventticketservice.event.subject.DomainEventPublisher;
import com.uow.eventticketservice.event.subject.Subject;
import com.uow.eventticketservice.model.Ticket;
import com.uow.eventticketservice.model.Vendor;
import com.uow.eventticketservice.simulation.TicketPool;
import com.uow.eventticketservice.simulation.Timer;
import com.uow.eventticketservice.simulation.participant.AbstractTicketHandler;
import com.uow.eventticketservice.util.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * A TicketProducer is a Runnable that produces tickets at a random interval.
 */
public class TicketProducer extends AbstractTicketHandler implements Producer {
    private static final Logger log = LoggerFactory.getLogger(TicketProducer.class);
    private final TicketPool ticketPool;
    private final Timer timer;
    private final Vendor vendor;
    private final Ticket ticket;
    private final Subject<String> subject;

    public TicketProducer(Timer timer, TicketPool ticketPool, Vendor vendor, Ticket ticket) {
        this.timer = timer;
        this.ticketPool = ticketPool;
        this.vendor = vendor;
        this.ticket = ticket;
        this.subject = new DomainEventPublisher<>();
    }

    /**
     * Initialize the observers.
     */
    @Override
    public void setObservers(List<DomainEventObserver<String>> observers) {
        for (DomainEventObserver<String> observer : observers) {
            subject.addObserver(observer);
        }
    }


    /**
     * Produce a ticket and add it to the pool.
     */
    @Override
    public void produce() {
        ticketPool.addTicket(ticket);
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
                subject.notifyObservers(Global.TICKET_PRODUCED); // Notify observers
                log.info("Vendor {} | produced ticket: {}", vendor.getName(), ticket);

                // Calculate the remaining time to wait
                long end = System.currentTimeMillis();
                long remainingWait = Math.max(0, Global.PRODUCE_TIME - (end - start));

                delayFor(remainingWait);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
