package com.iit.oop.eventticketservice.configurations;

import com.iit.oop.eventticketservice.service.limiter.TicketCounter;
import com.iit.oop.eventticketservice.service.ticket.TicketStatStreamingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Configuration
public class AppConfig {

    @Bean
    public TicketCounter ticketCounter() {
        return TicketCounter.getInstance();
    }

    @Bean
    public TicketStatStreamingService ticketStatStreamingService(SimpMessagingTemplate messagingTemplate, TicketCounter ticketCounter) {
        return new TicketStatStreamingService(messagingTemplate, ticketCounter);
    }
}
