/**
 * 
 */
package com.demo.ticketservice.service.aspect;

import java.time.Duration;
import java.time.Instant;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.demo.ticketservice.domain.EventReservation;
import com.demo.ticketservice.domain.ReservationStatusCode;

import redis.clients.jedis.Jedis;

/**
 * This ManageEventRegRedisAspect service is responsible for interacting with Redis in inserting and deleting the cache data 
 * for managing customer time Event Registration HOLD 
 * @author ssrinivasulu
 *
 */
@Aspect
@Component
public class ManageEventRegRedisAspect extends AbstractEventRegRedisAspect{
	private static final Logger LOGGER = LoggerFactory.getLogger(ManageEventRegRedisAspect.class);
	
    @Value("${ticketservice.redis.messages.ttl.duration}")
    private long durationMinutes;
    
    @After("execution(* com.demo.ticketservice.dao.EventReservationRepository.save(..))")
    public void interceptMessage(JoinPoint joinPoint) {
            
    	EventReservation eventReservation = (EventReservation) joinPoint.getArgs()[0];
    
        // this publishes the message
    	if(eventReservation.getReservationStatus().equals(ReservationStatusCode.HOLD))
    		insertHoldSeatsToRedisForExpiryCheck(eventReservation);
    	if(eventReservation.getReservationStatus().equals(ReservationStatusCode.CONFIRMED))
    		cleanpHoldEventsInRedisCache(eventReservation.getPurchaseConfirmationId());
    }
    
    /**
	 * This method is to save the redis key entry for a particular event registration, 
	 * typically all the event registration with HOLD status are stored in redis cache and TTL parameter is attached to it.
	 * @param eventReservation
	 */
	private void insertHoldSeatsToRedisForExpiryCheck(EventReservation eventReservation){
		LOGGER.debug("Inside Redis cache for saving Event HOLD Registration");
        final Jedis publisherJedis = jedisPool.getResource();
        publisherJedis.set(stringRedisSerializer.serialize(eventReservation.getPurchaseConfirmationId()), jsonRedisSerializer.serialize(eventReservation));
        publisherJedis.publish(stringRedisSerializer.serialize(eventReservation.getPurchaseConfirmationId()), jsonRedisSerializer.serialize(eventReservation));
        publisherJedis.expireAt(stringRedisSerializer.serialize(eventReservation.getPurchaseConfirmationId()), Instant.now().plus(Duration.ofMinutes(1)).getEpochSecond());
        LOGGER.debug("Successfully inserted registration hold data to redis for distributed caching");
	}
	
	/**
	 * This method is to cleanup the redis key entry stored for a particular event registration, 
	 * typically all the event registration with HOLD status are store in redis cache and TTL parameter is attached to it.
	 * @param purchaseConfirmationId
	 */
	private void cleanpHoldEventsInRedisCache(String purchaseConfirmationId){
		LOGGER.debug("Inside Redis cache cleanup for deleting Event HOLD Registration");
		final Jedis publisherJedis = jedisPool.getResource();
		if(publisherJedis.get(stringRedisSerializer.serialize(purchaseConfirmationId))!=null) {
			publisherJedis.del(purchaseConfirmationId);
	        LOGGER.debug("Successfully deleted registration HOLD data from redis as customer confirmed registration");
		}
	}
}
