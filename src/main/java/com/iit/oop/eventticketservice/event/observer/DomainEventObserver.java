package com.iit.oop.eventticketservice.event.observer;

public interface DomainEventObserver<T> {
    void onDomainEvent(T domainObject);
}

