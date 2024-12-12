package com.uow.eventticketservice.service.simulation.participant.consumer;

import com.uow.eventticketservice.model.Customer;
import com.uow.eventticketservice.model.Ticket;
import com.uow.eventticketservice.model.Transaction;
import com.uow.eventticketservice.core.ticket.TicketPool;
import com.uow.eventticketservice.service.simulation.Timer;
import com.uow.eventticketservice.service.simulation.participant.AbstractParticipant;
import com.uow.eventticketservice.util.Global;



/**
 * A TicketConsumer is a Runnable that consumes tickets at a random interval.
 */
public class TicketConsumer extends AbstractParticipant<Transaction> implements Consumer {
    private final TicketPool ticketPool;
    private final Customer customer;
    private final Timer timer;

    public TicketConsumer(Timer timer, TicketPool ticketPool, Customer customer) {
        this.ticketPool = ticketPool;
        this.customer = customer;
        this.timer = timer;
    }

    /**
     * Consume a ticket from the pool.
     *
     * @return the consumed ticket
     */
    @Override
    public Ticket consume() {
        Ticket ticket = ticketPool.getTicket();
        if (ticket == null) {
            log.warn("No ticket retrieved for customer {} due to interruption.", customer.getName());
            return null;
        }
        return ticket;
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

                Ticket ticket = consume();
                if (ticket == null) {
                    continue;
                }
                notifyObservers(ticket);
                log.info("Customer {} | consumed ticket: {}", customer.getName(), ticket.getName());

                // Calculate the remaining time to wait
                long end = System.currentTimeMillis();
                long remainingWait = Math.max(0, Global.CONSUME_TIME - (end - start));

                delayFor(remainingWait);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Notify the transaction observers.
     *
     * @param ticket the ticket to notify
     */
    private void notifyObservers(Ticket ticket) {
        Transaction transaction = new Transaction(customer, ticket, 1, ticket.getPrice());
        notifyObservers(transaction);
    }
}
