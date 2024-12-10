package com.uow.eventticketservice.simulation.data.factory;

import com.uow.eventticketservice.model.Customer;
import com.uow.eventticketservice.service.customer.CustomerService;
import com.uow.eventticketservice.simulation.data.DataGenerator;
import com.uow.eventticketservice.simulation.data.DataStore;
import com.uow.eventticketservice.util.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomerFactory implements DataFactory<Customer> {
    private final CustomerService customerService;
    private final DataGenerator dataGenerator;
    private final DataStore dataStore;
    private List<Customer> customers;

    @Autowired
    public CustomerFactory(CustomerService customerService) {
        this.customerService = customerService;
        this.customers = new ArrayList<>();
        this.dataGenerator = new DataGenerator();
        this.dataStore = DataStore.getInstance();
    }

    @Override
    public List<Customer> populate() {
        if (customers.isEmpty()) {
            List<Customer> existingCustomers = dataStore.getCustomers();
            if (!existingCustomers.isEmpty()) {
                customers = new ArrayList<>(existingCustomers);
                return existingCustomers;
            }
            customers = new ArrayList<>(customerService.getCustomersLimited());
            if (customers.isEmpty() || customers.size() < Global.SIMULATION_CUSTOMERS) {
                // fill the rest if not enough
                int customerCount = Global.SIMULATION_CUSTOMERS - customers.size();
                // generate customers
                for (int i = 0; i < customerCount; i++) {
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
