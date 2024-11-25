package com.iit.oop.eventticketservice.simulation.participant.producer;

import com.iit.oop.eventticketservice.event.Observerable;

public interface Producer extends Runnable, Observerable<Integer> {
    void produce();
}
