package com.uow.eventticketservice.event.observer;

import com.uow.eventticketservice.service.limiter.TicketCounter;
import com.uow.eventticketservice.controller.ticket.TicketStatStreamingController;
import com.uow.eventticketservice.util.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Observer for the Ticket domain object.
 */
@Component
public class TicketProducedObserver implements DomainEventObserver<String> {
    private final TicketCounter ticketCounter = TicketCounter.getInstance();
    private final TicketStatStreamingController ticketStatStreamingService;

    @Autowired
    public TicketProducedObserver(TicketStatStreamingController ticketStatStreamingController) {
        this.ticketStatStreamingService = ticketStatStreamingController;
    }

    /**
     * Update the ticket counter when a ticket is produced.
     *
     * @param domainObject the domain object
     */
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
