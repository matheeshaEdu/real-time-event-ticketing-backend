package com.iit.oop.eventticketservice.service.limiter;

import com.iit.oop.eventticketservice.service.config.ConfigManager;

import java.util.concurrent.atomic.AtomicInteger;

public class TicketCounter {
    private final AtomicInteger ticketCount; // Thread-safe ticket counter
    private final int maxLimit; // Maximum ticket limit

    private TicketCounter(int maxLimit) {
        this.ticketCount = new AtomicInteger(0);
        this.maxLimit = maxLimit;
    }

    private static class SingletonHolder {
        private static final TicketCounter INSTANCE = new TicketCounter(
                ConfigManager.getInstance().getConfig().getTotalTickets()
        );
    }

    public static TicketCounter getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public boolean isBelowLimit() {
        return ticketCount.get() < maxLimit;
    }

    public void increment() {
        ticketCount.incrementAndGet();
    }
}
