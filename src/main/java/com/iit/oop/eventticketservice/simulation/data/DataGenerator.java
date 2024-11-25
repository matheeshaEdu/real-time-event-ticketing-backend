package com.iit.oop.eventticketservice.simulation.data;

import com.github.javafaker.Faker;
import com.iit.oop.eventticketservice.model.Customer;
import com.iit.oop.eventticketservice.model.Ticket;
import com.iit.oop.eventticketservice.model.Vendor;

public class DataGenerator {
    private final Faker faker = new Faker();

    public Vendor makeVendor() {
        Vendor vendor = new Vendor();
        vendor.setName(faker.company().name());
        return vendor;
    }

    public Customer makeCustomer() {
        Customer customer = new Customer();
        customer.setName(faker.name().fullName());
        return customer;
    }

    public Ticket makeTicket(Vendor vendor) {
        Ticket ticket = new Ticket();
        ticket.setPrice(faker.number().numberBetween(100, 1000));
        ticket.setName(faker.book().title());
        ticket.setVendor(vendor);
        return ticket;
    }
}
