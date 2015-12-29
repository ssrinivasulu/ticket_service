/**
 * 
 */
package com.demo.ticketservice.dao;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.demo.ticketservice.domain.EventVenue;

/**
 * @author ssrinivasulu
 *
 */
@RestResource (path = "eventVenue", rel = "eventVenue")
public interface EventVenueRepository extends PagingAndSortingRepository<EventVenue, Long> {
	List<EventVenue> findById(Long id);
}
