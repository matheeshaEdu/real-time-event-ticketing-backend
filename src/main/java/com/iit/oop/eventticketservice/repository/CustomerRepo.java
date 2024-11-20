package com.iit.oop.eventticketservice.repository;

import com.iit.oop.eventticketservice.model.Customer;
import com.iit.oop.eventticketservice.model.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface CustomerRepo extends JpaRepository<Customer, Long> {
    @Override
    @NonNull
    Page<Customer> findAll(@NonNull Pageable pageable);
}
