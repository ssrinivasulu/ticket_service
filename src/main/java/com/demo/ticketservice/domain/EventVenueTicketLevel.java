/**
 * 
 */
package com.demo.ticketservice.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
//@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,isGetterVisibility = JsonAutoDetect.Visibility.ANY,getterVisibility = JsonAutoDetect.Visibility.ANY)
@Entity (name = "EventVenueTicketLevel")
@Table (name = "event_venue_ticket_level")
//@SequenceGenerator(name = "event_venue_ticket_level_seq", sequenceName = "event_venue_ticket_level_seq")
public class EventVenueTicketLevel implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue //(strategy = GenerationType.SEQUENCE)
	private Long id;
	 // The eventVenue with which this eventVenueTicketLevel is associated.
    @ManyToOne
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "event_venue_id", referencedColumnName = "id")
    private EventVenue eventVenue;
	
	@Column (name = "level_name", nullable = false)
	private String levelName;
	
	@Column (name = "level_description")
	private String levelDescription;
	
    @Column(name = "level_price", nullable = false)
    @NotNull
    private Double levelPrice;
    
    @Column(name = "level_rows", nullable = false)
    @NotNull
    private Integer levelRows;
    
    @Column(name = "level_numofseatsinrow", nullable = false)
    @NotNull
    private Integer levelNumOfSeatsInRow;
    
    @OneToMany (cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "eventVenueTicketLevel")
   	private Set<Seat> seats ;
       
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EventVenue getEventVenue() {
		return eventVenue;
	}

	public void setEventVenue(EventVenue eventVenue) {
		this.eventVenue = eventVenue;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getLevelDescription() {
		return levelDescription;
	}

	public void setLevelDescription(String levelDescription) {
		this.levelDescription = levelDescription;
	}

	public Double getLevelPrice() {
		return levelPrice;
	}

	public void setLevelPrice(Double levelPrice) {
		this.levelPrice = levelPrice;
	}

	public Integer getLevelRows() {
		return levelRows;
	}

	public void setLevelRows(Integer levelRows) {
		this.levelRows = levelRows;
	}

	public Integer getLevelNumOfSeatsInRow() {
		return levelNumOfSeatsInRow;
	}

	public void setLevelNumOfSeatsInRow(Integer levelNumOfSeatsInRow) {
		this.levelNumOfSeatsInRow = levelNumOfSeatsInRow;
	}

	public Set<Seat> getSeats() {
		return seats;
	}

	public void setSeats(Set<Seat> seats) {
		this.seats = seats;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(55, 113).append(this.id).append(this.levelName).toHashCode();
	}

	@Override
	public boolean equals(Object o) {
		EventVenueTicketLevel other = (EventVenueTicketLevel) o;
		return new EqualsBuilder()
				         .append(other.levelName, this.levelName)
				         .append(other.id, this.id)
				         .isEquals();
	}
	
}
