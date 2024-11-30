package com.iit.oop.eventticketservice.simulation.participant.consumer;

import com.iit.oop.eventticketservice.event.Observable;
import com.iit.oop.eventticketservice.model.Ticket;
import com.iit.oop.eventticketservice.model.Transaction;

public interface Consumer extends Runnable, Observable<Transaction> {
    Ticket consume();
}

