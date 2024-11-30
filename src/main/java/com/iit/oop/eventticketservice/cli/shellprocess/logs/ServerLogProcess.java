package com.iit.oop.eventticketservice.cli.shellprocess.logs;

import com.iit.oop.eventticketservice.cli.shellprocess.ShellProcess;
import com.iit.oop.eventticketservice.util.Global;
import com.iit.oop.eventticketservice.util.shell.ShellLogger;
import com.iit.oop.eventticketservice.util.shell.ShellScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Shell process to tail the server log file.
 */
public class ServerLogProcess implements ShellProcess {
    private static final Logger log = LoggerFactory.getLogger(ServerLogProcess.class);
    private final ShellScanner scanner;
    private final ShellLogger shellLogger;
    private final Object lock = new Object(); // Synchronization lock
    private volatile boolean running; // Volatile to ensure visibility across threads

    public ServerLogProcess(ShellScanner scanner, ShellLogger shellLogger) {
        this.scanner = scanner;
        this.shellLogger = shellLogger;
    }

    @Override
    public void execute() {
        running = true;
        Thread logReaderThread = new Thread(this::readLog, "LogReaderThread");
        Thread userInputListenerThread = new Thread(this::listenForUserInput, "UserInputListenerThread");

        userInputListenerThread.start();
        logReaderThread.start();

        // Wait for threads to terminate
        try {
            logReaderThread.join();
            userInputListenerThread.join();
        } catch (InterruptedException e) {
            log.error("Error while waiting for threads to terminate", e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Reads the server log file and logs new lines to the shell.
     */
    private void readLog() {
        try (RandomAccessFile reader = new RandomAccessFile(Global.LOG_PATH, "r")) {
            long filePointer = getLastNLinesFilePointer(reader, 10);
            reader.seek(filePointer);
            while (running) {
                filePointer = readNewLines(reader, filePointer);
                Thread.sleep(500); // Poll every half a second
            }
        } catch (IOException e) {
            log.error("Error while reading the log file: {}", e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupt status
            log.info("Log reading interrupted.");
        }
    }

    /**
     * Gets the file pointer position for the last N lines in the log file.
     *
     * @param reader RandomAccessFile instance.
     * @param n      Number of lines to read.
     * @return File pointer position for the last N lines.
     * @throws IOException If an error occurs while reading the file.
     */
    private long getLastNLinesFilePointer(RandomAccessFile reader, int n) throws IOException {
        long fileLength = reader.length();
        long filePointer = fileLength - 1;
        int lines = 0;

        reader.seek(filePointer);
        for (long pointer = filePointer; pointer >= 0; pointer--) {
            reader.seek(pointer);
            int readByte = reader.readByte();
            if (readByte == 0xA) { // New line character
                lines++;
                if (lines == n + 1) {
                    break;
                }
            }
        }
        return reader.getFilePointer();
    }

    /**
     * Reads new lines from the log file.
     *
     * @param reader      RandomAccessFile instance.
     * @param filePointer The current file pointer position.
     * @return Updated file pointer after reading new lines.
     * @throws IOException If an error occurs while reading the file.
     */
    private long readNewLines(RandomAccessFile reader, long filePointer) throws IOException {
        reader.seek(filePointer);
        String line;
        while ((line = reader.readLine()) != null) {
            shellLogger.verbose(line);
        }
        return reader.getFilePointer(); // Update file pointer
    }

    /**
     * Listens for user input to stop the log tailing process.
     */
    private void listenForUserInput() {
        shellLogger.notice("Press 'q' to stop tailing the log and return to the shell prompt.");
        while (running) {
            String input;
            try {
                input = scanner.scanString();
                if (input.equals("q")) {
                    break;
                }
            } catch (IllegalArgumentException e) {
                shellLogger.error(e.getMessage());
            }
        }
        stop();
    }

    /**
     * Gracefully stops all running threads and resources.
     */
    public void stop() {
        synchronized (lock) {
            if (running) {
                running = false;
                log.info("Stopping log reader and user input listener...");
            }
        }
    }
}
