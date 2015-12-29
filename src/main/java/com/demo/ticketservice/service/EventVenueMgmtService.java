package com.demo.ticketservice.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo.ticketservice.dao.CustomerRepository;
import com.demo.ticketservice.dao.EventVenueRepository;
import com.demo.ticketservice.dao.EventVenueTicketLevelRepository;
import com.demo.ticketservice.dao.SeatRepository;
import com.demo.ticketservice.domain.Customer;
import com.demo.ticketservice.domain.EventVenue;
import com.demo.ticketservice.domain.EventVenueTicketLevel;
import com.demo.ticketservice.domain.Seat;

/**
 * @author ssrinivasulu
 */
@Component
public class EventVenueMgmtService implements EventVenueService{
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private EventVenueRepository eventVenueRepository;
	@Autowired
	private EventVenueTicketLevelRepository eventVenueTicketLevelRepository;
	@Autowired
	private SeatRepository seatRepository;
	
	public String getHelloMessage() {
		return "Hello ";
	}

	@Override
	public Customer createCustomer(String firstName, String lastName, String middleName, String customerEmail) {
		Customer customer = new Customer();
		customer.setFirstName(firstName);
		customer.setLastName(lastName);
		customer.setMiddleName(middleName);
		customer.setCustomerEmail(customerEmail);
		customerRepository.save(customer);
		return customer;
	}

	@Override
	public Customer removeCustomer(long customerId) {
		Customer customer = customerRepository.findOne(customerId);
		this.customerRepository.delete(customerId);
		return customer;
	}

	@Override
	public Customer updateCustomer(long customerId, String firstName, String lastName, String middleName,
			String customerEmail) {
		Customer customer = customerRepository.findOne(customerId);
		customer.setFirstName(firstName);
		customer.setLastName(lastName);
		customer.setMiddleName(middleName);
		customer.setCustomerEmail(customerEmail);
		return customerRepository.save(customer);
	}

	@Override
	public Customer findCustomerById(long customerId) {
		return customerRepository.findOne(customerId);
	}
	
	@Override
	public Customer findByCustomerEmail(String emailId) {
		return customerRepository.findByCustomerEmail(emailId);
	}
	
	@Override
	public EventVenue findEventVenueById(long eventVenueId) {
		return eventVenueRepository.findOne(eventVenueId);
	}
	
	
	@Override
	public EventVenueTicketLevel findEventVenueTicketLevelById(long eventVenueTicketLevelId) {
		return eventVenueTicketLevelRepository.findOne(eventVenueTicketLevelId);
	}

	@Override
	public EventVenue createEventVenue(String name, String description,
			Set<EventVenueTicketLevel> eventVenueTicketLevels) {
		
		EventVenue eventVenue = new EventVenue();
		eventVenue.setName(name);
		eventVenue.setDescription(description);
		eventVenue.setEventVenueTicketLevels(eventVenueTicketLevels);
		eventVenueRepository.save(eventVenue);
		return eventVenue;
	}

	@Override
	public EventVenueTicketLevel createEventVenueTicketLevel(long eventVenueId, String levelName,
			String levelDescription, double levelPrice, int levelRows, int levelNumOfSeatsInRow, Set<Seat> seats) {
		EventVenueTicketLevel eventVenueTicketLevel = new EventVenueTicketLevel();
		eventVenueTicketLevel.setLevelName(levelName);
		eventVenueTicketLevel.setLevelDescription(levelDescription);
		eventVenueTicketLevel.setLevelPrice(levelPrice);
		eventVenueTicketLevel.setLevelRows(levelRows);
		eventVenueTicketLevel.setLevelNumOfSeatsInRow(levelNumOfSeatsInRow);
		eventVenueTicketLevel.setSeats(seats);
		EventVenue eventVenue = eventVenueRepository.findOne(eventVenueId);
		eventVenue.getEventVenueTicketLevels().add(eventVenueTicketLevel);
		eventVenueTicketLevel.setEventVenue(eventVenue);
		eventVenueTicketLevelRepository.save(eventVenueTicketLevel);
		return eventVenueTicketLevel;
	}

	@Override
	public Seat createEventVenueTicketLevelSeat(int rowNum, int seatNumber, long eventVenueTicketLevelId) {
		Seat eventVenueTicketLevelSeat = new Seat();
		eventVenueTicketLevelSeat.setRowNum(rowNum);
		eventVenueTicketLevelSeat.setSeatNumber(seatNumber);
		EventVenueTicketLevel eventVenueTicketLevel = eventVenueTicketLevelRepository.findOne(eventVenueTicketLevelId);
		eventVenueTicketLevelSeat.setEventVenueTicketLevel(eventVenueTicketLevel);
		seatRepository.save(eventVenueTicketLevelSeat);		
		return eventVenueTicketLevelSeat;
	}

	@Override
	public Seat removeEventVenueTicketLevelSeat(long seatId) {
		Seat seat = seatRepository.findOne(seatId);
		this.seatRepository.delete(seatId);
		return seat;
	}

}
