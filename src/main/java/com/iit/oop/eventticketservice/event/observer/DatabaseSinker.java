package com.iit.oop.eventticketservice.event.observer;

import com.iit.oop.eventticketservice.model.Transaction;
import com.iit.oop.eventticketservice.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSinker implements DomainEventObserver<Transaction> {
    private final TransactionService transactionService;

    @Autowired
    public DatabaseSinker(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public void onDomainEvent(Transaction domainObject) {
        transactionService.saveTransaction(domainObject);
    }
}
