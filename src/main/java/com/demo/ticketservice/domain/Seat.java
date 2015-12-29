package com.demo.ticketservice.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Entity (name = "Seat")
@Table (name = "seat")
//@SequenceGenerator(name = "seat_seq", sequenceName = "seat_seq")
public class Seat implements Serializable {

	public Seat() {
	}

	public Seat(Long id, Integer rowNum, Integer seatNumber, EventVenueTicketLevel eventVenueTicketLevel) {
		this.id = id;
		this.rowNum = rowNum;
		this.seatNumber = seatNumber;
		this.eventVenueTicketLevel = eventVenueTicketLevel;
	}

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue //(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@Column(name = "row_num", nullable = false)
    @NotNull
    private Integer rowNum;
	
	@Column(name = "seat_number", nullable = false)
    @NotNull
    private Integer seatNumber;
	
	 // The eventVenueTicketLevel with which this seat is associated.
	@ManyToOne
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "event_venue_ticket_level_id", referencedColumnName = "id")
    private EventVenueTicketLevel eventVenueTicketLevel;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getRowNum() {
		return rowNum;
	}

	public void setRowNum(Integer rowNum) {
		this.rowNum = rowNum;
	}

	public Integer getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(Integer seatNumber) {
		this.seatNumber = seatNumber;
	}

	public EventVenueTicketLevel getEventVenueTicketLevel() {
		return eventVenueTicketLevel;
	}

	public void setEventVenueTicketLevel(EventVenueTicketLevel eventVenueTicketLevel) {
		this.eventVenueTicketLevel = eventVenueTicketLevel;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(55, 113).append(this.id).append(this.rowNum).append(this.seatNumber).append(this.eventVenueTicketLevel).toHashCode();
	}

	@Override
	public boolean equals(Object o) {
		Seat other = (Seat) o;
		return new EqualsBuilder()
				         .append(other.rowNum, this.rowNum)
				         .append(other.seatNumber, this.seatNumber)
				         .append(other.eventVenueTicketLevel, this.eventVenueTicketLevel)
				         .append(other.id, this.id)
				         .isEquals();
	}
	
}
