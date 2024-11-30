package com.iit.oop.eventticketservice.event;

import com.iit.oop.eventticketservice.event.observer.DomainEventObserver;

import java.util.List;

public interface Observable<T> {
    void setObservers(List<DomainEventObserver<T>> observers);
}
