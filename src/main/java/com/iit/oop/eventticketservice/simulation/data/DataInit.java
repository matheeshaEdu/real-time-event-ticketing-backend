package com.iit.oop.eventticketservice.simulation.data;

import com.iit.oop.eventticketservice.model.Customer;
import com.iit.oop.eventticketservice.model.Ticket;
import com.iit.oop.eventticketservice.model.Vendor;
import com.iit.oop.eventticketservice.simulation.data.factory.DataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Initialize/ populate the data
 */
@Component
public class DataInit {
    private final DataFactory<Customer> customerFactory;
    private final DataFactory<Vendor> vendorFactory;
    private final DataFactory<Ticket> ticketFactory;

    @Autowired
    public DataInit(DataFactory<Customer> customerFactory, DataFactory<Vendor> vendorFactory, DataFactory<Ticket> ticketFactory) {
        this.customerFactory = customerFactory;
        this.vendorFactory = vendorFactory;
        this.ticketFactory = ticketFactory;
    }

    /**
     * Initialize the data
     */
    public void init() {
        customerFactory.populate();
        vendorFactory.populate();
        ticketFactory.populate();
    }
}
