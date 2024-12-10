# Real-Time Event Ticket System Backend

[![License](https://img.shields.io/badge/license-MIT-green)](LICENSE)

A scalable and high-performance backend system for managing real-time event ticketing operations. This project facilitates ticket production, consumption, and monitoring with robust concurrency management.

---

## Table of Contents
1. [Features](#features)
2. [System Architecture](#system-architecture)
3. [Tech Stack](#tech-stack)
4. [Installation](#installation)
5. [Configuration](#configuration)
6. [Usage](#usage)
7. [Testing](#testing)
8. [Deployment](#deployment)
9. [Contributing](#contributing)
10. [License](#license)
11. [Contact](#contact)

---

## Features
- Real-time WebSocket-based ticket updates.
- REST API for managing ticket configurations.
- Robust thread-safe concurrency control using locks.
- Dynamic ticket pool size with configurable limits.
- Dockerized setup for easy deployment.

---

## System Architecture
The system consists of:
1. **Producers**: Generate tickets based on configured limits.
2. **Consumers**: Process tickets from the pool in real-time.
3. **WebSocket Server**: Streams updates to connected clients.
4. **REST API**: Provides CRUD operations for managing configurations.

![System Architecture Diagram](system-architecture.png)

---

## Tech Stack
- **Backend**: Spring Boot (Java), WebSocket, RandomAccessFile for log handling.
- **Database**: MySQL
- **Concurrency**: Custom lock-based queue implementation.

---

## Configuration
The application can be configured using the configuration file located at `src/main/resources/config.properties`.
- **`maxThreadCount`**: Maximum number of threads for ticket producing or consuming.
- **`consumeTime`**: Time taken to consume a ticket in milliseconds.
- **`produceTime`**: Time taken to produce a ticket in milliseconds.
- **`simulationVendors`**: Number of dummy vendors to simulate.
- **`simulationTickets`**: Number of dummy tickets to simulate.
- **`simulationCustomers`**: Number of dummy customers to simulate.

---

## Installation

### Prerequisites
- JDK 17+
- Maven 3.6+

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/matheeshaEdu/real-time-event-ticketing-app.git
   cd real-time-event-ticketing-app
   ```
2. Build the project:
   ```bash
   mvn clean install
   ```
3. Run the application:
   ```bash
    java -jar target/ticketing-app-0.0.1-SNAPSHOT.jar
    ```
4. Access the application at `http://localhost:8080`.
5. Access the CLI via terminal.
