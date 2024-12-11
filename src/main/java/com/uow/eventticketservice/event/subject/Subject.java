package com.uow.eventticketservice.event.subject;

import com.uow.eventticketservice.event.observer.DomainEventObserver;

public interface Subject<T> {
    void addObserver(DomainEventObserver<T> observer);

    void removeObserver(DomainEventObserver<T> observer);

    void notifyObservers(T domainObject);
}
