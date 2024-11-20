package com.iit.oop.eventticketservice.simulation.data.factory;

import com.iit.oop.eventticketservice.model.Customer;
import com.iit.oop.eventticketservice.model.Ticket;
import com.iit.oop.eventticketservice.model.Vendor;
import com.iit.oop.eventticketservice.service.ticket.TicketService;
import com.iit.oop.eventticketservice.simulation.data.DataGenerator;
import com.iit.oop.eventticketservice.simulation.data.DataStore;
import com.iit.oop.eventticketservice.util.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class TicketFactory {
    private final TicketService ticketService;
    private List<Ticket> tickets;
    private final DataGenerator dataGenerator = new DataGenerator();
    private final DataStore dataStore = DataStore.getInstance();
    private final VendorFactory vendorFactory;

    @Autowired
    public TicketFactory(TicketService ticketService, VendorFactory vendorFactory) {
        this.ticketService = ticketService;
        this.vendorFactory = vendorFactory;
    }

    public List<Ticket> get() {
        if(tickets == null){
            List<Ticket> existingTickets = dataStore.getTickets();
            if (!existingTickets.isEmpty()) {
                tickets = existingTickets;
                return existingTickets;
            }
            tickets = ticketService.getTicketsLimited();
            if (tickets.isEmpty()) {
                List<Vendor> vendors = vendorFactory.get();
                Random random = new Random();
                for (int i = 0; i < Global.SIMULATION_CUSTOMERS; i++) {
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
