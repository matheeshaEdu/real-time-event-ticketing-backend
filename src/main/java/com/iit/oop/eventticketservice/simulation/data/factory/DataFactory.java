package com.iit.oop.eventticketservice.simulation.data.factory;

import java.util.List;

public interface DataFactory<T> {
    List<T> populate();
}
