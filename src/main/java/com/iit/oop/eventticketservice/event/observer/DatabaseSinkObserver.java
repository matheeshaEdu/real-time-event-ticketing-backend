package com.iit.oop.eventticketservice.event.observer;

import com.iit.oop.eventticketservice.model.Transaction;
import com.iit.oop.eventticketservice.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSinkObserver implements DomainEventObserver<Transaction> {
    private final TransactionService transactionService;

    @Autowired
    public DatabaseSinkObserver(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public void onDomainEvent(Transaction domainObject) {
        // Update the database with the new transaction
        // run in a separate thread to avoid blocking the current thread
        new Thread(() -> updateDatabase(domainObject)).start();
    }

    private void updateDatabase(Transaction domainObject) {
        transactionService.saveTransaction(domainObject);
    }
}
