package com.iit.oop.eventticketservice.simulation;

import com.iit.oop.eventticketservice.service.config.ConfigManager;
import com.iit.oop.eventticketservice.simulation.data.DataInit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Simulate the event ticket service
 */
@Component
public class Simulator {
    private final DataInit dataInit;
    private final ParticipantHandler participantHandler = new ParticipantHandler();
    private final ConfigManager configManager = ConfigManager.getInstance();

    @Autowired
    public Simulator(DataInit dataInit) {
        this.dataInit = dataInit;
    }

    /**
     * Simulate vendor and customer interactions
     */
    public void simulate() {
        int buyingRate = configManager.getConfig().getCustomerRetrievalRate();
        int sellingRate = configManager.getConfig().getTicketReleaseRate();
        dataInit.init();
        participantHandler.startSimulation(sellingRate, buyingRate);
    }

    public void stopSimulation() {
        participantHandler.stopAll();
    }
}
