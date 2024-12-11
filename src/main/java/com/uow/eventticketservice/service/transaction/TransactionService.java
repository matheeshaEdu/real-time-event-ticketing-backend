package com.uow.eventticketservice.service.transaction;

import com.uow.eventticketservice.dto.repo.VendorTransactionCount;
import com.uow.eventticketservice.model.Transaction;
import com.uow.eventticketservice.repository.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepo transactionRepo;

    @Autowired
    public TransactionService(TransactionRepo transactionRepo) {
        this.transactionRepo = transactionRepo;
    }

    public void saveTransaction(Transaction transaction) {
        transactionRepo.save(transaction);
    }

    public List<VendorTransactionCount> getTopVendors() {
        return transactionRepo.topVendors(PageRequest.of(0, 6));
    }

}
