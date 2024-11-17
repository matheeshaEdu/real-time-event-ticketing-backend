package com.iit.oop.eventticketservice.simulation;

import com.iit.oop.eventticketservice.model.Ticket;
import com.iit.oop.eventticketservice.service.config.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TicketPool {
    private static final Logger log = LoggerFactory.getLogger(TicketPool.class);
    private final Queue<Ticket> queue = new ConcurrentLinkedQueue<>();
    private final int maxSize;
    private final Lock lock = new ReentrantLock();

    // Private constructor for Singleton
    private TicketPool(int maxSize) {
        this.maxSize = maxSize;
    }

    // Lazy-loaded Singleton using a nested static class
    private static class SingletonHelper {
        private static final TicketPool INSTANCE = new TicketPool(
                ConfigManager.getInstance().getConfig().getMaxTicketCapacity()
        );
    }

    // Public accessor for the Singleton instance
    public static TicketPool getInstance() {
        return SingletonHelper.INSTANCE;
    }

    /**
     * Adds a ticket to the pool if there's capacity.
     * Uses a lock to ensure thread safety.
     */
    public void addTicket(Ticket ticket) {
        lock.lock();
        try {
            if (queue.size() < maxSize) {
                queue.add(ticket);
            } else {
                log.debug("TicketPool is full. Cannot add more tickets.");
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Retrieves and removes a ticket from the pool.
     * Uses a lock to ensure thread safety.
     *
     * @return a Ticket if available, or null if the pool is empty.
     */
    public Ticket getTicket() {
        lock.lock();
        try {
            if (queue.isEmpty()) {
                log.debug("TicketPool is empty. No tickets to retrieve.");
                return null;
            }
            return queue.poll();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Checks if the pool is empty.
     *
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty() {
        lock.lock();
        try {
            return queue.isEmpty();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Gets the current size of the pool.
     *
     * @return the number of tickets currently in the pool.
     */
    public int size() {
        lock.lock();
        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }
}
