package com.uow.eventticketservice.service.simulation.data;

import com.uow.eventticketservice.model.Customer;
import com.uow.eventticketservice.model.Ticket;
import com.uow.eventticketservice.model.Vendor;
import com.uow.eventticketservice.service.simulation.data.factory.DataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Initialize/ populate the data
 */
@Component
public class DataInit {
    private final List<DataFactory<?>> factories = new ArrayList<>();

    @Autowired
    public DataInit(
            DataFactory<Customer> customerFactory,
            DataFactory<Vendor> vendorFactory,
            DataFactory<Ticket> ticketFactory
    ) {
        factories.add(customerFactory);
        factories.add(vendorFactory);
        factories.add(ticketFactory);
    }

    /**
     * populate the data
     */
    public void init() {
        factories.forEach(dataFactory ->
                new Thread(dataFactory::populate).start()
        );
    }
}
