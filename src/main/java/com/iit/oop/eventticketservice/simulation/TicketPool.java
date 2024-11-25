package com.iit.oop.eventticketservice.simulation;

import com.iit.oop.eventticketservice.model.Ticket;
import com.iit.oop.eventticketservice.model.TicketConfig;
import com.iit.oop.eventticketservice.service.config.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * A thread-safe TicketPool using Singleton pattern.
 * Manages a bounded pool of tickets with concurrent access.
 */
public class TicketPool {
    private static final Logger log = LoggerFactory.getLogger(TicketPool.class);

    private final Queue<Ticket> queue;
    private TicketConfig config;
    private final Lock lock;
    private final Condition notFull;
    private final Condition notEmpty;

    // Private constructor for Singleton
    private TicketPool(TicketConfig config) {
        this.queue = new ConcurrentLinkedQueue<>();
        this.config = config;
        this.lock = new ReentrantLock();
        this.notFull = lock.newCondition();
        this.notEmpty = lock.newCondition();
    }

    // Lazy-loaded Singleton using nested static class
    private static class SingletonHelper {
        private static final TicketPool INSTANCE = new TicketPool(
                ConfigManager.getInstance().getConfig()
        );
    }

    /**
     * Gets the singleton instance of TicketPool.
     *
     * @return the singleton TicketPool instance
     */
    public static TicketPool getInstance() {
        return SingletonHelper.INSTANCE;
    }

    /**
     * Adds a ticket to the pool. Blocks if the pool is full.
     *
     * @param ticket the ticket to add
     */
    public void addTicket(Ticket ticket) {
        lock.lock();
        try {
            while (queue.size() >= config.getMaxTicketCapacity()) {
                log.info("Ticket pool is full. Waiting to add ticket.");
                notFull.await();
            }
            queue.add(ticket);
            notEmpty.signal(); // Signal that the pool is no longer empty
        } catch (InterruptedException e) {
            log.error("Thread interrupted while adding a ticket: {}", e.getMessage());
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Retrieves and removes a ticket from the pool. Blocks if the pool is empty.
     *
     * @return the retrieved ticket
     */
    public Ticket getTicket() {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                log.info("Ticket pool is empty. Waiting to retrieve ticket.");
                notEmpty.await();
            }
            Ticket ticket = queue.poll();
            notFull.signal(); // Signal that the pool is no longer full
            return ticket;
        } catch (InterruptedException e) {
            log.error("Thread interrupted while retrieving a ticket: {}", e.getMessage());
            Thread.currentThread().interrupt();
            return null;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Checks if the pool is empty.
     *
     * @return true if empty, false otherwise
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
     * @return the number of tickets currently in the pool
     */
    public int size() {
        lock.lock();
        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }

    public void reset(TicketConfig config) {
        lock.lock();
        try {
            queue.clear();
            setConfig(config);
        } finally {
            lock.unlock();
        }
    }

    private void setConfig(TicketConfig config) {
        this.config = config;
    }
}

