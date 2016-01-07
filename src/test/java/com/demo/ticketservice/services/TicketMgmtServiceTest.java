package com.demo.ticketservice.services;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.demo.ticketservice.TicketServiceApplication;
import com.demo.ticketservice.config.TicketServiceRedisConfig;
import com.demo.ticketservice.config.TicketServiceRedisKeyExpiredListener;
import com.demo.ticketservice.dao.SeatRepository;
import com.demo.ticketservice.dao.EventVenueTicketLevelRepository;
import com.demo.ticketservice.domain.EventReservation;
import com.demo.ticketservice.domain.EventVenueTicketLevel;
import com.demo.ticketservice.domain.Seat;
import com.demo.ticketservice.model.SeatHold;
import com.demo.ticketservice.service.TicketMgmtService;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author ssrinivasulu
 *
 */
@SpringApplicationConfiguration(TicketServiceApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class TicketMgmtServiceTest extends AbstractTicketServiceTest{
	@Autowired
    private ApplicationContext ctx;
	private static Logger logger = LoggerFactory.getLogger(TicketServiceApplication.class);
	
	@Autowired
	TicketMgmtService ticketMgmtService;
	
	@Autowired
	private SeatRepository seatRepository;
	
	@Autowired
	private EventVenueTicketLevelRepository eventVenueTicketLevelRepository;
	
	/**
	 * This test case is to check number of seats available
	 * @return
	 * @throws Throwable
	 */
	@Test
	public void testNumOfSeatsAvailable() throws Throwable {
		int numberOfSeatsAvailable = ticketMgmtService.numSeatsAvailable(Optional.of( 2L ));
		assertThat(numberOfSeatsAvailable, is(not(equalTo(seatRepository.count()))));
	}
	
	@Test
	public void testRetriveAvailableSeats() throws Throwable {
		List<Seat> availableSeats = ticketMgmtService.retriveAvailableSeats(Optional.of( 2L ));
		assertThat(availableSeats.size(), is(not(equalTo(seatRepository.count()))));
	}
	
	//@Test
	public void testRetriveEventReservationByPurchaseConfirmationId() throws Throwable {
		String purchaseConfirmationId = "";
		EventReservation eventReservation = ticketMgmtService.retriveEventReservationByPurchaseConfirmationId(purchaseConfirmationId);
		assertThat(eventReservation.getPurchaseConfirmationId(), is(equalTo(purchaseConfirmationId)));
	}
	
	@Test
	public void testFindAndHoldSeats() throws Throwable {
		redisListenerStart();
		List<Seat> availableSeats = ticketMgmtService.retriveAvailableSeats(Optional.empty());
		SeatHold seatHold = ticketMgmtService.findAndHoldSeats(20, Optional.of( 1 ), Optional.of( 3 ), "ssinivasulu@temp.com");
		assertThat(seatHold.getEventReservations().get(0).getNumberOfSeats(), is((equalTo(20))));

		//Creating seats for eventVenueTicketLevel 2
		EventVenueTicketLevel eventVenueTicketLevel = eventVenueTicketLevelRepository.findOne(2L);
		//checking to make sure there is no seats for level 2
		availableSeats = ticketMgmtService.retriveAvailableSeats(Optional.of( 2L ));
		assertThat(availableSeats.size(), is((equalTo(0))));
		Set<Seat> seats = new HashSet<Seat>();
		for (int i = 1; i <= eventVenueTicketLevel.getLevelRows(); i++) {
			for (int j = 1; j <= eventVenueTicketLevel.getLevelNumOfSeatsInRow(); j++) {
				seats.add(new Seat(null, i, j, eventVenueTicketLevel));
			}
			eventVenueTicketLevel.setSeats(seats);
			assertThat(eventVenueTicketLevel.getSeats().size(), is(i*eventVenueTicketLevel.getLevelNumOfSeatsInRow()));
			System.out.println(eventVenueTicketLevel.getSeats().size());
		}
		eventVenueTicketLevelRepository.save(eventVenueTicketLevel);
		availableSeats = ticketMgmtService.retriveAvailableSeats(Optional.of( 2L ));
		seatHold = ticketMgmtService.findAndHoldSeats(20, Optional.of( 2 ), Optional.empty(), "ssinivasulu@temp.com");
		//seatHold.getEventReservations().get(0).getId()
		assertThat(ticketMgmtService.retriveAvailableSeats(Optional.of( 2L )).size(), is((availableSeats.size()-20)));
		ticketMgmtService.reserveSeats(seatHold.getEventReservations().get(0).getId(), "ssinivasulu@temp.com");
		assertThat(ticketMgmtService.retriveAvailableSeats(Optional.of( 2L )).size(), is((availableSeats.size())));
	}
	
	/**
	 * This test case is to Hold the N number of seats on a particular level and confirm.
	 * @throws Throwable
	 */
	@Test
	public void testReserveSeats() throws Throwable {
		List<Seat> availableSeats =null;
		SeatHold seatHold = null;
		//Creating seats for eventVenueTicketLevel 3
		EventVenueTicketLevel eventVenueTicketLevel = eventVenueTicketLevelRepository.findOne(3L);
		//checking to make sure there is no seats for level 3
		availableSeats = ticketMgmtService.retriveAvailableSeats(Optional.of( 3L ));
		assertThat(availableSeats.size(), is((equalTo(0))));
		Set<Seat> seats = new HashSet<Seat>();
		for (int i = 1; i <= eventVenueTicketLevel.getLevelRows(); i++) {
			for (int j = 1; j <= eventVenueTicketLevel.getLevelNumOfSeatsInRow(); j++) {
				seats.add(new Seat(null, i, j, eventVenueTicketLevel));
			}
			eventVenueTicketLevel.setSeats(seats);
			assertThat(eventVenueTicketLevel.getSeats().size(), is(i*eventVenueTicketLevel.getLevelNumOfSeatsInRow()));
			System.out.println(eventVenueTicketLevel.getSeats().size());
		}
		eventVenueTicketLevelRepository.save(eventVenueTicketLevel);
		availableSeats = ticketMgmtService.retriveAvailableSeats(Optional.of( 3L ));
		seatHold = ticketMgmtService.findAndHoldSeats(20, Optional.of( 3 ), Optional.empty(), "ssinivasulu@temp.com");
		assertThat(ticketMgmtService.retriveAvailableSeats(Optional.of( 3L )).size(), is((availableSeats.size()-20)));
		String Confirmation_number = ticketMgmtService.reserveSeats(seatHold.getEventReservations().get(0).getId(), "ssinivasulu@temp.com");
		System.out.println(Confirmation_number);
	}
	

}
