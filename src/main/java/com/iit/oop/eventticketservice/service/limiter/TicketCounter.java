package com.iit.oop.eventticketservice.service.limiter;

import com.iit.oop.eventticketservice.model.TicketConfig;
import com.iit.oop.eventticketservice.service.config.ConfigManager;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Singleton class to keep track of the number of tickets produced and consumed
 */
public class TicketCounter {
    private final AtomicInteger producedTicketCount;
    private final AtomicInteger consumedTicketCount;
    private TicketConfig config;

    private TicketCounter(TicketConfig config) {
        this.producedTicketCount = new AtomicInteger(0);
        this.consumedTicketCount = new AtomicInteger(0);
        this.config = config;
    }

    /**
     * Get the singleton instance of the TicketCounter
     *
     * @return the singleton instance of the TicketCounter
     */
    public static TicketCounter getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * Get the number of tickets produced
     *
     * @return the number of tickets produced
     */
    public int getProducedTicketCount() {
        return producedTicketCount.get();
    }

    /**
     * Get the number of tickets consumed
     *
     * @return the number of tickets consumed
     */
    public int getConsumedTicketCount() {
        return consumedTicketCount.get();
    }

    /**
     * Check if the number of produced tickets is below the limit
     *
     * @return true if the number of produced tickets is below the limit, false otherwise
     */
    public boolean isProducedBelowLimit() {
        return producedTicketCount.get() < config.getTotalTickets();
    }

    /**
     * Increment the number of produced tickets by 1
     */
    public void incrementProduced() {
        producedTicketCount.incrementAndGet();
    }

    /**
     * Increment the number of consumed tickets by 1
     */
    public void incrementConsumed() {
        consumedTicketCount.incrementAndGet();
    }

    /**
     * Reset the TicketCounter with the given TicketConfig
     *
     * @param config the TicketConfig to reset the TicketCounter with
     */
    public void reset(TicketConfig config) {
        setConfig(config);
        producedTicketCount.set(0);
        consumedTicketCount.set(0);
    }

    /**
     * Reset the TicketCounter
     * Set the produced and consumed ticket count to 0
     */
    public void reset() {
        producedTicketCount.set(0);
        consumedTicketCount.set(0);
    }

    private void setConfig(TicketConfig config) {
        this.config = config;
    }

    private static class SingletonHolder {
        private static final TicketCounter INSTANCE = new TicketCounter(
                ConfigManager.getInstance().getConfig()
        );
    }
}
