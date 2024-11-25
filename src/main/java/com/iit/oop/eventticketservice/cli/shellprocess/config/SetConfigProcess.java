package com.iit.oop.eventticketservice.cli.shellprocess.config;

import com.iit.oop.eventticketservice.cli.shellprocess.ShellProcess;
import com.iit.oop.eventticketservice.model.TicketConfig;
import com.iit.oop.eventticketservice.service.config.ConfigManager;
import com.iit.oop.eventticketservice.util.shell.ShellLogger;
import com.iit.oop.eventticketservice.util.shell.ShellScanner;

public class SetConfigProcess implements ShellProcess {
    private final ConfigManager configManager = ConfigManager.getInstance();
    private final ShellLogger shellLogger = ShellLogger.getInstance();
    private final ShellScanner scan = ShellScanner.getInstance();

    public void execute() {
        setConfig();
    }

    private void setConfig() {
        int totalTickets = getConfigValue("total tickets");
        int ticketReleaseRate = getConfigValue("ticket release rate");
        int customerRetrievalRate = getConfigValue("customer retrieval rate");
        int maxTicketCapacity = getConfigValue("max ticket capacity");

        // set the user configuration
        TicketConfig ticketConfig = new TicketConfig(
                totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
        configManager.setUserConfig(ticketConfig);
    }

    private int getConfigValue(String configName) {
        while (true) {
            shellLogger.usageInfo("Enter " + configName + ": ");
            try {
                return scan.scanPositiveInt();
            } catch (IllegalArgumentException e) {
                shellLogger.error(e.getMessage());
            }
        }
    }
}