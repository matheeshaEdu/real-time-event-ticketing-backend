package com.iit.oop.eventticketservice.dto.ticket;

public class TicketStats {
    private final int ticketsProduced;
    private final int ticketsConsumed;

    public TicketStats(int ticketsProduced, int ticketsConsumed) {
        this.ticketsProduced = ticketsProduced;
        this.ticketsConsumed = ticketsConsumed;
    }

    public int getTicketsProduced() {
        return ticketsProduced;
    }

    public int getTicketsConsumed() {
        return ticketsConsumed;
    }
}

