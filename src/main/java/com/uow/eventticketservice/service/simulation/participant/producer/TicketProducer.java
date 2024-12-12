package com.uow.eventticketservice.service.simulation.participant.producer;

import com.uow.eventticketservice.model.Ticket;
import com.uow.eventticketservice.model.Vendor;
import com.uow.eventticketservice.core.ticket.TicketPool;
import com.uow.eventticketservice.service.simulation.Timer;
import com.uow.eventticketservice.service.simulation.participant.AbstractParticipant;
import com.uow.eventticketservice.util.Global;


/**
 * A TicketProducer is a Runnable that produces tickets at a random interval.
 */
public class TicketProducer extends AbstractParticipant<String> implements Producer {
    private final TicketPool ticketPool;
    private final Timer timer;
    private final Vendor vendor;
    private final Ticket ticket;

    public TicketProducer(Timer timer, TicketPool ticketPool, Vendor vendor, Ticket ticket) {
        this.timer = timer;
        this.ticketPool = ticketPool;
        this.vendor = vendor;
        this.ticket = ticket;
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
                notifyObservers(Global.TICKET_PRODUCED); // Notify observers
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
