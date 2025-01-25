# Ecom-Microservice

This project is a microservices-based e-commerce application built using Spring Boot. It includes various services such as Order Service, Shipping Service, and Inventory Service.

## Table of Contents

- [Technologies](#technologies)
- [Architecture](#architecture)
- [Services](#services)
- [Setup](#setup)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Exception Handling](#exception-handling)
- [Contributing](#contributing)

## Technologies

- Java
- Spring Boot
- Spring Cloud
- Maven
- PostgreSQL
- Feign Client
- Resilience4j
- Zipkin
- ELK Stack 

## Architecture

The application follows a microservices architecture with the following services:

- **Order Service**: Manages orders.
- **Shipping Service**: Manages shipping.
- **Inventory Service**: Manages inventory.

![Architecture Diagram](Diagram.png)

## Services

### Order Service

Handles order creation, retrieval, and cancellation.

### Shipping Service

Handles shipping confirmation.

### Inventory Service

Handles inventory management.

## Setup

1. Clone the repository:
   ```sh
   git clone https://github.com/Meet16123/ecom-microservice.git
   cd ecom-microservice
   ```

2. Build the project using Maven:
   ```sh
   mvn clean install
   ```

3. Run the services:
   ```sh
   cd order-service
   mvn spring-boot:run
   
   cd ../shipping-service
   mvn spring-boot:run
   
   cd ../inventory-service
   mvn spring-boot:run
   ```

## Usage

The services can be accessed via their respective endpoints. Use tools like Postman or cURL to interact with the APIs.

## API Endpoints

### Order Service
- `GET /orders`: Fetch all orders
- `GET /orders/{id}`: Fetch order by ID
- `POST /orders`: Create a new order
- `PUT /orders/cancel/{id}`: Cancel an order
- `POST /orders/confirmShipping/{id}`: Confirm shipping for an order

### Shipping Service
- `POST /shipping/confirmShipping`: Confirm shipping

### Inventory Service
- `POST /inventory/reduce`: Reduce inventory
- `POST /inventory/revert`: Revert inventory

## Exception Handling

Global exception handling is implemented using `@RestControllerAdvice` and custom exception classes.

## Contributing

Contributions are welcome! Please fork the repository and create a pull request.

