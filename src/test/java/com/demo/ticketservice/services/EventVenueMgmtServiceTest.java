package com.demo.ticketservice.services;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.demo.ticketservice.Application;
import com.demo.ticketservice.domain.Customer;
import com.demo.ticketservice.domain.EventVenue;
import com.demo.ticketservice.domain.EventVenueTicketLevel;
import com.demo.ticketservice.domain.Seat;
import com.demo.ticketservice.service.EventVenueMgmtService;

/**
 * @author ssrinivasulu
 *
 */
@SpringApplicationConfiguration(Application.class)
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class EventVenueMgmtServiceTest {
	
	@Autowired
	EventVenueMgmtService eventVenueMgmtService;
	
	@Test
    public void testCreatingCustomer() throws Throwable {
		Customer customer = eventVenueMgmtService.createCustomer("TestFirst", "TestLast", "A", "ssinivasulu@temp.com");

		//Create EventVenue
		eventVenueMgmtService.updateCustomer(customer.getId(), "TestFirst", "TestLast", "A", "ssinivasulu@temp1.com");
		customer = eventVenueMgmtService.findCustomerById(customer.getId());
		
		assertThat(customer.getCustomerEmail(), is(equalTo("ssinivasulu@temp1.com")));
		
		customer = eventVenueMgmtService.removeCustomer(customer.getId());
		customer = eventVenueMgmtService.findCustomerById(customer.getId());
		assertThat(customer, is(nullValue()));
		
    }
	
	/*NSERT INTO EVENT_VENUE(NAME, DESCRIPTION) VALUES ('Demo Stage Show', 'Demo Stage Show');

	INSERT INTO EVENT_VENUE_TICKET_LEVEL(EVENT_VENUE_ID, LEVEL_NAME, LEVEL_DESCRIPTION, LEVEL_PRICE, LEVEL_ROWS, LEVEL_NUMOFSEATSINROW) 
	VALUES (1, 'Orchestra', 'Orchestra', 100.0E0, 25, 50);
	INSERT INTO EVENT_VENUE_TICKET_LEVEL(EVENT_VENUE_ID, LEVEL_NAME, LEVEL_DESCRIPTION, LEVEL_PRICE, LEVEL_ROWS, LEVEL_NUMOFSEATSINROW) VALUES (1, 'Main', 'Main', 75.0E0, 20, 100);
	INSERT INTO EVENT_VENUE_TICKET_LEVEL(EVENT_VENUE_ID, LEVEL_NAME, LEVEL_DESCRIPTION, LEVEL_PRICE, LEVEL_ROWS, LEVEL_NUMOFSEATSINROW) VALUES (1, 'Balcony1', 'Balcony1', 50.0E0, 15, 100);
	INSERT INTO EVENT_VENUE_TICKET_LEVEL(EVENT_VENUE_ID, LEVEL_NAME, LEVEL_DESCRIPTION, LEVEL_PRICE, LEVEL_ROWS, LEVEL_NUMOFSEATSINROW) VALUES (1, 'Balcony2', 'Balcony2', 40.0E0, 15, 100);
*/
	@Test
    public void testCreatingEventVenue() throws Throwable {

		Set<EventVenueTicketLevel> eventVenueTicketLevels = new HashSet<EventVenueTicketLevel>();
		EventVenueTicketLevel eventVenueTicketLevel = new EventVenueTicketLevel();
		eventVenueTicketLevel.setLevelName("Orchestra1");
		eventVenueTicketLevel.setLevelDescription("Orchestra level1");
		eventVenueTicketLevel.setLevelPrice(100.0);
		eventVenueTicketLevel.setLevelRows(25);
		eventVenueTicketLevel.setLevelNumOfSeatsInRow(50);
		eventVenueTicketLevels.add(eventVenueTicketLevel);
		
		eventVenueTicketLevel = new EventVenueTicketLevel();
		eventVenueTicketLevel.setLevelName("Main1");
		eventVenueTicketLevel.setLevelDescription("Main level1");
		eventVenueTicketLevel.setLevelPrice(75.0);
		eventVenueTicketLevel.setLevelRows(20);
		eventVenueTicketLevel.setLevelNumOfSeatsInRow(100);
		eventVenueTicketLevels.add(eventVenueTicketLevel);
		
		eventVenueTicketLevel = new EventVenueTicketLevel();
		eventVenueTicketLevel.setLevelName("Balcony11");
		eventVenueTicketLevel.setLevelDescription("Balcony1 level1");
		eventVenueTicketLevel.setLevelPrice(50.0);
		eventVenueTicketLevel.setLevelRows(15);
		eventVenueTicketLevel.setLevelNumOfSeatsInRow(100);
		eventVenueTicketLevels.add(eventVenueTicketLevel);
		
		eventVenueTicketLevel = new EventVenueTicketLevel();
		eventVenueTicketLevel.setLevelName("Balcony22");
		eventVenueTicketLevel.setLevelDescription("Balcony2 level2");
		eventVenueTicketLevel.setLevelPrice(40.0);
		eventVenueTicketLevel.setLevelRows(15);
		eventVenueTicketLevel.setLevelNumOfSeatsInRow(100);
		eventVenueTicketLevels.add(eventVenueTicketLevel);
		
		//Adding Seat
		/*INSERT INTO SEAT(ROW_NUM, SEAT_NUMBER, EVENT_VENUE_TICKET_LEVEL_ID) VALUES (1, 1, 1);
		INSERT INTO SEAT(ROW_NUM, SEAT_NUMBER, EVENT_VENUE_TICKET_LEVEL_ID) VALUES (1, 2, 1);
		INSERT INTO SEAT(ROW_NUM, SEAT_NUMBER, EVENT_VENUE_TICKET_LEVEL_ID) VALUES (1, 3, 1);
		INSERT INTO SEAT(ROW_NUM, SEAT_NUMBER, EVENT_VENUE_TICKET_LEVEL_ID) VALUES (1, 4, 1);
		INSERT INTO SEAT(ROW_NUM, SEAT_NUMBER, EVENT_VENUE_TICKET_LEVEL_ID) VALUES (1, 5, 1);*/
		Seat seat =null;
		Set<Seat> seats = null;
		for (EventVenueTicketLevel eventVenueTicketLevel2 : eventVenueTicketLevels) {
			seats = new HashSet<Seat>();
			for (int i = 1; i <= eventVenueTicketLevel2.getLevelRows(); i++) {
				for (int j = 1; j <= eventVenueTicketLevel2.getLevelNumOfSeatsInRow(); j++) {
					seat = new Seat(null, i, j, eventVenueTicketLevel2);
					seats.add(seat);
				}
				eventVenueTicketLevel2.setSeats(seats);
				assertThat(eventVenueTicketLevel2.getSeats().size(), is(i*eventVenueTicketLevel2.getLevelNumOfSeatsInRow()));
				System.out.println(eventVenueTicketLevel2.getSeats().size());
			}
		}
		EventVenue eventVenue = eventVenueMgmtService.createEventVenue("Demo Stage Show test", "Demo Stage Show", eventVenueTicketLevels);
		assertThat(eventVenue.getName(), is(equalTo("Demo Stage Show test")));
		assertThat(eventVenue.getEventVenueTicketLevels().size(), is(equalTo(4)));
		eventVenueMgmtService.createEventVenueTicketLevel(eventVenue.getId(), "Balcony3", "Balcony3", 20.0, 10, 50, null);
		eventVenue = eventVenueMgmtService.findEventVenueById(eventVenue.getId());
		assertThat(eventVenue.getEventVenueTicketLevels().size(), is(equalTo(5)));
		
	}
	
}
