package com.uow.eventticketservice.configurations;

import com.uow.eventticketservice.core.ticket.TicketCounter;
import com.uow.eventticketservice.controller.ticket.TicketStatStreamingController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * Configuration class for the application
 */
@Configuration
public class AppConfig {

    /**
     * Create a TicketCounter bean
     * @return TicketCounter instance
     */
    @Bean
    public TicketCounter ticketCounter() {
        return TicketCounter.getInstance();
    }

    /**
     * Create a TicketStatStreamingService bean
     * @param messagingTemplate SimpMessagingTemplate instance
     * @param ticketCounter TicketCounter instance
     * @return TicketStatStreamingService instance
     */
    @Bean
    public TicketStatStreamingController ticketStatStreamingService(SimpMessagingTemplate messagingTemplate, TicketCounter ticketCounter) {
        return new TicketStatStreamingController(messagingTemplate, ticketCounter);
    }
}
