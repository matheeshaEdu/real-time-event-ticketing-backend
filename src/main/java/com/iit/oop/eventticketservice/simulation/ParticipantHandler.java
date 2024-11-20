package com.iit.oop.eventticketservice.simulation;

import com.iit.oop.eventticketservice.model.Customer;
import com.iit.oop.eventticketservice.model.Ticket;
import com.iit.oop.eventticketservice.model.Vendor;
import com.iit.oop.eventticketservice.simulation.consumer.TicketConsumer;
import com.iit.oop.eventticketservice.simulation.producer.TicketProducer;
import com.iit.oop.eventticketservice.util.Global;

import java.util.ArrayList;
import java.util.List;

/**
 * ParticipantHandler is responsible for starting and stopping the simulation.
 */
public class ParticipantHandler {
    private final List<Thread> producerThreadPool = new ArrayList<>();
    private final List<Thread> consumerThreadPool = new ArrayList<>();

    public void startSimulation(int sellingRate, int buyingRate) {
        int producerCount = getProducerThreadCount(sellingRate);
        int consumerCount = getConsumerThreadCount(buyingRate);
        startProducers(producerCount);
        startConsumers(consumerCount);
    }

    /**
     * Calculate the number of producer threads required to achieve the desired rate per minute.
     *
     * @param ratePerMinute the desired rate per minute
     * @return the number of producer threads required
     */
    private int getProducerThreadCount(int ratePerMinute) {
        // convert rate per minute to rate per milli-seconds
        int ratePerMilliSeconds = ratePerMinute / 60 * 1000;
        return (int) Math.ceil((double) ratePerMilliSeconds / Global.PRODUCE_TIME);
    }

    /**
     * Calculate the number of consumer threads required to achieve the desired rate per minute.
     *
     * @param ratePerMinute the desired rate per minute
     * @return the number of consumer threads required
     */
    private int getConsumerThreadCount(int ratePerMinute) {
        // convert rate per minute to rate per milli-seconds
        int ratePerMilliSeconds = ratePerMinute / 60 * 1000;
        return (int) Math.ceil((double) ratePerMilliSeconds / Global.CONSUME_TIME);
    }

    /**
     * Start the consumer threads.
     *
     * @param threads the number of consumer threads to start
     */
    private void startConsumers(int threads) {
        for (int i = 0; i < threads; i++) {
            TicketConsumer consumer = new TicketConsumer(new Customer());
            Thread consumerThread = new Thread(consumer, "Consumer-" + i);
            consumerThreadPool.add(consumerThread);
            consumerThread.start();
        }
    }

    /**
     * Start the producer threads.
     *
     * @param threads the number of producer threads to start
     */
    private void startProducers(int threads) {
        for (int i = 0; i < threads; i++) {
            TicketProducer producer = new TicketProducer(new Vendor(), new Ticket());
            Thread producerThread = new Thread(producer, "Producer-" + i);
            producerThreadPool.add(producerThread);
            producerThread.start();
        }
    }

    /**
     * Stop all producer and consumer threads.
     */
    public void stopAll() {
        producerThreadPool.forEach(Thread::interrupt);
        consumerThreadPool.forEach(Thread::interrupt);
        // try to shut down the threads gracefully
        producerThreadPool.forEach(thread -> {
            try {
                thread.join(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        consumerThreadPool.forEach(thread -> {
            try {
                thread.join(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
}
