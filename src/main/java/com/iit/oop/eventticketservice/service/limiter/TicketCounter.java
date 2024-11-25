package com.iit.oop.eventticketservice.service.limiter;

import com.iit.oop.eventticketservice.model.TicketConfig;
import com.iit.oop.eventticketservice.service.config.ConfigManager;

import java.util.concurrent.atomic.AtomicInteger;

public class TicketCounter {
    private final AtomicInteger ticketCount; // Thread-safe ticket counter
    private  TicketConfig config;

    private TicketCounter(TicketConfig config) {
        this.ticketCount = new AtomicInteger(0);
        this.config = config;
    }

    private static class SingletonHolder {
        private static final TicketCounter INSTANCE = new TicketCounter(
                ConfigManager.getInstance().getConfig()
        );
    }

    public static TicketCounter getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public boolean isBelowLimit() {
        return ticketCount.get() < config.getTotalTickets();
    }

    public void increment() {
        ticketCount.incrementAndGet();
    }

    public void reset(TicketConfig config) {
        setConfig(config);
        ticketCount.set(0);
    }

    private void setConfig(TicketConfig config) {
        this.config = config;
    }
}
