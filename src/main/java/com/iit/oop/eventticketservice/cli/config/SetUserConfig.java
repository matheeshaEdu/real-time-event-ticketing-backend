package com.iit.oop.eventticketservice.cli.config;

import com.iit.oop.eventticketservice.model.UserConfig;
import com.iit.oop.eventticketservice.cli.ShellProcess;
import com.iit.oop.eventticketservice.util.shell.ShellScanner;
import com.iit.oop.eventticketservice.service.config.ConfigManager;
import com.iit.oop.eventticketservice.util.shell.ShellLogger;

public class SetUserConfig implements ShellProcess {
    private final ConfigManager configManager = ConfigManager.getInstance();
    private final ShellLogger shellLogger = ShellLogger.getInstance();
    private final ShellScanner scan = ShellScanner.getInstance();

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