package com.iit.oop.eventticketservice.service.vendor;

import com.iit.oop.eventticketservice.model.Vendor;
import com.iit.oop.eventticketservice.repository.VendorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorService {

    private final VendorRepo vendorRepo;

    @Autowired
    public VendorService(VendorRepo vendorRepo) {
        this.vendorRepo = vendorRepo;
    }

    public List<Vendor> getVendorsLimited() {
        return vendorRepo.findAll(PageRequest.of(0, 5)).getContent();
    }

    public void saveVendor(Vendor vendor) {
        vendorRepo.save(vendor);
    }
}
