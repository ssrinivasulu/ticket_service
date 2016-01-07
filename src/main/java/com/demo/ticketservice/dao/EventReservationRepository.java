/**
 * 
 */
package com.demo.ticketservice.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.demo.ticketservice.domain.EventReservation;

/**
 * @author ssrinivasulu
 *
 */
@RestResource (path = "eventReservation", rel = "eventReservation")
public interface EventReservationRepository extends PagingAndSortingRepository<EventReservation, Long> {
	Page<EventReservation> findById(Long id, Pageable pageable);

	List<EventReservation> findById(Long id);
	
	public EventReservation findByPurchaseConfirmationId(String purchaseConfirmationId);
	
}
