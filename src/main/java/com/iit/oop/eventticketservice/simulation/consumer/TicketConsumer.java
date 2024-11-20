package com.iit.oop.eventticketservice.simulation.consumer;

import com.iit.oop.eventticketservice.model.Customer;
import com.iit.oop.eventticketservice.model.Ticket;
import com.iit.oop.eventticketservice.simulation.TicketPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Time;

public class TicketConsumer implements Consumer, Runnable {
    private static final TicketPool ticketPool = TicketPool.getInstance();
    private static final long SLEEP_TIME = 60000;
    private static final Logger log = LoggerFactory.getLogger(TicketConsumer.class);
    private final Customer customer;

    public TicketConsumer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public Ticket consume() {
        return ticketPool.getTicket();
    }

    @Override
    public void run() {
        // start time
        Time startTime = new Time(System.currentTimeMillis());
        consume();
        // end time
        Time endTime = new Time(System.currentTimeMillis());
        // calculate time taken
        long timeTaken = endTime.getTime() - startTime.getTime();
        try {
            Thread.sleep(SLEEP_TIME - timeTaken);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("Customer {} buy a ticket", customer.getName());
    }
}
