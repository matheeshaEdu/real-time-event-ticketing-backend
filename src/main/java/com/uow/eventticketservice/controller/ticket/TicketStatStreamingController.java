package com.uow.eventticketservice.controller.ticket;

import com.uow.eventticketservice.dto.response.TicketStat;
import com.uow.eventticketservice.core.ticket.TicketCounter;
import com.uow.eventticketservice.util.Global;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * This class is responsible for streaming ticket statistics to the WebSocket clients.
 * It sends the produced and consumed ticket counts to the clients.
 */
@Service
public class TicketStatStreamingController {
    private final TicketCounter ticketCounter;
    private final SimpMessagingTemplate messagingTemplate;

    public TicketStatStreamingController(
            SimpMessagingTemplate messagingTemplate, TicketCounter ticketCounter
    ) {
        this.messagingTemplate = messagingTemplate;
        this.ticketCounter = ticketCounter;
    }

    /**
     * Send the produced ticket count to the WebSocket clients
     */
    public void sendProducedUpdate() {
        sendUpdate(Global.WS_TOPIC_TICKET_PRODUCED, ticketCounter.getProducedTicketCount());
    }

    /**
     * Send the consumed ticket count to the WebSocket clients
     */
    public void sendConsumedUpdate() {
        sendUpdate(Global.WS_TOPIC_TICKET_CONSUMED, ticketCounter.getConsumedTicketCount());
    }

    private void sendUpdate(String topic, int count) {
        TicketStat ticketStat = new TicketStat(count, topic);
        new Thread(() -> messagingTemplate.convertAndSend(topic, ticketStat)).start();
    }
}

