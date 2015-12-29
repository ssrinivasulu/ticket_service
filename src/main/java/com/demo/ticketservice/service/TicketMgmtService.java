package com.demo.ticketservice.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.demo.ticketservice.dao.EventReservationRepository;
import com.demo.ticketservice.dao.SeatRepository;
import com.demo.ticketservice.dao.SeatReservedRepository;
import com.demo.ticketservice.domain.Customer;
import com.demo.ticketservice.domain.EventReservation;
import com.demo.ticketservice.domain.EventVenueTicketLevel;
import com.demo.ticketservice.domain.ReservationStatusCode;
import com.demo.ticketservice.domain.Seat;
import com.demo.ticketservice.domain.SeatReserved;
import com.demo.ticketservice.exception.RecordNotFoundException;
import com.demo.ticketservice.model.SeatHold;
@Component
public class TicketMgmtService implements TicketService{
	
	@Autowired
	private EventReservationRepository  eventReservationRepository;
	@Autowired
	private SeatReservedRepository seatReservedRepository;
	@Autowired
	private EventVenueMgmtService eventVenueMgmtService;
	@Autowired
	private SeatRepository seatRepository;
	
	@Autowired JdbcTemplate jdbcTemplate;
	
	@Override
	public int numSeatsAvailable(Optional<Long> venueLevel) {
		int numSeatsAvailable = 0;
		EventVenueTicketLevel eventVenueTicketLevel = eventVenueMgmtService.findEventVenueTicketLevelById(venueLevel.get());
		if(venueLevel.isPresent())
			numSeatsAvailable = seatRepository.numberOfavailableSeatsByEventVenueTicketLevelId(eventVenueTicketLevel);
		else
			numSeatsAvailable = seatRepository.numberOfavailableSeats();
		return numSeatsAvailable;
	}
	
	@Override
	public List<Seat> retriveAvailableSeats(Optional<Long> venueLevel){
		List<Seat> availableSeats = null;
		if(venueLevel.isPresent()){
			EventVenueTicketLevel eventVenueTicketLevel = eventVenueMgmtService.findEventVenueTicketLevelById(venueLevel.get());
			availableSeats = seatRepository.retriveAvailableSeatsByEventVenueTicketLevelId(eventVenueTicketLevel);
		}
		else
			availableSeats = seatRepository.retriveAvailableSeats();
		return availableSeats;
	}
	
