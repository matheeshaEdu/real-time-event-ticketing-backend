package com.uow.eventticketservice.event.observer;

public interface DomainEventObserver<T> {
    void onDomainEvent(T domainObject);
}

