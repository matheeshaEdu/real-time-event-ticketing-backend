package com.iit.oop.eventticketservice.simulation.consumer;

import com.iit.oop.eventticketservice.model.Customer;
import com.iit.oop.eventticketservice.model.Ticket;
import com.iit.oop.eventticketservice.simulation.AbstractTicketHandler;
import com.iit.oop.eventticketservice.simulation.TicketPool;
import com.iit.oop.eventticketservice.simulation.Timer;
import com.iit.oop.eventticketservice.util.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A TicketConsumer is a Runnable that consumes tickets at a random interval.
 */
public class TicketConsumer extends AbstractTicketHandler implements Consumer, Runnable {
    private static final TicketPool ticketPool = TicketPool.getInstance();
    private static final Logger log = LoggerFactory.getLogger(TicketConsumer.class);
    private final Customer customer;
    private final Timer timer = new Timer();

    public TicketConsumer(Customer customer) {
        this.customer = customer;
    }

    /**
     * Consume a ticket from the pool.
     *
     * @return the consumed ticket
     */
    @Override
    public Ticket consume() {
        return ticketPool.getTicket();
    }

    /**
     * Run the consumer thread.
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            long start = System.currentTimeMillis();
            try {
                // Generate a random delay between 0 and PRODUCE_TIME
                int interval = timer.getRandomDelay(Global.CONSUME_TIME);
                delayFor(interval);
                // Produce a ticket
                consume();
                log.info("Customer {} | consumed ticket: {}", customer.getName(), ticketPool.getTicket());
                // Calculate the remaining time to wait
                long end = System.currentTimeMillis();
                long remainingWait = Math.max(0, Global.CONSUME_TIME - (end - start));
                // Delay for the remaining time
                delayFor(remainingWait);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
