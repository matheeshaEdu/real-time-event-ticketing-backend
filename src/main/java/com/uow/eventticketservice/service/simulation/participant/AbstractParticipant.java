package com.uow.eventticketservice.service.simulation.participant;

import com.uow.eventticketservice.event.observer.DomainEventObserver;
import com.uow.eventticketservice.event.subject.DomainEventPublisher;
import com.uow.eventticketservice.event.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * AbstractParticipant provides a skeletal implementation of the Participant interface.
 * It provides a Subject to manage observers and a logger for logging.
 *
 * @param <T> The type of event to observe.
 */
public abstract class AbstractParticipant<T> implements Participant<T> {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    protected final Subject<T> subject;

    /**
     * Constructs a new AbstractParticipant.
     */
    public AbstractParticipant() {
        this.subject = new DomainEventPublisher<>();
    }

    /**
     * Sets the observers to notify when an event occurs.
     *
     * @param observers List of observers to notify.
     */
    @Override
    public void setObservers(List<DomainEventObserver<T>> observers) {
        for (DomainEventObserver<T> observer : observers) {
            subject.addObserver(observer);
        }
    }

    /**
     * Notifies all observers that an event has occurred.
     *
     * @param event The event that occurred.
     */
    protected void notifyObservers(T event) {
        subject.notifyObservers(event);
    }

    /**
     * Delays execution for the specified duration.
     *
     * @param millis Duration to sleep in milliseconds.
     * @throws InterruptedException If the thread is interrupted during sleep.
     */
    protected void delayFor(long millis) throws InterruptedException {
        if (millis > 0) {
            Thread.sleep(millis);
        }
    }
}