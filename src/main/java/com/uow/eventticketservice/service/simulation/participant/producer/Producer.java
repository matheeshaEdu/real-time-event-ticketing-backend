package com.uow.eventticketservice.service.simulation.participant.producer;

import com.uow.eventticketservice.service.simulation.participant.Participant;

public interface Producer extends Participant<String> {
    void produce();
}