package com.iit.oop.eventticketservice.repository;

import com.iit.oop.eventticketservice.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepo extends JpaRepository<Transaction, Long> {
}
