package com.uow.eventticketservice.util;

public final class Global {
    // Define constants
    public static final String APP_NAME = "EventTicketService";
    public static final String VERSION = "1.0.0";
    public static final String TICKET_PRODUCED = "ticketProduced";
    public static final String TICKET_CONSUMED = "ticketConsumed";
    public static final int MAX_RETRIES = 3;
    public static final int MAX_THREAD_COUNT = 1000;
    public static final long CONSUME_TIME = 50000;
    public static final long PRODUCE_TIME = 60000;
    public static final int SIMULATION_VENDORS = 20;
    public static final int SIMULATION_TICKETS = 60;
    public static final int SIMULATION_CUSTOMERS = 100;
    public static final String LOG_PATH = "src/main/resources/logs/application.log";
    public static final String CONFIG_PATH = "src/main/resources/config/userConfig.json";

    // websocket endpoints
    public static final String WS_ENDPOINT = "/ws";

    // websocket destinations
    public static final String WS_TOPIC_LOGS = "/topic/logs";
    public static final String WS_TOPIC_TICKET_PRODUCED = "/topic/ticket-produced";
    public static final String WS_TOPIC_TICKET_CONSUMED = "/topic/ticket-consumed";


    // Prevent instantiation
    private Global() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    // Add more constants as needed
}
