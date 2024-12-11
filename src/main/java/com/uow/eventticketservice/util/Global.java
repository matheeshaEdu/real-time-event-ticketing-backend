package com.uow.eventticketservice.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Global {
    // Define constants
    public static final String TICKET_PRODUCED;
    public static final int MAX_THREAD_COUNT;
    public static final long CONSUME_TIME;
    public static final long PRODUCE_TIME;
    public static final int SIMULATION_VENDORS;
    public static final int SIMULATION_TICKETS;
    public static final int SIMULATION_CUSTOMERS;
    public static final String LOG_PATH;
    public static final String CONFIG_PATH;
    public static final String WS_TOPIC_LOGS;
    public static final String WS_TOPIC_TICKET_PRODUCED;
    public static final String WS_TOPIC_TICKET_CONSUMED;

    static {
        Properties properties = new Properties();
        try (InputStream input = Global.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IOException("Unable to find config.properties");
            }
            properties.load(input);
        } catch (IOException ex) {
            throw new ExceptionInInitializerError("Failed to load configuration properties");
        }

        TICKET_PRODUCED = properties.getProperty("ticketProduced");
        MAX_THREAD_COUNT = Integer.parseInt(properties.getProperty("maxThreadCount"));
        CONSUME_TIME = Long.parseLong(properties.getProperty("consumeTime"));
        PRODUCE_TIME = Long.parseLong(properties.getProperty("produceTime"));
        SIMULATION_VENDORS = Integer.parseInt(properties.getProperty("simulationVendors"));
        SIMULATION_TICKETS = Integer.parseInt(properties.getProperty("simulationTickets"));
        SIMULATION_CUSTOMERS = Integer.parseInt(properties.getProperty("simulationCustomers"));
        LOG_PATH = properties.getProperty("logPath");
        CONFIG_PATH = properties.getProperty("configPath");
        WS_TOPIC_LOGS = properties.getProperty("ws.topic.logs");
        WS_TOPIC_TICKET_PRODUCED = properties.getProperty("ws.topic.ticketProduced");
        WS_TOPIC_TICKET_CONSUMED = properties.getProperty("ws.topic.ticketConsumed");
    }

    // Prevent instantiation
    private Global() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}