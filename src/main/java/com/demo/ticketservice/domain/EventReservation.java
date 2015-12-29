/**
 * 
 */
package com.demo.ticketservice.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * @author ssrinivasulu
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,isGetterVisibility = JsonAutoDetect.Visibility.ANY,getterVisibility = JsonAutoDetect.Visibility.ANY)
@Entity (name = "EventReservation")
@Table (name = "event_reservation")
//@SequenceGenerator(name = "event_reservation_seq", sequenceName = "event_reservation_seq")
public class EventReservation implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue //(strategy = GenerationType.SEQUENCE)
	@Column (name = "id", unique = true, nullable = false )
	private Long id;
	
	 // The customer with which this EventReservation is associated.
    @ManyToOne
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "customer_id", nullable = false, referencedColumnName = "id")
    @NotNull
    private Customer customer;
    
    // The eventVenueTicketLevel with which this EventReservation is associated.
    @ManyToOne
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "event_venue_ticket_level_id", nullable = false, referencedColumnName = "id")
    @NotNull
    private EventVenueTicketLevel eventVenueTicketLevel;
    
    @OneToMany (cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "eventReservation")
	private Set<SeatReserved> seatReserveds = new HashSet<SeatReserved>();
    
	@Column(name = "purchase_confirmation_id")
    private String purchaseConfirmationId;
	
	@Column(name = "number_of_seats")
    private Integer numberOfSeats;
	
	@Column(name = "cost")
    private Double cost;
	
	@Column(name = "customer_payment_method")
    private String customerPaymentMethod;
	
	@Column(name = "paid")
    private Boolean paid;
	
	@Column(name = "active",nullable = false)
	@NotNull
    private Boolean active;
	
	@Enumerated(EnumType.STRING)
	@NotNull
    private ReservationStatusCode reservationStatus;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EventVenueTicketLevel getEventVenueTicketLevel() {
		return eventVenueTicketLevel;
	}

	public void setEventVenueTicketLevel(EventVenueTicketLevel eventVenueTicketLevel) {
		this.eventVenueTicketLevel = eventVenueTicketLevel;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getPurchaseConfirmationId() {
		return purchaseConfirmationId;
	}

	public void setPurchaseConfirmationId(String purchaseConfirmationId) {
		this.purchaseConfirmationId = purchaseConfirmationId;
	}


	public Integer getNumberOfSeats() {
		return numberOfSeats;
	}

	public void setNumberOfSeats(Integer numberOfSeats) {
		this.numberOfSeats = numberOfSeats;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public String getCustomerPaymentMethod() {
		return customerPaymentMethod;
	}

	public void setCustomerPaymentMethod(String customerPaymentMethod) {
		this.customerPaymentMethod = customerPaymentMethod;
	}

	public Boolean getPaid() {
		return paid;
	}

	public void setPaid(Boolean paid) {
		this.paid = paid;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public ReservationStatusCode getReservationStatus() {
		return reservationStatus;
	}

	public void setReservationStatus(ReservationStatusCode reservationStatus) {
		this.reservationStatus = reservationStatus;
	}

	public Set<SeatReserved> getSeatReserveds() {
		return seatReserveds;
	}

	public void setSeatReserveds(Set<SeatReserved> seatReserveds) {
		this.seatReserveds = seatReserveds;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(55, 113).append(this.id).append(this.customer).append(this.eventVenueTicketLevel).toHashCode();
	}

	@Override
	public boolean equals(Object o) {
		EventReservation other = (EventReservation) o;
		return new EqualsBuilder()
				         .append(other.customer, this.customer)
				         .append(other.eventVenueTicketLevel, this.eventVenueTicketLevel)
				         .append(other.id, this.id)
				         .isEquals();
	}
	
}
