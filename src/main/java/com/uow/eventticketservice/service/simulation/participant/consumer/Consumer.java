package com.uow.eventticketservice.service.simulation.participant.consumer;

import com.uow.eventticketservice.event.Observable;
import com.uow.eventticketservice.model.Ticket;
import com.uow.eventticketservice.model.Transaction;

public interface Consumer extends Runnable, Observable<Transaction>{
    Ticket consume();
}

