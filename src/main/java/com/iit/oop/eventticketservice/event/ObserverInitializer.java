package com.iit.oop.eventticketservice.event;

import com.iit.oop.eventticketservice.event.observer.ConfigObserver;
import com.iit.oop.eventticketservice.event.observer.DatabaseSinkObserver;
import com.iit.oop.eventticketservice.event.observer.DomainEventObserver;
import com.iit.oop.eventticketservice.event.observer.TicketThresholdObserver;
import com.iit.oop.eventticketservice.model.TicketConfig;
import com.iit.oop.eventticketservice.model.Transaction;
import com.iit.oop.eventticketservice.util.spring.ApplicationContextHolder;

public class ObserverInitializer {
    private final DomainEventObserver<Integer> ticketThresholdObserver =
            new TicketThresholdObserver();

    public static ObserverInitializer getInstance() {
        return ObserverInitializerHolder.INSTANCE;
    }

    public DomainEventObserver<Transaction> getDatabaseSinkObserver() {
        return ApplicationContextHolder.getBean(DatabaseSinkObserver.class);
    }

    public DomainEventObserver<TicketConfig> getConfigObserver() {
        return ApplicationContextHolder.getBean(ConfigObserver.class);
    }

    public DomainEventObserver<Integer> getTicketThresholdObserver() {
        return ticketThresholdObserver;
    }

    private static class ObserverInitializerHolder {
        private static final ObserverInitializer INSTANCE = new ObserverInitializer();
    }
}