	@Override
	public SeatHold findAndHoldSeats(int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel,
			String customerEmail) {
		List<Seat> availableSeatBeforeBooked=new ArrayList<Seat>();
		Set<SeatReserved> availableSeatReservedBeforeBooked=new HashSet<SeatReserved>();
		List<Seat> tempSeatList=new ArrayList<Seat>();
		List<EventReservation> eventReservations = new ArrayList<EventReservation>();
		Customer customer = eventVenueMgmtService.findByCustomerEmail(customerEmail);
		if ( customer == null )
            throw new RecordNotFoundException("No customer found for customerId: " + customerEmail);
		
		if(minLevel.isPresent() && maxLevel.isPresent()) {
			for (int i = minLevel.get(); i <= maxLevel.get(); i++) {
				EventVenueTicketLevel eventVenueTicketLevel = eventVenueMgmtService.findEventVenueTicketLevelById(i);
				if ( eventVenueTicketLevel != null ){
					//availableSeatBeforeBooked.addAll(retriveAvailableSeats(Optional.of(eventVenueTicketLevel.getId())));
					tempSeatList = retriveAvailableSeats(Optional.of(eventVenueTicketLevel.getId()));
					if(tempSeatList.size()+availableSeatBeforeBooked.size() >numSeats){
						int count = numSeats-availableSeatBeforeBooked.size();
						for (int j = 0; j < count; j++) {
							availableSeatBeforeBooked.add(tempSeatList.get(j));
							availableSeatReservedBeforeBooked.add(new SeatReserved(null, tempSeatList.get(j), null));
						}
						eventReservations.add(createEventReservation(customer.getId(), eventVenueTicketLevel.getId(), availableSeatReservedBeforeBooked, ReservationStatusCode.HOLD));
						break;
					}
					else{
						availableSeatBeforeBooked.addAll(retriveAvailableSeats(Optional.of(eventVenueTicketLevel.getId())));
						for (Seat seat : tempSeatList) {
							SeatReserved seatReserved = new SeatReserved();
							seatReserved.setSeat(seat);
							availableSeatReservedBeforeBooked.add(seatReserved);
						}
						eventReservations.add(createEventReservation(customer.getId(), eventVenueTicketLevel.getId(), availableSeatReservedBeforeBooked, ReservationStatusCode.HOLD));
						continue;
					}
					
				}
			}	
		}
		else {
			if(minLevel.isPresent() && !maxLevel.isPresent()){
				EventVenueTicketLevel eventVenueTicketLevel = eventVenueMgmtService.findEventVenueTicketLevelById(minLevel.get());
				tempSeatList = retriveAvailableSeats(Optional.of(eventVenueTicketLevel.getId()));
				if(tempSeatList.size() >numSeats){
					int count = numSeats-availableSeatBeforeBooked.size();
					for (int j = 0; j < count; j++) {
						availableSeatBeforeBooked.add(tempSeatList.get(j));
						availableSeatReservedBeforeBooked.add(new SeatReserved(null, tempSeatList.get(j), null));
					}
				}
				else{
					availableSeatBeforeBooked.addAll(tempSeatList);
					for (Seat seat : tempSeatList) {
						SeatReserved seatReserved = new SeatReserved();
						seatReserved.setSeat(seat);
						availableSeatReservedBeforeBooked.add(seatReserved);
					}
				}
				eventReservations.add(createEventReservation(customer.getId(), eventVenueTicketLevel.getId(), availableSeatReservedBeforeBooked, ReservationStatusCode.HOLD));
			}	
			else if(maxLevel.isPresent() && !minLevel.isPresent()) {
				EventVenueTicketLevel eventVenueTicketLevel = eventVenueMgmtService.findEventVenueTicketLevelById(maxLevel.get());
				tempSeatList = retriveAvailableSeats(Optional.of(eventVenueTicketLevel.getId()));
				if(tempSeatList.size() >numSeats){
					int count = numSeats-availableSeatBeforeBooked.size();
					for (int j = 0; j < count; j++) {
						availableSeatBeforeBooked.add(tempSeatList.get(j));
						availableSeatReservedBeforeBooked.add(new SeatReserved(null, tempSeatList.get(j), null));
					}
				}
				else{
					availableSeatBeforeBooked.addAll(tempSeatList);
					for (Seat seat : tempSeatList) {
						SeatReserved seatReserved = new SeatReserved();
						seatReserved.setSeat(seat);
						availableSeatReservedBeforeBooked.add(seatReserved);
					}
				}
				eventReservations.add(createEventReservation(customer.getId(), eventVenueTicketLevel.getId(), availableSeatReservedBeforeBooked, ReservationStatusCode.HOLD));
			}
			else{
				
				tempSeatList = retriveAvailableSeats(Optional.empty());
				//need to group available seats by EventVenueTicketLevel and then book reservation for each level.
				if(tempSeatList.size() >numSeats){
					int count = numSeats-availableSeatBeforeBooked.size();
					for (int j = 0; j < count; j++) {
						availableSeatBeforeBooked.add(tempSeatList.get(j));
					}
				}
				else{
					availableSeatBeforeBooked.addAll(tempSeatList);
				}
				
				Long tempEventVenueTicketLevelId = null;
				for (Seat seat : availableSeatBeforeBooked) {
					if(tempEventVenueTicketLevelId ==null)
						tempEventVenueTicketLevelId = seat.getEventVenueTicketLevel().getId();
					if(tempEventVenueTicketLevelId.equals(seat.getEventVenueTicketLevel().getId()))
						availableSeatReservedBeforeBooked.add(new SeatReserved(null, seat, null));
					else{
						eventReservations.add(createEventReservation(customer.getId(), tempEventVenueTicketLevelId, availableSeatReservedBeforeBooked, ReservationStatusCode.HOLD));
						availableSeatReservedBeforeBooked.clear();
						availableSeatReservedBeforeBooked.add(new SeatReserved(null, seat, null));
						tempEventVenueTicketLevelId = seat.getEventVenueTicketLevel().getId();
					}
					eventReservations.add(createEventReservation(customer.getId(), tempEventVenueTicketLevelId, availableSeatReservedBeforeBooked, ReservationStatusCode.HOLD));
				}
			}	
			
		}
		return new SeatHold(eventReservations);
	}

