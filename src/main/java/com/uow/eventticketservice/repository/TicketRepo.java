package com.uow.eventticketservice.repository;

import com.uow.eventticketservice.model.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface TicketRepo extends JpaRepository<Ticket, Long> {
    @Override
    @NonNull
    Page<Ticket> findAll(@NonNull Pageable pageable);
}
