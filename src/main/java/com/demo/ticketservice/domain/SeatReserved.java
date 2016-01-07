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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
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
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
//@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,isGetterVisibility = JsonAutoDetect.Visibility.ANY,getterVisibility = JsonAutoDetect.Visibility.ANY)
@Entity (name = "SeatReserved")
@Table (name = "seat_reserved")
@SequenceGenerator(name = "seat_reserved_seq", sequenceName = "seat_reserved_seq")
public class SeatReserved implements Serializable {

	public SeatReserved() {
	}

	public SeatReserved(Long id, Seat seat, EventReservation eventReservation) {
		super();
		this.id = id;
		this.seat = seat;
		this.eventReservation = eventReservation;
	}

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue //(strategy = GenerationType.SEQUENCE)
	@Column (name = "id", unique = true, nullable = false )
	private Long id;
	
	 // The customer with which this EventReservation is associated.
    @ManyToOne
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "seat_id", nullable = false, referencedColumnName = "id")
    @NotNull
    private Seat seat;
    
    // The eventVenueTicketLevel with which this EventReservation is associated.
    @ManyToOne
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "event_reservation_id", referencedColumnName = "id")
    private EventReservation eventReservation;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Seat getSeat() {
		return seat;
	}

	public void setSeat(Seat seat) {
		this.seat = seat;
	}

	public EventReservation getEventReservation() {
		return eventReservation;
	}

	public void setEventReservation(EventReservation eventReservation) {
		this.eventReservation = eventReservation;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(55, 113).append(this.id).append(this.seat).append(this.eventReservation).toHashCode();
	}

	@Override
	public boolean equals(Object o) {
		SeatReserved other = (SeatReserved) o;
		return new EqualsBuilder()
				         .append(other.seat, this.seat)
				         .append(other.eventReservation, this.eventReservation)
				         .append(other.id, this.id)
				         .isEquals();
	}
	
}
