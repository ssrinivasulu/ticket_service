package com.demo.ticketservice.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
/**
 * This service layer is responsible for exposing api's to initiate the booking 
 * reservation process as well as confirming the reservation process. Please follow associated 
 * TicketMgmtServiceTest test case to exercise the api's. 
 * 
 * @author ssrinivasulu
 *
 */
@Component
public class TicketMgmtService implements TicketService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TicketMgmtService.class);

	
	public static final String CHANNEL_NAME = "ticketServiceChannel";
	@Autowired
	private EventReservationRepository  eventReservationRepository;
	@Autowired
	private SeatReservedRepository seatReservedRepository;
	@Autowired
	private EventVenueMgmtService eventVenueMgmtService;
	@Autowired
	private SeatRepository seatRepository;
	
	/*@Autowired
	private JedisPool jedisPool;
	
	@Autowired
	private JsonRedisSerializer jsonRedisSerializer;
	
	@Autowired
	private StringRedisSerializer stringRedisSerializer;*/
	
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
	
	/**
	 * This method is to save the redis key entry for a particular event registration, 
	 * typically all the event registration with HOLD status are stored in redis cache and TTL parameter is attached to it.
	 * @param eventReservation
	 */
	/*private void insertHoldSeatsToRedisForExpiryCheck(EventReservation eventReservation){
		LOGGER.debug("Inside Redis cache for saving Event HOLD Registration");
        final Jedis publisherJedis = jedisPool.getResource();
        publisherJedis.set(stringRedisSerializer.serialize(eventReservation.getPurchaseConfirmationId()), jsonRedisSerializer.serialize(eventReservation));
        publisherJedis.publish(stringRedisSerializer.serialize(eventReservation.getPurchaseConfirmationId()), jsonRedisSerializer.serialize(eventReservation));
        publisherJedis.expireAt(stringRedisSerializer.serialize(eventReservation.getPurchaseConfirmationId()), Instant.now().plus(Duration.ofMinutes(1)).getEpochSecond());
        LOGGER.debug("Successfully inserted registration hold data to redis for distributed caching");
	}*/
	
	/**
	 * This method is to cleanup the redis key entry stored for a particular event registration, 
	 * typically all the event registration with HOLD status are store in redis cache and TTL parameter is attached to it.
	 * @param purchaseConfirmationId
	 */
	/*private void cleanpHoldEventsInRedisCache(String purchaseConfirmationId){
		LOGGER.debug("Inside Redis cache cleanup for deleting Event HOLD Registration");
		final Jedis publisherJedis = jedisPool.getResource();
		if(publisherJedis.get(stringRedisSerializer.serialize(purchaseConfirmationId))!=null) {
			publisherJedis.del(purchaseConfirmationId);
	        LOGGER.debug("Successfully deleted registration HOLD data from redis as customer confirmed registration");
		}
	}*/

	/** 
	 * 
	 * @see com.demo.ticketservice.service.TicketService#findAndHoldSeats(int, java.util.Optional, java.util.Optional, java.lang.String)
	 */
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
		/*for (EventReservation eventReservation : eventReservations) {
			insertHoldSeatsToRedisForExpiryCheck(eventReservation);
		}*/
		return new SeatHold(eventReservations);
	}

	/* 
	 * This service is to confirm the existing hold reservation.
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
		eventReservationRepository.save(eventReservation);
		//cleanpHoldEventsInRedisCache(eventReservation.getPurchaseConfirmationId());
		return eventReservation.getPurchaseConfirmationId();
	}

	/* (non-Javadoc)
	 * @see com.demo.ticketservice.service.TicketService#createEventReservation(long, long, java.util.Set, com.demo.ticketservice.domain.ReservationStatusCode)
	 */
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
		//if(reservationStatus.equals(ReservationStatusCode.CONFIRMED))
		eventReservation.setPurchaseConfirmationId(UUID.randomUUID().toString());
		eventReservation.setNumberOfSeats(seatReserveds.size());
		eventReservation.setCost(eventVenueTicketLevel.getLevelPrice()*seatReserveds.size());
		eventReservation.setActive(true);
		if(reservationStatus.equals(ReservationStatusCode.CONFIRMED)){
			eventReservation.setCustomerPaymentMethod("CreditCard");
			eventReservation.setPaid(true);
		}	
		eventReservation.setReservationStatus(reservationStatus);
		for (SeatReserved seatReserved : seatReserveds) {
			seatReserved.setEventReservation(eventReservation);
		}
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
	public EventReservation updateEventReservation(EventReservation eventReservation) {
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
	public EventReservation retriveEventReservationByPurchaseConfirmationId(String purchaseConfirmationId){
		EventReservation eventReservation = eventReservationRepository.findByPurchaseConfirmationId(purchaseConfirmationId);
		if ( eventReservation == null )
            throw new RecordNotFoundException("No eventReservation found for eventReservationId: " + purchaseConfirmationId);
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
