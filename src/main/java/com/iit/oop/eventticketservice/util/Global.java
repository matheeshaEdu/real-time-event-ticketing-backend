package com.iit.oop.eventticketservice.util;

public final class Global {
    // Define constants
    public static final String APP_NAME = "EventTicketService";
    public static final String VERSION = "1.0.0";
    public static final int MAX_RETRIES = 3;
    public static final long CONSUME_TIME = 50000;
    public static final long PRODUCE_TIME = 60000;
    public static final int SIMULATION_VENDORS = 5;
    public static final int SIMULATION_TICKETS = 5;
    public static final int SIMULATION_CUSTOMERS = 10;
    public static final String LOG_PATH = "src/main/resources/logs/application.log";
    public static final String CONFIG_PATH = "src/main/resources/config/userConfig.json";

    // Prevent instantiation
    private Global() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    // Add more constants as needed
}
