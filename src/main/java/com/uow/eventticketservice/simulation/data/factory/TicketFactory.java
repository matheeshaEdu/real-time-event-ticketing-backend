package com.uow.eventticketservice.simulation.data.factory;

import com.uow.eventticketservice.model.Ticket;
import com.uow.eventticketservice.model.Vendor;
import com.uow.eventticketservice.service.ticket.TicketService;
import com.uow.eventticketservice.simulation.data.DataGenerator;
import com.uow.eventticketservice.simulation.data.DataStore;
import com.uow.eventticketservice.util.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class TicketFactory implements DataFactory<Ticket> {
    private final TicketService ticketService;
    private final DataGenerator dataGenerator = new DataGenerator();
    private final DataStore dataStore = DataStore.getInstance();
    private final VendorFactory vendorFactory;
    Random random;
    private List<Ticket> tickets = new ArrayList<>();

    @Autowired
    public TicketFactory(TicketService ticketService, VendorFactory vendorFactory) {
        this.ticketService = ticketService;
        this.vendorFactory = vendorFactory;
        this.random = new Random();
    }

    @Override
    public List<Ticket> populate() {
        if (tickets.isEmpty()) {
            List<Ticket> existingTickets = dataStore.getTickets();
            if (!existingTickets.isEmpty()) {
                tickets = new ArrayList<>(existingTickets);
                return tickets;
            }
            tickets = new ArrayList<>(ticketService.getTicketsLimited());
            if (tickets.isEmpty() || tickets.size() < Global.SIMULATION_TICKETS) {
                List<Vendor> vendors = vendorFactory.populate();
                // fill the rest if not enough
                int ticketCount = Global.SIMULATION_TICKETS - tickets.size();
                // generate tickets
                for (int i = 0; i < ticketCount; i++) {
                    Vendor randomVendor = vendors.get(random.nextInt(vendors.size()));
                    Ticket customer = dataGenerator.makeTicket(randomVendor);
                    ticketService.saveTicket(customer);
                    tickets.add(customer);
                }
            }
            // set store
            dataStore.setTickets(tickets);
        }
        return tickets;
    }
}
