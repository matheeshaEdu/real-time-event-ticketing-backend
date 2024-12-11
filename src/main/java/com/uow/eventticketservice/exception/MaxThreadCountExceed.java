package com.uow.eventticketservice.exception;

public class MaxThreadCountExceed extends RuntimeException {
    public MaxThreadCountExceed(String message) {
        super(message);
    }
}
