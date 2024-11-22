package com.iit.oop.eventticketservice.service.transaction;

import com.iit.oop.eventticketservice.model.Transaction;
import com.iit.oop.eventticketservice.repository.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepo transactionRepo;

    @Autowired
    public TransactionService(TransactionRepo transactionRepo) {
        this.transactionRepo = transactionRepo;
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepo.findById(id).orElse(null);
    }

    public List<Transaction> getTransactions() {
        return transactionRepo.findAll();
    }

    public void saveTransaction(Transaction transaction) {
        transactionRepo.save(transaction);
    }
}
