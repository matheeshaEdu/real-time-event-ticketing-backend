package com.uow.eventticketservice.repository;

import com.uow.eventticketservice.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface CustomerRepo extends JpaRepository<Customer, Long> {
    @Override
    @NonNull
    Page<Customer> findAll(@NonNull Pageable pageable);
}
