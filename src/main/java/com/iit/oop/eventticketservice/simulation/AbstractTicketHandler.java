package com.iit.oop.eventticketservice.simulation;

public abstract class AbstractTicketHandler {
    /**
     * Delays execution for the specified duration.
     *
     * @param millis Duration to sleep in milliseconds.
     * @throws InterruptedException If the thread is interrupted during sleep.
     */
    protected void delayFor(long millis) throws InterruptedException {
        if (millis > 0) {
            Thread.sleep(millis);
        }
    }
}