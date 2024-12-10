package com.uow.eventticketservice.repository;

import com.uow.eventticketservice.dto.repo.VendorTransactionCount;
import com.uow.eventticketservice.model.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepo extends JpaRepository<Transaction, Long> {
    @Query("SELECT new com.uow.eventticketservice.dto.repo.VendorTransactionCount(COUNT(t.id), v.name) " +
            "FROM Transaction t JOIN Ticket ti ON t.ticket.id = ti.id " +
            "JOIN Vendor v ON ti.vendor.id = v.id " +
            "GROUP BY v.name ORDER BY COUNT(t.id) desc")
    List<VendorTransactionCount> topVendors(Pageable pageable);
}
