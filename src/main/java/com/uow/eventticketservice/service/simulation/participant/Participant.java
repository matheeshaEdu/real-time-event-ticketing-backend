package com.uow.eventticketservice.service.simulation.participant;

import com.uow.eventticketservice.event.Observable;
import com.uow.eventticketservice.event.observer.DomainEventObserver;

import java.util.List;

public interface Participant<T> extends Runnable, Observable<T> {
    void setObservers(List<DomainEventObserver<T>> observers);
}
