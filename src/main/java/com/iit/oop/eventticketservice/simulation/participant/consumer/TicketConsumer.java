package com.iit.oop.eventticketservice.simulation.participant.consumer;

import com.iit.oop.eventticketservice.event.observer.DomainEventObserver;
import com.iit.oop.eventticketservice.event.subject.DomainEventPublisher;
import com.iit.oop.eventticketservice.event.subject.Subject;
import com.iit.oop.eventticketservice.model.Customer;
import com.iit.oop.eventticketservice.model.Ticket;
import com.iit.oop.eventticketservice.model.Transaction;
import com.iit.oop.eventticketservice.simulation.TicketPool;
import com.iit.oop.eventticketservice.simulation.Timer;
import com.iit.oop.eventticketservice.simulation.participant.AbstractTicketHandler;
import com.iit.oop.eventticketservice.util.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * A TicketConsumer is a Runnable that consumes tickets at a random interval.
 */
public class TicketConsumer extends AbstractTicketHandler implements Consumer {
    private static final Logger log = LoggerFactory.getLogger(TicketConsumer.class);
    private final TicketPool ticketPool;
    private final Customer customer;
    private final Timer timer;
    private final Subject<Transaction> transactionSubject;

    public TicketConsumer(Timer timer, TicketPool ticketPool, Customer customer) {
        this.ticketPool = ticketPool;
        this.customer = customer;
        this.timer = timer;
        this.transactionSubject = new DomainEventPublisher<>();
    }

    /**
     * Initialize the observers.
     */
    @Override
    public void setObservers(List<DomainEventObserver<Transaction>> observers) {
        for (DomainEventObserver<Transaction> observer : observers) {
            transactionSubject.addObserver(observer);
        }
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
                notifyTransactionObservers(ticket);
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
    private void notifyTransactionObservers(Ticket ticket) {
        Transaction transaction = new Transaction(customer, ticket, 1, ticket.getPrice());
        transactionSubject.notifyObservers(transaction);
    }
}
