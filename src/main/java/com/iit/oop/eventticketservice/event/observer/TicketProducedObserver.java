package com.iit.oop.eventticketservice.event.observer;

import com.iit.oop.eventticketservice.service.limiter.TicketCounter;
import com.iit.oop.eventticketservice.service.ticket.TicketStatStreamingService;
import com.iit.oop.eventticketservice.util.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TicketProducedObserver implements DomainEventObserver<String> {
    private final TicketCounter ticketCounter = TicketCounter.getInstance();
    private final TicketStatStreamingService ticketStatStreamingService;

    @Autowired
    public TicketProducedObserver(TicketStatStreamingService ticketStatStreamingService) {
        this.ticketStatStreamingService = ticketStatStreamingService;
    }

    @Override
    public void onDomainEvent(String domainObject) {
        if (domainObject.equals(Global.TICKET_PRODUCED)) {
            ticketCounter.incrementProduced();
        } else {
            return;
        }
        // send the updated ticket stats to the client
        ticketStatStreamingService.sendProducedUpdate();
    }
}
