package com.iit.oop.eventticketservice.service.ticket;

import com.iit.oop.eventticketservice.model.Ticket;
import com.iit.oop.eventticketservice.repository.TicketRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {
    private final TicketRepo ticketRepo;

    @Autowired
    public TicketService(TicketRepo ticketRepo) {
        this.ticketRepo = ticketRepo;
    }

    public List<Ticket> getTicketsLimited() {
        return ticketRepo.findAll(PageRequest.of(0, 5)).getContent();
    }

    public void saveTicket(Ticket ticket) {
        ticketRepo.save(ticket);
    }
}
