package com.iit.oop.eventticketservice.cli;

import com.iit.oop.eventticketservice.util.shell.ShellLogger;
import com.iit.oop.eventticketservice.util.shell.ShellScanner;
;

public class ShellManager {

    private Thread shellThread;
    private final ShellHandler shellHandler;

    public ShellManager(ShellHandler shellHandler) {
        this.shellHandler = shellHandler;
    }

    /**
     * Starts the ShellHandler in a separate thread.
     */
    public void start() {
        shellThread = new Thread(() -> {
            try {
                shellHandler.run();
            } catch (Exception e) {
                ShellLogger.getInstance().error("Error running shell: " + e.getMessage());
                Thread.currentThread().interrupt(); // Restore the interrupt status
            }
        });
        shellThread.setName("ShellThread");
        shellThread.setDaemon(true); // Set as a daemon thread so it exits when the JVM shuts down
        shellThread.start();
    }

    /**
     * Stops the ShellHandler by interrupting the thread.
     */
    public void stop() {
        if (shellThread != null && shellThread.isAlive()) {
            shellThread.interrupt();
            try {
                shellThread.join(); // Wait for the thread to terminate
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupt status
                ShellLogger.getInstance().error("Shell thread interrupted during shutdown.");
            }
        }
        ShellScanner.getInstance().close();
    }
}
