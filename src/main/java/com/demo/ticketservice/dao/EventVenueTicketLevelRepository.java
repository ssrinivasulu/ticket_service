/**
 * 
 */
package com.demo.ticketservice.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.demo.ticketservice.domain.Customer;
import com.demo.ticketservice.domain.EventVenueTicketLevel;

/**
 * @author ssrinivasulu
 *
 */
@RestResource (path = "eventVenueTicketLevel", rel = "eventVenueTicketLevel")
public interface EventVenueTicketLevelRepository extends PagingAndSortingRepository<EventVenueTicketLevel, Long> {
	List<EventVenueTicketLevel> findById(Long id);
	Page<Customer> findById(Long id, Pageable pageable);
	List<EventVenueTicketLevel> findByEventVenueId(Long id);
}
