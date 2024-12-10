package com.uow.eventticketservice.service.simulation.participant.producer;

import com.uow.eventticketservice.event.Observable;

public interface Producer extends Runnable, Observable<String> {
    void produce();
}
