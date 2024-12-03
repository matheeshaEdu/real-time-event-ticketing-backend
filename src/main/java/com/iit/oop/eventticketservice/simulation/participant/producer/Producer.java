package com.iit.oop.eventticketservice.simulation.participant.producer;

import com.iit.oop.eventticketservice.event.Observable;

public interface Producer extends Runnable, Observable<String> {
    void produce();
}
