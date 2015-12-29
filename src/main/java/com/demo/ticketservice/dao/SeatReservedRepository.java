/**
 * 
 */
package com.demo.ticketservice.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.demo.ticketservice.domain.SeatReserved;

/**
 * @author ssrinivasulu
 *
 */
@RestResource (path = "seatsReserved", rel = "seatsReserved")
public interface SeatReservedRepository extends PagingAndSortingRepository<SeatReserved, Long> {
	Page<SeatReserved> findById(Long id, Pageable pageable);

	List<SeatReserved> findById(Long id);
	
	Page<SeatReserved> findByEventReservationId(Long id, Pageable pageable);
	
	List<SeatReserved> findByEventReservationId(Long id);
}
