/**
 * 
 */
package com.demo.ticketservice.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * @author ssrinivasulu
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,isGetterVisibility = JsonAutoDetect.Visibility.ANY,getterVisibility = JsonAutoDetect.Visibility.ANY)
@Entity (name = "Customer")
@Table (name = "customer")
public class Customer implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue //(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@Column (name = "firstname", nullable = false)
	private String firstName;
	@Column (name = "lastname", nullable = false)
	private String lastName;
	@Column (name = "middlename")
	private String middleName;
	@Column (name = "customeremail", nullable = false)
	private String customerEmail;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	
	public String getCustomerEmail() {
		return customerEmail;
	}
	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}
	@Override
	public int hashCode() {
		return new HashCodeBuilder(55, 113).append(this.id).append(this.firstName).append(this.lastName).toHashCode();
	}

	@Override
	public boolean equals(Object o) {
		Customer other = (Customer) o;
		return new EqualsBuilder()
				         .append(other.firstName, this.firstName)
				         .append(other.lastName, this.lastName)
				         .append(other.id, this.id)
				         .isEquals();
	}
	
}
