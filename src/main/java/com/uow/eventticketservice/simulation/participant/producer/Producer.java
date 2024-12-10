package com.uow.eventticketservice.simulation.participant.producer;

import com.uow.eventticketservice.event.Observable;

public interface Producer extends Runnable, Observable<String> {
    void produce();
}
