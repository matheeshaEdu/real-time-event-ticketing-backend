package com.iit.oop.eventticketservice.service.config;

import com.iit.oop.eventticketservice.exception.ConfigNotFoundException;
import com.iit.oop.eventticketservice.model.TicketConfig;
import com.iit.oop.eventticketservice.util.validator.ValidationUtil;
import org.springframework.stereotype.Service;

@Service
public class UserConfigService {
    private final ConfigManager configManager = ConfigManager.getInstance();

    public TicketConfig getConfig() throws ConfigNotFoundException {
        return configManager.getConfig();
    }

    public void setConfig(TicketConfig ticketConfig) {
        // intercept the passed ticketConfig and validate it
        try {
            configManager.setUserConfig(
                    new TicketConfig(
                            ValidationUtil.validatePositiveInt(ticketConfig.getTotalTickets()),
                            ValidationUtil.validatePositiveInt(ticketConfig.getTicketReleaseRate()),
                            ValidationUtil.validatePositiveInt(ticketConfig.getCustomerRetrievalRate()),
                            ValidationUtil.validatePositiveInt(ticketConfig.getMaxTicketCapacity())
                    )
            );
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid configuration: " + e.getMessage());
        }
    }
}
