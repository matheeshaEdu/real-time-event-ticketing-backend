package com.iit.oop.eventticketservice;

import com.iit.oop.eventticketservice.cli.ShellHandler;
import com.iit.oop.eventticketservice.cli.ShellManager;
import com.iit.oop.eventticketservice.util.shell.ShellLogger;
import com.iit.oop.eventticketservice.util.shell.ShellScanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EventTicketServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventTicketServiceApplication.class, args);

        // Initialize and start the ShellManager
        ShellManager shellManager = new ShellManager(new ShellHandler());
        shellManager.start();

        // Add shutdown hooks for resource cleanup
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            shellManager.stop();
            ShellLogger.getInstance().close();
        }));
	}

}
