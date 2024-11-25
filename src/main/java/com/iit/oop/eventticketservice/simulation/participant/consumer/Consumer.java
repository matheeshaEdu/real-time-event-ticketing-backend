package com.iit.oop.eventticketservice.simulation.participant.consumer;

import com.iit.oop.eventticketservice.event.Observerable;
import com.iit.oop.eventticketservice.model.Ticket;
import com.iit.oop.eventticketservice.model.Transaction;

public interface Consumer extends Runnable, Observerable<Transaction> {
    Ticket consume();
}

