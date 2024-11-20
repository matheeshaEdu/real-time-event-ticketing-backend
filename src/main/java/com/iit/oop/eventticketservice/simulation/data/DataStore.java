package com.iit.oop.eventticketservice.simulation.data;

import com.iit.oop.eventticketservice.model.Customer;
import com.iit.oop.eventticketservice.model.Ticket;
import com.iit.oop.eventticketservice.model.Vendor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DataStore {
    private List<Vendor> vendors = new ArrayList<>();
    private List<Ticket> tickets = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    // Private constructor to prevent instantiation
    private DataStore() {
    }

    // Singleton instance holder
    private static class SingletonHolder {
        private static final DataStore INSTANCE = new DataStore(); // Default size 10
    }

    // Public method to get the singleton instance
    public static DataStore getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public List<Vendor> getVendors() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(vendors);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void setVendors(List<Vendor> vendors) {
        lock.writeLock().lock();
        try {
            this.vendors = vendors;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public List<Ticket> getTickets() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(tickets);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void setTickets(List<Ticket> tickets) {
        lock.writeLock().lock();
        try {
            this.tickets = tickets;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public List<Customer> getCustomers() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(customers);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void setCustomers(List<Customer> customers) {
        lock.writeLock().lock();
        try {
            this.customers = customers;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void clear() {
        lock.writeLock().lock();
        try {
            vendors = null;
            tickets = null;
            customers = null;
        } finally {
            lock.writeLock().unlock();
        }
    }
}
