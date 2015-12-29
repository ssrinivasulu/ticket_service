package com.demo.ticketservice.service;

import java.util.Set;

import com.demo.ticketservice.domain.Customer;
import com.demo.ticketservice.domain.EventVenue;
import com.demo.ticketservice.domain.EventVenueTicketLevel;
import com.demo.ticketservice.domain.Seat;

/**
 * @author ssrinivasulu
 *
 */
public interface EventVenueService {
	
	Customer createCustomer(String firstName, String lastName, String middleName, String customerEmail);
	
	Customer removeCustomer(long customerId);
	
	Customer updateCustomer(long customerId, String firstName, String lastName, String middleName, String customerEmail);
	
	Customer findCustomerById(long customerId);
	
	Customer findByCustomerEmail(String emailId);
	
	EventVenue findEventVenueById(long eventVenueId);
	
	EventVenueTicketLevel findEventVenueTicketLevelById(long eventVenueTicketLevelId);

	EventVenue createEventVenue(String name, String description, Set<EventVenueTicketLevel> eventVenueTicketLevels);

	EventVenueTicketLevel createEventVenueTicketLevel(long eventVenueId, String levelName, 
			String levelDescription, double levelPrice, int levelRows, int levelNumOfSeatsInRow, Set<Seat> seats);

	Seat createEventVenueTicketLevelSeat(int rowNum, int seatNumber, long eventVenueTicketLevelId);
	
	Seat removeEventVenueTicketLevelSeat(long seatId);
	
	
}
