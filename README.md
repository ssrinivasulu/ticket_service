# Ticket Service Application
Implement a simple ticket service that facilitates the discovery, temporary hold, and final reservation of seats within a high-demand performance venue.

## Overview
The current functionality is targetted specific to service layer functionality in exposing the api's required to implement the Ticket Service functionality, the service layer api's are tested using the Spring Boot, Spring Data JPA, spring boot integration test framework. The database used currently is an in memory HSQLDB ORM database. 

## Database Design
![alt tag](https://github.com/ssrinivasulu/ticket_service/blob/master/ticket-service-erd.jpg)

## Tables and high-level description
---------------------------------

Customer - Model to represent customer or user who will be requesting reservation tickets. 

### Master table with static data
Below are the tables where we store the data associated to the venue, events and associated seats information is stored. 
Event_Venue - Model to represent the event venue where the event activity will be performed.

Event_venue_ticket_level - Model to represent the different event venue levels where seating will be associated to it. This table will have many to one association with Event_venue table. 

Seat - Model to represent all the seating locations associated to Event_venue_ticket_level. This table will have many to one association with Event_venue_ticket_level table.   

##Transaction Table
Below are the key tables where the processing of reservation request from customers are handled. 
Event_reservation - This tables handles the reservation request initated by customer.
Seat_reservated - This tables holds the seat and reservation details based on the reservation booking from customer. 


# Service Layer
Below service layer implemntation  are the key components of this Ticket Service application where functionlaities specific to Event Venue management as well as Ticket Management services are exposed.
![alt tag](https://github.com/ssrinivasulu/ticket_service/blob/master/ticket_service-ServiceLayer.jpg)

Below is the ticket service process flow to provide an overview on customer request to initate the reservation request to check and hold tickets. Also provided event management service and ticket management service api support to check available seats, creating reservation with HOLD status and confirming seat and reservation.
![alt tag](https://github.com/ssrinivasulu/ticket_service/blob/master/ticket_service_process_flow.jpg)

