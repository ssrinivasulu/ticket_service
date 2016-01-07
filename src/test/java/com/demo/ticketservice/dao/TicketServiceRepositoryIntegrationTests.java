/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.demo.ticketservice.dao;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.demo.ticketservice.TicketServiceApplication;
import com.demo.ticketservice.domain.Customer;
import com.demo.ticketservice.domain.EventReservation;
import com.demo.ticketservice.domain.EventVenue;
import com.demo.ticketservice.domain.EventVenueTicketLevel;
import com.demo.ticketservice.domain.Seat;
import com.demo.ticketservice.domain.SeatReserved;

/**
 * Integration tests for {@link TicketServiceRepository}.
 * @author ssrinivasulu
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TicketServiceApplication.class)
public class TicketServiceRepositoryIntegrationTests {

	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	EventVenueRepository eventVenueRepository;
	
	@Autowired
	EventVenueTicketLevelRepository eventVenueTicketLevelRepository;
	
	@Autowired
	SeatRepository seatRepository;
	
	@Autowired
	EventReservationRepository eventReservationRepository;
	
	@Autowired
	SeatReservedRepository seatReservedRepository;
	
	@Test
	public void findsFirstPageOfCustomers() {

		Page<Customer> customers = this.customerRepository.findAll(new PageRequest(0, 10));
		assertThat(customers.getTotalElements(), is(greaterThan(1L)));
	}
	
	@Test
	@Transactional
	public void findsFirstPageOfEventVenues() {

		Page<EventVenue> eventVenues = this.eventVenueRepository.findAll(new PageRequest(0, 10));
		List<EventVenue> eventVenueList = eventVenues.getContent();
		for (EventVenue eventVenue : eventVenueList) {
			List<EventVenueTicketLevel> eventVenueTicketLevels = eventVenueTicketLevelRepository.findByEventVenueId(eventVenue.getId());
			assertThat(eventVenueTicketLevels.size(), is(equalTo(4)));
			for (EventVenueTicketLevel eventVenueTicketLevel : eventVenueTicketLevels) {
				List<Seat> seats = seatRepository.findByEventVenueTicketLevelId(eventVenueTicketLevel.getId());
				assertThat(seats.size(), is(equalTo(100)));
				//System.out.println(seatRepository.numberOfavailableSeatsByEventVenueTicketLevelId(eventVenueTicketLevel.getId()));
				List<Seat> availableSeats = seatRepository.retriveAvailableSeatsByEventVenueTicketLevelId(eventVenueTicketLevel);
				//assertThat(seatRepository.numberOfavailableSeatsByEventVenueTicketLevelId(eventVenueTicketLevel), is(equalTo(availableSeats.size())));
				break;
			}
		}
		System.out.println(seatRepository.numberOfavailableSeats());
		List<Seat> availableSeats = seatRepository.retriveAvailableSeats();
		//assertThat(seatRepository.numberOfavailableSeats(), is(equalTo(availableSeats.size())));

		assertThat(eventVenues.getTotalElements(), is(equalTo(1L)));
	}
	
	@Test
	@Transactional
	public void findsFirstPageOfEventReservations() {
		Page<EventReservation> eventReservations = this.eventReservationRepository.findAll(new PageRequest(0, 10));
		List<EventReservation> eventReservationList = eventReservations.getContent();
		for (EventReservation eventReservation : eventReservationList) {
			System.out.println(eventReservation.getNumberOfSeats());
			List<SeatReserved> seatReserveds = seatReservedRepository.findByEventReservationId(eventReservation.getId());
			assertThat(seatReserveds.size(), is(equalTo(10)));
		}
	}
	
}
