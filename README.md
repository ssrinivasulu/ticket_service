# Ticket Service Application
Implement a simple ticket service that facilitates the discovery, temporary hold, and final reservation of seats within a high-demand performance venue.

## Overview
The current functionality is targetted specific to build service layer functionality in exposing the required api's to implement the ticket service UI application, the service layer api's are implemented and tested using Spring Boot, Spring Data JPA and spring boot integration test framework. The database currently used is an in memory HSQLDB ORM database. 

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
Below service layer implemntation are the key components of this Ticket Service application where functionalities specific to Event venue management as well as Ticket management services are exposed.
![alt tag](https://github.com/ssrinivasulu/ticket_service/blob/master/ticket_service-ServiceLayer.jpg)

Below is the ticket service process flow where customer initiates the reservation request to check and hold tickets. As part of event management service and ticket management service api support, customer should be able to check available seats, create reservation with HOLD/CONFIRMED status and confirming seat and reservation.
![alt tag](https://github.com/ssrinivasulu/ticket_service/blob/master/ticket_service_flow_with_Redis.jpg)

##Redis Cache to Hold and Expire Reservation
Redis in-memory data structure store is used as a cache layer to HOLD reservations requested by customer, reservation are HOLD to a specific configurable time interval and then set to EXPIRE if it passed the time interval. 
##Jedis Listener Configuration
We are using Jedis publish subscriber listener to access expired event messages from Redis and update those event registrations as EXPIRED in database. 
##Confirm reservation before the TTL interval
Customers confirming the reservation before the time to live configuration interval, we will be deleting the event registration from redis datastore and update the registration to CONFIRMED status in database. We are using spring AOP to intercept the save registration DAO service to check the status of registration and based on HOLD or CONFIRMED status events in redis will be deleted or inserted. 

# Build and Deploy
The project is built based on java8 and Maven build process. Below are the steps to execute the test case via maven command

export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_45.jdk/Contents/Home/jre/

mvn clean install
