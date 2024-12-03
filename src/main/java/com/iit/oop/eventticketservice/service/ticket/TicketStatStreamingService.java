package com.iit.oop.eventticketservice.service.ticket;

import com.iit.oop.eventticketservice.service.limiter.TicketCounter;
import com.iit.oop.eventticketservice.util.Global;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class TicketStatStreamingService {
    private final TicketCounter ticketCounter;
    private final SimpMessagingTemplate messagingTemplate;

    public TicketStatStreamingService(
            SimpMessagingTemplate messagingTemplate, TicketCounter ticketCounter
    ) {
        this.messagingTemplate = messagingTemplate;
        this.ticketCounter = ticketCounter;
    }

    public void sendProducedUpdate() {
        sendUpdate(Global.WS_TOPIC_TICKET_PRODUCED, ticketCounter.getProducedTicketCount());
    }

    public void sendConsumedUpdate() {
        sendUpdate(Global.WS_TOPIC_TICKET_CONSUMED, ticketCounter.getConsumedTicketCount());
    }

    private void sendUpdate(String topic, int count) {
        new Thread(() -> messagingTemplate.convertAndSend(topic, count)).start();
    }
}

