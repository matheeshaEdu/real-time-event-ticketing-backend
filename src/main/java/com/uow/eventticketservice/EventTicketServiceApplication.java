package com.uow.eventticketservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Matheesha Gunarathne
 * @version 1.0
 */

@SpringBootApplication
@EnableScheduling
public class EventTicketServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventTicketServiceApplication.class, args);
    }

}
