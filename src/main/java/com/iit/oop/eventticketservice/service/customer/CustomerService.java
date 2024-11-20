package com.iit.oop.eventticketservice.service.customer;

import com.iit.oop.eventticketservice.model.Customer;
import com.iit.oop.eventticketservice.repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private CustomerRepo customerRepo;

    @Autowired
    public CustomerService(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    public void saveCustomer(Customer customer) {
        customerRepo.save(customer);
    }

    public List<Customer> getCustomersLimited() {
        return customerRepo.findAll(PageRequest.of(0, 10)).getContent();
    }
}
