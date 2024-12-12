package com.uow.eventticketservice.service.simulation.participant.consumer;

import com.uow.eventticketservice.service.simulation.participant.Participant;
import com.uow.eventticketservice.model.Transaction;
import com.uow.eventticketservice.model.Ticket;

public interface Consumer extends Participant<Transaction> {
    Ticket consume();
}