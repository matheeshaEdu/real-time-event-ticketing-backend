package com.iit.oop.eventticketservice.simulation;

import java.util.Random;

/**
 * Timer is responsible for generating delays.
 */
public class Timer {
    private final Random random = new Random();

    /**
     * Generates a random delay between 0 and maxDelay.
     *
     * @param maxDelay The maximum delay in milliseconds.
     * @return The random delay in seconds.
     */
    public int getRandomDelay(long maxDelay){
        // Generate a random delay between 0 and maxDelay
        // convert the delay to seconds
        int maxDelaySeconds = (int) maxDelay / 1000;
        return (int) Math.abs(random.nextLong() % maxDelaySeconds);
    }
}
