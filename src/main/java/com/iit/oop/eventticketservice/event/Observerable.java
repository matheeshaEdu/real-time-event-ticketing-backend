package com.iit.oop.eventticketservice.event;

import com.iit.oop.eventticketservice.event.observer.DomainEventObserver;

import java.util.List;

public interface Observerable<T> {
    void setObservers(List<DomainEventObserver<T>> observers);
}
