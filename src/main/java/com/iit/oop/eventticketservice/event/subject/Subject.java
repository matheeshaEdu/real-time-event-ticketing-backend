package com.iit.oop.eventticketservice.event.subject;

import com.iit.oop.eventticketservice.event.observer.DomainEventObserver;

public interface Subject<T> {
    void addObserver(DomainEventObserver<T> observer);
    void removeObserver(DomainEventObserver<T> observer);
    void notifyObservers(T domainObject);
}
