package com.iit.oop.eventticketservice.cli.menu;

import com.iit.oop.eventticketservice.cli.ShellProcess;
import com.iit.oop.eventticketservice.cli.config.GetUserConfig;
import com.iit.oop.eventticketservice.cli.config.SetUserConfig;
import com.iit.oop.eventticketservice.cli.logs.ServerLogs;
import com.iit.oop.eventticketservice.util.shell.ShellScanner;
import com.iit.oop.eventticketservice.util.shell.ShellLogger;
import org.springframework.boot.CommandLineRunner;


public class ShellHandler implements CommandLineRunner {
    private final ShellLogger shellLogger = ShellLogger.getInstance();
    private final ShellScanner scanner = ShellScanner.getInstance();
    private final ShellProcess serverLogs;
    private final ShellProcess userConfig;
    private final ShellProcess setConfig;

    public ShellHandler() {
        this.serverLogs = new ServerLogs();
        this.userConfig = new GetUserConfig();
        this.setConfig = new SetUserConfig();
    }

    private void handleSelection(int selection){
        switch (selection){
            case 1:
                serverLogs.start();
                break;
            case 2:
                userConfig.start();
                break;
            case 3:
                setConfig.start();
                break;
            case 4:
                System.exit(0);
                break;
            default:
                shellLogger.error("Invalid selection. Please try again.");
        }
    }

    @Override
    public void run(String... args) {
        shellLogger.usageInfo("Interactive CLI started. Type 'exit' to quit.");

        while (true) {
            shellLogger.usageInfo("Select an option:");
            shellLogger.usageInfo("1. View server logs");
            shellLogger.usageInfo("2. Get user configuration");
            shellLogger.usageInfo("3. Set user configuration");
            shellLogger.usageInfo("4. Exit");
            shellLogger.usageInfo("Enter selection: ");

            // Read user input
            String input = "";
            try {
                input = scanner.scanString();
            } catch (IllegalArgumentException e) {
                shellLogger.error(e.getMessage());
            }

            if (handleExitCommand(input)) {
                break;
            }
            processInput(input);
        }

        shellLogger.close();
        scanner.close();
        shellLogger.usageInfo("CLI session ended.");
    }

    private boolean handleExitCommand(String input) {
        if ("exit".equalsIgnoreCase(input)) {
            shellLogger.usageInfo("Exiting CLI...");
            return true; // Signal that we should exit
        }
        return false; // Continue running
    }

    private void processInput(String input) {
        try {
            int selection = Integer.parseInt(input);
            handleSelection(selection); // Process the valid selection
        } catch (NumberFormatException e) {
            shellLogger.error("Invalid selection. Please enter a valid number.");
        }
    }
}
