package com.uow.eventticketservice.core.ticket;

import com.uow.eventticketservice.model.Ticket;
import com.uow.eventticketservice.core.config.ConfigManager;
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
    private final Lock lock;
    private final Condition notFull;
    private final Condition notEmpty;
    private int capacity;

    // Private constructor for Singleton
    private TicketPool(int capacity) {
        this.queue = new ConcurrentLinkedQueue<>();
        this.lock = new ReentrantLock(true); // Fair lock
        this.notFull = lock.newCondition();
        this.notEmpty = lock.newCondition();
        this.capacity = capacity;
    }

    /**
     * Adds a ticket to the pool. Blocks if the pool is full.
     *
     * @param ticket the ticket to add
     */
    public void addTicket(Ticket ticket) {
        lock.lock();
        try {
            // Use a "while" loop to ensure condition is rechecked after waking up
            while (size() >= this.capacity) {
                log.info("Ticket pool is full. Waiting to add ticket.");
                notFull.await();
            }
            queue.add(ticket);
            log.info("Ticket added to the pool. Current size: {}", queue.size());
            notEmpty.signal(); // Notify that the pool is no longer empty
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
            // Use a "while" loop to ensure condition is rechecked after waking up
            while (isEmpty()) {
                log.info("Ticket pool is empty. Waiting to retrieve ticket.");
                notEmpty.await();
            }
            Ticket ticket = queue.poll();
            log.info("Ticket retrieved from the pool. Current size: {}", queue.size());
            notFull.signal(); // Notify that the pool is no longer full
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
     * Resets the pool and updates configuration.
     *
     * @param capacity the new configuration
     */
    public void reset(int capacity) {
        lock.lock();
        try {
            queue.clear();
            setCapacity(capacity);
            log.info("Ticket pool reset with new configuration.");
            notFull.signalAll(); // Notify waiting threads
            notEmpty.signalAll(); // Notify waiting threads
        } finally {
            lock.unlock();
        }
    }

    public void reset() {
        reset(this.capacity); // Use the existing configuration
    }

    private void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    private boolean isEmpty() {
        lock.lock();
        try {
            return queue.isEmpty();
        } finally {
            lock.unlock();
        }
    }

    private int size() {
        lock.lock();
        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }

    // Lazy-loaded Singleton using nested static class
    private static class SingletonHelper {
        private static final TicketPool INSTANCE = new TicketPool(
                ConfigManager.getInstance().getConfig().getMaxTicketCapacity()
        );
    }

    public static TicketPool getInstance() {
        return SingletonHelper.INSTANCE;
    }
}


