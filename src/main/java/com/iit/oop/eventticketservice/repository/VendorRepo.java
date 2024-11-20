package com.iit.oop.eventticketservice.repository;

import com.iit.oop.eventticketservice.model.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepo extends JpaRepository<Vendor, Long> {
    @Override
    @NonNull
    Page<Vendor> findAll(@NonNull Pageable pageable);
}
