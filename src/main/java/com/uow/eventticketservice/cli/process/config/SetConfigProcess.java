package com.uow.eventticketservice.cli.process.config;

import com.uow.eventticketservice.cli.process.Process;
import com.uow.eventticketservice.model.TicketConfig;
import com.uow.eventticketservice.service.config.ConfigManager;
import com.uow.eventticketservice.util.shell.ShellLogger;
import com.uow.eventticketservice.util.shell.ShellScanner;

public class SetConfigProcess implements Process {
    private final ConfigManager configManager;
    private final ShellLogger shellLogger ;
    private final ShellScanner scan;

    public SetConfigProcess(ConfigManager configManager, ShellLogger shellLogger, ShellScanner scan) {
        this.configManager = configManager;
        this.shellLogger = shellLogger;
        this.scan = scan;
    }

    public void execute() {
        setConfig();
    }

    private void setConfig() {
        TicketConfig ticketConfig = new TicketConfig(
                getConfigValue("total tickets"),
                getConfigValue("ticket release rate"),
                getConfigValue("customer retrieval rate"),
                getConfigValue("max ticket capacity")
        );
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