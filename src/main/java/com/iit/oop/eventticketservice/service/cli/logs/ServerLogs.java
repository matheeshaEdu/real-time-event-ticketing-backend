package com.iit.oop.eventticketservice.service.cli.logs;

import com.iit.oop.eventticketservice.service.cli.ShellProcess;
import com.iit.oop.eventticketservice.util.logger.ShellLogger;
import org.jline.terminal.Terminal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Shell process to tail the server log file
 */
@Component
public class ServerLogs implements ShellProcess {
    private static final Logger log = LoggerFactory.getLogger(ServerLogs.class);

    private final ShellLogger shellLogger;
    private Thread logReaderThread;

    @Value("${logging.file.name}")
    private String logPath;

    public ServerLogs(Terminal terminal) {
        this.shellLogger = new ShellLogger(terminal);
    }

    @Override
    public void start() {
        startLogReader();
        waitForUserInputToStop();
    }

    /**
     * Starts the log reader thread and manages its lifecycle.
     */
    private void startLogReader() {
        logReaderThread = new Thread(this::readLog);
        logReaderThread.setDaemon(true); // Allows JVM to exit if main thread ends
        logReaderThread.start();
    }

    /**
     * Reads the log file and outputs its content line by line.
     */
    private void readLog() {
        Process process = null;
        try (BufferedReader reader = createLogReader()) {
            process = startLogTailProcess();
            String line;

            while ((line = reader.readLine()) != null) {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
                shellLogger.info(line);
            }
        } catch (IOException e) {
            log.error("Error reading log file", e);
        } finally {
            if (process != null) {
                process.destroy(); // Ensure the process is terminated
            }
        }
    }

    /**
     * Stops the log reader when the user presses 'q'.
     */
    private void waitForUserInputToStop() {
        shellLogger.usageInfo("Press 'q' to stop tailing the log and return to the shell prompt.");
        try {
            while (true) {
                int key = System.in.read();
                if (key == 'q') { // Stop on user pressing 'q'
                    stopLogReader();
                    break;
                }
            }
        } catch (IOException e) {
            log.error("Error while waiting for user input to stop: {}", e.getMessage());
        }
    }

    /**
     * Stops the log reader thread safely.
     */
    private void stopLogReader() {
        if (logReaderThread != null && logReaderThread.isAlive()) {
            logReaderThread.interrupt(); // Signal the thread to stop
            try {
                logReaderThread.join(1000); // Wait for up to 2 seconds
                if (logReaderThread.isAlive()) {
                    log.warn("Log reader thread did not terminate in time. Forcing shutdown...");
                }
            } catch (InterruptedException e) {
                log.error("Error while stopping log reader thread", e);
                Thread.currentThread().interrupt(); // Restore interrupted status
            }
        }
    }

    /**
     * Creates a BufferedReader to read the log output.
     *
     * @return BufferedReader instance
     * @throws IOException if an error occurs during reader creation
     */
    private BufferedReader createLogReader() throws IOException {
        return new BufferedReader(new InputStreamReader(startLogTailProcess().getInputStream()));
    }

    /**
     * Starts the 'tail' process for the log file.
     *
     * @return the started Process
     * @throws IOException if an error occurs while starting the process
     */
    private Process startLogTailProcess() throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("tail", "-f", logPath);
        return processBuilder.start();
    }
}