	/* 
	 * This service is to confirm the existing hold resrvation.
	 * @see com.demo.ticketservice.service.TicketService#reserveSeats(long, java.lang.String)
	 */
	@Override
	public String reserveSeats(long seatHoldReservationId, String customerEmail) {
		EventReservation eventReservation = eventReservationRepository.findOne(seatHoldReservationId);
		if ( eventReservation == null )
            throw new RecordNotFoundException("No eventReservation found for seatHoldReservationId: " + seatHoldReservationId);
		
		if(eventReservation.getCustomer()!=null && eventReservation.getCustomer().getCustomerEmail()!=null
				&& !(eventReservation.getCustomer().getCustomerEmail().equals(customerEmail)))
			throw new RecordNotFoundException("Customer email not matched with the seatHoldReservationId: " + seatHoldReservationId);

		eventReservation.setReservationStatus(ReservationStatusCode.CONFIRMED);
		eventReservation.setPurchaseConfirmationId(UUID.randomUUID().toString());
		eventReservationRepository.save(eventReservation);
		return eventReservation.getPurchaseConfirmationId();
	}

	@Override
	public EventReservation createEventReservation(long customerId, long eventVenueTicketLevelId,
			Set<SeatReserved> seatReserveds, ReservationStatusCode reservationStatus) {
		EventReservation eventReservation = new EventReservation();
		Customer customer = eventVenueMgmtService.findCustomerById(customerId);
		if ( customer == null )
            throw new RecordNotFoundException("No customer found for customerId: " + customerId);
		eventReservation.setCustomer(eventVenueMgmtService.findCustomerById(customerId));
		EventVenueTicketLevel eventVenueTicketLevel = eventVenueMgmtService.findEventVenueTicketLevelById(eventVenueTicketLevelId);
		if ( eventVenueTicketLevel == null )
            throw new RecordNotFoundException("No eventVenueTicketLevel record found for eventVenueTicketLevelId: " + eventVenueTicketLevelId);
		eventReservation.setEventVenueTicketLevel(eventVenueTicketLevel);
		if(reservationStatus.equals(ReservationStatusCode.CONFIRMED))
			eventReservation.setPurchaseConfirmationId(UUID.randomUUID().toString());
		eventReservation.setNumberOfSeats(seatReserveds.size());
		eventReservation.setCost(eventVenueTicketLevel.getLevelPrice()*seatReserveds.size());
		eventReservation.setActive(true);
		eventReservation.setCustomerPaymentMethod("CreditCard");
		eventReservation.setPaid(true);
		eventReservation.setReservationStatus(reservationStatus);
		eventReservation.setSeatReserveds(seatReserveds);
		eventReservationRepository.save(eventReservation);
		return eventReservation;
	}

	@Override
	public EventReservation updateEventReservation(long eventReservationId, long eventVenueTicketLevelId, 
			Set<SeatReserved> seatReserveds, ReservationStatusCode reservationStatus) {
		EventReservation eventReservation = eventReservationRepository.findOne(eventReservationId);
		if ( eventReservation == null )
            throw new RecordNotFoundException("No eventReservation found for eventReservationId: " + eventReservationId);
		EventVenueTicketLevel eventVenueTicketLevel = eventVenueMgmtService.findEventVenueTicketLevelById(eventVenueTicketLevelId);
		if ( eventVenueTicketLevel == null )
            throw new RecordNotFoundException("No eventVenueTicketLevel record found for eventVenueTicketLevelId: " + eventVenueTicketLevelId);
		eventReservation.setEventVenueTicketLevel(eventVenueTicketLevel);
		eventReservation.setSeatReserveds(seatReserveds);
		if(reservationStatus.equals(ReservationStatusCode.CONFIRMED))
			eventReservation.setPurchaseConfirmationId(UUID.randomUUID().toString());
		eventReservation.setReservationStatus(reservationStatus);
		eventReservationRepository.save(eventReservation);
		return eventReservation;
	}

	@Override
	public EventReservation deleteEventReservation(long eventReservationId) {
		EventReservation eventReservation = eventReservationRepository.findOne(eventReservationId);
		if ( eventReservation == null )
            throw new RecordNotFoundException("No eventReservation found for eventReservationId: " + eventReservationId);
		eventReservationRepository.delete(eventReservation);
		return eventReservation;
	}

	@Override
	public SeatReserved createSeatReserved(long seatId, long eventReservationId) {
		Seat seat = seatRepository.findOne(seatId);
		if ( seat == null )
            throw new RecordNotFoundException("No seat found for seatId: " + seatId);
		EventReservation eventReservation = eventReservationRepository.findOne(eventReservationId);
		if ( eventReservation == null )
            throw new RecordNotFoundException("No eventReservation found for eventReservationId: " + eventReservationId);
		
		SeatReserved seatReserved = new SeatReserved();
		seatReserved.setSeat(seat);
		seatReserved.setEventReservation(eventReservation);
		seatReservedRepository.save(seatReserved);
		return seatReserved;
	}

}
