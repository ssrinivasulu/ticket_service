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

/**
 * @author ssrinivasulu
 *
 */
@RestResource (path = "customers", rel = "customers")
public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {
	Page<Customer> findById(Long id, Pageable pageable);

	List<Customer> findById(Long id);
	
	Customer findByCustomerEmail(String email);
	
}
