package com.uow.eventticketservice.simulation.data.factory;

import java.util.List;

public interface DataFactory<T> {
    List<T> populate();
}
