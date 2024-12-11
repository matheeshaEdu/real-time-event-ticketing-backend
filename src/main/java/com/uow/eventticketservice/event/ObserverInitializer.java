package com.uow.eventticketservice.event;

import com.uow.eventticketservice.event.observer.ConfigObserver;
import com.uow.eventticketservice.event.observer.TransactionSinkObserver;
import com.uow.eventticketservice.event.observer.DomainEventObserver;
import com.uow.eventticketservice.event.observer.TicketProducedObserver;
import com.uow.eventticketservice.model.TicketConfig;
import com.uow.eventticketservice.model.Transaction;
import com.uow.eventticketservice.util.spring.ApplicationContextHolder;

/**
 * Singleton class to initialize the observers
 */
public class ObserverInitializer {

    /**
     * Get the singleton instance of the ObserverInitializer
     *
     * @return the singleton instance of the ObserverInitializer
     */
    public static ObserverInitializer getInstance() {
        return ObserverInitializerHolder.INSTANCE;
    }

    /**
     * Get the observer for the transaction sink
     *
     * @return the observer for the transaction sink
     */
    public DomainEventObserver<Transaction> getTransactionSinkObserver() {
        return ApplicationContextHolder.getBean(TransactionSinkObserver.class);
    }

    /**
     * Get the observer for the ticket config
     *
     * @return the observer for the ticket config
     */
    public DomainEventObserver<TicketConfig> getConfigObserver() {
        return ApplicationContextHolder.getBean(ConfigObserver.class);
    }

    /**
     * Get the observer for the ticket produced
     *
     * @return the observer for the ticket produced
     */
    public DomainEventObserver<String> getTicketProducedObserver() {
        return ApplicationContextHolder.getBean(TicketProducedObserver.class);
    }

    private static class ObserverInitializerHolder {
        private static final ObserverInitializer INSTANCE = new ObserverInitializer();
    }
}
