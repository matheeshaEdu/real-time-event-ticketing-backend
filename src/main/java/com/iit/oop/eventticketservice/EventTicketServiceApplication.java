package com.iit.oop.eventticketservice;

import com.iit.oop.eventticketservice.service.cli.config.GetUserConfig;
import com.iit.oop.eventticketservice.service.cli.config.SetUserConfig;
import com.iit.oop.eventticketservice.service.cli.logs.ServerLogs;
import com.iit.oop.eventticketservice.service.cli.menu.ShellHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EventTicketServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventTicketServiceApplication.class, args);

		// start the shell in a new thread
		Thread shellThread = new Thread(() -> {
			// create a new shell handler
			ShellHandler shellHandler = new ShellHandler();
			// run the shell handler
            try {
                shellHandler.run();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
		shellThread.start();
	}

}
