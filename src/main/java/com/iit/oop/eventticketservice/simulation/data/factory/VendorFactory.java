package com.iit.oop.eventticketservice.simulation.data.factory;

import com.iit.oop.eventticketservice.model.Vendor;
import com.iit.oop.eventticketservice.service.vendor.VendorService;
import com.iit.oop.eventticketservice.simulation.data.DataGenerator;
import com.iit.oop.eventticketservice.simulation.data.DataStore;
import com.iit.oop.eventticketservice.util.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VendorFactory {
    private final VendorService vendorService;
    private List<Vendor> vendors;
    private final DataGenerator dataGenerator = new DataGenerator();
    private final DataStore dataStore = DataStore.getInstance();

    @Autowired
    public VendorFactory(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    public List<Vendor> get() {
        if(vendors == null){
            // get existing vendors
            List<Vendor> existingVendors = dataStore.getVendors();
            if (!existingVendors.isEmpty()) {
                vendors = existingVendors;
                return existingVendors;
            }            vendors = vendorService.getVendorsLimited();
            if (vendors.isEmpty()) {
                for (int i = 0; i < Global.SIMULATION_VENDORS; i++) {
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
