package com.uow.eventticketservice.event.observer;

import com.uow.eventticketservice.model.Transaction;
import com.uow.eventticketservice.service.limiter.TicketCounter;
import com.uow.eventticketservice.controller.ticket.TicketStatStreamingController;
import com.uow.eventticketservice.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionSinkObserver implements DomainEventObserver<Transaction> {
    private final TicketCounter ticketCounter = TicketCounter.getInstance();
    private final TransactionService transactionService;
    private final TicketStatStreamingController ticketStatStreamingService;

    @Autowired
    public TransactionSinkObserver(TransactionService transactionService, TicketStatStreamingController ticketStatStreamingService) {
        this.transactionService = transactionService;
        this.ticketStatStreamingService = ticketStatStreamingService;
    }

    @Override
    public void onDomainEvent(Transaction domainObject) {
        // Update the database with the new transaction
        // run in a separate thread to avoid blocking the current thread
        new Thread(() -> updateDatabase(domainObject)).start();
        // Increment the consumed ticket count
        ticketCounter.incrementConsumed();
        // Send the updated ticket stats to the client
        ticketStatStreamingService.sendConsumedUpdate();

    }

    private void updateDatabase(Transaction domainObject) {
        transactionService.saveTransaction(domainObject);
    }


}
