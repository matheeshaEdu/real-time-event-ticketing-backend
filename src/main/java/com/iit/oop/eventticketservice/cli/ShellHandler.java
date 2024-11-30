package com.iit.oop.eventticketservice.cli;

import com.iit.oop.eventticketservice.cli.shellprocess.ShellProcess;
import com.iit.oop.eventticketservice.cli.shellprocess.config.GetConfigProcess;
import com.iit.oop.eventticketservice.cli.shellprocess.config.SetConfigProcess;
import com.iit.oop.eventticketservice.cli.shellprocess.logs.ServerLogProcess;
import com.iit.oop.eventticketservice.service.config.ConfigManager;
import com.iit.oop.eventticketservice.util.shell.ShellLogger;
import com.iit.oop.eventticketservice.util.shell.ShellScanner;
import org.springframework.boot.CommandLineRunner;


/**
 * Handles the shell input and executes the appropriate shell process.
 */
public class ShellHandler implements CommandLineRunner {
    private final ShellProcess serverLogs;
    private final ShellProcess userConfig;
    private final ShellProcess setConfig;
    private final ShellProcess simulationProcess;
    private final ShellProcess stopSimulationProcess;
    // Shell utilities
    private final ShellLogger shellLogger = ShellLogger.getInstance();
    private final ShellScanner scanner = ShellScanner.getInstance();

    /**
     * Constructor for the ShellHandler class.
     *
     * @param simulationProcess      The process to run the simulation.
     * @param stopSimulationProcess  The process to stop the simulation.
     */
    public ShellHandler(ShellProcess simulationProcess, ShellProcess stopSimulationProcess) {
        ConfigManager configManager = ConfigManager.getInstance();
        // Initialize shell processes
        this.serverLogs = new ServerLogProcess(
                scanner,
                shellLogger
        );
        this.userConfig = new GetConfigProcess(
                configManager,
                shellLogger
        );
        this.setConfig = new SetConfigProcess(
                configManager,
                shellLogger,
                scanner
        );
        this.simulationProcess = simulationProcess;
        this.stopSimulationProcess = stopSimulationProcess;
    }


    /**
     * Handles the user selection and executes the appropriate shell process.
     *
     * @param selection The user selection.
     */
    private void handleSelection(int selection) {
        switch (selection) {
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

    /**
     * Runs the interactive CLI.
     *
     * @param args The command line arguments.
     */
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

    /**
     * Handles the exit command.
     *
     * @param input The user input.
     * @return True if the exit command was entered, false otherwise.
     */
    private boolean handleExitCommand(String input) {
        if ("exit".equalsIgnoreCase(input)) {
            shellLogger.usageInfo("Exiting CLI...");
            return true; // Signal that we should exit
        }
        return false; // Continue running
    }

    /**
     * Processes the user input.
     *
     * @param input The user input.
     */
    private void processInput(String input) {
        try {
            int selection = Integer.parseInt(input);
            handleSelection(selection); // Process the valid selection
        } catch (NumberFormatException e) {
            shellLogger.error("Invalid selection. Please enter a valid number.");
        }
    }
}
