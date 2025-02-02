package com.uow.eventticketservice.event.subject;

import com.uow.eventticketservice.event.observer.DomainEventObserver;

import java.util.ArrayList;
import java.util.List;

public class DomainEventPublisher<T> implements Subject<T> {
    private final List<DomainEventObserver<T>> observers = new ArrayList<>();

    public void addObserver(DomainEventObserver<T> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(DomainEventObserver<T> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(T domainObject) {
        for (DomainEventObserver<T> observer : observers) {
            observer.onDomainEvent(domainObject);
        }
    }
}

