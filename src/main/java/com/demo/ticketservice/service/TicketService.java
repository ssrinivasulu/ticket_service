/**
 * 
 */
package com.demo.ticketservice.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.demo.ticketservice.domain.EventReservation;
import com.demo.ticketservice.domain.ReservationStatusCode;
import com.demo.ticketservice.domain.Seat;
import com.demo.ticketservice.domain.SeatReserved;
import com.demo.ticketservice.model.SeatHold;

/**
 * @author ssrinivasulu
 *
 */
public interface TicketService {
/**
* The number of seats in the requested level that are neither held nor reserved
*
* @param venueLevel a numeric venue level identifier to limit the search
* @return the number of tickets available on the provided level
*/
  int numSeatsAvailable(Optional<Long> venueLevel);
  
  /**
  * The number of seats in the requested level that are neither held nor reserved
  *
  * @param venueLevel a numeric venue level identifier to limit the search
  * @return the number of seats available on the provided level
  */
  public List<Seat> retriveAvailableSeats(Optional<Long> venueLevel);
  
/**
* Find and hold the best available seats for a customer
*
* @param numSeats the number of seats to find and hold
* @param minLevel the minimum venue level
* @param maxLevel the maximum venue level
* @param customerEmail unique identifier for the customer
* @return a SeatHold object identifying the specific seats and related
information
*/
  SeatHold findAndHoldSeats(int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel, String customerEmail);
/**
* Commit seats held for a specific customer
*
* @param seatHoldId the seat hold identifier
* @param customerEmail the email address of the customer to which the seat hold
is assigned
* @return a reservation confirmation code
*/
String reserveSeats(long seatHoldReservationId, String customerEmail);


/**
 * Create EventReservation including associated seat details.
 * @param customerId
 * @param eventVenueTicketLevelId
 * @param seatReserveds
 * @param reservationStatus
 * @return EventReservation 
 */
EventReservation createEventReservation(long customerId, long eventVenueTicketLevelId, Set<SeatReserved> seatReserveds, ReservationStatusCode reservationStatus);

/**
 * @param eventReservationId
 * @param customerId
 * @param eventVenueTicketLevelId
 * @param seatReserveds
 * @param reservationStatus
 * @return
 */
EventReservation updateEventReservation(long eventReservationId, long eventVenueTicketLevelId, Set<SeatReserved> seatReserveds, ReservationStatusCode reservationStatus);
 
/**
 * @param eventReservationId
 * @return
 */
EventReservation deleteEventReservation(long eventReservationId);

/**
 * @param seatId
 * @param eventReservationId
 * @return
 */
SeatReserved createSeatReserved(long seatId, long eventReservationId);
}