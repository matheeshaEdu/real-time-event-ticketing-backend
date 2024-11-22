package com.iit.oop.eventticketservice.event.observer;

import com.iit.oop.eventticketservice.service.limiter.TicketCounter;

public class TicketThresholdMonitor implements DomainEventObserver<Integer> {
    private final TicketCounter ticketCounter = TicketCounter.getInstance();

    @Override
    public void onDomainEvent(Integer domainObject) {
        ticketCounter.increment();
    }
}
