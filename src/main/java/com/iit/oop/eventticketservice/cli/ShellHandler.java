package com.iit.oop.eventticketservice.cli;

import com.iit.oop.eventticketservice.cli.shellprocess.ShellProcess;
import com.iit.oop.eventticketservice.cli.shellprocess.config.GetConfigProcess;
import com.iit.oop.eventticketservice.cli.shellprocess.config.SetConfigProcess;
import com.iit.oop.eventticketservice.cli.shellprocess.logs.ServerLogProcess;
import com.iit.oop.eventticketservice.util.shell.ShellScanner;
import com.iit.oop.eventticketservice.util.shell.ShellLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class ShellHandler implements CommandLineRunner {
    private final ShellLogger shellLogger = ShellLogger.getInstance();
    private final ShellScanner scanner = ShellScanner.getInstance();
    private final ShellProcess serverLogs;
    private final ShellProcess userConfig;
    private final ShellProcess setConfig;

    @Autowired
    private ShellProcess simulationProcess;
    @Autowired
    private ShellProcess stopSimulationProcess;

    public ShellHandler() {
        this.serverLogs = new ServerLogProcess();
        this.userConfig = new GetConfigProcess();
        this.setConfig = new SetConfigProcess();
    }

    private void handleSelection(int selection){
        switch (selection){
            case 1:
                serverLogs.execute();
                break;
            case 2:
                userConfig.execute();
                break;
            case 3:
                setConfig.execute();
                break;
            case 4:
                simulationProcess.execute();
                break;
            case 5:
                stopSimulationProcess.execute();
                break;
            case 6:
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
            shellLogger.usageInfo("4. Run simulation");
            shellLogger.usageInfo("5. Stop simulation");
            shellLogger.usageInfo("6. Exit");
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
