package com.uow.eventticketservice.event;

import com.uow.eventticketservice.event.observer.DomainEventObserver;

import java.util.List;

/**
 * Interface for observable objects
 *
 * @param <T> the type of the object to observe
 */
public interface Observable<T> {
    void setObservers(List<DomainEventObserver<T>> observers);
}
