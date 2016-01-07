/**
 * 
 */
package com.demo.ticketservice.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.demo.ticketservice.domain.EventVenueTicketLevel;
import com.demo.ticketservice.domain.Seat;

/**
 * @author ssrinivasulu
 *
 */
@RestResource (path = "seats", rel = "seats")
public interface SeatRepository extends PagingAndSortingRepository<Seat, Long> {
	Page<Seat> findById(Long id, Pageable pageable);

	List<Seat> findById(Long id);
	
	List<Seat> findByEventVenueTicketLevelId(Long id);
	
	//@Query("select count(s) from com.demo.ticketservice.domain.Seat s left outer join com.demo.ticketservice.domain.SeatReserved sr on (s.id = sr.seat.id)")
	@Query("select count(s) from com.demo.ticketservice.domain.Seat s WHERE NOT EXISTS (select sr from com.demo.ticketservice.domain.SeatReserved sr where sr.seat.id = s.id)")
	int numberOfavailableSeats();
	
	@Query("select count(s) from com.demo.ticketservice.domain.Seat s WHERE s.eventVenueTicketLevel =?1 and NOT EXISTS (select sr from com.demo.ticketservice.domain.SeatReserved sr where sr.seat.id = s.id) ")
	int numberOfavailableSeatsByEventVenueTicketLevelId(EventVenueTicketLevel eventVenueTicketLevel);
	
	//@Query("select count(s) from com.demo.ticketservice.domain.Seat s left outer join com.demo.ticketservice.domain.SeatReserved sr on (s.id = sr.seat.id)")
	@Query("select s from com.demo.ticketservice.domain.Seat s WHERE NOT EXISTS (select sr from com.demo.ticketservice.domain.SeatReserved sr where sr.seat.id = s.id) order by s.eventVenueTicketLevel")
	List<Seat> retriveAvailableSeats();
	
	@Query("select s from com.demo.ticketservice.domain.Seat s WHERE s.eventVenueTicketLevel =?1 and NOT EXISTS (select sr from com.demo.ticketservice.domain.SeatReserved sr where sr.eventReservation.reservationStatus in ('HOLD', 'COMPLETE') and sr.seat.id = s.id) order by s.eventVenueTicketLevel")
	List<Seat> retriveAvailableSeatsByEventVenueTicketLevelId(EventVenueTicketLevel eventVenueTicketLevel);
}
