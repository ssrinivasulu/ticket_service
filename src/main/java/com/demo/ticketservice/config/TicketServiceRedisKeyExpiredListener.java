package com.demo.ticketservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.ticketservice.domain.EventReservation;
import com.demo.ticketservice.domain.ReservationStatusCode;
import com.demo.ticketservice.service.TicketMgmtService;

import redis.clients.jedis.JedisPubSub;

/**
 * This listener class is responsible to receive expired "Ticket service HOLD event registration messages" and 
 * update the event registration to EXPIRED status.  
 * @author ssrinivasulu
 *
 */
@Service
public class TicketServiceRedisKeyExpiredListener extends JedisPubSub {

	private static Logger logger = LoggerFactory.getLogger(TicketServiceRedisKeyExpiredListener.class);
	
	@Autowired
	private TicketMgmtService ticketMgmtService;
	
	@Override
    public void onMessage(String channel, String message) {
        logger.info("Message received. Channel: {}, Msg: {}", channel, message);
        try{
	        EventReservation  eventReservation = ticketMgmtService.retriveEventReservationByPurchaseConfirmationId(message);
	        if ( eventReservation != null ){
	            eventReservation.setReservationStatus(ReservationStatusCode.EXPIRED);
	        	ticketMgmtService.updateEventReservation(eventReservation);
	        }	
        }
        catch(Exception e){
        	logger.info("Exception Message: {}", e.getMessage());
        }
    }
}