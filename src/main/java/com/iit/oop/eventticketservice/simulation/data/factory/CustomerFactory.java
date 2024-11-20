package com.iit.oop.eventticketservice.simulation.data.factory;

import com.iit.oop.eventticketservice.model.Customer;
import com.iit.oop.eventticketservice.model.Vendor;
import com.iit.oop.eventticketservice.service.customer.CustomerService;
import com.iit.oop.eventticketservice.simulation.data.DataGenerator;
import com.iit.oop.eventticketservice.simulation.data.DataStore;
import com.iit.oop.eventticketservice.util.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerFactory {
    private final CustomerService customerService;
    private List<Customer> customers;
    private final DataGenerator dataGenerator = new DataGenerator();
    private final DataStore dataStore = DataStore.getInstance();

    @Autowired
    public CustomerFactory(CustomerService customerService) {
        this.customerService = customerService;
    }

    public List<Customer> get() {
        if(customers == null){
            List<Customer> existingCustomers = dataStore.getCustomers();
            if (!existingCustomers.isEmpty()) {
                customers = existingCustomers;
                return existingCustomers;
            }
            customers = customerService.getCustomersLimited();
            if (customers.isEmpty()) {
                for (int i = 0; i < Global.SIMULATION_CUSTOMERS; i++) {
                    Customer customer = dataGenerator.makeCustomer();
                    customerService.saveCustomer(customer);
                    customers.add(customer);
                }
            }
            // set store
            dataStore.setCustomers(customers);
        }
        return customers;
    }
}
