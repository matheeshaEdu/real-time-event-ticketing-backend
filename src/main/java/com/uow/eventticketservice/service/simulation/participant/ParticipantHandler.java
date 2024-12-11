package com.uow.eventticketservice.service.simulation.participant;

import com.uow.eventticketservice.event.ObserverInitializer;
import com.uow.eventticketservice.exception.MaxThreadCountExceed;
import com.uow.eventticketservice.model.Customer;
import com.uow.eventticketservice.model.Ticket;
import com.uow.eventticketservice.core.ticket.TicketCounter;
import com.uow.eventticketservice.core.ticket.TicketPool;
import com.uow.eventticketservice.service.simulation.Timer;
import com.uow.eventticketservice.service.simulation.data.DataStore;
import com.uow.eventticketservice.service.simulation.participant.consumer.TicketConsumer;
import com.uow.eventticketservice.service.simulation.participant.producer.TicketProducer;
import com.uow.eventticketservice.util.Global;
import com.uow.eventticketservice.util.log.DualLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * ParticipantHandler is responsible for starting and stopping the simulation.
 */
public class ParticipantHandler {
    private static final Logger log = LoggerFactory.getLogger(ParticipantHandler.class);
    private final DataStore dataStore;
    private final TicketCounter ticketCounter;
    private final TicketPool ticketPool;
    private final DualLogger dualLogger;
    private final ObserverInitializer observerInitializer;
    private final List<Thread> producerThreadPool;
    private final List<Thread> consumerThreadPool;
    private final Random random;
    private volatile boolean running;

    private static final int MILLI_SECONDS_PER_SECOND = 1000;
    private static final int SECONDS_PER_MINUTE = 60;

    public ParticipantHandler(TicketCounter ticketCounter) {
        this.ticketCounter = ticketCounter;
        this.producerThreadPool = new ArrayList<>();
        this.consumerThreadPool = new ArrayList<>();
        this.random = new Random();
        this.dualLogger = new DualLogger(log);
        this.observerInitializer = ObserverInitializer.getInstance();
        this.dataStore = DataStore.getInstance();
        this.ticketPool = TicketPool.getInstance();
        this.running = false;

    }

    /**
     * Start the simulation with the given selling and buying rates.
     *
     * @param sellingRate the rate at which tickets are released
     * @param buyingRate  the rate at which customers retrieve tickets
     */
    public void startSimulation(int sellingRate, int buyingRate) throws MaxThreadCountExceed {
        // check if the simulation is already running
        if (running) {
            dualLogger.logAndFailure("Simulation is already running. Please stop the simulation before starting a new one.");
            return;
        }
        int producerCount = getProducerThreadCount(sellingRate);
        int consumerCount = getConsumerThreadCount(buyingRate);
        // check if the number of threads is below the maximum thread count
        if (isBelowMaxThreadCount(producerCount) || isBelowMaxThreadCount(consumerCount)) {
            dualLogger.logAndFailure("The number of threads exceeds the maximum thread count. Please reduce the rates.");
            throw new MaxThreadCountExceed("The number of threads exceeds the maximum thread count.");
        }
        startProducers(producerCount);
        startConsumers(consumerCount);
        running = true;
    }

    /**
     * Check if the number of threads is below the maximum thread count.
     *
     * @param threadCount the number of threads
     * @return true if the number of threads is below the maximum thread count, false otherwise
     */
    private boolean isBelowMaxThreadCount(int threadCount) {
        return threadCount >= Global.MAX_THREAD_COUNT;
    }

    /**
     * Calculate the number of producer threads required to achieve the desired rate per minute.
     *
     * @param ratePerMinute the desired rate per minute
     * @return the number of producer threads required
     */
    private int getProducerThreadCount(int ratePerMinute) {
        // convert rate per minute to rate per milli-seconds
        double ratePerMilliSeconds = (double) ratePerMinute / (SECONDS_PER_MINUTE * MILLI_SECONDS_PER_SECOND);
        return (int) Math.ceil(ratePerMilliSeconds * Global.PRODUCE_TIME);
    }

    /**
     * Calculate the number of consumer threads required to achieve the desired rate per minute.
     *
     * @param ratePerMinute the desired rate per minute
     * @return the number of consumer threads required
     */
    private int getConsumerThreadCount(int ratePerMinute) {
        // convert rate per minute to rate per milli-seconds
        double ratePerMilliSeconds = (double) ratePerMinute / (SECONDS_PER_MINUTE * MILLI_SECONDS_PER_SECOND);
        return (int) Math.ceil(ratePerMilliSeconds * Global.CONSUME_TIME);
    }

    /**
     * Start the consumer threads.
     *
     * @param threads the number of consumer threads to start
     */
    private void startConsumers(int threads) {
        dualLogger.logAndAlert("Starting " + threads + " consumer threads...");
        List<Customer> customers = dataStore.getCustomers();
        // init consumer dependencies
        Timer timer = new Timer();

        for (int i = 0; i < threads; i++) {
            // get a random customer
            Customer randomCustomer = customers.get(random.nextInt(customers.size()));
            // create a consumer thread
            TicketConsumer consumer = new TicketConsumer(
                    timer, ticketPool, randomCustomer
            );
            // set observers
            consumer.setObservers(List.of(
                    observerInitializer.getTransactionSinkObserver()
            ));
            // start the consumer thread
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
        dualLogger.logAndAlert("Starting " + threads + " producer threads...");
        List<Ticket> tickets = dataStore.getTickets();
        // init producer dependencies
        Timer timer = new Timer();
        for (int i = 0; i < threads; i++) {
            // get a random vendor and ticket
            Ticket randomTicket = tickets.get(random.nextInt(tickets.size()));
            // create a producer thread
            TicketProducer producer = new TicketProducer(
                    timer, ticketPool, randomTicket.getVendor(), randomTicket
            );
            // set observers
            producer.setObservers(List.of(observerInitializer.getTicketProducedObserver()));
            // start the producer thread
            Thread producerThread = new Thread(producer, "Producer-" + i);
            producerThreadPool.add(producerThread);
            producerThread.start();
        }
    }

    /**
     * Stop all producer and consumer threads.
     */
    public void stopAll() {
        // check if the simulation is not running
        if (!running) {
            dualLogger.logAndFailure("Simulation is not running. Please start the simulation before stopping it.");
            return;
        }
        log.info("Stopping all producer consumer threads...");
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
        ticketPool.reset();
        producerThreadPool.clear();
        consumerThreadPool.clear();
        running = false;
        resetTicketCounter();
        dualLogger.logAndAlert("All producer consumer threads stopped.");

    }

    public boolean isRunning() {
        return running;
    }

    private void resetTicketCounter() {
        ticketCounter.reset();
    }
}
