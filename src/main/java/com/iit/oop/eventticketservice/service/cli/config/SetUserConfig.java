package com.iit.oop.eventticketservice.service.cli.config;

import com.iit.oop.eventticketservice.model.UserConfig;
import com.iit.oop.eventticketservice.service.cli.ShellProcess;
import com.iit.oop.eventticketservice.service.cli.input.Scan;
import com.iit.oop.eventticketservice.service.config.ConfigManager;
import com.iit.oop.eventticketservice.util.logger.ShellLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SetUserConfig implements ShellProcess {
    private static final ConfigManager configManager = ConfigManager.getInstance();
    private final ShellLogger shellLogger;
    private final Scan scan;

    @Autowired
    public SetUserConfig(ShellLogger shellLogger, Scan scan) {
        this.scan = scan;
        this.shellLogger = shellLogger;
    }

    public void start() {
        setConfig();
    }

    private void setConfig(){
        int totalTickets = getConfigValue("total tickets");
        int ticketReleaseRate = getConfigValue("ticket release rate");
        int customerRetrievalRate = getConfigValue("customer retrieval rate");
        int maxTicketCapacity = getConfigValue("max ticket capacity");

        // set the user configuration
        UserConfig userConfig = new UserConfig(
                totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
        configManager.setUserConfig(userConfig);
    }

    private int getConfigValue(String configName){
        while (true){
            shellLogger.usageInfo("Enter " + configName + ": ");
            try {
                return scan.scanPositiveInt();
            } catch (IllegalArgumentException e) {
                shellLogger.error(e.getMessage());
            }
        }
    }
}