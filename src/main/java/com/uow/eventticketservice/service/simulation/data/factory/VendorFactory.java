package com.uow.eventticketservice.service.simulation.data.factory;

import com.uow.eventticketservice.model.Vendor;
import com.uow.eventticketservice.service.vendor.VendorService;
import com.uow.eventticketservice.service.simulation.data.DataGenerator;
import com.uow.eventticketservice.service.simulation.data.DataStore;
import com.uow.eventticketservice.util.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VendorFactory implements DataFactory<Vendor> {
    private final VendorService vendorService;
    private final DataGenerator dataGenerator = new DataGenerator();
    private final DataStore dataStore = DataStore.getInstance();
    private List<Vendor> vendors = new ArrayList<>();

    @Autowired
    public VendorFactory(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @Override
    public List<Vendor> populate() {
        if (vendors.isEmpty()) {
            // get existing vendors
            List<Vendor> existingVendors = dataStore.getVendors();
            if (!existingVendors.isEmpty()) {
                vendors = new ArrayList<>(existingVendors);
                return vendors;
            }
            vendors = new ArrayList<>(vendorService.getVendorsLimited());
            if (vendors.isEmpty() || vendors.size() < Global.SIMULATION_VENDORS) {
                // fill the rest if not enough
                int vendorCount = Global.SIMULATION_VENDORS - vendors.size();
                // generate vendors
                for (int i = 0; i < vendorCount; i++) {
                    Vendor vendor = dataGenerator.makeVendor();
                    vendorService.saveVendor(vendor);
                    vendors.add(vendor);
                }
            }
            // set store
            dataStore.setVendors(vendors);
        }
        return vendors;
    }
}
