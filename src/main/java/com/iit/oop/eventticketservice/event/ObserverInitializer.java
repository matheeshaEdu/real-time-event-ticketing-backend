package com.iit.oop.eventticketservice.event;

import com.iit.oop.eventticketservice.event.observer.ConfigObserver;
import com.iit.oop.eventticketservice.event.observer.TransactionSinkObserver;
import com.iit.oop.eventticketservice.event.observer.DomainEventObserver;
import com.iit.oop.eventticketservice.event.observer.TicketProducedObserver;
import com.iit.oop.eventticketservice.model.TicketConfig;
import com.iit.oop.eventticketservice.model.Transaction;
import com.iit.oop.eventticketservice.util.spring.ApplicationContextHolder;

public class ObserverInitializer {

    public static ObserverInitializer getInstance() {
        return ObserverInitializerHolder.INSTANCE;
    }

    public DomainEventObserver<Transaction> getTransactionSinkObserver() {
        return ApplicationContextHolder.getBean(TransactionSinkObserver.class);
    }

    public DomainEventObserver<TicketConfig> getConfigObserver() {
        return ApplicationContextHolder.getBean(ConfigObserver.class);
    }

    public DomainEventObserver<String> getTicketProducedObserver() {
        return ApplicationContextHolder.getBean(TicketProducedObserver.class);
    }

    private static class ObserverInitializerHolder {
        private static final ObserverInitializer INSTANCE = new ObserverInitializer();
    }
}
