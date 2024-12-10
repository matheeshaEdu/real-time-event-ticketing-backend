package com.uow.eventticketservice.service.simulation.data.factory;

import java.util.List;

public interface DataFactory<T> {
    List<T> populate();
}
