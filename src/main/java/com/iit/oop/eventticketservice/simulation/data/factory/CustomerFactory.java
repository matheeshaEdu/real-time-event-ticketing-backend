package com.iit.oop.eventticketservice.simulation.data.factory;

import com.iit.oop.eventticketservice.model.Customer;
import com.iit.oop.eventticketservice.service.customer.CustomerService;
import com.iit.oop.eventticketservice.simulation.data.DataGenerator;
import com.iit.oop.eventticketservice.simulation.data.DataStore;
import com.iit.oop.eventticketservice.util.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomerFactory implements DataFactory<Customer> {
    private final CustomerService customerService;
    private List<Customer> customers = new ArrayList<>();
    private final DataGenerator dataGenerator = new DataGenerator();
    private final DataStore dataStore = DataStore.getInstance();

    @Autowired
    public CustomerFactory(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public List<Customer> populate() {
        if(customers.isEmpty()){
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
